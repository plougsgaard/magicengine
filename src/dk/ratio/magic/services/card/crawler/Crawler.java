package dk.ratio.magic.services.card.crawler;

import dk.ratio.magic.repository.card.CardDao;
import dk.ratio.magic.domain.db.card.Card;
import dk.ratio.magic.domain.db.card.Price;
import dk.ratio.magic.services.card.crawler.price.PriceCallable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
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
        cardName = cardName.replaceAll("Æ", "Ae").trim();

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

            // we need the image a bit later
            Future<byte[]> imageFuture = taskExecutor.submit(new ImageCallable(card));

            byte[] image = imageFuture.get();

            if (image == null) {
                logger.warn("Crawling could not be completed. " +
                            "[cardName: " + cardName +"] " +
                            "[card: " + card +"] " +
                            "[image: " + image +"] " +
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
                            "[image: " + image +"] " +
                            "");
                return cardDao.updateCard(card, image);
            }

            logger.info("Added card to database. " +
                        "[cardName: " + cardName +"] " +
                        "[card: " + card +"] " +
                        "[image: " + image +"] " +
                        "");

            // we submit the prices to be crawled at some point -
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
        try {
            List<Future<Price>> futures = new ArrayList<Future<Price>>
                    (PriceCallable.CALLABLES.length);

            for (Class callable : PriceCallable.CALLABLES) {
                Constructor<Callable<Price>> c = callable.getConstructor(
                        CardDao.class, Card.class);
                futures.add(taskExecutor.submit(c.newInstance(cardDao, card)));
            }
        }
        catch (Exception e) {
            logger.warn("Problem occured on updating prices. " +
                        "[Interrupted] " +
                        "[card.getCardName(): " + card.getCardName() +"].", e);
        }
    }
}
