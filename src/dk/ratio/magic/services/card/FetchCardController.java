package dk.ratio.magic.services.card;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.repository.card.CardDao;
import dk.ratio.magic.services.card.crawler.Crawler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Takes care of asynchronous calls that wish to know if a card
 * exists, and if so: (optionally) create the card from the various
 * sources around, and return a JSON representation of the most
 * fundamental features of a card.
 * <p/>
 * When a card is created its image is automatically stored in the
 * database for later retrieval.
 *
 * @see dk.ratio.magic.services.card.ImageController
 */
@Controller
public class FetchCardController
{
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private CardDao cardDao;
    @Autowired
    private Crawler cardCrawler;

    /**
     * TODO: probably not used anymore?!
     */
    @RequestMapping("/services/card/fetch")
    public ModelAndView showHandler(HttpServletRequest request)
    {
        String cardName = request.getParameter("card_name");

        if (StringUtils.isBlank(cardName)) {
            return getFailureView("(no card supplied)");
        }

        Card card = cardDao.getCard(cardName);

        if (card != null && !StringUtils.isBlank(card.getSetCode())) {
            // The card is already in the database
            return getSuccessView(card);
        }
        else {
            // The card is NOT in the database
            card = cardCrawler.crawlCard(cardName);

            if (card == null) {
                // Does not exist remotely either (or can't be parsed)
                return getFailureView(cardName);
            }
            return getSuccessView(card);
        }
    }

    @RequestMapping("/services/card/by-name/{cardName}")
    public ModelAndView fetchHandler(@PathVariable("cardName") String cardName)
    {
        Card card = cardDao.getCard(cardName);

        if (card != null && !StringUtils.isBlank(card.getSetCode())) {
            // The card is already in the database
            return getSuccessView(card);
        }
        else {
            // The card is NOT in the database
            card = cardCrawler.crawlCard(cardName);

            if (card == null) {
                // Does not exist remotely either (or can't be parsed)
                return getFailureView(cardName);
            }

            return getSuccessView(card);
        }
    }

    private ModelAndView getSuccessView(Card card)
    {
        HashMap<String, Object> model = new HashMap<String, Object>();

        model.put("id", card.getId());
        model.put("cardName", card.getCardName());
        model.put("manaCost", card.getManaCost());
        model.put("convertedManaCost", card.getConvertedManaCost());
        model.put("types", card.getTypes());
        model.put("price", card.getPrice());

        // tells the requester that the card exists
        model.put("exists", "true");

        return new ModelAndView("jsonView", model);
    }

    private ModelAndView getFailureView(String cardName)
    {
        HashMap<String, Object> model = new HashMap<String, Object>();

        model.put("cardName", cardName);

        // tells the requester that the card doesn't exist
        model.put("exists", "false");

        return new ModelAndView("jsonView", model);
    }
}