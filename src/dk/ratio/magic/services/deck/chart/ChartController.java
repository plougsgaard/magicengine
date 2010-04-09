package dk.ratio.magic.services.deck.chart;

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
            throws ServletException, IOException {

        ChartBuilder builder = new ChartBuilder(260, 600);
        BufferedImage image = null;

        if (chartType.equals("creature")) {
            image = builder.createCreatureManaCurveChart(deckDao.get(deckId));
        } else if (chartType.equals("spell")) {
            image = builder.createSpellManaCurveChart(deckDao.get(deckId));
        } else {
            // Default to the coalesced chart...
            image = builder.createCoalescedManaCurveChart(deckDao.get(deckId));
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        boolean written = ImageIO.write(image, "png", outputStream);

        if (!written) {
            logger.warn("Failed to write image!");
        }

        outputStream.flush();
        byte[] imageAsBytes = outputStream.toByteArray();
        outputStream.close();

        response.setContentType("image/png");
        OutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(imageAsBytes);
        responseOutputStream.flush();
        responseOutputStream.close();

        /**
         * Returning null tells Spring we have handled response.
         */
        return null;
    }
    

}
