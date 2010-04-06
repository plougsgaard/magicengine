package dk.ratio.magic.web.card;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.repository.card.CardDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CardController
{
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private CardDao cardDao;

    @RequestMapping(value = "/card/{cardId}", method = RequestMethod.GET)
    public ModelAndView showHandler(HttpServletRequest request,
                                    @PathVariable("cardId") Integer cardId)
    {
        ModelAndView mv = new ModelAndView("/card/show");

        Card card = cardDao.getPrices(cardDao.getCard(cardId));
        mv.addObject("card", card);

        return mv;
    }
}
