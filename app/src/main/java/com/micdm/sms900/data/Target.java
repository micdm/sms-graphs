package com.micdm.sms900.data;

import org.joda.time.DateTime;

public class Target {

    private final int _id;
    private Category _category;
    private final String _name;
    private String _title;
    private final DateTime _lastPaid;
    private final int _lastAmount;

    public Target(int id, Category category, String name, String title, DateTime lastPaid, int lastAmount) {
        _id = id;
        _category = category;
        _name = name;
        _title = title;
        _lastPaid = lastPaid;
        _lastAmount = lastAmount;
    }

    public int getId() {
        return _id;
    }

    public Category getCategory() {
        return _category;
    }

    public void setCategory(Category category) {
        _category = category;
    }

    public String getName() {
        return _name;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public String getPrettyTitle() {
        return (_title == null) ? _name : _title;
    }

    public DateTime getLastPaid() {
        return _lastPaid;
    }

    public int getLastAmount() {
        return _lastAmount;
    }
}
