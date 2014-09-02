package com.micdm.sms900.data;

import java.util.ArrayList;

public class CategoryList extends ArrayList<Category> {

    public CategoryList() {
        super();
    }

    public CategoryList(int capacity) {
        super(capacity);
    }

    public Category getById(int id) {
        for (Category category: this) {
            if (category.getId() == id) {
                return category;
            }
        }
        return null;
    }
}
