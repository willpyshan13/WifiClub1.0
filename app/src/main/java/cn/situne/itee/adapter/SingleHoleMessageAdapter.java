package cn.situne.itee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.manager.jsonentity.JsonTeeInfoList;

public class SingleHoleMessageAdapter extends BaseAdapter {
    Context context;
    List<JsonTeeInfoList.DataList.DataInfoList> dataInfoList;
    private RelativeLayout rlContain;
    private EditText tvTeeKey;
    private EditText etNumber;
    private EditText tvTeeValue;

    public SingleHoleMessageAdapter(Context context, List<JsonTeeInfoList.DataList.DataInfoList> dataInfoList) {
        this.context = context;
        this.dataInfoList = dataInfoList;

    }

    @Override
    public int getCount() {
        return dataInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        convertView = LayoutInflater.from(context).inflate(R.layout.hole_item, null);
        rlContain = (RelativeLayout) convertView.findViewById(R.id.re);

        tvTeeKey = new EditText(context);
        etNumber = new EditText(context);
        tvTeeValue = new EditText(context);

        LinearLayout.LayoutParams paramsContain = (LinearLayout.LayoutParams) rlContain.getLayoutParams();
        paramsContain.height = 100;
        rlContain.setLayoutParams(paramsContain);

        rlContain.addView(tvTeeKey);
        RelativeLayout.LayoutParams paramsTvHoleNoKey = (RelativeLayout.LayoutParams) tvTeeKey.getLayoutParams();
        paramsTvHoleNoKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvHoleNoKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvHoleNoKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvHoleNoKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvHoleNoKey.leftMargin = 40;
        tvTeeKey.setLayoutParams(paramsTvHoleNoKey);

        rlContain.addView(tvTeeValue);
        RelativeLayout.LayoutParams paramsTvTeeValue = (RelativeLayout.LayoutParams) tvTeeValue.getLayoutParams();
        paramsTvTeeValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTeeValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTeeValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvTeeValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvTeeValue.rightMargin = 40;
        tvTeeValue.setLayoutParams(paramsTvTeeValue);

        rlContain.addView(etNumber);
        RelativeLayout.LayoutParams paramsTvHoleNoValue = (RelativeLayout.LayoutParams) etNumber.getLayoutParams();
        paramsTvHoleNoValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvHoleNoValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvHoleNoValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvHoleNoValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvHoleNoValue.rightMargin = 130;
        etNumber.setLayoutParams(paramsTvHoleNoValue);


        tvTeeKey.setText(dataInfoList.get(position).getTeeName());
        tvTeeKey.setBackground(null);
        tvTeeKey.setEnabled(false);
        tvTeeKey.setTextColor(Color.BLACK);
        tvTeeKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvTeeValue.setText(AppUtils.getUnit(context));
        tvTeeValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvTeeValue.setTextColor(Color.GRAY);
        tvTeeValue.setBackground(null);
        tvTeeValue.setEnabled(false);

        etNumber.setText(dataInfoList.get(position).getTeeYard().toString());
        etNumber.setBackground(null);
        etNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        etNumber.setTextColor(Color.GRAY);

        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(StringUtils.EMPTY)) {
                    dataInfoList.get(position).setTeeYard(Integer.valueOf(editable.toString()));
                }
            }
        });

        return convertView;
    }


}
