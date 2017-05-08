/**
 * Project Name: itee
 * File Name:	 SelectAddressActivity.java
 * Package Name: cn.situne.itee.activity
 * Date:		 2015-07-10
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Locale;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:SelectAddressActivity <br/>
 * Function: FIXME. <br/>
 * Date: 2015-07-10 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class SelectAddressActivity extends BaseActivity {

    private RelativeLayout rlContentContainer;

    private ListView lvCity;
    private ListView lvProvince;
    private ListView lvCountry;

    private AddressAdapter adapterCity;
    private AddressAdapter adapterProvince;
    private AddressAdapter adapterCountry;

    private ArrayList<Country> countries;
    private ArrayList<Province> provinces;
    private ArrayList<City> cities;

    private SQLiteDatabase database;

    private String currentCountry;
    private String currentProvince;
    private String currentCity;

    private String passedIndex;

    @Override
    protected void initControls() {

        rlContentContainer = (RelativeLayout) findViewById(R.id.rl_content_container);

        lvCountry = new ListView(this);
        lvProvince = new ListView(this);
        lvCity = new ListView(this);

        database = openOrCreateDatabase(Constants.DB_NAME_ADDRESS, MODE_PRIVATE, null);

        Intent intent = getIntent();
        if (intent != null) {
            passedIndex = intent.getStringExtra(TransKey.COMMON_INDEX);
            currentCountry = intent.getStringExtra(TransKey.COMMON_ADDRESS_COUNTRY);
            currentProvince = intent.getStringExtra(TransKey.COMMON_ADDRESS_PROVINCE);
            currentCity = intent.getStringExtra(TransKey.COMMON_ADDRESS_CITY);
        }

        getTvLeftTitle().setText(R.string.select_address_region);
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    @SuppressWarnings("unchecked")
    protected void setListenersOfControls() {
        lvCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Country country = countries.get(position);
                provinces = getProvinces(country.getId());
                adapterProvince = new AddressAdapter(SelectAddressActivity.this, provinces);
                lvProvince.setAdapter(adapterProvince);

                lvCountry.setVisibility(View.GONE);
                lvProvince.setVisibility(View.VISIBLE);

                if (Locale.getDefault().equals(Locale.CHINA)) {
                    currentCountry = country.getNameCn();
                } else {
                    currentCountry = country.getName();
                }
            }
        });

        lvProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Province province = provinces.get(position);
                cities = getCities(province.getId());
                adapterCity = new AddressAdapter(SelectAddressActivity.this, cities);
                lvCity.setAdapter(adapterCity);

                lvProvince.setVisibility(View.GONE);
                lvCity.setVisibility(View.VISIBLE);

                if (Locale.getDefault().equals(Locale.CHINA)) {
                    currentProvince = province.getNameCn();
                } else {
                    currentCountry = province.getName();
                }
            }
        });

        lvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City city = cities.get(position);
                if (Locale.getDefault().equals(Locale.CHINA)) {
                    currentCity = city.getNameCn();
                } else {
                    currentCity = city.getName();
                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra(TransKey.COMMON_ADDRESS_COUNTRY, currentCountry);
                returnIntent.putExtra(TransKey.COMMON_ADDRESS_PROVINCE, currentProvince);
                returnIntent.putExtra(TransKey.COMMON_ADDRESS_CITY, currentCity);
                returnIntent.putExtra(TransKey.COMMON_ADDRESS_POST_CODE, city.getPostCode());
                returnIntent.putExtra(TransKey.COMMON_INDEX, passedIndex);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {
        rlContentContainer.addView(lvCountry);
        rlContentContainer.addView(lvProvince);
        rlContentContainer.addView(lvCity);

        LayoutUtils.setMatchParentLayout(lvCountry);
        LayoutUtils.setMatchParentLayout(lvProvince);
        LayoutUtils.setMatchParentLayout(lvCity);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void setPropertyOfControls() {
        lvCity.setVisibility(View.GONE);
        lvProvince.setVisibility(View.GONE);
        lvCountry.setVisibility(View.VISIBLE);

        countries = getCountries();
        adapterCountry = new AddressAdapter(this, countries);
        lvCountry.setAdapter(adapterCountry);

        provinces = getProvinces(-1);
        adapterProvince = new AddressAdapter(this, provinces);
        lvProvince.setAdapter(adapterProvince);

        cities = getCities(-1);
        adapterCity = new AddressAdapter(this, cities);
        lvCity.setAdapter(adapterCity);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_address;
    }

    @Override
    protected View.OnClickListener getBackListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lvCity.getVisibility() == View.VISIBLE) {
                    lvCity.setVisibility(View.GONE);
                    lvProvince.setVisibility(View.VISIBLE);
                } else if (lvProvince.getVisibility() == View.VISIBLE) {
                    lvProvince.setVisibility(View.GONE);
                    lvCountry.setVisibility(View.VISIBLE);
                } else {
                    finish();
                }
            }
        };
    }

    private ArrayList<Country> getCountries() {
        ArrayList<Country> countries = new ArrayList<>();
        Cursor cursor = database.rawQuery("select id,name,name_cn from country", new String[0]);
        while (cursor.moveToNext()) {
            Country country = new Country();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String nameCn = cursor.getString(cursor.getColumnIndex("name_cn"));
            country.setId(id);
            country.setName(name);
            country.setNameCn(nameCn);
            countries.add(country);
        }
        cursor.close();
        return countries;
    }

    private ArrayList<Province> getProvinces(int countryId) {
        ArrayList<Province> provinces = new ArrayList<>();
        if (countryId > -1) {
            Cursor cursor = database.rawQuery("select id,name,name_cn from province where cid = ? order by name asc",
                    new String[]{String.valueOf(countryId)});
            int index = 0;
            int currentIndex = -1;
            while (cursor.moveToNext()) {
                Province province = new Province();
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String nameCn = cursor.getString(cursor.getColumnIndex("name_cn"));
                if (StringUtils.isNotEmpty(currentProvince)) {
                    if (currentProvince.equals(name) || currentProvince.equals(nameCn)) {
                        currentIndex = index;
                    }
                }
                province.setId(id);
                province.setName(name);
                province.setNameCn(nameCn);
                province.setCid(countryId);
                provinces.add(province);
                index++;
            }
            if (currentIndex > -1) {
                Province province = provinces.get(currentIndex);
                provinces.remove(currentIndex);
                provinces.add(0, province);
            }
            cursor.close();
        }
        return provinces;
    }

    private ArrayList<City> getCities(int provinceId) {
        ArrayList<City> cities = new ArrayList<>();
        if (provinceId > -1) {
            Cursor cursor = database.rawQuery("select id,name,name_cn,post_code from city where pid = ? order by name asc",
                    new String[]{String.valueOf(provinceId)});
            int index = 0;
            int currentIndex = -1;
            while (cursor.moveToNext()) {
                City city = new City();
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String nameCn = cursor.getString(cursor.getColumnIndex("name_cn"));
                String postCode = cursor.getString(cursor.getColumnIndex("post_code"));
                if (StringUtils.isNotEmpty(currentCity)) {
                    if (currentCity.equals(name) || currentCity.equals(nameCn)) {
                        currentIndex = index;
                    }
                }
                city.setId(id);
                city.setName(name);
                city.setNameCn(nameCn);
                city.setPostCode(postCode);
                city.setPid(provinceId);
                cities.add(city);
                index++;
            }
            if (currentIndex > -1) {
                City city = cities.get(currentIndex);
                cities.remove(currentIndex);
                cities.add(0, city);
            }
            cursor.close();
        }
        return cities;
    }

    class AddressAdapter<T extends AddressListItem> extends BaseAdapter {

        private Context mContext;
        private ArrayList<T> dataSource;

        public AddressAdapter(Context context, ArrayList<T> dataSource) {
            this.mContext = context;
            this.dataSource = dataSource;
        }

        @Override
        public int getCount() {
            if (dataSource != null) {
                return dataSource.size();
            }
            return 0;
        }

        @Override
        public AddressListItem getItem(int position) {
            return dataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_of_address, parent, false);

                viewHolder.tvName = (IteeTextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tvSelectedFlag = (IteeTextView) convertView.findViewById(R.id.tv_selected_flag);
                viewHolder.tvDetailArrow = (IteeTextView) convertView.findViewById(R.id.tv_detail_arrow);

                viewHolder.tvName.setId(View.generateViewId());
                viewHolder.tvSelectedFlag.setId(View.generateViewId());
                viewHolder.tvDetailArrow.setId(View.generateViewId());

                LayoutUtils.setViewLayoutHeight(convertView, 100, mContext);
                LayoutUtils.setCellLeftKeyViewOfRelativeLayout(viewHolder.tvName, mContext);
                LayoutUtils.setRightArrow(viewHolder.tvDetailArrow, mContext);
                LayoutUtils.setLeftOfView(viewHolder.tvSelectedFlag, viewHolder.tvDetailArrow, 10, mContext);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            AddressListItem item = dataSource.get(position);

            String name;
            if (Locale.getDefault().equals(Locale.CHINA)) {
                name = item.getNameCn();
            } else {
                name = item.getName();
            }
            viewHolder.tvName.setText(name);

            if (item instanceof Country) {
                if (StringUtils.isNotEmpty(currentCountry)) {
                    if (currentCountry.equals(name)) {
                        viewHolder.tvSelectedFlag.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.tvSelectedFlag.setVisibility(View.INVISIBLE);
                    }
                }
            } else if (item instanceof Province) {

            } else if (item instanceof City) {
                viewHolder.tvDetailArrow.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.tvDetailArrow.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        class ViewHolder {
            IteeTextView tvName;
            IteeTextView tvSelectedFlag;
            IteeTextView tvDetailArrow;
        }
    }

    abstract class AddressListItem {
        private int id;
        private String name;
        private String nameCn;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNameCn() {
            return nameCn;
        }

        public void setNameCn(String nameCn) {
            this.nameCn = nameCn;
        }
    }

    class Country extends AddressListItem {

    }

    class Province extends AddressListItem {

        private int cid;

        public void setCid(int cid) {
            this.cid = cid;
        }

    }

    class City extends AddressListItem {

        private int pid;
        private String postCode;

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }
    }
}