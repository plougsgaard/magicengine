package dk.ratio.magic.web.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController
{
    @RequestMapping("/errors/default")
    public ModelAndView defaultHandler()
    {
        return new ModelAndView("/error/default");
    }

    @RequestMapping("/errors/db")
    public ModelAndView dbHandler()
    {
        return new ModelAndView("/error/db");
    }

    @RequestMapping("/errors/404")
    public ModelAndView _404Handler()
    {
        return new ModelAndView("/error/404");
    }
}
