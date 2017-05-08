/**
 * Project Name: itee
 * File Name:	 IteeNumerEditView.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-04-08
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view;

import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import cn.situne.itee.R;
import cn.situne.itee.fragment.BaseFragment;

/**
 * ClassName:IteeNumberEditView <br/>
 * Function: number edit view. <br/>
 * Date: 2015-04-08 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class IteeNumberEditView extends RelativeLayout {

    private BaseFragment mBaseFragment;

    private IteeTextView tvNumber;

    private int currentNum;

    private int minNum;
    private int maxNum;
    private NumberEditListener listener;

    public IteeNumberEditView(BaseFragment mBaseFragment) {
        super(mBaseFragment.getActivity());
        this.mBaseFragment = mBaseFragment;
        init();
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getMinNum() {
        return minNum;
    }

    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    private void init() {
        currentNum = 1;

        tvNumber = new IteeTextView(mBaseFragment);
        tvNumber.setGravity(Gravity.CENTER);

        IteeButton btnAdd = new IteeButton(mBaseFragment.getActivity());
        IteeButton btnMinus = new IteeButton(mBaseFragment.getActivity());
        btnAdd.setBackground(null);
        btnMinus.setBackground(null);
        setBackgroundResource(R.drawable.bg_number_edit);

        RelativeLayout.LayoutParams tvNumberLayoutParams = new LayoutParams(BaseFragment.MATCH_PARENT, BaseFragment.MATCH_PARENT);
        tvNumber.setLayoutParams(tvNumberLayoutParams);

        RelativeLayout.LayoutParams btnAddLayoutParams = new LayoutParams(mBaseFragment.getActualWidthOnThisDevice(50), BaseFragment.MATCH_PARENT);
        btnAddLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, BaseFragment.LAYOUT_TRUE);
        btnAdd.setLayoutParams(btnAddLayoutParams);

        RelativeLayout.LayoutParams btnMinusLayoutParams = new LayoutParams(mBaseFragment.getActualWidthOnThisDevice(50), BaseFragment.MATCH_PARENT);
        btnMinusLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, BaseFragment.LAYOUT_TRUE);
        btnMinus.setLayoutParams(btnMinusLayoutParams);

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                add();
            }
        });

        btnMinus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                minus();
            }
        });

        addView(tvNumber);
        addView(btnAdd);
        addView(btnMinus);
    }


    private void add() {
        if (maxNum == 0 || currentNum < maxNum) {
            currentNum++;
            tvNumber.setText(String.valueOf(getCurrentNum()));
            if (listener != null) {
                listener.onAdd(getCurrentNum());
            }
        }


    }

    private void minus() {

        if (currentNum > minNum) {
            currentNum--;
            if (currentNum < 0) {
                currentNum = 0;
            }
            tvNumber.setText(String.valueOf(getCurrentNum()));
            if (listener != null) {
                listener.onMinus(getCurrentNum());
            }

        }

    }

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
        tvNumber.setText(String.valueOf(currentNum));
    }

    public void setListener(NumberEditListener listener) {
        this.listener = listener;
    }

    public interface NumberEditListener {
        void onAdd(int currentNum);

        void onMinus(int currentNum);
    }
}