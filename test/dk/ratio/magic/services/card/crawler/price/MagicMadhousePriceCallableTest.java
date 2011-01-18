package dk.ratio.magic.services.card.crawler.price;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.card.Price;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Test
public class MagicMadhousePriceCallableTest {
	public void test01() {
		Card card = new Card();
		card.setCardName("Blinding Mage");
		MagicMadhousePriceCallable c = new MagicMadhousePriceCallable(null, card);
		Price price = c.getPrice();
		assertNotNull(price.getPrice());
		assertTrue(price.getPrice() > 0);
	}
}
