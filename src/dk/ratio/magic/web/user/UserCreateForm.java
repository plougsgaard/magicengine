package dk.ratio.magic.web.user;

import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.services.user.UserManager;
import dk.ratio.magic.util.web.Views;
import dk.ratio.magic.validation.user.RegisterUserValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user/create")
public class UserCreateForm
{
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserDao userDao;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView defaultHandler()
    {
        ModelAndView mv = new ModelAndView("/user/create/form");
        mv.addObject("user", new User());
        return mv;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView submitHandler(User user, BindingResult bindingResult,
                                      HttpServletRequest request)
    {
        new RegisterUserValidator().validate(userManager, userDao, user, bindingResult);
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("/user/create/form");
            logger.warn("Error binding the result. Number of errors: " + bindingResult.getErrorCount());
            for (FieldError error : bindingResult.getFieldErrors()) {
                logger.warn(error.getField() +  ": " +error.getDefaultMessage());
            }
            mv.addObject("bindingResult", bindingResult);
            mv.addObject("user", user);
            return mv;
        }

        user = userDao.create(user);
        return Views.redirect(request, "/user/" + user.getId() + "?new");
    }
}
