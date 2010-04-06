package dk.ratio.magic.services.card;

import com.sun.media.jai.codec.SeekableStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.renderable.ParameterBlock;
import java.io.*;
import java.net.URL;
import java.util.Random;

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

    @RequestMapping("/services/card/crop-image/{imageId}")
    public ModelAndView cropHandle(HttpServletRequest request, HttpServletResponse response,
                                   @PathVariable("imageId") Integer imageId)
            throws ServletException, IOException
    {
        response.setContentType("image/jpeg");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(crop(cardDao.getCardImage(imageId).getData()));
        outputStream.close();

        /*
         * Returning null tells Spring MVC that we have taken care of
         * writing the response ourselves.
         */
        return null;
    }

    private byte[] crop(byte[] data) throws IOException
    {
        /*
            7.3 Cropping an Image
            The Crop operation crops a rendered or renderable image to a specified rectangular area.
            The x, y, width, and height values are clipped to the source image's bounding box.
            These values are rounded to type int for rendered images.
            The Crop operation takes one rendered or renderable source image and four parameters.
            None of the parameters have default values; all must be supplied.

            x      22
            y      50
            width  267
            height 195
         */

        SeekableStream stream = SeekableStream.wrapInputStream(new ByteArrayInputStream(data), true);
        RenderedOp image = JAI.create("stream", stream);

        ParameterBlock parameters = new ParameterBlock();
        parameters.addSource(image);
        parameters.add(22f);
        parameters.add(50f);
        parameters.add(267f);
        parameters.add(195f);

        RenderedOp cropOperation = JAI.create("crop", parameters);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JAI.create("encode", cropOperation.getAsBufferedImage(), outputStream, "JPEG", null);

        return outputStream.toByteArray();
    }
}