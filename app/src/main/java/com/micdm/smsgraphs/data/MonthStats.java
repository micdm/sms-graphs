package com.micdm.smsgraphs.data;

import com.micdm.smsgraphs.data.outcomes.Outcome;

import java.util.ArrayList;
import java.util.List;

public class MonthStats {

    private final int year;
    private final int month;
    private final List<Outcome> outcomes = new ArrayList<Outcome>();

    public MonthStats(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public Outcome getOutcome(Outcome search) {
        for (Outcome outcome: outcomes) {
            if (outcome.isSame(search)) {
                return outcome;
            }
        }
        return null;
    }

    public List<Outcome> getOutcomes() {
        return outcomes;
    }

    public void addOutcome(Outcome outcome) {
        outcomes.add(outcome);
    }
}
