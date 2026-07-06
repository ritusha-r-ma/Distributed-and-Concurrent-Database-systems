package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Transaction {
    String transactionId;
    int beginTime;
    int endTime;
    List<Site> properlyUpSites;
    Map<String, Integer> snapshotValues;
    List<String> queueItems;

    public Transaction(String transactionId, int beginTime) {
        this.transactionId = transactionId;
        this.beginTime = beginTime;
        this.endTime = Integer.MAX_VALUE;
        this.properlyUpSites = new ArrayList<>();
        this.snapshotValues = new HashMap<>();
        this.queueItems = new ArrayList<>();
    }

    public int getBeginTime() {
        return beginTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public List<Site> getProperlyUpSites() {
        return properlyUpSites;
    }

    public void setSnapshotValues(Map<String, Integer> snapshotValues) {
        this.snapshotValues = snapshotValues;
    }

    public Map<String, Integer> getSnapshotValues() {
        return snapshotValues;
    }

    public List<String> getQueueItems() {
        return queueItems;
    }

    public void setQueueItems(List<String> queueItems) {
        this.queueItems = queueItems;
    }
}
