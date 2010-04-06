package dk.ratio.magic.web.cards;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CardsController
{
    @RequestMapping("/cards")
    public ModelAndView defaultHandler()
    {
        return new ModelAndView("/cards/list");
    }

    @RequestMapping("/cards/queue")
    public ModelAndView queueHandler()
    {
        return new ModelAndView("/cards/queue");
    }
}
