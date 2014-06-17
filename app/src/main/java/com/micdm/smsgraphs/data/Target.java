package com.micdm.smsgraphs.data;

public class Target {

    public final int id;
    public final String name;
    public String title;
    public Category category;

    public Target(int id, String name, String title, Category category) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.category = category;
    }
}
