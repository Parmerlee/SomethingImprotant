package com.bonc.mobile.hbmclient.terminal.view;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.NumberUtil;

/** 右侧列表的item */
public class StatisticsRightView extends LinearLayout {
	protected StatiticsListRightItemVIew[] mViews;
	private String[] mFilter;
	private String[] mDatas;
	private NumberFormat mFormat;

	public StatisticsRightView(Context context, String[] datas) {
		super(context);
		// TODO Auto-generated constructor stub
		setOrientation(HORIZONTAL);
		mFormat = new DecimalFormat("##0.0");
		mFormat.setGroupingUsed(false);
		mFormat.setMaximumFractionDigits(1);
		mFormat.setMinimumFractionDigits(1);
		mFormat.setMinimumIntegerDigits(1);
		creatChildViews(datas);

	}

	/** 将该行的子列设置为自动填满，仅在大小受限的区域内有效 */
	public void setFillInExactly() {
		for (StatiticsListRightItemVIew v : mViews) {
			LayoutParams lp = (LayoutParams) v.getLayoutParams();
			lp.weight = 1;
			lp.width = LayoutParams.MATCH_PARENT;
		}
	}

	/**
	 * 为每一行多重数值的item设置一个背景图片id
	 * 
	 * @param id
	 *            drawable资源id
	 * */
	public void setAllBackgroundByID(int id) {
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).setBackgroundResource(id);
		}
	}

	/**
	 * 为每一行多重数值的item设置一个背景drawable
	 * 
	 * @param drawable
	 *            欲添加的drawable
	 * */
	public void setAllBackgroundDrawable(Drawable drawale) {
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).setBackgroundDrawable(drawale);
		}
	}

	/**
	 * 重新装填数据，如果新数据比现有数据长会导致item延长,如果新数据比现有数据短则会导致item缩短
	 * 
	 * @param datas
	 *            每行数据的数组，每个元素表述一格数据
	 */
	public void rechargeData(String[] datas) {
		if (mViews == null) {
			return;
		}
		if (datas.length == mViews.length) {
			for (int i = 0; i < mViews.length; i++) {
				double value_double = 0;
				if (datas[i] != null) {
					if (mFilter != null && mFilter[i] != null) {
						if (mFilter[i].equals("+")) {
							try {
								String s = datas[i]
										.substring(datas[i].length() - 1);

								if (s.equals("%")) {
									value_double = NumberUtil
											.changeToDouble(datas[i].substring(
													0, datas[i].length() - 1)) * 100;
								} else {
									value_double = NumberUtil
											.changeToDouble(datas[i].substring(
													0, datas[i].length())) * 100;
								}
								if (value_double > 0) {
									mViews[i]
											.setContentColor(getResources()
													.getColor(
															R.color.default_minus_color));
								} else {
									mViews[i]
											.setContentColor(getResources()
													.getColor(
															R.color.default_plus_color));
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							mViews[i].setUnitText("%");

						} else if ("".equals(datas[i])) {
							mViews[i].setUnitText("");
							mViews[i].setContentText("");
						} else {
							try {
								double value = Double.parseDouble(datas[i]);

								String pattern = "##0.00";

								if (value < 10000) {
									mViews[i].setUnitText("万" + mFilter[i]);
									pattern = "##0";
								}

								if (value >= 10000 && value < 100000000) {
									mViews[i].setUnitText("万" + mFilter[i]);
									pattern = "##0.00";
								}

								if (value > 100000000) {
									mViews[i].setUnitText("亿" + mFilter[i]);
									pattern = "##0.00";

								}
								DecimalFormat dFormat = new DecimalFormat(
										pattern);
								mViews[i].setContentText(dFormat.format(value));

							} catch (Exception e) {
								mViews[i].setContentText(datas[i]);
								mViews[i].setUnitText(mFilter[i]);
							}
						}
					} else if (mFilter != null) {
						String ss = datas[i];
						String s = null;
						if (ss.length() == 0) {
							s = "0";
						} else {
							s = ss.substring(ss.length() - 1);
						}
						if (s.equals("%")) {
							value_double = NumberUtil.changeToDouble(datas[i]
									.substring(0, datas[i].length() - 1)) * 100;
						}
					}

					if (value_double != 0) {
						mViews[i].setContentText(mFormat.format(value_double));
					} else {
						mViews[i].setContentText(datas[i]);
					}
				} else {
					mViews[i].setContentText("");

				}
			}
		} else {
			// creatChildViews(datas);
		}
	}

	public void setFilter(String f[]) {
		mFilter = f;
		/*
		 * for (int i = 0; i < mDatas.length; i++){
		 * if(mFilter!=null&&i<mFilter.length&&mFilter[i]!=null){
		 * if(mFilter[i].equals("+")){
		 * 
		 * try{ String s=mDatas[i].substring(mDatas[i].length()-1); double
		 * value_double; if(s.equals("%")){
		 * value_double=NumberUtil.changeToDouble
		 * (mDatas[i].substring(0,mDatas[i].length()-1))*100; }else{
		 * value_double
		 * =NumberUtil.changeToDouble(mDatas[i].substring(0,mDatas[i]
		 * .length()))*100; } if(value_double>0){
		 * mViews[i].setContentColor(getResources
		 * ().getColor(R.color.default_minus_color)); }else{
		 * mViews[i].setContentColor
		 * (getResources().getColor(R.color.default_plus_color)); }
		 * }catch(Exception e){ e.printStackTrace(); } }
		 * 
		 * mViews[i].setUnitText("%");
		 * 
		 * }else{ mViews[i].setUnitText(mFilter[i]); } }
		 */
	}

	/** 根据数值创建一串子view */
	protected void creatChildViews(String[] datas) {
		removeAllViews();
		mViews = new StatiticsListRightItemVIew[datas.length];
		mDatas = datas;
		for (int i = 0; i < datas.length; i++) {
			mViews[i] = new StatiticsListRightItemVIew(getContext());
			addView(mViews[i]);
			LayoutParams lp = (LayoutParams) mViews[i]
					.getLayoutParams();
			lp.width = 140;
		}
	}
}
