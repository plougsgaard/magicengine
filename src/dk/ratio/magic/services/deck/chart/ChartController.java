package dk.ratio.magic.services.deck.chart;

import dk.ratio.magic.repository.deck.DeckDao;
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
    @Autowired
    private DeckDao deckDao;

    @RequestMapping("/services/deck/{deckId}/chart")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable("deckId") Integer deckId)
            throws ServletException, IOException
    {
        ChartBuilder builder = new ChartBuilder(200, 400);
        BufferedImage image = builder.createManaCurveChart(deckDao.getDeck(deckId));

        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", byteOutputStream );
        byteOutputStream.flush();
        byte[] bytes = byteOutputStream.toByteArray();
        byteOutputStream.close();

        response.setContentType("image/jpeg");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.close();

        /*
         * Returning null tells Spring MVC that we have taken care of
         * writing the response ourselves.
         */
        return null;
    }
}
