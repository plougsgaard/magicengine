package dk.ratio.magic.services.card.crawler.price;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.card.Price;
import dk.ratio.magic.repository.card.CardDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class BoosterPacksPriceCallable implements Callable<Price>
{
    private final Log logger = LogFactory.getLog(getClass());

    private static final String PATH = "http://booster-packs.com/index.php?Search=S%F8g&Itemid=1&option=com_virtuemart" +
                                       "&page=shop.browse&keyword=";
    private static final String ENCODING = "latin1";
    private static final int SELLER_ID = 3;
    private static final double RATE = 0.117657939d; // 1 Danish krone = 0.117657939 British pounds, 2010-04-09

    private CardDao cardDao;
    private Card card;

    public BoosterPacksPriceCallable(CardDao cardDao, Card card)
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
                        "<h3><a.*?>\\s*" + card.getCardName() +
                        "\\s*</a>.*?kr.([\\d,]+)<",
                        Pattern.CASE_INSENSITIVE);
        return PriceCallable.getPrice(PATH, card, ENCODING, p, SELLER_ID, RATE);
	}
}
