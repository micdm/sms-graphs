package com.micdm.smsgraphs;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.data.OperationReport;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.db.writers.DbTargetWriter;
import com.micdm.smsgraphs.fragments.StatsFragment;
import com.micdm.smsgraphs.fragments.TargetFragment;
import com.micdm.smsgraphs.fragments.TargetListFragment;
import com.micdm.smsgraphs.handlers.CategoryHandler;
import com.micdm.smsgraphs.handlers.OperationHandler;
import com.micdm.smsgraphs.handlers.OperationReportHandler;
import com.micdm.smsgraphs.handlers.TargetHandler;
import com.micdm.smsgraphs.loaders.CategoryLoader;
import com.micdm.smsgraphs.loaders.MessageLoader;
import com.micdm.smsgraphs.loaders.OperationLoader;
import com.micdm.smsgraphs.loaders.OperationReportLoader;
import com.micdm.smsgraphs.loaders.TargetLoader;
import com.micdm.utils.events.EventListener;
import com.micdm.utils.events.EventListenerManager;
import com.micdm.utils.pager.PagerActivity;
import com.micdm.utils.pager.PagerAdapter;

import org.joda.time.DateTime;

import java.util.Hashtable;
import java.util.Map;

// TODO: при первом запуске показать обучение
// TODO: добавить пролистывание месяцев свайпом
// TODO: вынести анализатор сообщений в отдельный сервис
public class MainActivity extends PagerActivity implements OperationReportHandler, OperationHandler, CategoryHandler, TargetHandler {

    private static final int MESSAGE_LOADER_ID = 0;
    private static final int OPERATION_REPORT_LOADER_ID = 1;
    private static final int CATEGORY_LOADER_ID = 2;
    private static final int TARGET_LOADER_ID = 3;

    private static final String STATE_KEY_TARGET = "target";

    private static final String FRAGMENT_TARGET_TAG = "target";

    private static final String EVENT_LISTENER_KEY_ON_LOAD_OPERATION_REPORT = "OnLoadOperationReport";
    private static final String EVENT_LISTENER_KEY_ON_LOAD_OPERATIONS = "OnLoadOperations";
    private static final String EVENT_LISTENER_KEY_ON_LOAD_CATEGORIES = "OnLoadCategories";
    private static final String EVENT_LISTENER_KEY_ON_LOAD_TARGETS = "OnLoadTargets";
    private static final String EVENT_LISTENER_KEY_ON_START_EDIT_TARGET = "OnStartEditTarget";
    private static final String EVENT_LISTENER_KEY_ON_EDIT_TARGET = "OnEditTarget";

    private final EventListenerManager events = new EventListenerManager();

    private OperationReport report;
    private CategoryList categories;
    private TargetList targets;
    private final Map<DateTime, MonthOperationList> _operations = new Hashtable<DateTime, MonthOperationList>();

    private int currentTarget;

    private final Map<Integer, DateTime> operationLoaders = new Hashtable<Integer, DateTime>();

