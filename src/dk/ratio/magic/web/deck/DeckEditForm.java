package dk.ratio.magic.web.deck;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.repository.card.CardDao;
import dk.ratio.magic.repository.deck.DeckDao;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.security.web.Policy;
import dk.ratio.magic.security.web.RestrictAccess;
import dk.ratio.magic.services.deck.chart.ChartBuilder;
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
import java.io.IOException;
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

    @RestrictAccess(Policy.PRIVATE)
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showHandler(HttpServletRequest request, @PathVariable("deckId") Integer deckId)
    {
        ModelAndView mv = new ModelAndView("/deck/edit");
        mv.addObject("deck", deckDao.get(deckId));

        /*
         * Sets the key used by js authentication when doing the async
         * callback method to retrieve the cards in the deck.
         */
        String encryptedPassword = deckDao.get(deckId).getAuthor().getPassword();
        String deckKey = userDao.SHA1(encryptedPassword + deckId);
        mv.addObject("deckKey", deckKey);

        return mv;
    }

    @RestrictAccess(Policy.PRIVATE)
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView submitHandler(HttpServletRequest request, @PathVariable("deckId") Integer deckId,
                                      Deck deck, BindingResult bindingResult)
    {
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

        /*
         * Preparing the deck to be saved
         */
        deck.setAuthor(deckDao.get(deckId).getAuthor());

        // Colours
        StringBuilder sb = new StringBuilder();
        if (request.getParameterValues("colours") != null) {
            for (String s : request.getParameterValues("colours")) {
                sb.append(s);
                sb.append(",");
            }
        }
        deck.setColours(sb.toString());

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

        if (deck.getFeatureCardId() == 0) {
            deck.setFeatureCardId(1);
        }

        // Save the deck
        deck.setCards(cards);

        ChartBuilder builder = new ChartBuilder(260, 600);
        try {
            deck = builder.buildCharts(deck);
        } catch (IOException _) {
            logger.warn("Could not build charts for deck.");
        }

        deckDao.update(deck);

        // Redirect to the edit site to avoid the POST-reload irritation element
        return Views.redirect(request, "/deck/" + deckId + "/edit");
    }

    public final static String RESTRICT_ACCESS_PRIVATE = 
            "You tried to edit a deck, but you are not the author!<br /><br /> " +
            "Only the author of a deck is allowed to edit it. That " +
            "is because the author of the deck is the one who created " +
            "the deck, and therefore also the one who should decide " +
            "whether it should be edited or not.<br /><br /> :)";
}