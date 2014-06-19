package com.micdm.smsgraphs.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.micdm.smsgraphs.R;
import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.data.CategoryStat;
import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.data.Operation;
import com.micdm.smsgraphs.handlers.OperationHandler;
import com.micdm.smsgraphs.misc.CategoryStatsListItemView;
import com.micdm.smsgraphs.misc.DateUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// TODO: перезагружать отчет при изменении продавцов
public class StatsFragment extends Fragment {

    private class CategoryStatsListAdapter extends BaseAdapter {

        private final List<CategoryStat> stats;

        public CategoryStatsListAdapter(List<CategoryStat> stats) {
            this.stats = stats;
        }

        @Override
        public int getCount() {
            return stats.size();
        }

        @Override
        public CategoryStat getItem(int position) {
            return stats.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).category.id;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(getActivity(), R.layout.v__stats__list_item, null);
            }
            CategoryStat stat = getItem(position);
            ((CategoryStatsListItemView) view).setPercentage(stat.percentage);
            TextView nameView = (TextView) view.findViewById(R.id.v__stats__list_item__name);
            nameView.setText(stat.category.name);
            TextView amountView = (TextView) view.findViewById(R.id.v__stats__list_item__amount);
            amountView.setText(stat.amount.toString());
            return view;
        }
    }

    private OperationHandler operationHandler;
    private final OperationHandler.OnLoadOperationsListener onLoadOperationsListener = new OperationHandler.OnLoadOperationsListener() {
        @Override
        public void onStartLoadOperations(Calendar month) {
            loadingOperationsView.setText(getString(R.string.fragment_stats_loading_operations, DateUtils.formatMonthForHuman(month).toLowerCase()));
            loadingOperationsView.setVisibility(View.VISIBLE);
        }
        @Override
        public void onFinishLoadOperations() {
            loadingOperationsView.setVisibility(View.GONE);
        }
        @Override
        public void onLoadOperations(MonthOperationList operations, boolean previous, boolean next) {
            previousView.setEnabled(previous);
            monthView.setText(DateUtils.formatMonthForHuman(operations.month));
            nextView.setEnabled(next);
            List<CategoryStat> stats = getCategoryStats(operations.operations);
            if (stats.size() == 0) {
                noCategoryStatsView.setVisibility(View.VISIBLE);
            } else {
                categoriesView.setAdapter(new CategoryStatsListAdapter(stats));
                BigDecimal total = getTotalSum(stats);
                totalView.setText(total.toString());
                noCategoryStatsView.setVisibility(View.GONE);
            }
        }
    };

    private View previousView;
    private TextView monthView;
    private View nextView;
    private ListView categoriesView;
    private TextView totalView;
    private View noCategoryStatsView;
    private TextView loadingOperationsView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        operationHandler = (OperationHandler) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f__stats, null);
        previousView = view.findViewById(R.id.f__stats__previous);
        previousView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operationHandler.loadPreviousMonthOperations();
            }
        });
        monthView = (TextView) view.findViewById(R.id.f__stats__month);
        nextView = view.findViewById(R.id.f__stats__next);
        nextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operationHandler.loadNextMonthOperations();
            }
        });
        categoriesView = (ListView) view.findViewById(R.id.f__stats__categories);
        totalView = (TextView) view.findViewById(R.id.f__stats__total);
        noCategoryStatsView = view.findViewById(R.id.f__stats__no_category_stats);
        loadingOperationsView = (TextView) view.findViewById(R.id.f__stats__loading_operations);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        operationHandler.addOnLoadOperationsListener(onLoadOperationsListener);
    }

    private List<CategoryStat> getCategoryStats(List<Operation> operations) {
        List<CategoryStat> stats = new ArrayList<CategoryStat>();
        for (Operation operation: operations) {
            Category category = operation.target.category;
            if (category == null) {
                continue;
            }
            CategoryStat stat = getCategoryStat(stats, category);
            if (stat == null) {
                stat = new CategoryStat(category);
                stats.add(stat);
            }
            stat.amount = stat.amount.add(operation.amount);
        }
        roundAmounts(stats);
        addPercentages(stats);
        sortCategoryStatsByName(stats);
        return stats;
    }

    private CategoryStat getCategoryStat(List<CategoryStat> stats, Category category) {
        for (CategoryStat stat: stats) {
            if (stat.category == category) {
                return stat;
            }
        }
        return null;
    }

    private void roundAmounts(List<CategoryStat> stats) {
        for (CategoryStat stat: stats) {
            stat.amount = stat.amount.setScale(0, BigDecimal.ROUND_UP);
        }
    }

    private void addPercentages(List<CategoryStat> stats) {
        BigDecimal total = getTotalSum(stats);
        for (CategoryStat stat: stats) {
            stat.percentage = stat.amount.divide(total, MathContext.DECIMAL64).doubleValue();
        }
    }

    private BigDecimal getTotalSum(List<CategoryStat> stats) {
        BigDecimal total = new BigDecimal(0);
        for (CategoryStat stat: stats) {
            total = total.add(stat.amount);
        }
        return total;
    }

    private void sortCategoryStatsByName(List<CategoryStat> stats) {
        Collections.sort(stats, new Comparator<CategoryStat>() {
            @Override
            public int compare(CategoryStat a, CategoryStat b) {
                return a.category.name.compareTo(b.category.name);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        operationHandler.removeOnLoadOperationsListener(onLoadOperationsListener);
    }
}
