package com.bonc.mobile.hbmclient.terminal.view;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;

/**
 * 2013-3-7
 * 
 * @author ZZZ
 *
 */
public class HuanBaseView extends LinearLayout {
	// List<PieInfo> list1= new ArrayList<PieInfo>();
	// List<PieInfo> list2= new ArrayList<PieInfo>();
	List<double[]> values = new ArrayList<double[]>();
	List<String[]> titles = new ArrayList<String[]>();
	String title1, title2;
	String[] bfbs1, bfbs2;
	double[] duliangs1, duliangs2;
	int[] colorLib = new int[] { Color.BLUE, Color.GREEN, Color.RED,
			Color.YELLOW, Color.CYAN };
	int[] colorsArr;

	public HuanBaseView(Context context) {
		super(context);
	}

	/**
	 * 
	 * @param context
	 * @param list
	 *            环图对象的数据集
	 * @param colorsArr
	 *            环图颜色库
	 * @param title
	 *            环图的标题
	 */
	// public HuanBaseView(Context context, List<PieInfo> list1,String title1){
	// super(context);
	// colorsArr=new int[list1.size()];
	// this.list1=list1;
	// this.title1=title1;
	// }
	// public HuanBaseView(Context context, List<PieInfo> list1,List<PieInfo>
	// list2, String title1,String title2){
	// super(context);
	// this.list1=list1;
	// this.list2=list2;
	// this.title1=title1;
	// this.title2=title2;
	// }
	// public View getView(){
	// colorsArr=new int[list1.size()];
	// bfbs1 = new String[list1.size()];
	// duliangs1= new double[list1.size()];
	// bfbs2 = new String[list2.size()];
	// duliangs2= new double[list2.size()];
	// for(int q=0;q<list1.size();q++){
	// bfbs1[q]=list1.get(q).bfb;
	// duliangs1[q]=NumberUtil.changeToDouble(list1.get(q).duliang);
	// colorsArr[q]=colorLib[q];
	// }
	// titles.add(bfbs1);
	// values.add(duliangs1);
	// if(list2.size()!=0){
	// for(int q=0;q<list2.size();q++){
	// bfbs2[q]=list2.get(q).bfb;
	// duliangs2[q]=NumberUtil.changeToDouble(list2.get(q).duliang);
	// colorsArr[q]=colorLib[q];
	// }
	// titles.add(bfbs2);
	// values.add(duliangs2);
	// }
	//
	//
	//
	// MultipleCategorySeries dataset= buildMultipleCategoryDataset("", titles,
	// values);
	// DefaultRenderer renderer = buildCategoryRenderer(colorsArr);
	// return ChartFactory.getDoughnutChartView(this.getContext(), dataset,
	// renderer);
	// }
	protected DefaultRenderer buildCategoryRenderer(int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setMargins(new int[] { 20, 30, 15, 0 });
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		renderer.setPanEnabled(false);// 禁止拖动图
		renderer.setLabelsColor(Color.BLACK);
		return renderer;
	}

	protected MultipleCategorySeries buildMultipleCategoryDataset(String title,
			List<String[]> titles, List<double[]> values) {
		MultipleCategorySeries series = new MultipleCategorySeries(title);
		int k = 0;
		for (double[] value : values) {
			if (k == 0)
				series.add(title1, titles.get(k), value);
			if (k == 1)
				series.add(title2, titles.get(k), value);
			k++;
		}
		return series;
	}
}