    private View loadingMessagesView;
    private ProgressBar loadingMessagesProgressView;
    private View loadingDataView;
    private View noOperationsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreCurrentValues(savedInstanceState);
        initLoaders();
        setupView();
        setupActionBar();
        setupPager((ViewPager) findViewById(R.id.a__main__pager));
    }

    private void restoreCurrentValues(Bundle state) {
        if (state != null) {
            currentTarget = state.getInt(STATE_KEY_TARGET);
        }
    }

    private void initLoaders() {
        LoaderManager manager = getLoaderManager();
        manager.initLoader(MESSAGE_LOADER_ID, null, getMessageLoaderCallbacks());
        manager.initLoader(OPERATION_REPORT_LOADER_ID, null, getOperationReportLoaderCallbacks());
        manager.initLoader(CATEGORY_LOADER_ID, null, getCategoryLoaderCallbacks());
        manager.initLoader(TARGET_LOADER_ID, null, getTargetLoaderCallbacks());
    }

    private LoaderManager.LoaderCallbacks<Void> getMessageLoaderCallbacks() {
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
            public void onLoadFinished(Loader<Void> loader, Void data) {
                LoaderManager manager = getLoaderManager();
                manager.getLoader(OPERATION_REPORT_LOADER_ID).onContentChanged();
                manager.getLoader(TARGET_LOADER_ID).onContentChanged();
                loadingMessagesView.setVisibility(View.GONE);
            }
            @Override
            public void onLoaderReset(Loader<Void> loader) {}
        };
    }

    private LoaderManager.LoaderCallbacks<OperationReport> getOperationReportLoaderCallbacks() {
        return new LoaderManager.LoaderCallbacks<OperationReport>() {
            @Override
            public Loader<OperationReport> onCreateLoader(int id, Bundle params) {
                return new OperationReportLoader(getApplicationContext(), ((CustomApplication) getApplication()).getDbHelper());
            }
            @Override
            public void onLoadFinished(Loader<OperationReport> loader, OperationReport data) {
                if (data == null) {
                    return;
                }
                report = data;
                noOperationsView.setVisibility((report.last == null) ? View.VISIBLE : View.GONE);
                hideLoadingDataView();
                events.notify(EVENT_LISTENER_KEY_ON_LOAD_OPERATION_REPORT, new EventListenerManager.OnIterateListener() {
                    @Override
                    public void onIterate(EventListener listener) {
                        ((OnLoadOperationReportListener) listener).onLoadOperationReport(report);
                    }
                });
            }
            @Override
            public void onLoaderReset(Loader<OperationReport> loader) {}
        };
    }

    private LoaderManager.LoaderCallbacks<CategoryList> getCategoryLoaderCallbacks() {
        return new LoaderManager.LoaderCallbacks<CategoryList>() {
            @Override
            public Loader<CategoryList> onCreateLoader(int id, Bundle params) {
                return new CategoryLoader(getApplicationContext(), ((CustomApplication) getApplication()).getDbHelper());
            }
            @Override
            public void onLoadFinished(Loader<CategoryList> loader, CategoryList data) {
                if (data == null) {
                    return;
                }
                categories = data;
                getLoaderManager().restartLoader(TARGET_LOADER_ID, null, getTargetLoaderCallbacks());
                events.notify(EVENT_LISTENER_KEY_ON_LOAD_CATEGORIES, new EventListenerManager.OnIterateListener() {
                    @Override
                    public void onIterate(EventListener listener) {
                        ((OnLoadCategoriesListener) listener).onLoadCategories(categories);
                    }
                });
            }
            @Override
            public void onLoaderReset(Loader<CategoryList> loader) {}
        };
    }

    private LoaderManager.LoaderCallbacks<TargetList> getTargetLoaderCallbacks() {
        return new LoaderManager.LoaderCallbacks<TargetList>() {
            @Override
            public Loader<TargetList> onCreateLoader(int id, Bundle params) {
                return new TargetLoader(getApplicationContext(), ((CustomApplication) getApplication()).getDbHelper(), categories);
            }
            @Override
            public void onLoadFinished(Loader<TargetList> loader, TargetList data) {
                if (data == null) {
                    return;
                }
                targets = data;
                updateWithNoCategoryCount();
                hideLoadingDataView();
                events.notify(EVENT_LISTENER_KEY_ON_LOAD_TARGETS, new EventListenerManager.OnIterateListener() {
                    @Override
                    public void onIterate(EventListener listener) {
                        ((OnLoadTargetsListener) listener).onLoadTargets(targets);
                    }
                });
                if (currentTarget != 0) {
                    events.notify(EVENT_LISTENER_KEY_ON_START_EDIT_TARGET, new EventListenerManager.OnIterateListener() {
                        @Override
                        public void onIterate(EventListener listener) {
                            ((OnStartEditTargetListener) listener).onStartEditTarget(targets.getById(currentTarget));
                        }
                    });
                }
                for (Map.Entry<Integer, DateTime> item: operationLoaders.entrySet()) {
                    getLoaderManager().restartLoader(item.getKey(), null, getOperationLoaderCallbacks(item.getValue()));
                }
            }
            @Override
            public void onLoaderReset(Loader<TargetList> loader) {}
        };
    }

    private LoaderManager.LoaderCallbacks<MonthOperationList> getOperationLoaderCallbacks(final DateTime date) {
        return new LoaderManager.LoaderCallbacks<MonthOperationList>() {
            @Override
            public Loader<MonthOperationList> onCreateLoader(int id, Bundle args) {
                return new OperationLoader(getApplicationContext(), ((CustomApplication) getApplication()).getDbHelper(), targets, date);
            }
            @Override
            public void onLoadFinished(Loader<MonthOperationList> loader, final MonthOperationList data) {
                if (data != null) {
                    _operations.put(data.month, data);
                    events.notify(EVENT_LISTENER_KEY_ON_LOAD_OPERATIONS, new EventListenerManager.OnIterateListener() {
                        @Override
                        public void onIterate(EventListener listener) {
                            ((OnLoadOperationsListener) listener).onLoadOperations(data);
                        }
                    });
                }
            }
            @Override
            public void onLoaderReset(Loader<MonthOperationList> loader) {}
        };
    }

    private void setupView() {
        setContentView(R.layout.a__main);
        loadingMessagesView = findViewById(R.id.a__main__loading_messages);
        loadingMessagesProgressView = (ProgressBar) findViewById(R.id.a__main__loading_messages_progress);
        loadingDataView = findViewById(R.id.a__main__loading_data);
        noOperationsView = findViewById(R.id.a__main__no_operations);
    }

    private void hideLoadingDataView() {
        if (report != null && targets != null) {
            loadingDataView.setVisibility(View.GONE);
        }
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
    protected void onSaveInstanceState(@NonNull Bundle state) {
        if (currentTarget != 0) {
            state.putInt(STATE_KEY_TARGET, currentTarget);
        }
        super.onSaveInstanceState(state);
    }

    @Override
    public void addOnLoadOperationReportListener(OnLoadOperationReportListener listener) {
        events.add(EVENT_LISTENER_KEY_ON_LOAD_OPERATION_REPORT, listener);
        if (report != null) {
            listener.onLoadOperationReport(report);
        }
    }

    @Override
    public void removeOnLoadOperationReportListener(OnLoadOperationReportListener listener) {
        events.remove(EVENT_LISTENER_KEY_ON_LOAD_OPERATION_REPORT, listener);
    }

    @Override
    public void loadOperations(DateTime date) {
        int id = getOperationLoaderId(date);
        getLoaderManager().initLoader(id, null, getOperationLoaderCallbacks(date));
        operationLoaders.put(id, date);
    }

    private int getOperationLoaderId(DateTime date) {
        return date.getYear() * 100 + date.getMonthOfYear();
    }

    @Override
    public void addOnLoadOperationsListener(OnLoadOperationsListener listener) {
        events.add(EVENT_LISTENER_KEY_ON_LOAD_OPERATIONS, listener);
        for (MonthOperationList operations: _operations.values()) {
            listener.onLoadOperations(operations);
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
    public void startEditTarget(final Target target) {
        currentTarget = target.id;
        events.notify(EVENT_LISTENER_KEY_ON_START_EDIT_TARGET, new EventListenerManager.OnIterateListener() {
            @Override
            public void onIterate(EventListener listener) {
                ((OnStartEditTargetListener) listener).onStartEditTarget(target);
            }
        });
        FragmentManager manager = getFragmentManager();
        TargetFragment fragment = (TargetFragment) manager.findFragmentByTag(FRAGMENT_TARGET_TAG);
        if (fragment == null || fragment.isDismissing()) {
            (new TargetFragment()).show(manager, FRAGMENT_TARGET_TAG);
        }
    }

    @Override
    public void finishEditTarget(Target target, boolean editNext) {
        updateWithNoCategoryCount();
        events.notify(EVENT_LISTENER_KEY_ON_EDIT_TARGET, new EventListenerManager.OnIterateListener() {
            @Override
            public void onIterate(EventListener listener) {
                ((OnEditTargetListener) listener).onEditTarget();
            }
        });
        for (int loaderId: operationLoaders.keySet()) {
            getLoaderManager().getLoader(loaderId).onContentChanged();
        }
        DbTargetWriter writer = new DbTargetWriter(((CustomApplication) getApplication()).getDbHelper());
        writer.write(target);
        currentTarget = 0;
        if (editNext) {
            Target nextTarget = getNextTargetToEdit(targets, target);
            startEditTarget(nextTarget);
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
        if (targets != null && currentTarget != 0) {
            listener.onStartEditTarget(targets.getById(currentTarget));
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
