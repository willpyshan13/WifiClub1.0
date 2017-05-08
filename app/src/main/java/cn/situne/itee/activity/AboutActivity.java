package cn.situne.itee.activity;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.view.IteeTextView;


public class AboutActivity extends BaseActivity {

    private RelativeLayout rlLogoContainer;
    private RelativeLayout rlLineContainer;
    private RelativeLayout rlPhoneContainer;
    private RelativeLayout rlEmailContainer;
    private RelativeLayout rlAddressContainer;
    private RelativeLayout rlVersionContainer;

    private ImageView ivLogo;
    private ImageView ivLine;

    private IteeTextView tvPhone;
    private IteeTextView tvPhoneContent;
    private IteeTextView tvEmail;
    private IteeTextView tvEmailContent;
    private IteeTextView tvAddress;
    private IteeTextView tvAddressContent;

    private IteeTextView tvVersionName;
    private IteeTextView tvVersionContent;

    @Override
    protected void initControls() {
        rlLogoContainer = (RelativeLayout) this.findViewById(R.id.rl_logo_container);
        rlLineContainer = (RelativeLayout) this.findViewById(R.id.rl_line_container);
        rlPhoneContainer = (RelativeLayout) this.findViewById(R.id.rl_phone_container);
        rlEmailContainer = (RelativeLayout) this.findViewById(R.id.rl_email_container);
        rlAddressContainer = (RelativeLayout) this.findViewById(R.id.rl_address_container);
        rlVersionContainer = (RelativeLayout) this.findViewById(R.id.rl_version_container);

        ivLogo = new ImageView(this);
        ivLine = new ImageView(this);

        tvPhone = new IteeTextView(this);
        tvPhoneContent = new IteeTextView(this);
        tvEmail = new IteeTextView(this);
        tvEmailContent = new IteeTextView(this);
        tvAddress = new IteeTextView(this);
        tvAddressContent = new IteeTextView(this);


        tvVersionName = new IteeTextView(this);
        tvVersionContent = new IteeTextView(this);


        tvPhone.setTextSize(Constants.FONT_SIZE_15);
        tvPhoneContent.setTextSize(Constants.FONT_SIZE_15);
        tvEmail.setTextSize(Constants.FONT_SIZE_15);
        tvEmailContent.setTextSize(Constants.FONT_SIZE_15);
        tvAddress.setTextSize(Constants.FONT_SIZE_15);
        tvAddressContent.setTextSize(Constants.FONT_SIZE_15);
        tvVersionName.setTextSize(Constants.FONT_SIZE_15);
        tvVersionContent.setTextSize(Constants.FONT_SIZE_15);


    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {

        rlLogoContainer.addView(ivLogo);
        RelativeLayout.LayoutParams paramsIvLogo = (RelativeLayout.LayoutParams) ivLogo.getLayoutParams();
        paramsIvLogo.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvLogo.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvLogo.addRule(RelativeLayout.CENTER_IN_PARENT, LAYOUT_TRUE);
        ivLogo.setLayoutParams(paramsIvLogo);

        rlLineContainer.addView(ivLine);
        RelativeLayout.LayoutParams paramsIvLine = (RelativeLayout.LayoutParams) ivLine.getLayoutParams();
        paramsIvLine.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvLine.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvLine.addRule(RelativeLayout.CENTER_IN_PARENT, LAYOUT_TRUE);
        ivLine.setLayoutParams(paramsIvLine);

        rlPhoneContainer.addView(tvPhone);
        RelativeLayout.LayoutParams paramsTvPhone = (RelativeLayout.LayoutParams) tvPhone.getLayoutParams();
        paramsTvPhone.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPhone.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPhone.addRule(RelativeLayout.ALIGN_PARENT_TOP, LAYOUT_TRUE);
        paramsTvPhone.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        tvPhone.setLayoutParams(paramsTvPhone);

        rlPhoneContainer.addView(tvPhoneContent);
        RelativeLayout.LayoutParams paramsTvPhoneContent = (RelativeLayout.LayoutParams) tvPhoneContent.getLayoutParams();
        paramsTvPhoneContent.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPhoneContent.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPhoneContent.addRule(RelativeLayout.BELOW, tvPhone.getId());
        paramsTvPhoneContent.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        tvPhoneContent.setLayoutParams(paramsTvPhoneContent);

        rlEmailContainer.addView(tvEmail);
        RelativeLayout.LayoutParams paramsTvEmail = (RelativeLayout.LayoutParams) tvEmail.getLayoutParams();
        paramsTvEmail.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvEmail.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvEmail.addRule(RelativeLayout.ALIGN_PARENT_TOP, LAYOUT_TRUE);
        paramsTvEmail.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        tvEmail.setLayoutParams(paramsTvEmail);

        rlEmailContainer.addView(tvEmailContent);
        RelativeLayout.LayoutParams paramsTvEmailContent = (RelativeLayout.LayoutParams) tvEmailContent.getLayoutParams();
        paramsTvEmailContent.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvEmailContent.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvEmailContent.addRule(RelativeLayout.BELOW, tvEmail.getId());
        paramsTvEmailContent.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        tvEmailContent.setLayoutParams(paramsTvEmailContent);

        rlAddressContainer.addView(tvAddress);
        RelativeLayout.LayoutParams paramsTvAddress = (RelativeLayout.LayoutParams) tvAddress.getLayoutParams();
        paramsTvAddress.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddress.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddress.addRule(RelativeLayout.ALIGN_PARENT_TOP, LAYOUT_TRUE);
        paramsTvAddress.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        tvAddress.setLayoutParams(paramsTvAddress);

        rlAddressContainer.addView(tvAddressContent);
        RelativeLayout.LayoutParams paramsTvAddressContent = (RelativeLayout.LayoutParams) tvAddressContent.getLayoutParams();
        paramsTvAddressContent.width = (int) (getWidth() * 0.6);
        paramsTvAddressContent.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddressContent.addRule(RelativeLayout.BELOW, tvAddress.getId());
        paramsTvAddressContent.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        tvAddressContent.setLayoutParams(paramsTvAddressContent);

        rlVersionContainer.addView(tvVersionName);
        RelativeLayout.LayoutParams tvVersionNameLayoutParams =
                new RelativeLayout.LayoutParams(getWidth(200), ViewGroup.LayoutParams.WRAP_CONTENT);
        tvVersionNameLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, LAYOUT_TRUE);
        tvVersionNameLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        tvVersionName.setLayoutParams(tvVersionNameLayoutParams);

        rlVersionContainer.addView(tvVersionContent);
        RelativeLayout.LayoutParams tvVersionContentLayoutParams =
                new RelativeLayout.LayoutParams(getWidth(600), ViewGroup.LayoutParams.WRAP_CONTENT);
        tvVersionContentLayoutParams.addRule(RelativeLayout.BELOW, tvVersionName.getId());
        tvVersionContentLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        tvVersionContent.setLayoutParams(tvVersionContentLayoutParams);
    }

