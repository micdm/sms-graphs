package com.micdm.sms900.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.micdm.sms900.CustomApplication;
import com.micdm.sms900.R;
import com.micdm.sms900.data.MonthOperationList;
import com.micdm.sms900.data.Operation;
import com.micdm.sms900.data.OperationStat;
import com.micdm.sms900.data.Target;
import com.micdm.sms900.events.EventManager;
import com.micdm.sms900.events.EventType;
import com.micdm.sms900.events.events.LoadOperationsEvent;
import com.micdm.sms900.events.events.RequestLoadOperationsEvent;
import com.micdm.sms900.events.events.RequestSetOperationIgnoredEvent;
import com.micdm.sms900.misc.DateUtils;
import com.micdm.sms900.misc.PercentageView;
import com.micdm.sms900.parcels.TargetParcel;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class OperationsFragment extends DialogFragment {

    private class OperationListAdapter extends BaseAdapter {

        private List<OperationStat> _stats;

        public void setStats(List<OperationStat> stats) {
            _stats = stats;
        }

        @Override
        public int getCount() {
            return (_stats == null) ? 0 : _stats.size();
        }

        @Override
        public OperationStat getItem(int position) {
            return _stats.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getOperation().getId();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = View.inflate(getActivity(), R.layout.v__operations__list_item, null);
            }
            OperationStat stat = getItem(position);
            Operation operation = stat.getOperation();
            ((PercentageView) view).setPercentage(stat.getPercentage());
            TextView createdView = (TextView) view.findViewById(R.id.v__operations__list_item__created);
            createdView.setText(DateUtils.formatForHuman(operation.getCreated()));
            TextView amountView = (TextView) view.findViewById(R.id.v__operations__list_item__amount);
            amountView.setText(String.valueOf(operation.getAmount()));
            if (operation.isIgnored()) {
                setStrikethroughTextStyle(createdView);
                setStrikethroughTextStyle(amountView);
            } else {
                setNormalTextStyle(createdView);
                setNormalTextStyle(amountView);
            }
            return view;
        }

        private void setStrikethroughTextStyle(TextView view) {
            view.setPaintFlags(view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        private void setNormalTextStyle(TextView view) {
            view.setPaintFlags(view.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    public static final String INIT_ARG_DATE = "date";
    public static final String INIT_ARG_TARGET = "target";

    private DateTime _date;
    private Target _target;

    private ListView _operationsView;
    private View _averageView;
    private TextView _averageValueView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        handleInitArguments();
    }

    private void handleInitArguments() {
        Bundle args = getArguments();
        _date = DateUtils.parseForBundle(args.getString(INIT_ARG_DATE));
        _target = ((TargetParcel) args.getParcelable(INIT_ARG_TARGET)).getTarget();
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.fragment_operations_title, DateUtils.formatMonthForHuman(_date).toLowerCase(), _target.getPrettyTitle()));
        View view = View.inflate(getActivity(), R.layout.f__operations, null);
        _operationsView = (ListView) view.findViewById(R.id.f__operations__operations);
        _operationsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OperationStat stat = ((OperationListAdapter) parent.getAdapter()).getItem(position);
                Operation operation = stat.getOperation();
                getEventManager().publish(new RequestSetOperationIgnoredEvent(operation, !operation.isIgnored()));
            }
        });
        _averageView = view.findViewById(R.id.f__operations__average);
        _averageValueView = (TextView) view.findViewById(R.id.f__operations__average_value);
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        subscribeForEvents();
        getEventManager().publish(new RequestLoadOperationsEvent(_date));
    }

    private void subscribeForEvents() {
        EventManager manager = getEventManager();
        manager.subscribe(this, EventType.LOAD_OPERATIONS, new EventManager.OnEventListener<LoadOperationsEvent>() {
            @Override
            public void onEvent(LoadOperationsEvent event) {
                MonthOperationList operations = event.getOperations();
                if (!operations.getMonth().equals(_date)) {
                    return;
                }
                List<Operation> allOperations = getTargetOperations(operations.getOperations());
                List<Operation> notIgnoredOperations = getNotIgnoredOperations(allOperations);
                OperationListAdapter adapter = (OperationListAdapter) _operationsView.getAdapter();
                if (adapter == null) {
                    adapter = new OperationListAdapter();
                    _operationsView.setAdapter(adapter);
                }
                adapter.setStats(getOperationStats(allOperations, notIgnoredOperations));
                adapter.notifyDataSetChanged();
                Integer average = getAverage(notIgnoredOperations);
                if (average != null) {
                    _averageValueView.setText(String.valueOf(average));
                    _averageView.setVisibility(View.VISIBLE);
                } else {
                    _averageView.setVisibility(View.GONE);
                }
            }
        });
    }

    private EventManager getEventManager() {
        return ((CustomApplication) getActivity().getApplication()).getEventManager();
    }

    private List<Operation> getTargetOperations(List<Operation> operations) {
        List<Operation> filtered = new ArrayList<Operation>();
        for (Operation operation: operations) {
            Target target = operation.getTarget();
            if (target != null && target.getId() == _target.getId()) {
                filtered.add(operation);
            }
        }
        return filtered;
    }

    private List<Operation> getNotIgnoredOperations(List<Operation> operations) {
        List<Operation> filtered = new ArrayList<Operation>();
        for (Operation operation: operations) {
            if (!operation.isIgnored()) {
                filtered.add(operation);
            }
        }
        return filtered;
    }

    private List<OperationStat> getOperationStats(List<Operation> allOperations, List<Operation> notIgnoredOperations) {
        int amount = getTargetAmount(notIgnoredOperations);
        List<OperationStat> stats = new ArrayList<OperationStat>();
        for (Operation operation: allOperations) {
            double percentage = operation.isIgnored() ? 0 : (double) operation.getAmount() / amount;
            stats.add(new OperationStat(operation, percentage));
        }
        return stats;
    }

    private int getTargetAmount(List<Operation> operations) {
        int amount = 0;
        for (Operation operation: operations) {
            amount += operation.getAmount();
        }
        return amount;
    }

    private Integer getAverage(List<Operation> operations) {
        return (operations.size() < 2) ? null : getTargetAmount(operations) / operations.size();
    }

    @Override
    public void onStop() {
        super.onStop();
        getEventManager().unsubscribeAll(this);
    }
}
