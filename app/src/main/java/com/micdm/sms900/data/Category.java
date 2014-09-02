package com.micdm.sms900.data;

public class Category {

    private final int _id;
    private String _name;

    public Category(int id, String name) {
        _id = id;
        _name = name;
    }

    @Override
    public boolean equals(Object another) {
        return (this == another) || ((another instanceof Category) && (getId() == ((Category) another).getId()));
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
