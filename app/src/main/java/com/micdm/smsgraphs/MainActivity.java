package com.micdm.smsgraphs;

import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.db.DbCategoryLoader;
import com.micdm.smsgraphs.db.DbTargetLoader;
import com.micdm.smsgraphs.fragments.TargetListFragment;
import com.micdm.smsgraphs.handlers.TargetHandler;
import com.micdm.smsgraphs.messages.MessageConverter;
import com.micdm.utils.events.EventListener;
import com.micdm.utils.events.EventListenerManager;
import com.micdm.utils.pager.PagerActivity;
import com.micdm.utils.pager.PagerAdapter;

import java.util.List;

public class MainActivity extends PagerActivity implements TargetHandler {

    private static final int MESSAGE_CONVERTER_LOADER_ID = 0;
    private static final int CATEGORY_LOADER_LOADER_ID = 1;
    private static final int TARGET_LOADER_LOADER_ID = 2;

    private static final String EVENT_LISTENER_KEY_ON_LOAD_TARGETS = "OnLoadTargets";

    private final EventListenerManager events = new EventListenerManager();

    private List<Category> categories;
    private TargetList targets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a__main);
        convert();
        setupActionBar();
        setupPager((ViewPager) findViewById(R.id.a__main__pager));
    }

    private void convert() {
        final Context context = this;
        getLoaderManager().initLoader(MESSAGE_CONVERTER_LOADER_ID, null, new LoaderManager.LoaderCallbacks<Void>() {
            @Override
            public Loader<Void> onCreateLoader(int id, Bundle params) {
                if (id == MESSAGE_CONVERTER_LOADER_ID) {
                    return new MessageConverter(context);
                }
                return null;
            }
            @Override
            public void onLoadFinished(Loader<Void> loader, Void result) {
                loadCategories();
            }
            @Override
            public void onLoaderReset(Loader<Void> loader) {
                // TODO: что-то нужно сделать?
            }
        });
    }

    private void loadCategories() {
        final Context context = this;
        getLoaderManager().initLoader(CATEGORY_LOADER_LOADER_ID, null, new LoaderManager.LoaderCallbacks<List<Category>>() {
            @Override
            public Loader<List<Category>> onCreateLoader(int id, Bundle params) {
                if (id == CATEGORY_LOADER_LOADER_ID) {
                    return new DbCategoryLoader(context);
                }
                return null;
            }
            @Override
            public void onLoadFinished(Loader<List<Category>> loader, List<Category> loaded) {
                categories = loaded;
                loadTargets();
            }
            @Override
            public void onLoaderReset(Loader<List<Category>> loader) {
                // TODO: что-то нужно сделать?
            }
        });
    }

    private void loadTargets() {
        final Context context = this;
        getLoaderManager().initLoader(TARGET_LOADER_LOADER_ID, null, new LoaderManager.LoaderCallbacks<TargetList>() {
            @Override
            public Loader<TargetList> onCreateLoader(int id, Bundle params) {
                if (id == TARGET_LOADER_LOADER_ID) {
                    return new DbTargetLoader(context, categories);
                }
                return null;
            }
            @Override
            public void onLoadFinished(Loader<TargetList> loader, TargetList loaded) {
                targets = loaded;
                events.notify(EVENT_LISTENER_KEY_ON_LOAD_TARGETS, new EventListenerManager.OnIterateListener() {
                    @Override
                    public void onIterate(EventListener listener) {
                        ((TargetHandler.OnLoadTargetsListener) listener).onLoadTargets(targets);
                    }
                });
            }
            @Override
            public void onLoaderReset(Loader<TargetList> loader) {
                // TODO: что-то нужно сделать?
            }
        });
    }

    @Override
    protected void setupPager(ViewPager pager) {
        super.setupPager(pager);
        addTargetListPage(pager);
    }

    private void addTargetListPage(ViewPager pager) {
        ActionBar.Tab tab = addTab(pager, R.layout.v__actionbar__target_list_tab);
        View view = tab.getCustomView();
        TextView titleView = (TextView) view.findViewById(R.id.v__actionbar__target_list_tab__title);
        titleView.setText(R.string.tab_title_targets);
        View countView = view.findViewById(R.id.v__actionbar__target_list_tab__count);
        countView.setVisibility(View.GONE);
        addPage(pager, new PagerAdapter.Page(getString(R.string.tab_title_targets), new TargetListFragment()));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void updateWithNoCategoryCount(int count) {
        ActionBar actionBar = getActionBar();
        if (actionBar == null) {
            return;
        }
        View view = actionBar.getTabAt(0).getCustomView();
        TextView countView = (TextView) view.findViewById(R.id.v__actionbar__target_list_tab__count);
        if (count == 0) {
            countView.setVisibility(View.GONE);
        } else {
            countView.setText(String.valueOf(count));
            countView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void addOnLoadTargetsListener(OnLoadTargetsListener listener) {
        events.add(EVENT_LISTENER_KEY_ON_LOAD_TARGETS, listener);
        if (targets != null) {
            listener.onLoadTargets(targets);
        }
    }

    @Override
    public void removeOnLoadTargetsListener(OnLoadTargetsListener listener) {
        events.remove(EVENT_LISTENER_KEY_ON_LOAD_TARGETS, listener);
    }
}
