package com.micdm.smsgraphs.data;

import java.util.ArrayList;
import java.util.List;

public class CategoryStat {

    public final Category category;
    public int amount;
    public double percentage;
    public final List<TargetStat> stats = new ArrayList<TargetStat>();

    public CategoryStat(Category category) {
        this.category = category;
    }
}
