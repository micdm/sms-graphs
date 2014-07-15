package com.micdm.smsgraphs.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import com.micdm.smsgraphs.data.Operation;
import com.micdm.smsgraphs.data.Target;

public class OperationParcel implements Parcelable {

    public static final Creator<OperationParcel> CREATOR = new Creator<OperationParcel>() {

        public OperationParcel createFromParcel(Parcel in) {
            Operation operation = new Operation(getTarget(in), in.readInt());
            return new OperationParcel(operation);
        }

        private Target getTarget(Parcel in) {
            return ((TargetParcel) in.readParcelable(null)).getTarget();
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
        out.writeParcelable(new TargetParcel(_operation.getTarget()), flags);
        out.writeInt(_operation.getAmount());
    }

    public Operation getOperation() {
        return _operation;
    }
}
