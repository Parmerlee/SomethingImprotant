package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.NumberUtil;

/** 右侧列表的item */
public class AutoFitView extends LinearLayout {

	protected View[] mViews;
	private LayoutInflater mInflater;
	Context context;
	float minWidth = 0;
	int minItemW = 16;
	int itemW = 0;

	int itemH = 0;
	int childCount = 0;

	public AutoFitView(Context context, int itemLayoutId, int childCount,
			float minWidth, boolean isFill) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		setOrientation(HORIZONTAL);
		minItemW = NumberUtil.DpToPx(context, minItemW);
		this.minWidth = minWidth;
		this.mInflater = LayoutInflater.from(context);
		removeAllViews();
		this.childCount = childCount;
		calculateWH(childCount);

		creatChildViews(itemLayoutId);
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

		itemW = 110;

		if (itemW * childCount < minWidth) {
			itemW = (int) Math.ceil(minWidth / childCount);
		}

		itemH = (int) context.getResources().getDimension(
				R.dimen.zhl_item_height);
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
	 * 创建子视图
	 *
	 * @param itemLayoutId
	 */
	protected void creatChildViews(int itemLayoutId) {

		mViews = new View[childCount];
		for (int i = 0; i < mViews.length; i++) {
			try {
				mViews[i] = mInflater.inflate(itemLayoutId, null);
				addView(mViews[i]);
				LayoutParams lp = (LayoutParams) mViews[i]
						.getLayoutParams();
				if (i == 0 || i == 3) {
					lp.width = itemW * 7 / 5;
				} else {
					lp.width = itemW;
				}
				lp.height = itemH;
				// if(i%2==0)mViews[i].setBackgroundColor(0xffff0000);
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public View getChildViewAt(int childId) {
		return mViews[childId];
	}

	public void setChildViewW(int childId, int changeW) {
		LayoutParams lp = (LayoutParams) mViews[childId]
				.getLayoutParams();

		lp.width = changeW;

	}

	public void setChildViewH(int childId, int changeH) {
		LayoutParams lp = (LayoutParams) mViews[childId]
				.getLayoutParams();

		lp.height = changeH;

	}

	public void setChildViewWH(int childId, int changeW, int changeH) {
		LayoutParams lp = (LayoutParams) mViews[childId]
				.getLayoutParams();
		lp.width = changeW;
		lp.height = changeH;

	}
}
