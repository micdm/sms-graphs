package com.micdm.smsgraphs.loaders;

import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.data.OperationReport;
import com.micdm.smsgraphs.data.TargetList;

public class LoaderResult {

    public final DataLoader.Task task;
    public final OperationReport report;
    public final CategoryList categories;
    public final TargetList targets;
    public final MonthOperationList operations;

    public LoaderResult(DataLoader.Task task, OperationReport report, CategoryList categories, TargetList targets) {
        this(task, report, categories, targets, null);
    }

    public LoaderResult(DataLoader.Task task, MonthOperationList operations) {
        this(task, null, null, null, operations);
    }

    public LoaderResult(DataLoader.Task task, OperationReport report, CategoryList categories, TargetList targets, MonthOperationList operations) {
        this.task = task;
        this.report = report;
        this.categories = categories;
        this.targets = targets;
        this.operations = operations;
    }
}
