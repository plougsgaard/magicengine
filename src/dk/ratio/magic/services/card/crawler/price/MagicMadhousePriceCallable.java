package dk.ratio.magic.services.card.crawler.price;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.card.Price;
import dk.ratio.magic.repository.card.CardDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class MagicMadhousePriceCallable implements Callable<Price>
{
    private final Log logger = LogFactory.getLog(getClass());

    private static final String PATH = "http://www.magicmadhouse.co.uk/catalog/advanced_search_" +
                                       "result.php?x=31&y=11&categories_id=21&inc_subcat=1&" +
                                       "manufacturers_id=&pfrom=&pto=&dfrom=&dto=&keywords=";
    private static final String ENCODING = "latin1";
    private static final int SELLER_ID = 2;
    private static final double RATE = 1;

    private CardDao cardDao;
    private Card card;

    public MagicMadhousePriceCallable(CardDao cardDao, Card card)
    {
        this.cardDao = cardDao;
        this.card = card;
    }

    public Price call()
    {
        Pattern p = Pattern.compile(
                        "<tr class=\"productListing.*?" +
                        ">\\s*(?:FOIL)?\\s*" + card.getCardName() + "\\s*<.*?" +
                        "([\\d]+\\.[\\d]+)&nbsp;" +
                        ".*?</tr>");
        Price price = PriceCallable.getPrice(PATH, card, ENCODING, p, SELLER_ID, RATE);
        return cardDao.addPrice(card, price);
    }
}
