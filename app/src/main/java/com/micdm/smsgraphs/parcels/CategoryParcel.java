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

    private final Category category;

    public CategoryParcel(Category category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(category.id);
        out.writeString(category.name);
    }

    public Category getCategory() {
        return category;
    }
}
