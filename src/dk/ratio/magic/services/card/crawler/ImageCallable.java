package dk.ratio.magic.services.card.crawler;

import dk.ratio.magic.domain.db.card.Card;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.URISyntaxException;
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

    /**
     * Find the image of a card in descending order of precision.
     *
     * The most precise is if both a set code and a card number is supplied.
     * The second most precise is if the set code is supplied.
     * If neither set code nor card number is supplied we just search by name.
     * If both of the two more precise searches fail, we just try and find it
     * by name.
     *
     * Order of precision:
     * 
     * 1) by card number, 2) by set code, 3) by name
     *
     * @return the image as a byte[]
     * @throws Exception if something went wrong
     */
    public byte[] call() throws Exception
    {
        final String setCode = card.getSetCode();
        final String cardNumber = card.getCardNumber();
        if (StringUtils.isBlank(cardNumber) && !StringUtils.isBlank(setCode)) {
            return findBySet(card);
        } else if (StringUtils.isBlank(cardNumber) || StringUtils.isBlank(setCode)) {
            return findByName(card);
        }
        return findByCardNumber(card);
    }

    /**
     * Finds an image from the card number.
     *
     * Calls `findBySet` on error.
     *
     * @param card card to find image for
     * @return the image as a byte[]
     * @throws IOException on error
     * @throws URISyntaxException on error
     */
    private byte[] findByCardNumber(Card card) throws IOException, URISyntaxException
    {
        final String url = "http://magiccards.info/scans/en/" +
                           card.getSetCode() + "/" +
                           card.getCardNumber() + ".jpg";
        final byte[] bytes = getBytes(url);
        return bytes == null ? findBySet(card) : bytes;
    }

    /**
     * Finds an image from the card's set code.
     *
     * Calls `findByName` on error.
     *
     * @param card card to find image for
     * @return the image as a byte[]
     * @throws IOException on error
     * @throws URISyntaxException on error
     */
    private byte[] findBySet(Card card) throws IOException, URISyntaxException
    {
        final String PATH = "http://magiccards.info/query?q=";
        final String ARGUMENTS = "%20e%3A" + card.getSetCode() + "%2Fen&v=card&s=cname";
        URL url = new URL(PATH + URLEncoder.encode(card.getCardName(), "latin1") + ARGUMENTS);
        final byte[] bytes = getBytes(findImageUrl(url));
        return bytes == null ? findByName(card) : bytes;
    }

    /**
     * Finds an image from the card's name.
     *
     * @param card card to find image for
     * @return the image as a byte[]
     * @throws IOException on error
     */
    private byte[] findByName(Card card) throws IOException
    {
        final String PATH = "http://magiccards.info/query?q=!";
        URL url = new URL(PATH + URLEncoder.encode(card.getCardName(), "latin1"));
        String imageUrl = findImageUrl(url);
        return getBytes(imageUrl);
    }

    /**
     * Downloads and parses a site to get the URL of the image.
     *
     *
     */
    /**
     * Downloads and parses a site to get the URL of the image.
     *
     * @param cardUrl url to parse
     * @return string URL of the image found, null if not found
     * @throws IOException on error
     */
    private String findImageUrl(URL cardUrl) throws IOException
    {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader((InputStream) cardUrl.getContent(), "UTF-8"));

        String line, html = "";
        while ((line = reader.readLine()) != null) {
            html += line;
        }

        // eat all unnecessary whitespace
        html = html.replaceAll("\\s+", " ");

        Pattern p = Pattern.compile("(/scans/en/\\w+/\\w+\\.jpg)");
        Matcher matcher = p.matcher(html);

        if (matcher.find()) {
            return "http://magiccards.info" + matcher.group(1);
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