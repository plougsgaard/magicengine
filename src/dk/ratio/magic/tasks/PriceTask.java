package dk.ratio.magic.tasks;

import dk.ratio.magic.domain.db.card.QueueItem;
import dk.ratio.magic.repository.card.CardDao;
import dk.ratio.magic.services.card.crawler.Crawler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceTask
{
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private CardDao cardDao;

    @Autowired
    private Crawler crawler;

    public void update() throws InterruptedException
    {
        QueueItem firstInQueue = cardDao.getFirstInQueue();
        logger.info("Updating price. [firstInQueue: " + firstInQueue + "]");
        crawler.updatePrice(firstInQueue);
    }
}
