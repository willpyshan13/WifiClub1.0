package cn.situne.itee.fragment.administration;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.manager.jsonentity.JsonSpecialDay;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;

public class SpecialDaysItemView extends RelativeLayout {

    public int position;


    private IteeEditText tvTag;
    private Context context;
    private ImageView imageView;
    private IteeTextView tvDescribe;
    private LinearLayout parentView;
    private RelativeLayout selfView;
    private Boolean isEdit;
    ArrayList<JsonSpecialDay.SpecialDay> dataSource ,deleteSource;


    private JsonSpecialDay.SpecialDay data;


    public SpecialDaysItemView(Context context) {
        super(context);
        initView();
    }

    public SpecialDaysItemView(Context context, LinearLayout parentView, JsonSpecialDay.SpecialDay data,
                               ArrayList<JsonSpecialDay.SpecialDay> dataSource ,ArrayList<JsonSpecialDay.SpecialDay> deleteSource) {
        super(context);
        this.context = context;
        this.parentView = parentView;
        this.selfView = this;
        this.data = data;
        this.dataSource = dataSource;
        this.deleteSource = deleteSource;
        initView();
    }


    private void initView() {
        imageView = new ImageView(context);
        data.setCdtAct("0");

        tvTag = new IteeEditText(context);
        tvDescribe = new IteeTextView(context);
        tvDescribe.setBackground(null);
        tvTag.setHint(getResources().getString(R.string.special_day_fragment_hint));
        addView(imageView);
        LayoutParams layoutParamsDelete = (LayoutParams) imageView.getLayoutParams();
        layoutParamsDelete.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParamsDelete.width = (int) (Utils.getWidth(context) * 0.12f);
        layoutParamsDelete.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParamsDelete.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        layoutParamsDelete.leftMargin = DensityUtil.getActualWidthOnThisDevice(20, context);
        imageView.setLayoutParams(layoutParamsDelete);
        imageView.setId(View.generateViewId());
        imageView.setImageResource(R.drawable.btn_circle_delete);


        addView(tvTag);
        LayoutParams layoutParamsName = (LayoutParams) tvTag.getLayoutParams();
        layoutParamsName.height = LayoutParams.WRAP_CONTENT;
        layoutParamsName.width = (int) (Utils.getWidth(context) * 0.7f);
        layoutParamsName.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        layoutParamsName.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
        tvTag.setLayoutParams(layoutParamsName);
        tvTag.setId(View.generateViewId());


        tvTag.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvTag.setSingleLine();
        tvTag.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        addView(tvDescribe);
        LayoutParams layoutParamsDescribe = (LayoutParams) tvDescribe.getLayoutParams();
        layoutParamsDescribe.height = LayoutParams.WRAP_CONTENT;
        layoutParamsDescribe.width = (int) (Utils.getWidth(context) * 0.2f);
        layoutParamsDescribe.rightMargin = DensityUtil.getActualWidthOnThisDevice(20, context);
        layoutParamsDescribe.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        layoutParamsDescribe.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParamsDescribe.addRule(RelativeLayout.RIGHT_OF, tvTag.getId());
        tvDescribe.setLayoutParams(layoutParamsDescribe);
        tvDescribe.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvDescribe.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        tvDescribe.setSingleLine();
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                parentView.removeView(selfView);
                dataSource.remove(data);
                deleteSource.add(data);
            }
        });
        AppUtils.addBottomSeparatorLine(selfView, context);
        tvTag.setText(data.getCdtName());
        tvDescribe.setText(data.getCdtFirstName());
        initData();
    }


    private void initData() {
        tvTag.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String tempI = temp.toString().trim();
                data.setCdtName(tempI);


                if (Utils.isStringNotNullOrEmpty(tempI) && tempI.length() > 0) {
                    if ("".equals(tvDescribe.getText())) {
                        tvDescribe.setText(tempI.substring(0, 1));
                        data.setCdtFirstName(tempI.substring(0, 1));
                    }
                } else {
                    tvDescribe.setText("");
                    data.setCdtFirstName("");
                }


            }
        });

//        //fix by syb.
//        tvDescribe.setFilters(new InputFilter[]{
//                new InputFilter() {
//                    @Override
//                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//                        if (" ".equals(source.toString())) {
//                            return "";
//                        }
//                        return null;
//                    }
//                }
//        });

    }


}
