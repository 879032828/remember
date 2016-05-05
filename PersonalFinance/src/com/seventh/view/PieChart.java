package com.seventh.view;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class PieChart {
	/**
	 * 画饼状图
	 */
	public void paintingPieChart(Context context, LinearLayout piechar,int varlue1,int varlue2) {
		// 饼状图每个板块所占的比例
		double[] values = { varlue1, varlue2};
		// 饼状图每个板块的颜色
		int[] colors = { Color.GREEN, Color.RED};
		// 画饼状图
		GraphicalView mPieChartView;
		CategorySeries series = new CategorySeries("title");
		
		// 支出所占的比例为values[0]的值
		series.add("收入", values[0]);
		// 收入达标所占的比例为values[1]的值
		series.add("支出", values[1]);

		DefaultRenderer renderer = new DefaultRenderer();
		// 设置背景颜色
		renderer.setBackgroundColor(0);
		// 设置是否使用背景颜色
		renderer.setApplyBackgroundColor(true);
		// 设置 轴标签字体大小
		renderer.setLabelsTextSize(8);
		// 设置 轴标签字体颜色
		renderer.setLabelsColor(Color.BLACK);
		// 图例字体大小
		renderer.setLegendTextSize(8);
		// 图形 4边的边距
		renderer.setMargins(new int[] { 0, 20, 0, 0 }); 
		
		renderer.setChartTitleTextSize(10);//设置图表标题的文字大小
		renderer.setChartTitle("支出收入比例");//设置图表的标题  默认是居中顶部显示
		// 为饼状图赋予颜色
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}

		// 集合饼状图各种属性于mPieChartView
		mPieChartView = ChartFactory.getPieChartView(context, series, renderer);
		//去除piechar中所有view
		piechar.removeAllViews();
		// 将mPieChartView加到piechar视图中
		piechar.addView(mPieChartView, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

}
