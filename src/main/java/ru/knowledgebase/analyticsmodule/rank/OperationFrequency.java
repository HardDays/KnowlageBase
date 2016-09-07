package ru.knowledgebase.analyticsmodule.rank;

import ru.knowledgebase.loggermodule.logenums.OPERATION;

/**
 * Created by vova on 07.09.16.
 */
public class OperationFrequency {
    private OPERATION operation;
    private int count;
    private double frequency;

    public OperationFrequency(OPERATION operation, int count, double frequency){
        this.operation = operation;
        this.count = count;
        this.frequency = frequency;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public OPERATION getOperation() {
        return operation;
    }

    public void setOperation(OPERATION operation) {
        this.operation = operation;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }
}
