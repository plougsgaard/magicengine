package dk.ratio.magic.web.user;

import dk.ratio.magic.domain.web.user.ProfileEdit;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.security.web.Policy;
import dk.ratio.magic.security.web.RestrictAccess;
import dk.ratio.magic.services.user.UserManager;
import dk.ratio.magic.util.web.Views;
import dk.ratio.magic.validation.user.UserEditValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user/{userId}/edit")
public class UserEditForm
{
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserManager userManager;

    @RestrictAccess(Policy.PRIVATE)
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView defaultHandler(HttpServletRequest request, @PathVariable("userId") Integer userId)
    {
        ModelAndView mv = new ModelAndView("/user/edit/form");
        mv.addObject("profileEdit", new ProfileEdit(userDao.get(userId)));
        return mv;
    }

    @RestrictAccess(Policy.PRIVATE)
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView submitHandler(HttpServletRequest request, @PathVariable("userId") Integer userId,
                                      ProfileEdit profileEdit, BindingResult bindingResult,
                                      HttpServletResponse response)
    {
        ModelAndView mv = new ModelAndView("/user/edit/form");

        new UserEditValidator().validate(userManager, userDao, profileEdit, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.warn("Something went wrong with user details validation. " +
                        "[bindingResult :"+ bindingResult + "] " +
                        "[profileEdit :"+ profileEdit + "] " +
                        "");
            mv.addObject("bindingResult", bindingResult);
            mv.addObject("profileEdit", profileEdit);
            return mv;
        }

        // update user and refresh session
        logger.info("Profile was edited. " +
                    "[profileEdit: " + profileEdit + "]");
        userDao.update(profileEdit);
        userManager.destroySessionUser(request, response);
        userManager.createSessionUser(request, response, userDao.get(userId));
        
        return mv;
    }

    public final static String RESTRICT_ACCESS_PRIVATE =
            "You tried to edit a profile that is not yours!<br /><br /> " +
            "Only the owner of the profile should edit it.<br /><br /> :)";
}
