package cn.situne.itee.common.widget.wheel;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.fragment.BaseFragment;


public abstract class BasePopFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_CANCELABLE_ON_TOUCH_OUTSIDE = "cancelable_onTouchOutside";

    private static final int TRANSLATE_DURATION = 200;
    private static final int ALPHA_DURATION = 300;

    protected BaseFragment baseFragment;
    private OnDismissedListener dismissedListener;
    private boolean mDismissed = true;
    private View mView;
    private LinearLayout mPanel;
    private ViewGroup mGroup;
    private View mBg;

    public void show(FragmentManager manager, String tag) {
        if (!mDismissed) {
            return;
        }
        mDismissed = false;
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void dismiss() {
        if (mDismissed) {
            return;
        }
        mDismissed = true;
        getFragmentManager().popBackStackImmediate();
    }

    public abstract void createContent(LinearLayout mParent);

    public boolean isNeedAnimation() {
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            View focusView = getActivity().getCurrentFocus();
            if (focusView != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }

        mView = createView();
        mGroup = (ViewGroup) getActivity().getWindow().getDecorView();

        createContent(mPanel);

        mGroup.addView(mView);

        if (isNeedAnimation()) {
            mBg.startAnimation(createAlphaInAnimation());
            mPanel.startAnimation(createTranslationInAnimation());
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private View createView() {
        FrameLayout parent = new FrameLayout(getActivity());
        parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mBg = new View(getActivity());

        FrameLayout.LayoutParams bgLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if (isTop()) {
            bgLayoutParams.topMargin = baseFragment.getActionBarHeight() + baseFragment.getStatusBarHeight();
        }

        mBg.setLayoutParams(bgLayoutParams);
        mBg.setBackgroundColor(Color.argb(136, 0, 0, 0));
        mBg.setId(View.generateViewId());
        mBg.setOnClickListener(this);

        mPanel = new LinearLayout(getActivity());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (isTop()) {
            params.gravity = Gravity.TOP;
            params.topMargin = baseFragment.getActionBarHeight() + baseFragment.getStatusBarHeight();
        } else {
            params.gravity = Gravity.BOTTOM;
        }
        mPanel.setLayoutParams(params);
        mPanel.setOrientation(LinearLayout.VERTICAL);

        mPanel.setBackgroundColor(Color.WHITE);

        parent.addView(mBg);
        parent.addView(mPanel);
        return parent;
    }

    private Animation createTranslationInAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
                1, type, 0);
        an.setDuration(TRANSLATE_DURATION);
        return an;
    }

    private Animation createAlphaInAnimation() {
        AlphaAnimation an = new AlphaAnimation(0, 1);
        an.setDuration(ALPHA_DURATION);
        return an;
    }

    private Animation createTranslationOutAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
                0, type, 1);
        an.setDuration(TRANSLATE_DURATION);
        an.setFillAfter(true);
        return an;
    }

    private Animation createAlphaOutAnimation() {
        AlphaAnimation an = new AlphaAnimation(1, 0);
        an.setDuration(ALPHA_DURATION);
        an.setFillAfter(true);
        return an;
    }

    private boolean getCancelableOnTouchOutside() {
        return getArguments().getBoolean(ARG_CANCELABLE_ON_TOUCH_OUTSIDE);
    }

    protected boolean isTop() {
        return false;
    }

    public LinearLayout getPanel() {
        return mPanel;
    }

    @Override
    public void onDestroyView() {
        mPanel.startAnimation(createTranslationOutAnimation());
        mBg.startAnimation(createAlphaOutAnimation());
        mView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mGroup.removeView(mView);
            }
        }, ALPHA_DURATION);
        if (dismissedListener != null) {
            dismissedListener.onDismissed();
        }
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mBg.getId() && !getCancelableOnTouchOutside()) {
            return;
        }
        dismiss();
    }

    public void setDismissedListener(OnDismissedListener dismissedListener) {
        this.dismissedListener = dismissedListener;
    }

    public void setBaseFragment(BaseFragment baseFragment) {
        this.baseFragment = baseFragment;
    }

    public static Builder<? extends BasePopFragment> createBuilder(BaseFragment mBaseFragment,
                                                                   FragmentManager fragmentManager) {
        return new Builder<>(mBaseFragment, fragmentManager);
    }

    public static class Builder<T extends BasePopFragment> {

        protected BaseFragment mBaseFragment;
        protected FragmentManager mFragmentManager;
        protected String mTag = "basePopFragment";
        protected boolean mCancelableOnTouchOutside;
        protected OnDismissedListener mListener;

        public Builder(BaseFragment mBaseFragment, FragmentManager fragmentManager) {
            this.mBaseFragment = mBaseFragment;
            mFragmentManager = fragmentManager;
        }

        public Builder<T> setTag(String tag) {
            mTag = tag;
            return this;
        }

        public Builder<T> setListener(OnDismissedListener listener) {
            this.mListener = listener;
            return this;
        }

        public Builder<T> setCancelableOnTouchOutside(boolean cancelable) {
            mCancelableOnTouchOutside = cancelable;
            return this;
        }

        public Bundle prepareArguments() {
            Bundle bundle = new Bundle();
            bundle.putBoolean(ARG_CANCELABLE_ON_TOUCH_OUTSIDE, mCancelableOnTouchOutside);
            return bundle;
        }

        public BasePopFragment show() {
            BasePopFragment fragment = (BasePopFragment) Fragment.instantiate(
                    mBaseFragment.getActivity(), BasePopFragment.class.getName(), prepareArguments());
            fragment.setDismissedListener(mListener);
            fragment.show(mFragmentManager, mTag);
            fragment.setBaseFragment(mBaseFragment);
            return fragment;
        }

    }

    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度
     */
    public int getWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return 屏幕高度
     */
    public int getHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    public int getWidth(float currentPx) {
        return (int) (currentPx / Constants.DESIGN_UI_WIDTH * getWidth());
    }

    public int getHeight(float currentPx) {
        return (int) (currentPx / Constants.DESIGN_UI_HEIGHT * getHeight());
    }

    public interface OnDismissedListener {
        void onDismissed();
    }

}
