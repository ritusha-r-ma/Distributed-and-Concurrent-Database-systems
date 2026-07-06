package org.example;

import java.util.ArrayList;
import java.util.List;

public class ReadWriteInstr {
    public enum IOType {
        READ,
        WRITE
    }

    int value;
    IOType type;
    String variableName;
    int timeStamp;
    List<Integer> downSites;

    public ReadWriteInstr(String variableName, int timeStamp, List<Integer> downSites) {
        this.type = IOType.READ;
        this.variableName = variableName;
        this.timeStamp = timeStamp;
        this.downSites = downSites;
    }

    // Constructor for WRITE instructions
    public ReadWriteInstr(String variableName, int value, int timeStamp, List<Integer> downSites) {
        this.type = IOType.WRITE;
        this.value = value;
        this.timeStamp = timeStamp;
        this.variableName = variableName;
        this.downSites = downSites;
    }

    public int getValue() {
        return value;
    }

    public IOType getType() {
        return type;
    }

    public String getVariableName() {
        return variableName;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public List<Integer> getDownSites() {
        return downSites;
    }
}
