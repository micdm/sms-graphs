package com.micdm.sms900.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.micdm.sms900.CustomApplication;
import com.micdm.sms900.R;
import com.micdm.sms900.data.Category;
import com.micdm.sms900.data.CategoryList;
import com.micdm.sms900.events.EventManager;
import com.micdm.sms900.events.EventType;
import com.micdm.sms900.events.events.EditCategoryEvent;
import com.micdm.sms900.events.events.LoadCategoriesEvent;
import com.micdm.sms900.events.events.RequestLoadCategoriesEvent;
import com.micdm.sms900.parcels.CategoryParcel;

public class CategoryFragment extends DialogFragment {

    public static final String INIT_ARG_CATEGORY = "category";

    private Category _category;
    private CategoryList _categories;

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
                Category category = getUpdatedCategory();
                if (category != null) {
                    getEventManager().publish(new EditCategoryEvent(category, false));
                }
            }
        });
        if (_category != null) {
            builder.setPositiveButton(R.string.fragment_category_remove_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getEventManager().publish(new EditCategoryEvent(_category, true));
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

    private Category getUpdatedCategory() {
        String name = _nameView.getText().toString();
        if (name.length() == 0) {
            return null;
        }
        if (isCategoryExist(name)) {
            return null;
        }
        Category category = _category;
        if (category == null) {
            category = new Category(0, name);
        } else {
            category.setName(name);
        }
        return category;
    }

    private boolean isCategoryExist(String name) {
        for (Category category: _categories) {
            if (category.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        subscribeForEvents();
        getEventManager().publish(new RequestLoadCategoriesEvent());
        if (_nameView.requestFocus()) {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void subscribeForEvents() {
        EventManager manager = getEventManager();
        manager.subscribe(this, EventType.LOAD_CATEGORIES, new EventManager.OnEventListener<LoadCategoriesEvent>() {
            @Override
            public void onEvent(LoadCategoriesEvent event) {
                _categories = event.getCategories();
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
