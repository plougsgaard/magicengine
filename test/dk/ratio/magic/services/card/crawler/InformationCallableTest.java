package dk.ratio.magic.services.card.crawler;

import dk.ratio.magic.domain.db.card.Card;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

@Test
public class InformationCallableTest {
    public void test01() throws Exception {
        InformationCallable ic = new InformationCallable("Civilized Scholar");
        Card c = ic.call();
        assertNotNull(c);
    }
}
