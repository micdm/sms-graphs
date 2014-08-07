package com.micdm.smsgraphs.fragments;

import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
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

public class TargetListFragment extends ListFragment {

    private class TargetListAdapter extends BaseAdapter {

        private TargetList _targets;

        public void setTargets(TargetList targets) {
            _targets = targets;
        }

        @Override
        public int getCount() {
            return (_targets == null) ? 0 : _targets.size();
        }

        @Override
        public Target getItem(int position) {
            return _targets.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(getActivity(), R.layout.v__target_list__list_item, null);
            }
            Target target = getItem(position);
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
            TextView categoryView = (TextView) view.findViewById(R.id.v__target_list__list_item__category);
            View noCategoryView = view.findViewById(R.id.v__target_list__list_item__no_category);
            Category category = target.getCategory();
            if (category == null) {
                categoryView.setVisibility(View.GONE);
                noCategoryView.setVisibility(View.VISIBLE);
            } else {
                categoryView.setVisibility(View.VISIBLE);
                categoryView.setText(category.getName());
                noCategoryView.setVisibility(View.GONE);
            }
            return view;
        }
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
                TargetListAdapter adapter = (TargetListAdapter) getListAdapter();
                if (adapter == null) {
                    adapter = new TargetListAdapter();
                    setListAdapter(adapter);
                }
                adapter.setTargets(event.getTargets());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private EventManager getEventManager() {
        return ((CustomApplication) getActivity().getApplication()).getEventManager();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Target target = ((TargetListAdapter) listView.getAdapter()).getItem(position);
        getEventManager().publish(new RequestEditTargetEvent(target));
    }

    @Override
    public void onStop() {
        super.onStop();
        getEventManager().unsubscribeAll(this);
    }
}
