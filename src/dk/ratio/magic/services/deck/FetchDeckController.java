package dk.ratio.magic.services.deck;

import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.repository.deck.DeckDao;
import dk.ratio.magic.repository.user.UserDao;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
public class FetchDeckController
{
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private DeckDao deckDao;

    @Autowired
    private UserDao userDao;

    @RequestMapping("/services/deck/fetch/{deckId}/key/{key}")
    public ModelAndView defaultHandler(@PathVariable Integer deckId, @PathVariable String key)
    {
        Deck deck = deckDao.get(deckId);

        String deckKey = userDao.SHA1(deck.getAuthor().getPassword() + deckId);

        logger.info("Fetching restricted deck. " +
                    "[deck.getId(): " + deck.getId() + "] " +
                    "[deck.getAuthor(): " +deck.getAuthor() + "] " +
                    "[deck.getAuthor().getPassword(): " + deck.getAuthor().getPassword() + "] " +
                    "[deckKey: " + deckKey + "] " +
                    "");

        if (StringUtils.isBlank(deckKey) || StringUtils.isBlank(key)) {
            logger.warn("No deckKey supplied.");
            return failHandler();
        }

        if (!deckKey.equals(key)) {
            logger.warn("Wrong deckKey.");
            return failHandler();
        }

        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("cards", deck.getCards());
        return new ModelAndView("jsonView", model);
    }

    /**
     * Gets the JSON view for the cards belonging to a certain
     * deck, with the constraint that the deck has to be public.
     * If it's not, null is returned.
     *
     * @param deckId deck's id
     * @return a JSON view with the cards belonging to the deck
     */
    @RequestMapping("/services/deck/fetch/{deckId}")
    public ModelAndView publicHandler(@PathVariable Integer deckId)
    {
        Deck deck = deckDao.get(deckId);

        if (!"public".equalsIgnoreCase(deck.getStatus())) {
            return failHandler();
        }

        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("cards", deck.getCards());
        return new ModelAndView("jsonView", model);
    }

    private ModelAndView failHandler() {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("fail", "true");
        return new ModelAndView("jsonView", model);
    }
}
