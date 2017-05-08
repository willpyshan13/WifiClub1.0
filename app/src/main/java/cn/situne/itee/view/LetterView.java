package cn.situne.itee.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("UnusedDeclaration")
public class LetterView extends View {
    public static final int COLOR_BG = 0xaa000000;
    public static final int COLOR_NO_BG = 0x00000000;
    public static final int COLOR_TEXT_SELECTED = 0xff386AB7;
    public static final int COLOR_TEXT_NORMAL = 0xff000000;
    public static final int SIZE_TEXT = 22;
    private static final String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private Paint paint;
    private int width;
    private int height;

    public void setViewHeight(int height) {
        this.height = height;
    }

    private int singleHeight;

    public int getViewHeight() {
        return height;
    }

    public LetterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(SIZE_TEXT);
        paint.setFakeBoldText(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (width == 0 || height == 0) {
            width = getWidth();
            height = 1000;
            singleHeight = height / letters.length();
        }

        for (int i = 0; i < letters.length(); i++) {
            if (currentSelectedIndex == i) {
                paint.setColor(COLOR_TEXT_SELECTED);
            } else {
                paint.setColor(COLOR_TEXT_NORMAL);
            }
            float xPos = (width - paint.measureText(letters.charAt(i) + StringUtils.EMPTY)) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(letters.charAt(i) + StringUtils.EMPTY, xPos, yPos, paint);
        }
    }

    private int currentSelectedIndex = 0;

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        currentSelectedIndex = (int) (event.getY() / singleHeight);
        if (currentSelectedIndex < 0) {
            currentSelectedIndex = 0;
        }
        if (currentSelectedIndex > letters.length() - 1) {
            currentSelectedIndex = letters.length() - 1;
        }
        if (letterChangeListener != null) {
            letterChangeListener.onLetterChange(currentSelectedIndex);
        }
        invalidate();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(COLOR_BG);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundColor(COLOR_NO_BG);
                break;
        }
        return true;
    }


    private OnLetterChangeListener letterChangeListener;

    public void setOnLetterChangeListener(OnLetterChangeListener letterChangeListener) {
        this.letterChangeListener = letterChangeListener;
    }

    public interface OnLetterChangeListener {
        void onLetterChange(int selectedIndex);
    }

    public void setSelectedIndex(int selectedIndex) {
        currentSelectedIndex = selectedIndex;
        invalidate();
    }

}
