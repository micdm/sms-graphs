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
            return ((CategoryParcel) in.readParcelable(null)).getCategory();
        }

        private DateTime getLastPaid(Parcel in) {
            return DateUtils.parseForBundle(in.readString());
        }

        public TargetParcel[] newArray(int size) {
            return new TargetParcel[size];
        }
    };

    private final Target target;

    public TargetParcel(Target target) {
        this.target = target;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(target.id);
        out.writeParcelable(new CategoryParcel(target.category), flags);
        out.writeString(target.name);
        out.writeString(target.title);
        out.writeString(DateUtils.formatForBundle(target.lastPaid));
        out.writeInt(target.lastAmount);
    }

    public Target getTarget() {
        return target;
    }
}
