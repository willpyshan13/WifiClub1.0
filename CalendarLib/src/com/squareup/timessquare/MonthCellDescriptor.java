
package com.squareup.timessquare;

import java.util.Date;

/**
 * Describes the state of a particular date cell in a {@link MonthView}.
 */
class MonthCellDescriptor {
    public enum RangeState {
        NONE, FIRST, MIDDLE, LAST
    }

    private final Date date;
    private final int value;
    private final boolean isCurrentMonth;
    private boolean isSelected;
    private final boolean isToday;
    private final boolean isSelectable;
    private boolean isHighlighted;
    private RangeState rangeState;
    private boolean isBookingHere;
    private boolean isNineHoles;
    private boolean isThreeTeeStart;
    private boolean isBlockTimes;
    private boolean isTwoTeeStart;
    private boolean isMemberOnly;
    private boolean isPrimeTime;
    private int teeTimeColor;

    MonthCellDescriptor(Date date, boolean currentMonth, boolean selectable, boolean selected,
                        boolean today, boolean highlighted, int value, RangeState rangeState) {
        this.date = date;
        isCurrentMonth = currentMonth;
        isSelectable = selectable;
        isHighlighted = highlighted;
        isSelected = selected;
        isToday = today;
        this.value = value;
        this.rangeState = rangeState;
    }

    MonthCellDescriptor(Date date, int value, boolean isCurrentMonth, boolean isToday, boolean isSelectable, boolean isSelected, boolean isHighlighted, RangeState rangeState, boolean isBookingHere, boolean isNineHoles, boolean isThreeTeeStart, boolean isBlockTimes, boolean isTwoTeeStart, boolean isMemberOnly, boolean isPrimeTime, int teeTimeColor) {
        this.date = date;
        this.value = value;
        this.isCurrentMonth = isCurrentMonth;
        this.isToday = isToday;
        this.isSelectable = isSelectable;
        this.isSelected = isSelected;
        this.isHighlighted = isHighlighted;
        this.rangeState = rangeState;
        this.isBookingHere = isBookingHere;
        this.isNineHoles = isNineHoles;
        this.isThreeTeeStart = isThreeTeeStart;
        this.isBlockTimes = isBlockTimes;
        this.isTwoTeeStart = isTwoTeeStart;
        this.isMemberOnly = isMemberOnly;
        this.isPrimeTime = isPrimeTime;

        this.teeTimeColor = teeTimeColor;
    }

    public boolean isNineHoles() {
        return isNineHoles;
    }

    public void setNineHoles(boolean isNineHoles) {
        this.isNineHoles = isNineHoles;
    }

    public boolean isThreeTeeStart() {
        return isThreeTeeStart;
    }

    public void setThreeTeeStart(boolean isThreeTeeStart) {
        this.isThreeTeeStart = isThreeTeeStart;
    }

    public Date getDate() {
        return date;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    boolean isHighlighted() {
        return isHighlighted;
    }

    void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public boolean isToday() {
        return isToday;
    }

    public RangeState getRangeState() {
        return rangeState;
    }

    public void setRangeState(RangeState rangeState) {
        this.rangeState = rangeState;
    }

    public int getValue() {
        return value;
    }

    public boolean isBookingHere() {
        return isBookingHere;
    }

    public void setBookingHere(boolean isBookingHere) {
        this.isBookingHere = isBookingHere;
    }

    public boolean isBlockTimes() {
        return isBlockTimes;
    }

    public void setBlockTimes(boolean isBlockTimes) {
        this.isBlockTimes = isBlockTimes;
    }

    public boolean isTwoTeeStart() {
        return isTwoTeeStart;
    }

    public void setTwoTeeStart(boolean isTwoTeeStart) {
        this.isTwoTeeStart = isTwoTeeStart;
    }

    public boolean isMemberOnly() {
        return isMemberOnly;
    }

    public void setMemberOnly(boolean isMemberOnly) {
        this.isMemberOnly = isMemberOnly;
    }

    public boolean isPrimeTime() {
        return isPrimeTime;
    }

    public void setPrimeTime(boolean isPrimeTime) {
        this.isPrimeTime = isPrimeTime;
    }

    public int getTeeTimeColor() {
        return teeTimeColor;
    }

    public void setTeeTimeColor(int teeTimeColor) {
        this.teeTimeColor = teeTimeColor;
    }

    @Override
    public String toString() {
        return "MonthCellDescriptor{"
                + "date="
                + date
                + ", value="
                + value
                + ", isCurrentMonth="
                + isCurrentMonth
                + ", isSelected="
                + isSelected
                + ", isToday="
                + isToday
                + ", isSelectable="
                + isSelectable
                + ", isHighlighted="
                + isHighlighted
                + ", rangeState="
                + rangeState
                + '}';
    }
}
