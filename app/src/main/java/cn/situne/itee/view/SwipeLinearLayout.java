package cn.situne.itee.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class SwipeLinearLayout extends LinearLayout {
    private Boolean mIsHorizontal;
    private float mFirstX;
    private float mFirstY;

    public int getmRightViewWidth() {
        return mRightViewWidth;
    }

    private SwipeLayoutListener swipeLayoutListener;

    public void setmRightViewWidth(int mRightViewWidth) {
        this.mRightViewWidth = mRightViewWidth;
    }

    private int mRightViewWidth;

    private final int mDuration = 100;

    private final int mDurationStep = 10;

    private boolean mIsShown;
    private int width;
    public int position;

    private AfterShowRightListener afterShowRightListener;

    public SwipeLayoutListener getSwipeLayoutListener() {
        return swipeLayoutListener;
    }

    public void setSwipeLayoutListener(SwipeLayoutListener swipeLayoutListener) {
        this.swipeLayoutListener = swipeLayoutListener;
    }

    public SwipeLinearLayout(Context context, int mRightViewWidth) {
        super(context);
        this.mRightViewWidth = mRightViewWidth;
        width = getResources().getDisplayMetrics().widthPixels;

    }


    /**
     * return true, deliver to listView. return false, deliver to child. if
     * move, return true
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float lastX = ev.getX();
        float lastY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsHorizontal = null;
               // System.out.println("onInterceptTouchEvent----->ACTION_DOWN");
                mFirstX = lastX;
                mFirstY = lastY;
                int temp = width - mRightViewWidth;
                if (lastX < temp) {
                    if (mIsShown) {
                        hiddenRight(this);
                        return true;
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = lastX - mFirstX;
                float dy = lastY - mFirstY;
                if (Math.abs(dx) >= 5 && Math.abs(dy) >= 5) {
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean isHitCurItemLeft(float x) {
        return x < getWidth() - mRightViewWidth;
    }

    /**
     * @param dx
     * @param dy
     * @return judge if can judge scroll direction
     */
    private boolean judgeScrollDirection(float dx, float dy) {
        boolean canJudge = true;

        if (Math.abs(dx) > 30 && Math.abs(dx) > 2 * Math.abs(dy)) {
            mIsHorizontal = true;
            if (swipeLayoutListener != null)
                swipeLayoutListener.scrollView(this);

           // System.out.println("mIsHorizontal---->" + mIsHorizontal);
        } else if (Math.abs(dy) > 30 && Math.abs(dy) > 2 * Math.abs(dx)) {
            mIsHorizontal = false;
           // System.out.println("mIsHorizontal---->" + mIsHorizontal);
        } else {
            canJudge = false;
        }

        return canJudge;
    }

    /**
     * return false, can't move any direction. return true, cant't move
     * vertical, can move horizontal. return super.onTouchEvent(ev), can move
     * both.
     */
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        float lastX = ev.getX();
        float lastY = ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
               // System.out.println("---->ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = lastX - mFirstX;
                float dy = lastY - mFirstY;

                // confirm is scroll direction
                if (mIsHorizontal == null) {
                    if (!judgeScrollDirection(dx, dy)) {
                        break;
                    }
                }

                if (mIsHorizontal) {
//                    if (mIsShown && mPreItemView != mCurrentItemView) {
//                        System.out.println("2---> hiddenRight");
//                        /**
//                         * 情况二：
//                         * <p>
//                         * 一个Item的右边布局已经显示，
//                         * <p>
//                         * 这时候左右滑动另外一个item,那个右边布局显示的item隐藏其右边布局
//                         * <p>
//                         * 向左滑动只触发该情况，向右滑动还会触发情况五
//                         */
//                        hiddenRight(mPreItemView);
//                    }
//
                    if (mIsShown) {
                        dx = dx - mRightViewWidth;
                        //System.out.println("======dx " + dx);
                    }
                    // can't move beyond boundary
                    if (dx < 0 && dx > -mRightViewWidth) {
                        this.scrollTo((int) (-dx), 0);
                    }

                    return true;
                } else {
                    if (mIsShown) {
                       // System.out.println("3---> hiddenRight");
                        /**
                         * 情况三：
                         * <p>
                         * 一个Item的右边布局已经显示，
                         * <p>
                         * 这时候上下滚动ListView,那么那个右边布局显示的item隐藏其右边布局
                         */
//                        hiddenRight(mPreItemView);
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
               // System.out.println("============ACTION_UP");
                clearPressedState();
                if (mIsShown) {
                    //System.out.println("4---> hiddenRight");
                    /**
                     * 情况四：
                     * <p>
                     * 一个Item的右边布局已经显示，
                     * <p>
                     * 这时候左右滑动当前一个item,那个右边布局显示的item隐藏其右边布局
                     */
//                    hiddenRight(mPreItemView);
                }
                if (mIsHorizontal != null && mIsHorizontal) {
                    if (mFirstX - lastX > mRightViewWidth / 2) {

                        showRight(this);
                        if (afterShowRightListener != null) {
                            afterShowRightListener.doAfterShowRight(this);
                        }

                    } else {
                       // System.out.println("5---> hiddenRight");
                        /**
                         * 情况五：
                         * <p>
                         * 向右滑动一个item,且滑动的距离超过了右边View的宽度的一半，隐藏之。
                         */
                        hiddenRight(this);
                    }

                    return true;
                }

                break;
        }

        return true;
    }

    private void clearPressedState() {

        if (this != null) {
            this.setPressed(false);
            setPressed(false);
            refreshDrawableState();
        }
        // invalidate();
    }

    private void showRight(View view) {
        //System.out.println("=========showRight");

        Message msg = new MoveHandler().obtainMessage();
        msg.obj = view;
        msg.arg1 = view.getScrollX();
        msg.arg2 = mRightViewWidth;
        msg.sendToTarget();

        mIsShown = true;
    }

    private void hiddenRight(View view) {
      //  System.out.println("=========hiddenRight");
        if (this == null) {
            return;
        }
        Message msg = new MoveHandler().obtainMessage();//
        msg.obj = view;
        msg.arg1 = view.getScrollX();
        msg.arg2 = 0;

        msg.sendToTarget();

        mIsShown = false;
    }

    public interface AfterShowRightListener {
        void doAfterShowRight(SwipeLinearLayout swipeLinearLayout);
    }

    public void setAfterShowRightListener(AfterShowRightListener afterShowRightListener) {
        this.afterShowRightListener = afterShowRightListener;
    }

    public void hideRight() {
        hiddenRight(this);
    }

    /**
     * show or hide right layout animation
     */
    @SuppressLint("HandlerLeak")
    class MoveHandler extends Handler {
        int stepX = 0;

        int fromX;

        int toX;

        View view;

        private boolean mIsInAnimation = false;

        private void animatioOver() {
            mIsInAnimation = false;
            stepX = 0;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (stepX == 0) {
                if (mIsInAnimation) {
                    return;
                }
                mIsInAnimation = true;
                view = (View) msg.obj;
                fromX = msg.arg1;
                toX = msg.arg2;
                stepX = (int) ((toX - fromX) * mDurationStep * 1.0 / mDuration);
                if (stepX < 0 && stepX > -1) {
                    stepX = -1;
                } else if (stepX > 0 && stepX < 1) {
                    stepX = 1;
                }
                if (Math.abs(toX - fromX) < 10) {
                    view.scrollTo(toX, 0);
                    animatioOver();
                    return;
                }
            }

            fromX += stepX;
            boolean isLastStep = (stepX > 0 && fromX > toX) || (stepX < 0 && fromX < toX);
            if (isLastStep) {
                fromX = toX;
            }

            view.scrollTo(fromX, 0);
            invalidate();

            if (!isLastStep) {
                this.sendEmptyMessageDelayed(0, mDurationStep);
            } else {
                animatioOver();
            }
        }
    }

    public interface SwipeLayoutListener {

        void scrollView(View item);

    }
}
