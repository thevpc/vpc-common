package net.vpc.common.webxml;

import java.util.ArrayList;
import java.util.List;

public class WebSessionConfig {
    private int sessionTimeout;
    private List<String> trackingModes=new ArrayList<>();

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public WebSessionConfig setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
        return this;
    }

    public List<String> getTrackingModes() {
        return trackingModes;
    }

    public WebSessionConfig setTrackingModes(List<String> trackingModes) {
        this.trackingModes = trackingModes;
        return this;
    }
}
