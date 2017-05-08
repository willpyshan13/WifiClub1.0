/**
 * Project Name: itee
 * File Name:	 ShopsPropertyEditFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:		 2015-04-07
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.shops;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonShopsSubclassGet;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:ShopsPropertyEditFragment <br/>
 * Function: FIXME. <br/>
 * Date: 2015-04-07 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShopsPropertyEditFragment extends BaseEditFragment {
    private List<JsonShopsSubclassGet.ProductTypeDetail> dataList;//data
    private List<PropertyItem> propertyItems;//view
    private String productId;
    private LinearLayout llBody;

    private LinearLayout addLayout;
    private View.OnClickListener delItemListener;
    private List<PutData> putAddDataList;
    private List<PutData> putEditDataList;
    private List<JsonShopsSubclassGet.ProductTypeDetail> putDelDataList;
    private int addIndexCode;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_property_edit;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            setFragmentMode(BaseEditFragment.FragmentMode.valueOf(bundle.getInt(TransKey.COMMON_FRAGMENT_MODE)));
            productId = bundle.getString(TransKey.SHOPS_PRODUCT_ID);
        }
        propertyItems = new ArrayList<>();
        putDelDataList = new ArrayList<>();
        llBody = (LinearLayout) rootView.findViewById(R.id.ll_body);
        LinearLayout llFooter = (LinearLayout) rootView.findViewById(R.id.ll_footer);

        addLayout = new LinearLayout(getBaseActivity());

        addLayout.setOrientation(LinearLayout.VERTICAL);
        addLayout.addView(AppUtils.getSeparatorLine(this));

        LinearLayout.LayoutParams addTextParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getActualWidthOnThisDevice(100));

        IteeTextView addText = new IteeTextView(getBaseActivity());
        addText.setText(getString(R.string.common_add));
        addText.setTextColor(getColor(R.color.common_blue));
        addText.setPadding(getActualWidthOnThisDevice(40), 0, 0, 0);
        addText.setGravity(Gravity.CENTER | Gravity.START);
        addText.setLayoutParams(addTextParams);

        addLayout.addView(addText);
        addLayout.addView(AppUtils.getSeparatorLine(this));


        llFooter.addView(addLayout);
        getSubClass();
    }

    private void initSubclassView() {
        for (JsonShopsSubclassGet.ProductTypeDetail item : dataList) {
            addSubclassView(item);
        }
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        delItemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PropertyItem item = (PropertyItem) v.getTag();

                if (item.getmLevel() == 1) {
                    propertyItems.remove(item);
                    llBody.removeView(v);
                }
                if (item.getmData() != null) {
                    putDelDataList.add(item.getmData());
                }
                // propertyItems.remove(v);
            }
        };
        addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubclassView(null);

            }
        });
    }

    private void addSubclassView(JsonShopsSubclassGet.ProductTypeDetail data) {

        LinearLayout itemLayout = new LinearLayout(getBaseActivity());
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.addView(AppUtils.getSeparatorLine(ShopsPropertyEditFragment.this));
        LinearLayout.LayoutParams itemBoxParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout itemBox = new RelativeLayout(getBaseActivity());
        itemBox.setLayoutParams(itemBoxParams);
        PropertyItem item = new PropertyItem(ShopsPropertyEditFragment.this, 1, getActualHeightOnThisDevice(100), data);
        item.setDelItemListener(delItemListener);
        item.refreshView();
        itemBox.addView(item);
        AppUtils.addBottomSeparatorLine(itemBox, ShopsPropertyEditFragment.this);
        itemLayout.addView(itemBox);
        LinearLayout.LayoutParams viewParmas = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 30);
        View view = new View(getBaseActivity());
        view.setLayoutParams(viewParmas);
        itemLayout.addView(view);
        llBody.addView(itemLayout);
        item.setContainers(itemLayout);
        propertyItems.add(item);

    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {

    }

    private void getSubClass() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));

        params.put(ApiKey.SHOPS_PRODUCT_ID, productId);
        HttpManager<JsonShopsSubclassGet> hh = new HttpManager<JsonShopsSubclassGet>(ShopsPropertyEditFragment.this) {

            @Override
            public void onJsonSuccess(JsonShopsSubclassGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataList = jo.getDataList();
                    initSubclassView();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsSubclassGet, params);

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.shop_setting_subcategories_edit);
        getTvRight().setText(R.string.common_ok);
        getTvRight().setBackground(null);

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                if (doCheck()) {
                    putSubClass();
                } else {

                    Utils.showShortToast(getBaseActivity(), R.string.msg_sub_categories_must_be_filled);
                }

            }
        });

    }

    private boolean doCheck() {

        for (PropertyItem propertyItem : propertyItems) {


            if (propertyItem.getTvName().getText().toString().length() <= 0) {
                return false;
            }
            if (propertyItem.getChildItems()!=null &&propertyItem.getChildItems().size()>0){
                if (! checkChild(propertyItem.getChildItems())) return false;


            }

        }
        return true;
    }



    private boolean checkChild(List<PropertyItem> childItems){

         for (PropertyItem propertyItem:childItems)  {
             if (propertyItem.getTvName().getText().toString().length() <= 0) {
                 return false;
             }

             if (propertyItem.getChildItems()!=null &&propertyItem.getChildItems().size()>0){
                return  checkChild(propertyItem.getChildItems());

             }

         }


        return true;
    }



    //  edit and  add
    private void initEditChild(List<PropertyItem> childItems, int parentIndexCode, boolean hasParent) {
        for (PropertyItem childItem : childItems) {
            JsonShopsSubclassGet.ProductTypeDetail jsData = childItem.getmData();

            if (jsData != null) {
                if (!jsData.getPraName().equals(childItem.getTvName().getText().toString())) {
                    PutData putData = new PutData();
                    putData.setLevelId(childItem.getmLevel());
                    putData.setId(childItem.getmData().getPraId());
                    putData.setValue(childItem.getTvName().getText().toString());
                    putEditDataList.add(putData);
                }
                if (childItem.getChildItems() != null && childItem.getChildItems().size() > 0) {
                    initEditChild(childItem.getChildItems(), childItem.getmData().getPraId(), true);
                }
            } else {//add childItem
                PutData putData = new PutData();
                putData.setLevelId(childItem.getmLevel());
                putData.setParentCode(parentIndexCode);
                if (hasParent) {
                    putData.setParentId(parentIndexCode);
                } else {
                    putData.setParentId(0);
                }

                putData.setCode(addIndexCode);
                putData.setValue(childItem.getTvName().getText().toString());
                putAddDataList.add(putData);
                addIndexCode++;
                if (childItem.getChildItems() != null && childItem.getChildItems().size() > 0) {
                    initEditChild(childItem.getChildItems(), putData.getCode(), false);
                }
            }
        }
    }
    // private int parentCode;

    private void putSubClass() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRODUCT_ID, productId);
        params.put(ApiKey.SHOPS_SUB_CLASS_LIST, getPutData());
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(ShopsPropertyEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    doBack();
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startPut(getActivity(), ApiManager.HttpApi.ShopsSubclassPut, params);


    }

    private String getPutData() {

        addIndexCode = 1;
        if (putAddDataList == null) {
            putAddDataList = new ArrayList<>();
        }
        if (putEditDataList == null) {
            putEditDataList = new ArrayList<>();
        }
        putAddDataList.clear();
        putEditDataList.clear();
        for (PropertyItem item : propertyItems) {
            JsonShopsSubclassGet.ProductTypeDetail jsData = item.getmData();
            if (jsData != null) {
                if (!jsData.getPraName().equals(item.getTvName().getText().toString())) {
                    PutData putData = new PutData();
                    putData.setLevelId(item.getmLevel());
                    putData.setId(item.getmData().getPraId());
                    putData.setValue(item.getTvName().getText().toString());
                    putEditDataList.add(putData);
                }
                if (item.getChildItems() != null && item.getChildItems().size() > 0) {

                    initEditChild(item.getChildItems(), jsData.getPraId(), true);
                }

            } else {
                PutData putData = new PutData();
                putData.setLevelId(item.getmLevel());
                putData.setParentCode(0);
                putData.setParentId(0);
                putData.setValue(item.getTvName().getText().toString());
                putData.setCode(addIndexCode);
                putAddDataList.add(putData);
                addIndexCode++;
                if (item.getChildItems() != null && item.getChildItems().size() > 0) {

                    initAddChild(item.getChildItems(), putData.getCode());

                }

            }

        }

        JSONArray arrayEdit = new JSONArray();
        for (PutData data : putEditDataList) {
            Map<String, String> tMap = new HashMap<>();

            tMap.put("value", String.valueOf(data.getValue()));
            tMap.put("id", String.valueOf(data.getId()));
            JSONObject jsonObject = new JSONObject(tMap);
            arrayEdit.put(jsonObject);
        }


        StringBuilder delSb = new StringBuilder();

        for (JsonShopsSubclassGet.ProductTypeDetail delItem : putDelDataList) {

            delSb.append(String.valueOf(delItem.getPraId()));
            delSb.append(Constants.STR_COMMA);
        }
        String delStr = delSb.toString();
        if (delStr.length() > 0) {

            delStr = delStr.substring(0, delStr.length() - 1);
        }


        JSONArray array = new JSONArray();
        for (PutData data : putAddDataList) {
            Map<String, String> tMap = new HashMap<>();
            tMap.put("level_id", String.valueOf(data.getLevelId()));
            tMap.put("parent_id", String.valueOf(data.getParentId()));
            tMap.put("parent_code", String.valueOf(data.getParentCode()));
            tMap.put("value", String.valueOf(data.getValue()));
            tMap.put("code", String.valueOf(data.getCode()));
            JSONObject jsonObject = new JSONObject(tMap);
            array.put(jsonObject);
        }
        Map<String, JSONObject> delMap = new HashMap<>();

        JSONObject jsDel = new JSONObject();
        try {
            jsDel.put("id", delStr);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
        delMap.put("del", jsDel);


        JSONArray subClassList = new JSONArray();
        try {

            JSONObject subClass = new JSONObject(delMap);
            subClass.put("edit", arrayEdit);
            subClass.put("add", array);

            subClassList.put(subClass);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

        return subClassList.toString();
    }


    private void initAddChild(List<PropertyItem> childItems, int parentIndexCode) {
        for (PropertyItem childItem : childItems) {
            PutData putData = new PutData();
            putData.setLevelId(childItem.getmLevel());
            putData.setParentCode(parentIndexCode);
            putData.setParentId(0);
            putData.setCode(addIndexCode);
            putData.setValue(childItem.getTvName().getText().toString());
            putAddDataList.add(putData);
            addIndexCode++;
            if (childItem.getChildItems() != null && childItem.getChildItems().size() > 0) {
                initAddChild(childItem.getChildItems(), putData.getCode());
            }
        }
    }

    class PutData {
        private String value;
        private int parentId;
        private int levelId;
        private int parentCode;
        private int code;
        private int id;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public int getLevelId() {
            return levelId;
        }

        public void setLevelId(int levelId) {
            this.levelId = levelId;
        }

        public int getParentCode() {
            return parentCode;
        }

        public void setParentCode(int parentCode) {
            this.parentCode = parentCode;
        }
    }

    class PropertyItem extends LinearLayout {
        private JsonShopsSubclassGet.ProductTypeDetail mData;
        private View containers;
        private IteeEditText tvName;
        private View.OnClickListener delItemListener;
        private int mLevel;
        private List<PropertyItem> childItems;
        private BaseFragment mFragment;

        public PropertyItem(BaseFragment fragment, int level, int height, JsonShopsSubclassGet.ProductTypeDetail data) {
            super(fragment.getBaseActivity());
            setOrientation(VERTICAL);
            mLevel = level;
            mFragment = fragment;
            mData = data;
            childItems = new ArrayList<>();
            RelativeLayout.LayoutParams myParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            this.setLayoutParams(myParams);
            RelativeLayout.LayoutParams bodyParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
            RelativeLayout body = new RelativeLayout(fragment.getBaseActivity());
            body.setLayoutParams(bodyParams);
            body.setPadding(mFragment.getActualWidthOnThisDevice(0), 0, mFragment.getActualWidthOnThisDevice(0), 0);
            switch (level) {
                case 2:
                    body.setPadding(mFragment.getActualWidthOnThisDevice(60), 0, mFragment.getActualWidthOnThisDevice(0), 0);
                    break;

                case 3:
                    body.setPadding(mFragment.getActualWidthOnThisDevice(100), 0, mFragment.getActualWidthOnThisDevice(0), 0);
                    break;
                default:
                    break;

            }
            RelativeLayout.LayoutParams delParams = new RelativeLayout.LayoutParams(height / 2, height / 2);
            delParams.addRule(RelativeLayout.CENTER_VERTICAL);
            delParams.leftMargin = mFragment.getActualWidthOnThisDevice(10);
            IteeButton del = new IteeButton(fragment.getBaseActivity());
            del.setLayoutParams(delParams);
            del.setBackgroundResource(R.drawable.icon_delete);
            del.setId(View.generateViewId());
            RelativeLayout.LayoutParams tvNameParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tvNameParams.leftMargin = mFragment.getActualWidthOnThisDevice(10);
            tvNameParams.addRule(RelativeLayout.RIGHT_OF, del.getId());
            tvNameParams.addRule(RelativeLayout.CENTER_VERTICAL);
            tvName = new IteeEditText(fragment.getBaseActivity());
            tvName.setLayoutParams(tvNameParams);
            tvName.setMinWidth(mFragment.getActualWidthOnThisDevice(100));
            tvName.setHint(getString(R.string.shop_setting_subcategories));

            RelativeLayout.LayoutParams addParams = new RelativeLayout.LayoutParams(height / 2, height / 2);
            addParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            addParams.addRule(RelativeLayout.CENTER_VERTICAL);
            addParams.rightMargin = mFragment.getActualWidthOnThisDevice(10);
            IteeButton add = new IteeButton(fragment.getBaseActivity());
            add.setLayoutParams(addParams);
            add.setBackgroundResource(R.drawable.btn_add_green);
            if (mLevel == 3) {
                add.setVisibility(View.GONE);
            }
            body.addView(del);
            body.addView(tvName);
            body.addView(add);
            AppUtils.addBottomSeparatorLine(body, fragment);
            this.addView(body);


            add.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    PropertyItem item = new PropertyItem(ShopsPropertyEditFragment.this, mLevel + 1, mFragment.getActualHeightOnThisDevice(100), null);
                    PropertyItem.this.addView(item);
                    childItems.add(item);
                }
            });

            del.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (delItemListener != null) {
                        delItemListener.onClick(containers);
                    }
                    if (mLevel != 1) {
                        LinearLayout parent = (LinearLayout) PropertyItem.this.getParent();
                        parent.removeView(PropertyItem.this);
                        PropertyItem item = (PropertyItem) parent;
                        item.getChildItems().remove(PropertyItem.this);
                    }
                }
            });


        }

        public JsonShopsSubclassGet.ProductTypeDetail getmData() {
            return mData;
        }

        public IteeEditText getTvName() {
            return tvName;
        }

        public void setContainers(View containers) {
            this.containers = containers;
            this.containers.setTag(this);
        }

        public int getmLevel() {
            return mLevel;
        }

        public void setDelItemListener(OnClickListener delItemListener) {
            this.delItemListener = delItemListener;
        }

        public List<PropertyItem> getChildItems() {
            return childItems;
        }

        private void refreshView() {

            if (mData != null) {
                tvName.setText(mData.getPraName());
                initChildView(mData.getItems());
            }
        }

        private void initChildView(List<JsonShopsSubclassGet.ProductTypeDetail> dataList) {
            if (dataList != null && dataList.size() > 0) {
                for (int i = 0; i < dataList.size(); i++) {
                    JsonShopsSubclassGet.ProductTypeDetail data = dataList.get(i);
                    PropertyItem item = new PropertyItem(ShopsPropertyEditFragment.this, mLevel + 1, mFragment.getActualHeightOnThisDevice(100), data);
                    item.setDelItemListener(delItemListener);
                    item.setContainers(this);
                    PropertyItem.this.addView(item);
                    childItems.add(item);
                    item.refreshView();
                }
            }
        }
    }
}