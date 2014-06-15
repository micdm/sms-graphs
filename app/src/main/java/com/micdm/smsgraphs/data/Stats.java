package com.micdm.smsgraphs.data;

import java.util.ArrayList;
import java.util.List;

public class Stats {

    private final List<CardStats> cards = new ArrayList<CardStats>();

    public CardStats getCardStats(String title) {
        for (CardStats stats: cards) {
            if (stats.getTitle().equals(title)) {
                return stats;
            }
        }
        return null;
    }

    public void addCardStats(CardStats stats) {
        cards.add(stats);
    }
}
