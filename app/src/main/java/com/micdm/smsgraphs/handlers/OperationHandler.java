package com.micdm.smsgraphs.handlers;

import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.utils.events.EventListener;

import java.util.Calendar;

public interface OperationHandler {

    public static interface OnLoadOperationsListener extends EventListener {
        public void onStartLoadOperations(Calendar month);
        public void onFinishLoadOperations();
        public void onLoadOperations(MonthOperationList operations, boolean previous, boolean next);
    }

    public void loadPreviousMonthOperations();
    public void loadNextMonthOperations();
    public void addOnLoadOperationsListener(OnLoadOperationsListener listener);
    public void removeOnLoadOperationsListener(OnLoadOperationsListener listener);
}
