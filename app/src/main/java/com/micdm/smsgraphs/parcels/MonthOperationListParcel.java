package com.micdm.smsgraphs.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.data.Operation;
import com.micdm.smsgraphs.misc.DateUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MonthOperationListParcel implements Parcelable {

    public static final Parcelable.Creator<MonthOperationListParcel> CREATOR = new Parcelable.Creator<MonthOperationListParcel>() {

        public MonthOperationListParcel createFromParcel(Parcel in) {
            MonthOperationList operations = new MonthOperationList(getMonth(in), getOperations(in));
            return new MonthOperationListParcel(operations);
        }

        private DateTime getMonth(Parcel in) {
            return DateUtils.parseForBundle(in.readString());
        }

        private List<Operation> getOperations(Parcel in) {
            int count = in.readInt();
            List<Operation> operations = new ArrayList<Operation>(count);
            for (int i = 0; i < count; i += 1) {
                operations.add(((OperationParcel) in.readParcelable(null)).getOperation());
            }
            return operations;
        }

        public MonthOperationListParcel[] newArray(int size) {
            return new MonthOperationListParcel[size];
        }
    };

    private final MonthOperationList _operations;

    public MonthOperationListParcel(MonthOperationList operations) {
        _operations = operations;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(DateUtils.formatForBundle(_operations.getMonth()));
        List<Operation> operations = _operations.getOperations();
        out.writeInt(operations.size());
        for (Operation operation: operations) {
            out.writeParcelable(new OperationParcel(operation), flags);
        }
    }

    public MonthOperationList getOperations() {
        return _operations;
    }
}
