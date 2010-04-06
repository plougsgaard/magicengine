package dk.ratio.magic.services.card.crawler.price;

import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.card.Price;
import dk.ratio.magic.repository.card.CardDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class PriceCallable implements Callable<List<Price>>
{
    private final Log logger = LogFactory.getLog(getClass());

    public enum UPDATE { AUTO, NO }

    private Class[] callables = {
            ManaleakPriceCallable.class,
            MagicMadhousePriceCallable.class
    };

    private List<Future<Price>> futures = new ArrayList<Future<Price>>(callables.length);


    private CardDao cardDao;
    private ThreadPoolTaskExecutor taskExecutor;
    private Card card;
    private UPDATE update;

    public PriceCallable(CardDao cardDao, ThreadPoolTaskExecutor taskExecutor, Card card, UPDATE update)
    {
        this.cardDao = cardDao;
        this.taskExecutor = taskExecutor;
        this.card = card;
        this.update = update;
    }

    @SuppressWarnings("unchecked")
    public List<Price> call() throws Exception
    {
        for (Class callable : callables) {
            Constructor<Callable<Price>> c = callable.getConstructor(Card.class);
            futures.add(taskExecutor.submit(c.newInstance(card)));
        }

        List<Price> prices = new ArrayList<Price>(futures.size());

        for (Future<Price> future : futures) {
            Price price = future.get();
            if (price != null && price.getPrice() != 0d) {
                prices.add(future.get());
            }
        }

        for (Price price : prices) {
            price.setCard(card);
        }

        if (update == UPDATE.AUTO) {
            cardDao.addPrices(card, prices);
        }

        return prices;
    }
}
