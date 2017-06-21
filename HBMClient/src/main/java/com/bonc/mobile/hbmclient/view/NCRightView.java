package com.bonc.mobile.hbmclient.view;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter.ColumnDisplyInfo;
import com.bonc.mobile.hbmclient.util.NumberUtil;
import com.bonc.mobile.hbmclient.util.StringUtil;

/** 右侧列表的item */
public class NCRightView extends LinearLayout {
	onChildClickListener ocl;
	protected NCListRightItemVIew[] mViews;
	RelativeLayout linearlayoutChart;

	public RelativeLayout getLinearlayoutChart() {
		return linearlayoutChart;
	}

	private String[] mFilter;
	ColumnDisplyInfo[] columnInfo;
	private NumberFormat mFormat;
	Context context;
	float linW = 0;
	int minW = 16;
	int itemW = 0;
	int firstColW = 0;// 第一列宽度
	int itemH = 0;
	int childCount = 0;

	public interface onChildClickListener {
		public void onClickChild(int childIndex);
	}

	public void setOnChildClickListener(onChildClickListener ocl) {
		this.ocl = ocl;
	}

	public void clickOnChild(int childIndex) {
		if (this.ocl != null) {
			ocl.onClickChild(childIndex);
		}
	}

	public int getItemW() {
		return itemW;
	}

	public void setItemW(int itemW) {
		this.itemW = itemW;
	}

	public int getItemH() {
		return itemH;
	}

	public void setItemH(int itemH) {
		this.itemH = itemH;
	}

	public NCRightView(Context context, ColumnDisplyInfo[] columnDisplyInfos,
			float width, boolean isFitW) {
		// TODO Auto-generated constructor stub
		super(context);
		this.context = context;
		setOrientation(HORIZONTAL);
		minW = NumberUtil.DpToPx(context, minW);
		linW = width;
		mFormat = NumberFormat.getInstance();
		mFormat.setGroupingUsed(false);
		mFormat.setMaximumFractionDigits(0);
		removeAllViews();
		if (columnDisplyInfos == null || columnDisplyInfos.length < 1)
			return;
		childCount = columnDisplyInfos.length;
		calculateWH(childCount, isFitW);

		creatChildViews(columnDisplyInfos);
	}

	public NCRightView(Context context, String[] str, int width, boolean isFitW) {
		// TODO Auto-generated constructor stub
		super(context);
		this.context = context;
		setOrientation(HORIZONTAL);
		minW = NumberUtil.DpToPx(context, minW);
		linW = width;
		mFormat = NumberFormat.getInstance();
		mFormat.setGroupingUsed(false);
		mFormat.setMaximumFractionDigits(0);
		removeAllViews();
		if (str == null || str.length < 1)
			return;
		childCount = str.length;
		calculateWH(childCount, isFitW);

		creatChildViews(str);
	}

	private void calculateWH(int childCount, boolean isFitWin) {
		// TODO Auto-generated method stub

		itemW = (int) context.getResources().getDimension(
				R.dimen.zhl_right_column_width);

		if (isFitWin) {
			itemW = (int) Math.ceil(linW / childCount);
		} else {
			if (itemW * childCount <= linW) {
				itemW = (int) Math.ceil(linW / childCount);
			}
		}
		firstColW = itemW;
		itemH = (int) context.getResources().getDimension(
				R.dimen.zhl_item_height);
	}

