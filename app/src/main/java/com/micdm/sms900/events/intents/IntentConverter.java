package com.micdm.sms900.events.intents;

import android.content.Intent;

import com.micdm.sms900.data.Category;
import com.micdm.sms900.data.CategoryList;
import com.micdm.sms900.data.MonthOperationList;
import com.micdm.sms900.data.Operation;
import com.micdm.sms900.data.OperationReport;
import com.micdm.sms900.data.Target;
import com.micdm.sms900.data.TargetList;
import com.micdm.sms900.events.Event;
import com.micdm.sms900.events.EventType;
import com.micdm.sms900.events.events.EditCategoryEvent;
import com.micdm.sms900.events.events.EditTargetEvent;
import com.micdm.sms900.events.events.FinishLoadMessagesEvent;
import com.micdm.sms900.events.events.LoadCategoriesEvent;
import com.micdm.sms900.events.events.LoadOperationReportEvent;
import com.micdm.sms900.events.events.LoadOperationsEvent;
import com.micdm.sms900.events.events.LoadTargetsEvent;
import com.micdm.sms900.events.events.ProgressLoadMessagesEvent;
import com.micdm.sms900.events.events.RequestEditTargetEvent;
import com.micdm.sms900.events.events.RequestLoadCategoriesEvent;
import com.micdm.sms900.events.events.RequestLoadOperationReportEvent;
import com.micdm.sms900.events.events.RequestLoadOperationsEvent;
import com.micdm.sms900.events.events.RequestLoadTargetsEvent;
import com.micdm.sms900.events.events.RequestMonthOperationsEvent;
import com.micdm.sms900.events.events.RequestSelectMonthEvent;
import com.micdm.sms900.events.events.RequestSetOperationIgnoredEvent;
import com.micdm.sms900.events.events.StartLoadMessagesEvent;
import com.micdm.sms900.misc.DateUtils;
import com.micdm.sms900.parcels.CategoryListParcel;
import com.micdm.sms900.parcels.CategoryParcel;
import com.micdm.sms900.parcels.MonthOperationListParcel;
import com.micdm.sms900.parcels.OperationParcel;
import com.micdm.sms900.parcels.OperationReportParcel;
import com.micdm.sms900.parcels.TargetListParcel;
import com.micdm.sms900.parcels.TargetParcel;

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
            case EDIT_CATEGORY:
                return getEditCategoryEvent(intent);
            case REQUEST_EDIT_TARGET:
                return getRequestEditTargetEvent(intent);
            case EDIT_TARGET:
                return getEditTargetEvent(intent);
            case REQUEST_MONTH_OPERATIONS:
                return getRequestMonthOperationsEvent(intent);
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
        String date = intent.getStringExtra("date");
        return new ProgressLoadMessagesEvent(total, current, (date == null) ? null : DateUtils.parseForBundle(date));
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

    private Event getEditCategoryEvent(Intent intent) {
        Category category = ((CategoryParcel) intent.getParcelableExtra("category")).getCategory();
        boolean needRemove = intent.getBooleanExtra("need_remove", false);
        return new EditCategoryEvent(category, needRemove);
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

    private Event getRequestMonthOperationsEvent(Intent intent) {
        DateTime date = DateUtils.parseForBundle(intent.getStringExtra("date"));
        return new RequestMonthOperationsEvent(date);
    }

    private Event getRequestSelectMonthEvent(Intent intent) {
        DateTime current = DateUtils.parseForBundle(intent.getStringExtra("current"));
        return new RequestSelectMonthEvent(current);
    }
}
