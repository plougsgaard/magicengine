package dk.ratio.magic.web.deck;

import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.domain.web.user.Credentials;
import dk.ratio.magic.repository.card.CardDao;
import dk.ratio.magic.repository.deck.DeckDao;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.security.web.Policy;
import dk.ratio.magic.security.web.RestrictAccess;
import dk.ratio.magic.services.user.UserManager;
import dk.ratio.magic.util.web.Views;
import dk.ratio.magic.validation.user.UserLoginValidator;
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
@RequestMapping({"/deck/{deckId}/delete"})
public class DeckDeleteForm
{
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private DeckDao deckDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserManager userManager;

    @RestrictAccess(Policy.PRIVATE)
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showHandler(HttpServletRequest request, @PathVariable("deckId") Integer deckId)
    {
        ModelAndView mv = new ModelAndView("/deck/delete/form");
        mv.addObject("credentials", new Credentials());
        return mv;
    }

    @RestrictAccess(Policy.PRIVATE)
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView submitHandler(HttpServletRequest request, @PathVariable("deckId") Integer deckId,
                                      Credentials credentials, BindingResult bindingResult)
    {
        // implicit authentication
        new UserLoginValidator().validate(userManager, userDao, credentials, bindingResult);
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("/deck/delete/form");
            mv.addObject("bindingResult", bindingResult);
            return mv;
        }

        logger.info("User requested and confirmed deletion of deck. " +
                    "[id: " + deckId + "] " +
                    "[credentials: " + credentials + "] " +
                    "");
        deckDao.delete(deckId);

        return Views.redirect(request, "/");
    }

    public final static String RESTRICT_ACCESS_PRIVATE =
            "You tried to delete a deck, but you are not the author!<br /><br /> " +
            "Only the author of a deck is allowed to delete it. That " +
            "is because the author of the deck is the one who created " +
            "the deck, and therefore also the one who should decide " +
            "whether it should be deleted or not.<br /><br /> :)";
}
