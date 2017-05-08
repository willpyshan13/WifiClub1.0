/**
 * Project Name: itee
 * File Name:	 AdministrationEditFragment.java
 * Package Name: cn.situne.itee.fragment.administration
 * Date:		 2015-01-21
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.administration;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonEditAdministration;
import cn.situne.itee.manager.jsonentity.JsonLogin;
import cn.situne.itee.view.CheckSwitchButton;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.PaymentPatternPopupWindow;
import cn.situne.itee.view.popwindow.DateFormatPopupWindow;

/**
 * ClassName:AdministrationEditFragment <br/>
 * Function: administration page. <br/>
 * UI:  02-3
 * Date: 2015-01-21 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class AdministrationEditFragment extends BaseFragment {

    private boolean isEdit;

    private RelativeLayout rlCityContainer;
    private RelativeLayout rlTimeZoneContainer;
    private RelativeLayout rlCurrencyContainer;
    private RelativeLayout rlLogoutAfterContainer;
    private LinearLayout llLogoutAfterContainer;
    private RelativeLayout rlSendReserveMessageContainer;
    private RelativeLayout rlOneTeeStartContainer;
    private RelativeLayout rlPaymentPatternContainer;
    private RelativeLayout rlSalesTaxExcludingContainer;
    private RelativeLayout rlSalesTaxContainer;
    private RelativeLayout rlShowSalesTaxContainer;
    private RelativeLayout rlUnitContainer;
    private RelativeLayout rlFirstDayContainer;
    private RelativeLayout rlAddCaddieFeeContainer;

    private IteeTextView tvCityName;
    private IteeTextView tvCityValue;

    private IteeTextView tvTimeZoneName;
    private IteeTextView tvTimeZoneValue;

    private IteeTextView tvDateFormatName;
    private IteeTextView tvDateFormatValue;

    private IteeTextView tvCurrencyName;
    private IteeTextView tvCurrencyValue;

    private String logoutAfterTimeM;
    private IteeTextView tvLogoutAfter;
    private IteeEditText etLogoutAfterTimeH;
    private IteeTextView tvLogoutAfterH;
    private IteeEditText etLogoutAfterTimeM;
    private IteeTextView tvLogoutAfterM;
    private CheckSwitchButton csbLogoutAfter;

    private String beforeLogoutAfterTimeM;

    private IteeTextView tvSendReserveMessage;
    private CheckSwitchButton csbSendReserveMessage;

    private IteeTextView tvOneTeeStart;
    private CheckSwitchButton csbOneTeeStart;

    private IteeTextView tvPaymentPatternName;
    private IteeTextView tvPaymentPatternValue;

    private IteeTextView tvSalesTaxExcluding;
    private CheckSwitchButton csbSalesTaxExcluding;

    private IteeTextView tvSalesTaxName;
    private IteeEditText etSalesTaxValue;
    private IteeTextView tvSalesPercent;

    private IteeTextView tvShowSalesTaxName;
    private CheckSwitchButton csbShowSalesTax;

    private IteeTextView tvUnit;
    private CheckSwitchButton csbUnit;

    private IteeTextView tvFirstDay;
    private CheckSwitchButton csbFirstDay;

    private IteeTextView tvAddCaddieFeeDay;
    private CheckSwitchButton csbAddCaddieFeeDay;

    private ArrayList<String> currencyNameList;
    private ArrayList<String> currencySymbolList;
    private ArrayList<Integer> currencyIdList;


    private String currentCityId;
    private Integer currentCurrencyId;

    private String swLogoutAfterStatus;
    private String swSendReserveMessageStatus;
    private String swOneTeeStartStatus;
    private String swSalesTaxExcludingStatus;
    private String paymentPattern;
    private String swUnitStatus;
    private String swFirstDayStatus;
    private String swAddCaddieFeeDayStatus;
    private String swShowSalesTax;

    private PaymentPatternPopupWindow paymentPatternPopupWindow;
    private View.OnClickListener listenerPaymentPattern;

    private DateFormatPopupWindow dateFormatPopupWindow;
    private AppUtils.NoDoubleClickListener listenerDateFormat;

    private String currentDateFormat;

    private AppUtils.NoDoubleClickListener listenerOk;

    private RelativeLayout rlDateFormatContainer;


    private RelativeLayout rlDateSettingContainer;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_do_edit_administration;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.do_edit_administration;
    }

    @Override
    protected void initControls(View rootView) {

        rlCityContainer = (RelativeLayout) rootView.findViewById(R.id.rl_city_container);
        rlTimeZoneContainer = (RelativeLayout) rootView.findViewById(R.id.rl_time_zone_container);
        rlDateFormatContainer = (RelativeLayout) rootView.findViewById(R.id.rl_date_format_container);

        rlDateSettingContainer = (RelativeLayout) rootView.findViewById(R.id.rl_date_setting_container);
        rlCurrencyContainer = (RelativeLayout) rootView.findViewById(R.id.rl_currency_container);
        rlLogoutAfterContainer = (RelativeLayout) rootView.findViewById(R.id.rl_logout_after_container);
        llLogoutAfterContainer = (LinearLayout) rootView.findViewById(R.id.ll_logout_after_container);
        rlSendReserveMessageContainer = (RelativeLayout) rootView.findViewById(R.id.rl_send_reserve_message_container);
        rlOneTeeStartContainer = (RelativeLayout) rootView.findViewById(R.id.rl_one_tee_start_container);
        rlOneTeeStartContainer.setVisibility(View.GONE);
        rlPaymentPatternContainer = (RelativeLayout) rootView.findViewById(R.id.rl_payment_pattern_container);
        rlSalesTaxExcludingContainer = (RelativeLayout) rootView.findViewById(R.id.rl_sales_tax_excluding_container);
        rlSalesTaxContainer = (RelativeLayout) rootView.findViewById(R.id.rl_sales_tax_container);
        rlShowSalesTaxContainer = (RelativeLayout) rootView.findViewById(R.id.rl_show_sales_tax_container);
        rlUnitContainer = (RelativeLayout) rootView.findViewById(R.id.rl_unit_container);
        rlFirstDayContainer = (RelativeLayout) rootView.findViewById(R.id.rl_first_day_container);
        rlAddCaddieFeeContainer = (RelativeLayout) rootView.findViewById(R.id.rl_add_caddie_fee_in_purchase_container);


        tvCityName = new IteeTextView(this);
        tvCityValue = new IteeTextView(this);

        tvTimeZoneName = new IteeTextView(this);
        tvTimeZoneValue = new IteeTextView(this);

        tvDateFormatName = new IteeTextView(this);
        tvDateFormatValue = new IteeTextView(this);

        tvCurrencyName = new IteeTextView(this);
        tvCurrencyValue = new IteeTextView(this);

        tvLogoutAfter = new IteeTextView(this);
        etLogoutAfterTimeH = new IteeEditText(this);
        tvLogoutAfterH = new IteeTextView(this);
        etLogoutAfterTimeM = new IteeEditText(this);
        tvLogoutAfterM = new IteeTextView(this);
        csbLogoutAfter = new CheckSwitchButton(this);

        tvSendReserveMessage = new IteeTextView(this);
        csbSendReserveMessage = new CheckSwitchButton(this);

        tvOneTeeStart = new IteeTextView(this);
        csbOneTeeStart = new CheckSwitchButton(this);

        tvPaymentPatternName = new IteeTextView(this);
        tvPaymentPatternValue = new IteeTextView(this);

        tvSalesTaxExcluding = new IteeTextView(this);
        csbSalesTaxExcluding = new CheckSwitchButton(this);

        tvSalesTaxName = new IteeTextView(this);
        etSalesTaxValue = new IteeEditText(this);
        tvSalesPercent = new IteeTextView(this);

        tvShowSalesTaxName = new IteeTextView(this);
        csbShowSalesTax = new CheckSwitchButton(this);

        tvUnit = new IteeTextView(this);
        csbUnit = new CheckSwitchButton(this, CheckSwitchButton.TYPE_METER_OR_YARD);

        tvFirstDay = new IteeTextView(this);
        csbFirstDay = new CheckSwitchButton(this, CheckSwitchButton.TYPE_SUN_OR_MON);

        tvAddCaddieFeeDay = new IteeTextView(this);
        csbAddCaddieFeeDay = new CheckSwitchButton(this);

        currencyNameList = new ArrayList<>();
        currencySymbolList = new ArrayList<>();
        currencyIdList = new ArrayList<>();

        currentDateFormat = AppUtils.getCurrentDateFormat(getActivity());
    }

    @Override
    protected void setDefaultValueOfControls() {
        changeDateFormatValue(currentDateFormat);
    }

    @Override
    protected void setListenersOfControls() {
        rlCurrencyContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    Bundle bundle = new Bundle();
                    bundle.putIntegerArrayList("currencyIdList", currencyIdList);
                    bundle.putStringArrayList("currencySymbolList", currencySymbolList);
                    bundle.putStringArrayList("currencyNameList", currencyNameList);
                    push(CurrencyListFragment.class, bundle);
                }
            }
        });

        rlCityContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    push(CityListFragment.class);
                }
            }
        });

        etLogoutAfterTimeH.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    etLogoutAfterTimeH.setHint(null);
                } else {
                    etLogoutAfterTimeH.setHint(Constants.DEFAULT_LOGOUT_TIME_HOUR);
                }
            }
        });


        etLogoutAfterTimeM.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    etLogoutAfterTimeM.setHint(null);
                } else {
                    etLogoutAfterTimeM.setHint(Constants.DEFAULT_LOGOUT_TIME_MIN);
                }
            }
        });

        listenerPaymentPattern = new View.OnClickListener() {

            public void onClick(View v) {
                paymentPatternPopupWindow.dismiss();
                switch (v.getId()) {
                    case R.id.btn_pay_cash:
                        tvPaymentPatternValue.setText(getText(R.string.tag_pay_cash));
                        paymentPattern = Constants.PAYMENT_PATTERN_CASH;
                        break;
                    case R.id.btn_third_party_payment:
                        tvPaymentPatternValue.setText(getText(R.string.tag_third_party_ui_show));
                        paymentPattern = Constants.PAYMENT_PATTERN_THIRD_PARTY;
                        break;
                    case R.id.btn_credit_card:
                        tvPaymentPatternValue.setText(getText(R.string.tag_credit_card));
                        paymentPattern = Constants.PAYMENT_PATTERN_CREDIT_CARD;
                        break;
                    default:
                        paymentPattern = Constants.PAYMENT_PATTERN_CASH;
                        break;
                }
            }
        };

        rlPaymentPatternContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(tvPaymentPatternValue.getWindowToken(), 0);
                paymentPatternPopupWindow = new PaymentPatternPopupWindow(getActivity(), listenerPaymentPattern);
                paymentPatternPopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_window),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        etSalesTaxValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    etSalesTaxValue.setHint(null);
                } else {
                    etSalesTaxValue.setHint(Constants.DEFAULT_SALES_TAX);
                }
            }
        });

        listenerOk = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                getTvLeftTitle().setText(R.string.title_administration);
                isEdit = !isEdit;
                etLogoutAfterTimeH.setEnabled(isEdit);
                etLogoutAfterTimeM.setEnabled(isEdit);
                csbLogoutAfter.setEnabled(isEdit);
                csbSendReserveMessage.setEnabled(isEdit);
                csbOneTeeStart.setEnabled(isEdit);
                rlPaymentPatternContainer.setEnabled(isEdit);
                csbSalesTaxExcluding.setEnabled(isEdit);
                etSalesTaxValue.setEnabled(isEdit);
                csbShowSalesTax.setEnabled(isEdit);
                csbUnit.setEnabled(isEdit);
                csbFirstDay.setEnabled(isEdit);
                csbAddCaddieFeeDay.setEnabled(isEdit);
                if (csbShowSalesTax.isChecked()) {
                    doShowTax();
                } else {
                    doNotShowTax();
                }
                if (isEdit) {
                    getTvRight().setBackground(null);
                    getTvRight().setText(R.string.common_ok);

                    etLogoutAfterTimeH.setVisibility(View.VISIBLE);
                    etLogoutAfterTimeM.setVisibility(View.VISIBLE);

                    if (Utils.isStringNotNullOrEmpty(logoutAfterTimeM)) {
                        etLogoutAfterTimeH.setText((Integer.parseInt(logoutAfterTimeM) / 60) + Constants.STR_EMPTY);
                        etLogoutAfterTimeM.setText((Integer.parseInt(logoutAfterTimeM) % 60) + Constants.STR_EMPTY);
                    }

                    tvLogoutAfterH.setText(Constants.TIME_HOUR_UPPERCASE);
                    tvLogoutAfterH.setTextColor(getResources().getColor(R.color.common_black));
                } else {
                    etLogoutAfterTimeH.setVisibility(View.GONE);
                    etLogoutAfterTimeM.setVisibility(View.GONE);

                    if (Utils.isStringNullOrEmpty(etLogoutAfterTimeH.getText().toString())) {
                        etLogoutAfterTimeH.setText(Constants.STR_0);
                    }
                    if (Utils.isStringNullOrEmpty(etLogoutAfterTimeM.getText().toString())) {
                        etLogoutAfterTimeM.setText(Constants.STR_0);
                    }

                    tvLogoutAfterH.setText(Constants.STR_SPACE + ((Integer.parseInt(etLogoutAfterTimeH.getText().toString()) * 60) + Integer.parseInt(etLogoutAfterTimeM.getText().toString())) + Constants.STR_SPACE);
                    tvLogoutAfterH.setTextColor(getResources().getColor(R.color.common_blue));
                    logoutAfterTimeM = tvLogoutAfterH.getText().toString().trim();
                    if (csbLogoutAfter.isChecked()) {
                        swLogoutAfterStatus = Constants.STR_1;
                    } else {
                        swLogoutAfterStatus = Constants.STR_0;
                    }

                    if (csbSendReserveMessage.isChecked()) {
                        swSendReserveMessageStatus = Constants.STR_1;
                    } else {
                        swSendReserveMessageStatus = Constants.STR_0;
                    }

                    if (csbOneTeeStart.isChecked()) {
                        swOneTeeStartStatus = Constants.STR_1;
                    } else {
                        swOneTeeStartStatus = Constants.STR_0;
                    }

                    if (csbSalesTaxExcluding.isChecked()) {
                        swSalesTaxExcludingStatus = Constants.STR_1;
                    } else {
                        swSalesTaxExcludingStatus = Constants.STR_0;
                    }


                    if (csbUnit.isChecked()) {
                        swUnitStatus = Constants.UNIT_METERS;
                    } else {
                        swUnitStatus = Constants.UNIT_YARDS;
                    }

                    if (csbFirstDay.isChecked()) {
                        swFirstDayStatus = Constants.FIRST_DAY_SUN;
                    } else {
                        swFirstDayStatus = Constants.FIRST_DAY_MON;
                    }

                    if (csbAddCaddieFeeDay.isChecked()) {
                        swAddCaddieFeeDayStatus = Constants.STR_FLAG_YES;
                    } else {
                        swAddCaddieFeeDayStatus = Constants.STR_FLAG_NO;
                    }

                    if (csbShowSalesTax.isChecked()) {
                        swShowSalesTax = Constants.STR_FLAG_YES;
                        doShowTax();
                    } else {
                        swShowSalesTax = Constants.STR_FLAG_NO;
                        doNotShowTax();
                    }

                    if (Integer.valueOf(logoutAfterTimeM) <= 0) {
                        logoutAfterTimeM = beforeLogoutAfterTimeM;
                        etLogoutAfterTimeH.setText((Integer.parseInt(logoutAfterTimeM) / 60) + Constants.STR_EMPTY);
                        etLogoutAfterTimeM.setText((Integer.parseInt(logoutAfterTimeM) % 60) + Constants.STR_EMPTY);
                        tvLogoutAfterH.setText(Constants.STR_SPACE + ((Integer.parseInt(etLogoutAfterTimeH.getText().toString()) * 60) + Integer.parseInt(etLogoutAfterTimeM.getText().toString())) + Constants.STR_SPACE);
                        Utils.showShortToast(getActivity(), R.string.do_edit_administration_logout_after_time_tips);
                    } else {
                        putAdministration();
                    }

                    getTvRight().setBackgroundResource(R.drawable.administration_icon_edit);
                    getTvRight().setText(Constants.STR_EMPTY);
                }
            }
        };

        listenerDateFormat = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_yyyy_mm_dd:
                        currentDateFormat = DateUtils.DATE_FORMAT_SHOW_YYYY_MM_DD;
                        break;
                    case R.id.tv_mm_dd_yyyy:
                        currentDateFormat = DateUtils.DATE_FORMAT_SHOW_MM_DD_YYYY;
                        break;
                    case R.id.tv_dd_mm_yyyy:
                        currentDateFormat = DateUtils.DATE_FORMAT_SHOW_DD_MM_YYYY;
                        break;
                    default:
                        break;
                }
                changeDateFormatValue(currentDateFormat);
                dateFormatPopupWindow.dismiss();
            }
        };

        rlDateFormatContainer.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                if (isEdit) {
                    dateFormatPopupWindow = new DateFormatPopupWindow(getActivity(), listenerDateFormat);
                    dateFormatPopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_window),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });


        rlDateSettingContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit) {
                    push(DateSettingFragment.class, null);
                }
            }
        });
        rlDateSettingContainer.setVisibility(View.GONE);
        csbShowSalesTax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    doShowTax();
                } else {
                    doNotShowTax();
                }
            }
        });

        csbSalesTaxExcluding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etSalesTaxValue.setEnabled(true);
                } else {
                    etSalesTaxValue.setEnabled(false);
                }
            }
        });
    }

    private void doNotShowTax() {
        csbSalesTaxExcluding.setChecked(false);
        csbSalesTaxExcluding.setEnabled(false);
        etSalesTaxValue.setEnabled(false);
        rlSalesTaxExcludingContainer.setBackgroundColor(getColor(R.color.common_light_gray));
        rlSalesTaxContainer.setBackgroundColor(getColor(R.color.common_light_gray));
    }

    private void doShowTax() {
        if (isEdit) {
            csbSalesTaxExcluding.setEnabled(true);
            if (csbSalesTaxExcluding.isChecked()) {
                etSalesTaxValue.setEnabled(true);
            } else {
                etSalesTaxValue.setEnabled(false);
            }
        }

        rlSalesTaxExcludingContainer.setBackgroundColor(getColor(R.color.common_white));
        rlSalesTaxContainer.setBackgroundColor(getColor(R.color.common_white));
    }

    @Override
    protected void setLayoutOfControls() {

        Context mContext = getActivity();

        LinearLayout.LayoutParams rowLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100));

        rlCityContainer.setLayoutParams(rowLayoutParams);
        rlTimeZoneContainer.setLayoutParams(rowLayoutParams);
        rlDateFormatContainer.setLayoutParams(rowLayoutParams);

        rlDateSettingContainer.setLayoutParams(rowLayoutParams);

        IteeTextView tvShowSetting = new IteeTextView(getBaseActivity());
        tvShowSetting.setText(getString(R.string.administration_date_setting));


        IteeTextView tvShowSettingDetail = new IteeTextView(getBaseActivity());
        tvShowSettingDetail.setTextColor(getColor(R.color.common_gray));

        ImageView settingRightIcon = new ImageView(getBaseActivity());
        settingRightIcon.setBackgroundResource(R.drawable.icon_right_arrow);

        RelativeLayout.LayoutParams tvShowSettingDetailParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, getActualHeightOnThisDevice(30));
        tvShowSettingDetailParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        tvShowSettingDetailParams.leftMargin = getActualWidthOnThisDevice(20);
        tvShowSettingDetailParams.bottomMargin = getActualHeightOnThisDevice(4);


        tvShowSettingDetail.setLayoutParams(tvShowSettingDetailParams);

        tvShowSettingDetail.setText(getString(R.string.administration_date_setting_comment));

        tvShowSettingDetail.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);
        rlDateSettingContainer.addView(settingRightIcon);
        rlDateSettingContainer.addView(tvShowSetting);
        rlDateSettingContainer.addView(tvShowSettingDetail);


        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvShowSetting, getActualWidthOnThisDevice(20), getBaseActivity());
        LayoutUtils.setRightArrow(settingRightIcon, getActualWidthOnThisDevice(20), getBaseActivity());


        rlCurrencyContainer.setLayoutParams(rowLayoutParams);
        rlLogoutAfterContainer.setLayoutParams(rowLayoutParams);
        rlSendReserveMessageContainer.setLayoutParams(rowLayoutParams);
        rlOneTeeStartContainer.setLayoutParams(rowLayoutParams);
        rlPaymentPatternContainer.setLayoutParams(rowLayoutParams);
        rlShowSalesTaxContainer.setLayoutParams(rowLayoutParams);
        rlSalesTaxExcludingContainer.setLayoutParams(rowLayoutParams);
        rlSalesTaxContainer.setLayoutParams(rowLayoutParams);
        rlUnitContainer.setLayoutParams(rowLayoutParams);
        rlFirstDayContainer.setLayoutParams(rowLayoutParams);
        rlAddCaddieFeeContainer.setLayoutParams(rowLayoutParams);

        rlCityContainer.addView(tvCityName);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayoutWithEightyPercent(tvCityName, mContext);

        rlCityContainer.addView(tvCityValue);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvCityValue, mContext);

        rlTimeZoneContainer.addView(tvTimeZoneName);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayoutWithEightyPercent(tvTimeZoneName, mContext);

        rlTimeZoneContainer.addView(tvTimeZoneValue);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvTimeZoneValue, mContext);

        rlDateFormatContainer.addView(tvDateFormatName);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayoutWithEightyPercent(tvDateFormatName, mContext);

        rlDateFormatContainer.addView(tvDateFormatValue);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvDateFormatValue, mContext);

        rlCurrencyContainer.addView(tvCurrencyName);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayoutWithEightyPercent(tvCurrencyName, getActivity());

        rlCurrencyContainer.addView(tvCurrencyValue);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvCurrencyValue, mContext);

        llLogoutAfterContainer.addView(tvLogoutAfter);
        LinearLayout.LayoutParams paramsTvLogoutAfter = (LinearLayout.LayoutParams) tvLogoutAfter.getLayoutParams();
        paramsTvLogoutAfter.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        paramsTvLogoutAfter.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        paramsTvLogoutAfter.setMargins(getActualWidthOnThisDevice(20), 0, 0, 0);
        tvLogoutAfter.setLayoutParams(paramsTvLogoutAfter);

        llLogoutAfterContainer.addView(etLogoutAfterTimeH);
        LinearLayout.LayoutParams paramsEtLogoutAfterTime = (LinearLayout.LayoutParams) etLogoutAfterTimeH.getLayoutParams();
        paramsEtLogoutAfterTime.width = (int) (getScreenWidth() * 0.15);
        paramsEtLogoutAfterTime.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        etLogoutAfterTimeH.setLayoutParams(paramsEtLogoutAfterTime);

        llLogoutAfterContainer.addView(tvLogoutAfterH);
        LinearLayout.LayoutParams paramsTvLogoutAfterH = (LinearLayout.LayoutParams) tvLogoutAfterH.getLayoutParams();
        paramsTvLogoutAfterH.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        paramsTvLogoutAfterH.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        tvLogoutAfterH.setLayoutParams(paramsTvLogoutAfterH);

        llLogoutAfterContainer.addView(etLogoutAfterTimeM);
        LinearLayout.LayoutParams paramsEtLogoutAfterTimeM = (LinearLayout.LayoutParams) etLogoutAfterTimeM.getLayoutParams();
        paramsEtLogoutAfterTimeM.width = (int) (getScreenWidth() * 0.15);
        paramsEtLogoutAfterTimeM.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        etLogoutAfterTimeM.setLayoutParams(paramsEtLogoutAfterTimeM);

        llLogoutAfterContainer.addView(tvLogoutAfterM);
        LinearLayout.LayoutParams paramsTvLogoutAfterM = (LinearLayout.LayoutParams) tvLogoutAfterM.getLayoutParams();
        paramsTvLogoutAfterM.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        paramsTvLogoutAfterM.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        tvLogoutAfterM.setLayoutParams(paramsTvLogoutAfterM);

        rlLogoutAfterContainer.addView(csbLogoutAfter);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(csbLogoutAfter, mContext);

        rlSendReserveMessageContainer.addView(tvSendReserveMessage);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayoutWithEightyPercent(tvSendReserveMessage, mContext);

        rlSendReserveMessageContainer.addView(csbSendReserveMessage);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(csbSendReserveMessage, mContext);

        rlOneTeeStartContainer.addView(tvOneTeeStart);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayoutWithEightyPercent(tvOneTeeStart, mContext);

        rlOneTeeStartContainer.addView(csbOneTeeStart);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(csbOneTeeStart, mContext);

        rlPaymentPatternContainer.addView(tvPaymentPatternName);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayoutWithEightyPercent(tvPaymentPatternName, mContext);

        rlPaymentPatternContainer.addView(tvPaymentPatternValue);

        ImageView icon = new ImageView(getBaseActivity());
        icon.setBackgroundResource(R.drawable.icon_black);
        rlPaymentPatternContainer.addView(icon);

        LayoutUtils.setRightArrow(icon,getActivity());

        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvPaymentPatternValue,80, mContext);

        rlSalesTaxExcludingContainer.addView(tvSalesTaxExcluding);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayoutWithEightyPercent(tvSalesTaxExcluding, mContext);

        rlSalesTaxExcludingContainer.addView(csbSalesTaxExcluding);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(csbSalesTaxExcluding, mContext);

        rlSalesTaxContainer.addView(tvSalesTaxName);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayoutWithEightyPercent(tvSalesTaxName, mContext);

        rlSalesTaxContainer.addView(etSalesTaxValue);
        RelativeLayout.LayoutParams paramsEtSalesTax = (RelativeLayout.LayoutParams) etSalesTaxValue.getLayoutParams();
        paramsEtSalesTax.width = getActualWidthOnThisDevice(300);
        paramsEtSalesTax.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsEtSalesTax.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsEtSalesTax.rightMargin = getActualWidthOnThisDevice(50);

        etSalesTaxValue.setLayoutParams(paramsEtSalesTax);


        rlSalesTaxContainer.addView(tvSalesPercent);
        RelativeLayout.LayoutParams paramsTvSalesPercent = (RelativeLayout.LayoutParams) tvSalesPercent.getLayoutParams();
        paramsTvSalesPercent.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvSalesPercent.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvSalesPercent.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvSalesPercent.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvSalesPercent.rightMargin = getActualWidthOnThisDevice(40);
        tvSalesPercent.setLayoutParams(paramsTvSalesPercent);

        rlShowSalesTaxContainer.addView(tvShowSalesTaxName);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayoutWithEightyPercent(tvShowSalesTaxName, mContext);

        rlShowSalesTaxContainer.addView(csbShowSalesTax);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(csbShowSalesTax, mContext);

        rlUnitContainer.addView(tvUnit);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvUnit, 20, getActivity());

        rlUnitContainer.addView(csbUnit);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(csbUnit, mContext);

        rlFirstDayContainer.addView(tvFirstDay);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvFirstDay, 20, getActivity());

        rlFirstDayContainer.addView(csbFirstDay);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(csbFirstDay, mContext);

        rlAddCaddieFeeContainer.addView(tvAddCaddieFeeDay);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayoutWithEightyPercent(tvAddCaddieFeeDay, getActivity());

        rlAddCaddieFeeContainer.addView(csbAddCaddieFeeDay);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(csbAddCaddieFeeDay, mContext);
    }

    @Override
    protected void setPropertyOfControls() {

        tvCityName.setText(R.string.do_edit_administration_city);
        tvCityName.setTextColor(getColor(R.color.common_black));

        tvTimeZoneName.setText(R.string.do_edit_administration_time_zone);
        tvTimeZoneName.setTextColor(getColor(R.color.common_black));

        tvDateFormatName.setText(R.string.do_edit_administration_date_format);
        tvDateFormatName.setTextColor(getColor(R.color.common_black));

        tvCurrencyName.setText(R.string.do_edit_administration_currency);
        tvCurrencyName.setTextColor(getColor(R.color.common_black));

        tvLogoutAfter.setText(R.string.do_edit_administration_logout_after);
        tvLogoutAfter.setTextColor(getColor(R.color.common_black));
        etLogoutAfterTimeH.setHint(Constants.DEFAULT_LOGOUT_TIME_HOUR);
        etLogoutAfterTimeH.setBackgroundResource(R.drawable.et_do_edit_logout_after_border);
        etLogoutAfterTimeH.setGravity(Gravity.END);
        etLogoutAfterTimeH.setSingleLine();
        etLogoutAfterTimeH.setInputType(InputType.TYPE_CLASS_NUMBER);

        tvLogoutAfterH.setText(R.string.do_edit_administration_logout_after_h);
        tvLogoutAfterH.setTextColor(getColor(R.color.common_black));
        etLogoutAfterTimeM.setHint(Constants.DEFAULT_LOGOUT_TIME_MIN);
        etLogoutAfterTimeM.setBackgroundResource(R.drawable.et_do_edit_logout_after_border);
        etLogoutAfterTimeM.setGravity(Gravity.END);
        etLogoutAfterTimeM.addTextChangedListener(new AppUtils.EditViewMinuteWatcher(etLogoutAfterTimeM, Constants.MAX_MINUTE));
        etLogoutAfterTimeM.setInputType(InputType.TYPE_CLASS_NUMBER);

        tvLogoutAfterM.setText(R.string.do_edit_administration_logout_after_m);
        tvLogoutAfterM.setTextColor(getColor(R.color.common_black));

        tvSendReserveMessage.setText(R.string.do_edit_administration_send_reserve_message);
        tvSendReserveMessage.setTextColor(getColor(R.color.common_black));

        tvOneTeeStart.setText(R.string.do_edit_administration__one_tee);
        tvOneTeeStart.setTextColor(getColor(R.color.common_black));

        tvPaymentPatternName.setText(R.string.do_edit_administration_payment_pattern);
        tvPaymentPatternName.setTextColor(getColor(R.color.common_black));

        tvPaymentPatternValue.setTextColor(getColor(R.color.common_gray));

        tvSalesTaxExcluding.setText(R.string.do_edit_administration_sales_tax_excluding);
        tvSalesTaxExcluding.setTextColor(getColor(R.color.common_black));

        tvSalesTaxName.setText(R.string.do_edit_administration_sales_tax);
        tvSalesTaxName.setTextColor(getColor(R.color.common_black));
        etSalesTaxValue.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        etSalesTaxValue.setSingleLine();
        etSalesTaxValue.setBackground(null);
        etSalesTaxValue.addTextChangedListener(new AppUtils.EditViewMoneyWatcher(etSalesTaxValue, Constants.MAX_TAX));

        tvSalesPercent.setText(Constants.STR_SYMBOL_PERCENT);

        tvShowSalesTaxName.setText(R.string.do_edit_administration_show_sales_tax);

        tvUnit.setText(R.string.do_edit_administration_unit);
        tvUnit.setTextColor(getColor(R.color.common_black));

        tvFirstDay.setText(R.string.do_edit_administration_first_day);
        tvFirstDay.setTextColor(getColor(R.color.common_black));

        tvAddCaddieFeeDay.setText(getString(R.string.do_edit_administration_add_caddie_fee));
        tvAddCaddieFeeDay.setTextColor(getColor(R.color.common_black));
    }

    @Override
    protected void configActionBar() {
        setNormalMenuActionBar();

        getTvLeftTitle().setText(R.string.title_administration);

        etLogoutAfterTimeH.setEnabled(isEdit);
        etLogoutAfterTimeM.setEnabled(isEdit);
        csbLogoutAfter.setEnabled(isEdit);
        csbSendReserveMessage.setEnabled(isEdit);
        csbOneTeeStart.setEnabled(isEdit);
        rlPaymentPatternContainer.setEnabled(isEdit);
        csbSalesTaxExcluding.setEnabled(isEdit);
        etSalesTaxValue.setEnabled(isEdit);
        csbShowSalesTax.setEnabled(isEdit);
        csbUnit.setEnabled(isEdit);
        csbFirstDay.setEnabled(isEdit);
        csbAddCaddieFeeDay.setEnabled(isEdit);
        if (csbShowSalesTax.isChecked()) {
            doShowTax();
        } else {
            doNotShowTax();
        }

        if (isEdit) {
            getTvRight().setBackground(null);
            getTvRight().setText(R.string.common_ok);
        } else {
            getTvRight().setBackgroundResource(R.drawable.administration_icon_edit);
            getTvRight().setText(Constants.STR_EMPTY);
        }
        getTvRight().setOnClickListener(listenerOk);
    }

    @Override
    protected void reShowWithBackValue() {
        if (getReturnValues() != null) {
            Bundle params = getReturnValues();
            if (params.containsKey("cityName") && params.containsKey("country") && params.containsKey("timeZone")) {
                tvCityValue.setText(params.getString("cityName") + Constants.STR_SPACE + params.getString("country"));
                tvTimeZoneValue.setText(params.getString("timeZone"));
            }
            if (params.containsKey("cityId")) {
                currentCityId = params.getString("cityId");
            }
            if (params.containsKey("currencyName") && params.containsKey("currencySymbol")) {
                tvCurrencyValue.setText(params.getString("currencyName") + Constants.STR_SPACE + params.getString("currencySymbol"));
            }
            if (params.containsKey("currencyId")) {
                currentCurrencyId = params.getInt("currencyId");
            }
        }
    }

    @Override
    protected void executeOnceOnCreate() {
        getAdministration();
    }

    private void getAdministration() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));

        HttpManager<JsonEditAdministration> hh = new HttpManager<JsonEditAdministration>(AdministrationEditFragment.this) {

            @Override
            public void onJsonSuccess(JsonEditAdministration jo) {
                initData(jo);
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        mQueue = Volley.newRequestQueue(getActivity());
        hh.start(getActivity(), ApiManager.HttpApi.AdministrationEdit, params);
    }

    private void initData(JsonEditAdministration jo) {

        currentCityId = String.valueOf(jo.getDataList().getDefaultCity().getCityId());
        currentCurrencyId = jo.getDataList().getDefaultCurrency().getDefaultCurrId();

        tvCityValue.setText(jo.getDataList().getDefaultCity().getCityName() + Constants.STR_COMMA + jo.getDataList().getDefaultCity().getCountry());

        tvTimeZoneValue.setText(jo.getDataList().getDefaultCity().getZoneCode());

        tvCurrencyValue.setText(jo.getDataList().getDefaultCurrency().getDefaultCurrency().replaceAll(Constants.STR_POUND_SIGN, " "));

        String[] logoutAfterTime = jo.getDataList().getLogoutAfter().split(Constants.TIME_HOUR_LOWERCASE);

        etLogoutAfterTimeH.setVisibility(View.GONE);
        etLogoutAfterTimeM.setVisibility(View.GONE);

        logoutAfterTimeM = String.valueOf((Integer.parseInt(logoutAfterTime[0])) * 60 + Integer.parseInt(logoutAfterTime[1]));

        beforeLogoutAfterTimeM = logoutAfterTimeM;

        tvLogoutAfterH.setText(Constants.STR_SPACE + logoutAfterTimeM + Constants.STR_SPACE);
        tvLogoutAfterH.setTextColor(getResources().getColor(R.color.common_blue));

        if (jo.getDataList().getLogoutAfterStatus() == Constants.SWITCH_RIGHT) {
            csbLogoutAfter.setChecked(true);
        } else {
            csbLogoutAfter.setChecked(false);
        }

        if (jo.getDataList().getSendReserveMessage() == Constants.SWITCH_RIGHT) {
            csbSendReserveMessage.setChecked(true);
        } else {
            csbSendReserveMessage.setChecked(false);
        }

        if (jo.getDataList().getDefaultOneTeeStartOnly() == Constants.SWITCH_RIGHT) {
            csbOneTeeStart.setChecked(true);
        } else {
            csbOneTeeStart.setChecked(false);
        }

        if (jo.getDataList().getDefaultPaymentPattern() != null) {
            int paymentPatternIndex = 1;
            try {
                paymentPatternIndex = Integer.valueOf(jo.getDataList().getDefaultPaymentPattern());
            } catch (NumberFormatException e) {
                Utils.log(e.getMessage());
            }

            paymentPattern = String.valueOf(paymentPatternIndex);

            switch (paymentPatternIndex) {
                case 1:
                    tvPaymentPatternValue.setText(R.string.tag_pay_cash);
                    break;
                case 2:
                    tvPaymentPatternValue.setText(R.string.tag_voucher);
                    break;
                case 3:
                    tvPaymentPatternValue.setText(R.string.tag_credit_card);
                    break;
                case 4:
                    tvPaymentPatternValue.setText(R.string.tag_third_party);
                    break;
                case 5:
                    tvPaymentPatternValue.setText(R.string.tag_balance_account);
                    break;
                case 6:
                    tvPaymentPatternValue.setText(R.string.tag_bank_transfer);
                    break;
                default:
                    tvPaymentPatternValue.setText(R.string.tag_pay_cash);
                    break;

            }
        } else {
            tvPaymentPatternValue.setText(R.string.tag_pay_cash);
        }

        if (jo.getDataList().getSalesTax() != null && !jo.getDataList().getSalesTax().equals(Constants.STR_0)) {
            etSalesTaxValue.setText(jo.getDataList().getSalesTax() + Constants.STR_EMPTY);
        } else {
            etSalesTaxValue.setText(Constants.DEFAULT_SALES_TAX);
        }

        if (jo.getDataList().getSalesTaxExcluding() == Constants.SWITCH_RIGHT) {
            csbSalesTaxExcluding.setChecked(true);
        } else {
            csbSalesTaxExcluding.setChecked(false);
        }

        if (StringUtils.isNotEmpty(jo.getDataList().getShowSalesTax()) && !jo.getDataList().getShowSalesTax().equals(Constants.STR_0)) {
            csbShowSalesTax.setChecked(true);
            doShowTax();
        } else {
            csbShowSalesTax.setChecked(false);
            doNotShowTax();
        }

        if (jo.getDataList().getUnit() != null && jo.getDataList().getUnit().equals(Constants.UNIT_METERS)) {
            csbUnit.setChecked(true);
        } else {
            csbUnit.setChecked(false);
        }

        if (jo.getDataList().getFirstDay() != null && jo.getDataList().getFirstDay().equals(Constants.FIRST_DAY_SUN)) {
            csbFirstDay.setChecked(true);
        } else {
            csbFirstDay.setChecked(false);
        }

        if (jo.getDataList().getCaddieFee() == Constants.SWITCH_RIGHT) {
            csbAddCaddieFeeDay.setChecked(true);
        } else {
            csbAddCaddieFeeDay.setChecked(false);
        }


        if (!TextUtils.isEmpty(jo.getDataList().getSalesTax())) {
            if (!TextUtils.isEmpty(jo.getDataList().getSalesTax())) {

            if (Constants.STR_0.equals(jo.getDataList().getSalesTax())){

                etSalesTaxValue.setText(Constants.STR_EMPTY);
            }else{
                etSalesTaxValue.setText(jo.getDataList().getSalesTax());
                 }


            }
        }
        etSalesTaxValue.setEnabled(false);

        List<JsonEditAdministration.Currency> currencyList = jo.getDataList().getCurrencyList();
        for (JsonEditAdministration.Currency currency : currencyList) {
            String[] currencyProperties = currency.getCurrency().split(Constants.STR_POUND_SIGN);
            currencyNameList.add(currencyProperties[0]);
            currencySymbolList.add(currencyProperties[1]);
            currencyIdList.add(currency.getCurrId());
        }

        if (StringUtils.isNotEmpty(jo.getDataList().getDateFormat())) {
            currentDateFormat = jo.getDataList().getDateFormat();
        }
    }

    private void putAdministration() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.ADMINISTRATION_CITY, currentCityId);
        params.put(ApiKey.ADMINISTRATION_TIME_ZONE, tvTimeZoneValue.getText().toString());
        params.put(ApiKey.ADMINISTRATION_CURRENCY, String.valueOf(currentCurrencyId));
        params.put(ApiKey.ADMINISTRATION_LOGOUT_AFTER, etLogoutAfterTimeH.getText().toString() + "h" + etLogoutAfterTimeM.getText().toString());
        params.put(ApiKey.ADMINISTRATION_LOGOUT_AFTER_STATUS, swLogoutAfterStatus);
        params.put(ApiKey.ADMINISTRATION_SEND_RESERVE_MESSAGE, swSendReserveMessageStatus);
        params.put(ApiKey.ADMINISTRATION_DEFAULT_ONE_TEE_START_ONLY, swOneTeeStartStatus);
        params.put(ApiKey.ADMINISTRATION_DEFAULT_PAYMENT_PATTERN, paymentPattern);
        params.put(ApiKey.ADMINISTRATION_SALES_TAX_EXCLUDING, swSalesTaxExcludingStatus);
        params.put(ApiKey.ADMINISTRATION_SALES_TAX, etSalesTaxValue.getValue());
        params.put(ApiKey.ADMINISTRATION_SHOW_SALES_TAX, swShowSalesTax);
        params.put(ApiKey.ADMINISTRATION_UNIT, swUnitStatus);
        params.put(ApiKey.ADMINISTRATION_FIRST_DAY, swFirstDayStatus);
        params.put(ApiKey.ADMINISTRATION_CADDIE_FEE, swAddCaddieFeeDayStatus);
        params.put(ApiKey.ADMINISTRATION_DATE_FORMAT, currentDateFormat);

        HttpManager<JsonEditAdministration> hh = new HttpManager<JsonEditAdministration>(AdministrationEditFragment.this) {

            @Override
            public void onJsonSuccess(JsonEditAdministration jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    JsonLogin loginInfo = AppUtils.getLoginInfo(getActivity());
                    loginInfo.setWeekFirstDay(swFirstDayStatus);
                    loginInfo.setCurrencyId(String.valueOf(currentCurrencyId));
                    loginInfo.setCurrencySymbol(AppUtils.getCurrentCurrencySymbolFromId(getActivity(),
                            String.valueOf(currentCurrencyId)));
                    loginInfo.setTimeZone(String.valueOf(tvTimeZoneValue.getText()));
                    loginInfo.setPaymentPattern(paymentPattern);
                    loginInfo.setSalesTax(etSalesTaxValue.getValue());
                    loginInfo.setSalesTaxExcluding(swSalesTaxExcludingStatus);
                    loginInfo.setShowSalesTax(swShowSalesTax);
                    loginInfo.setOneTeeOnly(swOneTeeStartStatus);
                    String unit = getString(R.string.do_edit_administration_meters);
                    if (Constants.STR_0.equals(swUnitStatus)) {
                        unit = getString(R.string.do_edit_administration_yards);
                    }
                    loginInfo.setUnit(unit);
                    Utils.save2SP(getActivity(), Constants.KEY_SP_LOGIN_INFO, loginInfo);
                    AppUtils.saveCurrentDateFormat(currentDateFormat, getActivity());
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.DoEditAdministration, params);
    }

    private void changeDateFormatValue(String currentDateFormat) {
        if (DateUtils.DATE_FORMAT_SHOW_MM_DD_YYYY.equals(currentDateFormat)) {
            tvDateFormatValue.setText(R.string.date_format_mm_dd_yyyy);
        } else if (DateUtils.DATE_FORMAT_SHOW_DD_MM_YYYY.equals(currentDateFormat)) {
            tvDateFormatValue.setText(R.string.date_format_dd_mm_yyyy);
        } else {
            tvDateFormatValue.setText(R.string.date_format_yyyy_mm_dd);
        }
    }
}
