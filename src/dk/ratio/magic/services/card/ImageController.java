package dk.ratio.magic.services.card;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import dk.ratio.magic.repository.card.CardDao;

/**
 * Writes an image (original-sized) to the output stream of the request.
 * <p/>
 * Is typically used by JavaScript that wants to load the image of a
 * card it just requested from the persistent storage.
 */
@Controller
public class ImageController
{
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private CardDao cardDao;

    @RequestMapping("/services/card/image")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        int id = Integer.parseInt(request.getParameter("id"));

        response.setContentType("image/jpeg");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(cardDao.getCardImage(id).getData());
        outputStream.close();

        /*
         * Returning null tells Spring MVC that we have taken care of
         * writing the response ourselves.
         */
        return null;
    }

    @RequestMapping("/services/card/image/{imageId}")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable("imageId") Integer imageId)
            throws ServletException, IOException
    {
        response.setContentType("image/jpeg");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(cardDao.getCardImage(imageId).getData());
        outputStream.close();

        /*
         * Returning null tells Spring MVC that we have taken care of
         * writing the response ourselves.
         */
        return null;
    }
}