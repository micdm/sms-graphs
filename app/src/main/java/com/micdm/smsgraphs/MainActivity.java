package com.micdm.smsgraphs;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.data.OperationReport;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.writers.DbTargetWriter;
import com.micdm.smsgraphs.fragments.StatsFragment;
import com.micdm.smsgraphs.fragments.TargetFragment;
import com.micdm.smsgraphs.fragments.TargetListFragment;
import com.micdm.smsgraphs.handlers.CategoryHandler;
import com.micdm.smsgraphs.handlers.OperationHandler;
import com.micdm.smsgraphs.handlers.TargetHandler;
import com.micdm.smsgraphs.loaders.DataLoader;
import com.micdm.smsgraphs.loaders.LoaderResult;
import com.micdm.smsgraphs.loaders.MessageLoader;
import com.micdm.utils.events.EventListener;
import com.micdm.utils.events.EventListenerManager;
import com.micdm.utils.pager.PagerActivity;
import com.micdm.utils.pager.PagerAdapter;

import java.util.Calendar;

// TODO: при первом запуске показать обучение
public class MainActivity extends PagerActivity implements OperationHandler, CategoryHandler, TargetHandler {

    private static final int MESSAGE_LOADER_ID = 0;
    private static final int DATA_LOADER_ID = 1;

    private static final String FRAGMENT_TARGET_TAG = "target";

    private static final String EVENT_LISTENER_KEY_ON_LOAD_OPERATIONS = "OnLoadOperations";
    private static final String EVENT_LISTENER_KEY_ON_LOAD_CATEGORIES = "OnLoadCategories";
    private static final String EVENT_LISTENER_KEY_ON_LOAD_TARGETS = "OnLoadTargets";
    private static final String EVENT_LISTENER_KEY_ON_START_EDIT_TARGET = "OnStartEditTarget";
    private static final String EVENT_LISTENER_KEY_ON_EDIT_TARGET = "OnEditTarget";

    private final EventListenerManager events = new EventListenerManager();

    private OperationReport report;
    private CategoryList categories;
    private TargetList targets;
    private Calendar month;
    private MonthOperationList operations;
    private Target currentTarget;

