package com.micdm.smsgraphs.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.micdm.smsgraphs.CustomApplication;
import com.micdm.smsgraphs.R;
import com.micdm.smsgraphs.data.OperationReport;
import com.micdm.smsgraphs.events.EventManager;
import com.micdm.smsgraphs.events.EventType;
import com.micdm.smsgraphs.events.events.LoadOperationReportEvent;
import com.micdm.smsgraphs.handlers.OperationReportHandler;
import com.micdm.smsgraphs.misc.DateUtils;

import org.joda.time.DateTime;

public class StatsFragment extends Fragment {

    private class MonthStatsPagerAdapter extends FragmentPagerAdapter {

        private final DateTime _last;
        private final int _count;

        public MonthStatsPagerAdapter(FragmentManager fm, DateTime last, int count) {
            super(fm);
            _last = last;
            _count = count;
        }

        @Override
        public int getCount() {
            return _count;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new MonthStatsFragment();
            fragment.setArguments(getFragmentArgs(position));
            return fragment;
        }

        private Bundle getFragmentArgs(int position) {
            Bundle args = new Bundle();
            args.putBoolean(MonthStatsFragment.INIT_ARG_IS_FIRST, position == 0);
            args.putBoolean(MonthStatsFragment.INIT_ARG_IS_LAST, position == (_count - 1));
            DateTime date = _last.minusMonths(_count - position - 1);
            args.putString(MonthStatsFragment.INIT_ARG_DATE, DateUtils.formatForBundle(date));
            return args;
        }
    }

    private static final String STATE_ITEM_CURRENT_ITEM = "current_item";

    private OperationReportHandler _operationReportHandler;

    private int _currentItem = -1;

    private ViewPager _pager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _operationReportHandler = (OperationReportHandler) activity;
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        if (state != null) {
            _currentItem = state.getInt(STATE_ITEM_CURRENT_ITEM, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f__stats, null);
        _pager = (ViewPager) view.findViewById(R.id.f__stats__pager);
        _pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                _currentItem = position;
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
        getEventManager().subscribe(this, EventType.LOAD_OPERATION_REPORT, new EventManager.OnEventListener<LoadOperationReportEvent>() {
            @Override
            public void onEvent(LoadOperationReportEvent event) {
                OperationReport report = event.getReport();
                int count = report.getMonthCount();
                _pager.setAdapter(new MonthStatsPagerAdapter(getChildFragmentManager(), report.getLast(), count));
                _pager.setCurrentItem((_currentItem == -1) ? (count - 1) : _currentItem, false);
            }
        });
        _operationReportHandler.loadOperationReport();
    }

    private EventManager getEventManager() {
        return ((CustomApplication) getActivity().getApplication()).getEventManager();
    }

    @Override
    public void onStop() {
        super.onStop();
        getEventManager().unsubscribeAll(this);
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        state.putInt(STATE_ITEM_CURRENT_ITEM, _pager.getCurrentItem());
        super.onSaveInstanceState(state);
    }
}
