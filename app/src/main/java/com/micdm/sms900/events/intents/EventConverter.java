package com.micdm.sms900.events.intents;

import android.content.Context;
import android.content.Intent;

import com.micdm.sms900.events.Event;
import com.micdm.sms900.events.EventType;
import com.micdm.sms900.events.events.EditCategoryEvent;
import com.micdm.sms900.events.events.EditTargetEvent;
import com.micdm.sms900.events.events.LoadCategoriesEvent;
import com.micdm.sms900.events.events.LoadOperationReportEvent;
import com.micdm.sms900.events.events.LoadOperationsEvent;
import com.micdm.sms900.events.events.LoadTargetsEvent;
import com.micdm.sms900.events.events.ProgressLoadMessagesEvent;
import com.micdm.sms900.events.events.RequestEditTargetEvent;
import com.micdm.sms900.events.events.RequestLoadOperationsEvent;
import com.micdm.sms900.events.events.RequestMonthOperationsEvent;
import com.micdm.sms900.events.events.RequestSelectMonthEvent;
import com.micdm.sms900.events.events.RequestSetOperationIgnoredEvent;
import com.micdm.sms900.misc.DateUtils;
import com.micdm.sms900.parcels.CategoryListParcel;
import com.micdm.sms900.parcels.CategoryParcel;
import com.micdm.sms900.parcels.MonthOperationListParcel;
import com.micdm.sms900.parcels.OperationParcel;
import com.micdm.sms900.parcels.OperationReportParcel;
import com.micdm.sms900.parcels.TargetListParcel;
import com.micdm.sms900.parcels.TargetParcel;

import org.joda.time.DateTime;

public class EventConverter {

    private final Context _context;

    public EventConverter(Context context) {
        _context = context;
    }

    public Intent convert(Event event) {
        Intent intent = new Intent(getIntentAction(event.getType()));
        switch (event.getType()) {
            case PROGRESS_LOAD_MESSAGES:
                intent.putExtra("total", ((ProgressLoadMessagesEvent) event).getTotal());
                intent.putExtra("current", ((ProgressLoadMessagesEvent) event).getCurrent());
                DateTime date = ((ProgressLoadMessagesEvent) event).getDate();
                intent.putExtra("date", (date == null) ? null : DateUtils.formatForBundle(date));
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
            case REQUEST_LOAD_OPERATIONS:
                intent.putExtra("date", DateUtils.formatForBundle(((RequestLoadOperationsEvent) event).getDate()));
                break;
            case LOAD_OPERATIONS:
                intent.putExtra("operations", new MonthOperationListParcel(((LoadOperationsEvent) event).getOperations()));
                break;
            case REQUEST_SET_OPERATION_IGNORED:
                intent.putExtra("operation", new OperationParcel(((RequestSetOperationIgnoredEvent) event).getOperation()));
                intent.putExtra("need_ignore", ((RequestSetOperationIgnoredEvent) event).needIgnore());
                break;
            case EDIT_CATEGORY:
                intent.putExtra("category", new CategoryParcel(((EditCategoryEvent) event).getCategory()));
                intent.putExtra("need_remove", ((EditCategoryEvent) event).needRemove());
                break;
            case REQUEST_EDIT_TARGET:
                intent.putExtra("target", new TargetParcel(((RequestEditTargetEvent) event).getTarget()));
                break;
            case EDIT_TARGET:
                intent.putExtra("target", new TargetParcel(((EditTargetEvent) event).getTarget()));
                intent.putExtra("need_edit_next", ((EditTargetEvent) event).needEditNext());
                break;
            case REQUEST_MONTH_OPERATIONS:
                intent.putExtra("date", DateUtils.formatForBundle(((RequestMonthOperationsEvent) event).getDate()));
                break;
            case REQUEST_SELECT_MONTH:
                intent.putExtra("current", DateUtils.formatForBundle(((RequestSelectMonthEvent) event).getCurrent()));
                break;
        }
        return intent;
    }

    public String getIntentAction(EventType type) {
        return String.format("%s.event.%s", _context.getPackageName(), type);
    }
}
