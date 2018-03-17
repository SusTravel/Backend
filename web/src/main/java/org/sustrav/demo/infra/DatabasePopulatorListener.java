package org.sustrav.demo.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.sustrav.demo.data.PlaceRepository;
import org.sustrav.demo.data.model.Place;
import org.sustrav.demo.services.ImagesDomainService;
import org.sustrav.demo.services.db.DatabasePopulator;

@Component
public class DatabasePopulatorListener implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${database.preload}")
    private boolean databasePreload;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ImagesDomainService imagesDomainService;

    @Autowired
    private DatabasePopulator databasePopulator;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if (!databasePreload)
            return;

        databasePopulator.populateDatabase();
        for (Place place : placeRepository.findAll())
            imagesDomainService.generateQRCodeForPlace(place.getId());

    }
}
