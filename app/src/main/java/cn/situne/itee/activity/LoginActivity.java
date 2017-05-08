/**
 * Project Name: itee
 * File Name:	 LoginActivity.java
 * Package Name: cn.situne.itee.activity
 * Date:		 2015-01-10
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.IteeApplication;
import cn.situne.itee.manager.IteeJsonRequest;
import cn.situne.itee.manager.jsonentity.JsonLogin;

/**
 * ClassName:LoginActivity <br/>
 * Function: login page. <br/>
 * UI:  01-1
 * Date: 2015-01-10 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class LoginActivity extends Activity {
    private final static String TIMER_TASK = "timerTask";

    @BindView(R.id.btn_login)
     Button btnLogin;
    @BindView(R.id.et_account)
     EditText etAccount;
    @BindView(R.id.et_password)
     EditText etPassword;
    @BindView(R.id.tv_forget_pass)
     TextView tvForgetPassword;
    @BindView(R.id.tv_about)
     TextView tvAbout;
    //新增的服务条款和隐私策略
    @BindView(R.id.ckbPrivacy)
    CheckBox ckbPrivacy;

    // countdown timer
    private ForgotPasswordCount forgotPasswordCount;
    private boolean isNeedForceUpdate = false;
    private String showConfigFlag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
//        getVersionAndUrl();//这是干嘛的？
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Utils.log("height = " + getResources().getDisplayMetrics().heightPixels);
        Utils.log("width = " + getResources().getDisplayMetrics().widthPixels);
        Utils.log("density = " + getResources().getDisplayMetrics().density);

        setDefaultValueOfControls();

        IteeApplication.getInstance().addActivity(this);
        copyDb();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String timerTask = data.getExtras().getString(TransKey.TIMER_TASK);
            if (TIMER_TASK.equals(timerTask)) {
                forgotPasswordCount = new ForgotPasswordCount(60000, 1000);
                forgotPasswordCount.start();
            }
        }
    }
    protected void setDefaultValueOfControls() {
        // read account name from SharedPreferences
        String account = (String) Utils.readFromSP(this, Constants.KEY_SP_LOGIN_USER_NAME);
        if (account != null) {  // account is not null
            etAccount.setText(account);
        }
    }

    @OnClick(R.id.tv_forget_pass)
    void tvForgetPassword_OnClick() {
        if (forgotPasswordCount != null) {
            forgotPasswordCount.cancel();
        }
        Intent intent = new Intent();
        if (etAccount.getText() != null) {
            String account = etAccount.getText().toString();
            if (account != null && !Constants.STR_EMPTY.equals(account)) {
                intent.putExtra(TransKey.ACCOUNT, account);
            }
        }
        intent.setClass(LoginActivity.this, ForgotPasswordActivity.class);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.push_in_right, R.anim.push_out_left);
    }

    @OnEditorAction(R.id.et_account)
    public boolean etAccout_OnEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND
                || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            etPassword.requestFocus();  // jump to password input
            return true;
        }
        return false;
    }

    @OnEditorAction(R.id.et_password)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND
                || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            Utils.hideKeyboard(LoginActivity.this);
            doLogin();
            return false;
        }
        return false;
    }

    @OnClick(R.id.tvPrivacy)
    public void tvPrivacy_OnClick(View v) {
        Utils.pushActivity(LoginActivity.this, PrivacyActivity.class, true);
    }


    @OnClick(R.id.tv_forget_pass)
    public void doForgetPass() {
        if (forgotPasswordCount != null) {
            forgotPasswordCount.cancel();
        }
        Intent intent = new Intent();
        if (etAccount.getText() != null) {
            String account = etAccount.getText().toString();
            if (account != null && !Constants.STR_EMPTY.equals(account)) {
                intent.putExtra(TransKey.ACCOUNT, account);
            }
        }
        intent.setClass(LoginActivity.this, ForgotPasswordActivity.class);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.push_in_right, R.anim.push_out_left);
    }

    @OnClick(R.id.tv_about)
    public void tvAboutListener_OnClick() {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, AboutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_in_right, R.anim.push_out_left);
    }

    /**
     * doLogin:login to server. <br/>
     */
    @OnClick(R.id.btn_login)
    public void doLogin() {
        if (doCheck()) {
            btnLogin.setEnabled(false);
            btnLogin.setTextColor(getResources().getColor(R.color.common_gray));

            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.LOGIN_USER_NAME, etAccount.getText().toString());
            params.put(ApiKey.LOGIN_USER_PWD, etPassword.getText().toString());
            params.put(ApiKey.LOGIN_LANG, Locale.getDefault().getLanguage());
            try {
                PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
                params.put(ApiKey.LOGIN_VERSION_CODE, String.valueOf(pi.versionCode));
                params.put(ApiKey.LOGIN_CHANNEL, AppUtils.getChannelId(this));
            } catch (Exception e) {
                Utils.log(e.getMessage());
            }

            HttpManager<JsonLogin> hh = new HttpManager<JsonLogin>(true) {
                @Override
                public void onJsonSuccess(JsonLogin jo) {
                    int returnCode = jo.getReturnCode();
                    String msg = jo.getReturnInfo();
                    if (returnCode == Constants.RETURN_CODE_LOGIN_SUCCESS) {    // login success
                        if (forgotPasswordCount != null) {
                            forgotPasswordCount.cancel();
                            tvForgetPassword.setEnabled(true);
                        }
                        Utils.log("Auth : " + jo.getAuth());
                        Utils.log("Tel : " + jo.getUserTel());
                        Utils.log("date_format : " + jo.getDateFormat());
                        showConfigFlag = jo.getShowConfigFlag();
                        if (StringUtils.isNotEmpty(jo.getDateFormat())) {
                            AppUtils.saveCurrentDateFormat(jo.getDateFormat(), LoginActivity.this);
                        }
                        // save login info
                        Utils.save2SP(LoginActivity.this, Constants.KEY_SP_LOGIN_INFO, jo);

                        AppUtils.saveCurrencyId(LoginActivity.this, jo.getCurrencyId());
                        AppUtils.saveCurrencySymbol(LoginActivity.this, jo.getCurrencySymbol());

                        Utils.log(" AppUtils.getToken() : " + AppUtils.getToken(LoginActivity.this));
                        Utils.log("getRefreshToken : " + AppUtils.getRefreshToken(LoginActivity.this));

                        AppUtils.removeShoppingCart(LoginActivity.this);
                        AppUtils.initAuthMap(LoginActivity.this);
                        Utils.save2SP(LoginActivity.this, Constants.KEY_SP_LOGIN_USER_NAME, etAccount.getText().toString());

                        if (Utils.isStringNotNullOrEmpty(jo.getUserLogo())) { // download new logo
                            DownImage di = new DownImage(jo.getUserLogo());
                            di.execute();
                        } else {
                            String logoPath = getApplicationContext().getFilesDir().getAbsolutePath()
                                    + File.separator + Constants.FILE_NAME_LOGO;
                            File file = new File(logoPath);
                            if (file.exists()) {
                                boolean res = file.delete();
                                Utils.log("delete result : " + res);
                            }
                        }

                        if (!jo.isUserIsChangePwd()) {  // login with original password
                            LoginActivity.this.finish();
                            Bundle bundle = new Bundle();
                            bundle.putString("showConfigFlag", showConfigFlag);

//                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                            startActivity(intent);

                            Utils.pushActivity(LoginActivity.this, MainActivity.class, true, bundle);
                        } else {    // login with temp password
                            Utils.pushActivity(LoginActivity.this, ChangeTempPassword.class, true);
                        }
                    } else {    // login failed
                        if (returnCode == Constants.RETURN_CODE_LOGIN_FAILED_USER_NOT_EXIST
                                || returnCode == Constants.RETURN_CODE_LOGIN_FAILED_INCORRECT_PWD
                                || returnCode == Constants.RETURN_CODE_LOGIN_FAILED_INVALID_TEMP_PWD) {
                            Utils.showShortToast(LoginActivity.this, msg);
                        }
                    }
                    btnLogin.setEnabled(true);
                    btnLogin.setTextColor(getResources().getColor(R.color.common_white));
                }

                @Override
                public void onJsonError(VolleyError error) {
                    btnLogin.setEnabled(true);
                    btnLogin.setTextColor(getResources().getColor(R.color.common_white));
                    NetworkResponse response = error.networkResponse;
                    if (response != null) {
                        Utils.debug(response.toString());
                    }
                    Utils.showShortToast(LoginActivity.this, R.string.msg_common_network_error);
                }
            };
            hh.start(LoginActivity.this, ApiManager.HttpApi.Login, params);
        }
    }

    /**
     * doCheck:check input data. <br/>
     */
    private boolean doCheck() {
        boolean isPass = true;
        if (Utils.isStringNullOrEmpty(etAccount.getText().toString())) {
            Utils.showShortToast(LoginActivity.this, R.string.msg_login_enter_your_username_and_password);
            return isPass = false;
        }
        if (Utils.isStringNullOrEmpty(etPassword.getText().toString())) {
            Utils.showShortToast(LoginActivity.this, R.string.msg_login_enter_your_username_and_password);
            return isPass = false;
        }
        if (etPassword.getText().length() < Constants.PASSWORD_MIN_SIZE
                || etPassword.getText().length() > Constants.PASSWORD_MAX_SIZE) {
            Utils.showShortToast(this, R.string.common_password_size_message);
            return isPass = false;

        }
        //增加检查服务条款是否勾选
        if (!ckbPrivacy.isChecked()) {
            isPass = false;
            Utils.showShortToast(this, "您必须同意《服务协议》和《隐私政策》");
            return isPass = false;
        }
        return isPass;
    }

    @Override
    public void onBackPressed() {
        finish();
        IteeApplication.getInstance().AppExit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IteeApplication.getInstance().finishActivity(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private Bitmap getLocalLogo(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            Utils.log(e.getMessage());
            return null;
        }
    }

    private void getVersionAndUrl() {
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);

            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.CHECK_VERSION_CHANNEL, AppUtils.getChannelId(this));
            params.put(ApiKey.CHECK_VERSION_VERSION_CODE, String.valueOf(pi.versionCode));

            RequestQueue queue = Volley.newRequestQueue(this);
            IteeJsonRequest.startNetAPI(queue, ApiManager.HttpApi.CheckVersion, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.has(JsonKey.VERSION_URL)
                                && !response.isNull(JsonKey.VERSION_URL)) {
                            String url = response.getString(JsonKey.VERSION_URL);
                            if (Utils.isStringNotNullOrEmpty(url)) {
                                AppUtils.setBaseUrl(url);
                            }
                        }
                        boolean hasNewVersion = response.has(JsonKey.VERSION_UPDATE)
                                && !response.isNull(JsonKey.VERSION_UPDATE)
                                && response.getInt(JsonKey.VERSION_UPDATE) != 0;

                        isNeedForceUpdate = response.has(JsonKey.VERSION_FORCED)
                                && !response.isNull(JsonKey.VERSION_FORCED)
                                && response.getInt(JsonKey.VERSION_FORCED) != 0;
                        if (!isNeedForceUpdate && hasNewVersion) {
//                            Utils.save2SP(LoginActivity.this, Constants.KEY_SP_LOGIN_USER_NAME, etAccount.getValue());
//                            UmengUpdateAgent.update(LoginActivity.this);
                        }
                    } catch (JSONException e) {
                        Utils.log(e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.showLongToast(LoginActivity.this, R.string.msg_common_network_error);
                }
            }, this, null);
        } catch (PackageManager.NameNotFoundException e) {
            Utils.log(e.getMessage());
        }
    }

    private void copyDb() {
        String dbName = Constants.DB_NAME_ADDRESS;
        File databasePath = getDatabasePath(dbName);
        if (!databasePath.exists()) {
            try {
                SQLiteDatabase database = openOrCreateDatabase(dbName, MODE_PRIVATE, null);
                database.close();

                InputStream inputStream = getAssets().open(dbName);
                FileOutputStream fos = new FileOutputStream(databasePath);

                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int read;
                while ((read = inputStream.read(buffer, 0, bufferSize)) != -1) {
                    fos.write(buffer, 0, read);
                }
                fos.flush();
                fos.close();
                inputStream.close();

            } catch (IOException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    class DownImage extends AsyncTask<String, Void, Void> {

        private String url;

        public DownImage(String url) {
            this.url = url;
        }

        @Override
        protected Void doInBackground(String... params) {
            String logoPath = getApplicationContext().getFilesDir().getAbsolutePath()
                    + File.separator + Constants.FILE_NAME_LOGO;
            URL myFileUrl = null;
            try {
                myFileUrl = new URL(url);
            } catch (MalformedURLException e) {
                Utils.log(e.getMessage());
            }
            if (myFileUrl != null) {
                try {
                    File file = new File(logoPath);
                    if (file.exists()) {
                        boolean res = file.delete();
                        Utils.log("delete result : " + res);
                    }
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setConnectTimeout(0);
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] bs = new byte[1024];
                    int len;
                    while ((len = is.read(bs)) != -1) {
                        fos.write(bs, 0, len);
                    }
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    Utils.log(e.getMessage());
                }
            }
            return null;
        }
    }

    /*定义一个倒计时的内部类*/
    class ForgotPasswordCount extends CountDownTimer {
        public ForgotPasswordCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tvForgetPassword.setText(R.string.login_forgot_password);
            tvForgetPassword.setTextColor(getResources().getColor(R.color.login_forgot_password));
            tvForgetPassword.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvForgetPassword.setText(millisUntilFinished / 1000 + Constants.STR_SPACE + Constants.STR_SECOND);
            tvForgetPassword.setTextColor(getResources().getColor(R.color.common_red));
            tvForgetPassword.setEnabled(false);
        }
    }
}
