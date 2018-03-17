package org.sustrav.demo.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mock")
public class RestCallsMockConfiguration {

    private String location;
    private String startWith;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartWith() {
        return startWith;
    }

    public void setStartWith(String startWith) {
        this.startWith = startWith;
    }

    @Override
    public String toString() {
        return "RestCallsMockConfiguration{" +
                "location='" + location + '\'' +
                ", startWith='" + startWith + '\'' +
                '}';
    }
}
