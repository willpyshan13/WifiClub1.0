package cn.situne.itee.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonShoppingCustomerListGet;
import cn.situne.itee.view.IteeSearchView;
import cn.situne.itee.view.IteeTextView;


public class ShoppingSearchProfileActivity extends FragmentActivity {

    protected RelativeLayout rlContainer;

    private IteeTextView footerMes;

    private IteeTextView tvLeftTitle;

    private IteeSearchView titleSearchView;
    private PullToRefreshListView searchDataList;
    private ListViewAdapter listViewAdapter;
    private ArrayList<JsonShoppingCustomerListGet.DataItem> dataList;

    private int currentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataList = new ArrayList<>();
        currentPage = 1;

        setContentView(R.layout.activity_shopping_search);
        rlContainer = (RelativeLayout) findViewById(com.mining.app.zxing.R.id.rl_parent);
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        if (getActionBar() != null) {
            getActionBar().hide();
        }

        RelativeLayout rlActionBar = new RelativeLayout(this);
       // rlActionBar.setBackgroundResource(R.drawable.bg_action_bar);

        rlActionBar.setBackgroundColor(getResources().getColor(R.color.common_light_gray));
        RelativeLayout.LayoutParams rlActionBarLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, actionBarHeight);
        rlActionBarLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlActionBar.setLayoutParams(rlActionBarLayoutParams);

        rlActionBar.setId(View.generateViewId());


        rlContainer.addView(rlActionBar);

        RelativeLayout.LayoutParams searchDataListParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        searchDataListParams.addRule(RelativeLayout.BELOW, rlActionBar.getId());
        searchDataList = new PullToRefreshListView(getApplication());
        searchDataList.setLayoutParams(searchDataListParams);
        searchDataList.getRefreshableView().setDivider(new ColorDrawable(Color.GRAY));
        searchDataList.getRefreshableView().setDividerHeight(getHeight(1));
        searchDataList.setMode(PullToRefreshBase.Mode.BOTH);
        searchDataList.setVisibility(View.GONE);





        searchDataList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getCustomerListData(titleSearchView.getText().toString(),true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getCustomerListData(titleSearchView.getText().toString(),false);
            }
        });


        rlContainer.addView(searchDataList);

        // setBackArrowAndLeftTitle(rlActionBar);
        initTitleView(rlActionBar);
        RelativeLayout rlInputAndBtn = new RelativeLayout(this);
        rlInputAndBtn.setBackgroundColor(getResources().getColor(R.color.common_white));

        RelativeLayout.LayoutParams rlInputAndBtnLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        rlInputAndBtnLayoutParams.setMargins(0, actionBarHeight, 0, 0);
        rlInputAndBtn.setLayoutParams(rlInputAndBtnLayoutParams);

        addFooterView();

        listViewAdapter = new ListViewAdapter();

        searchDataList.setAdapter(listViewAdapter);

        getCustomerListData("",true);
    }

    private int setBackArrow(RelativeLayout parent) {
        RelativeLayout rlIconContainer = new RelativeLayout(this);
        rlIconContainer.setId(View.generateViewId());

        RelativeLayout.LayoutParams rlIconContainerLayoutParams = new RelativeLayout.LayoutParams(getWidth(55), ViewGroup.LayoutParams.MATCH_PARENT);
        rlIconContainerLayoutParams.leftMargin = getWidth(20);
        rlIconContainer.setLayoutParams(rlIconContainerLayoutParams);

        parent.addView(rlIconContainer);

        ImageView ivIcon = new ImageView(this);
        ivIcon.setId(View.generateViewId());
        ivIcon.setBackgroundResource(R.drawable.icon_back);
        ivIcon.setScaleType(ImageView.ScaleType.FIT_XY);
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        RelativeLayout.LayoutParams paramsIvIcon = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsIvIcon.width = getWidth(50);
        paramsIvIcon.height = getWidth(50);
        paramsIvIcon.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        ivIcon.setLayoutParams(paramsIvIcon);
        rlIconContainer.addView(ivIcon);

        ImageView ivSeparator = new ImageView(this);
        ivSeparator.setId(View.generateViewId());
        ivSeparator.setImageResource(R.drawable.icon_separator);
        rlIconContainer.addView(ivSeparator);

        RelativeLayout.LayoutParams paramsIvSeparator = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsIvSeparator.width = 5;
        paramsIvSeparator.height = 50;
        paramsIvSeparator.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsIvSeparator.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsIvSeparator.rightMargin = 5;
        ivSeparator.setLayoutParams(paramsIvSeparator);

        Button btn = new Button(this);
        RelativeLayout.LayoutParams btnLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        btn.setLayoutParams(btnLayoutParams);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        btn.setBackground(null);
        rlIconContainer.addView(btn);

        return rlIconContainer.getId();
    }

    public int getWidth(float currentPx) {
        return (int) (currentPx / Constants.DESIGN_UI_WIDTH * (getResources().getDisplayMetrics().widthPixels));
    }

    public int getHeight(float currentPx) {
        return (int) (currentPx / Constants.DESIGN_UI_HEIGHT * (getResources().getDisplayMetrics().heightPixels));
    }

    protected int getWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    public int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (ShoppingSearchProfileActivity.this.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    private void initTitleView(RelativeLayout actionBar) {
        LinearLayout.LayoutParams titleViewParams = new LinearLayout.LayoutParams(Utils.getWidth(this), getActionBarHeight());
        LinearLayout titleView = new LinearLayout(ShoppingSearchProfileActivity.this);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(Utils.getWidth(this) - getWidth(160), getActionBarHeight() - 20);
        titleView.setGravity(Gravity.CENTER_VERTICAL);
        titleView.setPadding(0, 0, 0, 0);

        titleView.setBackgroundColor(getResources().getColor(R.color.common_light_gray));
        titleView.setLayoutParams(titleViewParams);

        titleSearchView = new IteeSearchView(ShoppingSearchProfileActivity.this);
        titleSearchView.setHint(getString(R.string.common_search));
        titleSearchView.setTextSize(14);
        titleSearchView.setBackground(getResources().getDrawable(R.drawable.bg_search_view));
        titleSearchView.setLayoutParams(llp);
        titleSearchView.setIcon(getActionBarHeight() - 20, R.drawable.icon_search_del, R.drawable.icon_search_del, R.drawable.icon_search, R.drawable.icon_search);
        titleSearchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_UP == event.getAction()) {
                    if (keyCode == 66) {

                        Utils.hideKeyboard(ShoppingSearchProfileActivity.this);
                        //api
                        getCustomerListData(titleSearchView.getText().toString(),true);
                    }
                }

                return false;
            }
        });

        titleSearchView.setRightIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleSearchView.setText(StringUtils.EMPTY);
            }
        });
        titleSearchView.setLeftIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(ShoppingSearchProfileActivity.this);
                getCustomerListData(titleSearchView.getText().toString(),true);
            }
        });

        titleSearchView.requestFocus();
        Utils.showKeyboard(titleSearchView, ShoppingSearchProfileActivity.this);

        LinearLayout.LayoutParams titleSearchViewLayoutParams
                = (LinearLayout.LayoutParams) titleSearchView.getLayoutParams();
        titleSearchViewLayoutParams.leftMargin = DensityUtil.getActualWidthOnThisDevice(10, ShoppingSearchProfileActivity.this);
        titleSearchView.setLayoutParams(titleSearchViewLayoutParams);

        titleView.addView(titleSearchView);

        Button cancelBtn  = new Button(this);
        cancelBtn.setText(getString(R.string.common_cancel));
        cancelBtn.setBackgroundColor(Color.TRANSPARENT);
        cancelBtn.setTextColor(getResources().getColor(R.color.common_blue));
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(TransKey.IS_BACK, true);
                ShoppingSearchProfileActivity.this.setResult(RESULT_OK, intent);
                finish();
            }
        });
        cancelBtn.setTextSize(Constants.FONT_SIZE_NORMAL);
        titleView.addView(cancelBtn);
        actionBar.addView(titleView);


    }

    private void addFooterView() {
        LinearLayout llFooterView = new LinearLayout(this);
        llFooterView.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams footerMesParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        footerMesParams.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, this);
        footerMesParams.bottomMargin = DensityUtil.getActualHeightOnThisDevice(5, this);
        footerMesParams.topMargin = DensityUtil.getActualHeightOnThisDevice(5, this);
        footerMes = new IteeTextView(this);
        footerMes.setLayoutParams(footerMesParams);
        footerMes.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        footerMes.setVisibility(View.GONE);
        footerMes.setSingleLine(false);
        llFooterView.addView(footerMes);

        //searchDataList.addFooterView(llFooterView);
    }

    private void getCustomerListData(String name, final boolean isRefresh) {


        if (isRefresh) {
            currentPage = 1;
        }
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(ShoppingSearchProfileActivity.this));
            params.put(ApiKey.COMMON_PAGE, String.valueOf(currentPage));
            params.put(ApiKey.SHOPPING_KEYWORD, name);

            HttpManager<JsonShoppingCustomerListGet> hh = new HttpManager<JsonShoppingCustomerListGet>(ShoppingSearchProfileActivity.this) {

                @Override
                public void onJsonSuccess(JsonShoppingCustomerListGet jo) {


                    Integer returnCode = jo.getReturnCode();

                    String msg = jo.getReturnInfo();
                    if (returnCode == Constants.RETURN_CODE_20301) {
                        searchDataList.setVisibility(View.VISIBLE);
                        currentPage++;
                        if (isRefresh) {
                            dataList.clear();
                            dataList = jo.getDataList();
                        }else{
                            dataList.addAll(jo.getDataList());
                        }


                        listViewAdapter.notifyDataSetChanged();

                        searchDataList.onRefreshComplete();
                        if (dataList.size() <= 0) {
                            footerMes.setText(StringUtils.EMPTY);
                            SpannableString ss = new SpannableString(titleSearchView.getText());
                            ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.common_blue)), 0, ss.length(),
                                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            ss.setSpan(new RelativeSizeSpan(1.3f), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                            footerMes.setVisibility(View.VISIBLE);

                            footerMes.append(getString(R.string.add_tee_time_setting_search_mes1));
                            footerMes.append(ss);
                            footerMes.append(getString(R.string.add_tee_time_setting_search_mes2));
                        } else {
                            footerMes.setVisibility(View.GONE);
                        }
                    } else {
                        Utils.showShortToast(ShoppingSearchProfileActivity.this, msg);
                    }
                }

                @Override
                public void onJsonError(VolleyError error) {

                }

            };
            hh.startGet(ShoppingSearchProfileActivity.this, ApiManager.HttpApi.ShoppingCustomerListGet, params);

    }

    class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ListViewItem item = new ListViewItem(ShoppingSearchProfileActivity.this.getApplication(), getHeight(65), getWidth(40));
            // item.setItemValue(dataList.get(position));
            item.setData(dataList.get(position));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.hideKeyboard(ShoppingSearchProfileActivity.this);


                    Intent intent = new Intent();
                    intent.putExtra(TransKey.BOOKING_CODE, dataList.get(position).getBkNoAll());
                    ShoppingSearchProfileActivity.this.setResult(RESULT_OK, intent);
                    finish();
                }
            });
            return item;
        }
    }

    class ListViewItem extends LinearLayout {

        //row 1
        private IteeTextView tvName;
        private IteeTextView tvCm;
        //row2
        private IteeTextView tvBookingNo;
        private IteeTextView tvDate;
        //row3
        private IteeTextView tvGreen;

        @SuppressLint("WrongViewCast")
        public ListViewItem(Context context, int itemHeight, int padding) {
            super(context);
            this.setOrientation(VERTICAL);

            //row1
            LinearLayout.LayoutParams row1Params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            LinearLayout row1 = new LinearLayout(context);
            row1.setLayoutParams(row1Params);
            row1.setOrientation(HORIZONTAL);

            LinearLayout.LayoutParams tvNameParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, itemHeight);
            tvNameParams.weight = 1;
            tvName = new IteeTextView(context);
            tvName.setTextColor(context.getResources().getColor(R.color.common_blue));
            tvName.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            tvName.setLayoutParams(tvNameParams);

            tvName.setTextSize(Constants.FONT_SIZE_MORE_LARGER);
            tvName.setPadding(padding, 0, 0, 0);


            LinearLayout.LayoutParams tvCmParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, itemHeight);
            tvCmParams.weight = 1;

            tvCm = new IteeTextView(context);
            tvCm.setTextColor(context.getResources().getColor(R.color.common_black));
            tvCm.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

            tvCm.setLayoutParams(tvCmParams);

            tvCm.setPadding(0, 0, padding, 0);
            row1.addView(tvName);
            row1.addView(tvCm);
            //row 2

            LinearLayout.LayoutParams row2Params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            LinearLayout row2 = new LinearLayout(context);
            row2.setLayoutParams(row2Params);


            LinearLayout.LayoutParams tvBookingNoParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, itemHeight);
            tvBookingNoParams.weight = 1;
            tvBookingNo = new IteeTextView(context);
            tvBookingNo.setTextColor(context.getResources().getColor(R.color.common_black));
            tvBookingNo.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            tvBookingNo.setLayoutParams(tvBookingNoParams);


            tvBookingNo.setPadding(padding, 0, 0, 0);

            LinearLayout.LayoutParams tvDateParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, itemHeight);
            tvDateParams.weight = 1;

            tvDate = new IteeTextView(context);
            tvDate.setTextColor(context.getResources().getColor(R.color.common_black));
            tvDate.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

            tvDate.setLayoutParams(tvCmParams);

            tvDate.setPadding(0, 0, padding, 0);
            row2.addView(tvBookingNo);
            row2.addView(tvDate);


            // row3
            LinearLayout.LayoutParams row3Params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            LinearLayout row3 = new LinearLayout(context);
            row3.setLayoutParams(row3Params);


            LinearLayout.LayoutParams tvGreenParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, itemHeight);
            tvGreenParams.weight = 1;
            tvGreen = new IteeTextView(context);
            tvGreen.setTextColor(context.getResources().getColor(R.color.common_black));
            tvGreen.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            tvGreen.setLayoutParams(tvGreenParams);

            tvGreen.setPadding(padding, 0, 0, 0);
            row3.addView(tvGreen);
            this.addView(row1);
            this.addView(row2);
            this.addView(row3);
        }

        public void setData(JsonShoppingCustomerListGet.DataItem dataItem) {
            tvBookingNo.setText(dataItem.getBkNo());
            tvDate.setText(dataItem.getDateTime());
            tvGreen.setText(dataItem.getHolesArea());
            tvCm.setText(dataItem.getMemCard());
            tvName.setText(dataItem.getName());
        }


    }
}
