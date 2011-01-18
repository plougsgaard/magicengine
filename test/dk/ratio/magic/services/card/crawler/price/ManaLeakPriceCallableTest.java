package dk.ratio.magic.services.card.crawler.price;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.card.Price;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Test
public class ManaLeakPriceCallableTest {
	public void test01() {
		Card card = new Card();
		card.setCardName("Blinding Mage");
		ManaleakPriceCallable c = new ManaleakPriceCallable(null, card);
		Price price = c.getPrice();
		assertNotNull(price.getPrice());
		assertTrue(price.getPrice() > 0);
	}
}
