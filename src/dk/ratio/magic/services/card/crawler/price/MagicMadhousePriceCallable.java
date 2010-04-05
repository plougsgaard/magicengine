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
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MagicMadhousePriceCallable implements Callable<Price>
{
    protected final Log logger = LogFactory.getLog(getClass());

    private static final String PATH = "http://www.magicmadhouse.co.uk/catalog/advanced_search_" +
                                       "result.php?x=31&y=11&categories_id=21&inc_subcat=1&" +
                                       "manufacturers_id=&pfrom=&pto=&dfrom=&dto=&keywords=";

    private Card card;
    private Price price;

    public MagicMadhousePriceCallable(Card card)
    {
        this.card = card;

        price = new Price();
        price.setPrice(0d);
        Seller seller = new Seller();
        seller.setId(2);
        price.setSeller(seller);
    }

    public Price call() throws Exception
    {
        /*
         * Special case for lands. (?i) means case insensitive
         */
        if (card.getCardName().matches("(?i)forest|swamp|mountain|island|plains")) {
            price.setPrice(0.10d);
        } else {
            try {
                // manaleak.com uses regular url encoding which is latin1
                URL url = new URL(PATH + URLEncoder.encode(card.getCardName(), "latin1"));

                // manaleak.com serves its content encoded with latin1
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader((InputStream) url.getContent(), "latin1"));

                String line, html = "";
                while ((line = reader.readLine()) != null) {
                    html += line;
                }

                // eat all unnecessary whitespace
                html = html.replaceAll("\\s+", " ");

                // look for this pattern. \\d means digit, \\. means dot, and
                // the square brackets groups them together. + means 1 or more.
                Pattern p = Pattern.compile("<tr.*?>.*?" +
                                    "<td.*?productListing-data\">.*?</td>.*?" +
                                    "<td.*?productListing-data\">.*?</td>.*?" +
                                    "<td.*?productListing-data\">.*?</td>.*?" +
                                    "<td.*?productListing-data\">.*?</td>.*?" +
                                    "<td.*?productListing-data\">.*?([\\d\\.]+)&nbsp;</td>");

                Matcher matcher = p.matcher(html);

                while (matcher.find()) {
                    Double candidatePrice = Double.parseDouble(matcher.group(1));
                    if (price.getPrice() == 0d || candidatePrice < price.getPrice()) {
                        price.setPrice(candidatePrice);
                    }
                }

            } catch (IOException e) {
                logger.error("Problem parsing the price.");
            }
        }

        return price;
    }

    
}
