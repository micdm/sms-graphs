package com.micdm.sms900.events.events;

import com.micdm.sms900.data.CategoryList;
import com.micdm.sms900.events.Event;
import com.micdm.sms900.events.EventType;

public class LoadCategoriesEvent extends Event {

    private final CategoryList _categories;

    public LoadCategoriesEvent(CategoryList categories) {
        super(EventType.LOAD_CATEGORIES);
        _categories = categories;
    }

    public CategoryList getCategories() {
        return _categories;
    }
}
