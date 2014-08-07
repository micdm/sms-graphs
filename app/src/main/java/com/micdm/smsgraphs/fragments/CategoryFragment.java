package com.micdm.smsgraphs.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

import com.micdm.smsgraphs.CustomApplication;
import com.micdm.smsgraphs.R;
import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.events.EventManager;
import com.micdm.smsgraphs.events.events.EditCategoryEvent;
import com.micdm.smsgraphs.parcels.CategoryParcel;

public class CategoryFragment extends DialogFragment {

    public static final String INIT_ARG_CATEGORY = "category";

    private Category _category;

    private EditText _nameView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        handleInitArguments();
    }

    private void handleInitArguments() {
        Bundle args = getArguments();
        CategoryParcel parcel = args.getParcelable(INIT_ARG_CATEGORY);
        _category = (parcel == null) ? null : parcel.getCategory();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.fragment_category_title);
        builder.setView(getDialogView());
        builder.setNeutralButton(R.string.fragment_category_save_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateCategory();
                getEventManager().publish(new EditCategoryEvent(_category));
            }
        });
        if (_category != null) {
            builder.setPositiveButton(R.string.fragment_category_remove_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        return builder.create();
    }

    private View getDialogView() {
        View view = View.inflate(getActivity(), R.layout.f__category, null);
        _nameView = (EditText) view.findViewById(R.id.f__category__name);
        if (_category != null) {
            _nameView.setText(_category.getName());
        }
        return view;
    }

    private void updateCategory() {
        String name = _nameView.getText().toString();
        if (name.length() != 0) {
            _category.setName(name);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (_nameView.requestFocus()) {
            // TODO: показывать клавиатуру
        }
    }

    private EventManager getEventManager() {
        return ((CustomApplication) getActivity().getApplication()).getEventManager();
    }
}
