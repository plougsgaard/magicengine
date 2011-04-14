package dk.ratio.magic.services.deck;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.repository.card.CardDao;
import dk.ratio.magic.repository.deck.DeckDao;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.services.deck.chart.ChartBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SaveDeckController
{
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private DeckDao deckDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CardDao cardDao;

    @RequestMapping("/services/deck/save/{deckId}/key/{key}")
    public ModelAndView defaultHandler(@PathVariable Integer deckId, @PathVariable String key,
                                       @RequestBody String json) throws InterruptedException {
        Deck deck = deckDao.get(deckId);

        String deckKey = userDao.SHA1(deck.getAuthor().getPassword() + deckId);

        if (StringUtils.isBlank(deckKey) || StringUtils.isBlank(key)) {
            logger.warn("No deckKey supplied.");
            return failHandler();
        }

        if (!deckKey.equals(key)) {
            logger.warn("Wrong deckKey.");
            return failHandler();
        }

        deck = updateFromJSON(deck, json);
        deck.setAuthor(deckDao.get(deckId).getAuthor());

        ChartBuilder builder = new ChartBuilder(260, 600);
        try {
            deck = builder.buildCharts(deck);
        } catch (IOException _) {
            logger.warn("Could not build charts for deck.");
        }

        deckDao.update(deck);

        HashMap<String, Object> model = new HashMap<String, Object>();
        return new ModelAndView("jsonView", model);
    }

    /*
     * Updates deck according to JSON data.
     *
     * JSON: {id, title, status, card_list{[id, count]}}
     */
    private Deck updateFromJSON(Deck deck, String json) {
        Map<String, String> d1 = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {}.getType());
        deck.setTitle(d1.get("title"));
        deck.setStatus(d1.get("status"));
        Map<Integer, Integer> d2 = new Gson().fromJson(d1.get("card_list"), new TypeToken<HashMap<Integer, Integer>>() {}.getType());
        List<Card> cards = new ArrayList<Card>(d2.size());
        for (Map.Entry<Integer, Integer> entry : d2.entrySet()) {
            Card card = cardDao.getCard(entry.getKey());
            card.setCount(entry.getValue());
            cards.add(card);
        }
        deck.setCards(cards);
        return deck;
    }

    private ModelAndView failHandler() {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("fail", "true");
        return new ModelAndView("jsonView", model);
    }
}
