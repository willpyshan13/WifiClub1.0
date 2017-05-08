package com.squareup.timessquare;

import java.util.Date;
import java.util.HashMap;

class CustomMonthDescriptor {
    private final int month;
    private final int year;
    private final Date date;
    private String label;

    public HashMap<String, CustomJsonAdvancedSetting.DaySetting> getMonthDataSourch() {
        return monthDataSourch;
    }

    public void setMonthDataSourch(HashMap<String, CustomJsonAdvancedSetting.DaySetting> monthDataSourch) {
        this.monthDataSourch = monthDataSourch;
    }
    private HashMap<String, CustomJsonAdvancedSetting.DaySetting> monthDataSourch;

    public CustomMonthDescriptor(int month, int year, Date date, String label) {
        this.month = month;
        this.year = year;
        this.date = date;
        this.label = label;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public Date getDate() {
        return date;
    }

    public String getLabel() {
        return label;
    }

    void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "MonthDescriptor{"
                + "label='"
                + label
                + '\''
                + ", month="
                + month
                + ", year="
                + year
                + '}';
    }
}
