package org.sustrav.demo.services;

import org.sustrav.demo.data.model.ImagesDomainAware;

import java.util.List;

public interface ImagesDomainService {
    void setDomainForEntities(List<? extends ImagesDomainAware> entites);

    String generateQRCodeForPlace(Long id);
}
