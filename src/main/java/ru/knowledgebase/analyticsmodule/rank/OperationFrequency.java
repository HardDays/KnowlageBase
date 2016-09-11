package ru.knowledgebase.analyticsmodule.rank;

import ru.knowledgebase.loggermodule.logenums.OPERATION;

/**
 * Created by vova on 07.09.16.
 */
public class OperationFrequency {
    private int count;
    private double frequency;

    public OperationFrequency(int count, double frequency){
        this.count = count;
        this.frequency = frequency;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }
}
