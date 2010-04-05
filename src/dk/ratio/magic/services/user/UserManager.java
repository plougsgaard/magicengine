package dk.ratio.magic.services.user;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.domain.web.user.Credentials;
import dk.ratio.magic.repository.user.UserDao;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class UserManager
{
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private UserDao userDao;

    public static final String USER_SESSION_NAME = "user_session";
    public static final String USER_COOKIE_SECRET = "user_cookie_secret";
    public static final String USER_COOKIE_ID = "user_cookie_id";

    final int DAYS = 24 * 60 * 60;

    public void createSessionUser(HttpServletRequest request, HttpServletResponse response, User user)
    {
        request.getSession().setAttribute(USER_SESSION_NAME, user);
    }

    public void createSessionUser(HttpServletRequest request, HttpServletResponse response, Credentials credentials)
    {
        User user = userDao.getUser(credentials.getEmail());

        request.getSession().setAttribute(USER_SESSION_NAME, user);

        if (credentials.isRemember()) {
            String cookieValue = makeCookieSecret(user);

            Cookie idCookie = new Cookie(USER_COOKIE_ID, String.valueOf(user.getId()));
            idCookie.setPath("/");
            idCookie.setMaxAge(30 * DAYS);

            Cookie secretCookie = new Cookie(USER_COOKIE_SECRET, cookieValue);
            secretCookie.setPath("/");
            secretCookie.setMaxAge(30 * DAYS);

            response.addCookie(idCookie);
            response.addCookie(secretCookie);
        }
    }

    public User getSessionUser(HttpServletRequest request)
    {
        return (User) request.getSession().getAttribute(USER_SESSION_NAME);
    }

    public void destroySessionUser(HttpServletRequest request, HttpServletResponse response)
    {
        // Destroy session
        request.getSession().removeAttribute(USER_SESSION_NAME);

        //Destroy cookies
        Cookie idCookie = new Cookie(USER_COOKIE_ID, "");
        idCookie.setPath("/");
        idCookie.setMaxAge(0);

        Cookie secretCookie = new Cookie(USER_COOKIE_SECRET, "");
        secretCookie.setPath("/");
        secretCookie.setMaxAge(0);

        response.addCookie(idCookie);
        response.addCookie(secretCookie);
    }

    public boolean isAuthentic(Credentials credentials)
    {
        User user = userDao.getUser(credentials.getEmail());

        if (user == null) {
            return false;
        }

        String encryptedPassword = userDao.SHA1(credentials.getPassword() + user.getPasswordSalt());

        logger.info(encryptedPassword);

        return encryptedPassword.equals(user.getPassword());
    }

    public boolean isAuthentic(int id, String password)
    {
        User user = userDao.getUser(id);

        if (user == null) {
            return false;
        }

        String encryptedPassword = userDao.SHA1(password + user.getPasswordSalt());

        logger.info(encryptedPassword);

        return encryptedPassword.equals(user.getPassword());
    }

    public boolean isLoggedIn(HttpServletRequest request)
    {
        return getSessionUser(request) != null;
    }

    public String makeCookieSecret(User user)
    {
        return userDao.SHA1(user.getPassword() + "secret" + user.getPasswordSalt());
    }
}
