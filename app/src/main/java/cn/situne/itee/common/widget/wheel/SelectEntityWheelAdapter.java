/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.situne.itee.common.widget.wheel;


import java.util.List;

import cn.situne.itee.manager.jsonentity.JsonBookingGoodsList;

/**
 * Numeric Wheel adapter.
 */
public class SelectEntityWheelAdapter implements SelectTimeWheelAdapter {

    /**
     * The default min value
     */
    public static final int DEFAULT_MAX_VALUE = 9;

    /**
     * The default max value
     */
    private static final int DEFAULT_MIN_VALUE = 0;

    private List<JsonBookingGoodsList.DataList.CategoryListItem> dataSource;
    private int maximumLength;


    public List<JsonBookingGoodsList.DataList.CategoryListItem> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<JsonBookingGoodsList.DataList.CategoryListItem> dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Constructor
     */
    public SelectEntityWheelAdapter(List<JsonBookingGoodsList.DataList.CategoryListItem> dataSource) {
        this.dataSource = dataSource;
        for (int i = 0; i < dataSource.size(); i++) {
            JsonBookingGoodsList.DataList.CategoryListItem temp = dataSource.get(i);
            if (temp.getAttrName().getBytes().length > maximumLength) {
                maximumLength = temp.getAttrName().getBytes().length;
            }
        }
        if (maximumLength < DEFAULT_MAX_VALUE) {
            maximumLength = DEFAULT_MAX_VALUE;
        }
    }

    @Override
    public String getItem(int index) {
        if (index >= 0 && index < getItemsCount()) {
            return dataSource.get(index).getAttrName();
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return dataSource.size();
    }

    @Override
    public int getMaximumLength() {
        return maximumLength;
    }


}
