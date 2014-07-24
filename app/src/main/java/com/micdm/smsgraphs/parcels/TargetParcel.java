package com.micdm.smsgraphs.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.misc.DateUtils;

import org.joda.time.DateTime;

public class TargetParcel implements Parcelable {

    public static final Creator<TargetParcel> CREATOR = new Creator<TargetParcel>() {

        public TargetParcel createFromParcel(Parcel in) {
            Target target = new Target(in.readInt(), getCategory(in), in.readString(), in.readString(), getLastPaid(in), in.readInt());
            return new TargetParcel(target);
        }

        private Category getCategory(Parcel in) {
            CategoryParcel parcel = in.readParcelable(null);
            return (parcel == null) ? null : parcel.getCategory();
        }

        private DateTime getLastPaid(Parcel in) {
            return DateUtils.parseForBundle(in.readString());
        }

        public TargetParcel[] newArray(int size) {
            return new TargetParcel[size];
        }
    };

    private final Target _target;

    public TargetParcel(Target target) {
        _target = target;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(_target.getId());
        Category category = _target.getCategory();
        out.writeParcelable((category == null) ? null : new CategoryParcel(category), flags);
        out.writeString(_target.getName());
        out.writeString(_target.getTitle());
        out.writeString(DateUtils.formatForBundle(_target.getLastPaid()));
        out.writeInt(_target.getLastAmount());
    }

    public Target getTarget() {
        return _target;
    }
}
