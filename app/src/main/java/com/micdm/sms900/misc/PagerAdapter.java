package com.micdm.sms900.misc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {

    public static class Page {

        public final String _title;
        public final Fragment _fragment;

        public Page(String title, Fragment fragment) {
            _title = title;
            _fragment = fragment;
        }

        public String getTitle() {
            return _title;
        }

        public Fragment getFragment() {
            return _fragment;
        }
    }

    private final List<Page> pages = new ArrayList<Page>();

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void add(Page page) {
        pages.add(page);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int i) {
        return pages.get(i).getFragment();
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public CharSequence getPageTitle(int i) {
        return pages.get(i).getTitle();
    }
}
