package com.micdm.smsgraphs.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.micdm.smsgraphs.R;
import com.micdm.smsgraphs.data.OperationReport;
import com.micdm.smsgraphs.handlers.OperationReportHandler;
import com.micdm.smsgraphs.misc.DateUtils;

import org.joda.time.DateTime;

import java.util.Hashtable;
import java.util.Map;

public class StatsFragment extends Fragment {

    private class MonthStatsPagerAdapter extends FragmentPagerAdapter {

        private final Map<Integer, Fragment> fragments = new Hashtable<Integer, Fragment>();
        private final DateTime last;
        private final int count;

        public MonthStatsPagerAdapter(FragmentManager fm, DateTime last, int count) {
            super(fm);
            this.last = last;
            this.count = count;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = fragments.get(position);
            if (fragment == null) {
                fragment = new MonthStatsFragment();
                fragment.setArguments(getFragmentArgs(position));
                fragments.put(position, fragment);
            }
            return fragment;
        }

        private Bundle getFragmentArgs(int position) {
            Bundle args = new Bundle();
            DateTime date = getDate(position);
            args.putInt(MonthStatsFragment.INIT_ARG_YEAR, date.getYear());
            args.putInt(MonthStatsFragment.INIT_ARG_MONTH, date.getMonthOfYear());
            return args;
        }

        public DateTime getDate(int position) {
            return last.minusMonths(count - position - 1);
        }

        public boolean hasPrevious(int position) {
            return position > 0;
        }

        public boolean hasNext(int position) {
            return position < count - 1;
        }
    }

    private OperationReportHandler operationReportHandler;
    private final OperationReportHandler.OnLoadOperationReportListener onLoadOperationReportListener = new OperationReportHandler.OnLoadOperationReportListener() {
        @Override
        public void onLoadOperationReport(OperationReport report) {
            int count = report.getMonthCount();
            pager.setAdapter(new MonthStatsPagerAdapter(getFragmentManager(), report.last, count));
            pager.setCurrentItem(count - 1, false);
        }
    };

    private View previousView;
    private TextView monthView;
    private View nextView;
    private ViewPager pager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        operationReportHandler = (OperationReportHandler) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f__stats, null);
        previousView = view.findViewById(R.id.f__stats__previous);
        previousView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(pager.getCurrentItem() - 1);
            }
        });
        monthView = (TextView) view.findViewById(R.id.f__stats__month);
        nextView = view.findViewById(R.id.f__stats__next);
        nextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            }
        });
        pager = (ViewPager) view.findViewById(R.id.f__stats__pager);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                MonthStatsPagerAdapter adapter = (MonthStatsPagerAdapter) pager.getAdapter();
                previousView.setVisibility(adapter.hasPrevious(position) ? View.VISIBLE : View.INVISIBLE);
                nextView.setVisibility(adapter.hasNext(position) ? View.VISIBLE : View.INVISIBLE);
                monthView.setText(DateUtils.formatMonthForHuman(adapter.getDate(position)));
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        operationReportHandler.addOnLoadOperationReportListener(onLoadOperationReportListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        operationReportHandler.removeOnLoadOperationReportListener(onLoadOperationReportListener);
    }
}
