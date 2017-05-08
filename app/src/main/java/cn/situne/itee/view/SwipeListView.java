package cn.situne.itee.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import cn.situne.itee.entity.BaseViewHolder;


public class SwipeListView extends ListView {


    public static final String DISABLE_RIGHT = "disable_right";
    private Boolean mIsHorizontal;


    private View mPreItemView;

    private View mCurrentItemView;

    private float mFirstX;

    private float mFirstY;

    private int mRightViewWidth;

    private SeipeListViewListener seipeListViewListener;

    public void setSeipeListViewListener(SeipeListViewListener seipeListViewListener) {

        this.seipeListViewListener = seipeListViewListener;
    }

    // private boolean mIsInAnimation = false;
    private final int mDuration = 100;

    private final int mDurationStep = 10;

    private boolean mIsShown;


    public SwipeListView(Context context) {
        this(context, null);
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setSelector(new ColorDrawable(Color.TRANSPARENT));
//        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
//                R.styleable.swipelistviewstyle);

        //获取自定义属性和默认值
//      mRightViewWidth = (int) mTypedArray.getDimension(R.styleable.swipelistviewstyle_right_width, 200);

//      mTypedArray.recycle();
    }

    /**
     * return true, deliver to listView. return false, deliver to child. if
     * move, return true
     */
    @Override
    public boolean onInterceptTouchEvent(@NonNull MotionEvent ev) {
        float lastX = ev.getX();
        float lastY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsHorizontal = null;
                //System.out.println("onInterceptTouchEvent----->ACTION_DOWN");
                mFirstX = lastX;
                mFirstY = lastY;
                int motionPosition = pointToPosition((int) mFirstX, (int) mFirstY);

                if (motionPosition >= 0) {
                    View currentItemView = getChildAt(motionPosition - getFirstVisiblePosition());
                    mPreItemView = mCurrentItemView;
                    mCurrentItemView = currentItemView;
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
                //System.out.println("onInterceptTouchEvent----->ACTION_UP");
                if (mIsShown && (mPreItemView != mCurrentItemView || isHitCurItemLeft(lastX))) {
                   // System.out.println("1---> hiddenRight");
                    /**
                     * 情况一：
                     * <p>
                     * 一个Item的右边布局已经显示，
                     * <p>
                     * 这时候点击任意一个item, 那么那个右边布局显示的item隐藏其右边布局
                     */
                    hiddenRight(mPreItemView);
                }
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
            if (seipeListViewListener != null)
                seipeListViewListener.scrollItem(mCurrentItemView);
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
        String disableFlag = null;
        if (mCurrentItemView != null) {
            if (mCurrentItemView.getTag() instanceof BaseViewHolder) {

                disableFlag = ((BaseViewHolder) mCurrentItemView.getTag()).disableright;
            } else if (mCurrentItemView.getTag() instanceof String) {

                disableFlag = String.valueOf(mCurrentItemView.getTag());

            }
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
              //  System.out.println("---->ACTION_DOWN");
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
                    if (mIsShown && mPreItemView != mCurrentItemView) {
                        //System.out.println("2---> hiddenRight");
                        /**
                         * 情况二：
                         * <p>
                         * 一个Item的右边布局已经显示，
                         * <p>
                         * 这时候左右滑动另外一个item,那个右边布局显示的item隐藏其右边布局
                         * <p>
                         * 向左滑动只触发该情况，向右滑动还会触发情况五
                         */
                        hiddenRight(mPreItemView);
                    }

                    if (mIsShown && mPreItemView == mCurrentItemView) {
                        dx = dx - mRightViewWidth;
                      //  System.out.println("======dx " + dx);
                    }

                    // can't move beyond boundary
                    if (dx < 0 && dx > -mRightViewWidth) {
                        if (mCurrentItemView != null && !DISABLE_RIGHT.equals(disableFlag)) {
                            mCurrentItemView.scrollTo((int) (-dx), 0);
                        }
                    }

                    return true;
                } else {
                    if (mIsShown) {
                        //System.out.println("3---> hiddenRight");
                        /**
                         * 情况三：
                         * <p>
                         * 一个Item的右边布局已经显示，
                         * <p>
                         * 这时候上下滚动ListView,那么那个右边布局显示的item隐藏其右边布局
                         */
                        hiddenRight(mPreItemView);
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
               // System.out.println("============ACTION_UP");
                clearPressedState();
                if (mIsShown) {
                   // System.out.println("4---> hiddenRight");
                    /**
                     * 情况四：
                     * <p>
                     * 一个Item的右边布局已经显示，
                     * <p>
                     * 这时候左右滑动当前一个item,那个右边布局显示的item隐藏其右边布局
                     */
                    hiddenRight(mPreItemView);
                }

                if (mIsHorizontal != null && mIsHorizontal) {

                    if (mCurrentItemView != null && !DISABLE_RIGHT.equals(disableFlag)) {
                        if (mFirstX - lastX > mRightViewWidth / 2) {
                            showRight(mCurrentItemView);
                        } else {
                           // System.out.println("5---> hiddenRight");
                            /**
                             * 情况五：
                             * <p>
                             * 向右滑动一个item,且滑动的距离超过了右边View的宽度的一半，隐藏之。
                             */
                            hiddenRight(mCurrentItemView);
                        }
                    }

                    if (mCurrentItemView != null) {
                        if (mIsShown) {
                            mCurrentItemView.setTag(mCurrentItemView.getId(), "show");
                        } else {
                            mCurrentItemView.setTag(mCurrentItemView.getId(), "hidden");
                        }
                    }

                    return true;
                }

                break;
        }

        return super.onTouchEvent(ev);
    }

    private void clearPressedState() {
        // TODO current item is still has background, issue
        if (mCurrentItemView != null) {
            mCurrentItemView.setPressed(false);
            setPressed(false);
            refreshDrawableState();
        }
        // invalidate();
    }

    public static final int SWIPLISTVIEW_TAG_FLAG = 999;

    private void showRight(View view) {
       // System.out.println("=========showRight");

        if (view != null) {
            Message msg = new MoveHandler().obtainMessage();
            msg.obj = view;
            msg.arg1 = view.getScrollX();
            msg.arg2 = mRightViewWidth;
            msg.sendToTarget();

            mIsShown = true;
        }
    }

    private void hiddenRight(View view) {
        //System.out.println("=========hiddenRight");
        if (mCurrentItemView == null) {
            return;
        }

        Message msg = new MoveHandler().obtainMessage();//
        msg.obj = view;
        if (view != null) {
            msg.arg1 = view.getScrollX();
            msg.arg2 = 0;
            msg.sendToTarget();
        }

        mIsShown = false;
    }

    public boolean isDeleteStatus() {
        return mIsShown;
    }

    public void hiddenRight() {
        hiddenRight(mCurrentItemView);
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

    public int getRightViewWidth() {
        return mRightViewWidth;
    }

    public void setRightViewWidth(int mRightViewWidth) {
        this.mRightViewWidth = mRightViewWidth;
    }

    public interface SeipeListViewListener {

        void scrollItem(View item);

    }
}
