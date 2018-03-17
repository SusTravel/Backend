package org.sustrav.demo.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHelper {

    @Value("${server.contextPath}")
    private String contextPath;

    @Value("${server.domain}")
    private String serverDomain;

    @Value("${server.port}")
    private String serverPort;

    @Value("${monitoring.active}")
    private boolean monitoringAvailabel;

    public String getApplicationPath() {
        return "http://" + serverDomain + ":" + serverPort + contextPath;
    }

    public boolean isMonitoringAvailabel() {
        return monitoringAvailabel;
    }
}
