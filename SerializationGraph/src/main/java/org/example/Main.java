package org.example;

public class Main {

    public static void main(String[] args) {
        Transaction t1 = new Transaction("T1");
        t1.addOperation(new Operation(Operation.Type.WRITE, "x1"));
        t1.addOperation(new Operation(Operation.Type.WRITE, "x2"));

        Transaction t2 = new Transaction("T2");
        t2.addOperation(new Operation(Operation.Type.WRITE, "x2"));
        t2.addOperation(new Operation(Operation.Type.WRITE, "x1"));

        SerializationGraph graph = new SerializationGraph();
        graph.addTransaction(t1);
        graph.addTransaction(t2);

        graph.buildGraph();
    }
}