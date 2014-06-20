package com.micdm.smsgraphs.data;

import java.util.ArrayList;

public class CategoryList extends ArrayList<Category> {

    public Category getById(int id) {
        for (Category category: this) {
            if (category.id == id) {
                return category;
            }
        }
        return null;
    }
}
