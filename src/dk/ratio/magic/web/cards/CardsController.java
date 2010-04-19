package dk.ratio.magic.web.cards;

import dk.ratio.magic.domain.db.card.Price;
import dk.ratio.magic.domain.db.user.User;
import dk.ratio.magic.repository.card.CardDao;
import dk.ratio.magic.services.user.UserManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

@Controller
public class CardsController
{
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private CardDao cardDao;

    @Autowired
    private UserManager userManager;

    @RequestMapping("/cards")
    public ModelAndView defaultHandler()
    {
        // return the first page
        return pageHandler(1);
    }

    @RequestMapping("/cards/page/{pageNumber}")
    public ModelAndView pageHandler(@PathVariable("pageNumber") Integer pageNumber)
    {
        ModelAndView mv = new ModelAndView("/cards/list");
        mv.addObject("cardPage", cardDao.getCardPage(pageNumber));
        return mv;
    }

    @RequestMapping("/cards/queue")
    public ModelAndView queueHandler()
    {
        return queuePageHandler(1);
    }

    @RequestMapping("/cards/queue/page/{pageNumber}")
    public ModelAndView queuePageHandler(@PathVariable("pageNumber") Integer pageNumber)
    {
        ModelAndView mv = new ModelAndView("/cards/queue/list");
        mv.addObject("cardPage", cardDao.getQueuePage(pageNumber));
        return mv;
    }

    @RequestMapping(value = "/cards/prices/consolidate", method = RequestMethod.GET)
    public ModelAndView consolidatePrices(HttpServletRequest request)
    {
        ModelAndView mv = new ModelAndView("cards/prices/consolidate");
        User sessionUser = userManager.getSessionUser(request);
        if (sessionUser == null) {
            ModelAndView errorModel = new ModelAndView("/error/disallow");
            errorModel.addObject("message", "You must be a user to do that.");
            return errorModel;
        }
        return mv;
    }

    @RequestMapping(value = "cards/prices/consolidate", method = RequestMethod.POST)
    public ModelAndView consolidatePricesPost(HttpServletRequest request)
    {
        ModelAndView mv = new ModelAndView("cards/prices/consolidate");
        User sessionUser = userManager.getSessionUser(request);
        if (sessionUser == null) {
            ModelAndView errorModel = new ModelAndView("/error/disallow");
            errorModel.addObject("message", "You must be a user to do that.");
            return errorModel;
        }
        double t0 = System.currentTimeMillis();
        List<Price> prices = cardDao.getPrices();
        double t1 = System.currentTimeMillis();
        logger.info("Retrieved all prices. " +
                    "[time: " + (t1 - t0) + "ms] " +
                    "[prices.size(): " + prices.size() + "] " +
                    "");
        t0 = System.currentTimeMillis();
        DecimalFormat df = new DecimalFormat("#.##",
                DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        for (Price price : prices) {
            price.setPrice(Double.valueOf(df.format(price.getPrice())));
        }
        cardDao.updatePrices(prices);
        t1 = System.currentTimeMillis();
        logger.info("Updated all prices. " +
                    "[time: " + (t1 - t0) + "ms] " +
                    "");
        mv.addObject("message", "success");
        return mv;
    }
}
