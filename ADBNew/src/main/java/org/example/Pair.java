package org.example;

public class Pair {
    String transactionId;
    String edgeType;

    public Pair(String transaction, String edgeName) {
        this.transactionId = transaction;
        this.edgeType = edgeName;
    }
    public String getTransactionId() {
        return transactionId;
    }

    public String getEdgeType() {
        return edgeType;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setEdgeType(String edgeType) {
        this.edgeType = edgeType;
    }
}
