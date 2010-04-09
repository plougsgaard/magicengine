package dk.ratio.magic.web.deck;

import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.repository.deck.DeckDao;
import dk.ratio.magic.services.user.UserManager;
import dk.ratio.magic.util.web.Views;
import dk.ratio.magic.validation.deck.NewDeckValidator;
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
@RequestMapping("/deck/create")
public class DeckCreateForm
{
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private DeckDao deckDao;

    @Autowired
    private UserManager userManager;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showHandler(HttpServletRequest request)
    {
        if (!userManager.isLoggedIn(request)) {
            return Views.loginRedirect(request);
        }
        return new ModelAndView("/deck/create");
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView submitHandler(HttpServletRequest request, Deck deck, BindingResult bindingResult)
    {
        if (!userManager.isLoggedIn(request)) {
            return Views.loginRedirect(request);
        }

        ModelAndView mv = new ModelAndView("/deck/create");
        new NewDeckValidator().validate(deckDao, deck, bindingResult);

        if (bindingResult.hasErrors()) {
            logger.error("Error binding the result. Number of errors: " + bindingResult.getErrorCount());
            for (FieldError error : bindingResult.getFieldErrors()) {
                logger.info(error.getField() +  ": " +error.getDefaultMessage());
            }
            mv.addObject("bindingResult", bindingResult);
            mv.addObject("deck", deck);
            return mv;
        }

        deck = deckDao.create(deck, userManager.getSessionUser(request));
        return Views.redirect(request, "/deck/" + deck.getId() + "/edit");
    }
}
