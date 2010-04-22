package dk.ratio.magic.services.card.crawler;

import dk.ratio.magic.domain.db.card.QueueItem;
import dk.ratio.magic.repository.card.CardDao;
import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.card.Price;
import dk.ratio.magic.services.card.crawler.price.BoosterPacksPriceCallable;
import dk.ratio.magic.services.card.crawler.price.MagicMadhousePriceCallable;
import dk.ratio.magic.services.card.crawler.price.ManaleakPriceCallable;
import dk.ratio.magic.services.card.crawler.price.PriceCallable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Crawler
{
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private CardDao cardDao;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    public Card crawlCard(String cardName)
    {
        Future<Card> informationFuture = taskExecutor.submit(
                new InformationCallable(cardName));

        try {
            Card card = informationFuture.get();

            if (card == null) {
                logger.warn("Failed crawling card. Card null. " +
                            "[cardName: " + cardName +"] " +
                            "[card: null] " +
                            "");
                return null;
            }

            // we need the image a bit later
            Future<byte[]> imageFuture = taskExecutor.submit(new ImageCallable(card));

            byte[] image = imageFuture.get();

            if (image == null) {
                logger.warn("Failed crawling card. Image null. " +
                            "[cardName: " + cardName +"] " +
                            "[card: " + card +"] " +
                            "[image: null] " +
                            "");
                return null;
            }

            Card existingCard = cardDao.getCard(card.getCardName());
            if (existingCard != null) {
                card.setId(existingCard.getId());
                card.setPrice(existingCard.getPrice());
                logger.info("Updated card in the database. " +
                            "[cardName: " + cardName +"] " +
                            "[card: " + card +"] " +
                            "[image: not null] " +
                            "");
                return cardDao.updateCard(card, image);
            }

            logger.info("Added card to database. " +
                        "[cardName: " + cardName +"] " +
                        "[card: " + card +"] " +
                        "[image: not null] " +
                        "");

            // persist card, then update prices asynchronously
            card = cardDao.addCard(card, image);
            updateAll(card);

            return card;
        }
        catch (InterruptedException e) {
            logger.warn("Crawling could not be completed. " +
                        "[Interrupted] " +
                        "[cardName: " + cardName +"].", e);
        }
        catch (ExecutionException e) {
            logger.warn("Crawling could not be completed. " +
                        "[Execution] " +
                        "[cardName: " + cardName +"].", e);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public void updateAll(Card card)
    {
        final Class[] classes = {
            ManaleakPriceCallable.class,
            MagicMadhousePriceCallable.class,
            BoosterPacksPriceCallable.class
        };
        try {
            for (Class callable : classes) {
                Constructor<Callable<Price>> c = callable.getConstructor(
                        CardDao.class, Card.class);
                taskExecutor.submit(c.newInstance(cardDao, card));
            }
        }
        catch (Exception e) {
            logger.warn("Problem occurred while updating prices. " +
                        "[card.getCardName(): " + card.getCardName() +"].", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void updatePrice(QueueItem firstInQueue)
    {
        final Class[] classes = {
            ManaleakPriceCallable.class,
            MagicMadhousePriceCallable.class,
            BoosterPacksPriceCallable.class
        };
        final Class priceClass = classes[firstInQueue.getSellerId() - 1];
        Card card = cardDao.getCard(firstInQueue.getCardId());
        try {
            Constructor<Callable<Price>> c = priceClass.getConstructor(CardDao.class, Card.class);
            taskExecutor.submit(c.newInstance(cardDao, card));
        }
        catch (Exception e) {
            logger.warn("Problem occurred while updating price. " +
                        "[card.getCardName(): " + card.getCardName() +"].", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void updatePrice(List<QueueItem> firstInQueue)
    {
        final Class[] classes = {
            ManaleakPriceCallable.class,
            MagicMadhousePriceCallable.class,
            BoosterPacksPriceCallable.class
        };
        for (QueueItem item : firstInQueue) {
            final Class priceClass = classes[item.getSellerId() - 1];
            Card card = cardDao.getCard(item.getCardId());
            try {
                Constructor<Callable<Price>> c = priceClass.getConstructor(CardDao.class, Card.class);
                taskExecutor.submit(c.newInstance(cardDao, card));
            }
            catch (Exception e) {
                logger.warn("Problem occurred while updating price. " +
                            "[card.getCardName(): " + card.getCardName() +"].", e);
            }
        }
    }
}
