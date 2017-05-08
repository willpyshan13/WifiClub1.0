package cn.situne.itee.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.situne.itee.R;
import cn.situne.itee.activity.MainActivity;
import cn.situne.itee.activity.SelectAddressActivity;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.manager.jsonentity.JsonGeneralInfo;
import cn.situne.itee.view.popwindow.AddressTypePopupWindow;
import cn.situne.itee.view.popwindow.SelectDatePopupWindow;

/**
 * ClassName:PlayerInfoAddressItemView <br/>
 * Function: address item. <br/>
 * Date: 2015-01-20 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class PlayerInfoAddressItemView extends RelativeLayout {

    public IteeTextView tvCountry, tvProvince, tvCity;
    public int position;

    private Context context;
    private BaseFragment mFragment;

    private TextView tvTag;
    private IteeEditText etAddress, etPostCode;
    private LinearLayout parentView;
    private RelativeLayout thisLL;
    private LinearLayout llRight;
    private JsonGeneralInfo.AddressItem addressItem;
    private PlayerInfoEditFragment.OnDeleteClickListener onDeleteClickListener;
    private int mIndex;
    private Boolean isFirst;

    public PlayerInfoAddressItemView(Context context, JsonGeneralInfo.AddressItem addressItem, int index, Boolean isEdit,
                                     LinearLayout parentView, PlayerInfoEditFragment.OnDeleteClickListener onDeleteClickListener, BaseFragment fragment) {
        super(context);
        this.context = context;
        this.mFragment = fragment;
        this.parentView = parentView;
        this.thisLL = this;
        this.addressItem = addressItem;
        this.onDeleteClickListener = onDeleteClickListener;
        this.mIndex = index;

        isFirst = parentView.getChildCount() == 0;
        changeEdit(isEdit);
    }


    public void changeEdit(Boolean isEdit) {
        ImageView imageView = new ImageView(context);
        TextView tvDescribe = new TextView(context);
        tvTag = new TextView(context);

        if (isEdit) {
            llRight = new LinearLayout(context);
            tvCountry = new IteeTextView(context);
            tvProvince = new IteeTextView(context);
            tvCity = new IteeTextView(context);
            etAddress = new IteeEditText(context);
            etPostCode = new IteeEditText(context);

            tvCountry.setTextSize(Constants.FONT_SIZE_NORMAL);
            tvProvince.setTextSize(Constants.FONT_SIZE_NORMAL);
            tvCity.setTextSize(Constants.FONT_SIZE_NORMAL);
            etAddress.setTextSize(Constants.FONT_SIZE_NORMAL);
            etPostCode.setTextSize(Constants.FONT_SIZE_NORMAL);

            tvProvince.setBackground(null);
            tvCity.setBackground(null);
            etAddress.setBackground(null);
            etPostCode.setBackground(null);

            tvCountry.setBackgroundResource(R.drawable.list_item_bottom_line);
            tvProvince.setBackgroundResource(R.drawable.list_item_bottom_line);
            tvCity.setBackgroundResource(R.drawable.list_item_bottom_line);
            etAddress.setBackgroundResource(R.drawable.list_item_bottom_line);

            tvCountry.setPadding(0, 0, 0, 0);
            tvProvince.setPadding(0, 0, 0, 0);
            tvCity.setPadding(0, 0, 0, 0);
            etAddress.setPadding(0, 0, 0, 0);
            etPostCode.setPadding(0, 0, 0, 0);


            int height = Utils.getHeight(context);

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
            tvTag.setTextSize(Constants.FONT_SIZE_NORMAL);
            tvTag.setSingleLine();

            if (isFirst) {
                tvTag.setTextColor(getResources().getColor(R.color.common_blue));
            } else {
                tvTag.setTextColor(getResources().getColor(R.color.common_black));
            }

            addView(llRight);
            LayoutParams layoutParamsDescribe = (LayoutParams) llRight.getLayoutParams();
            layoutParamsDescribe.height = LayoutParams.MATCH_PARENT;
            layoutParamsDescribe.width = (int) (Utils.getWidth(context) * 0.6f);
            layoutParamsDescribe.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            layoutParamsDescribe.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            llRight.setLayoutParams(layoutParamsDescribe);

            llRight.setOrientation(LinearLayout.VERTICAL);
            llRight.addView(tvCountry);
            LinearLayout.LayoutParams paramsCountry = (LinearLayout.LayoutParams) tvCountry.getLayoutParams();
            paramsCountry.height = (int) (height * 0.08f);
            paramsCountry.width = (int) (Utils.getWidth(context) * 0.6f);
            paramsCountry.gravity = Gravity.CENTER_VERTICAL;
            tvCountry.setLayoutParams(paramsCountry);

            llRight.addView(tvProvince);
            LinearLayout.LayoutParams paramsProvince = (LinearLayout.LayoutParams) tvProvince.getLayoutParams();
            paramsProvince.height = (int) (height * 0.08f);
            paramsProvince.width = (int) (Utils.getWidth(context) * 0.6f);
            paramsProvince.gravity = Gravity.CENTER_VERTICAL;
            tvProvince.setLayoutParams(paramsProvince);

            llRight.addView(tvCity);

            LinearLayout.LayoutParams paramsCity = (LinearLayout.LayoutParams) tvCity.getLayoutParams();
            paramsCity.height = (int) (height * 0.08f);
            paramsCity.width = (int) (Utils.getWidth(context) * 0.6f);
            paramsCity.gravity = Gravity.CENTER_VERTICAL;
            tvCity.setLayoutParams(paramsCity);

            llRight.addView(etAddress);

            LinearLayout.LayoutParams paramsAddress = (LinearLayout.LayoutParams) etAddress.getLayoutParams();
            paramsAddress.height = (int) (height * 0.08f);
            paramsAddress.width = (int) (Utils.getWidth(context) * 0.6f);
            paramsAddress.gravity = Gravity.CENTER_VERTICAL;
            etAddress.setLayoutParams(paramsAddress);


            llRight.addView(etPostCode);

            LinearLayout.LayoutParams paramsPostCode = (LinearLayout.LayoutParams) etPostCode.getLayoutParams();
            paramsPostCode.height = (int) (height * 0.08f);
            paramsPostCode.width = (int) (Utils.getWidth(context) * 0.6f);
            paramsPostCode.gravity = Gravity.CENTER_VERTICAL;
            etPostCode.setLayoutParams(paramsPostCode);

//            Drawable imTelDrawable = getResources().getDrawable(R.drawable.icon_right_arrow);
//            tvCity.setCompoundDrawablesWithIntrinsicBounds(imTelDrawable, null, null, null);
        } else {
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
            tvDescribe.setGravity(Gravity.END);

            tvDescribe.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            if (isFirst) {
                tvDescribe.setTextColor(getResources().getColor(R.color.common_blue));
                tvTag.setTextColor(getResources().getColor(R.color.common_blue));
            } else {
                tvDescribe.setTextColor(getResources().getColor(R.color.common_black));
                tvTag.setTextColor(getResources().getColor(R.color.common_black));
            }

        }


        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClickListener.OnAddressDeleteClick("retDelete", addressItem);
                parentView.removeView(thisLL);

            }
        });


        tvTag.setText(addressItem.getTag());
        if (isEdit) {
            setBackground(getResources().getDrawable(R.drawable.list_item_bottom_line));
            tvCountry.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            tvProvince.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            tvCity.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            etAddress.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            etPostCode.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);

            tvCountry.setText(addressItem.getCountry());
            tvProvince.setText(addressItem.getProvince());
            tvCity.setText(addressItem.getCity());
            etAddress.setText(addressItem.getValue());
            etPostCode.setText(addressItem.getZip());
            tvTag.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            tvProvince.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            tvCity.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            etAddress.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            etPostCode.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);


            tvCountry.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindowToken(), 0);

                    MainActivity mainActivity = (MainActivity) context;
                    Utils.hideKeyboard(mainActivity);

//                    Bundle bundle = new Bundle();
//                    bundle.putString("index", String.valueOf(mIndex));
//                    mainActivity.pushFragment(CityListFragment.class, bundle);

                    Intent intent = new Intent(context, SelectAddressActivity.class);
                    intent.putExtra(TransKey.COMMON_INDEX, String.valueOf(mIndex));
                    intent.putExtra(TransKey.COMMON_ADDRESS_COUNTRY, addressItem.getCountry());
                    intent.putExtra(TransKey.COMMON_ADDRESS_PROVINCE, addressItem.getProvince());
                    intent.putExtra(TransKey.COMMON_ADDRESS_CITY, addressItem.getCity());
                    mFragment.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_ADDRESS);

                }
            });

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
                            addressItem.setTag(tag);

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

            tvProvince.addTextChangedListener(new TextWatcher() {
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

                    JsonGeneralInfo.AddressItem phoneItemTemp = (JsonGeneralInfo.AddressItem) thisLL.getTag();
                    phoneItemTemp.setProvince(temp.toString());

                }
            });

            tvCity.addTextChangedListener(new TextWatcher() {
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

                    JsonGeneralInfo.AddressItem phoneItemTemp = (JsonGeneralInfo.AddressItem) thisLL.getTag();
                    phoneItemTemp.setCity(temp.toString());

                }
            });

            etAddress.addTextChangedListener(new TextWatcher() {
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

                    JsonGeneralInfo.AddressItem phoneItemTemp = (JsonGeneralInfo.AddressItem) thisLL.getTag();
                    phoneItemTemp.setValue(temp.toString());

                }
            });

            etPostCode.addTextChangedListener(new TextWatcher() {
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

                    JsonGeneralInfo.AddressItem phoneItemTemp = (JsonGeneralInfo.AddressItem) thisLL.getTag();
                    phoneItemTemp.setZip(temp.toString());

                }
            });


        } else {
            imageView.setVisibility(GONE);
            setBackground(null);
            LayoutParams layoutParamsName = (LayoutParams) tvTag.getLayoutParams();
            layoutParamsName.height = LayoutParams.WRAP_CONTENT;
            layoutParamsName.width = (int) (Utils.getWidth(context) * 0.2f);
            layoutParamsName.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            layoutParamsName.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
            tvTag.setLayoutParams(layoutParamsName);

            LayoutParams layoutParamsDescribe = (LayoutParams) tvDescribe.getLayoutParams();
            layoutParamsDescribe.width = (int) (Utils.getWidth(context) * 0.8f);
            tvDescribe.setLayoutParams(layoutParamsDescribe);

            tvDescribe.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            tvDescribe.setEllipsize(TextUtils.TruncateAt.END);
            if (addressItem != null) {
                String split = ", ";
                tvTag.setText(addressItem.getTag());
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(addressItem.getCountry());
                stringBuffer.append(split);
                stringBuffer.append(addressItem.getProvince());
                stringBuffer.append(split);
                stringBuffer.append(addressItem.getCity());
                stringBuffer.append(split);
                stringBuffer.append(addressItem.getValue());
                stringBuffer.append(split);
                stringBuffer.append(addressItem.getZip());

                tvDescribe.setText(stringBuffer);
            }
        }
    }

    public void setCountry(String country) {
        tvCountry.setText(country);
        addressItem.setCountry(country);
    }

    public void setProvince(String province) {
        tvProvince.setText(province);
        addressItem.setProvince(province);
    }

    public void setCity(String city) {
        tvCity.setText(city);
        addressItem.setCity(city);
    }

    public void setPostCode(String postCode) {
        etPostCode.setText(postCode);
        addressItem.setZip(postCode);
    }
}
