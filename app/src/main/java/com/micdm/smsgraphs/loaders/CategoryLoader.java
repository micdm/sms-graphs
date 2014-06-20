package com.micdm.smsgraphs.loaders;

import android.content.Context;

import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.readers.DbCategoryReader;

public class CategoryLoader extends Loader<CategoryList> {

    private final DbCategoryReader reader;
    private CategoryList categories;

    public CategoryLoader(Context context, DbHelper dbHelper, OnLoadListener onLoadListener) {
        super(context, onLoadListener);
        reader = new DbCategoryReader(dbHelper);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (categories == null) {
            forceLoad();
        } else {
            deliverResult(categories);
        }
    }

    @Override
    public CategoryList loadInBackground() {
        return reader.read();
    }

    @Override
    public void deliverResult(CategoryList categories) {
        super.deliverResult(categories);
        this.categories = categories;
    }
}
