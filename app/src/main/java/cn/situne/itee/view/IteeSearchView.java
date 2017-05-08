/**
 * Project Name: itee
 * File Name:	 IteeSearchView.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-03-27
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.teetime.RightDrawableOnTouchListener;

/**
 * ClassName:IteeSearchView <br/>
 * Function: 查询控件 <br/>
 * UI:  03-05-04
 * Date: 2015-03-27 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class IteeSearchView extends EditText {


    private Drawable mRightIconSelected;
    private Drawable mLeftIcon;
    private Drawable mLeftIconSelected;
    private int mIconWidth;

    private OnClickListener rightIconListener;
    private OnClickListener leftIconListener;

    public IteeSearchView(Context context) {
        super(context);
        this.setPadding(0, 0, 0, 0);
        this.setLines(1);
        this.setSingleLine();
        setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        addTextChangedListener(watcher);
    }

    public IteeSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextChangedListener(watcher);
    }

    public void setLeftIconListener(OnClickListener leftIconListener) {
        this.leftIconListener = leftIconListener;
    }

    public void setRightIconListener(OnClickListener rightIconListener) {
        this.rightIconListener = rightIconListener;
    }

    public void setIcon(int iconHeight, int rightIcon, int rightIconSelected, int leftIcon, int leftIconSelected) {

        mIconWidth = iconHeight;
        Drawable mRightIcon = getResources().getDrawable(rightIcon);
        if (mRightIcon != null) {
            mRightIcon.setBounds(0, 0, mIconWidth, iconHeight);
        }
        mRightIconSelected = getResources().getDrawable(R.drawable.btn_tel_delete);
        if (mRightIconSelected != null) {
            mRightIconSelected.setBounds(0, 0, mIconWidth, iconHeight);
        }
        mLeftIcon = getResources().getDrawable(leftIcon);
        if (mLeftIcon != null) {
            mLeftIcon.setBounds(0, 0, mIconWidth, iconHeight);
        }
        mLeftIconSelected = getResources().getDrawable(leftIconSelected);
        if (mLeftIconSelected != null) {
            mLeftIconSelected.setBounds(0, 0, mIconWidth, iconHeight);
        }
        this.setCompoundDrawablesRelative(mLeftIcon, null, null, null);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (Utils.isStringNotNullOrEmpty(IteeSearchView.this.getText().toString())) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    this.setCompoundDrawablesRelative(mLeftIconSelected, null, mRightIconSelected, null);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    this.setCompoundDrawablesRelative(mLeftIcon, null, mRightIconSelected, null);
                    int eventX = (int) event.getRawX();
                    int eventY = (int) event.getRawY();
                    Rect rect = new Rect();
                    getGlobalVisibleRect(rect);


                    rect.left = rect.right - mIconWidth;
                    if (rect.contains(eventX, eventY) && rightIconListener != null) {
                        rightIconListener.onClick(IteeSearchView.this);
                    }
                    Rect leftRect = new Rect();
                    getGlobalVisibleRect(leftRect);
                    leftRect.right = leftRect.left + mIconWidth - 3;
                    if (leftRect.contains(eventX, eventY) && leftIconListener != null) {

                        leftIconListener.onClick(IteeSearchView.this);

                    }
                    break;
                default:
                    break;


            }
        }


        return super.onTouchEvent(event);
    }


    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (Utils.isStringNotNullOrEmpty(IteeSearchView.this.getText().toString())) {
//                if (imNameDrawable == null) {
//                    imNameDrawable = getResources().getDrawable(R.drawable.btn_tel_delete);
//                }
//                imNameDrawable.setBounds(0, 0, iHeight, iHeight);
//                IteeSearchView.this.setCompoundDrawables(null, null, imNameDrawable, null);
                IteeSearchView.this.setCompoundDrawablesRelative(mLeftIconSelected, null, mRightIconSelected, null);
            } else {
                IteeSearchView.this.setCompoundDrawablesRelative(mLeftIcon, null, null, null);
            }
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (Utils.isStringNotNullOrEmpty(IteeSearchView.this.getText().toString())) {
                IteeSearchView.this.setCompoundDrawablesRelative(mLeftIconSelected, null, mRightIconSelected, null);
//                if (imNameDrawable == null) {
//                    imNameDrawable = getResources().getDrawable(R.drawable.btn_tel_delete);
//                }
//                imNameDrawable.setBounds(0, 0, iHeight, iHeight);
//                IteeSearchView.this.setCompoundDrawables(null, null, imNameDrawable, null);
//                setOnTouchListener(new RightDrawableOnTouchListener(ITeeClearUpEditText.this) {
//                    @Override
//                    public boolean onDrawableTouch(final MotionEvent event) {
//                        ITeeClearUpEditText.this.setText(Constants.STR_EMPTY);
//                        return false;
//                    }
//                });
            } else {
                IteeSearchView.this.setCompoundDrawablesRelative(mLeftIcon, null, null, null);
            }

        }
    };

}
