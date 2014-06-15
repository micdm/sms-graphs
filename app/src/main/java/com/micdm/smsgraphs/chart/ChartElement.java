package com.micdm.smsgraphs.chart;

import java.math.BigDecimal;

public class ChartElement {

    private final BigDecimal value;

    public ChartElement(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
