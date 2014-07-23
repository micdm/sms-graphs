package com.micdm.smsgraphs.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.micdm.smsgraphs.CustomApplication;
import com.micdm.smsgraphs.R;
import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.events.EventManager;
import com.micdm.smsgraphs.events.EventType;
import com.micdm.smsgraphs.events.events.EditTargetEvent;
import com.micdm.smsgraphs.events.events.LoadCategoriesEvent;
import com.micdm.smsgraphs.events.events.RequestLoadCategoriesEvent;
import com.micdm.smsgraphs.misc.DateUtils;
import com.micdm.smsgraphs.parcels.TargetParcel;

public class TargetFragment extends DialogFragment {

    private class CategoryListAdapter extends BaseAdapter {

        private CategoryList _categories;

        public void setCategories(CategoryList categories) {
            _categories = categories;
        }

        @Override
        public int getCount() {
            return (_categories == null) ? 0 : _categories.size() + 1;
        }

        @Override
        public Category getItem(int position) {
            return (position == 0) ? null : _categories.get(position - 1);
        }

        @Override
        public long getItemId(int position) {
            return (position == 0) ? 0 : getItem(position).getId();
        }

        public int getItemPosition(Category category) {
            return _categories.indexOf(category) + 1;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(getActivity(), R.layout.v__target__category_list_item, null);
            }
            String text = (position == 0) ? getString(R.string.fragment_target_no_category) : getItem(position).getName();
            ((TextView) view).setText(text);
            return view;
        }
    }

    public static final String INIT_ARG_TARGET = "target";

    private Target _target;

    private EditText _titleView;
    private Spinner _categoriesView;

    private boolean _isDismissing;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        handleInitArguments();
    }

    private void handleInitArguments() {
        Bundle args = getArguments();
        _target = ((TargetParcel) args.getParcelable(INIT_ARG_TARGET)).getTarget();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.fragment_target_title);
        View view = View.inflate(getActivity(), R.layout.f__target, null);
        TextView lastOperationView = (TextView) view.findViewById(R.id.f__target__last_operation);
        int lastAmount = _target.getLastAmount();
        lastOperationView.setText(getString(R.string.fragment_target_last_operation,
                DateUtils.formatForHuman(_target.getLastPaid()), lastAmount, getResources().getQuantityString(R.plurals.rubles, lastAmount)));
        _titleView = (EditText) view.findViewById(R.id.f__target__title);
        _titleView.setHint(_target.getName());
        String title = _target.getTitle();
        if (title != null) {
            _titleView.setText(title);
        }
        _categoriesView = (Spinner) view.findViewById(R.id.f__target__categories);
        builder.setView(view);
        builder.setNeutralButton(R.string.fragment_target_save_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _isDismissing = true;
                updateTarget();
                getEventManager().publish(new EditTargetEvent(_target, false));
            }
        });
        builder.setPositiveButton(R.string.fragment_target_next_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _isDismissing = true;
                updateTarget();
                getEventManager().publish(new EditTargetEvent(_target, true));
            }
        });
        return builder.create();
    }

    private void updateTarget() {
        String title = _titleView.getText().toString();
        _target.setTitle((title.length() == 0) ? null : title);
        _target.setCategory((Category) _categoriesView.getSelectedItem());
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
                Category category = _target.getCategory();
                if (category != null) {
                    _categoriesView.setSelection(adapter.getItemPosition(category));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private EventManager getEventManager() {
        return ((CustomApplication) getActivity().getApplication()).getEventManager();
    }

    public boolean isDismissing() {
        return _isDismissing;
    }

    @Override
    public void onStop() {
        super.onStop();
        getEventManager().unsubscribeAll(this);
    }
}
