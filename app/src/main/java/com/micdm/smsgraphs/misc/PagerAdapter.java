package com.micdm.smsgraphs.misc;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {

    public static class Page {

        public final String title;
        public final Fragment fragment;

        public Page(String title, Fragment fragment) {
            this.title = title;
            this.fragment = fragment;
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
        return pages.get(i).fragment;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public CharSequence getPageTitle(int i) {
        return pages.get(i).title;
    }
}
