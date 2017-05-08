/**
 * Project Name: itee
 * File Name:	 BaseFragment.java
 * Package Name: cn.situne.itee.fragment
 * Date:		 2015-01-10
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.analytics.MobclickAgent;

import cn.situne.itee.R;
import cn.situne.itee.activity.MainActivity;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:BaseFragment <br/>
 * Function: super class of fragment. <br/>
 * Date: 2015-01-10 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
@SuppressLint("InflateParams")
public abstract class BaseFragment extends Fragment {


    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    public static final int TITLE_NONE = -1;
    public static int LAYOUT_TRUE = RelativeLayout.TRUE;
    protected RequestQueue mQueue;
    protected Context mContext;
    private Bundle returnValues;
    private RelativeLayout rlContentContainer;
    private View rootView;
    private IteeTextView tvTitle;
    private IteeTextView tvRight;
    private IteeTextView tvLeftTitle;

    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    /**
     * 设置fragment的layoutId
     *
     * @return fragment的layoutId
     */
    protected abstract int getFragmentId();

    /**
     * 设置标题的id
     *
     * @return 标题的id
     */
    public abstract int getTitleResourceId();

    /**
     * 初始化layout中的控件
     */
    protected abstract void initControls(View rootView);

    /**
     * 设置控件的初始值
     */
    protected abstract void setDefaultValueOfControls();

    /**
     * 设置控件的Listener
     */
    protected abstract void setListenersOfControls();

    /**
     *
     */
    protected abstract void setLayoutOfControls();

    /**
     * 设置控件的属性
     */
    protected abstract void setPropertyOfControls();


    /**
     * 设置Actionbar样式
     */
    protected abstract void configActionBar();

    /**
     * 使用ResourceId设置ActionBar标题
     */
    private void setTitleWithResourceId() {
        int titleResId = getTitleResourceId();
        if (titleResId != TITLE_NONE) {
            getActivity().setTitle(getTitleResourceId());
        }
    }

    /**
     * 动态设置ActionBar标题
     */
    public void setTitle(String title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        } else {
            MainActivity activity = getBaseActivity();
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        }
    }

    protected void executeEveryOnCreate() {
        if (getBaseActivity() != null && getBaseActivity().getActionBar() != null) {
            getBaseActivity().getActionBar().show();
        }
        configActionBar();
    }

    protected void executeOnceOnCreate() {
    }


    public void push(Class<? extends BaseFragment> targetClazz) {
        push(targetClazz, null);
    }

    public void push(Class<? extends BaseFragment> targetClazz, Bundle bundle) {
        MainActivity activity = (MainActivity) getActivity();
        activity.pushFragment(targetClazz, bundle);
    }

    protected void setStackedActionBar() {
        final MainActivity container = getBaseActivity();
        container.setActionBarType(1);
        ActionBar actionBar = container.getSupportActionBar();
        if (actionBar != null) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            RelativeLayout rlActionBarMenu = (RelativeLayout) inflater.inflate(R.layout.action_bar, null);
            setBackArrowAndLeftTitle(rlActionBarMenu);
            setTvTitle(rlActionBarMenu);
            setTvRight(rlActionBarMenu);
            actionBar.setCustomView(rlActionBarMenu);
        }
    }

    protected void setShopActionBar() {
        final MainActivity container = getBaseActivity();
        container.setActionBarType(0);
        ActionBar actionBar = container.getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        RelativeLayout rlActionBarMenu = (RelativeLayout) inflater.inflate(R.layout.action_bar, null);
//        ImageView ivPackage = new ImageView(getActivity());
//        ivPackage.setImageResource(R.drawable.icon_shops_package);
//        ivPackage.setOnClickListener(listenerChoosePackage);
//
//        RelativeLayout.LayoutParams ivPackageLayoutParams = new RelativeLayout.LayoutParams(getScreenWidth(50), getScreenWidth(50));
//        ivPackageLayoutParams.addRule(RelativeLayout.LEFT_OF, getTvRight().getId());
//        ivPackageLayoutParams.rightMargin = getScreenWidth(20);
//        ivPackage.setLayoutParams(ivPackageLayoutParams);
//        parent.addView(ivPackage);
        actionBar.setCustomView(rlActionBarMenu);
    }


    protected void setMemberActionBar() {
        final MainActivity container = getBaseActivity();

        container.setActionBarType(0);

        ActionBar actionBar = container.getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        RelativeLayout rlActionBarMenu = (RelativeLayout) inflater.inflate(R.layout.action_bar, null);

        setBackArrow(rlActionBarMenu);

        setTvTitle(rlActionBarMenu);


        ImageView arrowDown = new ImageView(container);
        arrowDown.setBackgroundResource(R.drawable.icon_arrow_down);
        arrowDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        rlActionBarMenu.addView(arrowDown);

        RelativeLayout.LayoutParams paramsShopImg = (RelativeLayout.LayoutParams) arrowDown.getLayoutParams();
        paramsShopImg.width = 30;
        paramsShopImg.height = 30;
        paramsShopImg.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsShopImg.addRule(RelativeLayout.RIGHT_OF, tvTitle.getId());
        arrowDown.setLayoutParams(paramsShopImg);

        setTvRight(rlActionBarMenu);

        actionBar.setCustomView(rlActionBarMenu);
    }


    protected void setTeeTimeActionBar() {
        final MainActivity container = getBaseActivity();
        container.setActionBarType(0);

        ActionBar actionBar = container.getSupportActionBar();
        actionBar.setCustomView(null);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        RelativeLayout rlActionBarMenu = (RelativeLayout) inflater.inflate(R.layout.action_bar, null);
        setMenuIcon(rlActionBarMenu);
        setTvTitle(rlActionBarMenu);
        setTvRight(rlActionBarMenu);
//        tvTitle.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
//        tvTitle.getPaint().setAntiAlias(true);//抗锯齿

        ImageView ivUnderLine = new ImageView(container);
        ivUnderLine.setBackgroundColor(getColor(R.color.common_white));
        rlActionBarMenu.addView(ivUnderLine);

        RelativeLayout.LayoutParams paramsIvUnderLine = (RelativeLayout.LayoutParams) ivUnderLine.getLayoutParams();
        paramsIvUnderLine.width = (int) (getScreenWidth() * 0.3);
        paramsIvUnderLine.height = 2;
        paramsIvUnderLine.topMargin = 10;
        paramsIvUnderLine.addRule(RelativeLayout.BELOW, tvTitle.getId());
        paramsIvUnderLine.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        ivUnderLine.setLayoutParams(paramsIvUnderLine);

        actionBar.setCustomView(rlActionBarMenu);
    }

    protected void setChartActionBar() {
        final MainActivity container = getBaseActivity();
        container.setActionBarType(0);

        ActionBar actionBar = container.getSupportActionBar();
        actionBar.setCustomView(null);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        RelativeLayout rlActionBarMenu = (RelativeLayout) inflater.inflate(R.layout.action_bar, null);
        setMenuIcon(rlActionBarMenu);
        setTvTitle(rlActionBarMenu);
        setTvRight(rlActionBarMenu);

        ImageView ivUnderLine = new ImageView(container);
        ivUnderLine.setBackgroundColor(getColor(R.color.common_white));
        rlActionBarMenu.addView(ivUnderLine);

        RelativeLayout.LayoutParams paramsIvUnderLine = (RelativeLayout.LayoutParams) ivUnderLine.getLayoutParams();
        paramsIvUnderLine.width = (int) (getScreenWidth() * 0.3);
        paramsIvUnderLine.height = 2;
        paramsIvUnderLine.topMargin = 10;
        paramsIvUnderLine.addRule(RelativeLayout.BELOW, tvTitle.getId());
        paramsIvUnderLine.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        ivUnderLine.setLayoutParams(paramsIvUnderLine);

        actionBar.setCustomView(rlActionBarMenu);
    }

    protected void setCalendarActionBar() {
        final MainActivity container = getBaseActivity();
        container.setActionBarType(0);

        ActionBar actionBar = container.getSupportActionBar();
        actionBar.setCustomView(null);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        RelativeLayout rlActionBarMenu = (RelativeLayout) inflater.inflate(R.layout.action_bar, null);
        setBackArrowAndLeftTitle(rlActionBarMenu);
        setTvTitle(rlActionBarMenu);
        setTvRight(rlActionBarMenu);

        ImageView ivUnderLine = new ImageView(container);
        ivUnderLine.setBackgroundColor(getColor(R.color.common_white));
        rlActionBarMenu.addView(ivUnderLine);

        RelativeLayout.LayoutParams paramsIvUnderLine = (RelativeLayout.LayoutParams) ivUnderLine.getLayoutParams();
        paramsIvUnderLine.width = (int) (getScreenWidth() * 0.15);
        paramsIvUnderLine.height = 2;
        paramsIvUnderLine.topMargin = 10;
        paramsIvUnderLine.addRule(RelativeLayout.BELOW, tvTitle.getId());
        paramsIvUnderLine.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        ivUnderLine.setLayoutParams(paramsIvUnderLine);

        actionBar.setCustomView(rlActionBarMenu);
    }

    protected void setCalendarOnlyYearActionBar() {
        final MainActivity container = getBaseActivity();
        container.setActionBarType(0);

        ActionBar actionBar = container.getSupportActionBar();
        actionBar.setCustomView(null);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        RelativeLayout rlActionBarMenu = (RelativeLayout) inflater.inflate(R.layout.action_bar, null);
        setBackArrowAndLeftTitle(rlActionBarMenu);
        setTvTitle(rlActionBarMenu);
        setTvRight(rlActionBarMenu);

        ImageView ivUnderLine = new ImageView(container);
        ivUnderLine.setBackgroundColor(getColor(R.color.common_white));
        rlActionBarMenu.addView(ivUnderLine);

        RelativeLayout.LayoutParams paramsIvUnderLine = (RelativeLayout.LayoutParams) ivUnderLine.getLayoutParams();
        paramsIvUnderLine.width = (int) (getScreenWidth() * 0.15);
        paramsIvUnderLine.height = 2;
        paramsIvUnderLine.topMargin = 10;
        paramsIvUnderLine.addRule(RelativeLayout.BELOW, tvTitle.getId());
        paramsIvUnderLine.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        ivUnderLine.setLayoutParams(paramsIvUnderLine);

        actionBar.setCustomView(rlActionBarMenu);
    }

    protected void setNormalMenuActionBar() {
        final MainActivity container = getBaseActivity();
        container.setActionBarType(0);
        ActionBar actionBar = container.getSupportActionBar();
        actionBar.setCustomView(null);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        RelativeLayout rlActionBarMenu = (RelativeLayout) inflater.inflate(R.layout.action_bar, null);

        int btnIconId = setMenuIcon(rlActionBarMenu);
        ImageView ivSeparator = new ImageView(container);
        ivSeparator.setId(View.generateViewId());
        ivSeparator.setImageResource(R.drawable.icon_separator);
        ivSeparator.setVisibility(View.INVISIBLE);
        rlActionBarMenu.addView(ivSeparator);

        RelativeLayout.LayoutParams paramsIvSeparator = (RelativeLayout.LayoutParams) ivSeparator.getLayoutParams();
        paramsIvSeparator.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvSeparator.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvSeparator.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsIvSeparator.addRule(RelativeLayout.RIGHT_OF, btnIconId);
        paramsIvSeparator.leftMargin = 10;
        ivSeparator.setLayoutParams(paramsIvSeparator);

        tvTitle = new IteeTextView(getActivity());
        tvTitle.setId(View.generateViewId());
        rlActionBarMenu.addView(tvTitle);

        RelativeLayout.LayoutParams paramsTvTitle = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
        paramsTvTitle.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTitle.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTitle.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvTitle.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        paramsTvTitle.leftMargin = 10;
        tvTitle.setLayoutParams(paramsTvTitle);

        tvTitle.setTextSize(Constants.FONT_SIZE_LARGER);
        tvTitle.setTextColor(getColor(R.color.common_white));
        setTvRight(rlActionBarMenu);
        setLeftTitle(rlActionBarMenu, ivSeparator);
        tvTitle.bringToFront();
        actionBar.setCustomView(null);
        actionBar.setCustomView(rlActionBarMenu);
    }

    private void setLeftTitle(RelativeLayout rlActionBarMenu, ImageView ivSeparator) {
        tvLeftTitle = new IteeTextView(getActivity());
        rlActionBarMenu.addView(tvLeftTitle);

        RelativeLayout.LayoutParams paramsTvLeftTitle = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
        paramsTvLeftTitle.width = (int) (getScreenWidth() * 0.6);
        paramsTvLeftTitle.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsTvLeftTitle.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvLeftTitle.addRule(RelativeLayout.RIGHT_OF, ivSeparator.getId());
        tvLeftTitle.setLayoutParams(paramsTvLeftTitle);

        tvLeftTitle.setGravity(Gravity.CENTER_VERTICAL);
        tvLeftTitle.setId(View.generateViewId());
        tvLeftTitle.setTextSize(Constants.FONT_SIZE_LARGER);
        tvLeftTitle.setTextColor(getColor(R.color.common_white));
    }

    private int setMenuIcon(RelativeLayout rlActionBarMenu) {
        IteeButton btnIcon = new IteeButton(getBaseActivity());
        RelativeLayout.LayoutParams paramsBtnIcon = new RelativeLayout.LayoutParams(getActionBarHeight(), getActionBarHeight());
       // paramsBtnIcon.leftMargin = getActualWidthOnThisDevice(20);
        paramsBtnIcon.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsBtnIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        btnIcon.setLayoutParams(paramsBtnIcon);

        btnIcon.setId(View.generateViewId());
        btnIcon.setBackground(getDrawable(R.drawable.icon_sliding_menu_bg));
        btnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBaseActivity() != null) {
                    SlidingMenu menu = getBaseActivity().getMenu();
                    if (menu.isMenuShowing()) {
                        getBaseActivity().showContent();
                    } else {
                        getBaseActivity().showMenu();
                    }
                }
            }
        });
        rlActionBarMenu.addView(btnIcon);
        return btnIcon.getId();
    }

    private int setBackArrow(RelativeLayout rlActionBarMenu) {
        RelativeLayout rlIconContainer = new RelativeLayout(getActivity());
        rlIconContainer.setId(View.generateViewId());
        RelativeLayout.LayoutParams rlIconContainerLayoutParams = new RelativeLayout.LayoutParams(getActionBarHeight(), MATCH_PARENT);
      //  rlIconContainerLayoutParams.leftMargin = getActualWidthOnThisDevice(20);
        rlIconContainer.setLayoutParams(rlIconContainerLayoutParams);
        rlActionBarMenu.addView(rlIconContainer);

        ImageView ivIcon = new ImageView(getBaseActivity());
        ivIcon.setId(View.generateViewId());
        ivIcon.setBackgroundResource(R.drawable.icon_back);
       // ivIcon.setScaleType(ImageView.ScaleType.FIT_XY);
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().doBack();
                //Utils.showShortToast(getBaseActivity(),"ttt");
            }
        });

        RelativeLayout.LayoutParams paramsIvIcon = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        paramsIvIcon.width = getActionBarHeight();
        paramsIvIcon.height = getActionBarHeight();
        //paramsIvIcon.leftMargin = getActualWidthOnThisDevice(15);
        paramsIvIcon.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        ivIcon.setLayoutParams(paramsIvIcon);
        rlIconContainer.addView(ivIcon);

        ImageView ivSeparator = new ImageView(getBaseActivity());
        ivSeparator.setId(View.generateViewId());
        ivSeparator.setImageResource(R.drawable.icon_separator);
        ivSeparator.setVisibility(View.INVISIBLE);
        rlIconContainer.addView(ivSeparator);

        RelativeLayout.LayoutParams paramsIvSeparator = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        paramsIvSeparator.width = 5;
        paramsIvSeparator.height = 50;
        paramsIvSeparator.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsIvSeparator.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvSeparator.rightMargin = 5;
        ivSeparator.setLayoutParams(paramsIvSeparator);

        IteeButton btn = new IteeButton(getActivity());
        RelativeLayout.LayoutParams btnLayoutParams = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        btn.setLayoutParams(btnLayoutParams);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //doBack();
            }
        });
       // btn.setBackgroundResource(R.drawable.icon_back);
     //   rlIconContainer.addView(btn);

        return rlIconContainer.getId();
    }

    private void setBackArrowAndLeftTitle(RelativeLayout rlActionBarMenu) {
        int leftLayoutId = setBackArrow(rlActionBarMenu);

        tvLeftTitle = new IteeTextView(getActivity());
        rlActionBarMenu.addView(tvLeftTitle);

        RelativeLayout.LayoutParams paramsTvLeftTitle = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        paramsTvLeftTitle.width = (int) (getScreenWidth() * 0.5);
        paramsTvLeftTitle.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsTvLeftTitle.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvLeftTitle.addRule(RelativeLayout.RIGHT_OF, leftLayoutId);
        paramsTvLeftTitle.leftMargin = 10;
        tvLeftTitle.setLayoutParams(paramsTvLeftTitle);

        tvLeftTitle.setTextSize(Constants.FONT_SIZE_LARGER);
        tvLeftTitle.setSingleLine();
        tvLeftTitle.setEllipsize(TextUtils.TruncateAt.END);
        tvLeftTitle.setTextColor(getColor(R.color.common_white));
        tvLeftTitle.setGravity(Gravity.CENTER_VERTICAL);
    }

    public int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public void onBeforeApi() {

    }

    public void onAfterApi() {
    }

    public int getStatusBarHeight() {
        return dp2px(25);
    }

    protected void doBackWithRefresh() {
        getBaseActivity().doBackWithRefresh();
    }

    protected void doBackWithRefresh(Class<? extends BaseFragment> clazz) {
        getBaseActivity().doBackWithRefresh(clazz);
    }

    protected void doBack() {
        getBaseActivity().doBack();
    }

    protected void doBackWithReturnValue(Bundle bundle) {
        getBaseActivity().doBackWithReturnValue(bundle);
    }

    protected void doBackWithReturnValue(Bundle bundle, Class<? extends BaseFragment> clazz) {
        getBaseActivity().doBackWithReturnValue(bundle, clazz);
    }

    protected void doBackWithSegmentReturnValue(Bundle bundle, Class<? extends BaseFragment> clazz) {
        getBaseActivity().doBackWithSegmentReturnValue(bundle, clazz);
    }

    public Drawable getDrawable(int resId) {
        return getActivity().getResources().getDrawable(resId);
    }

    /**
     * 获取MainActivity
     *
     * @return MainActivity
     */
    public MainActivity getBaseActivity() {
        return (MainActivity) getActivity();
    }

    /**
     * 从Color.xml获取颜色
     */
    public int getColor(int colorId) {
        return this.getResources().getColor(colorId);
    }

    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度
     */
    public int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return 屏幕高度
     */
    public int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(float dpValue) {
        return DensityUtil.dip2px(getActivity(), dpValue);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dp(float pxValue) {
        return DensityUtil.px2dip(getActivity(), pxValue);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        boolean isFirstRun = rootView == null;
        if (isFirstRun) {
            final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), com.viewpagerindicator.R.style.Theme_PageIndicatorDefaults);
            // clone the inflater using the ContextThemeWrapper
            LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
            rootView = localInflater.inflate(getFragmentId(), container, false);
            setRootView(rootView);

            if (mQueue == null) {
                mQueue = Volley.newRequestQueue(getActivity());
            }

            rlContentContainer = (RelativeLayout) rootView.findViewById(R.id.rl_content_container);
            initControls(rootView);
            setPropertyOfControls();
            setDefaultValueOfControls();
            setLayoutOfControls();
            setListenersOfControls();
            configActionBar();
            setTitleWithResourceId();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        }

        if (isFirstRun) {
            executeOnceOnCreate();
        }
        executeEveryOnCreate();
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getReturnValues() != null) {
            reShowWithBackValue();
            setReturnValues(null);
        }
        MobclickAgent.onPageStart(getClass().getName()); //统计页面
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getName());
        MobclickAgent.onPause(getActivity());
    }

    protected void reShowWithBackValue() {

    }

    public IteeTextView getTvRight() {
        return tvRight;
    }

    private void setTvRight(RelativeLayout rlActionBarMenu) {
        tvRight = new IteeTextView(getActivity());
        rlActionBarMenu.addView(tvRight);
        rlActionBarMenu.setPadding(0, 0, 0, 0);

        RelativeLayout.LayoutParams paramsTvOk = (RelativeLayout.LayoutParams) tvRight.getLayoutParams();
        paramsTvOk.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvOk.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvOk.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvOk.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        tvRight.setLayoutParams(paramsTvOk);
        tvRight.setPadding(0, 0, getActualWidthOnThisDevice(20), 0);
        tvRight.setId(View.generateViewId());
        tvRight.setTextSize(Constants.FONT_SIZE_LARGER);
        tvRight.setTextColor(getResources().getColor(R.color.common_white));
        tvRight.setGravity(Gravity.CENTER_VERTICAL);
    }

    public IteeTextView getTvTitle() {
        return tvTitle;
    }

    private void setTvTitle(RelativeLayout rlActionBarMenu) {
        tvTitle = new IteeTextView(getActivity());
        tvTitle.setId(View.generateViewId());
        rlActionBarMenu.addView(tvTitle);

        RelativeLayout.LayoutParams paramsTvTitle = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
        paramsTvTitle.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTitle.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTitle.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvTitle.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        tvTitle.setLayoutParams(paramsTvTitle);
        tvTitle.setId(View.generateViewId());
        tvTitle.setTextSize(Constants.FONT_SIZE_LARGER);
        tvTitle.setTextColor(getColor(R.color.common_white));
    }

    public Bundle getReturnValues() {
        return returnValues;
    }

    public void setReturnValues(Bundle returnValues) {
        this.returnValues = returnValues;
    }

    public IteeTextView getTvLeftTitle() {
        return tvLeftTitle;
    }

    public void setOnBackListener(BaseFragment.OnPopBackListener onBackListener) {
        getBaseActivity().setOnBackListener(onBackListener);
    }

    public int getActualWidthOnThisDevice(float currentPx) {
        return DensityUtil.getActualWidthOnThisDevice(currentPx, getActivity());
    }

    public int getActualHeightOnThisDevice(float currentPx) {
        return DensityUtil.getActualHeightOnThisDevice(currentPx, getActivity());
    }

    public RelativeLayout getRlContentContainer() {
        return rlContentContainer;
    }

    public interface OnPopBackListener {
        boolean preDoBack();
    }
}
