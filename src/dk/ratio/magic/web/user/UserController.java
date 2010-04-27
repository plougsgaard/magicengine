package dk.ratio.magic.web.user;

import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.repository.deck.DeckDao;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.security.web.Policy;
import dk.ratio.magic.security.web.RestrictAccess;
import dk.ratio.magic.services.user.UserManager;
import dk.ratio.magic.util.repository.Page;
import dk.ratio.magic.util.web.Views;
import dk.ratio.magic.util.web._404Exception;
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
    private DeckDao deckDao;

    @Autowired
    private UserManager userManager;

    @RestrictAccess(Policy.PRIVATE)
    @RequestMapping("/user/{userId}")
    public ModelAndView userHandler(HttpServletRequest request, @PathVariable("userId") Integer userId)
    {
        return pageHandler(request, userId, 1);
    }

    @RestrictAccess(Policy.PRIVATE)
    @RequestMapping("/user/{userId}/page/{pageNumber}")
    public ModelAndView pageHandler(HttpServletRequest request, @PathVariable("userId") Integer userId,
                                    @PathVariable("pageNumber") Integer pageNumber)
    {
        final Page<Deck> deckPage = deckDao.getHiddenUserDeckPage(pageNumber, userId);
        if (deckPage.getPageCount() < pageNumber) {
            throw new _404Exception();
        }
        ModelAndView mv = new ModelAndView("/user/show");
        mv.addObject("user", userDao.get(userId));
        mv.addObject("deckPage", deckPage);
        return mv;
    }

    @RequestMapping("/user/logout")
    public ModelAndView logoutHandler(HttpServletRequest request, HttpServletResponse response)
    {
        userManager.destroySessionUser(request, response);
        return Views.redirect(request, "/");
    }

    public final static String RESTRICT_ACCESS_PRIVATE =
            "This page is sort of private.<br /><br /> :)";
}
