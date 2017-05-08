/**
 * Project Name: itee
 * File Name:	 IteeNetworkImageView.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-04-24
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * ClassName:IteeNetworkImageView <br/>
 * Function: . <br/>
 * Date: 2015-04-24 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class IteeNetworkImageView extends NetworkImageView {

    private Bitmap mLocalBitmap;

    private boolean mShowLocal;

    public void setLocalImageBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            mShowLocal = true;
        }
        this.mLocalBitmap = bitmap;
        requestLayout();
    }

    @Override
    public void setImageUrl(String url, ImageLoader imageLoader) {
        mShowLocal = false;
        super.setImageUrl(url, imageLoader);
    }

    public IteeNetworkImageView(Context context) {
        this(context, null);
    }

    public IteeNetworkImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IteeNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);
        if (mShowLocal) {
            setImageBitmap(mLocalBitmap);
        }
    }

}