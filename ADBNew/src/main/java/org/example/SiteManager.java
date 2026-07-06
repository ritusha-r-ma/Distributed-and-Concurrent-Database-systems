package org.example;

import java.util.*;

public class SiteManager {
    List<Site> sites;

    List<String> failureSites;
    public SiteManager() {
        sites = new ArrayList<>();
        failureSites = new ArrayList<>();
        applyDefaultSettings();
    }
    public void applyDefaultSettings() {
        for (int i = 1; i<=10; i++) {
            Site site = new Site(i, 0);
            sites.add(site);
            populateDefaultVariable(site);
        }
    }

    public void populateDefaultVariable(Site site) {
        Map<String, Integer> data = site.data;

        for (int i=2; i<=20; i = i+2) {
            data.put("x" + i, i*10);
        }

        for (int i=1; i<20; i = i+2) {
            if (1 + i%10 == site.siteNumber) {
                data.put("x" + i, i*10);
            }
        }
    }

    public Site getSiteObject(int siteNumber) {
        for (Site site : sites) {
            if (site.siteNumber == siteNumber) {
                return site;
            }
        }

        return null;
    }

    public void bringSiteDown(int siteNumber, int timeStamp) {
        for (Site site : sites) {
            if (site.siteNumber == siteNumber) {
                site.isRecoveredSite = true;
                site.setTimeStamp(timeStamp);
                site.setSiteStatus(Site.SiteStatus.DOWN);
                break;
            }
        }
    }

    public void bringSiteUp(int siteNumber, int timeStamp) {
        for (Site site : sites) {
            if (site.siteNumber == siteNumber) {
                site.setTimeStamp(timeStamp);
                site.setSiteStatus(Site.SiteStatus.UP);
                break;
            }
        }
    }

    public int getVariableValueForAnyUpSite(String variableName, List<Integer> downSites) {
        for (Site site : this.sites) {
            if (!downSites.contains(site.siteNumber) && site.getData().containsKey(variableName)) {
                return site.getData().get(variableName);
            }
        }

        return -1;
    }

    public List<Site> getSites() {
        return sites;
    }
}
