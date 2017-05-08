package com.squareup.timessquare;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

/**
 * TableRow that draws a divider between each cell. To be used with {@link CustomCalendarGridView}.
 */
public class ChooseCalendarRowView extends ViewGroup implements View.OnClickListener {
    private boolean isHeaderRow;
    private ChooseMonthView.Listener listener;

    public ChooseCalendarRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        child.setOnClickListener(this);
        super.addView(child, index, params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        long start = System.currentTimeMillis();
        final int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int rowHeight = 0;
        for (int c = 0, numChildren = getChildCount(); c < numChildren; c++) {
            final View child = getChildAt(c);
            // Calculate width cells, making sure to cover totalWidth.
            int l = ((c + 0) * totalWidth) / 7;
            int r = ((c + 1) * totalWidth) / 7;
            int cellSize = r - l;
            int cellWidthSpec = makeMeasureSpec(cellSize, EXACTLY);
            int cellHeightSpec = isHeaderRow ? makeMeasureSpec(cellSize, AT_MOST) : cellWidthSpec;
            child.measure(cellWidthSpec, cellHeightSpec * 3 / 2);
            // The row height is the height of the tallest cell.
            if (child.getMeasuredHeight() > rowHeight) {
                rowHeight = child.getMeasuredHeight();
            }
        }
        final int widthWithPadding = totalWidth + getPaddingLeft() + getPaddingRight();
        final int heightWithPadding = rowHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(widthWithPadding, heightWithPadding);
        Logr.d("Row.onMeasure %d ms", System.currentTimeMillis() - start);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        long start = System.currentTimeMillis();
        int cellHeight = bottom - top;
        int width = (right - left);
        for (int c = 0, numChildren = getChildCount(); c < numChildren; c++) {
            final View child = getChildAt(c);
            int l = ((c + 0) * width) / 7;
            int r = ((c + 1) * width) / 7;
            child.layout(l, 0, r, cellHeight);
        }
        Logr.d("Row.onLayout %d ms", System.currentTimeMillis() - start);
    }

    public void setIsHeaderRow(boolean isHeaderRow) {
        this.isHeaderRow = isHeaderRow;
    }

    @Override
    public void onClick(View v) {
        // Header rows don't have a click listener
        if (listener != null) {
            listener.handleClick((CustomMonthCellDescriptor) v.getTag());
        }
    }

    public void setListener(ChooseMonthView.Listener listener) {
        this.listener = listener;
    }

    public void setCellBackground(int resId) {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setBackgroundResource(resId);
        }
    }

    public void setCellTextColor(int resId) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof CustomCalendarCellView) {
                ((CustomCalendarCellView) getChildAt(i)).getTextView().setTextColor(resId);
            } else {
                ((TextView) v).setTextColor(resId);
            }
        }
    }

    public void setCellTextColor(ColorStateList colors) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof CustomCalendarCellView) {
                ((CustomCalendarCellView) getChildAt(i)).getTextView().setTextColor(colors);
            } else {
                ((TextView) v).setTextColor(colors);
            }
        }
    }

    public void setTypeface(Typeface typeface) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof CustomCalendarCellView) {
                ((CustomCalendarCellView) getChildAt(i)).getTextView().setTypeface(typeface);
            } else {
                ((TextView) v).setTypeface(typeface);
            }
        }
    }
}
