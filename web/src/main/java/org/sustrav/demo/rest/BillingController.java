package org.sustrav.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sustrav.demo.services.BillingService;


@RestController
public class BillingController {

    @Autowired
    private BillingService billingService;

    @RequestMapping(value = "/api/place/{id}/visit", method = RequestMethod.POST)
    public VisitPlaceResult visitPlace(@PathVariable("id") Long id, @RequestBody VisitPlacePayload payload) {
        int newResult = billingService.acceptPoint(id, payload.getLatitude(), payload.getLongitude());
        return new VisitPlaceResult(newResult);
    }

    public static class VisitPlaceResult {
        private int result;

        public VisitPlaceResult(int result) {
            this.result = result;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }
    }

    public static class VisitPlacePayload {
        private double latitude;
        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

}
