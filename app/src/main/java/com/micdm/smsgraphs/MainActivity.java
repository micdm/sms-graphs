package com.micdm.smsgraphs;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.data.Operation;
import com.micdm.smsgraphs.data.OperationReport;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.db.writers.DbCategoryWriter;
import com.micdm.smsgraphs.db.writers.DbOperationWriter;
import com.micdm.smsgraphs.db.writers.DbTargetWriter;
import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventManager;
import com.micdm.smsgraphs.events.EventType;
import com.micdm.smsgraphs.events.events.EditCategoryEvent;
import com.micdm.smsgraphs.events.events.EditTargetEvent;
import com.micdm.smsgraphs.events.events.FinishLoadMessagesEvent;
import com.micdm.smsgraphs.events.events.LoadCategoriesEvent;
import com.micdm.smsgraphs.events.events.LoadOperationReportEvent;
import com.micdm.smsgraphs.events.events.LoadOperationsEvent;
import com.micdm.smsgraphs.events.events.LoadTargetsEvent;
import com.micdm.smsgraphs.events.events.ProgressLoadMessagesEvent;
import com.micdm.smsgraphs.events.events.RequestEditTargetEvent;
import com.micdm.smsgraphs.events.events.RequestLoadOperationsEvent;
import com.micdm.smsgraphs.events.events.RequestSetOperationIgnoredEvent;
import com.micdm.smsgraphs.events.events.StartLoadMessagesEvent;
import com.micdm.smsgraphs.fragments.CategoryListFragment;
import com.micdm.smsgraphs.fragments.StatsFragment;
import com.micdm.smsgraphs.fragments.TargetFragment;
import com.micdm.smsgraphs.fragments.TargetListFragment;
import com.micdm.smsgraphs.loaders.CategoryLoader;
import com.micdm.smsgraphs.loaders.OperationLoader;
import com.micdm.smsgraphs.loaders.OperationReportLoader;
import com.micdm.smsgraphs.loaders.TargetLoader;
import com.micdm.smsgraphs.messages.MessageService;
import com.micdm.smsgraphs.misc.PagerActivity;
import com.micdm.smsgraphs.misc.PagerAdapter;
import com.micdm.smsgraphs.parcels.TargetParcel;

import org.joda.time.DateTime;

import java.util.Hashtable;
import java.util.Map;

// TODO: при первом запуске показать обучение
// TODO: падает, если в системе не русский язык
public class MainActivity extends PagerActivity {

    private static final int OPERATION_REPORT_LOADER_ID = 0;
    private static final int CATEGORY_LOADER_ID = 1;
    private static final int TARGET_LOADER_ID = 2;

    private static final String FRAGMENT_TARGET_TAG = "target";

    private OperationReport _report;
    private CategoryList _categories;
    private TargetList _targets;

    private final Map<Integer, DateTime> _operationLoaders = new Hashtable<Integer, DateTime>();

