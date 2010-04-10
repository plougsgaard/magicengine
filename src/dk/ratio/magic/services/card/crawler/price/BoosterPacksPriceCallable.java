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

class BoosterPacksPriceCallable implements Callable<Price>
{
    private final Log logger = LogFactory.getLog(getClass());

    private static final String PATH = "http://booster-packs.com/index.php?Search=S%F8g&Itemid=1&option=com_virtuemart" +
                                       "&page=shop.browse&keyword=";
    private static final double RATE = 0.117657939d; // 1 Danish krone = 0.117657939 British pounds, 2010-04-09

    private Card card;
    private Price price;

    public BoosterPacksPriceCallable(Card card)
    {
        this.card = card;

        price = new Price();
        price.setPrice(0d);
        Seller seller = new Seller();
        seller.setId(3);
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
                // this is the greedy version (will eat as much as it can)
                // though it has to guarantee the 'kr.' on the left side
                Pattern p = Pattern.compile(
                        "<h3><a.*?>\\s*" + card.getCardName() + "\\s*</a>.*?kr.([\\d\\.]+)<",
                        Pattern.CASE_INSENSITIVE);
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

        price.setPrice(RATE * price.getPrice());

        return price;
    }
}