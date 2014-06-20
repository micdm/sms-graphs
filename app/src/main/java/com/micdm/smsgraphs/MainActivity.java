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
import com.micdm.smsgraphs.loaders.CategoryLoader;
import com.micdm.smsgraphs.loaders.MessageLoader;
import com.micdm.smsgraphs.loaders.OperationLoader;
import com.micdm.smsgraphs.loaders.OperationReportLoader;
import com.micdm.smsgraphs.loaders.TargetLoader;
import com.micdm.utils.events.EventListener;
import com.micdm.utils.events.EventListenerManager;
import com.micdm.utils.pager.PagerActivity;
import com.micdm.utils.pager.PagerAdapter;

import java.util.Calendar;

// TODO: при первом запуске показать обучение
public class MainActivity extends PagerActivity implements OperationHandler, CategoryHandler, TargetHandler {

    private static final int MESSAGE_LOADER_ID = 0;
    private static final int OPERATION_REPORT_LOADER_ID = 1;
    private static final int CATEGORY_LOADER_ID = 2;
    private static final int TARGET_LOADER_ID = 3;
    private static final int OPERATION_LOADER_ID = 4;

    private static final String FRAGMENT_TARGET_TAG = "target";

    private static final String EVENT_LISTENER_KEY_ON_LOAD_OPERATIONS = "OnLoadOperations";
    private static final String EVENT_LISTENER_KEY_ON_LOAD_CATEGORIES = "OnLoadCategories";
    private static final String EVENT_LISTENER_KEY_ON_LOAD_TARGETS = "OnLoadTargets";
    private static final String EVENT_LISTENER_KEY_ON_START_EDIT_TARGET = "OnStartEditTarget";
    private static final String EVENT_LISTENER_KEY_ON_EDIT_TARGET = "OnEditTarget";

    private final DbHelper dbHelper = new DbHelper(this);
    private final EventListenerManager events = new EventListenerManager();

    private OperationReport report;
    private CategoryList categories;
    private TargetList targets;
    private Calendar month;
    private MonthOperationList operations;
    private Target currentTarget;

