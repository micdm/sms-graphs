package com.micdm.smsgraphs.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class MoneyStream {

    private final List<BigDecimal> amounts = new ArrayList<BigDecimal>();

    public boolean isSame(MoneyStream other) {
        return getClass().equals(other.getClass());
    }

    public void addAmount(BigDecimal amount) {
        amounts.add(amount);
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = new BigDecimal(0);
        for (BigDecimal amount: amounts) {
            total = total.add(amount);
        }
        return total;
    }
}
