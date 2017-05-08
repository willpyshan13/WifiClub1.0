package cn.situne.itee.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.manager.jsonentity.JsonGeneralInfo;
import cn.situne.itee.view.popwindow.AddressTypePopupWindow;
import cn.situne.itee.view.popwindow.SelectDatePopupWindow;


public class PlayerInfoEmailItemView extends RelativeLayout {

    public int position;
    private Context context;
    private IteeTextView tvTag;
    private ImageView imageView;
    private LinearLayout parentView;
    private IteeEditText tvDescribe;
    private RelativeLayout thisLL;
    private JsonGeneralInfo.EmailItem emailItem;
    private Boolean isEdit;
    private Boolean isFirst;
    private PlayerInfoEditFragment.OnDeleteClickListener onDeleteClickListener;

    public PlayerInfoEmailItemView(Context context) {
        super(context);
    }

    public PlayerInfoEmailItemView(Context context, Boolean isEdit, JsonGeneralInfo.EmailItem emailItem,
                                   LinearLayout parentView, PlayerInfoEditFragment.OnDeleteClickListener onDeleteClickListener) {
        super(context);
        this.context = context;
        this.parentView = parentView;
        this.thisLL = this;
        this.emailItem = emailItem;
        this.isEdit = isEdit;
        this.onDeleteClickListener = onDeleteClickListener;

        isFirst = parentView.getChildCount() == 0;
        initView();
    }

