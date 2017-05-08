/**
 * Copyright 2014  XCL-Charts
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @Project XCL-Charts
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package org.xclcharts.event.click;

import android.graphics.PointF;


/**
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @ClassName PointPosition
 * @Description 点位置记录信息基类
 */

public class PointPosition extends RectPosition {

    protected PointF mPoint = null;

    public PointPosition() {
    }

    public PointF getPosition() {
        return mPoint;
    }


    public String getPointInfo() {
        if (null == mPoint) {
            return "";
        }
        String info = "x:" + Float.toString(mPoint.x) + " y:" + Float.toString(mPoint.y);
        return info;
    }

}
