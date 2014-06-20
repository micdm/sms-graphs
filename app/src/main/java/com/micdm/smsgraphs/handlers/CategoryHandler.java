package com.micdm.smsgraphs.handlers;

import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.utils.events.EventListener;

public interface CategoryHandler {

    public static interface OnLoadCategoriesListener extends EventListener {
        public void onLoadCategories(CategoryList categories);
    }

    public void addOnLoadCategoriesListener(OnLoadCategoriesListener listener);
    public void removeOnLoadCategoriesListener(OnLoadCategoriesListener listener);
}
