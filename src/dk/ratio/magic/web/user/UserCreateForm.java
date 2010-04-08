package dk.ratio.magic.web.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user/create")
public class UserCreateForm
{
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView defaultHandler()
    {
        return new ModelAndView("/user/create/form");
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView submitHandler()
    {
        return new ModelAndView("/user/create/form");
    }
}
