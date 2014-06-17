package com.micdm.smsgraphs.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.micdm.smsgraphs.R;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.handlers.TargetHandler;

public class TargetListFragment extends ListFragment {

    private class TargetListAdapter extends BaseAdapter {

        private final TargetList targets;

        public TargetListAdapter(TargetList targets) {
            this.targets = targets;
        }

        @Override
        public int getCount() {
            return targets.size();
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
            TextView nameView = (TextView) view.findViewById(R.id.v__target_list__list_item__name);
            nameView.setText(target.name);
            if (target.category != null) {
                TextView categoryView = (TextView) view.findViewById(R.id.v__target_list__list_item__category);
                categoryView.setText(target.category.name);
            }
            return view;
        }
    }

    private TargetHandler handler;
    private TargetHandler.OnLoadTargetsListener onLoadTargetsListener = new TargetHandler.OnLoadTargetsListener() {
        @Override
        public void onLoadTargets(TargetList targets) {
            setListAdapter(new TargetListAdapter(targets));
            handler.updateWithNoCategoryCount(targets.getWithNoCategoryCount());
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
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeOnLoadTargetsListener(onLoadTargetsListener);
    }
}
