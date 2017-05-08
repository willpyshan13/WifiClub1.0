/**
 * Project Name: itee
 * File Name:	 CustomersTypeItemDataFragment.java
 * Package Name: cn.situne.itee.fragment.customers
 * Date:		 2015-03-24
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.customers;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonCommonProduct;
import cn.situne.itee.manager.jsonentity.JsonGreenFeeList;
import cn.situne.itee.manager.jsonentity.JsonMemberEditList;
import cn.situne.itee.view.CheckSwitchButton;
import cn.situne.itee.view.IteeDayEditText;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeGuestEditText;
import cn.situne.itee.view.IteeIntegerEditText;
import cn.situne.itee.view.IteeMoneyEditText;
import cn.situne.itee.view.IteePeriodEditText;
import cn.situne.itee.view.IteeRadioButton;
import cn.situne.itee.view.IteeRedDeleteButton;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.PricingTableProductItem;
import cn.situne.itee.view.SelectedWeekDay;
import cn.situne.itee.view.popwindow.SelectDatePopupWindow;

/**
 * ClassName:CustomersTypeItemDataFragment <br/>
 * Function: Edit or add member type. <br/>
 * Date: 2015-03-24 <br/>
 * UI:11-2
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class CustomersTypeItemDataFragment extends BaseEditFragment {

    private LinearLayout llContainer;

    private RelativeLayout rlTypeNameContainer;
    private RelativeLayout rlPeriodContainer;
    private RelativeLayout rlEndDate;
    private RelativeLayout rlAnnualFeeContainer;
    private RelativeLayout rlAnnualFeeValueContainer;
//    private RelativeLayout rlGuestNumContainer;
    private RelativeLayout rlSignWhenCheckInContainer;


    private RelativeLayout rlSignForGuestContainer;


    private RelativeLayout rlOverDraftContainer;
    private RelativeLayout rlTimeLimitContainer;
    private RelativeLayout rlAgeRangeContainer;
//    private RelativeLayout rlWeekTitleContainer;
//    private RelativeLayout rlWeekSelectContainer;
   // private LinearLayout llFeeListPriceContainer;
    private RelativeLayout rlPricingTableContainer;
    private RelativeLayout rlDeleteContainer;

    private IteeTextView tvTypeName;
    private IteeTextView tvPeriodOfValidity;
    private IteeTextView tvEndDateName;
    private IteeTextView tvEndDateValue;
    private IteeTextView tvAnnualFee;
//    private IteeTextView tvGuestNum;
    private IteeTextView tvSignWhenCheckIn;
    private IteeTextView tvOverdraft;
    private IteeTextView tvTimeLimit;
    private IteeTextView tvAge;
//    private IteeTextView tvWeek;
    private IteeTextView tvPricingTable;
    private IteeRedDeleteButton tvDelete;
    private IteeEditText etTypeNameValue;
    private IteePeriodEditText etPeriodOfValidityValue;
    private IteeMoneyEditText etAnnualFeeValue;
//    private IteeGuestEditText etGuestNumValue;
    private IteeMoneyEditText etOverdraftValue;
    private IteeDayEditText etTimeLimitValue;
    private IteeIntegerEditText etAgeStartValue;
    private IteeIntegerEditText etAgeEndValue;
    private ImageView ivAgeRangeConnector;
    private ImageView ivRightArrow;
    private CheckSwitchButton swAnnualFee;
    private CheckSwitchButton swSignWhenCheckIn;

    private CheckSwitchButton swSignForGuest;

    //private SelectedWeekDay selectedWeekDay;
    private int memberTypeId;
    private String memberTypeTypeId;

    private ArrayList<JsonCommonProduct> greenFeePriceItemList;
    private ArrayList<JsonGreenFeeList.GreenFee> addStateGreenFeeList;

    private AppUtils.NoDoubleClickListener listenerOk;

    private IteeRadioButton radioPeriod;
    private IteeRadioButton radioEndDate;

    private SelectDatePopupWindow selectDatePopupWindow;
    private String currentEndDate;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_customer_type_item_edit_or_add;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        llContainer = (LinearLayout) rootView.findViewById(R.id.ll_container);

        Context mContext = getActivity();

        rlTypeNameContainer = new RelativeLayout(mContext);
        rlPeriodContainer = new RelativeLayout(mContext);
        rlEndDate = new RelativeLayout(mContext);
        rlAnnualFeeContainer = new RelativeLayout(mContext);
        rlAnnualFeeValueContainer = new RelativeLayout(mContext);
       // rlGuestNumContainer = new RelativeLayout(mContext);
        rlSignWhenCheckInContainer = new RelativeLayout(mContext);


        rlSignForGuestContainer = new RelativeLayout(mContext);
        rlOverDraftContainer = new RelativeLayout(mContext);
        rlTimeLimitContainer = new RelativeLayout(mContext);
        rlAgeRangeContainer = new RelativeLayout(mContext);
//        rlWeekTitleContainer = new RelativeLayout(mContext);
//        rlWeekSelectContainer = new RelativeLayout(mContext);
       // llFeeListPriceContainer = new LinearLayout(mContext);
        rlPricingTableContainer = new RelativeLayout(mContext);
        rlDeleteContainer = new RelativeLayout(mContext);

        tvTypeName = new IteeTextView(this);
        tvPeriodOfValidity = new IteeTextView(this);
        tvEndDateName = new IteeTextView(this);
        tvEndDateValue = new IteeTextView(this);
        tvAnnualFee = new IteeTextView(this);
       // tvGuestNum = new IteeTextView(this);
        tvSignWhenCheckIn = new IteeTextView(this);
        tvOverdraft = new IteeTextView(this);
        tvTimeLimit = new IteeTextView(this);
        tvAge = new IteeTextView(this);
//        tvWeek = new IteeTextView(this);
        tvPricingTable = new IteeTextView(this);
        tvDelete = new IteeRedDeleteButton(getBaseActivity());

        etTypeNameValue = new IteeEditText(mContext);
        etPeriodOfValidityValue = new IteePeriodEditText(this);
        etAnnualFeeValue = new IteeMoneyEditText(this);
//        etGuestNumValue = new IteeGuestEditText(this);
        etOverdraftValue = new IteeMoneyEditText(this);
        etTimeLimitValue = new IteeDayEditText(this);
        etAgeStartValue = new IteeIntegerEditText(this);
        etAgeEndValue = new IteeIntegerEditText(this);

        ivAgeRangeConnector = new ImageView(mContext);
        ivRightArrow = new ImageView(mContext);

        swAnnualFee = new CheckSwitchButton(mContext);
        swAnnualFee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                boolean isCanEdit = getFragmentMode() != FragmentMode.FragmentModeBrowse;
                if (isCanEdit) {
                    etAnnualFeeValue.setEnabled(b);
                }

            }
        });
        swSignWhenCheckIn = new CheckSwitchButton(mContext);

        swSignForGuest= new CheckSwitchButton(mContext);

//        selectedWeekDay = new SelectedWeekDay(getActivity());
//        selectedWeekDay.setVisibility(View.GONE);

        greenFeePriceItemList = new ArrayList<>();

        radioPeriod = new IteeRadioButton(getActivity());
        radioEndDate = new IteeRadioButton(getActivity());

        radioPeriod.setId(View.generateViewId());
        radioEndDate.setId(View.generateViewId());


    }

    private void doSelectPeriod() {
        tvEndDateValue.setEnabled(false);
        etPeriodOfValidityValue.setEnabled(true);
        rlPeriodContainer.setBackgroundColor(getColor(R.color.common_white));
        rlEndDate.setBackgroundColor(getColor(R.color.common_light_gray));
    }

    @Override
    protected void setDefaultValueOfControls() {
        llContainer.setBackgroundColor(getColor(R.color.common_light_gray));
    }

    @Override
    protected void setListenersOfControls() {

        rlPricingTableContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.CUSTOMERS_MEMBER_TYPE_ID, memberTypeId);
                    bundle.putString(TransKey.CUSTOMERS_MEMBER_TYPE_NAME, etTypeNameValue.getValue());
                    bundle.putString(TransKey.CUSTOMERS_MEMBER_TYPE_TYPE_ID, memberTypeTypeId);
                    push(CustomersPricingListFragment.class, bundle);
                }
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    AppUtils.showDeleteAlert(CustomersTypeItemDataFragment.this, new AppUtils.DeleteConfirmListener() {
                        @Override
                        public void onClickDelete() {
                            deleteMember();
                        }
                    });
                }
            }
        });

        listenerOk = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
                    setFragmentMode(FragmentMode.FragmentModeEdit);
                    getTvRight().setText(R.string.common_save);
                    getTvRight().setBackground(null);
                    configState();
                    tvDelete.setVisibility(View.VISIBLE);
                    rlDeleteContainer.setVisibility(View.VISIBLE);
                } else if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
                    if (doCheck()) {
                        postMember();
                    }
                } else {
                    if (doCheck()) {
                        putMember();
                    }
                }
            }
        };

        radioPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioPeriod.isChecked()) {
                    radioEndDate.doUnchecked();
                    doSelectPeriod();
                } else {
                    radioEndDate.doChecked();
                    doSelectEndDate();
                }
            }
        });

        radioEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioEndDate.isChecked()) {
                    radioPeriod.doUnchecked();
                    doSelectEndDate();
                } else {
                    radioPeriod.doChecked();
                    doSelectPeriod();
                }
            }
        });

        tvEndDateValue.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {

            @Override
            public void noDoubleClick(View v) {
                SelectDatePopupWindow.OnDateSelectClickListener dateSelectReturn = new SelectDatePopupWindow.OnDateSelectClickListener() {
                    @Override
                    public void OnGoodItemClick(String flag, String content) {
                        switch (flag) {
                            case Constants.DATE_RETURN:
                                currentEndDate = content;
                                tvEndDateValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(content, getActivity()));
                                break;
                        }
                    }
                };
                selectDatePopupWindow = new SelectDatePopupWindow(getActivity(), tvEndDateValue.getText().toString(), dateSelectReturn);
                selectDatePopupWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    private void doSelectEndDate() {
        tvEndDateValue.setEnabled(true);
        etPeriodOfValidityValue.setEnabled(false);
        rlPeriodContainer.setBackgroundColor(getColor(R.color.common_light_gray));
        rlEndDate.setBackgroundColor(getColor(R.color.common_white));
    }

    @Override
    protected void setLayoutOfControls() {

        llContainer.addView(rlTypeNameContainer);
        llContainer.addView(rlPeriodContainer);
        llContainer.addView(rlEndDate);
        llContainer.addView(rlAnnualFeeContainer);
        llContainer.addView(rlAnnualFeeValueContainer);
        //llContainer.addView(rlGuestNumContainer);
        llContainer.addView(rlSignWhenCheckInContainer);
        llContainer.addView(rlSignForGuestContainer);

        llContainer.addView(rlOverDraftContainer);
        llContainer.addView(rlTimeLimitContainer);
        llContainer.addView(rlAgeRangeContainer);
//        llContainer.addView(rlWeekTitleContainer);
//        llContainer.addView(rlWeekSelectContainer);
        //llContainer.addView(llFeeListPriceContainer);
        llContainer.addView(rlPricingTableContainer);
        llContainer.addView(rlDeleteContainer);

        setLinearLayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100), rlTypeNameContainer);
        setLinearLayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100), rlPeriodContainer);
        setLinearLayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100), rlEndDate);
        setLinearLayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100), rlAnnualFeeContainer);
        setLinearLayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100), rlAnnualFeeValueContainer);
//        setLinearLayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100), rlGuestNumContainer);
        setLinearLayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100), rlSignWhenCheckInContainer);


        setLinearLayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100), rlSignForGuestContainer);

        setLinearLayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100), rlOverDraftContainer);
        setLinearLayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100), rlTimeLimitContainer);
        setLinearLayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100), rlAgeRangeContainer);
//        setLinearLayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(80), rlWeekTitleContainer);
//        setLinearLayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100), rlWeekSelectContainer);
      //  setLinearLayoutParams(MATCH_PARENT, WRAP_CONTENT, llFeeListPriceContainer);
        setLinearLayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100), rlPricingTableContainer);
        setLinearLayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(120), rlDeleteContainer);

        setLeftTitleLayoutParams(R.string.customers_type, tvTypeName, rlTypeNameContainer);

        etTypeNameValue.setHint(getString(R.string.customers_type));

        etPeriodOfValidityValue.setHint(getString(R.string.customers_period_in_months));

        rlPeriodContainer.addView(radioPeriod);
        rlPeriodContainer.addView(tvPeriodOfValidity);
        LayoutUtils.setCellLeftSquareViewOfRelativeLayout(radioPeriod, 45, 40, mContext);
        LayoutUtils.setWidthAndHeight(tvPeriodOfValidity,
                DensityUtil.getActualWidthOnThisDevice(300, mContext), DensityUtil.getActualHeightOnThisDevice(100, mContext));
        LayoutUtils.setRightOfView(tvPeriodOfValidity, radioPeriod, 20, mContext);

        rlEndDate.addView(radioEndDate);
        rlEndDate.addView(tvEndDateName);
        rlEndDate.addView(tvEndDateValue);
        LayoutUtils.setCellLeftSquareViewOfRelativeLayout(radioEndDate, 45, 40, mContext);
        LayoutUtils.setWidthAndHeight(tvEndDateName,
                DensityUtil.getActualWidthOnThisDevice(300, mContext), DensityUtil.getActualHeightOnThisDevice(100, mContext));
        LayoutUtils.setRightOfView(tvEndDateName, radioEndDate, 20, mContext);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvEndDateValue, mContext);

        setLeftTitleLayoutParams(R.string.customers_annual_fee, tvAnnualFee, rlAnnualFeeContainer);

        //setLeftTitleLayoutParams(R.string.customers_guest_per_day, tvGuestNum, rlGuestNumContainer);
//        etGuestNumValue.setHint(getString(R.string.customers_guest_show));

        setLeftTitleLayoutParams(R.string.customers_sign_when_check_in, tvSignWhenCheckIn, rlSignWhenCheckInContainer);

        IteeTextView tvSignForGuest = new IteeTextView(getBaseActivity());

        setLeftTitleLayoutParams(R.string.customers_sign_for_guest, tvSignForGuest, rlSignForGuestContainer);


        setLeftTitleLayoutParams(R.string.customers_over_draft, tvOverdraft, rlOverDraftContainer);
        etOverdraftValue.setHint(AppUtils.getCurrentCurrency(mContext));

        setLeftTitleLayoutParams(R.string.customers_time_limit, tvTimeLimit, rlTimeLimitContainer);
        etTimeLimitValue.setHint(getString(R.string.customers_time_limit_show));
        setLeftTitleLayoutParams(R.string.common_age, tvAge, rlAgeRangeContainer);
//        setLeftTitleLayoutParams(R.string.common_week, tvWeek, rlWeekTitleContainer);
        setLeftTitleLayoutParams(R.string.customers_pricing_table, tvPricingTable, rlPricingTableContainer);

        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            String textPricingTable = getString(R.string.event_pricing_table) + getString(R.string.common_operate_after_adding);
            SpannableStringBuilder ssb = new SpannableStringBuilder(textPricingTable);
            ssb.setSpan(new RelativeSizeSpan(1.f), 0, getString(R.string.event_pricing_table).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new RelativeSizeSpan(0.8f), getString(R.string.event_pricing_table).length(), textPricingTable.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvPricingTable.setText(ssb);
        }

        setAnnualFeeEditText();
        setAgeRangeEditText();
        if (getFragmentMode() != FragmentMode.FragmentModeAdd) {
            setRightArrow();
        }

        AppUtils.addBottomSeparatorLine(rlTypeNameContainer, this);
        AppUtils.addBottomSeparatorLine(rlPeriodContainer, this);
        AppUtils.addBottomSeparatorLine(rlEndDate, this);
        AppUtils.addBottomSeparatorLine(rlAnnualFeeContainer, this);
        AppUtils.addBottomSeparatorLine(rlAnnualFeeValueContainer, this);
//        AppUtils.addBottomSeparatorLine(rlGuestNumContainer, this);
        AppUtils.addBottomSeparatorLine(rlSignWhenCheckInContainer, this);
        AppUtils.addBottomSeparatorLine(rlSignForGuestContainer, this);



        AppUtils.addBottomSeparatorLine(rlOverDraftContainer, this);
        AppUtils.addBottomSeparatorLine(rlTimeLimitContainer, this);
        AppUtils.addBottomSeparatorLine(rlAgeRangeContainer, this);
        AppUtils.addTopSeparatorLine(rlPricingTableContainer, this);
        AppUtils.addBottomSeparatorLine(rlPricingTableContainer, this);

        setRightEditTextLayoutParams(etTypeNameValue, rlTypeNameContainer);
        setRightEditTextLayoutParams(etPeriodOfValidityValue, rlPeriodContainer);
//        setRightEditTextLayoutParams(etGuestNumValue, rlGuestNumContainer);
        setRightEditTextLayoutParams(etOverdraftValue, rlOverDraftContainer);
        setRightEditTextLayoutParams(etTimeLimitValue, rlTimeLimitContainer);
        setCheckSwitchButton(swAnnualFee, rlAnnualFeeContainer);
        setCheckSwitchButton(swSignWhenCheckIn, rlSignWhenCheckInContainer);

        setCheckSwitchButton(swSignForGuest, rlSignForGuestContainer);

        setDeleteButton(tvDelete, rlDeleteContainer);

      //  rlWeekSelectContainer.addView(selectedWeekDay);
       // RelativeLayout.LayoutParams selectedWeekDayLayoutParams = new RelativeLayout.LayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(200));
       // selectedWeekDay.setLayoutParams(selectedWeekDayLayoutParams);

//        etGuestNumValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        etAgeStartValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

        etPeriodOfValidityValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etTimeLimitValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etAgeStartValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

    }

    @Override
    protected void setPropertyOfControls() {

        //llFeeListPriceContainer.setOrientation(LinearLayout.VERTICAL);

        etTypeNameValue.setSingleLine();
        etPeriodOfValidityValue.setSingleLine();
        etAnnualFeeValue.setSingleLine();
//        etGuestNumValue.setSingleLine();
        etOverdraftValue.setSingleLine();
        etTimeLimitValue.setSingleLine();
        etAgeStartValue.setSingleLine();
        etAgeEndValue.setSingleLine();

        tvPeriodOfValidity.setText(R.string.customers_period_of_validity);
        tvEndDateName.setText(R.string.customers_end_date);

        etAgeStartValue.addTextChangedListener(new AppUtils.EditViewIntegerWatcher(etAgeStartValue, Constants.MAX_AGE));
        etAgeEndValue.addTextChangedListener(new AppUtils.EditViewIntegerWatcher(etAgeEndValue, Constants.MAX_AGE));

        configState();
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
            tvDelete.setVisibility(View.GONE);
            rlDeleteContainer.setVisibility(View.GONE);
            getTvLeftTitle().setText(R.string.common_edit);
            getTvRight().setBackgroundResource(R.drawable.icon_common_edit);
        } else if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            getTvLeftTitle().setText(R.string.common_add);
            getTvRight().setText(R.string.common_save);
        } else {
            getTvLeftTitle().setText(R.string.common_edit);
            getTvRight().setText(R.string.common_save);
        }
        getTvRight().setOnClickListener(listenerOk);
        boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlCustomersTypeEdit, getActivity());
        if (hasPermission) {
            getTvRight().setVisibility(View.VISIBLE);
        } else {
            getTvRight().setVisibility(View.INVISIBLE);
        }
        configState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void executeOnceOnCreate() {
        super.executeOnceOnCreate();
        Bundle bundle = getArguments();
        if (bundle != null) {
            memberTypeId = bundle.getInt(TransKey.CUSTOMERS_MEMBER_TYPE_ID);
            memberTypeTypeId = bundle.getString(TransKey.CUSTOMERS_MEMBER_TYPE_TYPE_ID, Constants.CUSTOMER_MEMBER);
            if (bundle.containsKey(TransKey.COMMON_FRAGMENT_MODE)) {
                setFragmentMode(FragmentMode.valueOf(bundle.getInt(TransKey.COMMON_FRAGMENT_MODE)));
            } else {
                setFragmentMode(FragmentMode.FragmentModeBrowse);
            }
            if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
                getGreenFeeList();
                currentEndDate = DateUtils.getAPIYearMonthDay(new Date());
                tvEndDateValue.setText(DateUtils.getCurrentShowYearMonthDayFromDate(new Date(), mContext));
                radioPeriod.doChecked();
                doSelectPeriod();
            } else {
                getMemberEditList();
            }
        }
    }

    private void configState() {
        boolean isCanEdit = getFragmentMode() != FragmentMode.FragmentModeBrowse;

       // selectedWeekDay.setCanUse(isCanEdit);
        etTypeNameValue.setEnabled(isCanEdit);
        etPeriodOfValidityValue.setEnabled(isCanEdit);
        tvEndDateValue.setEnabled(isCanEdit);
        etAnnualFeeValue.setEnabled(isCanEdit);
//        etGuestNumValue.setEnabled(isCanEdit);
        etOverdraftValue.setEnabled(isCanEdit);
        etTimeLimitValue.setEnabled(isCanEdit);
        etAgeStartValue.setEnabled(isCanEdit);
        etAgeEndValue.setEnabled(isCanEdit);
        swAnnualFee.setEnabled(isCanEdit);
        swSignWhenCheckIn.setEnabled(isCanEdit);


        swSignForGuest.setEnabled(isCanEdit);
        rlPricingTableContainer.setEnabled(isCanEdit);
        radioPeriod.setEnabled(isCanEdit);
        radioEndDate.setEnabled(isCanEdit);

        if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
            if (radioPeriod.isChecked()) {
                doSelectPeriod();
            } else {
                doSelectEndDate();
            }
        }

        if (!swAnnualFee.isChecked()) {
            etAnnualFeeValue.setEnabled(false);
        }

        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            rlDeleteContainer.setVisibility(View.INVISIBLE);
           // selectedWeekDay.selectAllDays();
        }

        if (Constants.CUSTOMER_NON_MEMBER.equals(memberTypeTypeId)) {
            rlPeriodContainer.setVisibility(View.GONE);
            rlEndDate.setVisibility(View.GONE);
            rlAnnualFeeContainer.setVisibility(View.GONE);
            rlAnnualFeeValueContainer.setVisibility(View.GONE);
//            rlGuestNumContainer.setVisibility(View.GONE);
            rlAgeRangeContainer.setVisibility(View.GONE);
//            rlWeekTitleContainer.setVisibility(View.GONE);
//            rlWeekSelectContainer.setVisibility(View.GONE);
            rlDeleteContainer.setVisibility(View.GONE);

            rlSignForGuestContainer.setVisibility(View.GONE);
            etTypeNameValue.setEnabled(false);
        }
    }

    private void setLinearLayoutParams(int width, int height, ViewGroup vg) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        if (vg == rlPricingTableContainer) {
            layoutParams.topMargin = getActualHeightOnThisDevice(20);
        }
        vg.setLayoutParams(layoutParams);
        if (vg == rlDeleteContainer) {
            vg.setBackgroundColor(getColor(R.color.common_light_gray));
        } else {
            vg.setBackgroundColor(getColor(R.color.common_white));
        }
    }

    private void setLeftTitleLayoutParams(int stringId, IteeTextView textView, RelativeLayout parent) {
        int width = getActualWidthOnThisDevice(360);
        if (textView == tvPricingTable) {
            width = getScreenWidth();
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, MATCH_PARENT);
        layoutParams.leftMargin = getActualWidthOnThisDevice(40);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        textView.setId(View.generateViewId());
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        textView.setTextSize(Constants.FONT_SIZE_NORMAL);
        textView.setText(stringId);
        textView.setTextColor(getColor(R.color.common_black));
        parent.addView(textView);
    }

    private void setRightEditTextLayoutParams(EditText editText, RelativeLayout parent) {
        editText.setId(View.generateViewId());
        editText.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        editText.setTextSize(Constants.FONT_SIZE_NORMAL);
        editText.setTextColor(getColor(R.color.common_gray));
        editText.setBackground(null);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        editText.setPadding(0, 6, 0, 0);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(280), MATCH_PARENT);
        layoutParams.rightMargin = getActualWidthOnThisDevice(40);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        editText.setLayoutParams(layoutParams);

        parent.addView(editText);
    }

    private void setCheckSwitchButton(CheckSwitchButton checkSwitchButton, RelativeLayout parent) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.rightMargin = getActualWidthOnThisDevice(40);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        checkSwitchButton.setLayoutParams(layoutParams);
        parent.addView(checkSwitchButton);
    }

    private void setAnnualFeeEditText() {
        etAnnualFeeValue.setBackgroundResource(R.drawable.bg_edittext_border);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(640), getActualHeightOnThisDevice(80));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, LAYOUT_TRUE);
        etAnnualFeeValue.setLayoutParams(layoutParams);
        rlAnnualFeeValueContainer.addView(etAnnualFeeValue);
    }

    private void setAgeRangeEditText() {
        etAgeStartValue.setBackgroundResource(R.drawable.bg_edittext_border);
        etAgeEndValue.setBackgroundResource(R.drawable.bg_edittext_border);
        ivAgeRangeConnector.setBackgroundColor(getColor(R.color.common_black));

        etAgeStartValue.setId(View.generateViewId());
        etAgeEndValue.setId(View.generateViewId());
        ivAgeRangeConnector.setId(View.generateViewId());

        RelativeLayout.LayoutParams etAgeEndValueLayoutParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(206), getActualHeightOnThisDevice(80));
        etAgeEndValueLayoutParams.rightMargin = getActualWidthOnThisDevice(40);
        etAgeEndValueLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        etAgeEndValueLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        etAgeEndValue.setLayoutParams(etAgeEndValueLayoutParams);


        RelativeLayout.LayoutParams ivAgeRangeConnectorLayoutParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(30), getActualHeightOnThisDevice(5));
        ivAgeRangeConnectorLayoutParams.rightMargin = getActualWidthOnThisDevice(20);
        ivAgeRangeConnectorLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivAgeRangeConnectorLayoutParams.addRule(RelativeLayout.LEFT_OF, etAgeEndValue.getId());
        ivAgeRangeConnector.setLayoutParams(ivAgeRangeConnectorLayoutParams);

        RelativeLayout.LayoutParams etAgeStartValueLayoutParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(206), getActualHeightOnThisDevice(80));
        etAgeStartValueLayoutParams.rightMargin = getActualWidthOnThisDevice(20);
        etAgeStartValueLayoutParams.addRule(RelativeLayout.LEFT_OF, ivAgeRangeConnector.getId());
        etAgeStartValueLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        etAgeStartValue.setLayoutParams(etAgeStartValueLayoutParams);

        rlAgeRangeContainer.addView(etAgeStartValue);
        rlAgeRangeContainer.addView(etAgeEndValue);
        rlAgeRangeContainer.addView(ivAgeRangeConnector);

    }

    private void setDeleteButton(IteeRedDeleteButton textView, RelativeLayout parent) {
        textView.setId(View.generateViewId());
        textView.setText(R.string.common_delete);
        textView.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(680), getActualHeightOnThisDevice(80));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, LAYOUT_TRUE);
        textView.setLayoutParams(layoutParams);
        parent.addView(textView);
    }

    private void setAddGreenFeeList(ArrayList<JsonGreenFeeList.GreenFee> feeList) {
        for (JsonGreenFeeList.GreenFee fee : feeList) {
            JsonCommonProduct jcp = new JsonCommonProduct();
            jcp.setProductId(fee.getGreenFeeId());
            jcp.setProductName(fee.getAreaName());
            jcp.setProductOriginalCost(String.valueOf(fee.getGreenFee()));

            jcp.setMemberProductNowCost(String.valueOf(fee.getGreenFee()));
            jcp.setGuestProductNowCost(String.valueOf(fee.getGreenFee()));
            jcp.setMemberProductDiscount(Constants.DEFAULT_DISCOUNT_PERCENT, Constants.DEFAULT_DISCOUNT_PERCENT,
                    Constants.MONEY_DISCOUNT_PERCENT);
            jcp.setGuestProductDiscount(Constants.DEFAULT_DISCOUNT_PERCENT, Constants.DEFAULT_DISCOUNT_PERCENT,
                    Constants.MONEY_DISCOUNT_PERCENT);


            boolean isNonMember = false;
            if (Constants.CUSTOMER_NON_MEMBER.equals(memberTypeTypeId)) {

                isNonMember = true;
            }


//            PricingTableProductItem pricingTableProductItem = new PricingTableProductItem(CustomersTypeItemDataFragment.this,
//                    getActualHeightOnThisDevice(100), PricingTableProductItem.PRICING_TABLE_PRODUCT_ITEM_MODEL_1, isNonMember);
//
//            pricingTableProductItem.setViewData(jcp);
//            pricingTableProductItem.refreshView();
//            llFeeListPriceContainer.addView(pricingTableProductItem);
            greenFeePriceItemList.add(jcp);
        }
    }

    private void setEditGreenFeeList(ArrayList<JsonMemberEditList.Fee> feeList, String memberName) {
        for (JsonMemberEditList.Fee fee : feeList) {
            JsonCommonProduct jcp = new JsonCommonProduct();
            jcp.setProductId(fee.getGreenFeeId());
            jcp.setNonMemberName(memberName);
            jcp.setFtgfId(fee.getFtgfId());
            jcp.setProductName(fee.getAreaName());
            jcp.setProductOriginalCost(fee.getGreenFee());

            if (Constants.STR_0.equals(String.valueOf(fee.getMemberMoneyDefault()))) {
                jcp.setMemberDiscountType(Constants.MONEY_DISCOUNT_MONEY);
                jcp.setMemberProductDiscount(fee.getMemberDiscountMoney());
            } else {
                jcp.setMemberDiscountType(Constants.MONEY_DISCOUNT_PERCENT);
                jcp.setMemberProductDiscount(fee.getMemberDiscount());
            }

           // jcp.setMemberMoneyDefault(String.valueOf(fee.getMemberMoneyDefault()));
            if (Constants.STR_0.equals(String.valueOf(fee.getGuestMoneyDefault()))) {
                jcp.setGuestDiscountType(Constants.MONEY_DISCOUNT_MONEY);
                jcp.setGuestProductDiscount(fee.getGuestDiscountMoney());
            } else {
                jcp.setGuestDiscountType(Constants.MONEY_DISCOUNT_PERCENT);
                jcp.setGuestProductDiscount(fee.getGuestDiscount());
            }
            //jcp.setGuestMoneyDefault(String.valueOf(fee.getGuestMoneyDefault()));
            boolean isNonMember = false;
            if (Constants.CUSTOMER_NON_MEMBER.equals(memberTypeTypeId)) {

                isNonMember = true;
            }

//            PricingTableProductItem pricingTableProductItem = new PricingTableProductItem(CustomersTypeItemDataFragment.this,
//                    getActualHeightOnThisDevice(100), PricingTableProductItem.PRICING_TABLE_PRODUCT_ITEM_MODEL_1, isNonMember);
//
//            pricingTableProductItem.setViewData(jcp);
//            pricingTableProductItem.refreshView();
//            llFeeListPriceContainer.addView(pricingTableProductItem);
            greenFeePriceItemList.add(jcp);
        }
    }

    private void setRightArrow() {
        rlPricingTableContainer.addView(ivRightArrow);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivRightArrow.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, LAYOUT_TRUE);
        layoutParams.rightMargin = getActualWidthOnThisDevice(40);
        ivRightArrow.setLayoutParams(layoutParams);

        ivRightArrow.setImageResource(R.drawable.icon_right_arrow);
    }

    private String generateAddStateGreenFeeList(ArrayList<JsonGreenFeeList.GreenFee> feeList) {
        JSONArray arrFeeList = new JSONArray();
        for (int i = 0; i < greenFeePriceItemList.size(); i++) {
            JsonCommonProduct jcp = greenFeePriceItemList.get(i);
            JsonGreenFeeList.GreenFee greenFee = feeList.get(i);
            try {
                JSONObject joFee = new JSONObject();
                joFee.put(ApiKey.MEMBER_GREEN_FEE_ID, jcp.getProductId());
                joFee.put(ApiKey.MEMBER_GREEN_FEE_TYPE, greenFee.getGreenFeeType());
                joFee.put(ApiKey.MEMBER_GREEN_FEE_MORE, jcp.getProductName().contains(Constants.GREEN_FEE_PLAY_MORE)
                        ? Constants.STR_1 : Constants.STR_0);
                if (Constants.MONEY_DISCOUNT_MONEY.equals(jcp.getMemberDiscountType())) {
                    joFee.put(ApiKey.MEMBER_MEMBER_DISCOUNT, Constants.STR_EMPTY);
                    joFee.put(ApiKey.MEMBER_MEMBER_DISCOUNT_MONEY, jcp.getMemberProductDiscount());
                } else {
                    joFee.put(ApiKey.MEMBER_MEMBER_DISCOUNT, jcp.getMemberProductDiscount());
                    joFee.put(ApiKey.MEMBER_MEMBER_DISCOUNT_MONEY, Constants.STR_EMPTY);
                }
                joFee.put(ApiKey.MEMBER_MEMBER_NOW_COST, jcp.getMemberProductNowCost());
                if (Constants.MONEY_DISCOUNT_MONEY.equals(jcp.getGuestDiscountType())) {
                    joFee.put(ApiKey.MEMBER_GUEST_DISCOUNT, Constants.STR_EMPTY);
                    joFee.put(ApiKey.MEMBER_GUEST_DISCOUNT_MONEY, jcp.getGuestProductDiscount());
                } else {
                    joFee.put(ApiKey.MEMBER_GUEST_DISCOUNT, jcp.getGuestProductDiscount());
                    joFee.put(ApiKey.MEMBER_GUEST_DISCOUNT_MONEY, Constants.STR_EMPTY);
                }
                joFee.put(ApiKey.MEMBER_GUEST_NOW_COST, jcp.getGuestProductNowCost());
                arrFeeList.put(joFee);
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
        return arrFeeList.toString();
    }

    private String generateEditStateGreenFeeList() {
        JSONArray arrFeeList = new JSONArray();
        for (int i = 0; i < greenFeePriceItemList.size(); i++) {
            JsonCommonProduct jcp = greenFeePriceItemList.get(i);
            try {
                JSONObject joFee = new JSONObject();
                joFee.put(ApiKey.MEMBER_GREEN_FEE_ID, jcp.getProductId());
                joFee.put(ApiKey.MEMBER_GREEN_FEE_FGTF_ID, jcp.getFtgfId());
                if (Constants.MONEY_DISCOUNT_MONEY.equals(jcp.getMemberDiscountType())) {
                    joFee.put(ApiKey.MEMBER_MEMBER_DISCOUNT, Constants.STR_EMPTY);
                    joFee.put(ApiKey.MEMBER_MEMBER_DISCOUNT_MONEY, jcp.getMemberProductDiscount());
                } else {
                    joFee.put(ApiKey.MEMBER_MEMBER_DISCOUNT, jcp.getMemberProductDiscount());
                    joFee.put(ApiKey.MEMBER_MEMBER_DISCOUNT_MONEY, Constants.STR_EMPTY);
                }
                joFee.put(ApiKey.MEMBER_MEMBER_NOW_COST, jcp.getMemberProductNowCost());
                if (Constants.MONEY_DISCOUNT_MONEY.equals(jcp.getGuestDiscountType())) {
                    joFee.put(ApiKey.MEMBER_GUEST_DISCOUNT, Constants.STR_EMPTY);
                    joFee.put(ApiKey.MEMBER_GUEST_DISCOUNT_MONEY, jcp.getGuestProductDiscount());
                } else {
                    joFee.put(ApiKey.MEMBER_GUEST_DISCOUNT, jcp.getGuestProductDiscount());
                    joFee.put(ApiKey.MEMBER_GUEST_DISCOUNT_MONEY, Constants.STR_EMPTY);
                }
                joFee.put(ApiKey.MEMBER_GUEST_NOW_COST, jcp.getGuestProductNowCost());
                arrFeeList.put(joFee);
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
        return arrFeeList.toString();
    }

    private void getGreenFeeList() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        HttpManager<JsonGreenFeeList> hh = new HttpManager<JsonGreenFeeList>(CustomersTypeItemDataFragment.this) {

            @Override
            public void onJsonSuccess(JsonGreenFeeList jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    addStateGreenFeeList = jo.getDataList();
                    setAddGreenFeeList(addStateGreenFeeList);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
            }
        };
        hh.startGet(this.getActivity(), ApiManager.HttpApi.MemberGreenFeeListGet, params);
    }


    private void getMemberEditList() {


        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.MEMBER_EDIT_LIST_MEMBER_TYPE_ID, String.valueOf(memberTypeId));
        HttpManager<JsonMemberEditList> hh = new HttpManager<JsonMemberEditList>(CustomersTypeItemDataFragment.this) {

            @Override
            public void onJsonSuccess(JsonMemberEditList jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    etTypeNameValue.setText(jo.getMemberName());
                    String periodOfValidity = jo.getMemberValidity() + getString(R.string.customers_month);

//                    etGuestNumValue.setText(String.valueOf(jo.getGuestNum()));
                    String overdraft = AppUtils.getCurrentCurrency(getActivity()) + Constants.STR_EMPTY + jo.getOverdraft();
                    etOverdraftValue.setText(overdraft);
                    String timeLimitValue = jo.getTimeLimit() + getString(R.string.player_info_day);
                    etTimeLimitValue.setText(timeLimitValue);
                    etAgeStartValue.setText(jo.getAgeStart());
                    etAgeEndValue.setText(jo.getAgeEnd());
                    swAnnualFee.setChecked(Constants.SWITCH_RIGHT == jo.getAnnualFee());
                    swSignWhenCheckIn.setChecked(Constants.SWITCH_RIGHT == jo.getCheckIn());

                    swSignForGuest.setChecked(Constants.SWITCH_RIGHT == jo.getSignGuest());
                   // selectedWeekDay.setSelectedWeekDays(jo.getWeek());
                    if (swAnnualFee.isChecked()) {
                        String fee = AppUtils.getCurrentCurrency(getActivity()) + Constants.STR_EMPTY + jo.getAnnualPrice();
                        etAnnualFeeValue.setText(fee);
                    }
                    if (jo.getFeeList() != null && jo.getFeeList().size() > 0) {
                        setEditGreenFeeList(jo.getFeeList(), jo.getMemberName());
                    }
                    currentEndDate = DateUtils.getAPIYearMonthDayFromCurrentShow(jo.getEndDate(), mContext);
                    if (Constants.STR_0.equals(jo.getEndType())) {
                        radioPeriod.doChecked();
                        radioEndDate.doUnchecked();
                        etPeriodOfValidityValue.setText(periodOfValidity);
                        rlEndDate.setBackgroundColor(getColor(R.color.common_light_gray));
                        tvEndDateValue.setText(DateUtils.getCurrentShowYearMonthDayFromDate(new Date(), getActivity()));
                    } else {
                        radioEndDate.doChecked();
                        radioPeriod.doUnchecked();
                        currentEndDate = jo.getEndDate();
                        tvEndDateValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(jo.getEndDate(), getActivity()));
                        rlPeriodContainer.setBackgroundColor(getColor(R.color.common_light_gray));
                    }
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
            }
        };
        hh.startGet(this.getActivity(), ApiManager.HttpApi.MemberEditListGet, params);
    }

    private void postMember() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));

        params.put(ApiKey.MEMBER_MEMBER_NAME, etTypeNameValue.getText().toString());
        params.put(ApiKey.MEMBER_ANNUAL_FEE, String.valueOf(swAnnualFee.isChecked() ? Constants.SWITCH_RIGHT
                : Constants.SWITCH_LEFT));
        params.put(ApiKey.MEMBER_ANNUAL_PRIZE, etAnnualFeeValue.getValue());
//        params.put(ApiKey.MEMBER_GUEST, etGuestNumValue.getText().toString());
        params.put(ApiKey.MEMBER_CHECK_IN, String.valueOf(swSignWhenCheckIn.isChecked() ? Constants.SWITCH_RIGHT
                : Constants.SWITCH_LEFT));
        params.put(ApiKey.MEMBER_OVERDRAFT, etOverdraftValue.getValue());
        params.put(ApiKey.MEMBER_TIME_LIMIT, etTimeLimitValue.getText().toString());
        params.put(ApiKey.MEMBER_AGE_START, etAgeStartValue.getText().toString());
        params.put(ApiKey.MEMBER_AGE_END, etAgeEndValue.getText().toString());
       // params.put(ApiKey.MEMBER_WEEK, selectedWeekDay.getSelectedWeekDays());
        params.put(ApiKey.MEMBER_DATA_INFO, generateAddStateGreenFeeList(addStateGreenFeeList));
        params.put(ApiKey.MEMBER_SIGN_GUEST,
                String.valueOf(swSignForGuest.isChecked() ? Constants.SWITCH_RIGHT : Constants.SWITCH_LEFT));
        String endType = radioPeriod.isChecked() ? Constants.STR_0 : Constants.STR_1;
        params.put(ApiKey.MEMBER_TYPE_FLAG, endType);
        if (radioEndDate.isChecked()) {
            params.put(ApiKey.MEMBER_END_DATE, currentEndDate);
        } else {
            params.put(ApiKey.MEMBER_MEMBER_VALIDITY, etPeriodOfValidityValue.getText().toString());
        }

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(CustomersTypeItemDataFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY) {
                    doBackWithRefresh();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.start(this.getActivity(), ApiManager.HttpApi.MemberPOST, params);
    }

    private void putMember() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.MEMBER_EDIT_LIST_MEMBER_TYPE_ID, String.valueOf(memberTypeId));

        params.put(ApiKey.MEMBER_MEMBER_NAME, etTypeNameValue.getText().toString());
        params.put(ApiKey.MEMBER_CHECK_IN,
                String.valueOf(swSignWhenCheckIn.isChecked() ? Constants.SWITCH_RIGHT : Constants.SWITCH_LEFT));



        params.put(ApiKey.MEMBER_SIGN_GUEST,
                String.valueOf(swSignForGuest.isChecked() ? Constants.SWITCH_RIGHT : Constants.SWITCH_LEFT));


        params.put(ApiKey.MEMBER_OVERDRAFT, etOverdraftValue.getValue());
        params.put(ApiKey.MEMBER_TIME_LIMIT, etTimeLimitValue.getText().toString());
        if (Constants.CUSTOMER_MEMBER.equals(memberTypeTypeId)) {
            params.put(ApiKey.MEMBER_AGE_START, etAgeStartValue.getText().toString());
            params.put(ApiKey.MEMBER_AGE_END, etAgeEndValue.getText().toString());
            //params.put(ApiKey.MEMBER_WEEK, selectedWeekDay.getSelectedWeekDays());

            params.put(ApiKey.MEMBER_ANNUAL_FEE,
                    String.valueOf(swAnnualFee.isChecked() ? Constants.SWITCH_RIGHT : Constants.SWITCH_LEFT));
            params.put(ApiKey.MEMBER_ANNUAL_PRIZE, etAnnualFeeValue.getValue());
//            params.put(ApiKey.MEMBER_GUEST, etGuestNumValue.getText().toString());

            String endType = radioPeriod.isChecked() ? Constants.STR_0 : Constants.STR_1;
            params.put(ApiKey.MEMBER_TYPE_FLAG, endType);
            if (radioEndDate.isChecked()) {
                params.put(ApiKey.MEMBER_END_DATE, currentEndDate);
            } else {
                params.put(ApiKey.MEMBER_MEMBER_VALIDITY, etPeriodOfValidityValue.getText().toString());
            }
        }

        params.put(ApiKey.MEMBER_DATA_INFO, generateEditStateGreenFeeList());
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(CustomersTypeItemDataFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();

                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    doBackWithRefresh();
                }
                Utils.showShortToast(getActivity(), msg);
            }

            @Override
            public void onJsonError(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startPut(this.getActivity(), ApiManager.HttpApi.MemberPUT, params);
    }

    private void deleteMember() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.MEMBER_EDIT_LIST_MEMBER_TYPE_ID, String.valueOf(memberTypeId));
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(CustomersTypeItemDataFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();

                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {
                    doBackWithRefresh();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startDelete(this.getActivity(), ApiManager.HttpApi.MemberDelete, params);
    }

    private boolean doCheck() {
        boolean res = true;
        if (Constants.CUSTOMER_MEMBER.equals(memberTypeTypeId)) {
            if (Utils.isStringNullOrEmpty(etTypeNameValue.getText().toString())) {
                res = false;
                Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.customers_type));
            }
            if (radioPeriod.isChecked() && Utils.isStringNullOrEmpty(etPeriodOfValidityValue.getText().toString())) {
                res = false;
                Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.customers_period_of_validity));
            }
//            if (Utils.isStringNullOrEmpty(etGuestNumValue.getText().toString())) {
//                res = false;
//                Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.customers_guest));
//            }
//            if (Utils.isStringNullOrEmpty(selectedWeekDay.getSelectedWeekDays())) {
//                res = false;
//                Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.common_week));
//            }
            if (Utils.isStringNotNullOrEmpty(etAgeStartValue.getText().toString())
                    && Utils.isStringNotNullOrEmpty(etAgeEndValue.getText().toString())) {
                if (Integer.valueOf(etAgeStartValue.getText().toString())
                        > Integer.valueOf(etAgeEndValue.getText().toString())) {
                    res = false;
                    Utils.showShortToast(getActivity(), AppUtils.generateLargerThanMessage(this, R.string.common_age_end, R.string.common_age_start));
                }
            }

            if (swAnnualFee.isChecked() && StringUtils.isEmpty(etAnnualFeeValue.getValue())) {
                res = false;
                Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.customers_annual_fee));
            }

            if (radioEndDate.isChecked() && StringUtils.isEmpty(tvEndDateValue.getText())) {
                res = false;
                Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.customers_end_date));
            }
        }


        if (Utils.isStringNullOrEmpty(etOverdraftValue.getText().toString())&&!Utils.isStringNullOrEmpty(etTimeLimitValue.getText().toString())) {
            res = false;
            Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.customers_over_draft));
        }
        if (Utils.isStringNullOrEmpty(etTimeLimitValue.getText().toString())&&!Utils.isStringNullOrEmpty(etOverdraftValue.getText().toString())) {
            res = false;
            Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.customers_time_limit));
        }
        return res;
    }
}