package dk.ratio.magic.web.user;

import dk.ratio.magic.repository.user.UserDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController
{
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private UserDao userDao;

    @RequestMapping("/user/home")
    public ModelAndView homeHandler()
    {
        return new ModelAndView("/user/home");
    }

    @RequestMapping("/user/{userId}")
    public ModelAndView userHandler(@PathVariable("userId") Integer userId)
    {
        ModelAndView mv = new ModelAndView("/user/show");
        mv.addObject("user", userDao.getUser(userId));
        return mv;
    }
}