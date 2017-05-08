/**
 * Project Name: itee
 * File Name:  JsonGreenFee.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-23
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonGreenFee <br/>
 * Function: To set GreenFee. <br/>
 * Date: 2015-03-23 <br/>
 *
 * @author luochao
 * @version 1.0
 * @see
 */
public class JsonGreenFee extends BaseJsonObject implements Serializable {

    private ArrayList<CoursePrice> coursePriceList;
    private ArrayList<HolesPrice> holesPriceList;

    public JsonGreenFee(JSONObject jsonObject) {
        super(jsonObject);
    }

    public ArrayList<CoursePrice> getCoursePriceList() {
        return coursePriceList;
    }

    public ArrayList<HolesPrice> getHolesPriceList() {
        return holesPriceList;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        coursePriceList = new ArrayList<>();
        holesPriceList = new ArrayList<>();
        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            JSONArray course_price_list = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.SHOP_05_COURSE_PRICE_LIST);
            JSONArray holes_price_list = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.SHOP_05_HOLES_PRICE_LIST);

            for (int i = 0; i < course_price_list.length(); i++) {
                JSONObject jsonObject = course_price_list.getJSONObject(i);
                CoursePrice bean = new CoursePrice();
                bean.setCoursePriceId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_COURSE_PRICE_ID));
                bean.setCoursePriceName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_COURSE_PRICE_NAME));
                bean.setCoursePriceFee(Utils.getDoubleFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_COURSE_PRICE_FEE));

                coursePriceList.add(bean);
            }

            for (int i = 0; i < holes_price_list.length(); i++) {
                JSONObject jsonObject = holes_price_list.getJSONObject(i);
                HolesPrice bean = new HolesPrice();
                bean.setHolesPriceId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_HOLES_PRICE_ID));
                bean.setHolesPriceName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_HOLES_PRICE_NAME));
                bean.setHolesPriceFee(Utils.getDoubleFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_HOLES_PRICE_FEE));

                holesPriceList.add(bean);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }


    }

    class CoursePrice implements Serializable {

        private int coursePriceId;
        private String coursePriceName;
        private double coursePriceFee;

        public int getCoursePriceId() {
            return coursePriceId;
        }

        public void setCoursePriceId(int coursePriceId) {
            this.coursePriceId = coursePriceId;
        }

        public String getCoursePriceName() {
            return coursePriceName;
        }

        public void setCoursePriceName(String coursePriceName) {
            this.coursePriceName = coursePriceName;
        }

        public double getCoursePriceFee() {
            return coursePriceFee;
        }

        public void setCoursePriceFee(double coursePriceFee) {
            this.coursePriceFee = coursePriceFee;
        }
    }

    class HolesPrice implements Serializable {
        private int holesPriceId;
        private String holesPriceName;
        private double holesPriceFee;

        public int getHolesPriceId() {
            return holesPriceId;
        }

        public void setHolesPriceId(int holesPriceId) {
            this.holesPriceId = holesPriceId;
        }

        public String getHolesPriceName() {
            return holesPriceName;
        }

        public void setHolesPriceName(String holesPriceName) {
            this.holesPriceName = holesPriceName;
        }

        public double getHolesPriceFee() {
            return holesPriceFee;
        }

        public void setHolesPriceFee(double holesPriceFee) {
            this.holesPriceFee = holesPriceFee;
        }
    }
}





