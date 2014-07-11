package com.micdm.smsgraphs.handlers;

import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.utils.events.EventListener;

import org.joda.time.DateTime;

public interface OperationHandler {

    public static interface OnLoadOperationsListener extends EventListener {
        public void onLoadOperations(MonthOperationList operations);
    }

    public void loadOperations(DateTime date);
    public void addOnLoadOperationsListener(OnLoadOperationsListener listener);
    public void removeOnLoadOperationsListener(OnLoadOperationsListener listener);
}
