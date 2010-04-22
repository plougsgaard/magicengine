package dk.ratio.magic.services.card.crawler;

import dk.ratio.magic.domain.db.card.Card;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        final String setCode = card.getSetCode();
        if (setCode == null || "".equals(setCode.trim())) {
            return findByName(card);
        }
        return findBySet(card);
    }

    private byte[] findBySet(Card card) throws IOException
    {
        final String url = "http://magiccards.info/scans/en/" +
                           card.getSetCode() + "/" +
                           card.getCardNumber() + ".jpg";
        return getBytes(url);
    }

    private byte[] findByName(Card card) throws IOException
    {
        final String PATH = "http://magiccards.info/query?q=!";
        URL url = new URL(PATH + URLEncoder.encode(card.getCardName(), "latin1"));

        BufferedReader reader = new BufferedReader(
                new InputStreamReader((InputStream) url.getContent(), "UTF-8"));

        String line, html = "";
        while ((line = reader.readLine()) != null) {
            html += line;
        }

        // eat all unnecessary whitespace
        html = html.replaceAll("\\s+", " ");

        Pattern p = Pattern.compile("(/scans/en/\\w+/\\d+\\.jpg)");
        Matcher matcher = p.matcher(html);

        if (matcher.find()) {
            return getBytes("http://magiccards.info" + matcher.group(1));
        } else {
            logger.warn("Could not find the card image URL.");
            return null;
        }
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