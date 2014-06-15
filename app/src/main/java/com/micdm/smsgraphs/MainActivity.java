package com.micdm.smsgraphs;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.micdm.smsgraphs.chart.Chart;
import com.micdm.smsgraphs.chart.ChartElement;
import com.micdm.smsgraphs.chart.ChartElementGroup;
import com.micdm.smsgraphs.chart.ChartView;
import com.micdm.smsgraphs.data.MonthStats;
import com.micdm.smsgraphs.data.Stats;
import com.micdm.smsgraphs.data.incomes.Income;
import com.micdm.smsgraphs.data.outcomes.Outcome;
import com.micdm.smsgraphs.parser.StatsBuilder;

import java.util.List;

public class MainActivity extends Activity {

    private static class CustomPagerAdapter extends FragmentPagerAdapter {

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ChartFragment();
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "CHART";
        }
    }

    public static class ChartFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_chart, container, false);
            drawChart((ChartView) view.findViewById(R.id.chart));
            return view;
        }

        private void drawChart(ChartView view) {
            try {
                StatsBuilder builder = new StatsBuilder(getActivity());
                Stats stats = builder.build();
                Chart chart = getChart(stats);
                view.setChart(chart);
            } catch (StatsBuilder.BuildError e) {}
        }

        private Chart getChart(Stats stats) {
            Chart chart = new Chart();
            MonthStats month = stats.getCardStats("VISA1234").getMonthStats(2014, 3);
            chart.addGroup(getIncomeChartGroup(month.getIncomes()));
            chart.addGroup(getOutcomeChartGroup(month.getOutcomes()));
            return chart;
        }

        private ChartElementGroup getIncomeChartGroup(List<Income> incomes) {
            ChartElementGroup group = new ChartElementGroup();
            for (Income income: incomes) {
                ChartElement element = new ChartElement(income.getTotalAmount());
                group.addElement(element);
            }
            return group;
        }

        private ChartElementGroup getOutcomeChartGroup(List<Outcome> outcomes) {
            ChartElementGroup group = new ChartElementGroup();
            for (Outcome outcome: outcomes) {
                ChartElement element = new ChartElement(outcome.getTotalAmount());
                group.addElement(element);
            }
            return group;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActionBar();
        setupPager();
    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    private void setupPager() {
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new CustomPagerAdapter(getFragmentManager()));
    }
}
