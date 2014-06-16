package com.micdm.smsgraphs.data;

public class OutcomeTarget {

    private final String title;
    private Category category;

    public OutcomeTarget(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
