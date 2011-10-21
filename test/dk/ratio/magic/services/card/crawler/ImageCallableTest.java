package dk.ratio.magic.services.card.crawler;

import dk.ratio.magic.domain.db.card.Card;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

@Test
public class ImageCallableTest {
    public void test01() throws Exception {
        ImageCallable ic = new ImageCallable(new InformationCallable("Civilized Scholar").call());
        byte[] b = ic.call();
        assertNotNull(b);
    }
}
