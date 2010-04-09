package dk.ratio.magic.web.user;

import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.services.user.UserManager;
import dk.ratio.magic.util.web.Views;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController
{
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserManager userManager;

    @RequestMapping("/user/home")
    public ModelAndView homeHandler()
    {
        return new ModelAndView("/user/home");
    }

    @RequestMapping("/user/{userId}")
    public ModelAndView userHandler(@PathVariable("userId") Integer userId)
    {
        ModelAndView mv = new ModelAndView("/user/show");
        mv.addObject("user", userDao.get(userId));
        return mv;
    }

    @RequestMapping("/user/logout")
    public ModelAndView logoutHandler(HttpServletRequest request, HttpServletResponse response)
    {
        userManager.destroySessionUser(request, response);
        return Views.redirect(request, "/");
    }
}
