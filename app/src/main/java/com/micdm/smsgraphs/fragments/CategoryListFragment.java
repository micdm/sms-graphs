package com.micdm.smsgraphs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.micdm.smsgraphs.CustomApplication;
import com.micdm.smsgraphs.R;
import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.events.EventManager;
import com.micdm.smsgraphs.events.EventType;
import com.micdm.smsgraphs.events.events.LoadCategoriesEvent;
import com.micdm.smsgraphs.events.events.RequestLoadCategoriesEvent;
import com.micdm.smsgraphs.parcels.CategoryParcel;

public class CategoryListFragment extends Fragment {

    private class CategoryListAdapter extends BaseAdapter {

        private CategoryList _categories;

        public void setCategories(CategoryList categories) {
            _categories = categories;
        }

        @Override
        public int getCount() {
            return (_categories == null) ? 0 : _categories.size();
        }

        @Override
        public Category getItem(int position) {
            return _categories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(getActivity(), R.layout.v__category_list__list_item, null);
            }
            Category category = getItem(position);
            ((TextView) view).setText(category.getName());
            return view;
        }
    }

    private static final String FRAGMENT_CATEGORY_TAG = "category";

    private ListView _categoriesView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f__category_list, null);
        _categoriesView = (ListView) view.findViewById(R.id.f__category_list__categories);
        _categoriesView.setEmptyView(view.findViewById(R.id.f__category_list__no_items));
        _categoriesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category category = ((CategoryListAdapter) parent.getAdapter()).getItem(position);
                showCategoryFragment(category);
            }
        });
        View addView = view.findViewById(R.id.f__category_list__add);
        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryFragment(null);
            }
        });
        return view;
    }

    private void showCategoryFragment(Category category) {
        FragmentManager manager = getChildFragmentManager();
        if (manager.findFragmentByTag(FRAGMENT_CATEGORY_TAG) == null) {
            CategoryFragment fragment = new CategoryFragment();
            fragment.setArguments(getCategoryFragmentArguments(category));
            fragment.show(manager, FRAGMENT_CATEGORY_TAG);
        }
    }

    private Bundle getCategoryFragmentArguments(Category category) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(CategoryFragment.INIT_ARG_CATEGORY, (category == null) ? null : new CategoryParcel(category));
        return arguments;
    }

    @Override
    public void onStart() {
        super.onStart();
        subscribeForEvents();
        getEventManager().publish(new RequestLoadCategoriesEvent());
    }

    private void subscribeForEvents() {
        EventManager manager = getEventManager();
        manager.subscribe(this, EventType.LOAD_CATEGORIES, new EventManager.OnEventListener<LoadCategoriesEvent>() {
            @Override
            public void onEvent(LoadCategoriesEvent event) {
                CategoryListAdapter adapter = (CategoryListAdapter) _categoriesView.getAdapter();
                if (adapter == null) {
                    adapter = new CategoryListAdapter();
                    _categoriesView.setAdapter(adapter);
                }
                adapter.setCategories(event.getCategories());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private EventManager getEventManager() {
        return ((CustomApplication) getActivity().getApplication()).getEventManager();
    }

    @Override
    public void onStop() {
        super.onStop();
        getEventManager().unsubscribeAll(this);
    }
}
