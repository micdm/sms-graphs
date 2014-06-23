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

    private final DbHelper dbHelper;
    private final OnLoadListener listener;

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
        report = null;
        targets = null;
        operations = null;
        forceLoad();
    }

    public void reloadOperations(Calendar month) {
        this.month = month;
        this.operations = null;
        forceLoad();
    }

    @Override
    public void onForceLoad() {
        if (report == null && targets == null && operations == null) {
            listener.onStartLoadAll();
        }
        if (operations == null) {
            listener.onStartLoadOperations(month);
        }
        super.onForceLoad();
    }

    @Override
    public LoaderResult loadInBackground() {
        OperationReport report = this.report;
        Calendar month = this.month;
        if (report == null) {
            report = getReport();
            if (month == null) {
                month = getLastMonth(report);
            }
        }
        CategoryList categories = this.categories;
        if (categories == null) {
            categories = getCategories();
        }
        TargetList targets = this.targets;
        if (targets == null) {
            targets = getTargets(categories);
        }
        MonthOperationList operations = this.operations;
        if (operations == null) {
            operations = getOperations(targets, month);
        }
        return new LoaderResult(report, categories, targets, operations);
    }

    private Calendar getLastMonth(OperationReport report) {
        if (report.last == null) {
            return null;
        }
        Calendar month = (Calendar) report.last.clone();
        month.set(Calendar.DAY_OF_MONTH, 1);
        return month;
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

    private MonthOperationList getOperations(TargetList targets, Calendar month) {
        if (month == null) {
            return null;
        }
        return (new DbOperationReader(dbHelper, targets, month)).read();
    }

    @Override
    public void deliverResult(LoaderResult data) {
        if (report == null && targets == null && operations == null) {
            listener.onFinishLoadAll();
        }
        if (operations == null) {
            listener.onFinishLoadOperations();
        }
        report = data.report;
        categories = data.categories;
        targets = data.targets;
        operations = data.operations;
        super.deliverResult(data);
    }
}
