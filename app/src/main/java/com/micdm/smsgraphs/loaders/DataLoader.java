package com.micdm.smsgraphs.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.data.OperationReport;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.readers.DbCategoryReader;
import com.micdm.smsgraphs.db.readers.DbOperationReader;
import com.micdm.smsgraphs.db.readers.DbOperationReportReader;
import com.micdm.smsgraphs.db.readers.DbTargetReader;

import java.util.Calendar;

public class DataLoader extends AsyncTaskLoader<LoaderResult> {

    public static interface OnLoadListener {
        public void onStartLoadAll();
        public void onFinishLoadAll();
        public void onStartLoadOperations(Calendar month);
        public void onFinishLoadOperations();
    }

    public static enum Task {
        LOAD_ALL,
        LOAD_OPERATIONS
    }

    private final DbHelper dbHelper;
    private final OnLoadListener listener;

    private Task task;
    private OperationReport report;
    private CategoryList categories;
    private TargetList targets;
    private Calendar month;
    private MonthOperationList operations;

    public DataLoader(Context context, DbHelper dbHelper, OnLoadListener listener) {
        super(context);
        this.dbHelper = dbHelper;
        this.listener = listener;
    }

    public void reloadAll() {
        task = Task.LOAD_ALL;
        report = null;
        targets = null;
        operations = null;
        forceLoad();
    }

    public void reloadOperations(Calendar month) {
        task = Task.LOAD_OPERATIONS;
        this.month = month;
        operations = null;
        forceLoad();
    }

    @Override
    public void onForceLoad() {
        switch (task) {
            case LOAD_ALL:
                listener.onStartLoadAll();
                break;
            case LOAD_OPERATIONS:
                listener.onStartLoadOperations(month);
                break;
        }
        super.onForceLoad();
    }

    @Override
    public LoaderResult loadInBackground() {
        switch (task) {
            case LOAD_ALL:
                return loadAll();
            case LOAD_OPERATIONS:
                return loadOperations();
            default:
                throw new RuntimeException("unknown task");
        }
    }

    private LoaderResult loadAll() {
        OperationReport report = this.report;
        if (report == null) {
            report = getReport();
        }
        CategoryList categories = this.categories;
        if (categories == null) {
            categories = getCategories();
        }
        TargetList targets = this.targets;
        if (targets == null) {
            targets = getTargets(categories);
        }
        return new LoaderResult(task, report, categories, targets);
    }

    private OperationReport getReport() {
        return (new DbOperationReportReader(dbHelper)).read();
    }

    private CategoryList getCategories() {
        return (new DbCategoryReader(dbHelper)).read();
    }

    private TargetList getTargets(CategoryList categories) {
        return (new DbTargetReader(dbHelper, categories)).read();
    }

    private LoaderResult loadOperations() {
        MonthOperationList operations = this.operations;
        if (operations == null && month != null) {
            operations = getOperations(targets, month);
        }
        return new LoaderResult(task, operations);
    }

    private MonthOperationList getOperations(TargetList targets, Calendar month) {
        if (month == null) {
            return null;
        }
        return (new DbOperationReader(dbHelper, targets, month)).read();
    }

    @Override
    public void deliverResult(LoaderResult data) {
        switch (task) {
            case LOAD_ALL:
                report = data.report;
                categories = data.categories;
                targets = data.targets;
                listener.onFinishLoadAll();
                break;
            case LOAD_OPERATIONS:
                operations = data.operations;
                listener.onFinishLoadOperations();
                break;
        }
        task = null;
        super.deliverResult(data);
    }
}
