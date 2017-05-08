package cn.situne.itee.fragment.teetime;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;


public abstract class RightDrawableOnTouchListener implements View.OnTouchListener {
    Drawable drawable;

    public RightDrawableOnTouchListener(EditText view) {
        super();
        final Drawable[] drawables = view.getCompoundDrawables();
        if (drawables.length == 4) {
            this.drawable = drawables[2];
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
     */
    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && drawable != null) {
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            final Rect bounds = drawable.getBounds();
            int fuzz = 60;
            if (x >= (v.getWidth() - bounds.width() - fuzz) && x <= (v.getWidth() - v.getPaddingRight() + fuzz)
                    && y >= (v.getPaddingTop() - fuzz) && y <= (v.getHeight() - v.getPaddingBottom()) + fuzz) {
                return onDrawableTouch(event);
            }
        }
        return false;
    }

    public abstract boolean onDrawableTouch(final MotionEvent event);

}