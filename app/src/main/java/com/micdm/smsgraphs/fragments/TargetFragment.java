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
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.handlers.CategoryHandler;
import com.micdm.smsgraphs.handlers.TargetHandler;

import java.util.List;

public class TargetFragment extends DialogFragment {

    private class CategoryListAdapter extends BaseAdapter {

        private final List<Category> categories;

        public CategoryListAdapter(List<Category> categories) {
            this.categories = categories;
        }

        @Override
        public int getCount() {
            return categories.size() + 1;
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
    private CategoryHandler.OnLoadCategoriesListener onLoadCategoriesListener = new CategoryHandler.OnLoadCategoriesListener() {
        @Override
        public void onLoadCategories(List<Category> categories) {
            Spinner view = (Spinner) getDialog().findViewById(R.id.f__target__categories);
            view.setAdapter(new CategoryListAdapter(categories));
        }
    };

    private TargetHandler targetHandler;
    private TargetHandler.OnStartEditTargetListener onStartEditTargetListener = new TargetHandler.OnStartEditTargetListener() {
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        categoryHandler = (CategoryHandler) activity;
        targetHandler = (TargetHandler) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.f__target, null);
        titleView = (EditText) view.findViewById(R.id.f__target__title);
        categoriesView = (Spinner) view.findViewById(R.id.f__target__categories);
        builder.setView(view);
        builder.setPositiveButton(R.string.fragment_target_next_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {

            }
        });
        builder.setNeutralButton(R.string.fragment_target_save_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                String title = titleView.getText().toString();
                if (title.length() != 0) {
                    target.title = title;
                }
                target.category = (Category) categoriesView.getSelectedItem();
                targetHandler.stopEditTarget();
            }
        });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        categoryHandler.addOnLoadCategoriesListener(onLoadCategoriesListener);
        targetHandler.addOnStartEditTargetListener(onStartEditTargetListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        categoryHandler.removeOnLoadCategoriesListener(onLoadCategoriesListener);
        targetHandler.removeOnStartEditTargetListener(onStartEditTargetListener);
    }
}