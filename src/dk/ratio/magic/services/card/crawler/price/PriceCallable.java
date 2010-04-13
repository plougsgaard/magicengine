package dk.ratio.magic.services.card.crawler.price;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.card.Price;
import dk.ratio.magic.domain.db.card.Seller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceCallable
{
    private static final Log logger = LogFactory.getLog(PriceCallable.class);

    public static final Class[] CALLABLES = {
            ManaleakPriceCallable.class,
            MagicMadhousePriceCallable.class,
            BoosterPacksPriceCallable.class
    };

    private static Price initPrice(int sellerId)
    {
        Price price = new Price();
        price.setPrice(0d);
        Seller seller = new Seller();
        seller.setId(sellerId);
        price.setSeller(seller);
        return price;
    }

    protected static Price getPrice(String path, Card card, String encoding,
                             Pattern p, int sellerId, double rate)
    {
        Price price = initPrice(sellerId);

        if (card.getCardName().matches("(?i)forest|swamp|mountain|island|plains")) {
            price.setPrice(0.10d);
            return price;
        }

        Matcher matcher = p.matcher(getHtml(path, card, encoding));

        while (matcher.find()) {
            Double candidatePrice = Double.parseDouble(matcher.group(1));
            if (price.getPrice() == 0d || candidatePrice < price.getPrice()) {
                price.setPrice(candidatePrice);
            }
        }

        price.setPrice(rate * price.getPrice());

        return price;
    }

    private static String getHtml(String path, Card card, String encoding)
    {
        try {
            URL url = new URL(path + URLEncoder.encode(card.getCardName(), encoding));
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader((InputStream) url.getContent(), encoding));

            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // eat all unnecessary whitespace
            return sb.toString().replaceAll("\\s+", " ");
        } catch (IOException e) {
            logger.warn("Failed to read HTML from request.", e);
        }
        return null;
    }
}
