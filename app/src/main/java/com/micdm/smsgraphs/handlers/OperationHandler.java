package com.micdm.smsgraphs.handlers;

import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.data.Operation;
import com.micdm.utils.events.EventListener;

import java.util.Calendar;
import java.util.List;

public interface OperationHandler {

    public static interface OnLoadOperationsListener extends EventListener {
        public void onLoadOperations(MonthOperationList operations);
    }

    public void addOnLoadOperationsListener(OnLoadOperationsListener listener);
    public void removeOnLoadOperationsListener(OnLoadOperationsListener listener);
}
