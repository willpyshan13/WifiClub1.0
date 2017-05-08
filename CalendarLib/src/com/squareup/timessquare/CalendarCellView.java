package com.squareup.timessquare;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.timessquare.MonthCellDescriptor.RangeState;

public class CalendarCellView extends RelativeLayout {

    private ImageView ivBookingHere;
    private TextView tvDay;
    private ImageView ivBlockTimes;
    private ImageView ivTwoTeeStart;
    private ImageView ivMemberOnly;
    private ImageView ivPrimeTime;

    private boolean isBookingHere;
    private boolean isNineHoles;
    private boolean isThreeTeeStart;
    private boolean isBlockTimes;
    private boolean isTwoTeeStart;
    private boolean isMemberOnly;
    private boolean isPrimeTime;

    private boolean isTeeTimeSetting1;
    private boolean isTeeTimeSetting2;

    public void setTeeTimeSetting1(boolean isTeeTimeSetting1) {
        this.isTeeTimeSetting1 = isTeeTimeSetting1;
    }

    public void setTeeTimeSetting2(boolean isTeeTimeSetting2) {
        this.isTeeTimeSetting2 = isTeeTimeSetting2;
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

    public void setIvBookingHere() {
        ivBookingHere.setImageResource(R.drawable.booking_here);
    }

    public ImageView getIvBookingHere() {
        return ivBookingHere;
    }

    public void setText(CharSequence str) {
        tvDay.setText(str);
    }

    public CharSequence getText() {
        return tvDay.getText();
    }


    public void setIvBlockTimes() {
        this.ivBlockTimes.setImageResource(R.drawable.block_times);
    }

    public ImageView getIvBlockTimes() {
        return ivBlockTimes;
    }

    public void setIvTwoTeeStart() {
        //this.ivTwoTeeStart.setImageResource(R.drawable.two_tee_start);
    }

    public ImageView getIvTwoTeeStart() {
        return ivTwoTeeStart;
    }

    public void setIvMemberOnly() {
        this.ivMemberOnly.setImageResource(R.drawable.merber_only);
    }

    public ImageView getIvMemberOnly() {
        return ivMemberOnly;
    }

    public void setIvPrimeTime() {
        this.ivPrimeTime.setImageResource(R.drawable.prime_time);
    }

    public ImageView getIvPrimeTime() {
        return ivPrimeTime;
    }

    private static final int[] STATE_SELECTABLE = {
            R.attr.state_selectable
    };
    private static final int[] STATE_CURRENT_MONTH = {
            R.attr.state_current_month
    };
    private static final int[] STATE_TODAY = {
            R.attr.state_today
    };
    private static final int[] STATE_HIGHLIGHTED = {
            R.attr.state_highlighted
    };
    private static final int[] STATE_RANGE_FIRST = {
            R.attr.state_range_first
    };
    private static final int[] STATE_RANGE_MIDDLE = {
            R.attr.state_range_middle
    };
    private static final int[] STATE_RANGE_LAST = {
            R.attr.state_range_last
    };

    private static final int[] STATE_TEE_TIME_SETTING_1 = {
            R.attr.state_tee_time_setting_1
    };
    private static final int[] STATE_TEE_TIME_SETTING_2 = {
            R.attr.state_tee_time_setting_2
    };

    private boolean isSelectable = false;
    private boolean isCurrentMonth = false;
    private boolean isToday = false;
    private boolean isHighlighted = false;
    private RangeState rangeState = RangeState.NONE;

    @SuppressWarnings("UnusedDeclaration")
    public CalendarCellView(Context context, AttributeSet attrs) {
        super(context, attrs);

        ivBookingHere = new ImageView(context);
        tvDay = new TextView(context);

        ivBlockTimes = new ImageView(context);
        ivTwoTeeStart = new ImageView(context);
        ivMemberOnly = new ImageView(context);
        ivPrimeTime = new ImageView(context);

        this.addView(ivBookingHere);
        LayoutParams paramsIvBookingHere = new LayoutParams(20, getActualHeightOnThisDevice(20));
        paramsIvBookingHere.width = getActualWidthOnThisDevice(20);
        paramsIvBookingHere.height = getActualHeightOnThisDevice(20);
        paramsIvBookingHere.setMargins(0, 0, 0, 0);
        ivBookingHere.setLayoutParams(paramsIvBookingHere);


        this.addView(tvDay);
        LayoutParams paramsTvDay = (LayoutParams) tvDay.getLayoutParams();
        paramsTvDay.width = LayoutParams.MATCH_PARENT;
        paramsTvDay.height = LayoutParams.MATCH_PARENT;
        paramsTvDay.setMargins(getActualWidthOnThisDevice(35), getActualHeightOnThisDevice(50), 0, 0);
        tvDay.setLayoutParams(paramsTvDay);


        this.addView(ivBlockTimes);
        LayoutParams paramsIvBlockTimes = new LayoutParams(getActualWidthOnThisDevice(20), getActualHeightOnThisDevice(20));
        paramsIvBlockTimes.width = getActualWidthOnThisDevice(20);
        paramsIvBlockTimes.height = getActualHeightOnThisDevice(20);
        paramsIvBlockTimes.setMargins(getActualWidthOnThisDevice(25), getActualHeightOnThisDevice(85), getActualWidthOnThisDevice(20), getActualHeightOnThisDevice(20));
        ivBlockTimes.setLayoutParams(paramsIvBlockTimes);

//        this.addView(ivTwoTeeStart);
//        LayoutParams paramsIvTwoTeeStart = new LayoutParams(getActualWidthOnThisDevice(20), getActualHeightOnThisDevice(20));
//        paramsIvTwoTeeStart.width = getActualWidthOnThisDevice(20);
//        paramsIvTwoTeeStart.height = getActualHeightOnThisDevice(20);
//        paramsIvTwoTeeStart.setMargins(getActualWidthOnThisDevice(30), getActualHeightOnThisDevice(85), getActualWidthOnThisDevice(20), getActualHeightOnThisDevice(20));
//        ivTwoTeeStart.setLayoutParams(paramsIvTwoTeeStart);

        this.addView(ivMemberOnly);
        LayoutParams paramsIvMemberOnly = new LayoutParams(getActualWidthOnThisDevice(20), getActualHeightOnThisDevice(20));
        paramsIvMemberOnly.width = getActualWidthOnThisDevice(20);
        paramsIvMemberOnly.height = getActualHeightOnThisDevice(20);
        paramsIvMemberOnly.setMargins(getActualWidthOnThisDevice(40), getActualHeightOnThisDevice(85), getActualWidthOnThisDevice(20), getActualHeightOnThisDevice(20));
        ivMemberOnly.setLayoutParams(paramsIvMemberOnly);

        this.addView(ivPrimeTime);
        LayoutParams paramsIvPrimeTime = new LayoutParams(getActualWidthOnThisDevice(20), getActualHeightOnThisDevice(20));
        paramsIvPrimeTime.width = getActualWidthOnThisDevice(20);
        paramsIvPrimeTime.height = getActualHeightOnThisDevice(20);
        paramsIvPrimeTime.setMargins(getActualWidthOnThisDevice(55), getActualHeightOnThisDevice(85), getActualWidthOnThisDevice(20), getActualHeightOnThisDevice(20));
        ivPrimeTime.setLayoutParams(paramsIvPrimeTime);

    }

    public void setSelectable(boolean isSelectable) {
        this.isSelectable = isSelectable;
        refreshDrawableState();
    }

    public void setCurrentMonth(boolean isCurrentMonth) {
        this.isCurrentMonth = isCurrentMonth;
        refreshDrawableState();
    }

    public void setToday(boolean isToday) {
        this.isToday = isToday;
        refreshDrawableState();
    }

    public void setRangeState(MonthCellDescriptor.RangeState rangeState) {
        this.rangeState = rangeState;
        refreshDrawableState();
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
        refreshDrawableState();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
//        final int[] drawableState = super.onCreateDrawableState(extraSpace + 5);
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 7);

        if (isSelectable) {
            mergeDrawableStates(drawableState, STATE_SELECTABLE);
        }

        if (isCurrentMonth) {
            mergeDrawableStates(drawableState, STATE_CURRENT_MONTH);
        }

        if (isToday) {
            mergeDrawableStates(drawableState, STATE_TODAY);
        }

        if (isHighlighted) {
            mergeDrawableStates(drawableState, STATE_HIGHLIGHTED);
        }

        if (rangeState == MonthCellDescriptor.RangeState.FIRST) {
            mergeDrawableStates(drawableState, STATE_RANGE_FIRST);
        } else if (rangeState == MonthCellDescriptor.RangeState.MIDDLE) {
            mergeDrawableStates(drawableState, STATE_RANGE_MIDDLE);
        } else if (rangeState == RangeState.LAST) {
            mergeDrawableStates(drawableState, STATE_RANGE_LAST);
        }

        if (isTeeTimeSetting1) {
            mergeDrawableStates(drawableState, STATE_TEE_TIME_SETTING_1);
        }
        if (isTeeTimeSetting2) {
            mergeDrawableStates(drawableState, STATE_TEE_TIME_SETTING_2);
        }

        return drawableState;
    }

    public TextView getTextView() {
        return tvDay;
    }

    public  int getActualWidthOnThisDevice(float pxValue) {
        return (int) (pxValue / 720* getContext().getResources().getDisplayMetrics().widthPixels);
    }

    public  int getActualHeightOnThisDevice(float pxValue) {
        return (int) (pxValue / 1280 * getContext().getResources().getDisplayMetrics().heightPixels);
    }

}