	/** 将该行的子列设置为自动填满，仅在大小受限的区域内有效 */
	public void setFillInExactly() {
		for (NCListRightItemVIew v : mViews) {
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
	 * 为每一行多重数值的item设置一个背景颜色
	 * 
	 * @param color
	 *            颜色值
	 * */
	public void setAllBackgroundColor(int color) {
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).setBackgroundColor(color);

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
	public void rechargeData(String[] datas, List<Double> trendData) {
		if (mViews == null) {
			return;
		}
		if (datas.length == mViews.length) {
			for (int i = 0; i < mViews.length; i++) {
				if (datas[i] != null) {
					LayoutParams lp = (LayoutParams) mViews[i]
							.getLayoutParams();
					if (i == 0) {
						lp.width = firstColW;
					} else {
						lp.width = itemW;
					}
					lp.height = itemH;
					mViews[i].setContentText(datas[i]);
					if (mFilter != null && mFilter[i] != null) {
						if (mFilter[i].equals("+") && !datas[i].equals("")) {
							String s = datas[i]
									.substring(datas[i].length() - 1);
							double value_double;
							if (s.equals("%")) {
								value_double = NumberUtil
										.changeToDouble(datas[i].substring(0,
												datas[i].length() - 1));
							} else {
								value_double = NumberUtil
										.changeToDouble(datas[i].substring(0,
												datas[i].length()));
							}
							if (value_double > 0) {
								mViews[i].setContentColor(0xffe60005);
							} else {
								mViews[i].setContentColor(0xff208600);
							}
						} else {
							mViews[i].setUnitText(mFilter[i]);
						}
					}

				} else {
					mViews[i].setContentText("");
				}
			}
		} else {
			// creatChildViews(datas);

		}
		if (trendData != null) {
			LayoutParams lp = (LayoutParams) linearlayoutChart
					.getLayoutParams();
			lp.width = itemW;
			lp.height = itemH;
			LineTrendView lineView = new LineTrendView(context);
			lineView.setData(trendData);
			linearlayoutChart.removeAllViews();
			linearlayoutChart.addView(lineView.getView());
		} else {
			linearlayoutChart.removeAllViews();
		}
	}

	/** 根据数值创建一串子view */
	protected void creatChildViews(String[] datas) {
		mViews = new NCListRightItemVIew[datas.length];
		for (int i = 0; i < datas.length; i++) {
			try {
				mViews[i] = new NCListRightItemVIew(getContext());
				addView(mViews[i]);
				LayoutParams lp = (LayoutParams) mViews[i]
						.getLayoutParams();
				if (i == 0) {
					lp.width = firstColW;
				} else {
					lp.width = itemW;
				}
				lp.height = itemH;
				mViews[i].setContentText(datas[i]);
			} catch (NotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	protected void creatChildViews(String[] datas, int itemLayoutId) {

		mViews = new NCListRightItemVIew[datas.length];
		for (int i = 0; i < datas.length; i++) {
			try {
				mViews[i] = new NCListRightItemVIew(getContext(), itemLayoutId);
				addView(mViews[i]);
				LayoutParams lp = (LayoutParams) mViews[i]
						.getLayoutParams();
				if (i == 0) {
					lp.width = firstColW;
				} else {
					lp.width = itemW;
				}
				final int indexOfChild = i;
				lp.height = itemH;
				mViews[i].setContentText(datas[i]);
				mViews[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						clickOnChild(indexOfChild);
					}
				});

			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 添加指标解释说明.
	 * 
	 * @param textSize
	 */
	public void addKpiExplain(final String kpiCodes[]) {

		if (mViews == null || mViews.length == 0) {
			return;
		}

		int vlen = mViews.length;

		for (int i = 0; i < vlen; i++) {

			String kpiExplain = "没有指标口径说明!";
			if (kpiCodes != null && i < kpiCodes.length
					&& !StringUtil.isNull(kpiCodes[i])) {
				Map<String, String> tempMap = new BusinessDao()
						.qryKpiInfoByCode(kpiCodes[i]);
				if (tempMap != null
						&& !StringUtil.isNull(tempMap.get("kpiDefine"))) {
					kpiExplain = tempMap.get("kpiName") + ":"
							+ tempMap.get("kpiDefine");
				}
			}

			final String explainString = kpiExplain;
			mViews[i].setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					Toast.makeText(context, explainString, Toast.LENGTH_LONG)
							.show();
					return false;
				}

			});

		}
	}

	public void setAllViewTextSize(int textSize) {
		if (mViews == null) {
			return;
		}
		for (int i = 0; i < columnInfo.length; i++) {
			try {
				mViews[i].setContentSize(textSize);

			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setAllViewTextColor(int color) {
		if (mViews == null) {
			return;
		}
		for (int i = 0; i < columnInfo.length; i++) {
			try {
				mViews[i].setContentColor(color);

			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void creatChildViews(ColumnDisplyInfo[] columnDisplyInfos) {
		// TODO Auto-generated method stub
		mViews = new NCListRightItemVIew[columnDisplyInfos.length];
		columnInfo = columnDisplyInfos;

		for (int i = 0; i < columnInfo.length; i++) {
			try {
				mViews[i] = new NCListRightItemVIew(getContext());
				addView(mViews[i]);
				LayoutParams lp = (LayoutParams) mViews[i]
						.getLayoutParams();
				if (i == 0) {
					lp.width = firstColW;
				} else {
					lp.width = itemW;
				}
				lp.height = itemH;

				if (columnInfo[i] != null) {
					String nowValueString = getShowValue(lp.width,
							columnInfo[i].getValue());

					mViews[i].setContentText(nowValueString);
					mViews[i].setUnitText(columnInfo[i].getUnit());
					int color = columnInfo[i].getColor();
					try {

						mViews[i].setContentColor(context.getResources()
								.getColor(color));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						mViews[i].setContentColor(color);
					}

				}
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String getShowValue(int width, String value) {
		// TODO Auto-generated method stub
		String valueString = value;
		/*
		 * int sLen= value.length(); if(width<minW*sLen) {
		 * 
		 * if(value.contains(".")) { valueString =
		 * value.substring(0,value.indexOf(".")); } }
		 */
		return valueString;
	}

	public void rechargeData(ColumnDisplyInfo[] columnDisplyInfos,
			List<Double> trendData2) {
		if (mViews == null) {
			return;
		}
		if (columnDisplyInfos.length == mViews.length) {
			for (int i = 0; i < mViews.length; i++) {
				if (columnDisplyInfos[i] != null) {
					LayoutParams lp = (LayoutParams) mViews[i]
							.getLayoutParams();
					if (i == 0) {
						lp.width = firstColW;
					} else {
						lp.width = itemW;
					}
					lp.height = itemH;
					String nowV = getShowValue(lp.width,
							columnDisplyInfos[i].getValue());
					mViews[i].setContentText(nowV);
					mViews[i].setUnitText(columnDisplyInfos[i].getUnit());
					int color = columnDisplyInfos[i].getColor();

					try {
						mViews[i].setContentColor(context.getResources()
								.getColor(color));
					} catch (NotFoundException e) {
						// TODO Auto-generated catch block
						mViews[i].setContentColor(color);
					}

					// mViews[i].setContentColor(context.getResources().getColor(color));

				}
			}
		}
		if (trendData2 != null) {
			LayoutParams lp = (LayoutParams) linearlayoutChart
					.getLayoutParams();
			lp.width = itemW;
			lp.height = itemH;
			LineTrendView lineView = new LineTrendView(context);
			lineView.setData(trendData2);
			linearlayoutChart.removeAllViews();
			linearlayoutChart.addView(lineView.getView());
		}

	}

	public void setViewClickListener(int i, OnClickListener listener) {
		mViews[i].setOnClickListener(listener);
	}

	public void setViewCompoundDrawable(int i, int drawable) {
		mViews[i].setContentCompoundDrawable(drawable);
	}

	/*
	 * public void setFirstColum(int setW) { // TODO Auto-generated method stub
	 * if(firstColW<setW) { firstColW = setW;
	 * 
	 * itemW = itemW-(firstColW-itemW)/(childCount-1); changeW();
	 * 
	 * 
	 * } }
	 */

	/*
	 * private void changeW() { // TODO Auto-generated method stub for (int i =
	 * 0; i < mViews.length; i++) {
	 * 
	 * LinearLayout.LayoutParams lp = new
	 * LinearLayout.LayoutParams(itemW,itemH); lp.width = itemW; if(i==0) {
	 * lp.width =firstColW;
	 * 
	 * }
	 * 
	 * 
	 * lp.height = itemH; mViews[i].setLayoutParams(lp);
	 * 
	 * 
	 * 
	 * 
	 * } }
	 */
}
