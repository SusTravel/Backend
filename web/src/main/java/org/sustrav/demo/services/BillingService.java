package org.sustrav.demo.services;

import javax.transaction.Transactional;

public interface BillingService {

    @Transactional
    int acceptPoint(long pointId, double latitude, double longitude);
}
