package com.micdm.smsgraphs.events.intents;

import android.content.Context;
import android.content.Intent;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;
import com.micdm.smsgraphs.events.events.LoadCategoriesEvent;
import com.micdm.smsgraphs.events.events.LoadOperationReportEvent;
import com.micdm.smsgraphs.events.events.LoadOperationsEvent;
import com.micdm.smsgraphs.events.events.LoadTargetsEvent;
import com.micdm.smsgraphs.events.events.ProgressLoadMessagesEvent;
import com.micdm.smsgraphs.parcels.CategoryListParcel;
import com.micdm.smsgraphs.parcels.MonthOperationListParcel;
import com.micdm.smsgraphs.parcels.OperationReportParcel;
import com.micdm.smsgraphs.parcels.TargetListParcel;

public class EventConverter {

    private final Context context;

    public EventConverter(Context context) {
        this.context = context;
    }

    public Intent convert(Event event) {
        Intent intent = new Intent(getIntentAction(event.getType()));
        switch (event.getType()) {
            case PROGRESS_LOAD_MESSAGES:
                intent.putExtra("total", ((ProgressLoadMessagesEvent) event).getTotal());
                intent.putExtra("current", ((ProgressLoadMessagesEvent) event).getCurrent());
                break;
            case LOAD_OPERATION_REPORT:
                intent.putExtra("report", new OperationReportParcel(((LoadOperationReportEvent) event).getReport()));
                break;
            case LOAD_CATEGORIES:
                intent.putExtra("categories", new CategoryListParcel(((LoadCategoriesEvent) event).getCategories()));
                break;
            case LOAD_TARGETS:
                intent.putExtra("targets", new TargetListParcel(((LoadTargetsEvent) event).getTargets()));
                break;
            case LOAD_OPERATIONS:
                intent.putExtra("operations", new MonthOperationListParcel(((LoadOperationsEvent) event).getOperations()));
                break;
        }
        return intent;
    }

    public String getIntentAction(EventType type) {
        return String.format("%s.event.%s", context.getPackageName(), type);
    }
}
