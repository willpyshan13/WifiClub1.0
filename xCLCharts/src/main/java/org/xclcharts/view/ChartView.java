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
 * @version 1.7
 */
package org.xclcharts.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.xclcharts.event.touch.ChartTouch;
import org.xclcharts.renderer.XChart;

import java.util.List;


/**
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com) QQ群: 374780627
 * @ClassName ChartView
 * @Description 含手势操作的XCL-Charts图表View基类
 */
public abstract class ChartView extends GraphicalView {

    private ChartTouch mChartTouch[];


    public ChartView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub

    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public ChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }


    @Override
    public void render(Canvas canvas) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        touchChart(event);
        return true;
    }

    /**
     * 绑定需要处理的图表对象类
     *
     * @return 对象集
     */
    public abstract List<XChart> bindChart();
    //////////////////////////////////////////////////////

    //////////////////////////////////////////////////////
    //用于手势操作来平移或放大缩小图表
    //////////////////////////////////////////////////////
    private int getTouchCharts() {
        if (null != mChartTouch) {
            return mChartTouch.length;
        }

        List<XChart> listCharts = bindChart();
        if (listCharts == null) {
            return 0;
        }
        int count = listCharts.size();
        if (0 == count) {
            return 0;
        }

        mChartTouch = new ChartTouch[count];
        for (int i = 0; i < count; i++) {
            mChartTouch[i] = new ChartTouch(this, listCharts.get(i));
        }

        return count;
    }


    public boolean touchChart(MotionEvent event) {
        int count = getTouchCharts();
        for (int i = 0; i < count; i++) {
            mChartTouch[i].handleTouch(event);
        }

        return true;
    }

}
