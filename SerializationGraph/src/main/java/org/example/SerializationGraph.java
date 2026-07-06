package org.example;

import java.util.*;

public class SerializationGraph {
    private final Map<String, Transaction> transactions;

    public SerializationGraph() {
        this.transactions = new HashMap<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
    }

    public void buildGraph() {
        for (Transaction t1 : transactions.values()) {
            for (Transaction t2 : transactions.values()) {
                if (!t1.getId().equals(t2.getId())) {
                    for (Operation op1 : t1.getOperations()) {
                        for (Operation op2 : t2.getOperations()) {
                            if (op1.getVariable().equals(op2.getVariable())) {
                                if ((op1.getType() == Operation.Type.WRITE && op2.getType() == Operation.Type.READ) ||
                                        (op1.getType() == Operation.Type.READ && op2.getType() == Operation.Type.WRITE) ||
                                        (op1.getType() == Operation.Type.WRITE && op2.getType() == Operation.Type.WRITE)) {
                                    System.out.println("Edge from " + t1.getId() + " to " + t2.getId() + " due to " + op1.getType() + "-" + op2.getType() + " conflict on " + op1.getVariable());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
