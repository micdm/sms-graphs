package com.micdm.smsgraphs.events.intents;

import android.content.Intent;

import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.data.Operation;
import com.micdm.smsgraphs.data.OperationReport;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;
import com.micdm.smsgraphs.events.events.EditTargetEvent;
import com.micdm.smsgraphs.events.events.FinishLoadMessagesEvent;
import com.micdm.smsgraphs.events.events.LoadCategoriesEvent;
import com.micdm.smsgraphs.events.events.LoadOperationReportEvent;
import com.micdm.smsgraphs.events.events.LoadOperationsEvent;
import com.micdm.smsgraphs.events.events.LoadTargetsEvent;
import com.micdm.smsgraphs.events.events.ProgressLoadMessagesEvent;
import com.micdm.smsgraphs.events.events.RequestEditTargetEvent;
import com.micdm.smsgraphs.events.events.RequestLoadCategoriesEvent;
import com.micdm.smsgraphs.events.events.RequestLoadOperationReportEvent;
import com.micdm.smsgraphs.events.events.RequestLoadOperationsEvent;
import com.micdm.smsgraphs.events.events.RequestLoadTargetsEvent;
import com.micdm.smsgraphs.events.events.RequestNextMonthOperationsEvent;
import com.micdm.smsgraphs.events.events.RequestPreviousMonthOperationsEvent;
import com.micdm.smsgraphs.events.events.RequestSelectMonthEvent;
import com.micdm.smsgraphs.events.events.RequestSetOperationIgnoredEvent;
import com.micdm.smsgraphs.events.events.StartLoadMessagesEvent;
import com.micdm.smsgraphs.misc.DateUtils;
import com.micdm.smsgraphs.parcels.CategoryListParcel;
import com.micdm.smsgraphs.parcels.MonthOperationListParcel;
import com.micdm.smsgraphs.parcels.OperationParcel;
import com.micdm.smsgraphs.parcels.OperationReportParcel;
import com.micdm.smsgraphs.parcels.TargetListParcel;
import com.micdm.smsgraphs.parcels.TargetParcel;

import org.joda.time.DateTime;

public class IntentConverter {

    public Event convert(Intent intent) {
        switch (getEventTypeFromIntent(intent)) {
            case START_LOAD_MESSAGES:
                return new StartLoadMessagesEvent();
            case PROGRESS_LOAD_MESSAGES:
                return getProgressLoadMessagesEvent(intent);
            case FINISH_LOAD_MESSAGES:
                return new FinishLoadMessagesEvent();
            case REQUEST_LOAD_OPERATION_REPORT:
                return new RequestLoadOperationReportEvent();
            case LOAD_OPERATION_REPORT:
                return getLoadOperationReportEvent(intent);
            case REQUEST_LOAD_CATEGORIES:
                return new RequestLoadCategoriesEvent();
            case LOAD_CATEGORIES:
                return getLoadCategoriesEvent(intent);
            case REQUEST_LOAD_TARGETS:
                return new RequestLoadTargetsEvent();
            case LOAD_TARGETS:
                return getLoadTargetsEvent(intent);
            case REQUEST_LOAD_OPERATIONS:
                return getRequestLoadOperationsEvent(intent);
            case LOAD_OPERATIONS:
                return getLoadOperationsEvent(intent);
            case REQUEST_SET_OPERATION_IGNORED:
                return getRequestSetOperationIgnoredEvent(intent);
            case REQUEST_EDIT_TARGET:
                return getRequestEditTargetEvent(intent);
            case EDIT_TARGET:
                return getEditTargetEvent(intent);
            case REQUEST_PREVIOUS_MONTH_OPERATIONS:
                return new RequestPreviousMonthOperationsEvent();
            case REQUEST_NEXT_MONTH_OPERATIONS:
                return new RequestNextMonthOperationsEvent();
            case REQUEST_SELECT_MONTH:
                return getRequestSelectMonthEvent(intent);
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

    private Event getProgressLoadMessagesEvent(Intent intent) {
        int total = intent.getIntExtra("total", -1);
        int current = intent.getIntExtra("current", -1);
        return new ProgressLoadMessagesEvent(total, current);
    }

    private Event getLoadOperationReportEvent(Intent intent) {
        OperationReport report = ((OperationReportParcel) intent.getParcelableExtra("report")).getReport();
        return new LoadOperationReportEvent(report);
    }

    private Event getLoadCategoriesEvent(Intent intent) {
        CategoryList categories = ((CategoryListParcel) intent.getParcelableExtra("categories")).getCategories();
        return new LoadCategoriesEvent(categories);
    }

    private Event getLoadTargetsEvent(Intent intent) {
        TargetList targets = ((TargetListParcel) intent.getParcelableExtra("targets")).getTargets();
        return new LoadTargetsEvent(targets);
    }

    private Event getRequestLoadOperationsEvent(Intent intent) {
        DateTime date = DateUtils.parseForBundle(intent.getStringExtra("date"));
        return new RequestLoadOperationsEvent(date);
    }

    private Event getLoadOperationsEvent(Intent intent) {
        MonthOperationList operations = ((MonthOperationListParcel) intent.getParcelableExtra("operations")).getOperations();
        return new LoadOperationsEvent(operations);
    }

    private Event getRequestSetOperationIgnoredEvent(Intent intent) {
        Operation operation = ((OperationParcel) intent.getParcelableExtra("operation")).getOperation();
        boolean needIgnore = intent.getBooleanExtra("need_ignore", false);
        return new RequestSetOperationIgnoredEvent(operation, needIgnore);
    }

    private Event getRequestEditTargetEvent(Intent intent) {
        Target target = ((TargetParcel) intent.getParcelableExtra("target")).getTarget();
        return new RequestEditTargetEvent(target);
    }

    private Event getEditTargetEvent(Intent intent) {
        Target target = ((TargetParcel) intent.getParcelableExtra("target")).getTarget();
        boolean needEditNext = intent.getBooleanExtra("need_edit_next", false);
        return new EditTargetEvent(target, needEditNext);
    }

    private Event getRequestSelectMonthEvent(Intent intent) {
        DateTime current = DateUtils.parseForBundle(intent.getStringExtra("current"));
        return new RequestSelectMonthEvent(current);
    }
}
