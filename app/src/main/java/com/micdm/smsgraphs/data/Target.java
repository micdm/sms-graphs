package com.micdm.smsgraphs.data;

import java.util.Date;

public class Target {

    public final int id;
    public Category category;
    public final String name;
    public String title;
    public final Date lastPaid;

    public Target(int id, Category category, String name, String title, Date lastPaid) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.title = title;
        this.lastPaid = lastPaid;
    }
}
