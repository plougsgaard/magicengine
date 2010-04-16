package dk.ratio.magic.web.cards;

import dk.ratio.magic.repository.card.CardDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CardsController
{
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private CardDao cardDao;

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
}
