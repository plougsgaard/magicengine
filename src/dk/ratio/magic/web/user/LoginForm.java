package dk.ratio.magic.web.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user/login")
public class LoginForm
{
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView defaultHandler()
    {
        return new ModelAndView("/user/login");
    }
}
