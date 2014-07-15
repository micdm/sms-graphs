package com.micdm.smsgraphs.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.readers.DbCategoryReader;

public class CategoryLoader extends AsyncTaskLoader<CategoryList> {

    private final DbHelper _dbHelper;

    private CategoryList _categories;

    public CategoryLoader(Context context, DbHelper dbHelper) {
        super(context);
        _dbHelper = dbHelper;
    }

    @Override
    protected void onStartLoading() {
        if (_categories == null) {
            forceLoad();
        } else {
            deliverResult(_categories);
        }
    }

    @Override
    public CategoryList loadInBackground() {
        return (new DbCategoryReader(_dbHelper)).read();
    }

    @Override
    public void deliverResult(CategoryList data) {
        _categories = data;
        super.deliverResult(data);
    }
}