    private View _loadingMessagesView;
    private ProgressBar _loadingMessagesProgressView;
    private View _loadingDataView;
    private View _noOperationsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startMessageService();
        initLoaders();
        setupView();
        setupActionBar();
        setupPager((ViewPager) findViewById(R.id.a__main__pager));
    }

    private void startMessageService() {
        Intent intent = new Intent(this, MessageService.class);
        startService(intent);
    }

    private void initLoaders() {
        LoaderManager manager = getLoaderManager();
        manager.initLoader(OPERATION_REPORT_LOADER_ID, null, getOperationReportLoaderCallbacks());
        manager.initLoader(CATEGORY_LOADER_ID, null, getCategoryLoaderCallbacks());
        manager.initLoader(TARGET_LOADER_ID, null, getTargetLoaderCallbacks());
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
                _report = data;
                _noOperationsView.setVisibility((_report.getLast() == null) ? View.VISIBLE : View.GONE);
                hideLoadingDataView();
                ((CustomApplication) getApplication()).getEventManager().publish(new LoadOperationReportEvent(data));
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
                _categories = data;
                // TODO: может, перезапускать только если категории изменились?
                getLoaderManager().restartLoader(TARGET_LOADER_ID, null, getTargetLoaderCallbacks());
                ((CustomApplication) getApplication()).getEventManager().publish(new LoadCategoriesEvent(data));
            }
            @Override
            public void onLoaderReset(Loader<CategoryList> loader) {}
        };
    }

    private LoaderManager.LoaderCallbacks<TargetList> getTargetLoaderCallbacks() {
        return new LoaderManager.LoaderCallbacks<TargetList>() {
            @Override
            public Loader<TargetList> onCreateLoader(int id, Bundle params) {
                return new TargetLoader(getApplicationContext(), ((CustomApplication) getApplication()).getDbHelper(), _categories);
            }
            @Override
            public void onLoadFinished(Loader<TargetList> loader, TargetList data) {
                if (data == null) {
                    return;
                }
                _targets = data;
                updateWithNoCategoryCount();
                hideLoadingDataView();
                ((CustomApplication) getApplication()).getEventManager().publish(new LoadTargetsEvent(data));
                for (Map.Entry<Integer, DateTime> item: _operationLoaders.entrySet()) {
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
                return new OperationLoader(getApplicationContext(), ((CustomApplication) getApplication()).getDbHelper(), _targets, date);
            }
            @Override
            public void onLoadFinished(Loader<MonthOperationList> loader, final MonthOperationList data) {
                if (data != null) {
                    ((CustomApplication) getApplication()).getEventManager().publish(new LoadOperationsEvent(data));
                }
            }
            @Override
            public void onLoaderReset(Loader<MonthOperationList> loader) {}
        };
    }

    private void setupView() {
        setContentView(R.layout.a__main);
        _loadingMessagesView = findViewById(R.id.a__main__loading_messages);
        _loadingMessagesProgressView = (ProgressBar) findViewById(R.id.a__main__loading_messages_progress);
        _loadingDataView = findViewById(R.id.a__main__loading_data);
        _noOperationsView = findViewById(R.id.a__main__no_operations);
    }

    private void hideLoadingDataView() {
        if (_report != null && _targets != null) {
            _loadingDataView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setupPager(ViewPager pager) {
        super.setupPager(pager);
        addStatsPage(pager);
        addCategoryListPage(pager);
        addTargetListPage(pager);
    }

    private void addStatsPage(ViewPager pager) {
        String title = getString(R.string.tab_title_stats);
        addTab(pager, title);
        addPage(pager, new PagerAdapter.Page(title, new StatsFragment()));
    }

    private void addCategoryListPage(ViewPager pager) {
        String title = getString(R.string.tab_title_categories);
        addTab(pager, title);
        addPage(pager, new PagerAdapter.Page(title, new CategoryListFragment()));
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
        View view = actionBar.getTabAt(2).getCustomView();
        TextView countView = (TextView) view.findViewById(R.id.v__actionbar__target_list_tab__count);
        int count = _targets.getWithNoCategoryCount();
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
        subscribeForEvents();
    }

    // TODO: не отписываться от событий сервиса при onStop
    private void subscribeForEvents() {
        EventManager manager = ((CustomApplication) getApplication()).getEventManager();
        manager.subscribe(this, EventType.START_LOAD_MESSAGES, new EventManager.OnEventListener<StartLoadMessagesEvent>() {
            @Override
            public void onEvent(StartLoadMessagesEvent event) {
                _loadingMessagesView.setVisibility(View.VISIBLE);
            }
        });
        manager.subscribe(this, EventType.PROGRESS_LOAD_MESSAGES, new EventManager.OnEventListener<ProgressLoadMessagesEvent>() {
            @Override
            public void onEvent(ProgressLoadMessagesEvent event) {
                _loadingMessagesProgressView.setMax(event.getTotal());
                _loadingMessagesProgressView.setProgress(event.getCurrent());
            }
        });
        manager.subscribe(this, EventType.FINISH_LOAD_MESSAGES, new EventManager.OnEventListener<FinishLoadMessagesEvent>() {
            @Override
            public void onEvent(FinishLoadMessagesEvent event) {
                LoaderManager manager = getLoaderManager();
                manager.getLoader(OPERATION_REPORT_LOADER_ID).onContentChanged();
                manager.getLoader(TARGET_LOADER_ID).onContentChanged();
                _loadingMessagesView.setVisibility(View.GONE);
            }
        });
        manager.subscribe(this, EventType.REQUEST_LOAD_OPERATION_REPORT, new EventManager.OnEventListener<Event>() {
            @Override
            public void onEvent(Event event) {
                getLoaderManager().initLoader(OPERATION_REPORT_LOADER_ID, null, getOperationReportLoaderCallbacks());
            }
        });
        manager.subscribe(this, EventType.REQUEST_LOAD_CATEGORIES, new EventManager.OnEventListener<Event>() {
            @Override
            public void onEvent(Event event) {
                getLoaderManager().initLoader(CATEGORY_LOADER_ID, null, getCategoryLoaderCallbacks());
            }
        });
        manager.subscribe(this, EventType.REQUEST_LOAD_TARGETS, new EventManager.OnEventListener<Event>() {
            @Override
            public void onEvent(Event event) {
                getLoaderManager().initLoader(TARGET_LOADER_ID, null, getTargetLoaderCallbacks());
            }
        });
        manager.subscribe(this, EventType.REQUEST_LOAD_OPERATIONS, new EventManager.OnEventListener<RequestLoadOperationsEvent>() {
            @Override
            public void onEvent(RequestLoadOperationsEvent event) {
                DateTime date = event.getDate();
                int id = getOperationLoaderId(date);
                getLoaderManager().initLoader(id, null, getOperationLoaderCallbacks(date));
                _operationLoaders.put(id, date);
            }
        });
        manager.subscribe(this, EventType.REQUEST_SET_OPERATION_IGNORED, new EventManager.OnEventListener<RequestSetOperationIgnoredEvent>() {
            @Override
            public void onEvent(RequestSetOperationIgnoredEvent event) {
                Operation operation = event.getOperation();
                boolean needIgnore = event.needIgnore();
                operation.setIgnored(needIgnore);
                DbOperationWriter writer = new DbOperationWriter(((CustomApplication) getApplication()).getDbHelper());
                writer.update(operation);
                int loaderId = getOperationLoaderId(operation.getCreated());
                getLoaderManager().getLoader(loaderId).onContentChanged();
            }
        });
        manager.subscribe(this, EventType.EDIT_CATEGORY, new EventManager.OnEventListener<EditCategoryEvent>() {
            @Override
            public void onEvent(EditCategoryEvent event) {
                editCategory(event.getCategory());
            }
        });
        manager.subscribe(this, EventType.REQUEST_EDIT_TARGET, new EventManager.OnEventListener<RequestEditTargetEvent>() {
            @Override
            public void onEvent(RequestEditTargetEvent event) {
                requestEditTarget(event.getTarget());
            }
        });
        manager.subscribe(this, EventType.EDIT_TARGET, new EventManager.OnEventListener<EditTargetEvent>() {
            @Override
            public void onEvent(EditTargetEvent event) {
                editTarget(event.getTarget(), event.needEditNext());
            }
        });
    }

    private int getOperationLoaderId(DateTime date) {
        return date.getYear() * 100 + date.getMonthOfYear();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((CustomApplication) getApplication()).getEventManager().unsubscribeAll(this);
    }

    private void editCategory(Category category) {
        DbCategoryWriter writer = new DbCategoryWriter(((CustomApplication) getApplication()).getDbHelper());
        if (category.getId() == 0) {
            writer.add(category);
        } else {
            writer.update(category);
        }
        notifyLoadersOnEditCategory();
    }

    private void notifyLoadersOnEditCategory() {
        getLoaderManager().getLoader(CATEGORY_LOADER_ID).onContentChanged();
    }

    private void requestEditTarget(Target target) {
        FragmentManager manager = getFragmentManager();
        TargetFragment fragment = (TargetFragment) manager.findFragmentByTag(FRAGMENT_TARGET_TAG);
        if (fragment == null || fragment.isDismissing()) {
            fragment = new TargetFragment();
            fragment.setArguments(getTargetFragmentArguments(target));
            fragment.show(manager, FRAGMENT_TARGET_TAG);
        }
    }

    private Bundle getTargetFragmentArguments(Target target) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(TargetFragment.INIT_ARG_TARGET, new TargetParcel(target));
        return arguments;
    }

    private void editTarget(Target edited, boolean editNext) {
        Target target = updateTarget(edited);
        updateWithNoCategoryCount();
        DbTargetWriter writer = new DbTargetWriter(((CustomApplication) getApplication()).getDbHelper());
        writer.update(target);
        notifyLoadersOnEditTarget();
        if (editNext) {
            Target nextTarget = getNextTargetToEdit(_targets, target);
            requestEditTarget(nextTarget);
        }
    }

    private Target updateTarget(Target edited) {
        Target target = _targets.getById(edited.getId());
        target.setCategory(edited.getCategory());
        target.setTitle(edited.getTitle());
        return target;
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
            if (next.getCategory() == null) {
                return next;
            }
            i += 1;
        }
        return (index + 1 == targets.size()) ? targets.get(0) : targets.get(index + 1);
    }

    private void notifyLoadersOnEditTarget() {
        getLoaderManager().getLoader(TARGET_LOADER_ID).onContentChanged();
        for (int loaderId: _operationLoaders.keySet()) {
            getLoaderManager().getLoader(loaderId).onContentChanged();
        }
    }
}
