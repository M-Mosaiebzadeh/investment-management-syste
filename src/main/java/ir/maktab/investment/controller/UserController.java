package ir.maktab.investment.controller;


import ir.maktab.investment.model.Request;
import ir.maktab.investment.model.RequestSubject;
import ir.maktab.investment.model.User;
import ir.maktab.investment.model.enums.RequestStatus;
import ir.maktab.investment.service.RequestSubjectService;
import ir.maktab.investment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String userHomePage(Model model, Authentication authentication) {
        String username = authentication.getName();
        model.addAttribute("username",username);
        return "/user/user-home";
    }


    @GetMapping("/information")
    public String information(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = (User) userService.loadUserByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("address","/user/edit-information");
        return "/information/information";
    }


    @PostMapping("/edit-information")
    public String editInformation(@Valid User user,
                                  BindingResult result,
                                  Authentication authentication) {

        if (result.hasErrors()){
            //TODO send responsible message and redirect to correct page
            return "redirect:/user/home";
        }

        User newUser = userService.editUser(user,authentication);
        userService.saveUser(newUser);
        return "redirect:/user/home";
    }


    @GetMapping("/change-password")
    public String changePassword(Model model) {
        model.addAttribute("address","/user/verify-change-password");
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
            return "redirect:/user/change-password";
        }
        userService.changePassword(user,newPassword);
        redirectAttributes.addFlashAttribute("success","Congratulation, The new password changed with success");
        return "redirect:/user/home";
    }
}
