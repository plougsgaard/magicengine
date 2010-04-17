package dk.ratio.magic.tasks;

import dk.ratio.magic.domain.db.card.QueueItem;
import dk.ratio.magic.repository.card.CardDao;
import dk.ratio.magic.services.card.crawler.Crawler;
import dk.ratio.magic.services.config.ConfigBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class PriceTask
{
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private CardDao cardDao;

    @Autowired
    private Crawler crawler;

    @Autowired
    private ConfigBean configBean;

    @Autowired
    private SecureRandom secureRandom;

    public void update() throws InterruptedException
    {
        randomSleep(60);
        if (configBean.isPriceTaskEnabled()) {
            List<QueueItem> firstInQueue = cardDao.getFirstInQueue();
            crawler.updatePrice(firstInQueue);
        } else {
            logger.info("Price Task disabled.");
        }
    }

    private void randomSleep(int seconds) throws InterruptedException
    {
        int randomDelay = Math.abs(secureRandom.nextInt(seconds) * 1000);
        Thread.sleep(randomDelay);
    }
}
