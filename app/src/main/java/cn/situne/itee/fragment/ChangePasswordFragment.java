/**
 * Project Name: itee
 * File Name:	 ChangePasswordFragment.java
 * Package Name: cn.situne.itee.fragment
 * Date:		 2015-01-20
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:ChangePasswordFragment <br/>
 * Function: change password. <br/>
 * UI:  02-2
 * Date: 2015-01-20 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ChangePasswordFragment extends BaseFragment {

    private RelativeLayout rlOldPasswordContainer;
    private RelativeLayout rlNewPasswordContainer;

    private IteeTextView tvOldPassword;
    private IteeTextView tvNewPassword;

    private IteeEditText etOldPassword;
    private IteeEditText etNewPassword;

    private AppUtils.NoDoubleClickListener listener;

    @Override
    protected void initControls(View rootView) {

        rlOldPasswordContainer = (RelativeLayout) rootView.findViewById(R.id.rl_old_password_container);
        rlNewPasswordContainer = (RelativeLayout) rootView.findViewById(R.id.rl_new_password_container);

        tvOldPassword = new IteeTextView(getActivity());
        tvNewPassword = new IteeTextView(getActivity());

        etOldPassword = new IteeEditText(this);
        etNewPassword = new IteeEditText(this);

    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        listener = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                if (doCheck()) {
                    Map<String, String> params = new HashMap<>();
                    params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
                    params.put(ApiKey.CHANGEPWD_OLD_PASSWORD, etOldPassword.getText().toString());
                    params.put(ApiKey.CHANGEPWD_NEW_PASSWORD, etNewPassword.getText().toString());
                    params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));

                    HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ChangePasswordFragment.this) {

                        @Override
                        public void onJsonSuccess(BaseJsonObject jo) {
                            String msg = jo.getReturnInfo();
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
                    hh.start(getActivity(), ApiManager.HttpApi.ChangePwd, params);
                }
            }
        };
    }

    @Override
    protected void setLayoutOfControls() {

        LinearLayout.LayoutParams layoutParamsRlOldPasswordContainer = new LinearLayout.LayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100));
        rlOldPasswordContainer.setLayoutParams(layoutParamsRlOldPasswordContainer);

        LinearLayout.LayoutParams layoutParamsRlNewPasswordContainer = new LinearLayout.LayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(100));
        rlNewPasswordContainer.setLayoutParams(layoutParamsRlNewPasswordContainer);

        rlOldPasswordContainer.addView(tvOldPassword);
        RelativeLayout.LayoutParams paramsTvOldPassword = (RelativeLayout.LayoutParams) tvOldPassword.getLayoutParams();
        paramsTvOldPassword.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvOldPassword.height = (int) (getScreenHeight() * 0.05);
        paramsTvOldPassword.topMargin = getActualWidthOnThisDevice(20);
        paramsTvOldPassword.leftMargin = getActualWidthOnThisDevice(20);
        paramsTvOldPassword.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvOldPassword.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        tvOldPassword.setLayoutParams(paramsTvOldPassword);

        rlOldPasswordContainer.addView(etOldPassword);
        RelativeLayout.LayoutParams paramsEtOldPassword = new RelativeLayout.LayoutParams((int) (getScreenWidth() * 0.6), getActualHeightOnThisDevice(100));
        paramsEtOldPassword.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsEtOldPassword.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        etOldPassword.setLayoutParams(paramsEtOldPassword);

        rlNewPasswordContainer.addView(tvNewPassword);
        RelativeLayout.LayoutParams paramsTvNewPassword = (RelativeLayout.LayoutParams) tvNewPassword.getLayoutParams();
        paramsTvNewPassword.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvNewPassword.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvNewPassword.topMargin = getActualWidthOnThisDevice(20);
        paramsTvNewPassword.leftMargin = getActualWidthOnThisDevice(20);
        paramsTvNewPassword.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvNewPassword.setLayoutParams(paramsTvNewPassword);

        rlNewPasswordContainer.addView(etNewPassword);
        RelativeLayout.LayoutParams paramsEtNewPassword = new RelativeLayout.LayoutParams((int) (getScreenWidth() * 0.6), getActualHeightOnThisDevice(100));
        paramsEtNewPassword.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsEtNewPassword.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        etNewPassword.setLayoutParams(paramsEtNewPassword);
    }

    @Override
    protected void setPropertyOfControls() {

        tvOldPassword.setText(R.string.old_password);
        tvOldPassword.setTextColor(getColor(R.color.common_black));
        tvNewPassword.setText(R.string.new_password);
        tvNewPassword.setTextColor(getColor(R.color.common_black));

        tvOldPassword.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvNewPassword.setTextSize(Constants.FONT_SIZE_NORMAL);
        etOldPassword.setTextSize(Constants.FONT_SIZE_NORMAL);
        etNewPassword.setTextSize(Constants.FONT_SIZE_NORMAL);

        etOldPassword.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etOldPassword.setBackground(null);
        etOldPassword.setTextColor(getColor(R.color.common_gray));
        etOldPassword.setPadding(0, 8, getActualWidthOnThisDevice(40), 0);

        etNewPassword.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etNewPassword.setTextColor(getColor(R.color.common_gray));
        etNewPassword.setBackground(null);
        etNewPassword.setPadding(0, 8, getActualWidthOnThisDevice(40), 0);
    }

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_change_password;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.change_password;
    }

    @Override
    protected void configActionBar() {
        setNormalMenuActionBar();

        getTvLeftTitle().setText(R.string.title_change_password);

        getTvRight().setText(R.string.common_save);
        getTvRight().setOnClickListener(listener);
    }

    private boolean doCheck() {
        boolean res = true;
        if (Utils.isStringNullOrEmpty(etOldPassword.getValue())) {
            res = false;
            Utils.showShortToast(getActivity(), MessageFormat.format(getString(R.string.common_must_be_filled_in), getString(R.string.old_password)));
        }
        if (Utils.isStringNullOrEmpty(etNewPassword.getValue())) {
            res = false;
            Utils.showShortToast(getActivity(), MessageFormat.format(getString(R.string.common_must_be_filled_in), getString(R.string.new_password)));
        }
        if (etOldPassword.getValue().length() < Constants.PASSWORD_MIN_SIZE || etOldPassword.getValue().length() > Constants.PASSWORD_MAX_SIZE) {
            res = false;
            Utils.showShortToast(getActivity(), R.string.common_password_size_message);
        }
        if (etNewPassword.getValue().length() < Constants.PASSWORD_MIN_SIZE || etNewPassword.getValue().length() > Constants.PASSWORD_MAX_SIZE) {
            res = false;
            Utils.showShortToast(getActivity(), R.string.common_password_size_message);
        }
        if (Utils.isStringNotNullOrEmpty(etOldPassword.getValue()) && Utils.isStringNotNullOrEmpty(etNewPassword.getValue())) {
            if (etOldPassword.getText().toString().equals(etNewPassword.getText().toString())) {
                res = false;
                Utils.showShortToast(getActivity(), "New password must not be same of old password");
            }
        }
        return res;
    }
}
