package com.micdm.smsgraphs.handlers;

import com.micdm.smsgraphs.data.Category;
import com.micdm.utils.events.EventListener;

import java.util.List;

public interface CategoryHandler {

    public static interface OnLoadCategoriesListener extends EventListener {
        public void onLoadCategories(List<Category> categories);
    }

    public void addOnLoadCategoriesListener(OnLoadCategoriesListener listener);
    public void removeOnLoadCategoriesListener(OnLoadCategoriesListener listener);
}
