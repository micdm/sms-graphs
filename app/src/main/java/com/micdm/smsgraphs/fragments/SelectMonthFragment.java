package com.micdm.smsgraphs.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.micdm.smsgraphs.CustomApplication;
import com.micdm.smsgraphs.R;
import com.micdm.smsgraphs.events.EventManager;
import com.micdm.smsgraphs.events.events.RequestMonthOperationsEvent;
import com.micdm.smsgraphs.misc.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

public class SelectMonthFragment extends DialogFragment {

    private class YearSpinnerAdapter extends BaseAdapter {

        private final DateTime _min;
        private final DateTime _max;

        public YearSpinnerAdapter(DateTime min, DateTime max) {
            _min = min;
            _max = max;
        }

        @Override
        public int getCount() {
            return (_min == null || _max == null) ? 0 : _max.getYear() - _min.getYear() + 1;
        }

        @Override
        public DateTime getItem(int position) {
            return _min.plusYears(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getMillis();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = View.inflate(getActivity(), R.layout.v__select_month__list_item, null);
            }
            DateTime date = getItem(position);
            ((TextView) view).setText(String.valueOf(date.getYear()));
            return view;
        }

        public int getYearPosition(int year) {
            return year - _min.getYear();
        }
    }

    private class MonthSpinnerAdapter extends BaseAdapter {

        private final DateTime _min;
        private final DateTime _max;
        private DateTime _begin;
        private DateTime _end;

        public MonthSpinnerAdapter(DateTime min, DateTime max) {
            _min = min;
            _max = max;
        }

        public void setYear(int year) {
            _begin = new DateTime(year, DateTimeConstants.JANUARY, 1, 0, 0);
            if (_begin.isBefore(_min)) {
                _begin = _min;
            }
            _end = new DateTime(year, DateTimeConstants.DECEMBER, 1, 0, 0);
            if (_end.isAfter(_max)) {
                _end = _max;
            }
        }

        @Override
        public int getCount() {
            return (_begin == null || _end == null) ? 0 : _end.getMonthOfYear() - _begin.getMonthOfYear() + 1;
        }

        @Override
        public DateTime getItem(int position) {
            return _begin.plusMonths(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getMillis();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = View.inflate(getActivity(), R.layout.v__select_month__list_item, null);
            }
            DateTime date = getItem(position);
            ((TextView) view).setText(DateUtils.formatMonthForHuman(date, true));
            return view;
        }

        public int getMonthPosition(int month) {
            return month - _begin.getMonthOfYear();
        }
    }

    public static final String INIT_ARG_MIN_DATE = "min_date";
    public static final String INIT_ARG_MAX_DATE = "max_date";
    public static final String INIT_ARG_CURRENT_DATE = "current_date";

    private Spinner _monthView;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.fragment_select_month_title);
        builder.setView(getDialogView());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DateTime date = (DateTime) _monthView.getSelectedItem();
                getEventManager().publish(new RequestMonthOperationsEvent(date));
            }
        });
        return builder.create();
    }

    private View getDialogView() {
        View view = View.inflate(getActivity(), R.layout.f__select_month, null);
        DateTime minDate = getMinDate();
        DateTime maxDate = getMaxDate();
        DateTime currentDate = getCurrentDate();
        Spinner yearView = setupYearView(view, minDate, maxDate, currentDate);
        _monthView = setupMonthView(view, minDate, maxDate, currentDate);
        yearView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MonthSpinnerAdapter adapter = (MonthSpinnerAdapter) _monthView.getAdapter();
                int year = ((YearSpinnerAdapter) parent.getAdapter()).getItem(position).getYear();
                adapter.setYear(year);
                adapter.notifyDataSetChanged();
                _monthView.setSelection(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        return view;
    }

    private Spinner setupYearView(View view, DateTime minDate, DateTime maxDate, DateTime currentDate) {
        Spinner yearView = (Spinner) view.findViewById(R.id.f__select_month__year);
        YearSpinnerAdapter yearSpinnerAdapter = new YearSpinnerAdapter(minDate, maxDate);
        yearView.setAdapter(yearSpinnerAdapter);
        yearView.setSelection(yearSpinnerAdapter.getYearPosition(currentDate.getYear()), false);
        return yearView;
    }

    private Spinner setupMonthView(View view, DateTime minDate, DateTime maxDate, DateTime currentDate) {
        Spinner monthView = (Spinner) view.findViewById(R.id.f__select_month__month);
        MonthSpinnerAdapter monthSpinnerAdapter = new MonthSpinnerAdapter(minDate, maxDate);
        monthSpinnerAdapter.setYear(currentDate.getYear());
        monthView.setAdapter(monthSpinnerAdapter);
        monthView.setSelection(monthSpinnerAdapter.getMonthPosition(currentDate.getMonthOfYear()), false);
        return monthView;
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
