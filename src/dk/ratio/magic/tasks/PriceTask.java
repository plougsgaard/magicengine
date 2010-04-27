package dk.ratio.magic.tasks;

import dk.ratio.magic.domain.db.card.QueueItem;
import dk.ratio.magic.repository.card.CardDao;
import dk.ratio.magic.services.card.crawler.Crawler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Notice: This is a `howto` class that has been left in, just in case
 * we ever need this kind of behaviour again - which is quite probable.
 */
@Service
//@Configuration
public class PriceTask
{
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private CardDao cardDao;

    @Autowired
    private Crawler crawler;

    //@Value("${pricetask.enabled}")
    private boolean enabled;
    
    private int count = 0;

    //@Scheduled(cron = "${pricetask.cron}")
    public void update()
    {
        if (enabled) {
            List<QueueItem> firstInQueue = cardDao.getFirstInQueue();
            crawler.updatePrice(firstInQueue);
        }
    }
}
