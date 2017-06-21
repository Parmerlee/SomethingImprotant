package com.bonc.mobile.hbmclient.view;

import java.text.NumberFormat;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.util.NumberUtil;
import com.bonc.mobile.hbmclient.util.StringUtil;

//import android.widget.TextView;

/** 右侧列表的item */
public class KpiTitleView extends LinearLayout {
	onChildClickListener ocl;
	protected KpiSortRightItemVIew[] mViews;

	private NumberFormat mFormat;

	Context context;
	float linW = 0;
	int minW = 16;
	int itemW = 0;
	int firstColW = 0;// 第一列宽度
	int itemH = 0;
	int childCount = 0;
	String[] datas;

	/**
	 * 
	 * @param context
	 * @param itemLayoutId
	 *            使用的布局
	 * @param datas要用的字符数组
	 * @param offsetN要额外多计算宽度的列数
	 * @param width基本填充宽度
	 */
	public KpiTitleView(Context context, int itemLayoutId, String[] datas,
			int offsetN, float width) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		setOrientation(HORIZONTAL);
		this.datas = datas;
		minW = NumberUtil.DpToPx(context, minW);
		linW = width;
		mFormat = NumberFormat.getInstance();
		mFormat.setGroupingUsed(false);
		mFormat.setMaximumFractionDigits(0);
		removeAllViews();
		// childCount = datas.length + offsetN;
		childCount = datas.length;
		calculateWH(childCount);

		creatChildViews(datas, itemLayoutId);
	}

	public interface onChildClickListener {
		public void onClickChild(int childIndex, int selectSortIndex,
								 int selectStatics);
	}

	public void setOnChildClickListener(onChildClickListener ocl) {
		this.ocl = ocl;
	}

	public void clickOnChild(int childIndex, int selectSortIndex,
			int selectStatics) {
		if (this.ocl != null) {
			ocl.onClickChild(childIndex, selectSortIndex, selectStatics);
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

	private void calculateWH(int childCount) {
		// TODO Auto-generated method stub

		itemW = (int) context.getResources().getDimension(
				R.dimen.zhl_right_column_width);

		if (itemW * childCount < linW) {
			itemW = (int) Math.ceil(linW / childCount);
		}
		firstColW = itemW;
		itemH = (int) context.getResources().getDimension(
				R.dimen.zhl_item_height);
	}

	/** 将该行的子列设置为自动填满，仅在大小受限的区域内有效 */
	public void setFillInExactly() {
		for (KpiSortRightItemVIew v : mViews) {
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
	public void rechargeData(String[] datas) {
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

					if (i == getSelectSortIndex()) {
						mViews[i].setStatics(getSelectStatics());

					} else {
						mViews[i].setNormal();
					}
				} else {
					mViews[i].setContentText("");
				}
			}
		} else {
			// creatChildViews(datas);

		}

	}

	public static final int TITLE_SORT_NO = 0;
	public static final int TITLE_SORT_UP = 1;// 升序
	public static final int TITLE_SORT_DOWN = 2;// 降序
	int selectSortIndex = -1;// 当前选中排序的列编号
	int selectStatics = 0;

	public int getSelectSortIndex() {
		return selectSortIndex;
	}

	public void setSelectSortIndex(int selectSortIndex) {
		this.selectSortIndex = selectSortIndex;
	}

	public int getSelectStatics() {
		return selectStatics;
	}

	public void setSelectStatics(int selectStatics) {
		this.selectStatics = selectStatics;
	}

	protected void creatChildViews(String[] datas, int itemLayoutId) {

		mViews = new KpiSortRightItemVIew[datas.length];
		for (int i = 0; i < datas.length; i++) {
			try {
				mViews[i] = new KpiSortRightItemVIew(getContext(), itemLayoutId);
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
				if (i == getSelectSortIndex()) {
					mViews[i].setStatics(getSelectStatics());

				} else {
					// mViews[i].setNormal();
					if (i == 0) {
						mViews[i].setNoticeAni();
					} else {
						mViews[i].setNormal();
					}
				}
				mViews[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						clickOnChild(indexOfChild, getSelectSortIndex(),
								getSelectStatics());
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
					return true;
				}

			});

		}
	}

	public void refresh() {
		// TODO Auto-generated method stub
		rechargeData(datas);
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
