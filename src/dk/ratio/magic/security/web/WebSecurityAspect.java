package dk.ratio.magic.security.web;

import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.repository.deck.DeckDao;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.services.user.UserManager;
import dk.ratio.magic.util.web.Views;
import dk.ratio.magic.util.web._404Exception;
import dk.ratio.magic.web.error.ErrorController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Service
public class WebSecurityAspect
{
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private UserManager userManager;

    @Autowired
    private DeckDao deckDao;

    @Autowired
    private UserDao userDao;

    /**
     * A point-cut that matches a typical @Controller method that returns a ModelAndView
     * object and is defined in dk.ratio.magic.web (or a sub-package).
     */
    @Pointcut("execution(public org.springframework.web.servlet.ModelAndView " +
              "dk.ratio.magic.web..*.*(..))")
    public void webModelAndViewCut() {}

    @Pointcut("webModelAndViewCut() && within(dk.ratio.magic.web.deck..*)")
    public void inDeck() {}

    @Pointcut("webModelAndViewCut() && within(dk.ratio.magic.web.user..*)")
    public void inUser() {}

    @Around("webModelAndViewCut() && @annotation(restrictAccess)")
    public Object enforceArgumentOrder(ProceedingJoinPoint joinPoint,
                                       RestrictAccess restrictAccess) throws Throwable
    {
        // 1st argument: HttpServletRequest
        Object[] arguments = joinPoint.getArgs();
        if (!(arguments[0] instanceof HttpServletRequest)) {
            throw new IllegalStateException("Wrong usage of @RestrictAccess. " +
                                            "HttpServletRequest must be the 1st argument.");
        }
        // 2nd argument: Integer
        if (restrictAccess.value() == Policy.PRIVATE && !(arguments[1] instanceof Integer)) {
            throw new IllegalStateException("Wrong usage of @RestrictAccess. " +
                                            "Integer must be the 2nd argument.");
        }
        return joinPoint.proceed();
    }

    @Around("webModelAndViewCut() && @annotation(restrictAccess) && args(request,..)")
    public Object applyDefaultRestrictAccess(ProceedingJoinPoint joinPoint,
                                             RestrictAccess restrictAccess,
                                             HttpServletRequest request) throws Throwable
    {
        if (!userManager.isLoggedIn(request)) {
            return Views.loginRedirect(request);
        }
        return joinPoint.proceed();
    }

    @Around("inDeck() && args(request,deckId,..)")
    public Object applyCheckDeckResource(ProceedingJoinPoint joinPoint,
                                         HttpServletRequest request,
                                         Integer deckId) throws Throwable
    {
        final Deck deck = deckDao.get(deckId);
        if (deck == null) {
            throw new _404Exception();
        }
        return joinPoint.proceed();
    }

    @Around("inDeck() && @annotation(restrictAccess) && args(request,deckId,..)")
    public Object applyDeckRestrictAccess(ProceedingJoinPoint joinPoint,
                                             RestrictAccess restrictAccess,
                                             HttpServletRequest request,
                                             Integer deckId) throws Throwable
    {
        final Deck deck = deckDao.get(deckId);
        User sessionUser = userManager.getSessionUser(request);
        if (sessionUser == null || sessionUser.getId() != deck.getAuthor().getId()) {
            String message = "No information attached.";
            try {
                message = (String) joinPoint.getThis().getClass().getField("RESTRICT_ACCESS_PRIVATE").get(null);
            } catch (NoSuchFieldException ignored) {}
            return Views.disallow(message);
        }
        return joinPoint.proceed();
    }

    @Around("inUser() && args(request,userId,..)")
    public Object applyCheckUserResource(ProceedingJoinPoint joinPoint,
                                         HttpServletRequest request,
                                         Integer userId) throws Throwable
    {
        final User user = userDao.get(userId);
        if (user == null) {
            throw new _404Exception();
        }
        return joinPoint.proceed();
    }

    @Around("inUser() && @annotation(restrictAccess) && args(request,userId,..)")
    public Object applyUserRestrictAccess(ProceedingJoinPoint joinPoint,
                                             RestrictAccess restrictAccess,
                                             HttpServletRequest request,
                                             Integer userId) throws Throwable
    {
        if (userId != userManager.getSessionUser(request).getId()) {
            String message = "No information attached.";
            try {
                message = (String) joinPoint.getThis().getClass().getField("RESTRICT_ACCESS_PRIVATE").get(null);
            } catch (NoSuchFieldException ignored) {}
            return Views.disallow(message);
        }
        return joinPoint.proceed();
    }
}
