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
        request.getSession().setAttribute("urlAfterSuccess", request.getRequestURI());
        return new ModelAndView(new RedirectView(request.getContextPath() + "/user/login"));
    }

    public static ModelAndView disallow(String message) {
        ModelAndView errorModel = new ModelAndView("/error/disallow");
        errorModel.addObject("message", message);
        return errorModel;
    }
}
