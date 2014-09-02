package com.micdm.sms900.fragments;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.micdm.sms900.CustomApplication;
import com.micdm.sms900.R;
import com.micdm.sms900.data.Category;
import com.micdm.sms900.data.CategoryStat;
import com.micdm.sms900.data.MonthOperationList;
import com.micdm.sms900.data.Operation;
import com.micdm.sms900.data.Target;
import com.micdm.sms900.data.TargetStat;
import com.micdm.sms900.events.EventManager;
import com.micdm.sms900.events.EventType;
import com.micdm.sms900.events.events.LoadOperationsEvent;
import com.micdm.sms900.events.events.RequestLoadOperationsEvent;
import com.micdm.sms900.events.events.RequestMonthOperationsEvent;
import com.micdm.sms900.events.events.RequestSelectMonthEvent;
import com.micdm.sms900.misc.DateUtils;
import com.micdm.sms900.misc.PercentageView;
import com.micdm.sms900.parcels.TargetParcel;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// TODO: когда приходит новое сообщение, списки схлопываются
public class MonthStatsFragment extends Fragment {

    private class CategoryStatsListAdapter extends BaseExpandableListAdapter {

        private List<CategoryStat> _stats;

        public void setStats(List<CategoryStat> stats) {
            _stats = stats;
        }

        @Override
        public int getGroupCount() {
            return (_stats == null) ? 0 : _stats.size();
        }

        @Override
        public int getChildrenCount(int position) {
            return getGroup(position).getStats().size();
        }

        @Override
        public CategoryStat getGroup(int position) {
            return _stats.get(position);
        }

        @Override
        public TargetStat getChild(int groupPosition, int childPosition) {
            return getGroup(groupPosition).getStats().get(childPosition);
        }

