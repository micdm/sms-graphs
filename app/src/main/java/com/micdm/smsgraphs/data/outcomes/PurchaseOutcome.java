package com.micdm.smsgraphs.data.outcomes;

import com.micdm.smsgraphs.data.MoneyStream;
import com.micdm.smsgraphs.data.OutcomeTarget;

public class PurchaseOutcome extends Outcome {

    private final OutcomeTarget target;

    public PurchaseOutcome(OutcomeTarget target) {
        this.target = target;
    }

    public OutcomeTarget getTarget() {
        return target;
    }

    @Override
    public boolean isSame(MoneyStream other) {
        return super.isSame(other) && target == ((PurchaseOutcome) other).getTarget();
    }
}
