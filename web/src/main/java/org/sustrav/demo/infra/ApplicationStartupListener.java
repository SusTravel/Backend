package org.sustrav.demo.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.sustrav.demo.MainApplication;

@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private ApplicationContextHelper contextHelper;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        MainApplication.LOGGER.info("Application is ready, open " + contextHelper.getApplicationPath());

        if (contextHelper.isMonitoringAvailabel())
            MainApplication.LOGGER.info("Monitoring is available as " + contextHelper.getApplicationPath() + "/monitoring");
    }
}
