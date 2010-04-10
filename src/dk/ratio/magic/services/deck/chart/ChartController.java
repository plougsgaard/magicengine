package dk.ratio.magic.services.deck.chart;

import dk.ratio.magic.domain.db.deck.Deck;
import dk.ratio.magic.repository.deck.DeckDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class ChartController
{
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private DeckDao deckDao;

    @RequestMapping("/services/deck/{deckId}/chart/{chartType}")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable("deckId") Integer deckId,
                                      @PathVariable("chartType") String chartType)
            throws ServletException, IOException
    {
        Deck deck = deckDao.get(deckId);
        if (deck.getChartCurveAll() == null || deck.getChartCurveAll().length == 0) {
            ChartBuilder builder = new ChartBuilder(260, 600);
            deck = deckDao.updateCharts(builder.buildCharts(deck));
        }

        response.setContentType("image/png");
        OutputStream outputStream = response.getOutputStream();

        if ("creature".equals(chartType)) {
            outputStream.write(deck.getChartCurveCreatures());
        } else if ("spell".equals(chartType)) {
            outputStream.write(deck.getChartCurveSpells());
        }
        else {
            outputStream.write(deck.getChartCurveAll());
        }

        outputStream.close();

        /**
         * Returning null tells Spring we have handled response.
         */
        return null;
    }


}
