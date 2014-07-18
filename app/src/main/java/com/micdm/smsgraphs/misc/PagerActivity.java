package com.micdm.smsgraphs.misc;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class PagerActivity extends FragmentActivity {

    protected void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    protected void setupPager(ViewPager pager) {
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int i) {
                ActionBar actionBar = getActionBar();
                if (actionBar == null) {
                    return;
                }
                actionBar.setSelectedNavigationItem(i);
            }
        });
    }

    protected void addPage(ViewPager pager, PagerAdapter.Page page) {
        ((PagerAdapter) pager.getAdapter()).add(page);
    }

    protected ActionBar.Tab addTab(ViewPager pager, String title) {
        return addTab(pager, title, 0);
    }

    protected ActionBar.Tab addTab(ViewPager pager, int viewResourceId) {
        return addTab(pager, null, viewResourceId);
    }

    private ActionBar.Tab addTab(final ViewPager pager, String title, int viewResourceId) {
        ActionBar actionBar = getActionBar();
        if (actionBar == null) {
            return null;
        }
        ActionBar.Tab tab = actionBar.newTab();
        if (viewResourceId == 0) {
            tab.setText(title);
        } else {
            tab.setCustomView(viewResourceId);
        }
        tab.setTabListener(new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                pager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {}
            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {}
        });
        actionBar.addTab(tab);
        return tab;
    }
}
