package com.micdm.smsgraphs.data;

import java.math.BigDecimal;

public class CategoryStat {

    public final Category category;
    public BigDecimal amount = new BigDecimal(0);
    public double percentage;

    public CategoryStat(Category category) {
        this.category = category;
    }
}
