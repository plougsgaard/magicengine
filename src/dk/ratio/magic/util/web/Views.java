package dk.ratio.magic.util.web;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

public class Views
{
    public static ModelAndView redirect(HttpServletRequest request, String path) {
        return new ModelAndView(new RedirectView(request.getContextPath() + path));
    }

    public static ModelAndView loginRedirect(HttpServletRequest request) {
        return new ModelAndView(new RedirectView(request.getContextPath() + "/user/login"));
    }
}