        @Override
        public long getGroupId(int position) {
            return getGroup(position).getCategory().getId();
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return getChild(groupPosition, childPosition).getTarget().getId();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int position, boolean isExpanded, View view, ViewGroup parent) {
            if (view == null) {
                view = View.inflate(getActivity(), R.layout.v__stats__list_item_category, null);
            }
            CategoryStat stat = getGroup(position);
            ((PercentageView) view).setPercentage(stat.getPercentage());
            TextView nameView = (TextView) view.findViewById(R.id.v__stats__list_item_category__name);
            nameView.setText(stat.getCategory().getName());
            TextView amountView = (TextView) view.findViewById(R.id.v__stats__list_item_category__amount);
            int amount = stat.getAmount();
            if (amount == 0) {
                setStrikethroughTextStyle(nameView);
                amountView.setVisibility(View.GONE);
            } else {
                setNormalTextStyle(nameView);
                amountView.setText(String.valueOf(amount));
                amountView.setVisibility(View.VISIBLE);
            }
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
            if (view == null) {
                view = View.inflate(getActivity(), R.layout.v__stats__list_item_target, null);
            }
            TargetStat stat = getChild(groupPosition, childPosition);
            ((PercentageView) view).setPercentage(stat.getPercentage());
            TextView nameView = (TextView) view.findViewById(R.id.v__stats__list_item_target__name);
            Target target = stat.getTarget();
            nameView.setText(target.getPrettyTitle());
            TextView amountView = (TextView) view.findViewById(R.id.v__stats__list_item_target__amount);
            int amount = stat.getAmount();
            if (amount == 0) {
                setStrikethroughTextStyle(nameView);
                amountView.setVisibility(View.GONE);
            } else {
                setNormalTextStyle(nameView);
                amountView.setText(String.valueOf(amount));
                amountView.setVisibility(View.VISIBLE);
            }
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private void setStrikethroughTextStyle(TextView view) {
            view.setPaintFlags(view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        private void setNormalTextStyle(TextView view) {
            view.setPaintFlags(view.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    public static final String INIT_ARG_IS_FIRST = "is_first";
    public static final String INIT_ARG_IS_LAST = "is_last";
    public static final String INIT_ARG_DATE = "date";

    private static final String FRAGMENT_OPERATIONS_TAG = "operations";

    private boolean _isFirst;
    private boolean _isLast;
    private DateTime _date;

    private ExpandableListView _categoriesView;
    private TextView _totalView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        handleInitArguments();
    }

    private void handleInitArguments() {
        Bundle args = getArguments();
        _isFirst = args.getBoolean(INIT_ARG_IS_FIRST);
        _isLast = args.getBoolean(INIT_ARG_IS_LAST);
        _date = DateUtils.parseForBundle(args.getString(INIT_ARG_DATE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f__month_stats, null);
        View previousView = view.findViewById(R.id.f__month_stats__previous);
        previousView.setVisibility(_isFirst ? View.INVISIBLE : View.VISIBLE);
        previousView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEventManager().publish(new RequestMonthOperationsEvent(_date.minusMonths(1)));
            }
        });
        View nextView = view.findViewById(R.id.f__month_stats__next);
        nextView.setVisibility(_isLast ? View.INVISIBLE : View.VISIBLE);
        nextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEventManager().publish(new RequestMonthOperationsEvent(_date.plusMonths(1)));
            }
        });
        TextView monthView = (TextView) view.findViewById(R.id.f__month_stats__month);
        monthView.setText(DateUtils.formatMonthForHuman(_date));
        monthView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEventManager().publish(new RequestSelectMonthEvent(_date));
            }
        });
        _categoriesView = (ExpandableListView) view.findViewById(R.id.f__month_stats__categories);
        _categoriesView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CategoryStatsListAdapter adapter = (CategoryStatsListAdapter) parent.getExpandableListAdapter();
                TargetStat stat = adapter.getChild(groupPosition, childPosition);
                FragmentManager manager = getChildFragmentManager();
                if (manager.findFragmentByTag(FRAGMENT_OPERATIONS_TAG) == null) {
                    OperationsFragment fragment = new OperationsFragment();
                    fragment.setArguments(getOperationsFragmentArguments(stat.getTarget()));
                    fragment.show(manager, FRAGMENT_OPERATIONS_TAG);
                }
                return true;
            }
        });
        _totalView = (TextView) view.findViewById(R.id.f__month_stats__total);
        return view;
    }

    private Bundle getOperationsFragmentArguments(Target target) {
        Bundle arguments = new Bundle();
        arguments.putString(OperationsFragment.INIT_ARG_DATE, DateUtils.formatForBundle(_date));
        arguments.putParcelable(OperationsFragment.INIT_ARG_TARGET, new TargetParcel(target));
        return arguments;
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
                List<CategoryStat> stats = getCategoryStats(operations.getOperations());
                CategoryStatsListAdapter adapter = (CategoryStatsListAdapter) _categoriesView.getExpandableListAdapter();
                if (adapter == null) {
                    adapter = new CategoryStatsListAdapter();
                    _categoriesView.setAdapter(adapter);
                }
                adapter.setStats(stats);
                adapter.notifyDataSetChanged();
                _totalView.setText(String.valueOf(getTotalSum(stats)));
            }
        });
    }

    private EventManager getEventManager() {
        return ((CustomApplication) getActivity().getApplication()).getEventManager();
    }

    private List<CategoryStat> getCategoryStats(List<Operation> operations) {
        List<CategoryStat> stats = new ArrayList<CategoryStat>();
        Category noCategory = new Category(0, getString(R.string.no_category));
        for (Operation operation: operations) {
            Target target = operation.getTarget();
            Category category = target.getCategory();
            if (category == null) {
                category = noCategory;
            }
            CategoryStat categoryStat = updateCategoryStat(stats, category, operation);
            updateTargetStat(categoryStat.getStats(), target, operation);
        }
        addPercentages(stats);
        sortByName(stats);
        return stats;
    }

    private CategoryStat updateCategoryStat(List<CategoryStat> stats, Category category, Operation operation) {
        CategoryStat stat = getCategoryStat(stats, category);
        if (stat == null) {
            stat = new CategoryStat(category);
            stats.add(stat);
        }
        if (!operation.isIgnored()) {
            stat.setAmount(stat.getAmount() + operation.getAmount());
        }
        return stat;
    }

    private CategoryStat getCategoryStat(List<CategoryStat> stats, Category category) {
        for (CategoryStat stat: stats) {
            if (stat.getCategory().getId() == category.getId()) {
                return stat;
            }
        }
        return null;
    }

    private void updateTargetStat(List<TargetStat> stats, Target target, Operation operation) {
        TargetStat targetStat = getTargetStat(stats, target);
        if (targetStat == null) {
            targetStat = new TargetStat(target);
            stats.add(targetStat);
        }
        if (!operation.isIgnored()) {
            targetStat.setAmount(targetStat.getAmount() + operation.getAmount());
        }
    }

    public TargetStat getTargetStat(List<TargetStat> stats, Target target) {
        for (TargetStat stat: stats) {
            if (stat.getTarget().getId() == target.getId()) {
                return stat;
            }
        }
        return null;
    }

    private void addPercentages(List<CategoryStat> stats) {
        int total = getTotalSum(stats);
        if (total == 0) {
            return;
        }
        for (CategoryStat categoryStat: stats) {
            int categoryAmount = categoryStat.getAmount();
            if (categoryAmount == 0) {
                continue;
            }
            categoryStat.setPercentage((double) categoryAmount / total);
            for (TargetStat targetStat: categoryStat.getStats()) {
                int targetAmount = targetStat.getAmount();
                if (targetAmount != 0) {
                    targetStat.setPercentage((double) targetAmount / categoryAmount);
                }
            }
        }
    }

    private int getTotalSum(List<CategoryStat> stats) {
        int total = 0;
        for (CategoryStat stat: stats) {
            total += stat.getAmount();
        }
        return total;
    }

    private void sortByName(List<CategoryStat> stats) {
        Collections.sort(stats, new Comparator<CategoryStat>() {
            @Override
            public int compare(CategoryStat a, CategoryStat b) {
                return a.getCategory().getName().compareTo(b.getCategory().getName());
            }
        });
        for (CategoryStat categoryStat: stats) {
            Collections.sort(categoryStat.getStats(), new Comparator<TargetStat>() {
                @Override
                public int compare(TargetStat a, TargetStat b) {
                    return a.getTarget().getName().compareTo(b.getTarget().getName());
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getEventManager().unsubscribeAll(this);
    }
}
