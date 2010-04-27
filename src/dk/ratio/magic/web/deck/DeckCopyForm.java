package dk.ratio.magic.web.deck;

import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.repository.deck.DeckDao;
import dk.ratio.magic.security.web.Policy;
import dk.ratio.magic.security.web.RestrictAccess;
import dk.ratio.magic.services.user.UserManager;
import dk.ratio.magic.util.web.Views;
import dk.ratio.magic.validation.deck.NewDeckValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping({"/deck/{deckId}/copy"})
public class DeckCopyForm
{
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private DeckDao deckDao;

    @Autowired
    private UserManager userManager;

    @RestrictAccess(Policy.PRIVATE)
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showHandler(HttpServletRequest request, @PathVariable("deckId") Integer deckId)
    {
        ModelAndView mv = new ModelAndView("/deck/copy/form");
        mv.addObject("deck", new Deck());
        return mv;
    }

    @RestrictAccess(Policy.PRIVATE)
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView submitHandler(HttpServletRequest request, @PathVariable("deckId") Integer deckId,
                                      Deck deck, BindingResult bindingResult)
    {
        ModelAndView mv = new ModelAndView("/deck/copy/form");
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

        logger.info("User requested and confirmed copying of a deck. " +
                    "[id: " + deckId + "]");
        deck = deckDao.copy(userManager.getSessionUser(request), deckId, deck.getTitle());

        return Views.redirect(request, "/deck/" + deck.getId() + "/edit");
    }

    public final static String RESTRICT_ACCESS_PRIVATE = 
            "You tried to copy a deck, but you are not the author!<br /><br /> " +
            "Only the author of a deck is allowed to copy it. That " +
            "is because the author of the deck is the one who created " +
            "the deck, and therefore also the one who should decide " +
            "whether it should be copied or not. <br /><br /> :)";
}