package org.example;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private final String id;
    private final List<Operation> operations;

    public Transaction(String id) {
        this.id = id;
        this.operations = new ArrayList<>();
    }

    public void addOperation(Operation op) {
        operations.add(op);
    }

    public String getId() {
        return id;
    }

    public List<Operation> getOperations() {
        return operations;
    }
}

