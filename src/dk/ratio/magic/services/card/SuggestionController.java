package dk.ratio.magic.services.card;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.repository.card.CardDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;

/**
 * Requested every time someone want to auto-suggest a name
 * of a partially typed card name.
 * <p/>
 * Expects: suggestion_input
 * <p/>
 * Renders a suggestion (which is an unordered list) via the
 * corresponding template.
 */
@Controller
public class SuggestionController
{
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private CardDao cardDao;

    @RequestMapping("/services/card/suggestion")
    public ModelAndView showHandler(HttpServletRequest request)
    {
        String input = request.getParameter("search-input");
        HashMap<String, Object> model = new HashMap<String, Object>();

        if (input != null && !input.trim().isEmpty()) {
            List<Card> cardSuggestions = cardDao.getSuggestions(input);
            model.put("cardSuggestions", cardSuggestions);
        }
        else {
            logger.error("Called with invalid arguments.");
        }

        return new ModelAndView("services/card/suggestion", "model", model);
    }
}