package com.micdm.sms900.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.micdm.sms900.data.CategoryList;
import com.micdm.sms900.db.DbHelper;
import com.micdm.sms900.db.readers.DbCategoryReader;

public class CategoryLoader extends AsyncTaskLoader<CategoryList> {

    private final DbHelper _dbHelper;

    private CategoryList _categories;

    public CategoryLoader(Context context, DbHelper dbHelper) {
        super(context);
        _dbHelper = dbHelper;
    }

    @Override
    protected void onStartLoading() {
        if (_categories == null || takeContentChanged()) {
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
