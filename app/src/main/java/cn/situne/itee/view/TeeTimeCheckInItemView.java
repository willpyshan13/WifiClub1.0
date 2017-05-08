package cn.situne.itee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.ParameterDoEditCustomersBooking;
import cn.situne.itee.fragment.BaseFragment;

public class TeeTimeCheckInItemView extends LinearLayout {

    public int position;
    private BaseFragment mFragment;
    private LayoutInflater mInflater;
    private ParameterDoEditCustomersBooking.Booking booking;
    private LinearLayout llContainer;
    private IteeTextView tvName;
    private IteeTextView tvDescribe;

    public TeeTimeCheckInItemView(BaseFragment mFragment, ParameterDoEditCustomersBooking.Booking booking, int mRightWidth, int position) {
        super(mFragment.getActivity());
        this.mFragment = mFragment;
        this.booking = booking;
        this.position = position;
        mInflater = (LayoutInflater) mFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
    }
    public TeeTimeCheckInItemView(Context mFragment, AttributeSet attrs) {
        super(mFragment, attrs);
    }

    public TeeTimeCheckInItemView(Context mFragment, AttributeSet attrs, int defStyleAttr) {
        super(mFragment, attrs, defStyleAttr);
    }

    public void setTvName(String tvNameString) {
        tvName.setText(tvNameString);
    }

    public void setTvDescribe(String tvDescribeString) {
        tvDescribe.setText(tvDescribeString);
    }

    private void initView() {

        LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.tee_time_check_in_item, this, false);
        addView(layout);
        tvName = new IteeTextView(mFragment.getActivity());
        tvDescribe = new IteeTextView(mFragment.getActivity());

        llContainer = (LinearLayout) layout.findViewById(R.id.ll_container);
        int height = Utils.getHeight(mFragment.getActivity());
        int width = Utils.getWidth(mFragment.getActivity());

        LinearLayout.LayoutParams layoutParamsLL = (LinearLayout.LayoutParams) llContainer.getLayoutParams();
        layoutParamsLL.height = (int) (height * 0.08f);
        layoutParamsLL.width = LayoutParams.WRAP_CONTENT;
        llContainer.setLayoutParams(layoutParamsLL);
        llContainer.addView(tvName);
        LinearLayout.LayoutParams layoutParamsName = (LinearLayout.LayoutParams) tvName.getLayoutParams();
        layoutParamsName.leftMargin = 10;
        layoutParamsName.height = 0;
        layoutParamsName.width = LayoutParams.WRAP_CONTENT;
        layoutParamsName.weight = 1;
        tvName.setLayoutParams(layoutParamsName);
        tvName.setTextColor(getResources().getColor(R.color.common_black));

        llContainer.addView(tvDescribe);
        LinearLayout.LayoutParams layoutParamsDescribe = (LinearLayout.LayoutParams) tvDescribe.getLayoutParams();
        layoutParamsDescribe.height = 0;
        layoutParamsDescribe.leftMargin = 10;
        layoutParamsDescribe.width = LayoutParams.WRAP_CONTENT;
        layoutParamsDescribe.weight = 1;
        tvDescribe.setLayoutParams(layoutParamsDescribe);
        tvDescribe.setTextSize(Constants.FONT_SIZE_SMALLER);
        tvDescribe.setTextColor(getResources().getColor(R.color.common_gray));

        initData();

    }

    private void initData() {
        tvName.setText(R.string.tee_time_agent);
        tvDescribe.setText("FanHongxiu");
    }

}
