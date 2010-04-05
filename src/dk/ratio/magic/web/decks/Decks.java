package dk.ratio.magic.web.decks;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Decks
{
    @RequestMapping("/")
    public ModelAndView decksHandler()
    {
        return new ModelAndView("/decks/decks");
    }
}
