package cn.situne.itee.adapter;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.BaseViewHolder;
import cn.situne.itee.entity.PurchaseRefundExpandable;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.player.PlayerRefundFragment;
import cn.situne.itee.manager.jsonentity.JsonPurchaseHistoryDetailRecord;
import cn.situne.itee.view.IteeMoneyEditText;
import cn.situne.itee.view.IteeNumberEditView;
import cn.situne.itee.view.IteeTextView;

public class RefundDetailAdapter extends BaseExpandableListAdapter {

    public static final int TYPE_GOOD = 0;
    public static final int TYPE_AA = 1;
    public static final int TYPE_PACKAGE = 2;
    public static final int TYPE_REFUND = 3;

    public static final int TYPE_TIMES = 4;
    PlayerRefundFragment.OnDateSelectClickListener mListener;
    List<PurchaseRefundExpandable> data;
    BaseFragment mBaseFragment;
    ExpandableListView mListView;
    private Map<Integer, PurchaseRefundExpandable> checkedItem;
    private String currency;
    private int allChildCount;
    private int allCount;


    public RefundDetailAdapter(BaseFragment fragment, ExpandableListView listview,
                               List<PurchaseRefundExpandable> data, PlayerRefundFragment.OnDateSelectClickListener listener, int allChildCount) {

        this.data = data;
        this.mBaseFragment = fragment;
        this.mListView = listview;
        mListener = listener;
        this.allChildCount = allChildCount;
        currency = AppUtils.getCurrentCurrency(mBaseFragment.getActivity());
        checkedItem = new HashMap<>();
        if (data != null) {
            allCount = data.size() + allChildCount;
        }


    }

    public Map<Integer, PurchaseRefundExpandable> getCheckedItem() {
        return checkedItem;
    }

    public void setCheckedItem(Map<Integer, PurchaseRefundExpandable> checkedItem) {
        this.checkedItem = checkedItem;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return data.get(groupPosition).getGoodList().get(childPosition);

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        final ChildViewHolder holder;
        if (convertView == null) {

            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(mBaseFragment.getActivity()).inflate(R.layout.item_of_refund_child, null);

            holder.rlChildRefunded = (RelativeLayout) convertView.findViewById(R.id.rl_background_refunded);
            holder.rlChildNormal = (RelativeLayout) convertView.findViewById(R.id.rl_background_normal);
            holder.textViewName = (IteeTextView) convertView.findViewById(R.id.tv_name);
            holder.textViewPrice = (IteeTextView) convertView.findViewById(R.id.tv_price);
            holder.textViewCount = (IteeTextView) convertView.findViewById(R.id.tv_count);
            holder.cb_child = (CheckBox) convertView.findViewById(R.id.cb_child);
            holder.llBottom = (LinearLayout) convertView.findViewById(R.id.ll_bottom_container);


            holder.nev = new IteeNumberEditView(mBaseFragment);
            holder.refundPrice = new IteeMoneyEditText(mBaseFragment);
            holder.warn = new IteeTextView(mBaseFragment.getActivity());
            holder.tvCurrency = new IteeTextView(mBaseFragment.getActivity());
            holder.warn.setVisibility(View.GONE);
            holder.refundPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            holder.refundPrice.setBackground(mBaseFragment.getDrawable(R.drawable.textview_corner));
            holder.tvCurrency.setTextColor(mBaseFragment.getColor(R.color.common_white));
            holder.tvCurrency.setText(AppUtils.getCurrentCurrency(mBaseFragment.getActivity()));
            holder.warn.setBackground(mBaseFragment.getDrawable(R.drawable.icon_conflict_warn));

            holder.llBottom.addView(holder.nev);
            holder.llBottom.addView(holder.tvCurrency);
            holder.llBottom.addView(holder.refundPrice);
            holder.llBottom.addView(holder.warn);


            LinearLayout.LayoutParams paramsLL = (LinearLayout.LayoutParams) holder.llBottom.getLayoutParams();
            paramsLL.width = LinearLayout.LayoutParams.MATCH_PARENT;
            paramsLL.height = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.08f);
            holder.llBottom.setLayoutParams(paramsLL);


            RelativeLayout.LayoutParams paramsRL = (RelativeLayout.LayoutParams) holder.rlChildNormal.getLayoutParams();
            paramsRL.width = LinearLayout.LayoutParams.MATCH_PARENT;
            paramsRL.height = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.08f);
            holder.rlChildNormal.setLayoutParams(paramsRL);
            holder.rlChildRefunded.setLayoutParams(paramsRL);

