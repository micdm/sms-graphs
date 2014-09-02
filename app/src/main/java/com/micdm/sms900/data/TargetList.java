package com.micdm.sms900.data;

import java.util.ArrayList;

public class TargetList extends ArrayList<Target> {

    public TargetList() {
        super();
    }

    public TargetList(int capacity) {
        super(capacity);
    }

    public int getWithNoCategoryCount() {
        int count = 0;
        for (Target target: this) {
            if (target.getCategory() == null) {
                count += 1;
            }
        }
        return count;
    }

    public Target getById(int id) {
        for (Target target: this) {
            if (target.getId() == id) {
                return target;
            }
        }
        return null;
    }
}
