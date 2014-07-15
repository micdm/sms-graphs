package com.micdm.smsgraphs.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetList;

public class TargetListParcel implements Parcelable {

    public static final Creator<TargetListParcel> CREATOR = new Creator<TargetListParcel>() {

        public TargetListParcel createFromParcel(Parcel in) {
            int count = in.readInt();
            TargetList targets = new TargetList(count);
            for (int i = 0; i < count; i += 1) {
                targets.add(((TargetParcel) in.readParcelable(null)).getTarget());
            }
            return new TargetListParcel(targets);
        }

        public TargetListParcel[] newArray(int size) {
            return new TargetListParcel[size];
        }
    };

    private final TargetList _targets;

    public TargetListParcel(TargetList targets) {
        _targets = targets;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(_targets.size());
        for (Target target: _targets) {
            out.writeParcelable(new TargetParcel(target), flags);
        }
    }

    public TargetList getTargets() {
        return _targets;
    }
}
