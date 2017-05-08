package cn.situne.itee.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.LinkedList;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.jsonentity.JsonCaddieLevelGet;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;

public class CaddiesLevelAdapter extends BaseAdapter {

    private BaseFragment mBaseFragment;
    private LinkedList<JsonCaddieLevelGet.Level> caddieLevelList;
    private int iconDelete;
    private boolean isEdit;

    public CaddiesLevelAdapter(BaseFragment mBaseFragment, LinkedList<JsonCaddieLevelGet.Level> caddieLevelList) {
        this.mBaseFragment = mBaseFragment;
        this.caddieLevelList = caddieLevelList;
        iconDelete = R.drawable.icon_delete;
    }

    @Override
    public int getCount() {
        if (caddieLevelList != null) {
            return caddieLevelList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return caddieLevelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(mBaseFragment.getActivity()).inflate(R.layout.item_of_fragment_caddies_level, null);

            holder.iconDelete = (ImageView) convertView.findViewById(R.id.icon_delete);
            holder.levelName = (IteeEditText) convertView.findViewById(R.id.et_level);
            holder.tvAddLevel = (IteeTextView) convertView.findViewById(R.id.tv_add_level);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.levelName.getLayoutParams();
            layoutParams.width = 300;
            holder.levelName.setLayoutParams(layoutParams);

            holder.levelName.setBackground(null);
            holder.iconDelete.setImageResource(iconDelete);

            holder.levelName.setPadding(0, 8, 0, 0);
            holder.levelName.setTextColor(mBaseFragment.getResources().getColor(R.color.common_deep_gray));

            holder.levelName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText t = (EditText) v;
                    t.setFocusable(true);
                    t.setFocusableInTouchMode(true);
                    t.requestFocus();
                }
            });

            holder.iconDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final int index = (int) v.getTag();
                    AppUtils.showDeleteAlert(mBaseFragment, new AppUtils.DeleteConfirmListener() {
                        @Override
                        public void onClickDelete() {
                            caddieLevelList.remove(index);
                            notifyDataSetChanged();
                        }
                    });
                }
            });

            convertView.setTag(holder);

            ListView.LayoutParams paramsConverView = new ListView.LayoutParams(720, 100);
            convertView.setLayoutParams(paramsConverView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JsonCaddieLevelGet.Level level = caddieLevelList.get(position);
        holder.levelName.setText(level.getLevName());
        holder.levelName.setEnabled(isEdit());
        holder.iconDelete.setTag(position);

        if (isEdit()) {
            holder.iconDelete.setVisibility(View.VISIBLE);
        } else {
            holder.iconDelete.setVisibility(View.INVISIBLE);
        }

        if (level.getLevId() == -1) {
            holder.iconDelete.setVisibility(View.GONE);
            holder.levelName.setVisibility(View.GONE);
            holder.tvAddLevel.setVisibility(View.VISIBLE);
            holder.levelName.setEnabled(false);
            holder.tvAddLevel.setText(R.string.staff_add_level);
            holder.tvAddLevel.setTextSize(Constants.FONT_SIZE_NORMAL);

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    JsonCaddieLevelGet.Level ll = new JsonCaddieLevelGet.Level();
                    JsonCaddieLevelGet.Level lastLevel = caddieLevelList.getLast();
                    caddieLevelList.removeLast();
                    caddieLevelList.addLast(ll);
                    caddieLevelList.addLast(lastLevel);
                    notifyDataSetChanged();
                }
            });
            ListView.LayoutParams layoutParams = new ListView.LayoutParams(720, 100);
//            convertView.setLayoutParams(layoutParams);
        } else {
            holder.iconDelete.setVisibility(View.VISIBLE);
            holder.levelName.setVisibility(View.VISIBLE);
            holder.tvAddLevel.setVisibility(View.GONE);
            holder.levelName.setEnabled(true);
            convertView.setOnClickListener(null);
        }


        return convertView;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public class ViewHolder {
        ImageView iconDelete;
        IteeEditText levelName;
        IteeTextView tvAddLevel;
    }

}
