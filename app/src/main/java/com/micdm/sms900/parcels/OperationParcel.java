package com.micdm.sms900.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import com.micdm.sms900.data.Operation;
import com.micdm.sms900.data.Target;
import com.micdm.sms900.misc.DateUtils;

import org.joda.time.DateTime;

public class OperationParcel implements Parcelable {

    public static final Creator<OperationParcel> CREATOR = new Creator<OperationParcel>() {

        public OperationParcel createFromParcel(Parcel in) {
            Operation operation = new Operation(in.readInt(), getTarget(in), getCreated(in), in.readInt(), (in.readInt() == 1));
            return new OperationParcel(operation);
        }

        private Target getTarget(Parcel in) {
            return ((TargetParcel) in.readParcelable(null)).getTarget();
        }

        private DateTime getCreated(Parcel in) {
            return DateUtils.parseForBundle(in.readString());
        }

        public OperationParcel[] newArray(int size) {
            return new OperationParcel[size];
        }
    };

    private final Operation _operation;

    public OperationParcel(Operation operation) {
        _operation = operation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(_operation.getId());
        out.writeParcelable(new TargetParcel(_operation.getTarget()), flags);
        out.writeString(DateUtils.formatForDb(_operation.getCreated()));
        out.writeInt(_operation.getAmount());
        out.writeInt(_operation.isIgnored() ? 1 : 0);
    }

    public Operation getOperation() {
        return _operation;
    }
}
