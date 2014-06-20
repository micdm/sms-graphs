package com.micdm.smsgraphs.data;

import java.util.Calendar;

public class Target {

    public final int id;
    public Category category;
    public final String name;
    public String title;
    public final Calendar lastPaid;
    public final int lastAmount;

    public Target(int id, Category category, String name, String title, Calendar lastPaid, int lastAmount) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.title = title;
        this.lastPaid = lastPaid;
        this.lastAmount = lastAmount;
    }
}
