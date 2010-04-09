package dk.ratio.magic.web.user;

import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.domain.web.user.Credentials;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.services.user.UserManager;
import dk.ratio.magic.util.web.Views;
import dk.ratio.magic.validation.user.UserLoginValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user/login")
public class LoginForm
{
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserDao userDao;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView defaultHandler(HttpServletRequest request)
    {
        if (userManager.getSessionUser(request) != null) {
            return Views.redirect(request, "/user/home");
        }
        return new ModelAndView("/user/login");
    }

    /**
     * Handles POST requests to the login form.
     *
     * Redirects to
     *
     * @param request       -
     * @param response      -
     * @param credentials   the credentials (email, password) that was entered
     *                      by the user; automatically inferred by Spring MVC
     *                      (with the restriction that input names should match)
     * @param bindingResult the result of the implicit binding between the request
     *                      and the java object credentials (in this case)
     *                      contains potential errors and such
     *
     * @return redirection view to user's homepage if all went well, otherwise
     *         we redirect (to avoid serving a site on a post request) to the
     *         login page.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView submitHandler(HttpServletRequest request, HttpServletResponse response,
                                      Credentials credentials, BindingResult bindingResult)
    {
        // implicit authentication
        new UserLoginValidator().validate(userManager, userDao, credentials, bindingResult);
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("/user/login");
            mv.addObject("bindingResult", bindingResult);
            return mv;
        }

        // set session
        User user = userManager.createSessionUser(request, response, credentials);

        String urlAfterSuccess = (String) request.getSession().getAttribute("urlAfterSuccess");
        if (StringUtils.isBlank(urlAfterSuccess)) {
            // if a forward url was set not set we default to /user/home
            return Views.redirect(request, "/user/" + user.getId());
        }
        return new ModelAndView(new RedirectView(urlAfterSuccess));
    }
}
