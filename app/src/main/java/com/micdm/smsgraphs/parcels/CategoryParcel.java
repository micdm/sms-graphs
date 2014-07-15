package com.micdm.smsgraphs.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import com.micdm.smsgraphs.data.Category;

public class CategoryParcel implements Parcelable {

    public static final Creator<CategoryParcel> CREATOR = new Creator<CategoryParcel>() {

        public CategoryParcel createFromParcel(Parcel in) {
            Category category = new Category(in.readInt(), in.readString());
            return new CategoryParcel(category);
        }

        public CategoryParcel[] newArray(int size) {
            return new CategoryParcel[size];
        }
    };

    private final Category _category;

    public CategoryParcel(Category category) {
        _category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(_category.getId());
        out.writeString(_category.getName());
    }

    public Category getCategory() {
        return _category;
    }
}
