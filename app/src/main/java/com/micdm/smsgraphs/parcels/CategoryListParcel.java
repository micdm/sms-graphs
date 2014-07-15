package com.micdm.smsgraphs.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.data.CategoryList;

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

    private final CategoryList categories;

    public CategoryListParcel(CategoryList categories) {
        this.categories = categories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(categories.size());
        for (Category category: categories) {
            out.writeParcelable(new CategoryParcel(category), flags);
        }
    }

    public CategoryList getCategories() {
        return categories;
    }
}
