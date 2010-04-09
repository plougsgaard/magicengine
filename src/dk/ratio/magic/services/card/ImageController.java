package dk.ratio.magic.services.card;

import com.sun.media.jai.codec.SeekableStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.media.jai.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.renderable.ParameterBlock;
import java.io.*;

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
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private CardDao cardDao;

    @RequestMapping("/services/card/image")
    public ModelAndView handleRequestFun(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        int id = Integer.parseInt(request.getParameter("id"));

        response.setContentType("image/jpeg");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(cardDao.getImage(id));
        outputStream.close();

        /*
         * Returning null tells Spring MVC that we have taken care of
         * writing the response ourselves.
         */
        return null;
    }

    @RequestMapping("/services/card/image/{cardId}")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable("cardId") Integer cardId)
            throws ServletException, IOException
    {
        response.setContentType("image/jpeg");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(cardDao.getImage(cardId));
        outputStream.close();

        /*
         * Returning null tells Spring MVC that we have taken care of
         * writing the response ourselves.
         */
        return null;
    }

    @RequestMapping("/services/card/image/{cardId}/thumbnail")
    public ModelAndView handleThumbnail(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable("cardId") Integer cardId)
            throws ServletException, IOException
    {
        byte[] image = cardDao.getThumbnail(cardId);

        if (image == null || image.length == 0) {
            logger.info("No cached thumbnail for card. Creating one. " +
                        "[cardId: " + cardId + "]");
            image = resize(cardDao.getImage(cardId));
            cardDao.setThumbnail(cardId, image);
        }

        response.setContentType("image/jpeg");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(image);
        outputStream.close();

        /*
         * Returning null tells Spring MVC that we have taken care of
         * writing the response ourselves.
         */
        return null;
    }

    @RequestMapping("/services/card/image/{cardId}/cutout")
    public ModelAndView cropHandle(HttpServletRequest request, HttpServletResponse response,
                                   @PathVariable("cardId") Integer cardId)
            throws ServletException, IOException
    {
        byte[] image = cardDao.getCutout(cardId);

        if (image == null || image.length == 0) {
            logger.info("No cached cutout for card. Creating one. " +
                        "[cardId: " + cardId + "]");
            image = crop(cardDao.getImage(cardId));
            cardDao.setCutout(cardId, image);
        }

        response.setContentType("image/jpeg");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(image);
        outputStream.close();

        /*
         * Returning null tells Spring MVC that we have taken care of
         * writing the response ourselves.
         */
        return null;
    }

    private byte[] crop(byte[] data)
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

    private byte[] resize(byte[] data)
    {
        /*
            8.3.2 Scaling Transformation
            Scaling, also known as minification and magnification, enlarges or shrinks an image.
            An x-value defines the amount of scaling in the x direction, and a y-value defines the
            amount of scaling in the y direction. The Scale operation both translates and resizes.

            Scaling is often used to geometrically register multiple images prior to performing a
            combination operation, such as image addition, subtraction, division, or compositing.
            Scaling can also be used to correct geometric distortions introduced in the image
            acquisition process, although the Affine operation ("Affine Transformation" on page 272)
            would be more suitable for this.

            xScale          0.705128205 (target 220px)
            yScale          0.705128205 (target 314px)
            xTrans          0.0f
            yTrans          0.0f
            interpolation   new InterpolationBicubic2()
         */

        SeekableStream stream = SeekableStream.wrapInputStream(new ByteArrayInputStream(data), true);
        RenderedOp image = JAI.create("stream", stream);

        ParameterBlock parameters = new ParameterBlock();
        parameters.addSource(image);
        parameters.add(0.705128205f);
        parameters.add(0.705128205f);
        parameters.add(0.0f);
        parameters.add(0.0f);
        parameters.add(new InterpolationBilinear());

        RenderedOp cropOperation = JAI.create("scale", parameters, null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JAI.create("encode", cropOperation.getAsBufferedImage(), outputStream, "JPEG", null);

        return outputStream.toByteArray();
    }
}