package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

public class EditCategoryEvent extends Event {

    private final Category _category;
    private final boolean _needRemove;

    public EditCategoryEvent(Category category, boolean needRemove) {
        super(EventType.EDIT_CATEGORY);
        _category = category;
        _needRemove = needRemove;
    }

    public Category getCategory() {
        return _category;
    }

    public boolean needRemove() {
        return _needRemove;
    }
}
