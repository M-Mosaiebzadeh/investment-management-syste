package ir.maktab.investment.controller;


import ir.maktab.investment.model.User;
import ir.maktab.investment.service.UserService;
import ir.maktab.investment.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static ir.maktab.investment.service.util.HomeUtil.*;


@Controller
public class homeController {
    private final UserService userService;
    private VerificationCodeService verificationCodeService;

    @Autowired
    public homeController(UserService userService,
                          VerificationCodeService verificationCodeService) {

        this.userService = userService;
        this.verificationCodeService = verificationCodeService;
    }


    @GetMapping("/")
    public String goHome(Model model) {

        if (isAnonymousUser())
            model.addAttribute("isAnonymous", true);
        else
            model.addAttribute("isAnonymous", false);

        return "/home/home-page";
    }

    @GetMapping("/home")
    public String personalHome() {
        return directLinkForRole();
    }


    @GetMapping("/login")
    public String goToLoginForm() {
        if (!isAnonymousUser()) {
            return "redirect:/home";
        }
        return "/home/login-form";
    }

    @GetMapping("/check-role")
    public String check() {
        return directLinkForRole();
    }

    @GetMapping("/register")
    public String goToUserRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("address", "/registration");
        return "/home/register-form";
    }

    @PostMapping("/registration")
    public String signUpForm(@Valid User user,
                             BindingResult result,
                             RedirectAttributes message,
                             HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {

        if (result.hasErrors())
            return "/home/register-form";

        userService.saveUserFirstTime(user);
        message.addFlashAttribute("success", String.format("%s, You have registered successfully.", user.getUsername()));
        message.addFlashAttribute("success1", "please check your email to verify your account.");

//        message.addAttribute("error","");

        String siteUrl = getSiteUrl(request);
        verificationCodeService.sendVerificationCodeForRegistration(user, siteUrl);
        return "redirect:/login";
    }

    @GetMapping("/registration/verify")
    public String verifyRegistration(@RequestParam("code") String verificationCode,
                                     RedirectAttributes message) {
        Map<Boolean, String> verificationResult = userService.verifyRegistrationAccount(verificationCode);
        if (verificationResult.containsKey(false)) {
            message.addFlashAttribute("error", verificationResult.get(false));
        } else {
            message.addFlashAttribute("success1", verificationResult.get(true));
        }
        return "redirect:/login";
    }



    @GetMapping("/login-error")
    public String login(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }

        }

        model.addAttribute("error", errorMessage);
        return "/home/login-form";
    }


    @GetMapping("/forget-password/change-password")
    public String forgetPassword() {
        if (!isAnonymousUser()) {
            return "redirect:/home";
        }

        return "/password/forget-password-page";
    }

//    TODO make 3 option for forget password and in html
    @GetMapping("/forget-password/recovery-password")
    public String recoveryPassword(@RequestParam("recovery-type") String fieldType,
                                   @RequestParam("recovery-value") String fieldValue,
                                   RedirectAttributes redirectAttributes,
                                   HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        if (fieldType.equals("by")) {
            redirectAttributes.addFlashAttribute("error", "please select one recovery way!!");
            return "redirect:/forget-password/change-password";
        }

        User user = userService.checkUserExistForForgetPassword(fieldType,fieldValue);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Sorry, we cant find any user with this %s",fieldType));
            return "redirect:/forget-password/change-password";
        }


        String siteUrl = getSiteUrl(request);
        userService.verifyAccountForForgetPassword(user);
        verificationCodeService.sendVerificationCodeForForgetPassword(user, siteUrl);
        redirectAttributes.addFlashAttribute("success", "check your email to change password");
        return "redirect:/login";
    }

    @GetMapping("/forget-password/verify")
    public String verifyForgetPassword(@RequestParam("code") String verificationCode,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {
        User user = userService.loadUserByVerificationCode(verificationCode);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "invalid code, try later...");
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "/password/forget-password-form";
    }


    @PostMapping("/forget-password/result")
    public String resultOfForgetPassword(@RequestParam("password") String password,
                                         @RequestParam("user-id") Long id,
                                         RedirectAttributes redirectAttributes) {
        User user = userService.loadById(id);
        userService.changePassword(user,password);

        redirectAttributes.addFlashAttribute("success1","password change, login with new password...");
        return "redirect:/login";
    }

    @GetMapping("/about-us")
    public String aboutUs() {
        return "/home/about-us";
    }

    @GetMapping("/education")
    public String education() {
        return "/home/education";
    }

    @GetMapping("/contact-us")
    public String contactUs() {
        return "/home/contact-us";
    }
}