    private void initView() {
        imageView = new ImageView(context);

        tvTag = new IteeTextView(context);
        tvDescribe = new IteeEditText(context);
        addView(imageView);
        LayoutParams layoutParamsDelete = (LayoutParams) imageView.getLayoutParams();
        layoutParamsDelete.height = LayoutParams.WRAP_CONTENT;
        layoutParamsDelete.width = (int) (Utils.getWidth(context) * 0.1f);
        layoutParamsDelete.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParamsDelete.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        imageView.setLayoutParams(layoutParamsDelete);
        imageView.setId(View.generateViewId());
        imageView.setImageResource(R.drawable.btn_circle_delete);


        addView(tvTag);
        LayoutParams layoutParamsName = (LayoutParams) tvTag.getLayoutParams();
        layoutParamsName.height = LayoutParams.WRAP_CONTENT;
        layoutParamsName.width = (int) (Utils.getWidth(context) * 0.2f);
        layoutParamsName.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        layoutParamsName.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
        tvTag.setLayoutParams(layoutParamsName);
        tvTag.setId(View.generateViewId());
        tvTag.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvTag.setSingleLine();
        addView(tvDescribe);
        LayoutParams layoutParamsDescribe = (LayoutParams) tvDescribe.getLayoutParams();
        layoutParamsDescribe.height = LayoutParams.WRAP_CONTENT;
        layoutParamsDescribe.width = (int) (Utils.getWidth(context) * 0.7f);
        layoutParamsDescribe.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        layoutParamsDescribe.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParamsDescribe.addRule(RelativeLayout.RIGHT_OF, tvTag.getId());
        tvDescribe.setLayoutParams(layoutParamsDescribe);
        tvDescribe.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvDescribe.setSingleLine();
        tvDescribe.setGravity(Gravity.END);
        tvDescribe.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);


        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClickListener.OnEmailDeleteClick("retDelete", emailItem);
                parentView.removeView(thisLL);
            }
        });

        initData();
    }

    public void changeEdit(Boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit) {

            imageView.setVisibility(VISIBLE);
            LayoutParams layoutParamsName = (LayoutParams) tvTag.getLayoutParams();
            layoutParamsName.height = LayoutParams.WRAP_CONTENT;
            layoutParamsName.width = (int) (Utils.getWidth(context) * 0.2f);
            layoutParamsName.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            layoutParamsName.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
            tvTag.setLayoutParams(layoutParamsName);
            LayoutParams layoutParamsDescribe = (LayoutParams) tvDescribe.getLayoutParams();
            layoutParamsDescribe.width = (int) (Utils.getWidth(context) * 0.7f);
            tvDescribe.setLayoutParams(layoutParamsDescribe);

//            Drawable imTelDrawable = getResources().getDrawable(R.drawable.icon_right_arrow);
//            tvDescribe.setCompoundDrawablesWithIntrinsicBounds(imTelDrawable, null, null, null);
            tvDescribe.setBackgroundResource(R.drawable.list_item_bottom_line);
            tvTag.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1:home 2:work 3:mobile 4:other 5:custom
                    SelectDatePopupWindow.OnDateSelectClickListener onDateSelectClickListener = new SelectDatePopupWindow.OnDateSelectClickListener() {
                        @Override
                        public void OnGoodItemClick(String flag, String content) {

                            String tag = null;
                            switch (flag) {
                                case "1":
                                    tag = context.getString(R.string.tag_home);
                                    break;
                                case "2":
                                    tag = context.getString(R.string.tag_work);
                                    break;
                                case "3":
                                    tag = context.getString(R.string.tag_mobile);
                                    break;
                                case "4":
                                    tag = context.getString(R.string.tag_other);
                                    break;
                                case "5":
                                    tag = content;
                                    break;
                                default:
                                    break;
                            }

                            tvTag.setText(tag);
                            emailItem.setTag(tag);

                        }
                    };
                    String paramTag = tvTag.getText().toString();

                    if (tvTag.getText().toString().equals(context.getString(R.string.tag_home))) {
                        paramTag = null;
                    } else if (tvTag.getText().toString().equals(context.getString(R.string.tag_work))) {
                        paramTag = null;
                    } else if (tvTag.getText().toString().equals(context.getString(R.string.tag_mobile))) {
                        paramTag = null;
                    } else if (tvTag.getText().toString().equals(context.getString(R.string.tag_other))) {
                        paramTag = null;
                    }

                    AddressTypePopupWindow menuWindow = new AddressTypePopupWindow(context, onDateSelectClickListener, paramTag, false);
                    menuWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            });

            if (isFirst) {
                tvTag.setTextColor(getResources().getColor(R.color.common_blue));
                tvDescribe.setTextColor(getResources().getColor(R.color.common_black));
            } else {
                tvTag.setTextColor(getResources().getColor(R.color.common_black));
                tvDescribe.setTextColor(getResources().getColor(R.color.common_black));
            }
            setBackground(null);
        } else {
//            setBackgroundResource(R.drawable.list_item_bottom_line);

            if (isFirst) {
                tvTag.setTextColor(getResources().getColor(R.color.common_blue));
                tvDescribe.setTextColor(getResources().getColor(R.color.common_blue));
            } else {
                tvTag.setTextColor(getResources().getColor(R.color.common_black));
                tvDescribe.setTextColor(getResources().getColor(R.color.common_black));

                if (parentView.getChildCount() > 0) {
                    View childAt = parentView.getChildAt(parentView.getChildCount() - 1);
                    childAt.setBackgroundResource(R.drawable.list_item_bottom_line);
                }
            }

            imageView.setVisibility(GONE);
            LayoutParams layoutParamsName = (LayoutParams) tvTag.getLayoutParams();
            layoutParamsName.height = LayoutParams.WRAP_CONTENT;
            layoutParamsName.width = (int) (Utils.getWidth(context) * 0.2f);
            layoutParamsName.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            layoutParamsName.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
            LayoutParams layoutParamsDescribe = (LayoutParams) tvDescribe.getLayoutParams();
            layoutParamsDescribe.width = (int) (Utils.getWidth(context) * 0.8f);
            tvDescribe.setLayoutParams(layoutParamsDescribe);

            tvDescribe.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            tvDescribe.setBackground(null);
        }
        tvDescribe.setEnabled(isEdit);
    }

    private void initData() {
        if (emailItem != null) {
            tvTag.setText(emailItem.getTag());
            tvDescribe.setText(emailItem.getValue());
        }
        //fix by syb.
        tvDescribe.setFilters(new InputFilter[]{
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

        tvDescribe.addTextChangedListener(new TextWatcher() {
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

                JsonGeneralInfo.EmailItem phoneItemTemp = (JsonGeneralInfo.EmailItem) thisLL.getTag();
                phoneItemTemp.setValue(temp.toString());

            }
        });


        changeEdit(isEdit);

    }

}
