/**
 * Project Name: itee
 * File Name:	 ShopsTripleProductLine.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-04-08
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.view;

import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.situne.itee.fragment.BaseFragment;

/**
 * ClassName:ShopsTripleProductLine <br/>
 * Function: a line with three product. <br/>
 * Date: 2015-04-08 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShopsTripleProductLine extends LinearLayout {

    public static final int MAX_NUM = 3;

    private BaseFragment mBaseFragment;
    private int currentIndex;

    private ShopsProductWithPicture[] products = new ShopsProductWithPicture[MAX_NUM];
    private RelativeLayout[] rlContainers = new RelativeLayout[MAX_NUM];

    public ShopsTripleProductLine(BaseFragment mBaseFragment) {
        super(mBaseFragment.getActivity());
        this.mBaseFragment = mBaseFragment;

        init();
    }

    private void init() {

        setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams containerLayoutParams = new LayoutParams(BaseFragment.MATCH_PARENT, mBaseFragment.getActualHeightOnThisDevice(265));
        containerLayoutParams.topMargin = mBaseFragment.getActualHeightOnThisDevice(25);
        setLayoutParams(containerLayoutParams);

        setOrientation(HORIZONTAL);
        currentIndex = 0;
        for (int i = 0; i < products.length; i++) {
            RelativeLayout rlContainer = new RelativeLayout(mBaseFragment.getActivity());
            rlContainer.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, BaseFragment.MATCH_PARENT);
            layoutParams.weight = 1;
            layoutParams.topMargin = mBaseFragment.getActualHeightOnThisDevice(5);
            layoutParams.bottomMargin = mBaseFragment.getActualHeightOnThisDevice(5);
            rlContainer.setLayoutParams(layoutParams);
            addView(rlContainer);
            rlContainers[i] = rlContainer;
        }
    }

    public void addProduct(ShopsProductWithPicture productView) {
        if (currentIndex < products.length) {
            products[currentIndex] = productView;
            RelativeLayout rlContainer = rlContainers[currentIndex];
            rlContainer.addView(productView);
            currentIndex++;
        }
    }

    public ShopsProductWithPicture getLeftProduct() {
        return products[0];
    }

    public ShopsProductWithPicture getMiddleProduct() {
        return products[1];
    }

    public ShopsProductWithPicture getRightProduct() {
        return products[2];
    }
}  