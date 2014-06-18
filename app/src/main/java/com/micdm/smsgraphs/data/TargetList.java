package com.micdm.smsgraphs.data;

import java.util.ArrayList;

public class TargetList extends ArrayList<Target> {

    public int getWithNoCategoryCount() {
        int count = 0;
        for (Target target: this) {
            if (target.category == null) {
                count += 1;
            }
        }
        return count;
    }

    public Target getFirstWithNoCategory() {
        for (Target target: this) {
            if (target.category == null) {
                return target;
            }
        }
        return null;
    }

    public Target getTargetById(int id) {
        for (Target target: this) {
            if (target.id == id) {
                return target;
            }
        }
        return null;
    }
}
