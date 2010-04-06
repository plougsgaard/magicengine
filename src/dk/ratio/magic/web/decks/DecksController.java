package dk.ratio.magic.web.decks;

import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.domain.web.decks.DeckFilter;
import dk.ratio.magic.repository.deck.DeckDao;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.util.web.Views;
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
        ModelAndView mv = new ModelAndView("/decks/list");
        mv.addObject("deckPage", deckDao.getPublicDeckPage(pageNumber));
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
        ModelAndView mv = new ModelAndView("/decks/user/list");
        mv.addObject("deckPage", deckDao.getPublicUserDeckPage(
                pageNumber, userId));
        User user = userDao.getUser(userId);
        mv.addObject("user", user);

        return mv;
    }





    @RequestMapping(value = "/decks/filterz", method = RequestMethod.POST)
    public ModelAndView filterHandler(HttpServletRequest request,
                                      DeckFilter deckFilter, BindingResult bindingResult)
    {
        logger.info("[deckFilter: " + deckFilter + "]");
        logger.info("[bindingResult: " + bindingResult + "]");

        new FilterValidator().validate(deckFilter, bindingResult);
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("/user/login");
            mv.addObject("bindingResult", bindingResult);
            return mv;
        }

        return Views.redirect(request, "/");
    }
}
