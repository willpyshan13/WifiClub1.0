package cn.situne.itee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.manager.jsonentity.JsonTeeNameList;

public class CourseTeeAdapter extends BaseAdapter {
    Context context;
    List<JsonTeeNameList.DataList> dataLists;

    View.OnClickListener deleteListener;
    public RelativeLayout rlContainer;
    public ImageView ivDeleteIcon;
    public EditText etColorTee;

    public void setDeleteListener(View.OnClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public CourseTeeAdapter(Context context, List<JsonTeeNameList.DataList> dataLists) {
        this.context = context;
        this.dataLists = dataLists;

    }

    @Override
    public int getCount() {
        return dataLists.size();
    }

    @Override
    public Object getItem(int position) {
        return dataLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        convertView = LayoutInflater.from(context).inflate(R.layout.course_tee_item, null);
        rlContainer = (RelativeLayout) convertView.findViewById(R.id.rl_container);
        ivDeleteIcon = new ImageView(context);
        etColorTee = new EditText(context);

        LinearLayout.LayoutParams paramsContainer = (LinearLayout.LayoutParams) rlContainer.getLayoutParams();
        paramsContainer.height = 100;
        rlContainer.setLayoutParams(paramsContainer);

        rlContainer.addView(ivDeleteIcon);
        RelativeLayout.LayoutParams paramsIvDeleteIcon = (RelativeLayout.LayoutParams) ivDeleteIcon.getLayoutParams();
        paramsIvDeleteIcon.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvDeleteIcon.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvDeleteIcon.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsIvDeleteIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsIvDeleteIcon.leftMargin = 40;
        ivDeleteIcon.setLayoutParams(paramsIvDeleteIcon);

        rlContainer.addView(etColorTee);
        RelativeLayout.LayoutParams paramsEtColorTee = (RelativeLayout.LayoutParams) etColorTee.getLayoutParams();
        paramsEtColorTee.width = 150;
        paramsEtColorTee.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEtColorTee.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsEtColorTee.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsEtColorTee.leftMargin = 120;
        etColorTee.setLayoutParams(paramsEtColorTee);

        ivDeleteIcon.setBackgroundResource(R.drawable.icon_delete);

        etColorTee.setText(dataLists.get(position).getTeeName());
        etColorTee.setTextSize(Constants.FONT_SIZE_NORMAL);
        etColorTee.setBackground(null);
        etColorTee.setTextColor(Color.GRAY);

        ivDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setTag(position);
                deleteListener.onClick(view);
            }
        });

        etColorTee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(Constants.STR_EMPTY)) {
                    dataLists.get(position).setTeeName(editable.toString());

                }
            }
        });

        return convertView;
    }

}
