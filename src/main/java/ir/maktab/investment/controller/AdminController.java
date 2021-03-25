package ir.maktab.investment.controller;

import ir.maktab.investment.model.Request;
import ir.maktab.investment.model.RequestSubject;
import ir.maktab.investment.model.Response;
import ir.maktab.investment.model.User;
import ir.maktab.investment.model.enums.Role;
import ir.maktab.investment.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private RequestSubjectService requestSubjectService;
    private VerificationCodeService verificationCodeService;
    private RequestService requestService;

    @Autowired
    public AdminController(UserService userService,
                           RequestSubjectService requestSubjectService,
                           VerificationCodeService verificationCodeService,
                           RequestService requestService) {
        this.userService = userService;
        this.requestSubjectService = requestSubjectService;
        this.verificationCodeService = verificationCodeService;
        this.requestService = requestService;
    }

    @GetMapping("/home")
    public String homePage(Model model, Authentication authentication) {
        String username = authentication.getName();
        model.addAttribute("username",username);
        return "/admin/admin-home";
    }

    @GetMapping("/information")
    public String information(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = (User) userService.loadUserByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("address", "/admin/edit-information");
        return "/information/information";
    }

    @PostMapping("/edit-information")
    public String editInformation(@Valid User admin,
                                  BindingResult result,
                                  Authentication authentication) {

        if (result.hasErrors()) {
            //TODO send responsible message and redirect to correct page
            return "redirect:/admin/home";
        }

        User newAdmin = userService.editAdmin(admin, authentication);
        userService.saveAdmin(newAdmin);
        return "redirect:/admin/home";
    }


    @GetMapping("/show/admin-list")
    public String showAdmins(Model model) {
        List<User> users = userService.loadAllUserWithRole(Role.ADMIN);
        model.addAttribute("users", users);
        return "/admin/show-list/show-admin-list";
    }

    @GetMapping("/show/responsible-list")
    public String showResponsibles(Model model) {
        List<User> users = userService.loadAllUserWithRole(Role.RESPONSIBLE);
        model.addAttribute("users", users);
        return "/admin/show-list/show-responsible-list";
    }


    @GetMapping("/show/responsible-list/details/{responsible-id}")
    public String showResponsibleDetailsWithTableOfResponse(@PathVariable("responsible-id") Long responsibleId,
                                                            Model model) {
        User user = userService.loadById(responsibleId);
        model.addAttribute("user",user);
        return "/admin/show-responsible/responsible-information-and-response";

    }

    @GetMapping("/show/responsible-list/details/response/{response-id}")
    public String showResponsibleDetails(@PathVariable("response-id") Long responseId,
                                         Model model) throws Exception {

        Request request = requestService.findRequestByResponseId(responseId);
        model.addAttribute("request",request);
        return "/admin/show-responsible/responsible-response";
    }

    @GetMapping("/show/user-list")
    public String showUsers(Model model) {
        List<User> users = userService.loadAllUserWithRole(Role.USER);
        model.addAttribute("users", users);
        return "/admin/show-list/show-user-list";
    }


    @GetMapping("/show/user-list/details/{user-id}")
    public String showUserDetailsWithTableOfRequest(@PathVariable("user-id") Long userId,
                                                            Model model) {
        User user = userService.loadById(userId);
        model.addAttribute("user",user);
        return "/admin/show-user/user-information-and-request";

    }

    @GetMapping("/show/user-list/details/request/{request-id}")
    public String showUserDetails(@PathVariable("request-id") Long requestId,
                                         Model model) throws Exception {

        Request request = requestService.findById(requestId);
        model.addAttribute("request",request);
        return "/admin/show-user/user-request";
    }



    @GetMapping("/show/user-list/edit/{id}")
    public String editUserDetailsWithId(@PathVariable("id") Long id, Model model) {
        User user = userService.loadById(id);

        if (user.getRole().equals(Role.USER))
            model.addAttribute("address","/admin/show/user-list");
        else if (user.getRole().equals(Role.RESPONSIBLE))
            model.addAttribute("address","/admin/show/responsible-list");
        else if (user.getRole().equals(Role.ADMIN))
            model.addAttribute("address","/admin/show/admin-list");

        model.addAttribute("user", user);
        return "/information/information";
    }

    @PostMapping("/show/user-list")
    public String redirectToUserListWithMethodGet(@Valid User user,
                                                  BindingResult result) {
        if (result.hasErrors()){
            //TODO send responsible message and redirect to correct page
            return "redirect:/admin/home";
        }

        // TODO handle in Thymeleaf
        user.setRole(Role.USER);
        userService.saveUser(user);
        return "redirect:/admin/show/user-list/details/"+user.getId();
    }

    @PostMapping("/show/responsible-list")
    public String redirectToResponsibleListWithMethodGet(@Valid User responsible,
                                                         BindingResult result) {

            if (result.hasErrors()){
                //TODO send responsible message and redirect to correct page
                return "redirect:/admin/home";
            }

        // TODO handle in Thymeleaf
        responsible.setRole(Role.RESPONSIBLE);
        userService.saveResponsible(responsible);
        return "redirect:/admin/show/responsible-list/details/"+responsible.getId();
    }

    @PostMapping("/show/admin-list")
    public String redirectToAdminListWithMethodGet() {
        return "redirect:/admin/show/admin-list";
    }


    @GetMapping("/show/user-list/is-enable/{id}")
    public String enableAndDisableUserDetailsWithId(@PathVariable("id") Long id) {
        User user = userService.loadById(id);
        user.setIsEnabled(!user.isEnabled());
        userService.saveUser(user);

        if (user.getRole().equals(Role.RESPONSIBLE))
            return "redirect:/admin/show/responsible-list/details/" + id;
        else if (user.getRole().equals(Role.USER))
            return "redirect:/admin/show/user-list/details/"+id;
        else
            return "redirect:/admin/home";
    }

    @GetMapping("/show/user-list/is-deleted/{user-id}")
    public String deleteAndUndeleteUserWithId(@PathVariable("user-id") Long id) {
        User user = userService.loadById(id);
        user.setIsDeleted(!user.getIsDeleted());
        userService.saveUser(user);

        if (user.getRole().equals(Role.RESPONSIBLE))
            return "redirect:/admin/show/responsible-list/details/"+id;
        else if (user.getRole().equals(Role.USER))
            return "redirect:/admin/show/user-list/details/"+id;
        else
            return "redirect:/admin/home";
    }

    @GetMapping("/subject")
    public String subject(Model model) {
        List<RequestSubject> subjects = requestSubjectService.findAll();
        model.addAttribute("subjects",subjects);
        return "/admin/subject/show-all-subject";
    }

    @GetMapping("/new-subject")
    public String newSubject(@ModelAttribute RequestSubject subject) {
        requestSubjectService.save(subject);
        return "redirect:/admin/subject";
    }

    @PostMapping("/edit-subject")
    public String editSubject(@ModelAttribute RequestSubject subject) {
        requestSubjectService.save(subject);
        return "redirect:/admin/subject";
    }

    @PostMapping("/delete-subject")
    public String deleteSubject(@ModelAttribute RequestSubject subject) {
       subject.setIsDeleted(!subject.getIsDeleted());
       requestSubjectService.save(subject);
        return "redirect:/admin/subject";
    }

    @GetMapping("/create-responsible")
    public String createResponsible(Model model) {
        model.addAttribute("user",new User());
        model.addAttribute("address","/admin/responsible-registration");
        return "/home/register-form";
    }

    @PostMapping("/responsible-registration")
    public String sendEmailToResponsible(@Valid User user,
                                         BindingResult result,
                                         RedirectAttributes message)
            throws UnsupportedEncodingException, MessagingException {

        if (result.hasErrors())
            return "/home/register-form";

        String password = userService.saveResponsibleFirstTime(user);
        verificationCodeService.sendUsernameAndPasswordForCreateResponsible(user,password);

        message.addFlashAttribute("success",String.format("Responsible[%s], registered successfully.",user.getUsername()));

        return "redirect:/admin/home";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {
        model.addAttribute("address","/admin/verify-change-password");
        return "/password/change-password-form";
    }

    @PostMapping("/verify-change-password")
    public String verifyChangePassword(@RequestParam("current-password") String oldPassword,
                                       @RequestParam("password") String newPassword,
                                       Authentication authentication,
                                       RedirectAttributes redirectAttributes) {
        User user = (User) userService.loadUserByUsername(authentication.getName());
        Boolean isEqualOldPasswords = userService.isPasswordsEqual(user,oldPassword);
        if (!isEqualOldPasswords){
            redirectAttributes.addFlashAttribute("error","Sorry, Current password is incorrect!!");
            return "redirect:/admin/change-password";
        }
        userService.changePassword(user,newPassword);
        redirectAttributes.addFlashAttribute("success","Congratulation, The new password changed with success");
        return "redirect:/admin/home";
    }

    @GetMapping("/change-password/responsible-user")
    public String changeResponsiblePassword(@RequestParam("user-id") Long userId,
                                            Model model) {

        model.addAttribute("address","/admin/verify-change-password/responsible-user");
        model.addAttribute("userId",userId);
        return "/password/change-password-form";
    }

    @PostMapping("/verify-change-password/responsible-user")
    public String verifyChangePasswordForUserOrResponsible(@RequestParam("user-id") Long userId,
                                                           @RequestParam("password") String newPassword,
                                                           RedirectAttributes redirectAttributes) {

        User user = userService.loadById(userId);
        userService.changePassword(user,newPassword);
        redirectAttributes.addFlashAttribute("success",String.format("The %s, new password changed with success",user.getUsername()));
        return "redirect:/admin/home";
    }
}
