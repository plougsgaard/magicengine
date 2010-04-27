package dk.ratio.magic.web.decks;

import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.domain.web.decks.DeckFilter;
import dk.ratio.magic.repository.deck.DeckDao;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.util.repository.Page;
import dk.ratio.magic.util.web.Views;
import dk.ratio.magic.util.web._404Exception;
import dk.ratio.magic.validation.decks.FilterValidator;
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

@Controller
public class DecksController
{
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private DeckDao deckDao;

    @Autowired
    private UserDao userDao;

    @RequestMapping({"/", "/decks"})
    public ModelAndView decksHandler()
    {
        // return the first page
        return pageHandler(1);
    }

    @RequestMapping("/decks/page/{pageNumber}")
    public ModelAndView pageHandler(@PathVariable("pageNumber") Integer pageNumber)
    {
        final Page<Deck> deckPage = deckDao.getPublicDeckPage(pageNumber);
        if (deckPage.getPageCount() < pageNumber) {
            throw new _404Exception();
        }
        ModelAndView mv = new ModelAndView("/decks/list");
        mv.addObject("deckPage", deckPage);
        return mv;
    }

    @RequestMapping("/decks/user/{userId}")
    public ModelAndView userDecksHandler(@PathVariable("userId") Integer userId)
    {
        // return the first page
        return userPageHandler(1, userId);
    }

    @RequestMapping("/decks/user/{userId}/page/{pageNumber}")
    public ModelAndView userPageHandler(@PathVariable("pageNumber") Integer pageNumber,
                                          @PathVariable("userId") Integer userId)
    {
        final Page<Deck> deckPage = deckDao.getPublicUserDeckPage(pageNumber, userId);
        if (deckPage.getPageCount() < pageNumber) {
            throw new _404Exception();            
        }
        ModelAndView mv = new ModelAndView("/decks/user/list");
        mv.addObject("deckPage", deckPage);
        User user = userDao.get(userId);
        mv.addObject("user", user);

        return mv;
    }

}
