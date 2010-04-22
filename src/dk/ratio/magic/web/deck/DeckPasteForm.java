package dk.ratio.magic.web.deck;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.domain.web.deck.PasteBean;
import dk.ratio.magic.repository.card.CardDao;
import dk.ratio.magic.repository.deck.DeckDao;
import dk.ratio.magic.repository.user.UserDao;
import dk.ratio.magic.services.card.crawler.Crawler;
import dk.ratio.magic.services.deck.chart.ChartBuilder;
import dk.ratio.magic.services.deck.chart.Pair;
import dk.ratio.magic.services.user.UserManager;
import dk.ratio.magic.util.web.Views;
import dk.ratio.magic.validation.deck.EditDeckValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/deck/paste")
public class DeckPasteForm
{
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private DeckDao deckDao;

    @Autowired
    private CardDao cardDao;

    @Autowired
    private Crawler crawler;

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserDao userDao;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showHandler(HttpServletRequest request)
    {
        /*
         * Authentication block
         */
        if (!userManager.isLoggedIn(request)) {
            return Views.loginRedirect(request);
        }

        final ModelAndView mv = new ModelAndView("/deck/paste");
        mv.addObject("pasteBean", new PasteBean());
        return mv;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView submitHandler(HttpServletRequest request,
                                      PasteBean pasteBean, BindingResult bindingResult) throws IOException
    {
        /*
         * Authentication block
         */
        if (!userManager.isLoggedIn(request)) {
            return Views.loginRedirect(request);
        }

        final ModelAndView mv = new ModelAndView("/deck/paste");

        Pair<List<Card>, String> pair = parseCards(pasteBean.getCards());

        List<Card> cards = pair.getFirst();
        pasteBean.setCards(pair.getSecond());

        Deck deck = new Deck();
        Collections.sort(cards);
        deck.setCards(cards);

        mv.addObject("deck", deck);
        mv.addObject("pasteBean", pasteBean);

        int landsCount = 0, creaturesCount = 0, spellsCount = 0;
        ArrayList<Card> lands = new ArrayList<Card>();
        ArrayList<Card> creatures = new ArrayList<Card>();
        ArrayList<Card> spells = new ArrayList<Card>();

        for (Card card : cards) {
            if (card.getTypes().contains("Land")) {
                lands.add(card);
                landsCount += card.getCount();
            } else if (card.getTypes().contains("Creature")) {
                creatures.add(card);
                creaturesCount += card.getCount();
            } else {
                spells.add(card);
                spellsCount += card.getCount();
            }
        }
        mv.addObject("lands", lands);
        mv.addObject("creatures", creatures);
        mv.addObject("spells", spells);
        mv.addObject("landsCount", landsCount);
        mv.addObject("creaturesCount", creaturesCount);
        mv.addObject("spellsCount", spellsCount);

        return mv;
    }

    private Pair<List<Card>, String> parseCards(String cardString) throws IOException
    {
        ArrayList<Card> cards = new ArrayList<Card>();
        StringBuilder sb = new StringBuilder();

        Pattern pattern = Pattern.compile(
                "\\s*(\\d+)[\\sx]*([\\w\\s,'\\-\\\\!/]+)");
        BufferedReader reader = new BufferedReader(new StringReader(cardString));

        String line;
        while ((line = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                int count = Integer.parseInt(matcher.group(1));
                String cardName = matcher.group(2).trim();

                if ("".equals(cardName)) {
                    logger.debug("Card name empty. [line: " + line + "]");
                    continue;
                }

                Card card = cardDao.getCard(cardName);

                if (card != null) {
                    card.setCount(count);
                    cards.add(card);
                    sb.append(line);
                    sb.append("\n");
                    continue;
                }

                card = crawler.crawlCard(cardName);

                if (card != null) {
                    card.setCount(count);
                    cards.add(card);
                    sb.append(line);
                    sb.append("\n");
                    continue;
                }

                logger.debug("Card not recognized. [line: " + line + "]");
            }
        }

        return new Pair<List<Card>, String>(cards, sb.toString());
    }
}