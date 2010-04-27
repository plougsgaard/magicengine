package dk.ratio.magic.web.cards;

import dk.ratio.magic.security.web.RestrictAccess;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cards/search")
public class CardsSearchForm
{
    @RestrictAccess
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView defaultHandler()
    {
        return new ModelAndView("/cards/search/form");
    }
}