            LinearLayout.LayoutParams paramsWarn = (LinearLayout.LayoutParams) holder.warn.getLayoutParams();
            paramsWarn.width = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.02f);
            paramsWarn.height = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.02f);
            paramsWarn.gravity = Gravity.CENTER_VERTICAL;
            holder.warn.setLayoutParams(paramsWarn);

            LinearLayout.LayoutParams paramstvCurrency = (LinearLayout.LayoutParams) holder.tvCurrency.getLayoutParams();
            paramstvCurrency.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            paramstvCurrency.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            paramstvCurrency.gravity = Gravity.CENTER_VERTICAL;
            paramstvCurrency.setMargins(20, 0, 0, 0);
            holder.tvCurrency.setLayoutParams(paramstvCurrency);


            LinearLayout.LayoutParams paramsNev = (LinearLayout.LayoutParams) holder.nev.getLayoutParams();
            paramsNev.width = (int) (Utils.getWidth(mBaseFragment.getActivity()) * 0.4f);
            paramsNev.height = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.06f);
            paramsNev.gravity = Gravity.CENTER_VERTICAL;
            paramsNev.setMargins(20, 0, 0, 0);
            holder.nev.setLayoutParams(paramsNev);

            LinearLayout.LayoutParams paramsRefundCount = (LinearLayout.LayoutParams) holder.refundPrice.getLayoutParams();
            paramsRefundCount.width = (int) (Utils.getWidth(mBaseFragment.getActivity()) * 0.4f);
            paramsRefundCount.height = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.06f);
            paramsRefundCount.setMargins(20, 0, 0, 0);
            paramsRefundCount.gravity = Gravity.CENTER_VERTICAL;
            holder.refundPrice.setLayoutParams(paramsRefundCount);
            holder.refundPrice.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            holder.refundPrice.setPadding(0, 0, 0, 0);


            RelativeLayout.LayoutParams paramsIvArrow = (RelativeLayout.LayoutParams) holder.textViewName.getLayoutParams();
            paramsIvArrow.width = (int) (Utils.getWidth(mBaseFragment.getActivity()) * 0.4f);
            paramsIvArrow.height = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.08f);
            paramsIvArrow.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            paramsIvArrow.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            paramsIvArrow.setMargins(40, 0, 0, 0);
            holder.textViewName.setLayoutParams(paramsIvArrow);
            holder.textViewName.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

            RelativeLayout.LayoutParams paramsTextView = (RelativeLayout.LayoutParams) holder.textViewPrice.getLayoutParams();
            paramsTextView.width = (int) (Utils.getWidth(mBaseFragment.getActivity()) * 0.4f);
            paramsTextView.height = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.04f);
            paramsTextView.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            paramsTextView.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            paramsTextView.setMargins(0, 0, 20, 0);
            holder.textViewPrice.setLayoutParams(paramsTextView);
            holder.textViewPrice.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            holder.textViewPrice.setId(View.generateViewId());

            RelativeLayout.LayoutParams paramsIvIcon = (RelativeLayout.LayoutParams) holder.textViewCount.getLayoutParams();
            paramsIvIcon.width = (int) (Utils.getWidth(mBaseFragment.getActivity()) * 0.4f);
            paramsIvIcon.height = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.04f);
            paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            paramsIvIcon.addRule(RelativeLayout.BELOW, holder.textViewPrice.getId());
            paramsIvIcon.setMargins(0, 0, 20, 0);
            holder.textViewCount.setLayoutParams(paramsIvIcon);
            holder.textViewCount.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            convertView.setTag(holder);

        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }



        PurchaseRefundExpandable.GoodListItem goodListItem = data.get(groupPosition).getGoodList().get(childPosition);
        if (goodListItem.isCheck()) {
            holder.llBottom.setVisibility(View.VISIBLE);

            holder.nev.setMinNum(1);
            holder.nev.setMaxNum(goodListItem.getCount());
            holder.nev.setCurrentNum(goodListItem.getCount());

            BigDecimal countPrice = new BigDecimal(goodListItem.getPrice()).multiply(new BigDecimal(goodListItem.getCount()));
            holder.refundPrice.setText(Utils.get2DigitDecimalString(countPrice.toString()));
            holder.nev.setListener(new IteeNumberEditView.NumberEditListener() {
                @Override
                public void onAdd(int currentNum) {
                    checkedItem.get(groupPosition).getGoodList().get(childPosition).setCount(currentNum);
                }

                @Override
                public void onMinus(int currentNum) {
                    checkedItem.get(groupPosition).getGoodList().get(childPosition).setCount(currentNum);
                }
            });

            holder.refundPrice.addTextChangedListener(new TextWatcher() {
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
                    temp = temp.toString().replace(AppUtils.getCurrentCurrency(mBaseFragment.getActivity()), StringUtils.EMPTY);
                    if (Utils.isStringNotNullOrEmpty(temp.toString())) {

                        PurchaseRefundExpandable.GoodListItem goodListItem = data.get(groupPosition).getGoodList().get(childPosition);

                        BigDecimal changedNum = new BigDecimal(temp.toString());
                        BigDecimal price = new BigDecimal(goodListItem.getPrice()).multiply(new BigDecimal(goodListItem.getCount()));
                        if (changedNum.compareTo(price) == 1) {
                            holder.warn.setVisibility(View.VISIBLE);

                        } else {
                            holder.warn.setVisibility(View.GONE);
                            checkedItem.get(groupPosition).getGoodList().get(childPosition).setPrice(temp.toString());
                            mListener.OnGoodItemClick("add", StringUtils.EMPTY);


                        }
                    } else {
                        checkedItem.get(groupPosition).getGoodList().get(childPosition).setPrice(Constants.STR_0);
                        mListener.OnGoodItemClick("add", StringUtils.EMPTY);

                    }

                }
            });

        } else {
            holder.llBottom.setVisibility(View.GONE);
        }


        if (data.get(groupPosition) != null && Constants.STR_1.equals(String.valueOf(data.get(groupPosition).getRefundflag()))) {
            holder.rlChildRefunded.setVisibility(View.VISIBLE);
            holder.rlChildRefunded.bringToFront();
            holder.rlChildRefunded.setAlpha(0.8f);
            holder.rlChildRefunded.removeAllViews();
            IteeTextView text = new IteeTextView(mBaseFragment.getActivity());
            text.setText(mBaseFragment.getString(R.string.play_refunded));
            text.setTextColor(mBaseFragment.getColor(R.color.common_white));
            text.setTextSize(Constants.FONT_SIZE_MORE_LARGER);
            holder.rlChildRefunded.addView(text);
            RelativeLayout.LayoutParams refundedTextParam = (RelativeLayout.LayoutParams) text.getLayoutParams();
            refundedTextParam.addRule(RelativeLayout.CENTER_IN_PARENT);
            text.setLayoutParams(refundedTextParam);
            text.setGravity(Gravity.CENTER);
            holder.cb_child.setEnabled(false);
        } else {
            holder.rlChildRefunded.setVisibility(View.GONE);
            holder.rlChildNormal.bringToFront();
        }
        holder.textViewName.setText(goodListItem.getName());
        holder.textViewPrice.setText(currency + Utils.get2DigitDecimalString(goodListItem.getPrice()));
        holder.textViewCount.setText(Constants.STR_MULTIPLY + String.valueOf(goodListItem.getCount()));
        holder.textViewCount.setTextColor(mBaseFragment.getActivity().getResources().getColor(R.color.common_blue));




        return convertView;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (data.get(groupPosition).getGoodList() == null) {
            return 0;
        } else {
            return data.get(groupPosition).getGoodList().size();
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public int getGroupCount() {

        return data.size();

    }

    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;

    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final GroupViewHolder holder;

        if (convertView == null) {

            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(mBaseFragment.getActivity()).inflate(R.layout.item_of_refund_group, null);
            holder.rlGroupRefunded = (RelativeLayout) convertView.findViewById(R.id.rl_background_refunded);
            holder.rlGroupNormal = (RelativeLayout) convertView.findViewById(R.id.rl_background_normal);
            holder.textViewName = (IteeTextView) convertView.findViewById(R.id.tv_name);
            holder.textViewPrice = (IteeTextView) convertView.findViewById(R.id.tv_price);
            holder.textViewCount = (IteeTextView) convertView.findViewById(R.id.tv_count);
            holder.cb_group = (CheckBox) convertView.findViewById(R.id.cb_group);
            holder.llBottom = (LinearLayout) convertView.findViewById(R.id.ll_bottom_container);
            holder.voucherLayout = (LinearLayout) convertView.findViewById(R.id.voucherLayout);

            holder.nev = new IteeNumberEditView(mBaseFragment);
            holder.refundPrice = new IteeMoneyEditText(mBaseFragment);
            holder.tvCurrency = new IteeTextView(mBaseFragment.getActivity());
            holder.warn = new IteeTextView(mBaseFragment.getActivity());
            holder.warn.setVisibility(View.GONE);
            holder.refundPrice.setBackground(mBaseFragment.getDrawable(R.drawable.textview_corner));
            holder.tvCurrency.setTextColor(mBaseFragment.getColor(R.color.common_white));
            holder.tvCurrency.setText(AppUtils.getCurrentCurrency(mBaseFragment.getActivity()));
            holder.warn.setBackground(mBaseFragment.getDrawable(R.drawable.icon_conflict_warn));

            holder.llBottom.addView(holder.nev);
            holder.llBottom.addView(holder.tvCurrency);
            holder.llBottom.addView(holder.refundPrice);
            holder.llBottom.addView(holder.warn);


            LinearLayout.LayoutParams paramsLL = (LinearLayout.LayoutParams) holder.llBottom.getLayoutParams();
            paramsLL.width = LinearLayout.LayoutParams.MATCH_PARENT;
            paramsLL.height = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.08f);
            holder.llBottom.setLayoutParams(paramsLL);


            RelativeLayout.LayoutParams paramsRL = (RelativeLayout.LayoutParams) holder.rlGroupNormal.getLayoutParams();
            paramsRL.width = LinearLayout.LayoutParams.MATCH_PARENT;
            paramsRL.height = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.08f);
            holder.rlGroupNormal.setLayoutParams(paramsRL);
            holder.rlGroupRefunded.setLayoutParams(paramsRL);

            LinearLayout.LayoutParams paramsWarn = (LinearLayout.LayoutParams) holder.warn.getLayoutParams();
            paramsWarn.width = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.02f);
            paramsWarn.height = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.02f);
            paramsWarn.gravity = Gravity.CENTER_VERTICAL;
            holder.warn.setLayoutParams(paramsWarn);
            holder.warn.setGravity(Gravity.CENTER_VERTICAL);


            LinearLayout.LayoutParams paramstvCurrency = (LinearLayout.LayoutParams) holder.tvCurrency.getLayoutParams();
            paramstvCurrency.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            paramstvCurrency.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            paramstvCurrency.gravity = Gravity.CENTER_VERTICAL;
            paramstvCurrency.setMargins(20, 0, 0, 0);
            holder.tvCurrency.setLayoutParams(paramstvCurrency);

            LinearLayout.LayoutParams paramsNev = (LinearLayout.LayoutParams) holder.nev.getLayoutParams();
            paramsNev.width = (int) (Utils.getWidth(mBaseFragment.getActivity()) * 0.4f);
            paramsNev.height = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.06f);
            paramsNev.gravity = Gravity.CENTER_VERTICAL;
            paramsNev.setMargins(20, 0, 0, 0);
            holder.nev.setLayoutParams(paramsNev);
            holder.nev.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

            LinearLayout.LayoutParams paramsRefundCount = (LinearLayout.LayoutParams) holder.refundPrice.getLayoutParams();
            paramsRefundCount.width = (int) (Utils.getWidth(mBaseFragment.getActivity()) * 0.4f);
            paramsRefundCount.height = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.06f);
            paramsRefundCount.gravity = Gravity.CENTER_VERTICAL;
            paramsRefundCount.setMargins(20, 0, 0, 0);
            holder.refundPrice.setLayoutParams(paramsRefundCount);
            holder.refundPrice.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);


            holder.cb_group.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams paramsIvCheckBtn = (RelativeLayout.LayoutParams) holder.cb_group.getLayoutParams();
            paramsIvCheckBtn.width = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.04f);
            paramsIvCheckBtn.height = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.04f);
            paramsIvCheckBtn.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            paramsIvCheckBtn.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            paramsIvCheckBtn.setMargins(20, 0, 0, 0);
            holder.cb_group.setLayoutParams(paramsIvCheckBtn);
            holder.cb_group.setGravity(Gravity.CENTER_VERTICAL);
            holder.cb_group.setId(View.generateViewId());

            RelativeLayout.LayoutParams paramsIvArrow = (RelativeLayout.LayoutParams) holder.textViewName.getLayoutParams();
            paramsIvArrow.width = (int) (Utils.getWidth(mBaseFragment.getActivity()) * 0.4f);
            paramsIvArrow.height = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.08f);
            paramsIvArrow.addRule(RelativeLayout.RIGHT_OF, holder.cb_group.getId());
            paramsIvArrow.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            paramsIvArrow.setMargins(20, 0, 0, 0);
            holder.textViewName.setLayoutParams(paramsIvArrow);
            holder.textViewName.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

            RelativeLayout.LayoutParams paramsTextView = (RelativeLayout.LayoutParams) holder.textViewPrice.getLayoutParams();
            paramsTextView.width = (int) (Utils.getWidth(mBaseFragment.getActivity()) * 0.4f);
            paramsTextView.height = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.04f);
            paramsTextView.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            paramsTextView.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            paramsTextView.setMargins(0, 0, 20, 0);
            holder.textViewPrice.setLayoutParams(paramsTextView);
            holder.textViewPrice.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            holder.textViewPrice.setId(View.generateViewId());

            RelativeLayout.LayoutParams paramsIvIcon = (RelativeLayout.LayoutParams) holder.textViewCount.getLayoutParams();
            paramsIvIcon.width = (int) (Utils.getWidth(mBaseFragment.getActivity()) * 0.4f);
            paramsIvIcon.height = (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.04f);
            paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            paramsIvIcon.addRule(RelativeLayout.BELOW, holder.textViewPrice.getId());
            paramsIvIcon.setMargins(0, 0, 20, 0);
            holder.textViewCount.setLayoutParams(paramsIvIcon);
            holder.textViewCount.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            convertView.setTag(holder);

        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        holder.voucherLayout.removeAllViews();

        if (data.get(groupPosition).getVoucherItems()!=null){
            for(JsonPurchaseHistoryDetailRecord.VoucherItem voucherItem :  data.get(groupPosition).getVoucherItems()){
                LinearLayout.LayoutParams voucherLayoutParams = new  LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,mBaseFragment.getActualHeightOnThisDevice(60));
                RelativeLayout voucherLayout = new RelativeLayout(mBaseFragment.getBaseActivity());
                voucherLayout.setLayoutParams(voucherLayoutParams);


                RelativeLayout.LayoutParams t1Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.MATCH_PARENT);
                t1Params.leftMargin  = mBaseFragment.getActualWidthOnThisDevice(100);
                IteeTextView t1 = new IteeTextView(mBaseFragment.getBaseActivity());
                t1.setLayoutParams(t1Params);
                t1.setText(mBaseFragment.getString(R.string.shopping_voucher));
                t1.setGravity(Gravity.CENTER_VERTICAL);


                IteeTextView t2 = new IteeTextView(mBaseFragment.getBaseActivity());
                RelativeLayout.LayoutParams t2Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.MATCH_PARENT);
                t2Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                t2Params.rightMargin = mBaseFragment.getActualWidthOnThisDevice(20);
                t2.setLayoutParams(t2Params);
                t2.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity()) + Constants.STR_SEPARATOR + voucherItem.getVoucherMoney());


                voucherLayout.addView(t1);
                voucherLayout.addView(t2);
                holder.voucherLayout.addView(voucherLayout);
            }

        }


        final PurchaseRefundExpandable expandable = data.get(groupPosition);


        holder.cb_group.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                switch (expandable.getType()) {
                    case TYPE_GOOD:
                        if (isChecked) {
                            holder.llBottom.setVisibility(View.VISIBLE);

                            PurchaseRefundExpandable from = (PurchaseRefundExpandable) getGroup(groupPosition);

                            BigDecimal countPrice = new BigDecimal(from.getPrice()).multiply(new BigDecimal(from.getCount()));
                            holder.refundPrice.setText(Utils.get2DigitDecimalString(countPrice.toString()));

                            PurchaseRefundExpandable to = new PurchaseRefundExpandable();
                            to.setId(from.getId());
                            to.setCount(from.getCount());
                            to.setPrice(countPrice.toString());
                            to.setName(from.getName());
                            to.setType(from.getType());

                            checkedItem.put(groupPosition, to);
                            allCount++;
                            holder.nev.setMinNum(1);
                            holder.nev.setMaxNum(from.getCount());
                            holder.nev.setCurrentNum(from.getCount());


                            holder.nev.setListener(new IteeNumberEditView.NumberEditListener() {
                                @Override
                                public void onAdd(int currentNum) {
                                    checkedItem.get(groupPosition).setCount(currentNum);
                                }

                                @Override
                                public void onMinus(int currentNum) {

                                    checkedItem.get(groupPosition).setCount(currentNum);


                                }
                            });
                            holder.refundPrice.addTextChangedListener(new TextWatcher() {
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

                                    temp = temp.toString().replace(AppUtils.getCurrentCurrency(mBaseFragment.getActivity()), StringUtils.EMPTY);
                                    if (Utils.isStringNotNullOrEmpty(temp.toString())) {
                                        PurchaseRefundExpandable from = (PurchaseRefundExpandable) getGroup(groupPosition);

                                        BigDecimal changedNum = new BigDecimal(temp.toString());
                                        BigDecimal price = new BigDecimal(from.getPrice()).multiply(new BigDecimal(from.getCount()));
                                        if (changedNum.compareTo(price) == 1) {
                                            holder.warn.setVisibility(View.VISIBLE);

                                        } else {
                                            if (checkedItem != null && checkedItem.get(groupPosition) != null) {
                                                holder.warn.setVisibility(View.GONE);
                                                checkedItem.get(groupPosition).setPrice(temp.toString());
                                                mListener.OnGoodItemClick("add", StringUtils.EMPTY);
                                            }
                                        }
                                    } else {
                                        checkedItem.get(groupPosition).setPrice(Constants.STR_0);
                                        mListener.OnGoodItemClick("add", StringUtils.EMPTY);
                                    }

                                }
                            });
                            mListener.OnGoodItemClick("add", from.getPrice());
                        } else {
                            holder.llBottom.setVisibility(View.GONE);
                            PurchaseRefundExpandable to = checkedItem.get(groupPosition);

                            if (to != null) {
                                checkedItem.remove(groupPosition);
                                allCount--;
                            }
                            if (to != null) {
                                mListener.OnGoodItemClick("add", Constants.STR_SEPARATOR + to.getPrice());
                            }
                        }

                      //  LayoutUtils.setListViewHeightBasedOnChildren(mListView, allCount, (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.082f));
                        break;
                    case TYPE_AA:
                    case TYPE_PACKAGE:
                        if (isChecked) {

                            PurchaseRefundExpandable from = (PurchaseRefundExpandable) getGroup(groupPosition);

                            PurchaseRefundExpandable to = new PurchaseRefundExpandable();

                            to.setId(from.getId());
                            to.setCount(from.getCount());
                            to.setPrice(from.getPrice());
                            to.setName(from.getName());
                            to.setType(from.getType());
//
//                            if (from.getGoodList() != null && from.getGoodList().size() > 0) {
//                                List<PurchaseRefundExpandable.GoodListItem> toGoodList = new ArrayList<PurchaseRefundExpandable.GoodListItem>();
//                                for (int i = 0; i < from.getGoodList().size(); i++) {
//
//                                    PurchaseRefundExpandable.GoodListItem toGood = new PurchaseRefundExpandable.GoodListItem();
//                                    PurchaseRefundExpandable.GoodListItem fromGood = from.getGoodList().get(i);
//
//                                    toGood.setId(fromGood.getId());
//                                    toGood.setCount(fromGood.getCount());
//                                    toGood.setPrice(fromGood.getPrice());
//                                    toGood.setName(fromGood.getName());
//                                    toGoodList.add(toGood);
//                                }
//                                to.setGoodList(toGoodList);
//                            }
                            checkedItem.put(groupPosition, to);
//                            allCount = allCount + getChildrenCount(groupPosition);

//                            for (int i = 0; i < countChildren; i++) {
//                                PurchaseRefundExpandable.GoodListItem goodListItem = (PurchaseRefundExpandable.GoodListItem) getChild(groupPosition, i);
//                                goodListItem.setCheck(true);
//                                notifyDataSetChanged();
//                            }


//                            LayoutUtils.setListViewHeightBasedOnChildren(mListView, allCount,
//                                    (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.082f));
                            Log.e("syb:allCount", allCount + StringUtils.EMPTY);
                            mListener.OnGoodItemClick("add", StringUtils.EMPTY);
                        } else {

                            PurchaseRefundExpandable purchaseRefundExpandable = checkedItem.get(groupPosition);

                            if (purchaseRefundExpandable != null) {
                                checkedItem.remove(groupPosition);
//                                allCount = allCount - getChildrenCount(groupPosition);
                            }

//                            for (int i = 0; i < countChildren; i++) {
//                                PurchaseRefundExpandable.GoodListItem goodListItem = (PurchaseRefundExpandable.GoodListItem) getChild(groupPosition, i);
//                                goodListItem.setCheck(false);
//
//
//                                notifyDataSetChanged();
//                            }
                            holder.llBottom.setVisibility(View.GONE);
//                            LayoutUtils.setListViewHeightBasedOnChildren(mListView, allCount,
//                                    (int) (Utils.getHeight(mBaseFragment.getActivity()) * 0.082f));

                            mListener.OnGoodItemClick("sub", StringUtils.EMPTY);
                        }

                        break;
                }


            }

        });

        switch (expandable.getType()) {
            case TYPE_GOOD:
                holder.textViewName.setText(expandable.getName());
                holder.textViewPrice.setText(currency + Utils.get2DigitDecimalString(expandable.getPrice()));
                holder.textViewCount.setText(Constants.STR_MULTIPLY + String.valueOf(expandable.getCount()));

                break;
            case TYPE_AA:

                holder.textViewName.setText(mBaseFragment.getActivity().getResources().getString(R.string.play_refund_aa) + expandable.getName());
                holder.textViewPrice.setText(currency + Utils.get2DigitDecimalString(expandable.getPrice()));
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.textViewPrice.getLayoutParams();
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                holder.textViewPrice.setLayoutParams(layoutParams);
                holder.textViewCount.setVisibility(View.GONE);

                holder.cb_group.setEnabled(false);
                holder.cb_group.setBackgroundResource(R.drawable.black_check_ban);

                break;
            case TYPE_PACKAGE:
                holder.textViewName.setText(expandable.getName());
                holder.textViewPrice.setText(currency + Utils.get2DigitDecimalString(expandable.getPrice()));
                RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) holder.textViewPrice.getLayoutParams();
                layoutParams1.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                layoutParams1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                holder.textViewPrice.setLayoutParams(layoutParams1);
                holder.textViewCount.setVisibility(View.GONE);
                break;
            case TYPE_REFUND:
                holder.textViewName.setText(expandable.getName());
                holder.textViewPrice.setText(Utils.get2DigitDecimalString(expandable.getPrice()));
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) holder.textViewPrice.getLayoutParams();
                layoutParams2.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                layoutParams2.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                holder.textViewPrice.setLayoutParams(layoutParams2);
                holder.textViewCount.setVisibility(View.GONE);

                break;
        }

        //set gray background to front , mark it refunded.
        if (expandable.getRefundflag() != null && Constants.STR_1.equals(String.valueOf(expandable.getRefundflag()))) {
            holder.rlGroupRefunded.setVisibility(View.VISIBLE);
            holder.rlGroupRefunded.bringToFront();
            holder.rlGroupRefunded.setAlpha(0.8f);
            holder.rlGroupRefunded.removeAllViews();
            IteeTextView text = new IteeTextView(mBaseFragment.getActivity());
            text.setText(mBaseFragment.getString(R.string.play_refunded));
            text.setTextColor(mBaseFragment.getColor(R.color.common_white));
            text.setTextSize(Constants.FONT_SIZE_MORE_LARGER);
            holder.rlGroupRefunded.addView(text);

            RelativeLayout.LayoutParams refundedTextParam = (RelativeLayout.LayoutParams) text.getLayoutParams();
            refundedTextParam.addRule(RelativeLayout.CENTER_IN_PARENT);
            text.setLayoutParams(refundedTextParam);
            text.setGravity(Gravity.CENTER);
            holder.cb_group.setEnabled(false);

        } else {
            holder.rlGroupRefunded.setVisibility(View.GONE);
            holder.rlGroupNormal.bringToFront();
        }

        holder.textViewCount.setTextColor(mBaseFragment.getActivity().getResources().getColor(R.color.common_blue));
        holder.textViewCount.setTextSize(Constants.FONT_SIZE_NORMAL);

        if (data.get(groupPosition).getPricingDataList()!=null&&data.get(groupPosition).getPricingDataList().size()>0){


            //holder.cb_group.setVisibility(View.GONE);

            if (data.get(groupPosition).getPricingDataList()!=null){

                for (JsonPurchaseHistoryDetailRecord.PricingData pricing : data.get(groupPosition).getPricingDataList()) {


                    holder.cb_group.setEnabled(false);
                    holder.cb_group.setBackgroundResource(R.drawable.black_check_ban);
                    holder.voucherLayout.addView(getHistoryItemLayout(pricing.getProductName(), pricing.getDiscountPrice(), pricing.getQty(), 2));
                    holder.textViewName.setText(mBaseFragment.getString(R.string.pricing_deduct) + pricing.getPricingTimes());
                }
            }
//            voucherLayou
        }
        return convertView;

    }


    private RelativeLayout getHistoryItemLayout(String name, String price, String count, int type) {
        LinearLayout.LayoutParams goodsLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams goodsNameParams = new RelativeLayout.LayoutParams((int) (Utils.getWidth(mBaseFragment.getBaseActivity()) * 0.7f), (int) (Utils.getHeight(mBaseFragment.getBaseActivity()) * 0.08f));
        goodsNameParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        goodsNameParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        goodsNameParams.setMargins(mBaseFragment.getActualWidthOnThisDevice(40), 0, 0, 0);
        if (type == 2) {
            goodsNameParams.setMargins(mBaseFragment.getActualWidthOnThisDevice(60), 0, 0, 0);
        }
        RelativeLayout.LayoutParams goodsPriceParams = new RelativeLayout.LayoutParams((int) (Utils.getWidth(mBaseFragment.getBaseActivity()) * 0.2f), (int) (Utils.getHeight(mBaseFragment.getBaseActivity()) * 0.04f));
        goodsPriceParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        goodsPriceParams.setMargins(0, 0, mBaseFragment.getActualWidthOnThisDevice(40), 0);
        RelativeLayout goodsLayout = new RelativeLayout(mBaseFragment.getBaseActivity());
        if (type != 2)
            AppUtils.addTopSeparatorLine(goodsLayout, mBaseFragment);
        IteeTextView goodsName = new IteeTextView(mBaseFragment.getBaseActivity());
        IteeTextView goodsPrice = new IteeTextView(mBaseFragment.getBaseActivity());
        goodsPrice.setId(View.generateViewId());

        RelativeLayout.LayoutParams goodsCountParams = new RelativeLayout.LayoutParams((int) (Utils.getWidth(mBaseFragment.getBaseActivity()) * 0.4f), (int) (Utils.getHeight(mBaseFragment.getBaseActivity()) * 0.04f));

        goodsCountParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        goodsCountParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        goodsCountParams.addRule(RelativeLayout.BELOW, goodsPrice.getId());
        goodsCountParams.setMargins(0, 0, mBaseFragment.getActualWidthOnThisDevice(40), 0);

        IteeTextView goodsCount = new IteeTextView(mBaseFragment.getBaseActivity());

        goodsLayout.setLayoutParams(goodsLayoutParams);
        goodsName.setLayoutParams(goodsNameParams);
        goodsPrice.setLayoutParams(goodsPriceParams);
        goodsCount.setLayoutParams(goodsCountParams);


        goodsLayout.addView(goodsName);
        goodsLayout.addView(goodsPrice);
        goodsLayout.addView(goodsCount);

        if (type == 3){
            goodsPrice.setVisibility(View.GONE);

            goodsCount.setVisibility(View.GONE);
        }
        goodsName.setText(name);
        goodsPrice.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity()) + Utils.get2DigitDecimalString(price));
        goodsPrice.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        goodsCount.setText(Constants.STR_MULTIPLY + count);
        goodsCount.setGravity(Gravity.END);
        if (Constants.STR_EMPTY.equals(count)) {
            goodsPriceParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            goodsCount.setVisibility(View.GONE);
        } else {
            goodsPriceParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

        }

        goodsCount.setTextColor(mBaseFragment.getResources().getColor(R.color.common_blue));
        goodsCount.setTextSize(Constants.FONT_SIZE_SMALLER);

        if (type == 2) {
            goodsLayout.setBackgroundColor(mBaseFragment.getColor(R.color.common_gray));
        }

        if (type == 99) {

            goodsLayout.setBackgroundColor(mBaseFragment.getColor(R.color.common_light_gray));
            goodsPrice.setText(AppUtils.getCurrentCurrency(mBaseFragment.getBaseActivity()) + "-" + Utils.get2DigitDecimalString(price));
        }
        return goodsLayout;
    }



    @Override
    public boolean hasStableIds() {

        return true;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public double getTotalRefund() {
        double total = 0;
        for (int i = 0; i < data.size(); i++) {
            PurchaseRefundExpandable purchaseRefundExpandable = data.get(i);
            if (Constants.STR_0.equals(String.valueOf(purchaseRefundExpandable.getRefundflag()))) {
                total += Double.valueOf(purchaseRefundExpandable.getPrice()) * purchaseRefundExpandable.getCount();
            }
        }
        return total;
    }

    class GroupViewHolder extends BaseViewHolder {


        CheckBox cb_group;
        LinearLayout llBottom;
        IteeTextView textViewName;
        IteeTextView textViewPrice;
        IteeTextView textViewCount;
        IteeNumberEditView nev;
        IteeMoneyEditText refundPrice;
        IteeTextView tvCurrency;
        IteeTextView warn;
        RelativeLayout rlGroupRefunded;
        RelativeLayout rlGroupNormal;
        LinearLayout voucherLayout;
    }

    class ChildViewHolder extends BaseViewHolder {

        CheckBox cb_child;
        LinearLayout llBottom;
        IteeTextView textViewName;
        IteeTextView textViewPrice;
        IteeTextView textViewCount;
        IteeNumberEditView nev;
        IteeMoneyEditText refundPrice;
        IteeTextView tvCurrency;
        IteeTextView warn;

        RelativeLayout rlChildRefunded;
        RelativeLayout rlChildNormal;


    }
}