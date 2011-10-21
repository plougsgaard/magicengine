package dk.ratio.magic.services.card.crawler;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.card.Price;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Test
public class CrawlerTest {
    public void test01() {
        Crawler crawler = new Crawler();
        Card card = crawler.crawlCard("Civilized Scholar");
        assertNotNull(card);
    }
}
