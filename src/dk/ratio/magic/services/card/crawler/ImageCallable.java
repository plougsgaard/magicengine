package dk.ratio.magic.services.card.crawler;

import dk.ratio.magic.domain.db.card.Card;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.URL;
import java.util.concurrent.Callable;

class ImageCallable implements Callable<byte[]>
{
    private final Log logger = LogFactory.getLog(getClass());

    private Card card;

    public ImageCallable(Card card)
    {
        this.card = card;
    }

    public byte[] call() throws Exception
    {
        String url = "http://magiccards.info/scans/en/" +
                     card.getSetCode() + "/" +
                     card.getCardNumber() + ".jpg";
        return getBytes(url);
    }

    /**
     * Get the image bytes from a remote location.
     *
     * @param imageUrlString location of the image file
     *
     * @return the downloaded image
     */
    private byte[] getBytes(String imageUrlString)
    {
        try {
            URL imageUrl = new URL(imageUrlString);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            InputStream reader = imageUrl.openStream();
            int b;
            while ((b = reader.read()) != -1) {
                byteArray.write(b);
            }
            return byteArray.toByteArray();
        }
        catch (IOException e) {
            logger.error("I/O failure.", e);
        }
        return null;
    }
}