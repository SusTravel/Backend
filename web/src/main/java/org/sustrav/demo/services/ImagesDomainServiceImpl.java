package org.sustrav.demo.services;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.sustrav.demo.data.PlaceRepository;
import org.sustrav.demo.data.model.ImagesDomainAware;
import org.sustrav.demo.data.model.Place;
import org.sustrav.demo.utils.QRCodeHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class ImagesDomainServiceImpl implements ApplicationListener<ApplicationReadyEvent>, ImagesDomainService {

    public static final Logger LOGGER = LoggerFactory.getLogger(ImagesDomainServiceImpl.class);

    @Value("${imageStorage.location}")
    private String imagesLocation;

    @Value("${imageStorage.domain}")
    private String imagesDomain;

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    private PlaceRepository placeRepository;

    @Override
    public void setDomainForEntities(List<? extends ImagesDomainAware> entites) {
        for (ImagesDomainAware item : entites)
            item.setDomainForImages(imagesDomain + "/");
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        if (serverPort == -1) {
            // not available under test execution
            return;
        }

        LOGGER.info("ImagesDomainService deals with location={}, domain={}", imagesLocation, imagesDomain);

        File storage = new File(imagesLocation);
        if (!storage.exists())
            throw new IllegalArgumentException("Directory " + imagesLocation + " does not exist");

        InputStream testFileIo = null;
        OutputStream testFileOut = null;
        try {
            testFileIo = getClass().getClassLoader().getResource("static/success.png").openStream();
            testFileOut = new FileOutputStream(imagesLocation + "/success.png");
            IOUtils.copy(testFileIo, testFileOut);
        } catch (Exception ex) {
            throw new IllegalAccessError("Can't save file to the " + imagesLocation + " - " + ex.getMessage());
        } finally {
            IOUtils.closeQuietly(testFileIo);
            IOUtils.closeQuietly(testFileOut);
        }

        try {
            URL validationUrl = new URL(imagesDomain + "/success.png");
            HttpURLConnection http = (HttpURLConnection) validationUrl.openConnection();
            if (http.getResponseCode() != 200)
                throw new IllegalStateException("Http code - " + http.getResponseCode());
        } catch (Exception ex) {
            throw new IllegalStateException("Can't read " + imagesDomain + "/success.png", ex);
        }
    }

    @Override
    public String generateQRCodeForPlace(Long id) {
        Place place = placeRepository.findOne(id);
        String name = imagesLocation + "/" + place.getQrcodeId();
        FileOutputStream out = null;
        try {
            FileUtils.forceMkdir(new File(imagesLocation + "/"));
            out = new FileOutputStream(name);
            IOUtils.write(QRCodeHandler.generateCode(place).toByteArray(), out);
        } catch (Exception ex) {
            LOGGER.error("Can't save file as " + name, ex);
            name = ex.getMessage();
        } finally {
            IOUtils.closeQuietly(out);
        }
        return name;
    }

}
