package dk.ratio.magic.web.users;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UsersController
{
    @RequestMapping("/users")
    public ModelAndView defaultHandler()
    {
        return new ModelAndView("/users/list");
    }
}
