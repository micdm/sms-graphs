package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

public class EditCategoryEvent extends Event {

    private final Category _category;

    public EditCategoryEvent(Category category) {
        super(EventType.EDIT_CATEGORY);
        _category = category;
    }

    public Category getCategory() {
        return _category;
    }
}
