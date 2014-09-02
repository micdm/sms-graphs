package com.micdm.sms900.data;

public class OperationStat {

    private final Operation _operation;
    private final double _percentage;

    public OperationStat(Operation operation, double percentage) {
        _operation = operation;
        _percentage = percentage;
    }

    public Operation getOperation() {
        return _operation;
    }

    public double getPercentage() {
        return _percentage;
    }
}
