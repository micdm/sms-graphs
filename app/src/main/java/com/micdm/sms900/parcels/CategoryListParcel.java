package com.micdm.sms900.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import com.micdm.sms900.data.Category;
import com.micdm.sms900.data.CategoryList;

public class CategoryListParcel implements Parcelable {

    public static final Creator<CategoryListParcel> CREATOR = new Creator<CategoryListParcel>() {

        public CategoryListParcel createFromParcel(Parcel in) {
            int count = in.readInt();
            CategoryList categories = new CategoryList(count);
            for (int i = 0; i < count; i += 1) {
                categories.add(((CategoryParcel) in.readParcelable(null)).getCategory());
            }
            return new CategoryListParcel(categories);
        }

        public CategoryListParcel[] newArray(int size) {
            return new CategoryListParcel[size];
        }
    };

    private final CategoryList _categories;

    public CategoryListParcel(CategoryList categories) {
        _categories = categories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(_categories.size());
        for (Category category: _categories) {
            out.writeParcelable(new CategoryParcel(category), flags);
        }
    }

    public CategoryList getCategories() {
        return _categories;
    }
}
