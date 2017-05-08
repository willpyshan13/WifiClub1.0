/**
 * Project Name: itee
 * File Name:	 PieChartView.java
 * Package Name: cn.situne.itee.view.chart
 * Date:		 2015-08-05
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import org.xclcharts.chart.PieChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.event.click.ArcPosition;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.info.PlotArcLabelInfo;
import org.xclcharts.renderer.plot.PlotLegend;
import org.xclcharts.view.ChartView;

import java.util.LinkedList;
import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:PieChartView <br/>
 * Function: Pie chart view. <br/>
 * Date: 2015-08-05 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class PieChartView extends RelativeLayout {

    private IteeTextView tvTitle;
    private PieChartInnerView chartInnerView;
//    private String title;

    public PieChartView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        tvTitle = new IteeTextView(context);
        tvTitle.setGravity(Gravity.CENTER_VERTICAL);

        chartInnerView = new PieChartInnerView(context);
        addView(chartInnerView);
        RelativeLayout.LayoutParams layoutParams
                = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        chartInnerView.setLayoutParams(layoutParams);
        setBackgroundColor(getContext().getResources().getColor(R.color.common_light_gray));

        addView(tvTitle);

        RelativeLayout.LayoutParams paramsTvTitle = (LayoutParams) tvTitle.getLayoutParams();
        paramsTvTitle.topMargin = DensityUtil.getActualHeightOnThisDevice(20, getContext());
        paramsTvTitle.leftMargin = DensityUtil.getActualWidthOnThisDevice(20, getContext());
        tvTitle.setLayoutParams(paramsTvTitle);
    }

    public void setChartData(LinkedList<PieData> chartData) {
        chartInnerView.setChartData(chartData);
    }

    public void setTitle(CharSequence title) {
        tvTitle.setText(title);
    }

    public void setChartSize(int width, int height) {
        chartInnerView.setChartSize(width, height);
    }

    public static class PieChartInnerView extends ChartView {

        Paint mPaintToolTip = new Paint(Paint.ANTI_ALIAS_FLAG);
        private String TAG = "PieChartView";
        private PieChart chart = new PieChart();
        private LinkedList<PieData> chartData = new LinkedList<>();

        public PieChartInnerView(Context context) {
            super(context);
            initView();
        }

        @Override
        public List<XChart> bindChart() {
            return null;
        }

        public void setChartData(LinkedList<PieData> chartData) {
            this.chartData = chartData;
            chartRender();
        }

        public void setTitle(String title) {
            chart.setTitle(title);
        }

        public void setChartSize(int width, int height) {
            chart.setChartRange(width, height);
        }

        private void initView() {

            chart.saveLabelsPosition(XEnum.LabelSaveType.ALL);
        }

        private void chartRender() {
            try {

                chart.setDataSource(chartData);

                //标签显示(隐藏，显示在中间，显示在扇区外面,折线注释方式)
                chart.setLabelStyle(XEnum.SliceLabelStyle.BROKENLINE);
                chart.getLabelBrokenLine().setLinePointStyle(XEnum.LabelLinePoint.END);
                chart.syncLabelColor();
                chart.syncLabelPointColor();
                chart.syncLabelLineColor();

                chart.getLabelPaint().setTextSize(Constants.FONT_SIZE_LARGER);
                chart.getPlotLegend().getPaint().setTextSize(Constants.FONT_SIZE_MORE_LARGER);


                //图的内边距
                //注释折线较长，缩进要多些
                int[] ltrb = new int[4];
                ltrb[0] = (int) (DensityUtil.getScreenWidth(getContext()) * 0.25); //left
                ltrb[1] = DensityUtil.getActualHeightOnThisDevice(40, getContext()); //top
                ltrb[2] = (int) (DensityUtil.getScreenWidth(getContext()) * 0.25); //right
                ltrb[3] = DensityUtil.getActualHeightOnThisDevice(40, getContext()); //bottom

                chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

                //标题
                chart.setTitleAlign(XEnum.HorizontalAlign.LEFT);

                //隐藏渲染效果
                chart.hideGradient();
                //显示边框
                //chart.showRoundBorder();


                //激活点击监听
                chart.ActiveListenItemClick();
                chart.showClikedFocus();
                chart.disablePanMode();

                //显示图例
                PlotLegend legend = chart.getPlotLegend();
                legend.show();
                legend.setHorizontalAlign(XEnum.HorizontalAlign.LEFT);
                legend.setVerticalAlign(XEnum.VerticalAlign.BOTTOM);
                legend.showBorder();
                legend.showBackground();
                legend.showBox();

                postInvalidate();


            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }


        @Override
        public void render(Canvas canvas) {
            try {
                chart.render(canvas);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }


        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            //图所占范围大小
            chart.setChartRange(w, h);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                triggerClick(event.getX(), event.getY());
            }
            return true;
        }


        //触发监听
        private void triggerClick(float x, float y) {
            if (!chart.getListenItemClickStatus()) {
                return;
            }
            ArcPosition record = chart.getPositionRecord(x, y);

            if (chart.getLabelsPosition() != null) {
                for (int i = 0; i < chart.getLabelsPosition().size(); i++) {
                    PlotArcLabelInfo info = chart.getLabelsPosition().get(i);
                    if (info != null && info.getLabelPointF() != null) {
                        Utils.log(info.getLabelPointF().x + " : " + info.getLabelPointF().y);
                    }
                }
            }

            if (null == record) {
                return;
            }

            PieData pData = chartData.get(record.getDataID());

            //	boolean isInvaldate = true;
            for (int i = 0; i < chartData.size(); i++) {
                PieData cData = chartData.get(i);
                if (i == record.getDataID()) {
                    if (cData.getSelected()) {
                        break;
                    } else {
                        cData.setSelected(true);
                    }
                } else {
                    cData.setSelected(false);
                }
            }

            //显示选中框
//            chart.showFocusArc(record, pData.getSelected());
//            chart.getFocusPaint().setStyle(Paint.Style.STROKE);
//            chart.getFocusPaint().setStrokeWidth(5);
//            chart.getFocusPaint().setColor(Color.GREEN);
//            chart.getFocusPaint().setAlpha(100);


            //在点击处显示tooltip
            mPaintToolTip.setColor(Color.RED);
            mPaintToolTip.setTextSize(Constants.FONT_SIZE_NORMAL);
            chart.getToolTip().setCurrentXY(x, y);
            chart.getToolTip().addToolTip(pData.getKey(), mPaintToolTip);

            this.refreshChart();
        }

//        class DrawLabelTask extends AsyncTask<Void, Void, Void> {
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                while (chart.getLabelsPosition().size() == 0) {
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                int width = 160;
//                int height = 60;
//                RelativeLayout rlContainer = (RelativeLayout) getParent();
//                for (int i = 0; i < chart.getLabelsPosition().size(); i++) {
//                    PlotArcLabelInfo info = chart.getLabelsPosition().get(i);
//
//                    TextView textView = new TextView(getContext());
//                    rlContainer.addView(textView);
//
//                    textView.setTextSize(Constants.FONT_SIZE_SMALLEST);
//                    textView.setLines(2);
//
//                    RelativeLayout.LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
//
//                    layoutParams.width = width;
//                    layoutParams.height = height;
//
//                    String label = chartData.get(i).getLabel();
//
//                    configXY(width, height, info, textView, layoutParams);
//
//                    textView.setText(label);
//                    textView.setLayoutParams(layoutParams);
//                }
//
//            }
//
//            private void configXY(int width, int height, PlotArcLabelInfo info, TextView textView, LayoutParams layoutParams) {
//                float stopX = info.getLabelPointF().x;
//                float stopY = info.getLabelPointF().y;
//
//                float cirX = info.getX();
//                float cirY = info.getY();
//
//                if (Float.compare(stopX, cirX) == 0) { //位于中间竖线上
//                    if (Float.compare(stopY, cirY) == 1) {//中点上方,左折线
//                        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
//                        layoutParams.leftMargin = (int) info.getLabelPointF().x - width / 2;
//                        layoutParams.topMargin = (int) info.getLabelPointF().y - height;
//
//                    } else { //中点下方,右折线
//                        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
//                        layoutParams.leftMargin = (int) info.getLabelPointF().x - width / 2;
//                        layoutParams.topMargin = (int) info.getLabelPointF().y - height;
//                    }
//                } else if (Float.compare(stopY, cirY) == 0) { //中线横向两端
//                    if (Float.compare(stopX, cirX) == 0 ||
//                            Float.compare(stopX, cirX) == -1) {//左边
//                        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
//                        layoutParams.leftMargin = (int) info.getLabelPointF().x - width;
//                        layoutParams.topMargin = (int) info.getLabelPointF().y - height / 2;
//                    } else {
//                        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
//                        layoutParams.leftMargin = (int) info.getLabelPointF().x;
//                        layoutParams.topMargin = (int) info.getLabelPointF().y - height / 2;
//                    }
//
//                } else if (Float.compare(stopX, cirX) == 1) {//右边
//                    textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
//                    layoutParams.leftMargin = (int) info.getLabelPointF().x;
//                    layoutParams.topMargin = (int) info.getLabelPointF().y - height / 2;
//                } else if (Float.compare(stopX, cirX) == -1) {//左边
//                    textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
//                    layoutParams.leftMargin = (int) info.getLabelPointF().x - width;
//                    layoutParams.topMargin = (int) info.getLabelPointF().y - height / 2;
//                } else {
//                    textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
//                    layoutParams.leftMargin = (int) info.getLabelPointF().x;
//                    layoutParams.topMargin = (int) info.getLabelPointF().y;
//                }
//            }
//        }
    }
}