package com.micdm.smsgraphs.events.intents;

import android.content.Intent;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;
import com.micdm.smsgraphs.events.events.EditTargetEvent;
import com.micdm.smsgraphs.events.events.FinishLoadMessagesEvent;
import com.micdm.smsgraphs.events.events.LoadCategoriesEvent;
import com.micdm.smsgraphs.events.events.LoadOperationReportEvent;
import com.micdm.smsgraphs.events.events.LoadOperationsEvent;
import com.micdm.smsgraphs.events.events.LoadTargetsEvent;
import com.micdm.smsgraphs.events.events.ProgressLoadMessagesEvent;
import com.micdm.smsgraphs.events.events.StartLoadMessagesEvent;
import com.micdm.smsgraphs.parcels.CategoryListParcel;
import com.micdm.smsgraphs.parcels.MonthOperationListParcel;
import com.micdm.smsgraphs.parcels.OperationReportParcel;
import com.micdm.smsgraphs.parcels.TargetListParcel;

public class IntentConverter {

    public Event convert(Intent intent) {
        switch (getEventTypeFromIntent(intent)) {
            case START_LOAD_MESSAGES:
                return new StartLoadMessagesEvent();
            case PROGRESS_LOAD_MESSAGES:
                return new ProgressLoadMessagesEvent(intent.getIntExtra("total", -1), intent.getIntExtra("current", -1));
            case FINISH_LOAD_MESSAGES:
                return new FinishLoadMessagesEvent();
            case LOAD_OPERATION_REPORT:
                return new LoadOperationReportEvent(((OperationReportParcel) intent.getParcelableExtra("report")).getReport());
            case LOAD_CATEGORIES:
                return new LoadCategoriesEvent(((CategoryListParcel) intent.getParcelableExtra("categories")).getCategories());
            case LOAD_TARGETS:
                return new LoadTargetsEvent(((TargetListParcel) intent.getParcelableExtra("targets")).getTargets());
            case LOAD_OPERATIONS:
                return new LoadOperationsEvent(((MonthOperationListParcel) intent.getParcelableExtra("operations")).getOperations());
            case EDIT_TARGET:
                return new EditTargetEvent();
            default:
                throw new RuntimeException("unknown event type");
        }
    }

    private EventType getEventTypeFromIntent(Intent intent) {
        String action = intent.getAction();
        String[] parts = action.split("\\.");
        String typeName = parts[parts.length - 1];
        for (EventType type: EventType.values()) {
            if (type.toString().equals(typeName)) {
                return type;
            }
        }
        throw new RuntimeException("unknown event type");
    }
}
