package org.sustrav.demo.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.sustrav.demo.services.db.DatabasePopulator;

@Component
public class DatabasePopulatorListener implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${database.preload}")
    private boolean databasePreload;


    @Autowired
    private DatabasePopulator databasePopulator;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if (databasePreload)
            databasePopulator.populateDatabase();
    }
}
