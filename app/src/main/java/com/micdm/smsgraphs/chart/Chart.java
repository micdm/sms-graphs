package com.micdm.smsgraphs.chart;

import java.util.ArrayList;
import java.util.List;

public class Chart {

    private final List<ChartElementGroup> groups = new ArrayList<ChartElementGroup>();

    public List<ChartElementGroup> getGroups() {
        return groups;
    }

    public void addGroup(ChartElementGroup group) {
        groups.add(group);
    }
}
