package dk.ratio.magic.web.decks;

import dk.ratio.magic.domain.web.decks.DeckFilterBean;
import dk.ratio.magic.util.web.Views;
import dk.ratio.magic.validation.decks.FilterValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class Decks
{
    protected final Log logger = LogFactory.getLog(getClass());

    @RequestMapping("/")
    public ModelAndView decksHandler()
    {
        return new ModelAndView("/decks/decks");
    }

    @RequestMapping(value = "/decks/filter", method = RequestMethod.POST)
    public ModelAndView filterHandler(HttpServletRequest request,
                                      DeckFilterBean deckFilterBean, BindingResult bindingResult)
    {
        logger.info("[deckFilterBean: " + deckFilterBean + "]");
        logger.info("[bindingResult: " + bindingResult + "]");

        new FilterValidator().validate(deckFilterBean, bindingResult);
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("/user/login");
            mv.addObject("bindingResult", bindingResult);
            return mv;
        }

        return Views.redirect(request, "/");
    }
}
