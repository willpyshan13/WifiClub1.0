package cn.situne.itee.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import java.util.HashMap;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonForgotPassword;
import cn.situne.itee.view.ForgetPasswordTipStyleSpan;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;


public class ForgotPasswordActivity extends BaseActivity {

    private final static String TIMER_TASK = "timerTask";

    private RelativeLayout rlEditViewContainer;
    private RelativeLayout rlBtnContainer;
    private RelativeLayout rlTipsContainer;

    private IteeEditText etAccount;

    private IteeButton btnRandomPassword;

    private IteeTextView tvTips;

    @Override
    protected void initControls() {

        rlEditViewContainer = (RelativeLayout) this.findViewById(R.id.rl_editview_container);
        rlBtnContainer = (RelativeLayout) this.findViewById(R.id.rl_btn_randompwd_container);
        rlTipsContainer = (RelativeLayout) this.findViewById(R.id.rl_btn_tips_container);

        etAccount = new IteeEditText(this);

        btnRandomPassword = new IteeButton(this);

        tvTips = new IteeTextView(this);
    }

    @Override
    protected void setDefaultValueOfControls() {

        Intent i = getIntent();
        if (i != null) {
            String account = i.getStringExtra(TransKey.ACCOUNT);
            if (account != null) {
                etAccount.setText(account);
            }
        }

    }


    @Override
    protected void setListenersOfControls() {
        btnRandomPassword.setOnClickListener(new AppUtils.NoDoubleClickListener(ForgotPasswordActivity.this) {
            @Override
            public void noDoubleClick(View view) {

                if (Utils.isStringNotNullOrEmpty(etAccount.getText().toString())) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(ApiKey.LOGIN_USER_NAME, etAccount.getText().toString());
                    HttpManager<JsonForgotPassword> hh
                            = new HttpManager<JsonForgotPassword>(ForgotPasswordActivity.this) {

                        @Override
                        public void onJsonSuccess(JsonForgotPassword jo) {
                            int returnCode = jo.getReturnCode();
                            String msg = jo.getReturnInfo();
                            if (returnCode == Constants.RETURN_CODE_FORGOT_PASSWORD_USER_EXIST) {

                                Utils.showShortToast(ForgotPasswordActivity.this, msg);
                                Intent intent = new Intent();
                                //把返回数据存入Intent
                                intent.putExtra(TransKey.TIMER_TASK, TIMER_TASK);
                                //设置返回数据
                                ForgotPasswordActivity.this.setResult(RESULT_OK, intent);
                                finish();
                                overridePendingTransition(R.anim.push_in_left, R.anim.push_out_right);

                            } else if (returnCode == Constants.RETURN_CODE_FORGOT_PASSWORD_USER_NOT_EXIST) {
                                Utils.showShortToast(ForgotPasswordActivity.this, msg);
                            }


                        }

                        @Override
                        public void onJsonError(VolleyError error) {

                        }
                    };


                    hh.start(ForgotPasswordActivity.this, ApiManager.HttpApi.ForgotPwd, params);


                } else if (!Utils.isStringNotNullOrEmpty(etAccount.getText().toString())) {
                    Utils.showShortToast(ForgotPasswordActivity.this, R.string.login_forgot_password_input_account);
                }

            }
        });
    }

    @Override
    protected void setLayoutOfControls() {

        rlEditViewContainer.addView(etAccount);
        RelativeLayout.LayoutParams paramsEtAccount = (RelativeLayout.LayoutParams) etAccount.getLayoutParams();
        paramsEtAccount.width = (int) (getWidth() * 0.95);
        paramsEtAccount.height = (int) (getHeight() * 0.2);
        paramsEtAccount.topMargin = dp2px(10);
        paramsEtAccount.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        etAccount.setLayoutParams(paramsEtAccount);

        rlBtnContainer.addView(btnRandomPassword);
        RelativeLayout.LayoutParams paramsBtnRandomPassword = (RelativeLayout.LayoutParams) btnRandomPassword.getLayoutParams();
        paramsBtnRandomPassword.width = (int) (getWidth() * 0.95);
        paramsBtnRandomPassword.height = (int) (getHeight() * 0.2);
        paramsBtnRandomPassword.topMargin = dp2px(10);
        paramsBtnRandomPassword.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        btnRandomPassword.setLayoutParams(paramsBtnRandomPassword);

        rlTipsContainer.addView(tvTips);
        RelativeLayout.LayoutParams paramsTvTips = (RelativeLayout.LayoutParams) tvTips.getLayoutParams();
        paramsTvTips.width = (int) (getWidth() * 0.9);
        paramsTvTips.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTips.topMargin = dp2px(10);
        paramsTvTips.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        tvTips.setLayoutParams(paramsTvTips);
    }

    @Override
    protected void setPropertyOfControls() {

        getTvLeftTitle().setText(R.string.forgot_password);
        getTvLeftTitle().setTextSize(Constants.FONT_SIZE_LARGER); //20160121 modify by zfg

        etAccount.setId(View.generateViewId());
        etAccount.setBackgroundResource(R.drawable.et_account_forgot_border);
        etAccount.setHint(R.string.forgot_password_account);
        etAccount.setHintTextColor(getResources().getColor(R.color.common_gray));
        etAccount.setTextSize(Constants.FONT_SIZE_NORMAL);
        etAccount.setPadding(20, 0, 0, 0);
        etAccount.setSingleLine();
        etAccount.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        etAccount.setSelectAllOnFocus(false);

        btnRandomPassword.setBackgroundResource(R.drawable.bg_common_edit);
        btnRandomPassword.setText(R.string.forgot_password_send_random_password);
        btnRandomPassword.setTextColor(getColor(R.color.common_white));

        //TextView中局部字体加粗和局部字体颜色设置
        SpannableStringBuilder ssb = new SpannableStringBuilder(getResources().getText(R.string.forgot_password_tips));
//        ssb.setSpan(new ForgetPasswordTipStyleSpan(Typeface.BOLD), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ssb.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTips.setText(ssb);
        tvTips.setTextColor(getResources().getColor(R.color.common_gray));
        tvTips.setMovementMethod(LinkMovementMethod.getInstance());
        tvTips.setSingleLine(false);
        tvTips.setTextSize(Constants.FONT_SIZE_SMALLER);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forgot_password;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
