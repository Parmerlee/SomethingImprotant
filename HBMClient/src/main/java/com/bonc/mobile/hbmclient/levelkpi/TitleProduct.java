/**
 * TitleProduct
 */
package com.bonc.mobile.hbmclient.levelkpi;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.adapter.levelkpi.DimensionAdapter;
import com.bonc.mobile.hbmclient.visitor.IConfig;

/**
 * @author liweigao 尽量只创建局部变量，因为每一个方法都可能被单独调用
 */
public class TitleProduct extends LinearProduct implements
		LinearBuilder<TitleProduct> {
	private IConfig visitor;
	private DimensionAdapter dimenAdapter;

	/**
	 * @param context
	 */
	public TitleProduct(Context context) {
		super(context);
	}

	public TitleProduct(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.dimenAdapter = new DimensionAdapter(context);
	}

	public void setConfigData(IConfig visitor) {
		this.visitor = visitor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#buildBackground
	 * ()
	 */
	@Override
	public void buildBackground() {
		setBackgroundResource(R.mipmap.icon_title_small_blue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#buildRelationColumn
	 * ()
	 */
	@Override
	public void buildRelationColumn() {
		addView(getDivider());
		TextView tv = getTextView("关联", DimensionAdapter.RELATION_WIDTH_DP);
		addView(tv);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#buildTrendColumn
	 * ()
	 */
	@Override
	public void buildTrendColumn() {
		addView(getDivider());
		addView(getTextView("趋势图", DimensionAdapter.TREND_WIDTH_DP));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#buildKpiColumn()
	 */
	@Override
	public void buildKpiColumn() {
		String[] title = this.visitor.getTitleName();
		String[] title_right = new String[title.length - 1];
		System.arraycopy(title, 1, title_right, 0, title_right.length);
		for (int i = 0; i < title_right.length; i++) {
			addView(getDivider());
			String titleName = title_right[i];
			addView(getTextView(titleName, DimensionAdapter.KPI_COLUMN_WIDTH_DP));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#getProduct()
	 */
	@Override
	public TitleProduct getProduct() {
		// TODO Auto-generated method stub
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#buildKpiNameColumn
	 * ()
	 */
	@Override
	public void buildKpiNameColumn() {
		String[] title = this.visitor.getTitleName();
		addView(getKpiNameTextView(title[0]));
	}

	private TextView getKpiNameTextView(String s) {
		TextView tv = new TextView(getContext());
		tv.setText(s);
		tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		tv.setGravity(Gravity.CENTER);
		tv.setTextAppearance(getContext(), R.style.mainkpi_title_style);
		tv.setSingleLine(true);
		return tv;
	}

	private TextView getTextView(String s, float width) {
		int pxWidth = this.dimenAdapter.fromDPtoPX(width);

		TextView tv = new TextView(getContext());
		tv.setText(s);
		tv.setLayoutParams(new LayoutParams(pxWidth, LayoutParams.FILL_PARENT));
		tv.setGravity(Gravity.CENTER);
		tv.setTextAppearance(getContext(), R.style.mainkpi_title_style);
		tv.setSingleLine(true);
		return tv;
	}

	private View getDivider() {
		View divider = new View(getContext());
		divider.setBackgroundResource(R.color.c3bdbd);
		LayoutParams lp = new LayoutParams(2, 16);
		lp.gravity = Gravity.CENTER;
		divider.setLayoutParams(lp);
		return divider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#
	 * buildMainKpiNameColumn()
	 */
	@Override
	public void buildMainKpiNameColumn() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#buildMainKpiColumn
	 * ()
	 */
	@Override
	public void buildMainKpiColumn() {
		// TODO Auto-generated method stub

	}

}
