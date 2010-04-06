package dk.ratio.magic.web.decks;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/decks/search")
public class DecksSearchForm
{
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView defaultHandler()
    {
        return new ModelAndView("/decks/search/form");
    }
}