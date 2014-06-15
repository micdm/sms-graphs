package com.micdm.smsgraphs.data;

import com.micdm.smsgraphs.data.incomes.Income;
import com.micdm.smsgraphs.data.outcomes.Outcome;

import java.util.ArrayList;
import java.util.List;

public class MonthStats {

    private final int year;
    private final int month;
    private final List<Income> incomes = new ArrayList<Income>();
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

    public Income getIncome(Income search) {
        for (Income income: incomes) {
            if (income.isSame(search)) {
                return income;
            }
        }
        return null;
    }

    public List<Income> getIncomes() {
        return incomes;
    }

    public void addIncome(Income income) {
        incomes.add(income);
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
