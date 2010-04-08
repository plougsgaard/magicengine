package dk.ratio.magic.web.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user/{userId}/edit")
public class UserEditForm
{
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView defaultHandler(@PathVariable("userId") Integer userId)
    {
        return new ModelAndView("/user/edit/form");
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView submitHandler(@PathVariable("userId") Integer userId)
    {
        return new ModelAndView("/user/edit/form");
    }
}
