package com.micdm.smsgraphs.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.micdm.smsgraphs.CustomApplication;
import com.micdm.smsgraphs.R;
import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.data.CategoryStat;
import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.data.Operation;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetStat;
import com.micdm.smsgraphs.events.EventManager;
import com.micdm.smsgraphs.events.EventType;
import com.micdm.smsgraphs.events.events.LoadOperationsEvent;
import com.micdm.smsgraphs.handlers.OperationHandler;
import com.micdm.smsgraphs.misc.DateUtils;
import com.micdm.smsgraphs.misc.PercentageView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MonthStatsFragment extends Fragment {

    private class CategoryStatsListAdapter extends BaseExpandableListAdapter {

        private List<CategoryStat> stats;

        public void setStats(List<CategoryStat> stats) {
            this.stats = stats;
        }

        @Override
        public int getGroupCount() {
            return (stats == null) ? 0 : stats.size();
        }

        @Override
        public int getChildrenCount(int position) {
            return getGroup(position).stats.size();
        }

        @Override
        public CategoryStat getGroup(int position) {
            return stats.get(position);
        }

        @Override
        public TargetStat getChild(int groupPosition, int childPosition) {
            return getGroup(groupPosition).stats.get(childPosition);
        }

        @Override
        public long getGroupId(int position) {
            return getGroup(position).category.id;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return getChild(groupPosition, childPosition).target.id;
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
            ((PercentageView) view).setPercentage(stat.percentage);
            TextView nameView = (TextView) view.findViewById(R.id.v__stats__list_item_category__name);
            nameView.setText(stat.category.name);
            TextView amountView = (TextView) view.findViewById(R.id.v__stats__list_item_category__amount);
            amountView.setText(String.valueOf(stat.amount));
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
            if (view == null) {
                view = View.inflate(getActivity(), R.layout.v__stats__list_item_target, null);
            }
            TargetStat stat = getChild(groupPosition, childPosition);
            ((PercentageView) view).setPercentage(stat.percentage);
            TextView nameView = (TextView) view.findViewById(R.id.v__stats__list_item_target__name);
            nameView.setText(stat.target.title == null ? stat.target.name : stat.target.title);
            TextView amountView = (TextView) view.findViewById(R.id.v__stats__list_item_target__amount);
            amountView.setText(String.valueOf(stat.amount));
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }

    public static final String INIT_ARG_IS_FIRST = "is_first";
    public static final String INIT_ARG_IS_LAST = "is_last";
    public static final String INIT_ARG_DATE = "date";

    private OperationHandler operationHandler;

    private boolean isFirst;
    private boolean isLast;
    private DateTime date;

    private ExpandableListView categoriesView;
    private TextView totalView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        operationHandler = (OperationHandler) activity;
        handleInitArguments();
    }

    private void handleInitArguments() {
        Bundle args = getArguments();
        isFirst = args.getBoolean(INIT_ARG_IS_FIRST);
        isLast = args.getBoolean(INIT_ARG_IS_LAST);
        date = DateUtils.parseForBundle(args.getString(INIT_ARG_DATE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f__month_stats, null);
        View previousView = view.findViewById(R.id.f__month_stats__previous);
        previousView.setVisibility(isFirst ? View.INVISIBLE : View.VISIBLE);
        View nextView = view.findViewById(R.id.f__month_stats__next);
        nextView.setVisibility(isLast ? View.INVISIBLE : View.VISIBLE);
        TextView monthView = (TextView) view.findViewById(R.id.f__month_stats__month);
        monthView.setText(DateUtils.formatMonthForHuman(date));
        categoriesView = (ExpandableListView) view.findViewById(R.id.f__month_stats__categories);
        totalView = (TextView) view.findViewById(R.id.f__month_stats__total);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getEventManager().subscribe(this, EventType.LOAD_OPERATIONS, new EventManager.OnEventListener<LoadOperationsEvent>() {
            @Override
            public void onEvent(LoadOperationsEvent event) {
                MonthOperationList operations = event.getOperations();
                if (!operations.month.equals(date)) {
                    return;
                }
                List<CategoryStat> stats = getCategoryStats(operations.operations);
                CategoryStatsListAdapter adapter = (CategoryStatsListAdapter) categoriesView.getExpandableListAdapter();
                if (adapter == null) {
                    adapter = new CategoryStatsListAdapter();
                    categoriesView.setAdapter(adapter);
                }
                adapter.setStats(stats);
                adapter.notifyDataSetChanged();
                totalView.setText(String.valueOf(getTotalSum(stats)));
            }
        });
        operationHandler.loadOperations(date);
    }

    private EventManager getEventManager() {
        return ((CustomApplication) getActivity().getApplication()).getEventManager();
    }

    private List<CategoryStat> getCategoryStats(List<Operation> operations) {
        List<CategoryStat> stats = new ArrayList<CategoryStat>();
        Category noCategory = new Category(0, getString(R.string.no_category));
        for (Operation operation: operations) {
            Category category = operation.target.category;
            CategoryStat stat = updateCategoryStat(stats, (category == null) ? noCategory : category, operation.amount);
            updateTargetStat(stat.stats, operation.target, operation.amount);
        }
        addPercentages(stats);
        sortByName(stats);
        return stats;
    }

    private CategoryStat updateCategoryStat(List<CategoryStat> stats, Category category, int amount) {
        CategoryStat stat = getCategoryStat(stats, category);
        if (stat == null) {
            stat = new CategoryStat(category);
            stats.add(stat);
        }
        stat.amount += amount;
        return stat;
    }

    private CategoryStat getCategoryStat(List<CategoryStat> stats, Category category) {
        for (CategoryStat stat: stats) {
            if (stat.category.id == category.id) {
                return stat;
            }
        }
        return null;
    }

    private void updateTargetStat(List<TargetStat> stats, Target target, int amount) {
        TargetStat targetStat = getTargetStat(stats, target);
        if (targetStat == null) {
            targetStat = new TargetStat(target);
            stats.add(targetStat);
        }
        targetStat.amount += amount;
    }

    public TargetStat getTargetStat(List<TargetStat> stats, Target target) {
        for (TargetStat stat: stats) {
            if (stat.target.id == target.id) {
                return stat;
            }
        }
        return null;
    }

    private void addPercentages(List<CategoryStat> stats) {
        int total = getTotalSum(stats);
        for (CategoryStat categoryStat: stats) {
            categoryStat.percentage = (double) categoryStat.amount / total;
            for (TargetStat targetStat: categoryStat.stats) {
                targetStat.percentage = (double) targetStat.amount / categoryStat.amount;
            }
        }
    }

    private int getTotalSum(List<CategoryStat> stats) {
        int total = 0;
        for (CategoryStat stat: stats) {
            total += stat.amount;
        }
        return total;
    }

    private void sortByName(List<CategoryStat> stats) {
        Collections.sort(stats, new Comparator<CategoryStat>() {
            @Override
            public int compare(CategoryStat a, CategoryStat b) {
                return a.category.name.compareTo(b.category.name);
            }
        });
        for (CategoryStat stat: stats) {
            Collections.sort(stat.stats, new Comparator<TargetStat>() {
                @Override
                public int compare(TargetStat a, TargetStat b) {
                    return a.target.name.compareTo(b.target.name);
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