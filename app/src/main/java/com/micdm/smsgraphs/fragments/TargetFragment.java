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

import com.micdm.smsgraphs.R;
import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.handlers.CategoryHandler;
import com.micdm.smsgraphs.handlers.TargetHandler;

// TODO: показывать дату последней операции и сумму
public class TargetFragment extends DialogFragment {

    private class CategoryListAdapter extends BaseAdapter {

        private CategoryList categories;

        public void setCategories(CategoryList categories) {
            this.categories = categories;
        }

        @Override
        public int getCount() {
            return (categories == null) ? 0 : categories.size() + 1;
        }

        @Override
        public Category getItem(int position) {
            return (position == 0) ? null : categories.get(position - 1);
        }

        @Override
        public long getItemId(int position) {
            return (position == 0) ? 0 : getItem(position).id;
        }

        public int getItemPosition(Category category) {
            return categories.indexOf(category) + 1;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(getActivity(), R.layout.v__target__category_list_item, null);
            }
            String text = (position == 0) ? getString(R.string.fragment_target_no_category) : getItem(position).name;
            ((TextView) view).setText(text);
            return view;
        }
    }

    private CategoryHandler categoryHandler;
    private final CategoryHandler.OnLoadCategoriesListener onLoadCategoriesListener = new CategoryHandler.OnLoadCategoriesListener() {
        @Override
        public void onLoadCategories(CategoryList categories) {
            Spinner view = (Spinner) getDialog().findViewById(R.id.f__target__categories);
            CategoryListAdapter adapter = (CategoryListAdapter) view.getAdapter();
            if (adapter == null) {
                adapter = new CategoryListAdapter();
                view.setAdapter(adapter);
            }
            adapter.setCategories(categories);
            adapter.notifyDataSetChanged();
        }
    };

    private TargetHandler targetHandler;
    private final TargetHandler.OnStartEditTargetListener onStartEditTargetListener = new TargetHandler.OnStartEditTargetListener() {
        @Override
        public void onStartEditTarget(Target editable) {
            target = editable;
            titleView.setHint(target.name);
            if (target.title != null) {
                titleView.setText(target.title);
            }
            if (target.category != null) {
                CategoryListAdapter adapter = (CategoryListAdapter) categoriesView.getAdapter();
                categoriesView.setSelection(adapter.getItemPosition(target.category));
            }
        }
    };

    private Target target;

    private EditText titleView;
    private Spinner categoriesView;

    private boolean isDismissing;
    private boolean editNext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        categoryHandler = (CategoryHandler) activity;
        targetHandler = (TargetHandler) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.fragment_target_title);
        View view = View.inflate(getActivity(), R.layout.f__target, null);
        titleView = (EditText) view.findViewById(R.id.f__target__title);
        categoriesView = (Spinner) view.findViewById(R.id.f__target__categories);
        builder.setView(view);
        builder.setNeutralButton(R.string.fragment_target_save_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editNext = false;
            }
        });
        builder.setPositiveButton(R.string.fragment_target_next_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editNext = true;
            }
        });
        return builder.create();
    }

    private void updateTarget() {
        String title = titleView.getText().toString();
        target.title = (title.length() == 0) ? null : title;
        target.category = (Category) categoriesView.getSelectedItem();
    }

    @Override
    public void onStart() {
        super.onStart();
        categoryHandler.addOnLoadCategoriesListener(onLoadCategoriesListener);
        targetHandler.addOnStartEditTargetListener(onStartEditTargetListener);
    }

    public boolean isDismissing() {
        return isDismissing;
    }

    @Override
    public void onStop() {
        super.onStop();
        categoryHandler.removeOnLoadCategoriesListener(onLoadCategoriesListener);
        targetHandler.removeOnStartEditTargetListener(onStartEditTargetListener);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isDismissing = true;
        updateTarget();
        targetHandler.finishEditTarget(editNext);
    }
}
