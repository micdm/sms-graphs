package com.micdm.smsgraphs.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import com.micdm.smsgraphs.data.OperationReport;
import com.micdm.smsgraphs.misc.DateUtils;

import org.joda.time.DateTime;

public class OperationReportParcel implements Parcelable {

    public static final Creator<OperationReportParcel> CREATOR = new Creator<OperationReportParcel>() {

        public OperationReportParcel createFromParcel(Parcel in) {
            OperationReport report = new OperationReport(getFirst(in), getLast(in));
            return new OperationReportParcel(report);
        }

        private DateTime getFirst(Parcel in) {
            return DateUtils.parseForBundle(in.readString());
        }

        private DateTime getLast(Parcel in) {
            return DateUtils.parseForBundle(in.readString());
        }

        public OperationReportParcel[] newArray(int size) {
            return new OperationReportParcel[size];
        }
    };

    private final OperationReport _report;

    public OperationReportParcel(OperationReport report) {
        _report = report;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(DateUtils.formatForBundle(_report.getFirst()));
        out.writeString(DateUtils.formatForBundle(_report.getLast()));
    }

    public OperationReport getReport() {
        return _report;
    }
}
