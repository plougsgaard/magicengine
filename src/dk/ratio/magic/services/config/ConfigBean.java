package dk.ratio.magic.services.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Properties;

@Service
public class ConfigBean
{
    private final Log logger = LogFactory.getLog(getClass());

    @Resource(name = "localProperties")
    Properties properties;

    public boolean isDebugEnabled()
    {
        return testProperty("debug.enabled");
    }

    public boolean isPriceTaskEnabled()
    {
        return testProperty("pricetask.enabled");
    }

    private boolean testProperty(String booleanProperty)
    {
        String property = properties.getProperty(booleanProperty);
        return property == null || "true".equals(property);
    }
}
