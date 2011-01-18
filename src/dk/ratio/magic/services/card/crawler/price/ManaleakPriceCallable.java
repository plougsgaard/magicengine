package dk.ratio.magic.services.card.crawler.price;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.card.Price;
import dk.ratio.magic.repository.card.CardDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class ManaleakPriceCallable implements Callable<Price>
{
    private final Log logger = LogFactory.getLog(getClass());

    private static final String PATH = "http://www.manaleak.com/store/advanced_search_result.php?" +
                                       "search_in_description=1&inc_subcat=1&x=0&y=0&exact_title=1" +
                                       "&categories_id=&rarities_id=&colours_id=&keywords=";
    private static final String ENCODING = "latin1";
    private static final int SELLER_ID = 1;
    private static final double RATE = 1;

    private CardDao cardDao;
    private Card card;

    public ManaleakPriceCallable(CardDao cardDao, Card card)
    {
        this.cardDao = cardDao;
        this.card = card;
    }

	public Price call()
    {
        return cardDao.addPrice(card, getPrice());
    }

	protected Price getPrice() {
		Pattern p = Pattern.compile(
                "&nbsp;\\u00A3([\\d\\.]+)p&nbsp;");
        return PriceCallable.getPrice(PATH, card, ENCODING, p, SELLER_ID, RATE);
	}
}
