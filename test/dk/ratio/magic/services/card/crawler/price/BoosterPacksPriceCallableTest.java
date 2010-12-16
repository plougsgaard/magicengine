package dk.ratio.magic.services.card.crawler.price;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.card.Price;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Test
public class BoosterPacksPriceCallableTest {
	public void test01() {
		Card card = new Card();
		card.setCardName("Blinding Mage");
		BoosterPacksPriceCallable c = new BoosterPacksPriceCallable(null, card);
		Price price = c.getPrice();
		assertNotNull(price.getPrice());
		assertTrue(price.getPrice() > 0);
	}
}
