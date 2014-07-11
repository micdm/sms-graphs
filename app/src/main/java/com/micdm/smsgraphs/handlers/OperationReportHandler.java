package com.micdm.smsgraphs.handlers;

import com.micdm.smsgraphs.data.OperationReport;
import com.micdm.utils.events.EventListener;

public interface OperationReportHandler {

    public static interface OnLoadOperationReportListener extends EventListener {
        public void onLoadOperationReport(OperationReport report);
    }

    public void addOnLoadOperationReportListener(OnLoadOperationReportListener listener);
    public void removeOnLoadOperationReportListener(OnLoadOperationReportListener listener);
}