    private View loadingTargetsView;
    private View loadingCategoriesView;
    private View noOperationsView;
    private View loadingOperationReportView;
    private View loadingMessagesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLoaders();
        setupView();
        setupActionBar();
        setupPager((ViewPager) findViewById(R.id.a__main__pager));
    }

    private void initLoaders() {
        LoaderManager manager = getLoaderManager();
        manager.initLoader(MESSAGE_LOADER_ID, null, getMessageLoaderCallbacks());
        manager.initLoader(OPERATION_REPORT_LOADER_ID, null, getOperationReportLoaderCallbacks());
        manager.initLoader(CATEGORY_LOADER_ID, null, getCategoryLoaderCallbacks());
        manager.initLoader(TARGET_LOADER_ID, null, getTargetLoaderCallbacks());
        manager.initLoader(OPERATION_LOADER_ID, null, getOperationLoaderCallbacks());
    }

    private LoaderManager.LoaderCallbacks getMessageLoaderCallbacks() {
        final Context context = this;
        return new LoaderManager.LoaderCallbacks<Integer>() {
            @Override
            public Loader<Integer> onCreateLoader(int id, Bundle params) {
                return new MessageLoader(context, dbHelper, new MessageLoader.OnLoadListener() {
                    @Override
                    public void onStartLoad() {
                        loadingMessagesView.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onFinishLoad() {
                        loadingMessagesView.setVisibility(View.GONE);
                    }
                });
            }
            @Override
            public void onLoadFinished(Loader<Integer> loader, Integer result) {
                if (result != 0) {
                    for (int id: new int[] {OPERATION_REPORT_LOADER_ID, TARGET_LOADER_ID, OPERATION_LOADER_ID}) {
                        getLoaderManager().getLoader(id).onContentChanged();
                    }
                }
            }
            @Override
            public void onLoaderReset(Loader<Integer> loader) {}
        };
    }

    private LoaderManager.LoaderCallbacks getOperationReportLoaderCallbacks() {
        final Context context = this;
        return new LoaderManager.LoaderCallbacks<OperationReport>() {
            @Override
            public Loader<OperationReport> onCreateLoader(int id, Bundle params) {
                return new OperationReportLoader(context, dbHelper, new OperationReportLoader.OnLoadListener() {
                    @Override
                    public void onStartLoad() {
                        loadingOperationReportView.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onFinishLoad() {
                        loadingOperationReportView.setVisibility(View.GONE);
                    }
                });
            }
            @Override
            public void onLoadFinished(Loader<OperationReport> loader, OperationReport loaded) {
                report = loaded;
                noOperationsView.setVisibility((report.last == null) ? View.VISIBLE : View.GONE);
            }
            @Override
            public void onLoaderReset(Loader<OperationReport> loader) {}
        };
    }

    private LoaderManager.LoaderCallbacks getCategoryLoaderCallbacks() {
        final Context context = this;
        return new LoaderManager.LoaderCallbacks<CategoryList>() {
            @Override
            public Loader<CategoryList> onCreateLoader(int id, Bundle params) {
                return new CategoryLoader(context, dbHelper, new CategoryLoader.OnLoadListener() {
                    @Override
                    public void onStartLoad() {
                        loadingCategoriesView.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onFinishLoad() {
                        loadingCategoriesView.setVisibility(View.GONE);
                    }
                });
            }
            @Override
            public void onLoadFinished(Loader<CategoryList> loader, CategoryList loaded) {
                categories = loaded;
                events.notify(EVENT_LISTENER_KEY_ON_LOAD_CATEGORIES, new EventListenerManager.OnIterateListener() {
                    @Override
                    public void onIterate(EventListener listener) {
                        ((OnLoadCategoriesListener) listener).onLoadCategories(categories);
                    }
                });
                getLoaderManager().restartLoader(TARGET_LOADER_ID, null, getTargetLoaderCallbacks());

            }
            @Override
            public void onLoaderReset(Loader<CategoryList> loader) {}
        };
    }

    private LoaderManager.LoaderCallbacks getTargetLoaderCallbacks() {
        final Context context = this;
        return new LoaderManager.LoaderCallbacks<TargetList>() {
            @Override
            public Loader<TargetList> onCreateLoader(int id, Bundle params) {
                return new TargetLoader(context, dbHelper, categories, new TargetLoader.OnLoadListener() {
                    @Override
                    public void onStartLoad() {
                        loadingTargetsView.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onFinishLoad() {
                        loadingTargetsView.setVisibility(View.GONE);
                    }
                });
            }
            @Override
            public void onLoadFinished(Loader<TargetList> loader, TargetList loaded) {
                targets = loaded;
                if (targets != null) {
                    updateWithNoCategoryCount();
                    events.notify(EVENT_LISTENER_KEY_ON_LOAD_TARGETS, new EventListenerManager.OnIterateListener() {
                        @Override
                        public void onIterate(EventListener listener) {
                            ((OnLoadTargetsListener) listener).onLoadTargets(targets);
                        }
                    });
                    Calendar mnth = (month == null) ? getLastMonth() : month;
                    if (mnth != null) {
                        loadMonthOperations(mnth);
                    }
                }
            }
            @Override
            public void onLoaderReset(Loader<TargetList> loader) {}
        };
    }

    private LoaderManager.LoaderCallbacks getOperationLoaderCallbacks() {
        final Context context = this;
        return new LoaderManager.LoaderCallbacks<MonthOperationList>() {
            @Override
            public Loader<MonthOperationList> onCreateLoader(int id, Bundle params) {
                return new OperationLoader(context, dbHelper, targets, month, new OperationLoader.OnLoadListener() {
                    @Override
                    public void onStartLoad() {
                        events.notify(EVENT_LISTENER_KEY_ON_LOAD_OPERATIONS, new EventListenerManager.OnIterateListener() {
                            @Override
                            public void onIterate(EventListener listener) {
                                ((OnLoadOperationsListener) listener).onStartLoadOperations(month);
                            }
                        });
                    }
                    @Override
                    public void onFinishLoad() {
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
            public void onLoadFinished(Loader<MonthOperationList> loader, MonthOperationList loaded) {
                operations = loaded;
                if (operations != null) {
                    events.notify(EVENT_LISTENER_KEY_ON_LOAD_OPERATIONS, new EventListenerManager.OnIterateListener() {
                        @Override
                        public void onIterate(EventListener listener) {
                            ((OnLoadOperationsListener) listener).onLoadOperations(operations, hasPreviousMonth(), hasNextMonth());
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
        loadingOperationReportView = findViewById(R.id.a__main__loading_operation_report);
        noOperationsView = findViewById(R.id.a__main__no_operations);
        loadingCategoriesView = findViewById(R.id.a__main__loading_categories);
        loadingTargetsView = findViewById(R.id.a__main__loading_targets);
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
        getLoaderManager().getLoader(MESSAGE_LOADER_ID).onContentChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    private Calendar getLastMonth() {
        if (report == null || report.last == null) {
            return null;
        }
        Calendar month = (Calendar) report.last.clone();
        month.set(Calendar.DAY_OF_MONTH, 1);
        return month;
    }

    private void loadMonthOperations(Calendar month) {
        this.month = month;
        getLoaderManager().restartLoader(OPERATION_LOADER_ID, null, getOperationLoaderCallbacks());
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
                ((OnEditTargetListener) listener).onEditTarget(currentTarget);
            }
        });
        loadMonthOperations(month);
        DbTargetWriter writer = new DbTargetWriter(dbHelper);
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
