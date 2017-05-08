
/**
 * Project Name: itee
 * File Name:	 CheckSwitchButton.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-03-4
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.CheckBox;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.fragment.BaseFragment;

/**
 * ClassName:CheckSwitchButton <br/>
 * Function: 工程中的开关按钮. <br/>

 * Date: 2015-03-04 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
@SuppressWarnings("UnusedDeclaration")
public class CheckSwitchButton extends CheckBox {
    public static final int TYPE_ON_OR_OFF = 0;
    public static final int TYPE_METER_OR_YARD = 1;
    public static final int TYPE_SUN_OR_MON = 2;
    public static final int TYPE_DISCOUNT_OR_CURRENCY = 3;

    private final int MAX_ALPHA = 255;
    private final float VELOCITY = 350;
    // 开关种类
    public int mType;
    private Paint mPaint;
    private ViewParent mParent;
    private Bitmap mBottom;
    private Bitmap mCurBtnPic;
    private Bitmap mBtnPressed;
    private Bitmap mBtnNormal;
    private Bitmap mFrame;
    private Bitmap mMask;
    private RectF mSaveLayerRectF;
    private PorterDuffXfermode mXfermode;
    private float mFirstDownY;
    private float mFirstDownX;
    private float mRealPos;
    private float mBtnPos;
    private float mBtnOnPos;
    private float mBtnOffPos;
    private float mMaskWidth;
    private float mMaskHeight;
    private float mBtnWidth;
    private float mBtnInitPos;
    private int mClickTimeout;
    private int mTouchSlop;
    private int mAlpha = MAX_ALPHA;
    private boolean mChecked = false;
    private boolean mBroadcasting;
    private boolean mTurningOn;
    private PerformClick mPerformClick;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;
    private boolean mAnimating;
    private float mVelocity;
    // private final float EXTENDED_OFFSET_Y = 15;
    private float mExtendOffsetY;
    private float mAnimationPosition;
    private float mAnimatedVelocity;

    public CheckSwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.checkboxStyle);
    }

    public CheckSwitchButton(Context context, int mType) {
        this(context, null);
        this.mType = mType;
        initView(context);
    }

    public CheckSwitchButton(Context context) {
        this(context, null);
        this.mType = TYPE_ON_OR_OFF;
        initView(context);
    }

    public CheckSwitchButton(BaseFragment mFragment) {
        this(mFragment.getActivity(), null);
        this.mType = TYPE_ON_OR_OFF;
        initView(mFragment.getActivity());
    }

    public CheckSwitchButton(BaseFragment mFragment, int mType) {
        this(mFragment.getActivity(), null);
        this.mType = mType;
        initView(mFragment.getActivity());
    }

    public CheckSwitchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint("Recycle")
    private void initView(Context context) {
        /*TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.switch_button);
        mType = typedArray.getInteger(R.styleable.switch_button_type, 1);*/

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        Resources resources = context.getResources();

        // get viewConfiguration
        mClickTimeout = ViewConfiguration.getPressedStateDuration() + ViewConfiguration.getTapTimeout();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        // get Bitmap
        int checkSwitch = 0;
        if (mType == TYPE_ON_OR_OFF) {// on or off
            checkSwitch = R.drawable.checkswitch_button_on_off;
        } else if (mType == TYPE_METER_OR_YARD) {// meters or yards
            checkSwitch = R.drawable.checkswitch_button_my;
        } else if (mType == TYPE_SUN_OR_MON) {// SUN or MON
            checkSwitch = R.drawable.checkswitch_button_sm;
        }

        if (mType > 2) {
            if (Constants.CURRENCY_ID_CN.equals(AppUtils.getCurrentCurrencyId(context))) {
                checkSwitch = R.drawable.checkswitch_button_pr;
            } else if (Constants.CURRENCY_ID_US.equals(AppUtils.getCurrentCurrencyId(context))) {
                checkSwitch = R.drawable.checkswitch_button_pu;
            } else if (Constants.CURRENCY_ID_EU.equals(AppUtils.getCurrentCurrencyId(context))) {
                checkSwitch = R.drawable.checkswitch_button_pe;
            } else if (Constants.CURRENCY_ID_FR.equals(AppUtils.getCurrentCurrencyId(context))) {
                checkSwitch = R.drawable.checkswitch_button_pg;
            }
        }

        mBottom = BitmapFactory.decodeResource(resources, checkSwitch);

        if (mType > 2) {
            mBtnPressed = BitmapFactory.decodeResource(resources, R.drawable.checkswitch_btn_pressed_big);
            mBtnNormal = BitmapFactory.decodeResource(resources, R.drawable.checkswitch_btn_unpressed_big);
            mFrame = BitmapFactory.decodeResource(resources, R.drawable.checkswitch_frame_big);
            mMask = BitmapFactory.decodeResource(resources, R.drawable.checkswitch_mask_big);
        } else {
            mBtnPressed = BitmapFactory.decodeResource(resources, R.drawable.checkswitch_btn_pressed);
            mBtnNormal = BitmapFactory.decodeResource(resources, R.drawable.checkswitch_btn_unpressed);
            mFrame = BitmapFactory.decodeResource(resources, R.drawable.checkswitch_frame);
            mMask = BitmapFactory.decodeResource(resources, R.drawable.checkswitch_mask);
        }

        mCurBtnPic = mBtnNormal;

        mBtnWidth = mBtnPressed.getWidth();
        mMaskWidth = mMask.getWidth();
        mMaskHeight = mMask.getHeight();

        mBtnOffPos = mMaskWidth - mBtnWidth / 2;
        mBtnOnPos = mBtnWidth / 2;
        mBtnPos = mChecked ? mBtnOnPos : mBtnOffPos;
        mRealPos = getRealPos(mBtnPos);
        final float density = getResources().getDisplayMetrics().density;
        mVelocity = (int) (VELOCITY * density + 0.5f);
        mExtendOffsetY = 0;
        mSaveLayerRectF = new RectF(0, mExtendOffsetY, mMask.getWidth(), mMask.getHeight() + mExtendOffsetY);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    }

    @Override
    public void setEnabled(boolean enabled) {
        mAlpha = enabled ? MAX_ALPHA : MAX_ALPHA / 2;
        super.setEnabled(enabled);
    }

    public boolean isChecked() {
        return mChecked;
    }

    /**
     * <p>
     * Changes the checked state of this button.
     * </p>
     *
     * @param checked true to check the button, false to uncheck it
     */
    public void setChecked(boolean checked) {

        if (mChecked != checked) {
            mChecked = checked;

            mBtnPos = checked ? mBtnOnPos : mBtnOffPos;
            mRealPos = getRealPos(mBtnPos);
            invalidate();

            // Avoid infinite recursions if setChecked() is called from a
            // listener
            if (mBroadcasting) {
                return;
            }

            mBroadcasting = true;
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(CheckSwitchButton.this, mChecked);
            }
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(CheckSwitchButton.this, mChecked);
            }

            mBroadcasting = false;
        }
    }

    public void toggle() {
        setChecked(!mChecked);
    }

    private void setCheckedDelayed(final boolean checked) {
        this.postDelayed(new Runnable() {

            @Override
            public void run() {
                setChecked(checked);
            }
        }, 10);
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes. This callback is used for internal purpose only.
     *
     * @param listener the callback to call on checked state change
     */
    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        float deltaX = Math.abs(x - mFirstDownX);
        float deltaY = Math.abs(y - mFirstDownY);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                attemptClaimDrag();
                mFirstDownX = x;
                mFirstDownY = y;
                mCurBtnPic = mBtnPressed;
                mBtnInitPos = mChecked ? mBtnOnPos : mBtnOffPos;
                break;
            case MotionEvent.ACTION_MOVE:
                float time = event.getEventTime() - event.getDownTime();
                mBtnPos = mBtnInitPos + event.getX() - mFirstDownX;
                if (mBtnPos >= mBtnOffPos) {
                    mBtnPos = mBtnOffPos;
                }
                if (mBtnPos <= mBtnOnPos) {
                    mBtnPos = mBtnOnPos;
                }
                mTurningOn = mBtnPos > (mBtnOffPos - mBtnOnPos) / 2 + mBtnOnPos;

                mRealPos = getRealPos(mBtnPos);
                break;
            case MotionEvent.ACTION_UP:
                mCurBtnPic = mBtnNormal;
                time = event.getEventTime() - event.getDownTime();
                if (deltaY < mTouchSlop && deltaX < mTouchSlop && time < mClickTimeout) {
                    if (mPerformClick == null) {
                        mPerformClick = new PerformClick();
                    }
                    if (!post(mPerformClick)) {
                        performClick();
                    }
                } else {
                    startAnimation(!mTurningOn);
                }
                break;
        }

        invalidate();
        return isEnabled();
    }

    @Override
    public boolean performClick() {
        startAnimation(!mChecked);
        return true;
    }

    /**
     * ͨTries to claim the user's drag motion, and requests disallowing any
     * ancestors from stealing events in the drag.
     */
    private void attemptClaimDrag() {
        mParent = getParent();
        if (mParent != null) {
            mParent.requestDisallowInterceptTouchEvent(true);
        }
    }

    private float getRealPos(float btnPos) {
        return btnPos - mBtnWidth / 2;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        canvas.saveLayerAlpha(mSaveLayerRectF, mAlpha, Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
                | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        canvas.drawBitmap(mMask, 0, mExtendOffsetY, mPaint);
        mPaint.setXfermode(mXfermode);

        canvas.drawBitmap(mBottom, mRealPos, mExtendOffsetY, mPaint);
        mPaint.setXfermode(null);
        canvas.drawBitmap(mFrame, 0, mExtendOffsetY, mPaint);

        canvas.drawBitmap(mCurBtnPic, mRealPos, mExtendOffsetY, mPaint);
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) mMaskWidth, (int) (mMaskHeight + 2 * mExtendOffsetY));
    }

    private void startAnimation(boolean turnOn) {
        mAnimating = true;
        mAnimatedVelocity = turnOn ? -mVelocity : mVelocity;
        mAnimationPosition = mBtnPos;

        new SwitchAnimation().run();
    }

    private void stopAnimation() {
        mAnimating = false;
    }

    private void doAnimation() {
        mAnimationPosition += mAnimatedVelocity * FrameAnimationController.ANIMATION_FRAME_DURATION / 1000;
        if (mAnimationPosition <= mBtnOnPos) {
            stopAnimation();
            mAnimationPosition = mBtnOnPos;
            setCheckedDelayed(true);
        } else if (mAnimationPosition >= mBtnOffPos) {
            stopAnimation();
            mAnimationPosition = mBtnOffPos;
            setCheckedDelayed(false);
        }
        moveView(mAnimationPosition);
    }

    private void moveView(float position) {
        mBtnPos = position;
        mRealPos = getRealPos(mBtnPos);
        invalidate();
    }

    private final class PerformClick implements Runnable {
        public void run() {
            performClick();
        }
    }

    private final class SwitchAnimation implements Runnable {

        @Override
        public void run() {
            if (!mAnimating) {
                return;
            }
            doAnimation();
            FrameAnimationController.requestAnimationFrame(this);
        }
    }
}

