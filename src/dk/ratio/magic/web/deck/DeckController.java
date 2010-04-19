package dk.ratio.magic.web.deck;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.repository.deck.DeckDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class DeckController
{
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private DeckDao deckDao;

    @RequestMapping("/deck/{deckId}")
    public ModelAndView showHandler(HttpServletRequest request, @PathVariable("deckId") int deckId)
    {
        ModelAndView mv = new ModelAndView("/deck/show");
        Deck deck = deckDao.get(deckId);
        final List<Card> cards = deck.getCards();
        Collections.sort(cards);
        mv.addObject("deck", deck);

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
}
