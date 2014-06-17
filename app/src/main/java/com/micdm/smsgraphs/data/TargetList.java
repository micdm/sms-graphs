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
}
