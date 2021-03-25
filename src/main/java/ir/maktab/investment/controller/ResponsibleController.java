package ir.maktab.investment.controller;


import ir.maktab.investment.model.Request;
import ir.maktab.investment.model.Response;
import ir.maktab.investment.model.User;
import ir.maktab.investment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequestMapping("/responsible")
public class ResponsibleController {
    private UserService userService;

    @Autowired
    public ResponsibleController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String responsibleHomePage(Model model, Authentication authentication) {
        String username = authentication.getName();
        model.addAttribute("username",username);
        return "/responsible/responsible-home";
    }

    @GetMapping("/information")
    public String information(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = (User) userService.loadUserByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("address","/responsible/edit-information");
        return "/information/information";
    }

    @PostMapping("/edit-information")
    public String editInformation(@Valid User responsible,
                                  BindingResult result,
                                  Authentication authentication) {

        if (result.hasErrors()){
            //TODO send responsible message and redirect to correct page
            return "redirect:/responsible/home";
        }

        User newResponsible = userService.editResponsible(responsible,authentication);
        userService.saveResponsible(newResponsible);
        return "redirect:/responsible/home";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {
        model.addAttribute("address","/responsible/verify-change-password");
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
            return "redirect:/responsible/change-password";
        }
        userService.changePassword(user,newPassword);
        redirectAttributes.addFlashAttribute("success","Congratulation, The new password changed with success");
        return "redirect:/responsible/home";
    }

}
