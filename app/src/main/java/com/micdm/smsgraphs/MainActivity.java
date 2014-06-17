package com.micdm.smsgraphs;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.db.DbCategoryReader;
import com.micdm.smsgraphs.db.DbTargetReader;
import com.micdm.smsgraphs.db.DbTargetWriter;
import com.micdm.smsgraphs.fragments.TargetFragment;
import com.micdm.smsgraphs.fragments.TargetListFragment;
import com.micdm.smsgraphs.handlers.CategoryHandler;
import com.micdm.smsgraphs.handlers.TargetHandler;
import com.micdm.smsgraphs.messages.MessageConverter;
import com.micdm.utils.events.EventListener;
import com.micdm.utils.events.EventListenerManager;
import com.micdm.utils.pager.PagerActivity;
import com.micdm.utils.pager.PagerAdapter;

import java.util.List;

public class MainActivity extends PagerActivity implements CategoryHandler, TargetHandler {

    private static final int MESSAGE_CONVERTER_LOADER_ID = 0;
    private static final int CATEGORY_LOADER_LOADER_ID = 1;
    private static final int TARGET_LOADER_LOADER_ID = 2;

    private static final String FRAGMENT_TARGET_TAG = "target";

    private static final String EVENT_LISTENER_KEY_ON_LOAD_CATEGORIES = "OnLoadCategories";
    private static final String EVENT_LISTENER_KEY_ON_LOAD_TARGETS = "OnLoadTargets";
    private static final String EVENT_LISTENER_KEY_ON_START_EDIT_TARGET = "OnStartEditTarget";
    private static final String EVENT_LISTENER_KEY_ON_EDIT_TARGET = "OnEditTarget";

    private final EventListenerManager events = new EventListenerManager();

    private List<Category> categories;
    private TargetList targets;
    private Target currentTarget;

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
                    return new DbCategoryReader(context);
                }
                return null;
            }
            @Override
            public void onLoadFinished(Loader<List<Category>> loader, List<Category> loaded) {
                categories = loaded;
                events.notify(EVENT_LISTENER_KEY_ON_LOAD_CATEGORIES, new EventListenerManager.OnIterateListener() {
                    @Override
                    public void onIterate(EventListener listener) {
                        ((OnLoadCategoriesListener) listener).onLoadCategories(categories);
                    }
                });
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
                    return new DbTargetReader(context, categories);
                }
                return null;
            }
            @Override
            public void onLoadFinished(Loader<TargetList> loader, TargetList loaded) {
                targets = loaded;
                updateWithNoCategoryCount();
                events.notify(EVENT_LISTENER_KEY_ON_LOAD_TARGETS, new EventListenerManager.OnIterateListener() {
                    @Override
                    public void onIterate(EventListener listener) {
                        ((OnLoadTargetsListener) listener).onLoadTargets(targets);
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

    private void updateWithNoCategoryCount() {
        ActionBar actionBar = getActionBar();
        if (actionBar == null) {
            return;
        }
        View view = actionBar.getTabAt(0).getCustomView();
        TextView countView = (TextView) view.findViewById(R.id.v__actionbar__target_list_tab__count);
        int count = targets.getWithNoCategoryCount();
        if (count == 0) {
            countView.setVisibility(View.GONE);
        } else {
            countView.setText(String.valueOf(count));
            countView.setVisibility(View.VISIBLE);
        }
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
    public void addOnLoadCategoriesListener(OnLoadCategoriesListener listener) {
        events.add(EVENT_LISTENER_KEY_ON_LOAD_CATEGORIES, listener);
        if (categories != null) {
            listener.onLoadCategories(categories);
        }
    }

    @Override
    public void removeOnLoadCategoriesListener(OnLoadCategoriesListener listener) {
        events.remove(EVENT_LISTENER_KEY_ON_LOAD_CATEGORIES, listener);
    }

    @Override
    public void startEditTarget(Target target) {
        currentTarget = target;
        events.notify(EVENT_LISTENER_KEY_ON_START_EDIT_TARGET, new EventListenerManager.OnIterateListener() {
            @Override
            public void onIterate(EventListener listener) {
                ((OnStartEditTargetListener) listener).onStartEditTarget(currentTarget);
            }
        });
        FragmentManager manager = getFragmentManager();
        TargetFragment fragment = (TargetFragment) manager.findFragmentByTag(FRAGMENT_TARGET_TAG);
        if (fragment == null || fragment.isDismissing()) {
            (new TargetFragment()).show(manager, FRAGMENT_TARGET_TAG);
        }
    }

    @Override
    public void finishEditTarget(boolean editNext) {
        updateWithNoCategoryCount();
        events.notify(EVENT_LISTENER_KEY_ON_EDIT_TARGET, new EventListenerManager.OnIterateListener() {
            @Override
            public void onIterate(EventListener listener) {
                ((OnEditTargetListener) listener).onEditTarget(currentTarget);
            }
        });
        (new DbTargetWriter(this)).write(currentTarget);
        if (editNext) {
            Target nextTarget = targets.getFirstWithNoCategory();
            if (nextTarget == null) {
                int index = targets.indexOf(currentTarget);
                nextTarget = targets.get((index == targets.size() - 1) ? 0 : index + 1);
            }
            currentTarget = null;
            startEditTarget(nextTarget);
        } else {
            currentTarget = null;
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

    @Override
    public void addOnStartEditTargetListener(OnStartEditTargetListener listener) {
        events.add(EVENT_LISTENER_KEY_ON_START_EDIT_TARGET, listener);
        if (currentTarget != null) {
            listener.onStartEditTarget(currentTarget);
        }
    }

    @Override
    public void removeOnStartEditTargetListener(OnStartEditTargetListener listener) {
        events.remove(EVENT_LISTENER_KEY_ON_START_EDIT_TARGET, listener);
    }

    @Override
    public void addOnEditTargetListener(OnEditTargetListener listener) {
        events.add(EVENT_LISTENER_KEY_ON_EDIT_TARGET, listener);
    }

    @Override
    public void removeOnEditTargetListener(OnEditTargetListener listener) {
        events.remove(EVENT_LISTENER_KEY_ON_EDIT_TARGET, listener);
    }
}
