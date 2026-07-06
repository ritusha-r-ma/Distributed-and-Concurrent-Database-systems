package org.example;

import java.util.HashMap;
import java.util.Map;

public class Site {
    public enum SiteStatus {
        UP,
        DOWN
    }
    int siteNumber;
    SiteStatus siteStatus;
    int timeStamp;

    boolean isRecoveredSite;
    Map<String, Integer> data;

    Site(int siteNumber, int timeStamp) {
        this.siteNumber = siteNumber;
        this.timeStamp = timeStamp;
        this.isRecoveredSite = false;
        this.data = new HashMap<>();
        this.siteStatus = SiteStatus.UP;
    }

    public void setSiteStatus(SiteStatus siteStatus) {
        this.siteStatus = siteStatus;
    }

    public SiteStatus getSiteStatus() {
        return siteStatus;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public int getSiteNumber() {
        return siteNumber;
    }

    public Map<String, Integer> getData() {
        return data;
    }

    public boolean isRecoveredSite() {
        return isRecoveredSite;
    }

    public void setRecoveredSite(boolean recoveredSite) {
        isRecoveredSite = recoveredSite;
    }
}
