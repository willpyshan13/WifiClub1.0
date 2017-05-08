/**
 * Project Name: itee
 * File Name:	 DepartmentFragment.java
 * Package Name: cn.situne.itee.fragment.staff
 * Date:		2015-03-03
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.staff;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
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
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeRedDeleteButton;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:DepartmentFragment <br/>
 * Function: 部分权限 <br/>
 * UI:  07-2
 * Date: 2015-03-03 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class DepartmentFragment extends BaseEditFragment {

    private RelativeLayout rlDepartment;
    private RelativeLayout rlAuthority;
    private RelativeLayout rlLevel;
    private LinearLayout rlDelete;

    private IteeTextView tvDepartmentKey;
    private IteeEditText etDepartmentValue;
    private IteeTextView tvAuthority;
    private ImageView ivARightIcon;
    private IteeTextView tvLevel;
    private ImageView ivLRightIcon;
    private IteeRedDeleteButton btDelete;
    private View.OnClickListener rightOkListener;
    private int departmentId;
    private String departmentName;
    private int courseId;
    private int type;

    private FragmentMode mode;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_staff_department;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt(TransKey.STAFF_DEPARTMENT_TYPE);
            mode = FragmentMode.valueOf(bundle.getInt(TransKey.COMMON_FRAGMENT_MODE));
            setFragmentMode(mode);
            departmentName = bundle.getString(TransKey.STAFF_DEPARTMENT_NAME, Constants.STR_EMPTY);
            departmentId = bundle.getInt(TransKey.STAFF_DEPARTMENT_ID);
            courseId = bundle.getInt(TransKey.STAFF_COURSE_ID);
        }

        rlDepartment = (RelativeLayout) rootView.findViewById(R.id.rl_department);
        rlAuthority = (RelativeLayout) rootView.findViewById(R.id.rl_authority);
        AppUtils.addTopSeparatorLine(rlAuthority, this);
        rlLevel = (RelativeLayout) rootView.findViewById(R.id.rl_level);

        AppUtils.addTopSeparatorLine(rlLevel, this);
        rlLevel.setVisibility(View.GONE);
        rlDelete = (LinearLayout) rootView.findViewById(R.id.rl_delete);

        tvDepartmentKey = new IteeTextView(getActivity());
        etDepartmentValue = new IteeEditText(getActivity());


        tvAuthority = new IteeTextView(getActivity());
        ivARightIcon = new ImageView(getActivity());
        tvLevel = new IteeTextView(getActivity());
        ivLRightIcon = new ImageView(getActivity());
        btDelete = new IteeRedDeleteButton(getActivity());
    }

    @Override
    protected void setDefaultValueOfControls() {

        LinearLayout.LayoutParams paramDepartment = (LinearLayout.LayoutParams) rlDepartment.getLayoutParams();
        paramDepartment.height = getActualHeightOnThisDevice(100);
        rlDepartment.setLayoutParams(paramDepartment);

        rlDepartment.addView(tvDepartmentKey);
        RelativeLayout.LayoutParams paramTvDepartmentKey = (RelativeLayout.LayoutParams) tvDepartmentKey.getLayoutParams();
        paramTvDepartmentKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvDepartmentKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvDepartmentKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvDepartmentKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvDepartmentKey.leftMargin = 40;
        tvDepartmentKey.setLayoutParams(paramTvDepartmentKey);


        rlDepartment.addView(etDepartmentValue);
        RelativeLayout.LayoutParams paramsTvDepartmentValue = (RelativeLayout.LayoutParams) etDepartmentValue.getLayoutParams();
        paramsTvDepartmentValue.width = (int) (getScreenWidth() * 0.50);
        paramsTvDepartmentValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvDepartmentValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvDepartmentValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvDepartmentValue.rightMargin = 40;
        etDepartmentValue.setLayoutParams(paramsTvDepartmentValue);

        LinearLayout.LayoutParams paramAuthority = (LinearLayout.LayoutParams) rlAuthority.getLayoutParams();
        paramAuthority.height = getActualHeightOnThisDevice(100);
        rlAuthority.setLayoutParams(paramAuthority);
        rlAuthority.setBackgroundResource(R.drawable.bg_linear_selector_color_white);//给该布局添加点击变色

        rlAuthority.addView(tvAuthority);
        RelativeLayout.LayoutParams paramTvAuthority = (RelativeLayout.LayoutParams) tvAuthority.getLayoutParams();
        paramTvAuthority.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvAuthority.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvAuthority.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvAuthority.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvAuthority.leftMargin = 40;
        tvAuthority.setLayoutParams(paramTvAuthority);

        rlAuthority.addView(ivARightIcon);
        RelativeLayout.LayoutParams paramsTvARightIcon = (RelativeLayout.LayoutParams) ivARightIcon.getLayoutParams();
        paramsTvARightIcon.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvARightIcon.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvARightIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvARightIcon.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvARightIcon.rightMargin = 40;
        ivARightIcon.setLayoutParams(paramsTvARightIcon);

        LinearLayout.LayoutParams paramLevel = (LinearLayout.LayoutParams) rlLevel.getLayoutParams();
        paramLevel.height = getActualHeightOnThisDevice(100);
        rlLevel.setLayoutParams(paramLevel);

        rlLevel.addView(tvLevel);
        RelativeLayout.LayoutParams paramTvLevel = (RelativeLayout.LayoutParams) tvLevel.getLayoutParams();
        paramTvLevel.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvLevel.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTvLevel.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramTvLevel.addRule(RelativeLayout.CENTER_VERTICAL);
        paramTvLevel.leftMargin = 40;
        tvLevel.setLayoutParams(paramTvLevel);

        rlLevel.addView(ivLRightIcon);
        RelativeLayout.LayoutParams paramsTvLRightIcon = (RelativeLayout.LayoutParams) ivLRightIcon.getLayoutParams();
        paramsTvLRightIcon.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvLRightIcon.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvLRightIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvLRightIcon.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvLRightIcon.rightMargin = 40;
        ivLRightIcon.setLayoutParams(paramsTvLRightIcon);

        rlDelete.setGravity(Gravity.CENTER);

        rlDelete.addView(btDelete);
        LinearLayout.LayoutParams paramBtDelete = (LinearLayout.LayoutParams) btDelete.getLayoutParams();
        paramBtDelete.width = AppUtils.getLargerButtonWidth(this);
        paramBtDelete.height = AppUtils.getLargerButtonHeight(this);
        paramBtDelete.topMargin = getActualHeightOnThisDevice(30);
        paramBtDelete.bottomMargin = getActualHeightOnThisDevice(30);


        btDelete.setLayoutParams(paramBtDelete);
        if (Constants.CADDIE_DEPARTMENT_ID == type) {
            rlLevel.setVisibility(View.VISIBLE);
            etDepartmentValue.setEnabled(false);
            AppUtils.addBottomSeparatorLine(rlLevel, this);
        } else {
            AppUtils.addBottomSeparatorLine(rlAuthority, this);
        }

        if (mode == FragmentMode.FragmentModeAdd || Constants.CADDIE_DEPARTMENT_ID == type) {
            btDelete.setVisibility(View.GONE);
        }
        etDepartmentValue.setText(departmentName);
    }


    @Override
    protected void setListenersOfControls() {

        rlAuthority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mode == FragmentMode.FragmentModeAdd && departmentId == 0) {
                    if (doCheck()) {
                        AppUtils.showSaveConfirm(DepartmentFragment.this, new AppUtils.SaveConfirmListener() {
                            @Override
                            public void onClickSave() {
                                postDepartmentData(false);
                            }
                        });
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, departmentId);
                    bundle.putInt(TransKey.STAFF_COURSE_ID, courseId);
                    push(AuthorityFragment.class, bundle);
                }

            }
        });

        rlLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mode == FragmentMode.FragmentModeAdd && departmentId == 0) {
                    if (doCheck()) {
                        AppUtils.showSaveConfirm(DepartmentFragment.this, new AppUtils.SaveConfirmListener() {
                            @Override
                            public void onClickSave() {

                                postDepartmentDataLevel(false);

                            }
                        });
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, departmentId);
                    bundle.putInt(TransKey.STAFF_COURSE_ID, courseId);
                    push(CaddiesLevelFragment.class, bundle);
                }

            }
        });


        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.showDeleteAlert(DepartmentFragment.this, new AppUtils.DeleteConfirmListener() {
                    @Override
                    public void onClickDelete() {
                        delDepartmentData();
                    }
                });
            }
        });


        rightOkListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == FragmentMode.FragmentModeAdd) {
                    postDepartmentData(true);
                } else {
                    putDepartmentData();
                }
            }
        };
    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {

        tvDepartmentKey.setText(R.string.staff_department);
        tvDepartmentKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvDepartmentKey.setTextColor(getColor(R.color.common_black));

        etDepartmentValue.setSingleLine();
        etDepartmentValue.setBackground(null);
        etDepartmentValue.setGravity(Gravity.END);
        etDepartmentValue.setHint(getString(R.string.staff_department));
        etDepartmentValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etDepartmentValue.setTextColor(getColor(R.color.common_black));

        tvAuthority.setText(R.string.staff_authority);
        tvAuthority.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvAuthority.setTextColor(getColor(R.color.common_black));

        ivARightIcon.setBackgroundResource(R.drawable.icon_right_arrow);

        tvLevel.setText(R.string.staff_level);
        tvLevel.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvLevel.setTextColor(getColor(R.color.common_black));

        ivLRightIcon.setBackgroundResource(R.drawable.icon_right_arrow);


    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.staff_department);
        if (Constants.CADDIE_DEPARTMENT_ID != type) {
            getTvRight().setBackground(null);
            getTvRight().setText(R.string.common_save);
            getTvRight().setOnClickListener(rightOkListener);
        }
    }

    //change
    private void putDepartmentData() {
        if (doCheck()) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.STAFF_DEPARTMENT_ID, String.valueOf(departmentId));
            params.put(ApiKey.STAFF_DEPARTMENT_NAME, etDepartmentValue.getText().toString());
            HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(DepartmentFragment.this) {

                @Override
                public void onJsonSuccess(BaseJsonObject jo) {
                    int returnCode = jo.getReturnCode();
                    String msg = jo.getReturnInfo();
                    Utils.showShortToast(getActivity(), msg);
                    if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                        getBaseActivity().doBackWithRefresh();
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
            Utils.hideKeyboard(getBaseActivity());
            hh.startPut(getActivity(), ApiManager.HttpApi.StaffDepartmentPostOrPut, params);

        }
    }

    private void delDepartmentData() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.STAFF_DEPARTMENT_ID, String.valueOf(departmentId));


        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(DepartmentFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {
                    getBaseActivity().doBackWithRefresh();
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
        Utils.hideKeyboard(getBaseActivity());
        hh.startDelete(getActivity(), ApiManager.HttpApi.StaffDepartmentPostOrPut, params);
    }


    //add
    private void postDepartmentData(final boolean isBack) {

        if (doCheck()) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.STAFF_DEPARTMENT_NAME, etDepartmentValue.getText().toString());
            HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(DepartmentFragment.this) {

                @Override
                public void onJsonSuccess(BaseJsonObject jo) {
                    int returnCode = jo.getReturnCode();
                    String msg = jo.getReturnInfo();

                    if (returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY) {
                        if (isBack) {
                            getBaseActivity().doBackWithRefresh();
                        } else {
                            Bundle bundle = new Bundle();
                            if (mode == FragmentMode.FragmentModeAdd) {
                                departmentId = Integer.parseInt(jo.getAddId());
                                bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, Integer.parseInt(jo.getAddId()));
                            } else {
                                bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, departmentId);
                            }
                            bundle.putInt(TransKey.STAFF_COURSE_ID, courseId);
                            push(AuthorityFragment.class, bundle);
                            mode = FragmentMode.FragmentModeEdit;
                            setFragmentMode(mode);
                        }
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
            Utils.hideKeyboard(getBaseActivity());
            hh.start(getActivity(), ApiManager.HttpApi.StaffDepartmentPostOrPut, params);

        }
    }


    private void postDepartmentDataLevel(final boolean isBack) {

        if (doCheck()) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.STAFF_DEPARTMENT_NAME, etDepartmentValue.getText().toString());
            HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(DepartmentFragment.this) {

                @Override
                public void onJsonSuccess(BaseJsonObject jo) {
                    int returnCode = jo.getReturnCode();
                    String msg = jo.getReturnInfo();

                    if (returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY) {
                        if (isBack) {
                            getBaseActivity().doBackWithRefresh();
                        }

                        Bundle bundle = new Bundle();
                        if (mode == FragmentMode.FragmentModeAdd) {
                            departmentId = Integer.parseInt(jo.getAddId());
                            bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, Integer.parseInt(jo.getAddId()));
                        } else {
                            bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, departmentId);

                        }
                        bundle.putInt(TransKey.STAFF_COURSE_ID, courseId);
                        push(CaddiesLevelFragment.class, bundle);

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
            Utils.hideKeyboard(getBaseActivity());
            hh.start(getActivity(), ApiManager.HttpApi.StaffDepartmentPostOrPut, params);

        }
    }

    private boolean doCheck() {
        boolean res = true;
        if (Utils.isStringNullOrEmpty(etDepartmentValue.getText().toString())) {
            Utils.showShortToast(getActivity(), MessageFormat.format(getString(R.string.common_must_be_filled_in), getString(R.string.staff_department)));
            res = false;
        }
        return res;
    }
}
