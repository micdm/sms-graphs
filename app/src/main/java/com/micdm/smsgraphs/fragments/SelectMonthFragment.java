package com.micdm.smsgraphs.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.micdm.smsgraphs.CustomApplication;
import com.micdm.smsgraphs.events.EventManager;
import com.micdm.smsgraphs.events.events.RequestLoadOperationsEvent;
import com.micdm.smsgraphs.misc.DateUtils;

import org.joda.time.DateTime;

public class SelectMonthFragment extends DialogFragment {

    public static final String INIT_ARG_MIN_DATE = "min_date";
    public static final String INIT_ARG_MAX_DATE = "max_date";
    public static final String INIT_ARG_CURRENT_DATE = "current_date";

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DateTime current = getCurrentDate();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                getEventManager().publish(new RequestLoadOperationsEvent(new DateTime(year, monthOfYear + 1, 1, 0, 0)));
            }
        }, current.getYear(), current.getMonthOfYear() - 1, 1);
        DatePicker picker = dialog.getDatePicker();
        picker.setMinDate(getMinDate().getMillis());
        picker.setMaxDate(getMaxDate().getMillis());
        return dialog;
    }

    private DateTime getMinDate() {
        return DateUtils.parseForBundle(getArguments().getString(INIT_ARG_MIN_DATE));
    }

    private DateTime getMaxDate() {
        return DateUtils.parseForBundle(getArguments().getString(INIT_ARG_MAX_DATE));
    }

    private DateTime getCurrentDate() {
        return DateUtils.parseForBundle(getArguments().getString(INIT_ARG_CURRENT_DATE));
    }

    private EventManager getEventManager() {
        return ((CustomApplication) getActivity().getApplication()).getEventManager();
    }
}
