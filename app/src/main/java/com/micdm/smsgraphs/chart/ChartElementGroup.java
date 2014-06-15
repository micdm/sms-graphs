package com.micdm.smsgraphs.chart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ChartElementGroup {

    private final List<ChartElement> elements = new ArrayList<ChartElement>();

    public List<ChartElement> getElements() {
        return elements;
    }

    public void addElement(ChartElement element) {
        elements.add(element);
    }

    public BigDecimal getTotalValue() {
        BigDecimal total = new BigDecimal(0);
        for (ChartElement element: elements) {
            total = total.add(element.getValue());
        }
        return total;
    }
}
