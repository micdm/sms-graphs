package com.micdm.smsgraphs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.micdm.smsgraphs.CustomApplication;
import com.micdm.smsgraphs.R;
import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.events.EventManager;
import com.micdm.smsgraphs.events.EventType;
import com.micdm.smsgraphs.events.events.LoadTargetsEvent;
import com.micdm.smsgraphs.events.events.RequestEditTargetEvent;
import com.micdm.smsgraphs.events.events.RequestLoadTargetsEvent;
import com.micdm.smsgraphs.misc.DateUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class TargetListFragment extends Fragment {

    private class TargetListAdapter extends BaseExpandableListAdapter {

        private List<Category> _keys;
        private List<List<Target>> _values;

        public void setTargets(SortedMap<Category, List<Target>> targets) {
            _keys = new ArrayList<Category>(targets.keySet());
            _values = new ArrayList<List<Target>>(targets.values());
        }

        @Override
        public int getGroupCount() {
            return (_keys == null) ? 0 : _keys.size();
        }

        @Override
        public int getChildrenCount(int position) {
            return _values.get(position).size();
        }

        @Override
        public Category getGroup(int position) {
            return _keys.get(position);
        }

        @Override
        public Target getChild(int groupPosition, int childPosition) {
            return _values.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int position) {
            return getGroup(position).getId();
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return getChild(groupPosition, childPosition).getId();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int position, boolean isExpanded, View view, ViewGroup parent) {
            if (view == null) {
                view = View.inflate(getActivity(), R.layout.v__target_list__category, null);
            }
            Category category = getGroup(position);
            view.setBackgroundColor(getCategoryBackgroundColor(category));
            TextView nameView = (TextView) view.findViewById(R.id.v__target_list__category__name);
            nameView.setText(category.getName());
            return view;
        }

        private int getCategoryBackgroundColor(Category category) {
            int id = (category.getId() == 0) ? R.color.targets_no_category_background : R.color.targets_category_background;
            return getResources().getColor(id);
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
            if (view == null) {
                view = View.inflate(getActivity(), R.layout.v__target_list__target, null);
            }
            Target target = getChild(groupPosition, childPosition);
            TextView titleView = (TextView) view.findViewById(R.id.v__target_list__list_item__title);
            TextView nameView = (TextView) view.findViewById(R.id.v__target_list__list_item__name);
            String title = target.getTitle();
            if (title == null) {
                titleView.setText(target.getName());
                nameView.setVisibility(View.GONE);
            } else {
                titleView.setText(title);
                nameView.setVisibility(View.VISIBLE);
                nameView.setText(target.getName());
            }
            TextView lastOperationView = (TextView) view.findViewById(R.id.v__target_list__list_item__last_operation);
            int lastAmount = target.getLastAmount();
            lastOperationView.setText(getString(R.string.fragment_target_list_last_operation,
                    DateUtils.formatForHuman(target.getLastPaid()), lastAmount, getResources().getQuantityString(R.plurals.rubles, lastAmount)));
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public Integer getNoCategoryGroupIndex() {
            for (int i = 0; i < _keys.size(); i += 1) {
                if (_keys.get(i).getId() == 0) {
                    return i;
                }
            }
            return null;
        }
    }

    private static final Comparator<Category> CATEGORY_COMPARATOR = new Comparator<Category>() {
        @Override
        public int compare(Category a, Category b) {
            if (a.equals(b)) {
                return 0;
            }
            if (a.getId() == 0) {
                return -1;
            }
            if (b.getId() == 0) {
                return 1;
            }
            return a.getName().compareTo(b.getName());
        }
    };

    private ExpandableListView _targetsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _targetsView = (ExpandableListView) inflater.inflate(R.layout.f__target_list, null);
        _targetsView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Target target = ((TargetListAdapter) _targetsView.getExpandableListAdapter()).getChild(groupPosition, childPosition);
                getEventManager().publish(new RequestEditTargetEvent(target));
                return true;
            }
        });
        return _targetsView;
    }

    @Override
    public void onStart() {
        super.onStart();
        subscribeForEvents();
        getEventManager().publish(new RequestLoadTargetsEvent());
    }

    private void subscribeForEvents() {
        EventManager manager = getEventManager();
        manager.subscribe(this, EventType.LOAD_TARGETS, new EventManager.OnEventListener<LoadTargetsEvent>() {
            @Override
            public void onEvent(LoadTargetsEvent event) {
                TargetListAdapter adapter = (TargetListAdapter) _targetsView.getExpandableListAdapter();
                boolean needExpandNoCategoryGroup = false;
                if (adapter == null) {
                    adapter = new TargetListAdapter();
                    _targetsView.setAdapter(adapter);
                    needExpandNoCategoryGroup = true;
                }
                adapter.setTargets(getGroupedTargets(event.getTargets()));
                adapter.notifyDataSetChanged();
                if (needExpandNoCategoryGroup) {
                    Integer index = adapter.getNoCategoryGroupIndex();
                    if (index != null) {
                        _targetsView.expandGroup(index);
                    }
                }
            }
        });
    }

    private EventManager getEventManager() {
        return ((CustomApplication) getActivity().getApplication()).getEventManager();
    }

    private SortedMap<Category, List<Target>> getGroupedTargets(TargetList targets) {
        SortedMap<Category, List<Target>> grouped = new TreeMap<Category, List<Target>>(CATEGORY_COMPARATOR);
        Category noCategory = new Category(0, getString(R.string.no_category));
        for (Target target: targets) {
            Category category = target.getCategory();
            if (category == null) {
                category = noCategory;
            }
            if (!grouped.containsKey(category)) {
                grouped.put(category, new ArrayList<Target>());
            }
            grouped.get(category).add(target);
        }
        return grouped;
    }

    @Override
    public void onStop() {
        super.onStop();
        getEventManager().unsubscribeAll(this);
    }
}
