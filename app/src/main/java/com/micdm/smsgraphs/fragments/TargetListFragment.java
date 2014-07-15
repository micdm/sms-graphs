package com.micdm.smsgraphs.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.micdm.smsgraphs.CustomApplication;
import com.micdm.smsgraphs.R;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.events.EventManager;
import com.micdm.smsgraphs.events.EventType;
import com.micdm.smsgraphs.events.events.EditTargetEvent;
import com.micdm.smsgraphs.events.events.LoadTargetsEvent;
import com.micdm.smsgraphs.handlers.TargetHandler;
import com.micdm.smsgraphs.misc.DateUtils;

public class TargetListFragment extends ListFragment {

    private class TargetListAdapter extends BaseAdapter {

        private TargetList targets;

        public void setTargets(TargetList targets) {
            this.targets = targets;
        }

        @Override
        public int getCount() {
            return (targets == null) ? 0 : targets.size();
        }

        @Override
        public Target getItem(int position) {
            return targets.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).id;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(getActivity(), R.layout.v__target_list__list_item, null);
            }
            Target target = getItem(position);
            TextView titleView = (TextView) view.findViewById(R.id.v__target_list__list_item__title);
            TextView nameView = (TextView) view.findViewById(R.id.v__target_list__list_item__name);
            if (target.title == null) {
                titleView.setText(target.name);
                nameView.setVisibility(View.GONE);
            } else {
                titleView.setText(target.title);
                nameView.setVisibility(View.VISIBLE);
                nameView.setText(target.name);
            }
            TextView lastOperationView = (TextView) view.findViewById(R.id.v__target_list__list_item__last_operation);
            lastOperationView.setText(getString(R.string.fragment_target_list_last_operation,
                    DateUtils.formatForHuman(target.lastPaid), target.lastAmount, getResources().getQuantityString(R.plurals.rubles, target.lastAmount)));
            TextView categoryView = (TextView) view.findViewById(R.id.v__target_list__list_item__category);
            View noCategoryView = view.findViewById(R.id.v__target_list__list_item__no_category);
            if (target.category == null) {
                categoryView.setVisibility(View.GONE);
                noCategoryView.setVisibility(View.VISIBLE);
            } else {
                categoryView.setVisibility(View.VISIBLE);
                categoryView.setText(target.category.name);
                noCategoryView.setVisibility(View.GONE);
            }
            return view;
        }
    }

    private TargetHandler targetHandler;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        targetHandler = (TargetHandler) activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        getEventManager().subscribe(this, EventType.LOAD_TARGETS, new EventManager.OnEventListener<LoadTargetsEvent>() {
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
        targetHandler.loadTargets();
        getEventManager().subscribe(this, EventType.EDIT_TARGET, new EventManager.OnEventListener<EditTargetEvent>() {
            @Override
            public void onEvent(EditTargetEvent event) {
                ((TargetListAdapter) getListView().getAdapter()).notifyDataSetChanged();
            }
        });
    }

    private EventManager getEventManager() {
        return ((CustomApplication) getActivity().getApplication()).getEventManager();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Target target = ((TargetListAdapter) listView.getAdapter()).getItem(position);
        targetHandler.requestEditTarget(target);
    }

    @Override
    public void onStop() {
        super.onStop();
        getEventManager().unsubscribeAll(this);
    }
}
