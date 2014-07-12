package com.micdm.smsgraphs.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.readers.DbCategoryReader;

public class CategoryLoader extends AsyncTaskLoader<CategoryList> {

    private final DbHelper dbHelper;

    private CategoryList categories;

    public CategoryLoader(Context context, DbHelper dbHelper) {
        super(context);
        this.dbHelper = dbHelper;
    }

    @Override
    protected void onStartLoading() {
        if (categories == null) {
            forceLoad();
        } else {
            deliverResult(categories);
        }
    }

    @Override
    public CategoryList loadInBackground() {
        return (new DbCategoryReader(dbHelper)).read();
    }

    @Override
    public void deliverResult(CategoryList data) {
        categories = data;
        super.deliverResult(data);
    }
}
