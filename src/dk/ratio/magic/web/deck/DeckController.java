package dk.ratio.magic.web.deck;

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
import java.util.Collections;

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
        Deck deck = deckDao.getDeck(deckId);
        Collections.sort(deck.getCards());
        mv.addObject("deck", deck);

        return mv;
    }
}