    @Override
    protected void setPropertyOfControls() {

        getTvLeftTitle().setText(R.string.title_about);
        getTvLeftTitle().setTextSize(Constants.FONT_SIZE_LARGER); //20160121 modify by zfg

        ivLogo.setId(View.generateViewId());
        ivLogo.setImageResource(R.drawable.about_logo);

        ivLine.setId(View.generateViewId());
        ivLine.setImageResource(R.drawable.about_line);

        tvPhone.setId(View.generateViewId());
        tvPhone.setTextColor(getColor(R.color.common_blue));
        tvPhone.setText(R.string.about_phone);

        tvPhoneContent.setGravity(Gravity.CENTER);
        tvPhoneContent.setText(R.string.about_phone_content);
        tvPhoneContent.setTextColor(getColor(R.color.common_white));

        tvEmail.setId(View.generateViewId());
        tvEmail.setTextColor(getColor(R.color.common_blue));
        tvEmail.setText(R.string.about_email);

        tvEmailContent.setGravity(Gravity.CENTER);
        tvEmailContent.setText(R.string.about_email_content);
        tvEmailContent.setTextColor(getColor(R.color.common_white));

        tvAddress.setId(View.generateViewId());
        tvAddress.setTextColor(getColor(R.color.common_blue));
        tvAddress.setText(R.string.about_office_address);

        tvAddressContent.setGravity(Gravity.CENTER);
        tvAddressContent.setText(R.string.about_office_address_content);
        tvAddressContent.setTextColor(getColor(R.color.common_white));
        tvAddressContent.setSingleLine(false);

        tvVersionName.setText(R.string.about_version);
        tvVersionName.setTextColor(getColor(R.color.common_blue));
        tvVersionName.setId(View.generateViewId());
        tvVersionName.setGravity(Gravity.CENTER);

        tvVersionContent.setText(Utils.getVersionName(this));
        tvVersionContent.setTextColor(getColor(R.color.common_white));
        tvVersionContent.setId(View.generateViewId());
        tvVersionContent.setGravity(Gravity.CENTER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.push_out_right, R.anim.push_in_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
