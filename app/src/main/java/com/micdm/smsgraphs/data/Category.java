package com.micdm.smsgraphs.data;

public class Category {

    private final int _id;
    private String _name;

    public Category(int id, String name) {
        _id = id;
        _name = name;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
}
