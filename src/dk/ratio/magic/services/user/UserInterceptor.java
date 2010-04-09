package dk.ratio.magic.services.user;

import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.repository.user.UserDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor extends HandlerInterceptorAdapter
{
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserDao userDao;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception
    {
        // If a user session exists we need not do anything
        if (request.getAttribute(UserManager.USER_SESSION_NAME) != null) {
            return super.preHandle(request, response, handler);
        }

        // If no cookies on this domain are present, let's just get on with it
        if (request.getCookies() == null) {
            return super.preHandle(request, response, handler);
        }

        Integer cookieId = null;
        String cookieSecret = null;

        for (Cookie cookie : request.getCookies()) {
            if (UserManager.USER_COOKIE_SECRET.equals(cookie.getName())) {
                cookieSecret = cookie.getValue();
            }
            if (UserManager.USER_COOKIE_ID.equals(cookie.getName())) {
                try {
                    cookieId = Integer.parseInt(cookie.getValue());
                } catch (NumberFormatException ignored) {}
            }
        }

        // Decide whether or not to create a session based on the cookies
        if (cookieId != null && cookieSecret != null) {
            User user = userDao.get(cookieId);
            if (userManager.makeCookieSecret(user).equals(cookieSecret)) {
                userManager.createSessionUser(request, response, user);
            }
        }

        return super.preHandle(request, response, handler);
    }
}
