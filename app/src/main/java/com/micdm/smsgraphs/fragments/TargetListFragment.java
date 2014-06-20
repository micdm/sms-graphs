package com.micdm.smsgraphs.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.micdm.smsgraphs.R;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.handlers.TargetHandler;
import com.micdm.smsgraphs.misc.DateUtils;

// TODO: к дате последней операции добавить сумму
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
            TextView lastPaidView = (TextView) view.findViewById(R.id.v__target_list__list_item__last_paid);
            lastPaidView.setText(DateUtils.formatForHuman(target.lastPaid));
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

    private TargetHandler handler;
    private final TargetHandler.OnLoadTargetsListener onLoadTargetsListener = new TargetHandler.OnLoadTargetsListener() {
        @Override
        public void onLoadTargets(TargetList targets) {
            TargetListAdapter adapter = (TargetListAdapter) getListAdapter();
            if (adapter == null) {
                adapter = new TargetListAdapter();
                setListAdapter(adapter);
            }
            adapter.setTargets(targets);
            adapter.notifyDataSetChanged();
        }
    };
    private final TargetHandler.OnEditTargetListener onEditTargetListener = new TargetHandler.OnEditTargetListener() {
        @Override
        public void onEditTarget(Target target) {
            ((TargetListAdapter) getListView().getAdapter()).notifyDataSetChanged();
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        handler = (TargetHandler) activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        handler.addOnLoadTargetsListener(onLoadTargetsListener);
        handler.addOnEditTargetListener(onEditTargetListener);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Target target = ((TargetListAdapter) listView.getAdapter()).getItem(position);
        handler.startEditTarget(target);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeOnLoadTargetsListener(onLoadTargetsListener);
        handler.removeOnEditTargetListener(onEditTargetListener);
    }
}
