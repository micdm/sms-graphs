package com.micdm.sms900.data;

public class TargetStat {

    private final Target _target;
    private int _amount;
    private double _percentage;

    public TargetStat(Target target) {
        _target = target;
    }

    public Target getTarget() {
        return _target;
    }

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        _amount = amount;
    }

    public double getPercentage() {
        return _percentage;
    }

    public void setPercentage(double percentage) {
        _percentage = percentage;
    }
}
