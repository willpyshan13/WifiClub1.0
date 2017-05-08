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


import cn.situne.itee.manager.jsonentity.JsonChangeTransferArea;

/**
 * Numeric Wheel adapter.
 */
public class SelectTransferTimeWheelAdapter implements SelectTimeWheelAdapter {

    /**
     * The default min value
     */
    public static final int DEFAULT_MAX_VALUE = 9;

    /**
     * The default max value
     */
    private static final int DEFAULT_MIN_VALUE = 0;

    private JsonChangeTransferArea.TransferTimeItem dataSource;
    private int maximumLength;


    public JsonChangeTransferArea.TransferTimeItem getDataSource() {
        return dataSource;
    }

    public void setDataSource(JsonChangeTransferArea.TransferTimeItem dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Constructor
     */
    public SelectTransferTimeWheelAdapter(JsonChangeTransferArea.TransferTimeItem dataSource) {
        int maximumLength = 0;
        this.dataSource = dataSource;
        for (int i = 0; i < dataSource.timeList.size(); i++) {
            String temp = dataSource.timeList.get(i);
            if (temp.length() > maximumLength) {
                maximumLength = temp.length();
            }
        }
        this.maximumLength = maximumLength;
    }

    @Override
    public String getItem(int index) {
        if (index >= 0 && index < getItemsCount()) {
            return dataSource.timeList.get(index);
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return dataSource.timeList.size();
    }

    @Override
    public int getMaximumLength() {
        return maximumLength;
    }


}
