package org.example;

public class Operation {
    enum Type { READ, WRITE }

    private final Type type;
    private final String variable;

    public Operation(Type type, String variable) {
        this.type = type;
        this.variable = variable;
    }

    public Type getType() {
        return type;
    }

    public String getVariable() {
        return variable;
    }
}

