package com.micdm.smsgraphs.data;

import java.util.ArrayList;
import java.util.List;

public class CategoryStat {

    private final Category _category;
    private int _amount;
    private double _percentage;
    private final List<TargetStat> _stats = new ArrayList<TargetStat>();

    public CategoryStat(Category category) {
        _category = category;
    }

    public Category getCategory() {
        return _category;
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

    public List<TargetStat> getStats() {
        return _stats;
    }
}
