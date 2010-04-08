package dk.ratio.magic.services.card.crawler;

import dk.ratio.magic.repository.card.CardDao;
import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.card.Price;
import dk.ratio.magic.services.card.crawler.price.PriceCallable;
import dk.ratio.magic.services.card.crawler.price.PriceCallable.UPDATE;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
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
                logger.warn("Crawling could not be completed. " +
                            "[cardName: " + cardName +"] " +
                            "[card: " + card +"] " +
                            "");
                return null;
            }

            Future<byte[]> imageFuture = taskExecutor.submit(
                    new ImageCallable(card));

            byte[] image = imageFuture.get();

            if (image == null) {
                logger.warn("Crawling could not be completed. " +
                            "[cardName: " + cardName +"] " +
                            "[card: " + card +"] " +
                            "[image: " + image +"] " +
                            "");
                return null;
            }

            // Prices take too long to process. Will be added at some point, though.
            logger.info("Added card to database. " +
                        "[cardName: " + cardName +"] " +
                        "[card: " + card +"] " +
                        "[image: " + image +"] " +
                        "");
            return cardDao.addCard(card, image);
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

    public Card updatePrice(Card card)
    {
        Future<List<Price>> pricesFuture = taskExecutor.submit(
                new PriceCallable(cardDao, taskExecutor, card, UPDATE.NO)
        );

        try {
            List<Price> prices = pricesFuture.get();

            if (prices.size() == 0) {
                logger.error("Found no (new) prices.");
                return null;
            }

            return cardDao.addPrices(card, prices);
        }
        catch (InterruptedException e) {
            logger.warn("Crawling could not be completed. " +
                        "[Interrupted] " +
                        "[card.getCardName(): " + card.getCardName() +"].", e);
        }
        catch (ExecutionException e) {
            logger.warn("Crawling could not be completed. " +
                        "[Execution] " +
                        "[card.getCardName(): " + card.getCardName() +"].", e);
        }
        return null;
    }

}
