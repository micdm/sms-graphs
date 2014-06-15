package com.micdm.smsgraphs.data;

import java.util.ArrayList;
import java.util.List;

public class CardStats {

    private final String title;
    private final List<MonthStats> months = new ArrayList<MonthStats>();

    public CardStats(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public MonthStats getMonthStats(int year, int month) {
        for (MonthStats stats: months) {
            if (stats.getYear() == year && stats.getMonth() == month) {
                return stats;
            }
        }
        return null;
    }

    public void addMonthStats(MonthStats stats) {
        months.add(stats);
    }
}
