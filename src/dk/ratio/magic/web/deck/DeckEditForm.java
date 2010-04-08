package dk.ratio.magic.web.deck;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.repository.card.CardDao;
import dk.ratio.magic.repository.deck.DeckDao;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.services.user.UserManager;
import dk.ratio.magic.util.web.Views;
import dk.ratio.magic.validation.deck.EditDeckValidator;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/deck/{deckId}/edit")
public class DeckEditForm
{
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private DeckDao deckDao;

    @Autowired
    private CardDao cardDao;

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserDao userDao;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showHandler(@PathVariable("deckId") Integer deckId, HttpServletRequest request)
    {
        if (!userManager.isLoggedIn(request)) {
            return Views.loginRedirect(request);
        }

        Deck originalDeck = deckDao.getDeck(deckId);
        User author = originalDeck.getAuthor();
        User sessionUser = userManager.getSessionUser(request);

        if (sessionUser == null || sessionUser.getId() != author.getId()) {
            ModelAndView errorModel = new ModelAndView("/error/disallow");
            errorModel.addObject("message", "You must be the owner of the deck to edit it.");
            return errorModel;
        }

        ModelAndView mv = new ModelAndView("/deck/edit");
        mv.addObject("deck", deckDao.getDeck(deckId));

        /*
         * Sets the key used by js authentication when doing the async
         * callback method to retrieve the cards in the deck.
         */
        String encryptedPassword = deckDao.getDeck(deckId).getAuthor().getPassword();
        String deckKey = userDao.SHA1(encryptedPassword + deckId);
        mv.addObject("deckKey", deckKey);

        return mv;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView submitHandler(@PathVariable("deckId") Integer deckId, HttpServletRequest request,
                                      Deck deck, BindingResult bindingResult)
    {
        /*
         * Authentication block
         */
        if (!userManager.isLoggedIn(request)) {
            return Views.loginRedirect(request);
        }

        Deck originalDeck = deckDao.getDeck(deckId);
        User author = originalDeck.getAuthor();
        User sessionUser = userManager.getSessionUser(request);

        if (sessionUser == null || sessionUser.getId() != author.getId()) {
            ModelAndView errorModel = new ModelAndView("/error/disallow");
            errorModel.addObject("message", "You must be the owner of the deck to edit it.");
            return errorModel;
        }

        /*
         * Validation block
         */
        new EditDeckValidator().validate(deckDao, deck, bindingResult);
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("/deck/edit");
            logger.error("Error binding the result. Number of errors: " + bindingResult.getErrorCount());
            for (FieldError error : bindingResult.getFieldErrors()) {
                logger.info(error.getField() +  ": " +error.getDefaultMessage());
            }
            mv.addObject("bindingResult", bindingResult);
            mv.addObject("deck", deck);
            return mv;
        }

        logger.info("Deck being edited: [" + deck + "]");

        /*
         * Preparing the deck to be saved
         */
        deck.setAuthor(author);

        // Colours
        String colours = "";
        if (request.getParameterValues("colours") != null) {
            for (String s : request.getParameterValues("colours")) {
                colours += s + ",";
            }
        }
        deck.setColours(colours);


        // Cards
        List<Card> cards = new ArrayList<Card>();

        for (Object o : request.getParameterMap().keySet()) {
            if (o instanceof String) {
                String s = (String) o;
                if (s.startsWith("card_")) {
                    int count = Integer.parseInt(request.getParameter(s));
                    if (count == 0) {
                        // Throw away cards that don't occur.
                        continue;
                    }
                    int cardId = Integer.parseInt(s.substring(s.indexOf("_") + 1));
                    Card card = cardDao.getCard(cardId);
                    card.setCount(count);
                    cards.add(card);
                }
            }
        }

        // Save the deck
        deck.setCards(cards);
        deckDao.update(deck);

        // Redirect to the edit site to avoid the POST-reload irritation element
        return Views.redirect(request, "/deck/" + deckId + "/edit");
    }
}