package cn.situne.itee.activity;

import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

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

public class ChangeTempPassword extends BaseActivity {

    private RelativeLayout rlActionBarMenu;
    private RelativeLayout rlOldPasswordContainer;
    private RelativeLayout rlNewPasswordContainer;

    private IteeTextView tvNewPassword;
    private IteeTextView tvConfirmPassword;

    private IteeEditText etNewPassword;
    private IteeEditText etConfirmPassword;

    private ActionBar actionBar;
    private IteeTextView tvTitle;
    private IteeTextView tvRight;
    private ImageView ivIcon;

    @Override
    protected void initControls() {
        rlActionBarMenu = (RelativeLayout) findViewById(R.id.rl_actionbar_menu_container);
        rlOldPasswordContainer = (RelativeLayout) findViewById(R.id.rl_old_password_container);
        rlNewPasswordContainer = (RelativeLayout) findViewById(R.id.rl_new_password_container);

        tvNewPassword = new IteeTextView(this);
        tvConfirmPassword = new IteeTextView(this);

        etNewPassword = new IteeEditText(this);
        etConfirmPassword = new IteeEditText(this);

        ivIcon = new ImageView(this);
        tvRight = new IteeTextView(this);
        tvTitle = new IteeTextView(this);

    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(ChangeTempPassword.this);
                if (doCheck()) {
                    changeTempPassword();
                }
            }
        });
    }

    private void changeTempPassword() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(this));
        params.put(ApiKey.CHANGEPWD_NEW_PASSWORD, etNewPassword.getValue());
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(this));

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_CHANGE_PASSWORD_SUCCESS) {
                    ChangeTempPassword.this.finish();
                    Utils.pushActivity(ChangeTempPassword.this, MainActivity.class, true);
                } else {
                    Utils.showShortToast(ChangeTempPassword.this, msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(ChangeTempPassword.this, R.string.msg_common_network_error);
            }
        };
        hh.start(this, ApiManager.HttpApi.ChangePwd, params);
    }

    private boolean doCheck() {
        boolean res = true;
        if (etNewPassword.getValue().length() < Constants.PASSWORD_MIN_SIZE || etNewPassword.getValue().length() > Constants.PASSWORD_MAX_SIZE) {
            res = false;
            Utils.showShortToast(this, R.string.common_password_size_message);
        }
        if (etConfirmPassword.getValue().length() < Constants.PASSWORD_MIN_SIZE || etConfirmPassword.getValue().length() > Constants.PASSWORD_MAX_SIZE) {
            res = false;
            Utils.showShortToast(this, R.string.common_password_size_message);
        }
        if (Utils.isStringNotNullOrEmpty(etNewPassword.getValue())
                && Utils.isStringNotNullOrEmpty(etConfirmPassword.getValue())
                && !etNewPassword.getValue().equals(etConfirmPassword.getValue())) {
            res = false;
            Utils.showShortToast(this, R.string.msg_passwords_does_not_match);
        }
        return res;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void setLayoutOfControls() {

        LinearLayout.LayoutParams rlOldPasswordContainerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getHeight(100));
        rlOldPasswordContainer.setLayoutParams(rlOldPasswordContainerLayoutParams);

        LinearLayout.LayoutParams rlNewPasswordContainerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getHeight(100));
        rlNewPasswordContainer.setLayoutParams(rlNewPasswordContainerLayoutParams);

        rlActionBarMenu.addView(ivIcon);

        RelativeLayout.LayoutParams paramsIvIcon = (RelativeLayout.LayoutParams) ivIcon.getLayoutParams();
        paramsIvIcon.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvIcon.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvIcon.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        ivIcon.setLayoutParams(paramsIvIcon);


        rlActionBarMenu.addView(tvTitle);

        RelativeLayout.LayoutParams paramsTvTitle = new RelativeLayout.LayoutParams(getWidth(200), getHeight(100));
        paramsTvTitle.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTitle.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTitle.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvTitle.addRule(RelativeLayout.RIGHT_OF, ivIcon.getId());
        tvTitle.setLayoutParams(paramsTvTitle);

        rlActionBarMenu.addView(tvRight);

        RelativeLayout.LayoutParams paramsTvOk = (RelativeLayout.LayoutParams) tvRight.getLayoutParams();
        paramsTvOk.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvOk.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvOk.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvOk.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvOk.rightMargin = (int) (getWidth() * 0.05);
        tvRight.setLayoutParams(paramsTvOk);

        rlOldPasswordContainer.addView(tvNewPassword);
        RelativeLayout.LayoutParams paramsTvNewPassword = new RelativeLayout.LayoutParams(getWidth(200), getHeight(100));
        paramsTvNewPassword.width = (int) (getWidth() * 0.5);
        paramsTvNewPassword.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsTvNewPassword.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvNewPassword.leftMargin = getWidth(40);
        tvNewPassword.setLayoutParams(paramsTvNewPassword);

        rlOldPasswordContainer.addView(etNewPassword);
        RelativeLayout.LayoutParams paramsEtOldPassword = new RelativeLayout.LayoutParams(getWidth(200), getHeight(100));
        paramsEtOldPassword.width = (int) (getWidth() * 0.5);
        paramsEtOldPassword.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsEtOldPassword.rightMargin = getWidth(40);
        etNewPassword.setLayoutParams(paramsEtOldPassword);

        rlNewPasswordContainer.addView(tvConfirmPassword);
        RelativeLayout.LayoutParams paramsTvConfirmPassword = new RelativeLayout.LayoutParams(getWidth(200), getHeight(100));
        paramsTvConfirmPassword.width = (int) (getWidth() * 0.5);
        paramsTvConfirmPassword.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsTvConfirmPassword.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvConfirmPassword.leftMargin = getWidth(40);
        tvConfirmPassword.setLayoutParams(paramsTvConfirmPassword);

        rlNewPasswordContainer.addView(etConfirmPassword);
        RelativeLayout.LayoutParams paramsEtNewPassword = new RelativeLayout.LayoutParams(getWidth(200), getHeight(100));
        paramsEtNewPassword.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsEtNewPassword.rightMargin = getWidth(40);
        etConfirmPassword.setLayoutParams(paramsEtNewPassword);
    }

    @Override
    protected void setPropertyOfControls() {
        actionBar = getSupportActionBar();

        actionBar.hide();

        tvTitle.setTextSize(Constants.FONT_SIZE_LARGER);
        tvTitle.setTextColor(getColor(R.color.common_white));
        tvTitle.setText(R.string.title_change_password);

        tvRight.setTextSize(Constants.FONT_SIZE_LARGER);
        tvRight.setTextColor(getResources().getColor(R.color.common_white));
        tvRight.setText(R.string.common_ok);

        ivIcon.setId(View.generateViewId());
        ivIcon.setBackgroundResource(R.drawable.icon_back);
        ivIcon.setVisibility(View.INVISIBLE);

        tvNewPassword.setText(R.string.new_password);
        tvNewPassword.setTextColor(getColor(R.color.common_black));
        tvNewPassword.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);

        tvConfirmPassword.setText(R.string.confirm_password);
        tvConfirmPassword.setTextColor(getColor(R.color.common_black));
        tvConfirmPassword.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);

        etNewPassword.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etNewPassword.setBackground(null);
        etNewPassword.setTextColor(getColor(R.color.common_gray));
        etNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etNewPassword.setPadding(0, 8, 0, 0);

        etConfirmPassword.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etConfirmPassword.setTextColor(getColor(R.color.common_gray));
        etConfirmPassword.setBackground(null);
        etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etConfirmPassword.setPadding(0, 8, 0, 0);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_temp_password;
    }

}
