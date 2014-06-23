package com.micdm.smsgraphs.loaders;

import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.data.OperationReport;
import com.micdm.smsgraphs.data.TargetList;

public class LoaderResult {

    public final OperationReport report;
    public final CategoryList categories;
    public final TargetList targets;
    public final MonthOperationList operations;

    public LoaderResult(OperationReport report, CategoryList categories, TargetList targets, MonthOperationList operations) {
        this.report = report;
        this.categories = categories;
        this.targets = targets;
        this.operations = operations;
    }
}
