package cn.situne.itee.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.jsonentity.JsonStaffDepartmentListGet;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.SwipeListView;


public class StaffEditAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private BaseFragment baseFragment = null;
    private List<JsonStaffDepartmentListGet.ItemData> data;
    private int mRightWidth = 0;
    /**
     * 单击事件监听器
     */
    private onRightItemClickListener mListener = null;
    private OnClickListener itemListener;

    public StaffEditAdapter(BaseFragment baseFragment, List<JsonStaffDepartmentListGet.ItemData> data, int rightWidth) {
        this.baseFragment = baseFragment;
        this.data = data;
        mRightWidth = rightWidth;
    }

    public List<JsonStaffDepartmentListGet.ItemData> getData() {
        return data;
    }

    public void setData(List<JsonStaffDepartmentListGet.ItemData> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final JsonStaffDepartmentListGet.ItemData itemData = data.get(position);

        ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(baseFragment.getActivity());
//        if (convertView == null) {
        convertView = inflater.inflate(R.layout.item_of_fragment_staff_edit, null, false);//该布局添加了点击变色
        holder = new ViewHolder();
        holder.member_item_left = (RelativeLayout) convertView.findViewById(R.id.member_item_left);
        holder.member_item_right = (LinearLayout) convertView.findViewById(R.id.member_item_right);
        holder.tv_area = (IteeTextView) convertView.findViewById(R.id.tv_area);
        holder.tv_number = (IteeTextView) convertView.findViewById(R.id.tv_number);
        holder.iv_right_icon = (ImageView) convertView.findViewById(R.id.iv_right_icon);
        holder.iv_right_icon.setImageResource(R.drawable.icon_right_arrow);
        holder.itemRightEdit = (IteeTextView) convertView.findViewById(R.id.item_right_delete);

        LayoutUtils.setLayoutHeight(holder.member_item_left, 100, baseFragment.getActivity());

        LayoutParams layoutParams = holder.member_item_right.getLayoutParams();
        layoutParams.width = AppUtils.getRightButtonWidth(baseFragment.getActivity());

        holder.itemRightEdit.setTextSize(Constants.FONT_SIZE_NORMAL);
        holder.itemRightEdit.setTextColor(baseFragment.getColor(R.color.common_white));
        holder.itemRightEdit.setGravity(Gravity.CENTER);

        holder.iv_right_icon.setId(View.generateViewId());

        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(holder.tv_area, baseFragment.getActivity());
        LayoutUtils.setRightArrow(holder.iv_right_icon, baseFragment.getActivity());
        LayoutUtils.setLeftOfView(holder.tv_number, holder.iv_right_icon, 10, baseFragment.getActivity());

        convertView.setBackgroundColor(baseFragment.getColor(R.color.common_white));

        convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }

        if (data.get(position).getEditStatus() == 1) {
            convertView.setTag(null);
        } else {
            convertView.setTag(SwipeListView.DISABLE_RIGHT);
        }

//        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
//                LayoutParams.MATCH_PARENT);
//        holder.member_item_left.setLayoutParams(lp1);
//        LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
//        holder.member_item_right.setLayoutParams(lp2);

        holder.tv_area.setText(itemData.getDepartmentName());
        holder.tv_number.setText(String.valueOf(itemData.getNumber()));
        holder.tv_number.setTextSize(Constants.FONT_SIZE_15);
        holder.tv_area.setTextSize(Constants.FONT_SIZE_15);
        holder.member_item_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position);
                }
            }
        });
        holder.member_item_left.setOnClickListener(getItemListener());
        holder.member_item_left.setTag(position);

        return convertView;
    }

    public OnClickListener getItemListener() {
        return itemListener;
    }

    public void setItemListener(OnClickListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setOnRightItemClickListener(onRightItemClickListener listener) {
        mListener = listener;
    }

    public interface onRightItemClickListener {
        void onRightItemClick(View v, int position);
    }

    static class ViewHolder {
        RelativeLayout member_item_left;
        LinearLayout member_item_right;
        IteeTextView itemRightEdit;
        private IteeTextView tv_area;
        private IteeTextView tv_number;
        private ImageView iv_right_icon;
    }
}
