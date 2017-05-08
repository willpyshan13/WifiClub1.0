/**
 * Project Name: itee
 * File Name:	 AddProfileFragment.java
 * Package Name: cn.situne.itee.fragment.staff
 * Date:		 2015-03-03
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.staff;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.baoyz.actionsheet.ActionSheet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.activity.SelectPhotoActivity;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.ImageUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonCaddieLevelGet;
import cn.situne.itee.manager.jsonentity.JsonGeneralInfo;
import cn.situne.itee.manager.jsonentity.JsonNfcCheckCardForBookingNoGet;
import cn.situne.itee.manager.jsonentity.JsonReturnUserId;
import cn.situne.itee.manager.jsonentity.JsonUserDetailGet;
import cn.situne.itee.view.CheckSwitchButton;
import cn.situne.itee.view.IteeNetworkImageView;
import cn.situne.itee.view.IteeRedDeleteButton;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.PlayerInfoEmailItemView;
import cn.situne.itee.view.PlayerInfoPhoneItemView;
import cn.situne.itee.view.PurchasePlayersPopup;

import static android.view.View.OnClickListener;

/**
 * ClassName:AgentsAuthorityFragment <br/>
 * Function: add/edit/delete profile <br/>
 * Date: 2015-03-03 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class AddEditProfileFragment extends BaseEditFragment {

    private Bitmap isPhotoChange;
    private RelativeLayout rlPhotoContainer;
    private RelativeLayout rlNameContainer;
    private RelativeLayout rlDepartmentContainer;
    private RelativeLayout rlLevelContainer;
    private RelativeLayout rlNumberContainer;
    private RelativeLayout rlManagerContainer;

    private LinearLayout llPhoneContainer;
    private LinearLayout llEmailContainer;
    private RelativeLayout rlAddPhoneContainer;
    private RelativeLayout rlAddEmailContainer;

    private RelativeLayout rlUserNameContainer;
    private RelativeLayout rlPasswordContainer;
    private RelativeLayout rlAuthorityContainer;
    private LinearLayout rlDeleteContainer;

    private IteeTextView tvPhoto;
    private IteeNetworkImageView ivPhoto;

    private IteeTextView tvName;
    private EditText etNameValue;

    private IteeTextView tvDepartment;
    private EditText etDepartmentValue;

    private IteeTextView tvLevel;
    private IteeTextView tvLevelValue;

    private IteeTextView tvNumber;
    private EditText etNumberValue;

    private IteeTextView tvManager;
    private CheckSwitchButton swManager;

    private IteeTextView tvAddPhone;
    private TextView tvPhoneRequired;

    private IteeTextView tvAddEmail;
    private IteeTextView tvEmailRequired;
    private View viSeparatorEmailTop;
    private View viSeparatorEmailBottom;

    private IteeTextView tvUserName;
    private EditText etUserNameValue;

    private IteeTextView tvPassword;
    private EditText etPasswordValue;

    private IteeTextView tvAuthority;
    private ImageView ivAuthorityArrow;

    private IteeRedDeleteButton btnDelete;

    private ArrayList<JsonGeneralInfo.PhoneItem> phoneList;
    private ArrayList<JsonGeneralInfo.EmailItem> emailList;

    private ArrayList<JsonCaddieLevelGet.Level> caddieLevelList;

    private OnClickListener listenerRightOk;

    private Integer courseId;
    private Integer departmentId;
    private int levelId;
    private int type;
    private Integer passedUserId;
    private String departmentName;

    private String fromPage;

    private Integer memberId;
    private String photoUrl;
    private String attrId;
    private PlayerInfoEditFragment.OnDeleteClickListener onDeleteClickListener = new PlayerInfoEditFragment.OnDeleteClickListener() {
        @Override
        public void OnPhoneDeleteClick(String flag, JsonGeneralInfo.PhoneItem phoneItem) {
            phoneList.remove(phoneItem);
            if (phoneList.size() == 0) {
                tvPhoneRequired.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void OnEmailDeleteClick(String flag, JsonGeneralInfo.EmailItem emailItem) {
            emailList.remove(emailItem);
            if (emailList.size() == 0) {
                tvEmailRequired.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void OnAddressDeleteClick(String flag, JsonGeneralInfo.AddressItem addressItem) {

        }
    };

    @Override
    protected int getFragmentId() {
        Log.d("AddEditProfileFragment", "need page");
        return R.layout.fragment_staff_add_profile;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        attrId = Constants.STR_0;

        rlPhotoContainer = (RelativeLayout) rootView.findViewById(R.id.rl_add_profile_photo_container);
        rlNameContainer = (RelativeLayout) rootView.findViewById(R.id.rl_add_profile_name_container);
        rlDepartmentContainer = (RelativeLayout) rootView.findViewById(R.id.rl_add_profile_department_container);
        rlLevelContainer = (RelativeLayout) rootView.findViewById(R.id.rl_add_profile_level_container);
        rlNumberContainer = (RelativeLayout) rootView.findViewById(R.id.rl_add_profile_number_container);
        rlManagerContainer = (RelativeLayout) rootView.findViewById(R.id.rl_add_profile_manager_container);

        llPhoneContainer = (LinearLayout) rootView.findViewById(R.id.ll_add_profile_phone_container);
        llEmailContainer = (LinearLayout) rootView.findViewById(R.id.ll_add_profile_email_container);

        rlAddPhoneContainer = (RelativeLayout) rootView.findViewById(R.id.rl_add_profile_add_phone_container);
        rlAddEmailContainer = (RelativeLayout) rootView.findViewById(R.id.rl_add_profile_add_email_container);

        viSeparatorEmailTop = rootView.findViewById(R.id.vi_separator_email_top);
        viSeparatorEmailBottom = rootView.findViewById(R.id.vi_separator_email_bottom);

        rlUserNameContainer = (RelativeLayout) rootView.findViewById(R.id.rl_add_profile_username_container);
        rlPasswordContainer = (RelativeLayout) rootView.findViewById(R.id.rl_add_profile_password_container);
        rlAuthorityContainer = (RelativeLayout) rootView.findViewById(R.id.rl_add_profile_authority_container);
        rlDeleteContainer = (LinearLayout) rootView.findViewById(R.id.rl_add_profile_delete_container);

        tvPhoto = new IteeTextView(getActivity());
        ivPhoto = new IteeNetworkImageView(getActivity());

        tvName = new IteeTextView(getActivity());
        etNameValue = new EditText(getActivity());

        tvDepartment = new IteeTextView(getActivity());
        etDepartmentValue = new EditText(getActivity());

        tvLevel = new IteeTextView(getActivity());
        tvLevelValue = new IteeTextView(getActivity());

        tvNumber = new IteeTextView(getActivity());
        etNumberValue = new EditText(getActivity());

        tvManager = new IteeTextView(getActivity());
        swManager = new CheckSwitchButton(this);

        tvAddPhone = new IteeTextView(getActivity());
        tvPhoneRequired = new TextView(getActivity());

        tvAddEmail = new IteeTextView(getActivity());
        tvEmailRequired = new IteeTextView(getActivity());

        tvUserName = new IteeTextView(getActivity());
        etUserNameValue = new EditText(getActivity());

        tvPassword = new IteeTextView(getActivity());
        etPasswordValue = new EditText(getActivity());

        tvAuthority = new IteeTextView(getActivity());
        ivAuthorityArrow = new ImageView(getActivity());

        btnDelete = new IteeRedDeleteButton(getActivity());

        phoneList = new ArrayList<>();
        emailList = new ArrayList<>();
        caddieLevelList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            memberId = bundle.getInt(TransKey.COMMON_MEMBER_ID);
            photoUrl = bundle.getString(TransKey.COMMON_PHOTO_URL);

            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE);
            boolean isAdd = bundle.getBoolean(TransKey.STAFF_IS_ADD);
            if (isAdd) {
                setFragmentMode(FragmentMode.FragmentModeAdd);
            } else {
                setFragmentMode(FragmentMode.FragmentModeBrowse);
            }
            departmentId = bundle.getInt(TransKey.STAFF_DEPARTMENT_ID);
            departmentName = bundle.getString(TransKey.STAFF_DEPARTMENT_NAME);
            courseId = bundle.getInt(TransKey.STAFF_COURSE_ID);
            passedUserId = bundle.getInt(TransKey.STAFF_PASSED_USER_ID);
            type = bundle.getInt(TransKey.STAFF_DEPARTMENT_TYPE);
        }

    }

    @Override
    protected void setDefaultValueOfControls() {

        tvPhoto.setText(R.string.common_photo);
        tvName.setText(R.string.common_name);
        etNameValue.setHint(getString(R.string.common_name));

        tvDepartment.setText(R.string.staff_department);
        tvLevel.setText(R.string.staff_level);
        tvNumber.setText(R.string.staff_no);
        tvManager.setText(R.string.staff_manager);
        tvAddPhone.setText(R.string.staff_add_phone);
        tvPhoneRequired.setText(R.string.common_required);
        tvAddEmail.setText(R.string.staff_add_email);
        tvEmailRequired.setText(R.string.common_required);
        tvEmailRequired.setVisibility(View.INVISIBLE);
        tvUserName.setText(R.string.common_username);

        etUserNameValue.setHint(getString(R.string.common_username));
        tvPassword.setText(R.string.common_password);
        etPasswordValue.setHint(getString(R.string.common_password));
        ivAuthorityArrow.setImageResource(R.drawable.icon_right_arrow);


        etDepartmentValue.setText(departmentName);
    }

    @Override
    protected void setListenersOfControls() {


        //fix by syb.
        etUserNameValue.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        if (" ".equals(source.toString())) {
                            return "";
                        }
                        return null;
                    }
                }
        });
        //fix by syb.
        etPasswordValue.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        if (" ".equals(source.toString())) {
                            return "";
                        }
                        return null;
                    }
                }
        });


        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SelectPhotoActivity.class);
                startActivityForResult(intent, SelectPhotoActivity.REQUEST_CODE_SET);
            }
        });

        rlAddPhoneContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    int childCount = llPhoneContainer.getChildCount();
                    String tag;
                    switch (childCount) {
                        case 0:
                            tag = getString(R.string.tag_home);
                            break;
                        case 1:
                            tag = getString(R.string.tag_work);
                            break;
                        case 2:
                            tag = getString(R.string.tag_mobile);
                            break;
                        case 3:
                            tag = getString(R.string.tag_other);
                            break;
                        default:
                            tag = getString(R.string.tag_home);
                            break;
                    }

                    JsonGeneralInfo.PhoneItem addressItem = new JsonGeneralInfo.PhoneItem();
                    addressItem.setTag(tag);
                    phoneList.add(addressItem);
                    addPhoneView(addressItem);
                }
            }
        });

        rlAddEmailContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    int childCount = llPhoneContainer.getChildCount();
                    String tag;
                    switch (childCount) {
                        case 0:
                            tag = getString(R.string.tag_home);
                            break;
                        case 1:
                            tag = getString(R.string.tag_work);
                            break;
                        case 2:
                            tag = getString(R.string.tag_mobile);
                            break;
                        case 3:
                            tag = getString(R.string.tag_other);
                            break;
                        default:
                            tag = getString(R.string.tag_home);
                            break;
                    }

                    JsonGeneralInfo.EmailItem addressItem = new JsonGeneralInfo.EmailItem();
                    addressItem.setTag(tag);
                    emailList.add(addressItem);
                    addEmailView(addressItem);
                }
            }
        });

        listenerRightOk = new OnClickListener() {

            @Override
            public void onClick(View v) {
                rlAuthorityContainer.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
                if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
                    Utils.hideKeyboard(getActivity());
                    postProfile();
                } else {
                    if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                        Utils.hideKeyboard(getActivity());
                        putProfile();
                    } else {
                        getTvRight().setText(R.string.common_save);
                        getTvRight().setBackground(null);
                        setFragmentMode(FragmentMode.FragmentModeEdit);

                        rlAddEmailContainer.setVisibility(View.VISIBLE);
                        viSeparatorEmailTop.setVisibility(View.VISIBLE);
                        viSeparatorEmailBottom.setVisibility(View.VISIBLE);
                        rlAddPhoneContainer.setVisibility(View.VISIBLE);
                        rlDeleteContainer.setVisibility(View.VISIBLE);

                        change2EditState();
                    }
                }
            }
        };

        btnDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                    AppUtils.showDeleteAlert(AddEditProfileFragment.this, new AppUtils.DeleteConfirmListener() {
                        @Override
                        public void onClickDelete() {
                            deleteProfile();
                        }
                    });
                }
            }
        });


        rlLevelContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    if (Utils.isListNotNullOrEmpty(caddieLevelList)) {
                        String[] otherLevels = null;
                        if (caddieLevelList.size() > 1) {
                            otherLevels = new String[caddieLevelList.size() - 1];
                            for (int i = 0; i < caddieLevelList.size() - 1; i++) {
                                otherLevels[i] = caddieLevelList.get(i).getLevName();
                            }
                        }
                        final String lastLevel = caddieLevelList.get(caddieLevelList.size() - 1).getLevName();
                        getBaseActivity().setTheme(com.baoyz.actionsheet.R.style.ActionSheetStyleIOS7);
                        ActionSheet.createBuilder(getActivity(), getFragmentManager())
                                .setCancelButtonTitle(lastLevel)
                                .setOtherButtonTitles(otherLevels)
                                .setCancelableOnTouchOutside(true).setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                            }

                            @Override
                            public void onDismissWithCancelButton(ActionSheet actionSheet) {
                                levelId = caddieLevelList.get(caddieLevelList.size() - 1).getLevId();
                                tvLevelValue.setText(lastLevel);
                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                                levelId = caddieLevelList.get(index).getLevId();
                                tvLevelValue.setText(caddieLevelList.get(index).getLevName());
                            }
                        }).show();
                    }
                }
            }
        });

        rlAuthorityContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.STAFF_PASSED_USER_ID, passedUserId);
                    bundle.putInt(TransKey.STAFF_DEPARTMENT_ID, departmentId);
                    push(AuthorityFragment.class, bundle);
                }
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {
        LinearLayout.LayoutParams paramsPhoto = (LinearLayout.LayoutParams) rlPhotoContainer.getLayoutParams();
        paramsPhoto.height = getActualHeightOnThisDevice(150);
        rlPhotoContainer.setLayoutParams(paramsPhoto);

        LinearLayout.LayoutParams paramsName = (LinearLayout.LayoutParams) rlNameContainer.getLayoutParams();
        paramsName.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        rlNameContainer.setLayoutParams(paramsName);

        LinearLayout.LayoutParams paramsDepartment = (LinearLayout.LayoutParams) rlDepartmentContainer.getLayoutParams();
        paramsDepartment.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        rlDepartmentContainer.setLayoutParams(paramsDepartment);

        LinearLayout.LayoutParams paramsLevel = (LinearLayout.LayoutParams) rlLevelContainer.getLayoutParams();
        paramsLevel.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        rlLevelContainer.setLayoutParams(paramsLevel);

        LinearLayout.LayoutParams paramsNumber = (LinearLayout.LayoutParams) rlNumberContainer.getLayoutParams();
        paramsNumber.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        rlNumberContainer.setLayoutParams(paramsNumber);

        LinearLayout.LayoutParams paramsManager = (LinearLayout.LayoutParams) rlManagerContainer.getLayoutParams();
        paramsManager.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        rlManagerContainer.setLayoutParams(paramsManager);

        LinearLayout.LayoutParams paramsAddPhone = (LinearLayout.LayoutParams) rlAddPhoneContainer.getLayoutParams();
        paramsAddPhone.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        rlAddPhoneContainer.setLayoutParams(paramsAddPhone);

        LinearLayout.LayoutParams paramsAddEmail = (LinearLayout.LayoutParams) rlAddEmailContainer.getLayoutParams();
        paramsAddEmail.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        rlAddEmailContainer.setLayoutParams(paramsAddEmail);

        LinearLayout.LayoutParams paramsUserName = (LinearLayout.LayoutParams) rlUserNameContainer.getLayoutParams();
        paramsUserName.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        paramsUserName.topMargin = getActualHeightOnThisDevice(25);
        rlUserNameContainer.setLayoutParams(paramsUserName);


        LinearLayout.LayoutParams paramsPassword = (LinearLayout.LayoutParams) rlPasswordContainer.getLayoutParams();
        paramsPassword.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        rlPasswordContainer.setLayoutParams(paramsPassword);

        LinearLayout.LayoutParams paramsAuthority = (LinearLayout.LayoutParams) rlAuthorityContainer.getLayoutParams();
        paramsAuthority.height = getActualHeightOnThisDevice(Constants.ROW_HEIGHT);
        rlAuthorityContainer.setLayoutParams(paramsAuthority);

        LinearLayout.LayoutParams paramsDelete = (LinearLayout.LayoutParams) rlDeleteContainer.getLayoutParams();
        paramsDelete.height = WRAP_CONTENT;

        rlDeleteContainer.setLayoutParams(paramsDelete);
        rlDeleteContainer.setGravity(Gravity.CENTER);

        rlPhotoContainer.addView(tvPhoto);
        RelativeLayout.LayoutParams paramsTvPhoto = (RelativeLayout.LayoutParams) tvPhoto.getLayoutParams();
        paramsTvPhoto.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPhoto.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPhoto.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvPhoto.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvPhoto.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvPhoto.setLayoutParams(paramsTvPhoto);

        rlPhotoContainer.addView(ivPhoto);
        RelativeLayout.LayoutParams paramsIvPhoto = (RelativeLayout.LayoutParams) ivPhoto.getLayoutParams();
        paramsIvPhoto.width = getActualWidthOnThisDevice(156);
        paramsIvPhoto.height = getActualHeightOnThisDevice(116);
        paramsIvPhoto.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvPhoto.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsIvPhoto.rightMargin = getActualWidthOnThisDevice(40);
        ivPhoto.setLayoutParams(paramsIvPhoto);


        rlNameContainer.addView(tvName);
        RelativeLayout.LayoutParams paramsTvName = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
        paramsTvName.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvName.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvName.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvName.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvName.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvName.setLayoutParams(paramsTvName);

        rlNameContainer.addView(etNameValue);
        RelativeLayout.LayoutParams paramsTvNameValue = (RelativeLayout.LayoutParams) etNameValue.getLayoutParams();
        paramsTvNameValue.width = (int) (getScreenWidth() * 0.5);
        paramsTvNameValue.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsTvNameValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvNameValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvNameValue.rightMargin = getActualWidthOnThisDevice(40);
        paramsTvNameValue.topMargin = getActualHeightOnThisDevice(25);
        etNameValue.setLayoutParams(paramsTvNameValue);


        rlDepartmentContainer.addView(tvDepartment);
        RelativeLayout.LayoutParams paramsTvDepartment = (RelativeLayout.LayoutParams) tvDepartment.getLayoutParams();
        paramsTvDepartment.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvDepartment.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvDepartment.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvDepartment.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvDepartment.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvDepartment.setLayoutParams(paramsTvDepartment);

        rlDepartmentContainer.addView(etDepartmentValue);
        RelativeLayout.LayoutParams paramsTvDepartmentValue = (RelativeLayout.LayoutParams) etDepartmentValue.getLayoutParams();
        paramsTvDepartmentValue.width = (int) (getScreenWidth() * 0.5);
        paramsTvDepartmentValue.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsTvDepartmentValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvDepartmentValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvDepartmentValue.rightMargin = getActualWidthOnThisDevice(40);
        etDepartmentValue.setLayoutParams(paramsTvDepartmentValue);


        rlLevelContainer.addView(tvLevel);
        RelativeLayout.LayoutParams paramsTvLevel = (RelativeLayout.LayoutParams) tvLevel.getLayoutParams();
        paramsTvLevel.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvLevel.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvLevel.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvLevel.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvLevel.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvLevel.setLayoutParams(paramsTvLevel);

        rlLevelContainer.addView(tvLevelValue);
        RelativeLayout.LayoutParams paramsTvLevelValue = (RelativeLayout.LayoutParams) tvLevelValue.getLayoutParams();
        paramsTvLevelValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvLevelValue.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsTvLevelValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvLevelValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvLevelValue.rightMargin = getActualWidthOnThisDevice(40);
        tvLevelValue.setLayoutParams(paramsTvLevelValue);


        rlNumberContainer.addView(tvNumber);
        RelativeLayout.LayoutParams paramsTvNumber = (RelativeLayout.LayoutParams) tvNumber.getLayoutParams();
        paramsTvNumber.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvNumber.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvNumber.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvNumber.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvNumber.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvNumber.setLayoutParams(paramsTvNumber);

        rlNumberContainer.addView(etNumberValue);
        RelativeLayout.LayoutParams paramsTvNumberValue = (RelativeLayout.LayoutParams) etNumberValue.getLayoutParams();
        paramsTvNumberValue.width = (int) (getScreenWidth() * 0.5);
        paramsTvNumberValue.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsTvNumberValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvNumberValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvNumberValue.rightMargin = getActualWidthOnThisDevice(40);
        etNumberValue.setLayoutParams(paramsTvNumberValue);

//        rlNumberContainer.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Utils.showShortToast(getBaseActivity(),"haopahoaaho");
//
//                Map<String, String> params = new HashMap<>();
//                params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
//
//
//                HttpManager<JsonNfcCheckCardForBookingNoGet> hh = new HttpManager<JsonNfcCheckCardForBookingNoGet>(this) {
//
//                    @Override
//                    public void onJsonSuccess(JsonNfcCheckCardForBookingNoGet jo) {
//                        int returnCode = jo.getReturnCode();
//                        String msg = jo.getReturnInfo();
//                        if (returnCode == Constants.NFC_RETURN_CODE_20602) {
//
//                        } else {
//                            Utils.showShortToast(getActivity(), msg);
//                        }
//                    }
//
//                    @Override
//                    public void onJsonError(VolleyError error) {
//
//                        NetworkResponse response = error.networkResponse;
//                        if (response != null) {
//                            Utils.debug(response.toString());
//                        }
//                        Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
//                    }
//                };
//                hh.startGet(getActivity(), ApiManager.HttpApi.NfcBagCardCheckGet, params);
//
//
//            }
//        });


        rlManagerContainer.addView(tvManager);
        RelativeLayout.LayoutParams paramsTvManager = (RelativeLayout.LayoutParams) tvManager.getLayoutParams();
        paramsTvManager.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvManager.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvManager.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvManager.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvManager.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvManager.setLayoutParams(paramsTvManager);

        rlManagerContainer.addView(swManager);
        RelativeLayout.LayoutParams paramsSwManager = (RelativeLayout.LayoutParams) swManager.getLayoutParams();
        paramsSwManager.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsSwManager.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsSwManager.rightMargin = getActualWidthOnThisDevice(40);
        paramsSwManager.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsSwManager.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        swManager.setLayoutParams(paramsSwManager);

        rlAddPhoneContainer.addView(tvAddPhone);
        RelativeLayout.LayoutParams paramsTvAddPhone = (RelativeLayout.LayoutParams) tvAddPhone.getLayoutParams();
        paramsTvAddPhone.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddPhone.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddPhone.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvAddPhone.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvAddPhone.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvAddPhone.setLayoutParams(paramsTvAddPhone);

        rlAddPhoneContainer.addView(tvPhoneRequired);
        RelativeLayout.LayoutParams paramsTvPhoneRequired = (RelativeLayout.LayoutParams) tvPhoneRequired.getLayoutParams();
        paramsTvPhoneRequired.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPhoneRequired.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPhoneRequired.rightMargin = getActualWidthOnThisDevice(40);
        paramsTvPhoneRequired.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvPhoneRequired.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        tvPhoneRequired.setLayoutParams(paramsTvPhoneRequired);


        rlAddEmailContainer.addView(tvAddEmail);
        RelativeLayout.LayoutParams paramsTvAddEmail = (RelativeLayout.LayoutParams) tvAddEmail.getLayoutParams();
        paramsTvAddEmail.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddEmail.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddEmail.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvAddEmail.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvAddEmail.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvAddEmail.setLayoutParams(paramsTvAddEmail);

        rlAddEmailContainer.addView(tvEmailRequired);
        RelativeLayout.LayoutParams paramsTvEmailRequired = (RelativeLayout.LayoutParams) tvEmailRequired.getLayoutParams();
        paramsTvEmailRequired.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvEmailRequired.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvEmailRequired.rightMargin = getActualWidthOnThisDevice(40);
        paramsTvEmailRequired.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvEmailRequired.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        tvEmailRequired.setLayoutParams(paramsTvEmailRequired);


        rlUserNameContainer.addView(tvUserName);
        RelativeLayout.LayoutParams paramsTvUserName = (RelativeLayout.LayoutParams) tvUserName.getLayoutParams();
        paramsTvUserName.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvUserName.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvUserName.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvUserName.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvUserName.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvUserName.setLayoutParams(paramsTvUserName);

        rlUserNameContainer.addView(etUserNameValue);
        RelativeLayout.LayoutParams paramsTvUserNameValue = (RelativeLayout.LayoutParams) etUserNameValue.getLayoutParams();
        paramsTvUserNameValue.width = (int) (getScreenWidth() * 0.5);
        paramsTvUserNameValue.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsTvUserNameValue.rightMargin = getActualWidthOnThisDevice(40);
        paramsTvUserNameValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvUserNameValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        etUserNameValue.setLayoutParams(paramsTvUserNameValue);


        rlPasswordContainer.addView(tvPassword);
        RelativeLayout.LayoutParams paramsTvPassword = (RelativeLayout.LayoutParams) tvPassword.getLayoutParams();
        paramsTvPassword.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPassword.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPassword.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvPassword.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvPassword.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvPassword.setLayoutParams(paramsTvPassword);

        rlPasswordContainer.addView(etPasswordValue);
        RelativeLayout.LayoutParams paramsTvPasswordValue = (RelativeLayout.LayoutParams) etPasswordValue.getLayoutParams();
        paramsTvPasswordValue.width = (int) (getScreenWidth() * 0.5);
        paramsTvPasswordValue.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsTvPasswordValue.rightMargin = getActualWidthOnThisDevice(40);
        paramsTvPasswordValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvPasswordValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        etPasswordValue.setLayoutParams(paramsTvPasswordValue);

        rlAuthorityContainer.addView(tvAuthority);
        RelativeLayout.LayoutParams paramsTvAuthority = (RelativeLayout.LayoutParams) tvAuthority.getLayoutParams();
        paramsTvAuthority.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAuthority.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAuthority.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvAuthority.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvAuthority.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvAuthority.setLayoutParams(paramsTvAuthority);

        rlAuthorityContainer.addView(ivAuthorityArrow);
        RelativeLayout.LayoutParams paramsIvAuthorityArrow = (RelativeLayout.LayoutParams) ivAuthorityArrow.getLayoutParams();
        paramsIvAuthorityArrow.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvAuthorityArrow.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvAuthorityArrow.rightMargin = getActualWidthOnThisDevice(40);
        paramsIvAuthorityArrow.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsIvAuthorityArrow.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        ivAuthorityArrow.setLayoutParams(paramsIvAuthorityArrow);


        rlDeleteContainer.addView(btnDelete);
        LinearLayout.LayoutParams paramsBtnDelete = (LinearLayout.LayoutParams) btnDelete.getLayoutParams();
        paramsBtnDelete.width = AppUtils.getLargerButtonWidth(this);
        paramsBtnDelete.height = AppUtils.getLargerButtonHeight(this);
        paramsBtnDelete.topMargin = getActualHeightOnThisDevice(30);
        paramsBtnDelete.bottomMargin = getActualHeightOnThisDevice(30);

        btnDelete.setLayoutParams(paramsBtnDelete);

        llPhoneContainer.setBackgroundColor(getColor(R.color.common_white));
        llEmailContainer.setBackgroundColor(getColor(R.color.common_white));

        int leftPadding = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        int rightPadding = DensityUtil.getActualWidthOnThisDevice(20, mContext);
        llPhoneContainer.setPadding(leftPadding, 0, rightPadding, 0);
        llEmailContainer.setPadding(leftPadding, 0, rightPadding, 0);

        if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
            rlAddEmailContainer.setVisibility(View.GONE);
            rlAddPhoneContainer.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setPropertyOfControls() {

        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            rlDeleteContainer.setVisibility(View.GONE);
        } else {
            ivPhoto.setEnabled(false);
            etNameValue.setEnabled(false);
            etDepartmentValue.setEnabled(false);
            tvLevelValue.setEnabled(false);
            etNumberValue.setEnabled(false);
            etUserNameValue.setEnabled(false);
            etPasswordValue.setEnabled(false);
            swManager.setEnabled(false);
        }

        etNameValue.setSingleLine();
        etDepartmentValue.setSingleLine();
        etNumberValue.setSingleLine();
        etUserNameValue.setSingleLine();
        etPasswordValue.setSingleLine();

        etNameValue.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        etDepartmentValue.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        etNumberValue.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        etUserNameValue.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        etPasswordValue.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);


        etNameValue.setBackground(null);
        etDepartmentValue.setBackground(null);
        tvLevelValue.setBackground(null);
        etNumberValue.setBackground(null);
        etUserNameValue.setBackground(null);
        etPasswordValue.setBackground(null);

        etNameValue.setPadding(0, 10, 0, 5);
        etDepartmentValue.setPadding(0, 10, 0, 5);
        tvLevelValue.setPadding(0, 10, 0, 5);
        etNumberValue.setPadding(0, 10, 0, 5);
        etUserNameValue.setPadding(0, 10, 0, 5);
        etPasswordValue.setPadding(0, 10, 0, 5);

        etNameValue.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        etDepartmentValue.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        etUserNameValue.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        etPasswordValue.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        etDepartmentValue.setEnabled(false);

        if (Constants.CADDIE_DEPARTMENT_ID != type) {
            rlLevelContainer.setVisibility(View.GONE);
            rlNumberContainer.setVisibility(View.GONE);
        }

        etNameValue.setTextColor(getColor(R.color.common_deep_gray));
        etDepartmentValue.setTextColor(getColor(R.color.common_deep_gray));
        tvLevelValue.setTextColor(getColor(R.color.common_deep_gray));
        etNumberValue.setTextColor(getColor(R.color.common_deep_gray));
        etUserNameValue.setTextColor(getColor(R.color.common_deep_gray));
        etPasswordValue.setTextColor(getColor(R.color.common_deep_gray));

        tvPhoto.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvPhoto.setTextColor(getResources().getColor(R.color.common_black));

        if (Utils.isStringNotNullOrEmpty(photoUrl)) {
            AppUtils.showNetworkImage(ivPhoto, photoUrl);
        } else {
            AppUtils.showNetworkImage(ivPhoto, Constants.PHOTO_DEFAULT_URL);
        }


        tvName.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvName.setTextColor(getResources().getColor(R.color.common_black));

        etNameValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etNameValue.setTextColor(getResources().getColor(R.color.common_gray));

        tvDepartment.setText(R.string.staff_department);
        tvDepartment.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvDepartment.setTextColor(getResources().getColor(R.color.common_black));

        etDepartmentValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etDepartmentValue.setTextColor(getResources().getColor(R.color.common_gray));

        tvLevel.setText(R.string.staff_level);
        tvLevel.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvLevel.setTextColor(getResources().getColor(R.color.common_black));

        tvLevelValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvLevelValue.setTextColor(getResources().getColor(R.color.common_gray));

        tvNumber.setText(R.string.staff_no);
        tvNumber.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvNumber.setTextColor(getResources().getColor(R.color.common_black));

        etNumberValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etNumberValue.addTextChangedListener(new AppUtils.EditViewIntegerWatcher(etNumberValue, Constants.MAX_CADDIE_NUMBER_VALUE));
        etNumberValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        tvLevelValue.setTextColor(getResources().getColor(R.color.common_gray));


        tvManager.setText(R.string.staff_manager);
        tvManager.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvManager.setTextColor(getResources().getColor(R.color.common_black));

        tvAddPhone.setText(R.string.staff_add_phone);
        tvAddPhone.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvAddPhone.setTextColor(getResources().getColor(R.color.common_blue));

        tvPhoneRequired.setText(R.string.common_required);
        tvPhoneRequired.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvPhoneRequired.setTextColor(getResources().getColor(R.color.common_red));

        tvAddEmail.setText(R.string.staff_add_email);
        tvAddEmail.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvAddEmail.setTextColor(getResources().getColor(R.color.common_blue));

        tvEmailRequired.setText(R.string.common_required);
        tvEmailRequired.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvEmailRequired.setTextColor(getResources().getColor(R.color.common_red));

        tvUserName.setText(R.string.common_username);
        tvUserName.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvUserName.setTextColor(getResources().getColor(R.color.common_black));

        etUserNameValue.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvPassword.setText(R.string.common_password);
        tvPassword.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvPassword.setTextColor(getResources().getColor(R.color.common_black));

        etPasswordValue.setTextSize(Constants.FONT_SIZE_NORMAL);

        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            String textAuthority = getString(R.string.staff_authority) + getString(R.string.common_operate_after_adding);
            SpannableStringBuilder ssb = new SpannableStringBuilder(textAuthority);
            ssb.setSpan(new RelativeSizeSpan(1.f), 0, getString(R.string.staff_authority).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new RelativeSizeSpan(0.8f), getString(R.string.staff_authority).length(), textAuthority.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvAuthority.setText(textAuthority);
        } else {
            tvAuthority.setText(R.string.staff_authority);
            rlDeleteContainer.setVisibility(View.INVISIBLE);
        }
        tvAuthority.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvAuthority.setTextColor(getResources().getColor(R.color.common_black));

        ivAuthorityArrow.setImageResource(R.drawable.icon_right_arrow);


        rlPhotoContainer.setBackgroundColor(Color.WHITE);
        rlNameContainer.setBackgroundColor(Color.WHITE);
        rlDepartmentContainer.setBackgroundColor(Color.WHITE);
        rlLevelContainer.setBackgroundColor(Color.WHITE);
        rlNumberContainer.setBackgroundColor(Color.WHITE);
        rlManagerContainer.setBackgroundColor(Color.WHITE);

        llPhoneContainer.setBackgroundColor(Color.WHITE);
        llEmailContainer.setBackgroundColor(Color.WHITE);
        rlAddPhoneContainer.setBackgroundColor(Color.WHITE);
        rlAddEmailContainer.setBackgroundColor(Color.WHITE);

        rlUserNameContainer.setBackgroundColor(Color.WHITE);
        rlPasswordContainer.setBackgroundColor(Color.WHITE);
        rlAuthorityContainer.setBackgroundColor(Color.WHITE);
        rlDeleteContainer.setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            getTvLeftTitle().setText(R.string.player_info_edit_add_gender_info);
            getTvRight().setBackground(null);
            getTvRight().setText(R.string.common_save);
        } else {
            getTvLeftTitle().setText(R.string.common_edit);
            if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                getTvRight().setBackground(null);
                getTvRight().setText(R.string.common_save);
                rlAddEmailContainer.setVisibility(View.VISIBLE);
                viSeparatorEmailTop.setVisibility(View.VISIBLE);
                viSeparatorEmailBottom.setVisibility(View.VISIBLE);
                rlAddPhoneContainer.setVisibility(View.VISIBLE);
                rlDeleteContainer.setVisibility(View.VISIBLE);
            } else {
                getTvRight().setBackgroundResource(R.drawable.administration_icon_edit);
                getTvRight().setText(Constants.STR_EMPTY);

                rlDeleteContainer.setVisibility(View.INVISIBLE);
            }
        }

        getTvRight().setOnClickListener(listenerRightOk);
    }

    private void postProfile() {
        if (doCheck()) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.STAFF_USER_NAME, etNameValue.getText().toString());
            params.put(ApiKey.COMMON_COURES_ID, String.valueOf(courseId));
            params.put(ApiKey.STAFF_DEPARTMENT_ID, String.valueOf(departmentId));
            params.put(ApiKey.STAFF_MANAGER, String.valueOf(swManager.isChecked() ? Constants.STR_1 : Constants.STR_0));
            params.put(ApiKey.STAFF_ACCOUNT, etUserNameValue.getText().toString());
            params.put(ApiKey.STAFF_PASSWORD, etPasswordValue.getText().toString());
            params.put(ApiKey.STAFF_PHONE_LIST, getPhoneString());
            params.put(ApiKey.STAFF_EMAIL_LIST, getEmailString());
            if (Constants.CADDIE_DEPARTMENT_ID == type) {
                params.put(ApiKey.STAFF_LEVEL_ID, String.valueOf(levelId));
                params.put(ApiKey.STAFF_STAFF_NO, etNumberValue.getText().toString());
            }

            HttpManager<JsonReturnUserId> hh = new HttpManager<JsonReturnUserId>(AddEditProfileFragment.this) {

                @Override
                public void onJsonSuccess(JsonReturnUserId jo) {
                    int returnCode = jo.getReturnCode();
                    String msg = jo.getReturnInfo();

                    if (returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY) {
                        if (isPhotoChange != null) {
                            uploadPhoto(isPhotoChange, String.valueOf(jo.getDataList().getUserId()));
                        } else {
                            if (fromPage.equals(StaffDepartmentMemberListFragment.class.getName())) {
                                doBackWithRefresh(StaffDepartmentMemberListFragment.class);
                            } else {

                                doBackWithRefresh();
                            }
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
                }
            };
            hh.start(getBaseActivity(), ApiManager.HttpApi.StaffUserDetailPost, params);

        }
    }

    private void getProfile() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, String.valueOf(passedUserId));

        HttpManager<JsonUserDetailGet> hh = new HttpManager<JsonUserDetailGet>(AddEditProfileFragment.this) {

            @Override
            public void onJsonSuccess(JsonUserDetailGet jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {

                    if (!jo.isCanEdit()) {
                        getTvRight().setVisibility(View.GONE);
                    } else {
                        getTvRight().setVisibility(View.VISIBLE);
                    }

                    passedUserId = jo.getUserId();
                    departmentId = jo.getDepartmentId();
                    attrId = jo.getAttrId();

                    etNameValue.setText(jo.getUserName());

                    etDepartmentValue.setText(jo.getDepartmentName());
                    swManager.setChecked(Constants.STAFF_MANAGER.equals(jo.getManager()));

                    etUserNameValue.setText(jo.getAccount());
                    etPasswordValue.setText(jo.getPassword());

                    if (Utils.isListNotNullOrEmpty(jo.getPhoneList())) {
                        for (int i = 0; i < jo.getPhoneList().size(); i++) {
                            JsonUserDetailGet.Phone phone = jo.getPhoneList().get(i);
                            JsonGeneralInfo.PhoneItem phoneItem = new JsonGeneralInfo.PhoneItem();
                            phoneItem.setStatus(phone.getPhoneStatus());
                            phoneItem.setTag(phone.getPhoneTag());
                            phoneItem.setValue(phone.getPhone());
                            addPhoneView(phoneItem);
                            phoneList.add(phoneItem);
                        }
                    }

                    if (Utils.isListNotNullOrEmpty(jo.getEmailList())) {
                        for (int i = 0; i < jo.getEmailList().size(); i++) {
                            JsonUserDetailGet.Email email = jo.getEmailList().get(i);
                            JsonGeneralInfo.EmailItem emailItem = new JsonGeneralInfo.EmailItem();
                            emailItem.setStatus(email.getEmailStatus());
                            emailItem.setTag(email.getEmailTag());
                            emailItem.setValue(email.getEmail());
                            addEmailView(emailItem);
                            emailList.add(emailItem);
                        }
                    } else {
                        viSeparatorEmailTop.setVisibility(View.GONE);
                        viSeparatorEmailBottom.setVisibility(View.GONE);
                    }

                    if (Constants.CADDIE_DEPARTMENT_ID == type) {
                        levelId = jo.getLevelId();
                        tvLevelValue.setText(jo.getLevelName());
                        if (Utils.isStringNotNullOrEmpty(jo.getStaffNo())) {
//                            String numberValue = String.format(Constants.CADDIES_NO_FORMAT, Integer.valueOf(jo.getStaffNo()));    //TMD 疯了？这样写加个毛的格式化String！
                            etNumberValue.setText(jo.getStaffNo());
                        }
                    }

                } else {
                    String msg = jo.getReturnInfo();
                    Utils.showShortToast(getActivity(), msg);
                }
                if (Constants.CADDIE_DEPARTMENT_ID == type) {
                    getCaddieLevel();
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
        hh.startGet(getBaseActivity(), ApiManager.HttpApi.StaffUserDetailGet, params);
    }

    private void putProfile() {
        if (doCheck()) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.STAFF_USER_ID, String.valueOf(passedUserId));
            params.put(ApiKey.STAFF_USER_NAME, etNameValue.getText().toString());
            params.put(ApiKey.COMMON_COURES_ID, String.valueOf(courseId));
            params.put(ApiKey.STAFF_DEPARTMENT_ID, String.valueOf(departmentId));
            params.put(ApiKey.STAFF_MANAGER, String.valueOf(swManager.isChecked() ? Constants.STR_1 : Constants.STR_0));
            params.put(ApiKey.STAFF_ACCOUNT, etUserNameValue.getText().toString());
            params.put(ApiKey.STAFF_PASSWORD, etPasswordValue.getText().toString());
            params.put(ApiKey.STAFF_PHONE_LIST, getPhoneString());
            params.put(ApiKey.STAFF_EMAIL_LIST, getEmailString());

            params.put(ApiKey.STAFF_ATTRI_ID, attrId);


            if (Constants.CADDIE_DEPARTMENT_ID == type) {
                params.put(ApiKey.STAFF_LEVEL_ID, String.valueOf(levelId));
                params.put(ApiKey.STAFF_STAFF_NO, etNumberValue.getText().toString());
            }

            HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(AddEditProfileFragment.this) {

                @Override
                public void onJsonSuccess(BaseJsonObject jo) {
                    int returnCode = jo.getReturnCode();
                    String msg = jo.getReturnInfo();
                    Utils.showShortToast(getActivity(), msg);
                    if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                        if (isPhotoChange != null) {
                            uploadPhoto(isPhotoChange, String.valueOf(memberId));
                        } else {
                            if (fromPage.equals(StaffDepartmentMemberListFragment.class.getName())) {
                                doBackWithRefresh(StaffDepartmentMemberListFragment.class);
                            } else {
                                doBackWithRefresh();
                            }
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
            hh.startPut(getBaseActivity(), ApiManager.HttpApi.StaffUserDetailPut, params);

        }
    }

    private void deleteProfile() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.STAFF_DEPARTMENT_ID, String.valueOf(departmentId));
        params.put(ApiKey.COMMON_USER_ID, String.valueOf(passedUserId));
        params.put(ApiKey.STAFF_ATTRI_ID, attrId);
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(AddEditProfileFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {
                    if (fromPage.equals(StaffDepartmentMemberListFragment.class.getName())) {
                        doBackWithRefresh(StaffDepartmentMemberListFragment.class);
                    } else {
                        getBaseActivity().doBackWithRefresh();
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
            }
        };
        hh.startDelete(getBaseActivity(), ApiManager.HttpApi.StaffUserDetailDelete, params);
    }

    private void getCaddieLevel() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_COURES_ID, String.valueOf(courseId));
        HttpManager<JsonCaddieLevelGet> hh = new HttpManager<JsonCaddieLevelGet>(AddEditProfileFragment.this) {

            @Override
            public void onJsonSuccess(JsonCaddieLevelGet jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    caddieLevelList.addAll(jo.getDataList());
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
        hh.startGet(getBaseActivity(), ApiManager.HttpApi.StaffCaddieLevelGet, params);
    }

    private boolean doCheck() {
        boolean res = true;
        if (Utils.isStringNullOrEmpty(etNameValue.getText().toString())) {
            Utils.showShortToast(getActivity(), R.string.staff_fill_name);
            res = false;
        } else if (Utils.isStringNullOrEmpty(etUserNameValue.getText().toString())) {
            Utils.showShortToast(getActivity(), R.string.staff_fill_username);
            res = false;
        } else if (Utils.isStringNullOrEmpty(etPasswordValue.getText().toString())) {
            Utils.showShortToast(getActivity(), R.string.staff_fill_password);
            res = false;
        } else if (Constants.CADDIE_DEPARTMENT_ID == type) {
            if (Utils.isStringNullOrEmpty(tvLevelValue.getText().toString())) {
                Utils.showShortToast(getActivity(), R.string.staff_select_level);
                res = false;
            }
            if (Utils.isStringNullOrEmpty(etNumberValue.getText().toString())) {
                Utils.showShortToast(getActivity(), R.string.staff_fill_number);
                res = false;
            }
        } else if (!doCheckEmail()) {
            res = false;
        } else if (!doCheckPhone()) {
            res = false;
        } else if (!doCheckUserName()) {
            res = false;
        } else if (!doCheckPassword()) {
            res = false;
        }
        return res;
    }

    private boolean doCheckEmail() {
        boolean res = true;
        if (emailList.size() > 0) {
            for (int i = 0; i < emailList.size(); i++) {
                JsonGeneralInfo.EmailItem emailItem = emailList.get(i);
                if (Utils.isStringNullOrEmpty(emailItem.getTag())) {
                    res = false;
                    Utils.showShortToast(getActivity(), R.string.custom_mail_tag_must_not_be_null);
                }
                if (Utils.isStringNullOrEmpty(emailItem.getValue())) {
                    res = false;
                    Utils.showShortToast(getActivity(), R.string.staff_fill_email);
                } else {
                    boolean isEmail = Utils.isEmail(emailItem.getValue());
                    if (!isEmail) {
                        res = false;
                        Utils.showShortToast(getActivity(), getResources().getString(R.string.error_email_address_format));
                    }
                }
            }
        }
        return res;
    }

    private boolean doCheckPhone() {
        boolean res = true;
        if (phoneList.size() == 0) {
            res = false;
            Utils.showShortToast(getActivity(), R.string.staff_add_one_phone_number_at_least);
        } else {
            for (int i = 0; i < phoneList.size(); i++) {
                JsonGeneralInfo.PhoneItem phoneItem = phoneList.get(i);
                if (Utils.isStringNullOrEmpty(phoneItem.getTag())) {
                    res = false;
                    Utils.showShortToast(getActivity(), R.string.custom_phone_tag_must_not_be_null);
                }
                if (Utils.isStringNullOrEmpty(phoneItem.getValue())) {
                    res = false;
                    Utils.showShortToast(getActivity(), R.string.staff_fill_phone_number);
                } else {
                    boolean isPhone = Utils.isMobile(phoneItem.getValue());
                    if (!isPhone) {
                        res = false;
                        Utils.showShortToast(getActivity(), R.string.error_mes00003);
                    }
                }
            }
        }
        return res;
    }

    private boolean doCheckUserName() {
        boolean res = true;
        boolean isPhone = Utils.isMobile(etUserNameValue.getText().toString());
        boolean isEmail = Utils.isEmail(etUserNameValue.getText().toString());
        if (!isEmail && !isPhone) {
            res = false;
            Utils.showShortToast(getActivity(), getResources().getString(R.string.error_username_format));
        }

        return res;
    }

    private boolean doCheckPassword() {
        boolean res = true;
        if (etPasswordValue.getText().toString().length() < Constants.PASSWORD_MIN_SIZE || etPasswordValue.getText().toString().length() > Constants.PASSWORD_MAX_SIZE) {
            res = false;
            Utils.showShortToast(getActivity(), R.string.common_password_size_message);
        }
        return res;
    }

    private void change2EditState() {

        boolean isEdit = getFragmentMode() != FragmentMode.FragmentModeBrowse;

        ivPhoto.setEnabled(true);
        etNameValue.setEnabled(true);
        tvLevelValue.setEnabled(false);
        etNumberValue.setEnabled(true);
        swManager.setEnabled(true);
        etUserNameValue.setEnabled(true);
        etPasswordValue.setEnabled(true);

        if (llPhoneContainer.getChildCount() == 0) {
            tvPhoneRequired.setVisibility(View.VISIBLE);
        } else {
            tvPhoneRequired.setVisibility(View.GONE);
            for (int i = 0; i < llPhoneContainer.getChildCount(); i++) {
                PlayerInfoPhoneItemView playerInfoPhoneItemView = (PlayerInfoPhoneItemView) llPhoneContainer.getChildAt(i);
                playerInfoPhoneItemView.changeEdit(isEdit);
            }
        }
        if (llEmailContainer.getChildCount() == 0) {
            tvEmailRequired.setVisibility(View.INVISIBLE);
        } else {
            tvEmailRequired.setVisibility(View.GONE);
            for (int i = 0; i < llEmailContainer.getChildCount(); i++) {
                PlayerInfoEmailItemView playerInfoPhoneItemView = (PlayerInfoEmailItemView) llEmailContainer.getChildAt(i);
                playerInfoPhoneItemView.changeEdit(isEdit);
            }
        }

        if (isEdit) {
            etUserNameValue.setTextColor(Color.BLACK);
            etPasswordValue.setTextColor(Color.BLACK);
            etNameValue.setTextColor(Color.BLACK);
        }
    }

    @Override
    protected void executeOnceOnCreate() {
        super.executeOnceOnCreate();
        if (getFragmentMode() != FragmentMode.FragmentModeAdd) {
            getProfile();
        } else {
            if (Constants.CADDIE_DEPARTMENT_ID == type) {
                getCaddieLevel();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case SelectPhotoActivity.REQUEST_CODE_SET:
                if (resultCode == FragmentActivity.RESULT_OK) {
                    byte[] b = data.getByteArrayExtra("bitmap");
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    if (bitmap != null) {
                        ivPhoto.setImageBitmap(bitmap);
                        isPhotoChange = bitmap;
                    }

                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadPhoto(Bitmap bitmap, String id) {

        Bitmap bitmapTemp = ImageUtils.imageZoom(bitmap);
        byte[] b = ImageUtils.convertBitmap2Bytes(bitmapTemp);
        HashMap<String, byte[]> files = new HashMap<>();
        files.put("img", b);
        HashMap<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(mContext));
        params.put("type", Constants.UPLOAD_TYPE_STAFF);
        params.put("id", id);

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(AddEditProfileFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                if (fromPage.equals(StaffDepartmentMemberListFragment.class.getName())) {
                    doBackWithRefresh(StaffDepartmentMemberListFragment.class);
                } else {
                    doBackWithRefresh();
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.uploadFile(getActivity(), ApiManager.HttpApi.CommonPhoto, files, params);
    }

    private void addPhoneView(JsonGeneralInfo.PhoneItem phoneItem) {
        tvPhoneRequired.setVisibility(View.INVISIBLE);
        PlayerInfoPhoneItemView playerInfoTelItemView = new PlayerInfoPhoneItemView(getActivity(),
                getFragmentMode() != BaseEditFragment.FragmentMode.FragmentModeBrowse, phoneItem, llPhoneContainer, onDeleteClickListener);
        llPhoneContainer.addView(playerInfoTelItemView);
        playerInfoTelItemView.setTag(phoneItem);
        LinearLayout.LayoutParams paramsAddressWarn = (LinearLayout.LayoutParams) playerInfoTelItemView.getLayoutParams();
        paramsAddressWarn.height = (int) (getScreenHeight() * 0.08f);
        playerInfoTelItemView.setLayoutParams(paramsAddressWarn);
    }

    private void addEmailView(JsonGeneralInfo.EmailItem emailItem) {
        tvEmailRequired.setVisibility(View.GONE);

        PlayerInfoEmailItemView playerInfoTelItemView = new PlayerInfoEmailItemView(getActivity(),
                getFragmentMode() != FragmentMode.FragmentModeBrowse, emailItem, llEmailContainer, onDeleteClickListener);
        llEmailContainer.addView(playerInfoTelItemView);
        playerInfoTelItemView.setTag(emailItem);
        LinearLayout.LayoutParams paramsAddressWarn = (LinearLayout.LayoutParams) playerInfoTelItemView.getLayoutParams();
        paramsAddressWarn.height = (int) (getScreenHeight() * 0.08f);
        playerInfoTelItemView.setLayoutParams(paramsAddressWarn);

    }

    private String getEmailString() {
        JSONArray ja = new JSONArray();
        for (int i = 0; i < emailList.size(); i++) {
            JsonGeneralInfo.EmailItem emailItem = emailList.get(i);
            String format = "\"tag\":\"{0}\",\"value\":\"{1}\",\"status\":\"1\"";
            String emailString = MessageFormat.format(format, emailItem.getTag(), emailItem.getValue());
            try {
                JSONObject jo = new JSONObject("{" + emailString + "}");
                ja.put(jo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ja.toString();
    }

    private String getPhoneString() {
        JSONArray ja = new JSONArray();
        for (int i = 0; i < phoneList.size(); i++) {
            JsonGeneralInfo.PhoneItem phoneItem = phoneList.get(i);
            String format = "\"tag\":\"{0}\",\"value\":\"{1}\",\"status\":\"1\"";
            String phoneString = MessageFormat.format(format, phoneItem.getTag(), phoneItem.getValue());
            try {
                JSONObject jo = new JSONObject("{" + phoneString + "}");
                ja.put(jo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ja.toString();
    }
}
