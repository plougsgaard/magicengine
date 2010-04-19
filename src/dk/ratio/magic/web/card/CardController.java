package dk.ratio.magic.web.card;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.repository.card.CardDao;
import dk.ratio.magic.services.card.crawler.Crawler;
import dk.ratio.magic.services.user.UserManager;
import dk.ratio.magic.util.web.Views;
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
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private CardDao cardDao;

    @Autowired
    private UserManager userManager;

    @Autowired
    private Crawler cardCrawler;

    @RequestMapping(value = "/card/{cardId}", method = RequestMethod.GET)
    public ModelAndView showHandler(HttpServletRequest request,
                                    @PathVariable("cardId") Integer cardId)
    {
        ModelAndView mv = new ModelAndView("/card/show");

        User sessionUser = userManager.getSessionUser(request);
        if (sessionUser != null) {
            mv.addObject("sessionUser", sessionUser);
        }

        Card card = cardDao.getPrices(cardDao.getCard(cardId));
        mv.addObject("card", card);

        return mv;
    }

    @RequestMapping(value = "/card/search", method = RequestMethod.POST)
    public ModelAndView searchHandler(HttpServletRequest request)
    {
        String cardName = request.getParameter("search-input");

        Card card = cardDao.getCard(cardName);

        if (card == null) {
            User sessionUser = userManager.getSessionUser(request);
            if (sessionUser == null) {
                ModelAndView errorModel = new ModelAndView("/error/disallow");
                errorModel.addObject("message", "You must be a user to search for new cards.");
                return errorModel;
            }

            card = cardCrawler.crawlCard(cardName);

            if (card == null) {
                ModelAndView errorModel = new ModelAndView("/error/disallow");
                errorModel.addObject("message", "Card not found.");
                return errorModel;
            }
        }

        return Views.redirect(request, "/card/" + card.getId());
    }

    @RequestMapping(value = "/card/{cardId}/price/update", method = RequestMethod.POST)
    public ModelAndView updatePriceHandler(HttpServletRequest request,
                                    @PathVariable("cardId") Integer cardId)
    {
        ModelAndView mv = Views.redirect(request, "/card/" + cardId);

        User sessionUser = userManager.getSessionUser(request);
        if (sessionUser != null) {
            mv.addObject("sessionUser", sessionUser);
        }

        if (sessionUser == null) {
            ModelAndView errorModel =
                    new ModelAndView("/errors/disallow");
            errorModel.addObject("message", "You must be logged in to do that.");
            return errorModel;
        }

        Card card = cardDao.getCard(cardId);

        if (card == null) {
             ModelAndView errorModel =
                    new ModelAndView("/errors/disallow");
            errorModel.addObject("message", "Card does not exist.");
            return errorModel;
        }

        cardCrawler.updateAll(card);
        return mv;
    }

    @RequestMapping(value = "/card/{cardId}/update", method = RequestMethod.POST)
    public ModelAndView updateCardHandler(HttpServletRequest request,
                                          @PathVariable("cardId") Integer cardId)
    {
        ModelAndView mv = Views.redirect(request, "/card/" + cardId);

        User sessionUser = userManager.getSessionUser(request);
        if (sessionUser == null) {
            ModelAndView errorModel =
                    new ModelAndView("/errors/disallow");
            errorModel.addObject("message", "You must be logged in to do that.");
            return errorModel;
        }

        Card card = cardDao.getCard(cardId);

        if (card == null) {
             ModelAndView errorModel =
                    new ModelAndView("/errors/disallow");
            errorModel.addObject("message", "Card does not exist.");
            return errorModel;
        }

        cardCrawler.crawlCard(card.getCardName());
        return mv;
    }
}
