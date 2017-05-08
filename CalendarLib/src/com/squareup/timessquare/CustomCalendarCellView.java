package com.squareup.timessquare;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.timessquare.CustomMonthCellDescriptor.RangeState;

public class CustomCalendarCellView extends RelativeLayout {

    private TextView tvDay;

    private TextView tvTopLeft;
    private TextView tvTopRight;
    private TextView tvBottomLeft;
    private TextView tvBottomRight;

    public TextView getTvBottomRight() {
        return tvBottomRight;
    }

    public void setTvBottomRight(TextView tvBottomRight) {
        this.tvBottomRight = tvBottomRight;
    }

    public TextView getTvTopLeft() {
        return tvTopLeft;
    }

    public void setTvTopLeft(TextView tvTopLeft) {
        this.tvTopLeft = tvTopLeft;
    }

    public TextView getTvTopRight() {
        return tvTopRight;
    }

    public void setTvTopRight(TextView tvTopRight) {
        this.tvTopRight = tvTopRight;
    }

    public TextView getTvBottomLeft() {
        return tvBottomLeft;
    }

    public void setTvBottomLeft(TextView tvBottomLeft) {
        this.tvBottomLeft = tvBottomLeft;
    }

    public void setText(CharSequence str) {
        tvDay.setText(str);
    }

    public CharSequence getText() {
        return tvDay.getText();
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
    private int width;
    private int height;

    @SuppressWarnings("UnusedDeclaration")
    public CustomCalendarCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        RelativeLayout all = new RelativeLayout(context);

        View centerInAll = new View(context);
        tvDay = new TextView(context);

        tvTopLeft = new TextView(context);
        tvTopRight = new TextView(context);
        tvBottomLeft = new TextView(context);
        tvBottomRight = new TextView(context);


        this.addView(tvDay);
        LayoutParams paramsTvDay = (LayoutParams) tvDay.getLayoutParams();
        paramsTvDay.width = LayoutParams.MATCH_PARENT;
        paramsTvDay.height = LayoutParams.MATCH_PARENT;
        tvDay.setLayoutParams(paramsTvDay);
        tvDay.setGravity(Gravity.CENTER);

        this.addView(all);
        LayoutParams paramsAll = (LayoutParams) all.getLayoutParams();
        paramsAll.width = LayoutParams.MATCH_PARENT;
        paramsAll.height = LayoutParams.MATCH_PARENT;
        all.setLayoutParams(paramsAll);


        all.addView(centerInAll);
        centerInAll.setId(View.generateViewId());
        LayoutParams paramsAllCenter = (LayoutParams) centerInAll.getLayoutParams();
        paramsAllCenter.width = 1;
        paramsAllCenter.height = LayoutParams.MATCH_PARENT;
        paramsAllCenter.addRule(RelativeLayout.CENTER_HORIZONTAL, TRUE);
        centerInAll.setLayoutParams(paramsAllCenter);



        all.addView(tvTopLeft);
        LayoutParams paramsIvBookingHere = new LayoutParams(50, 50);
        paramsIvBookingHere.addRule(RelativeLayout.LEFT_OF, centerInAll.getId());
        paramsIvBookingHere.setMargins(0, 10, 0, 0);
        tvTopLeft.setLayoutParams(paramsIvBookingHere);
        tvTopLeft.setGravity(Gravity.END);
        tvTopLeft.setTextColor(context.getResources().getColor(R.color.calendar_text_comment));


        all.addView(tvTopRight);
        LayoutParams paramsIvBlockTimes = new LayoutParams(50, 50);
        paramsIvBlockTimes.addRule(RelativeLayout.RIGHT_OF, centerInAll.getId());
        paramsIvBlockTimes.setMargins(0, 10, 0, 0);
        tvTopRight.setLayoutParams(paramsIvBlockTimes);
        tvTopRight.setGravity(Gravity.START);
        tvTopRight.setTextColor(context.getResources().getColor(R.color.calendar_text_comment));

        all.addView(tvBottomLeft);
        RelativeLayout.LayoutParams paramsIvTwoTeeStart = (RelativeLayout.LayoutParams) tvBottomLeft.getLayoutParams();
        paramsIvTwoTeeStart.height = 50;
        paramsIvTwoTeeStart.width = 50;
        paramsIvTwoTeeStart.addRule(RelativeLayout.LEFT_OF, centerInAll.getId());

        paramsIvTwoTeeStart.topMargin = 100;
        tvBottomLeft.setLayoutParams(paramsIvTwoTeeStart);
        tvBottomLeft.setGravity(Gravity.END);
        tvBottomLeft.setTextColor(context.getResources().getColor(R.color.calendar_text_comment));


        all.addView(tvBottomRight);
        LayoutParams paramsIvMemberOnly = new LayoutParams(50, 50);
        paramsIvMemberOnly.addRule(RelativeLayout.RIGHT_OF, centerInAll.getId());
        paramsIvMemberOnly.topMargin = 100;
        tvBottomRight.setLayoutParams(paramsIvMemberOnly);
        tvBottomRight.setGravity(Gravity.START);
        tvBottomRight.setTextColor(context.getResources().getColor(R.color.calendar_text_comment));


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

    public void setRangeState(CustomMonthCellDescriptor.RangeState rangeState) {
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

        if (rangeState == CustomMonthCellDescriptor.RangeState.FIRST) {
            mergeDrawableStates(drawableState, STATE_RANGE_FIRST);
        } else if (rangeState == CustomMonthCellDescriptor.RangeState.MIDDLE) {
            mergeDrawableStates(drawableState, STATE_RANGE_MIDDLE);
        } else if (rangeState == RangeState.LAST) {
            mergeDrawableStates(drawableState, STATE_RANGE_LAST);
        }
//
//        if (isTeeTimeSetting1) {
//            mergeDrawableStates(drawableState, STATE_TEE_TIME_SETTING_1);
//        }
//        if (isTeeTimeSetting2) {
//            mergeDrawableStates(drawableState, STATE_TEE_TIME_SETTING_2);
//        }

        return drawableState;
    }

    public TextView getTextView() {
        return tvDay;
    }

}
