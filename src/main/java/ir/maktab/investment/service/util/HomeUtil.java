package ir.maktab.investment.service.util;

import ir.maktab.investment.model.User;
import ir.maktab.investment.model.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeUtil {

    public static String directLinkForRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()){

            Object principal = authentication.getPrincipal();

            if (principal.equals("anonymousUser"))
                return "redirect:/";

            User user = (User) principal;
            if (user.getRole().equals(Role.ADMIN))
                return "redirect:/admin/home";
            else if (user.getRole().equals(Role.RESPONSIBLE))
                return "redirect:/responsible/home";
            else if (user.getRole().equals(Role.USER))
                return "redirect:/user/home";
        }
        return "redirect:/";
    }

    public static Boolean isAnonymousUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser"))
            return true;
        return false;
    }

    public static String getSiteUrl(HttpServletRequest request) {
        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(),"");
    }
}
