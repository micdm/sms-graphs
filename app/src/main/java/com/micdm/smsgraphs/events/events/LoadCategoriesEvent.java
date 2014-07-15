package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

public class LoadCategoriesEvent extends Event {

    private final CategoryList categories;

    public LoadCategoriesEvent(CategoryList categories) {
        super(EventType.LOAD_CATEGORIES);
        this.categories = categories;
    }

    public CategoryList getCategories() {
        return categories;
    }
}