    private View loadingMessagesView;
    private ProgressBar loadingMessagesProgressView;
    private View loadingDataView;
    private View noOperationsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restartLoaders();
        setupView();
        setupActionBar();
        setupPager((ViewPager) findViewById(R.id.a__main__pager));
    }

    private void restartLoaders() {
        LoaderManager manager = getLoaderManager();
        manager.restartLoader(MESSAGE_LOADER_ID, null, getMessageLoaderCallbacks());
        manager.restartLoader(DATA_LOADER_ID, null, getDataLoaderCallbacks());
    }

    private LoaderManager.LoaderCallbacks getMessageLoaderCallbacks() {
        return new LoaderManager.LoaderCallbacks<Void>() {
            @Override
            public Loader<Void> onCreateLoader(int id, Bundle params) {
                return new MessageLoader(getApplicationContext(), ((CustomApplication) getApplication()).getDbHelper(), new MessageLoader.OnLoadListener() {
                    @Override
                    public void onStartLoad() {
                        loadingMessagesView.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onProgress(int total, int current) {
                        loadingMessagesProgressView.setMax(total);
                        loadingMessagesProgressView.setProgress(current);
                    }
                    @Override
                    public void onFinishLoad() {
                        loadingMessagesView.setVisibility(View.GONE);
                    }
                });
            }
            @Override
            public void onLoadFinished(Loader<Void> loader, Void result) {
                DataLoader dataLoader = (DataLoader) (Loader<?>) getLoaderManager().getLoader(DATA_LOADER_ID);
                dataLoader.reloadAll();
            }
            @Override
            public void onLoaderReset(Loader<Void> loader) {}
        };
    }

    private LoaderManager.LoaderCallbacks getDataLoaderCallbacks() {
        final Context context = this;
        return new LoaderManager.LoaderCallbacks<LoaderResult>() {
            @Override
            public Loader<LoaderResult> onCreateLoader(int id, Bundle params) {
                return new DataLoader(getApplicationContext(), ((CustomApplication) getApplication()).getDbHelper(), new DataLoader.OnLoadListener() {
                    @Override
                    public void onStartLoadAll() {
                        loadingDataView.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onFinishLoadAll() {
                        loadingDataView.setVisibility(View.GONE);
                    }
                    @Override
                    public void onStartLoadOperations(final Calendar month) {
                        events.notify(EVENT_LISTENER_KEY_ON_LOAD_OPERATIONS, new EventListenerManager.OnIterateListener() {
                            @Override
                            public void onIterate(EventListener listener) {
                                ((OnLoadOperationsListener) listener).onStartLoadOperations(month);
                            }
                        });
                    }
                    @Override
                    public void onFinishLoadOperations() {
                        events.notify(EVENT_LISTENER_KEY_ON_LOAD_OPERATIONS, new EventListenerManager.OnIterateListener() {
                            @Override
                            public void onIterate(EventListener listener) {
                                ((OnLoadOperationsListener) listener).onFinishLoadOperations();
                            }
                        });
                    }
                });
            }
            @Override
            public void onLoadFinished(Loader<LoaderResult> loader, LoaderResult loaded) {
                if (loaded.report != report) {
                    report = loaded.report;
                    noOperationsView.setVisibility((report.last == null) ? View.VISIBLE : View.GONE);
                }
                if (loaded.categories != categories) {
                    categories = loaded.categories;
                    events.notify(EVENT_LISTENER_KEY_ON_LOAD_CATEGORIES, new EventListenerManager.OnIterateListener() {
                        @Override
                        public void onIterate(EventListener listener) {
                            ((OnLoadCategoriesListener) listener).onLoadCategories(categories);
                        }
                    });
                }
                if (loaded.targets != targets) {
                    targets = loaded.targets;
                    updateWithNoCategoryCount();
                    events.notify(EVENT_LISTENER_KEY_ON_LOAD_TARGETS, new EventListenerManager.OnIterateListener() {
                        @Override
                        public void onIterate(EventListener listener) {
                            ((OnLoadTargetsListener) listener).onLoadTargets(targets);
                        }
                    });
                }
                if (loaded.operations != operations) {
                    operations = loaded.operations;
                    month = operations.month;
                    events.notify(EVENT_LISTENER_KEY_ON_LOAD_OPERATIONS, new EventListenerManager.OnIterateListener() {
                        @Override
                        public void onIterate(EventListener listener) {
                            ((OnLoadOperationsListener) listener).onLoadOperations(operations, hasPreviousMonth(), hasNextMonth());
                        }
                    });
                }
            }
            @Override
            public void onLoaderReset(Loader<LoaderResult> loader) {}
        };
    }

    private void setupView() {
        setContentView(R.layout.a__main);
        loadingMessagesView = findViewById(R.id.a__main__loading_messages);
        loadingMessagesProgressView = (ProgressBar) findViewById(R.id.a__main__loading_messages_progress);
        loadingDataView = findViewById(R.id.a__main__loading_data);
        noOperationsView = findViewById(R.id.a__main__no_operations);
    }

    @Override
    protected void setupPager(ViewPager pager) {
        super.setupPager(pager);
        addStatsPage(pager);
        addTargetListPage(pager);
    }

    private void addStatsPage(ViewPager pager) {
        String title = getString(R.string.tab_title_stats);
        addTab(pager, title);
        addPage(pager, new PagerAdapter.Page(title, new StatsFragment()));
    }

    private void addTargetListPage(ViewPager pager) {
        String title = getString(R.string.tab_title_targets);
        ActionBar.Tab tab = addTab(pager, R.layout.v__actionbar__target_list_tab);
        View view = tab.getCustomView();
        TextView titleView = (TextView) view.findViewById(R.id.v__actionbar__target_list_tab__title);
        titleView.setText(title);
        View countView = view.findViewById(R.id.v__actionbar__target_list_tab__count);
        countView.setVisibility(View.GONE);
        addPage(pager, new PagerAdapter.Page(title, new TargetListFragment()));
    }

    private void updateWithNoCategoryCount() {
        ActionBar actionBar = getActionBar();
        if (actionBar == null) {
            return;
        }
        View view = actionBar.getTabAt(1).getCustomView();
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
        getLoaderManager().getLoader(MESSAGE_LOADER_ID).forceLoad();
    }

    @Override
    public void loadPreviousMonthOperations() {
        Calendar month = (Calendar) operations.month.clone();
        month.add(Calendar.MONTH, -1);
        loadMonthOperations(month);
    }

    @Override
    public void loadNextMonthOperations() {
        Calendar month = (Calendar) operations.month.clone();
        month.add(Calendar.MONTH, 1);
        loadMonthOperations(month);
    }

    private void loadMonthOperations(Calendar month) {
        this.month = month;
        DataLoader dataLoader = (DataLoader) (Loader<?>) getLoaderManager().getLoader(DATA_LOADER_ID);
        dataLoader.reloadOperations(month);
    }

    private boolean hasPreviousMonth() {
        return report.first.before(operations.month);
    }

    private boolean hasNextMonth() {
        Calendar month = (Calendar) operations.month.clone();
        month.add(Calendar.MONTH, 1);
        return report.last.after(month);
    }

    @Override
    public void addOnLoadOperationsListener(OnLoadOperationsListener listener) {
        events.add(EVENT_LISTENER_KEY_ON_LOAD_OPERATIONS, listener);
        if (operations != null) {
            listener.onLoadOperations(operations, hasPreviousMonth(), hasNextMonth());
        }
    }

    @Override
    public void removeOnLoadOperationsListener(OnLoadOperationsListener listener) {
        events.remove(EVENT_LISTENER_KEY_ON_LOAD_OPERATIONS, listener);
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
                ((OnEditTargetListener) listener).onEditTarget();
            }
        });
        loadMonthOperations(month);
        DbTargetWriter writer = new DbTargetWriter(((CustomApplication) getApplication()).getDbHelper());
        writer.write(currentTarget);
        if (editNext) {
            Target nextTarget = getNextTargetToEdit(targets, currentTarget);
            currentTarget = null;
            startEditTarget(nextTarget);
        } else {
            currentTarget = null;
        }
    }

    private Target getNextTargetToEdit(TargetList targets, Target current) {
        if (targets.size() == 1) {
            return current;
        }
        int index = targets.indexOf(current);
        int i = index + 1;
        while (i != index) {
            if (i == targets.size()) {
                i = 0;
            }
            Target next = targets.get(i);
            if (next.category == null) {
                return next;
            }
            i += 1;
        }
        return (index + 1 == targets.size()) ? targets.get(0) : targets.get(index + 1);
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
