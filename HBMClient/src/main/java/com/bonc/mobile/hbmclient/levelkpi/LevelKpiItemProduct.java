/**
 * LevelKpiItemProduct
 */
package com.bonc.mobile.hbmclient.levelkpi;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.adapter.levelkpi.IConfigAndDataAdapter;
import com.bonc.mobile.hbmclient.composite.levelkpi.KpiBranch;
import com.bonc.mobile.hbmclient.composite.levelkpi.KpiNameLeaf;
import com.bonc.mobile.hbmclient.composite.levelkpi.RelationLeaf;
import com.bonc.mobile.hbmclient.composite.levelkpi.TrendLeaf;

/**
 * @author liweigao 尽量使用局部变量，因为每个方法都是分离的，可以独立创建所指定的列.如果是全局变量，设置参数时，需要判断此变量是否为空
 */
public class LevelKpiItemProduct extends LinearProduct implements
		LinearBuilder<LevelKpiItemProduct> {
	private IConfigAndDataAdapter dataAdapter;

	private KpiNameLeaf mKpiNameLeaf;
	private RelationLeaf mRelationLeaf;
	private TrendLeaf mTrendLeaf;
	private KpiBranch mKpiBranch;
	private KpiNameLeaf mMainKpiNameLeaf;
	private KpiBranch mMainKpiBranch;

	/**
	 * @param context
	 */
	public LevelKpiItemProduct(Context context) {
		super(context);
		initialProduct();
	}

	public LevelKpiItemProduct(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private void initialProduct() {
		setOrientation(LinearLayout.HORIZONTAL);
		android.widget.AbsListView.LayoutParams lp = new android.widget.AbsListView.LayoutParams(
				android.widget.AbsListView.LayoutParams.FILL_PARENT,
				(int) getContext().getResources().getDimension(
						R.dimen.level_row_height));
		setLayoutParams(lp);
	}

	public void setConfigAndDataAdapter(IConfigAndDataAdapter cada) {
		this.dataAdapter = cada;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#getProduct()
	 */
	@Override
	public LevelKpiItemProduct getProduct() {
		// TODO Auto-generated method stub
		return this;
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
		this.mRelationLeaf = new RelationLeaf(getContext());
		this.mRelationLeaf.constructView();
		addView(this.mRelationLeaf.getView());
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
		this.mTrendLeaf = new TrendLeaf(getContext());
		this.mTrendLeaf.constructView();
		addView(this.mTrendLeaf.getView());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#buildKpiColumn()
	 */
	@Override
	public void buildKpiColumn() {
		String[] keys = this.dataAdapter.getColKey();
		this.mKpiBranch = new KpiBranch(getContext(), keys);
		this.mKpiBranch.iteratorConstructView();
		addView(this.mKpiBranch.iteratorGetView());

		this.mKpiBranch.iteratorSetRelationKpiStyle();
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
		this.mKpiNameLeaf = new KpiNameLeaf(getContext());
		this.mKpiNameLeaf.constructView();
		addView(this.mKpiNameLeaf.getView());

		this.mKpiNameLeaf.iteratorSetRelationKpiStyle();
	}

	/**
	 * @param s
	 * @see KpiNameLeaf#setText(String)
	 */
	public void setText(String s) {
		if (this.mKpiNameLeaf != null) {
			if (s == null) {
				s = "--";
			}
			mKpiNameLeaf.setText(s);
		}
	}

	/**
	 * @param menuCode
	 * @param optime
	 * @param areaCode
	 * @param kpiCode
	 * @param dataType
	 * @see KpiNameLeaf#setOnClickListener(String,
	 *      String, String, String,
	 *      String)
	 */
	public void setOnClickListener(String kpiCode) {
		String menuCode = this.dataAdapter.getMenuCode();
		String optime = this.dataAdapter.getOpTime();
		String areaCode = this.dataAdapter.getAreaCode();
		String dataType = this.dataAdapter.getDataType().getDateFlag();

		if (this.mKpiNameLeaf != null) {
			mKpiNameLeaf.setOnClickListener(menuCode, optime, areaCode,
					kpiCode, dataType);
		}
		if (this.mTrendLeaf != null) {
			this.mTrendLeaf.setOnClickListener(menuCode, optime, areaCode,
					kpiCode, dataType);
		}
		if (this.mKpiBranch != null) {
			this.mKpiBranch.setOnClickListener(menuCode, optime, areaCode,
					kpiCode, dataType);
		}
		if (this.mMainKpiNameLeaf != null) {
			this.mMainKpiNameLeaf.setOnClickListener(menuCode, optime,
					areaCode, kpiCode, dataType);
		}
		if (this.mMainKpiBranch != null) {
			this.mMainKpiBranch.setOnClickListener(menuCode, optime, areaCode,
					kpiCode, dataType);
		}
	}

	public void setLevelStyle(String level) {
		if ("1".equals(level)) {
			// setBackgroundResource(R.color.transparent_blank);
			setBackgroundResource(R.mipmap.rectangle_light_blue_horizontal_long);
			if (this.mKpiNameLeaf != null) {
				this.mKpiNameLeaf.setFirstLevelStyle();
			}
			if (this.mKpiBranch != null) {
				this.mKpiBranch.iteratorSetFirstLevelStyle();
			}

		} else {
			setBackgroundResource(R.color.second_level_white);
			if (this.mKpiNameLeaf != null) {
				this.mKpiNameLeaf.setSecondLevelStyle();
			}
			if (this.mKpiBranch != null) {
				this.mKpiBranch.iteratorSetSecondLevelStyle();
			}
		}
	}

	public void setImageResource(String relationTag, String kpiCode) {
		if (this.mRelationLeaf != null) {
			if ("1".equals(relationTag)) {
				String dataType = this.dataAdapter.getDataType().getDateFlag();
				String areaCode = this.dataAdapter.getAreaCode();
				String showDate = this.dataAdapter.getOpTime();
				String menuCode = this.dataAdapter.getMenuCode();
				this.mRelationLeaf.setImageResource(kpiCode, dataType,
						showDate, areaCode, menuCode);
			} else {
				this.mRelationLeaf.removeImageResource();
			}
		}
	}

	public void setTrendData(List<Double> data) {
		if (this.mTrendLeaf != null) {
			if (data != null) {
				this.mTrendLeaf.setTrendData(data);
			}
		}
	}

	public void setKpiData(Map<String, String> data) {
		if (this.mKpiBranch != null) {
			if (data != null) {
				this.mKpiBranch.iteratorSetData(data);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.builder.levelkpi.LinearBuilder#
	 * buildMainKpiNameColumn()
	 */
	@Override
	public void buildMainKpiNameColumn() {
		this.mMainKpiNameLeaf = new KpiNameLeaf(getContext());
		this.mMainKpiNameLeaf.constructView();
		addView(this.mMainKpiNameLeaf.getView());

		this.mMainKpiNameLeaf.iteratorSetMainKpiStyle();
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
		String[] keys = this.dataAdapter.getColKey();
		this.mMainKpiBranch = new KpiBranch(getContext(), keys);
		this.mMainKpiBranch.iteratorConstructView();
		addView(this.mMainKpiBranch.iteratorGetView());

		this.mMainKpiBranch.iteratorSetMainKpiStyle();
	}

	public void setMainKpiNameText(String s) {
		if (this.mMainKpiNameLeaf != null) {
			if (s == null) {
				s = "--";
			}
			mMainKpiNameLeaf.setText(s);
		}
	}

	public void setMainKpiData(Map<String, String> data) {
		if (this.mMainKpiBranch != null) {
			if (data != null) {
				this.mMainKpiBranch.iteratorSetData(data);
			}
		}
	}

	public void setOnLongClickListener(String kpiCode) {
		if (this.mKpiNameLeaf != null) {
			Map<String, Map<String, String>> kpiInfo = dataAdapter
					.getKpiRuleAndUnit();
			if (kpiInfo != null) {
				String kpiDefine = kpiInfo.get(kpiCode).get("kpi_define");
				if (kpiDefine != null) {
					this.mKpiNameLeaf.setOnLongClickListener(kpiDefine);
				}
			}
		}
		if (this.mMainKpiNameLeaf != null) {
			Map<String, Map<String, String>> kpiInfo = dataAdapter
					.getKpiRuleAndUnit();
			if (kpiInfo != null) {
				String kpiDefine = kpiInfo.get(kpiCode).get("kpi_define");
				if (kpiDefine != null) {
					this.mMainKpiNameLeaf.setOnLongClickListener(kpiDefine);
				}
			}
		}
	}
}
