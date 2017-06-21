/**
 * LKpiRowBuilder
 */
package common.share.lwg.util.builder.mainkpi;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.LinearLayout;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.composite.levelkpi.KpiLeaf;
import com.bonc.mobile.hbmclient.composite.levelkpi.KpiNameLeaf;
import com.bonc.mobile.hbmclient.composite.levelkpi.RelationLeaf;
import com.bonc.mobile.hbmclient.composite.levelkpi.TrendLeaf;
import common.share.lwg.util.mediator.proxy_impl.mainkpi.DataColleague;

/**
 * @author liweigao
 * 
 */
public class LKpiRowBuilder extends LinearLayout implements
		ILinearBuilder<LKpiRowBuilder> {
	private KpiNameLeaf mKpiNameLeaf;
	private RelationLeaf mRelationLeaf;
	private TrendLeaf mTrendLeaf;
	private Hashtable<String, KpiLeaf> kpiLeaves = new Hashtable<String, KpiLeaf>();

	/**
	 * @param context
	 */
	public LKpiRowBuilder(Context context) {
		super(context);
		initialProduct();
	}

	private void initialProduct() {
		setOrientation(LinearLayout.HORIZONTAL);
		android.widget.AbsListView.LayoutParams lp = new android.widget.AbsListView.LayoutParams(
				android.widget.AbsListView.LayoutParams.FILL_PARENT,
				(int) getContext().getResources().getDimension(
						R.dimen.level_row_height));
		setLayoutParams(lp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see common.share.lwg.util.builder.mainkpi.ILinearBuilder#getProduct()
	 */
	@Override
	public LKpiRowBuilder getProduct() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * common.share.lwg.util.builder.mainkpi.ILinearBuilder#buildColumnType(
	 * java.lang.Object[])
	 */
	@Override
	public void buildColumnType(Object... obj) {
		Map<String, String> item = (Map<String, String>) obj[0];
		String key = item.get("RELATION_KPIVALUE_COLUMN");
		KpiLeaf leaf = new KpiLeaf(getContext(), key);
		leaf.constructView();
		this.kpiLeaves.put(key, leaf);
		addView(leaf.getView());
		leaf.iteratorSetRelationKpiStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * common.share.lwg.util.builder.mainkpi.ILinearBuilder#buildColumnType0
	 * (java.lang.Object[])
	 */
	@Override
	public void buildColumnType0(Object... obj) {
		this.mRelationLeaf = new RelationLeaf(getContext());
		this.mRelationLeaf.constructView();
		addView(this.mRelationLeaf.getView());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * common.share.lwg.util.builder.mainkpi.ILinearBuilder#buildColumnType1
	 * (java.lang.Object[])
	 */
	@Override
	public void buildColumnType1(Object... obj) {
		this.mTrendLeaf = new TrendLeaf(getContext());
		this.mTrendLeaf.constructView();
		addView(this.mTrendLeaf.getView());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * common.share.lwg.util.builder.mainkpi.ILinearBuilder#buildColumnType2
	 * (java.lang.Object[])
	 */
	@Override
	public void buildColumnType2(Object... obj) {
		this.mKpiNameLeaf = new KpiNameLeaf(getContext());
		this.mKpiNameLeaf.constructView();
		addView(this.mKpiNameLeaf.getView());

		this.mKpiNameLeaf.iteratorSetRelationKpiStyle();
	}

	public void setColumnType2Args(Object... args) {
		if (this.mKpiNameLeaf != null) {
			String kpiCode = (String) args[0];
			DataColleague dataColleague = (DataColleague) args[1];
			Map<String, String> kpiConfig = dataColleague.getKpiConfigInfo()
					.get(kpiCode);
			if (kpiConfig == null) {
				kpiConfig = new HashMap<String, String>();
			}

			String kpiName = kpiConfig.get("KPI_NAME");
			String kpiLevel = kpiConfig.get("KPI_LEVEL");

			if (kpiName == null) {
				kpiName = "--";
			}
			mKpiNameLeaf.setText(kpiName);
			if ("1".equals(kpiLevel)) {
				this.mKpiNameLeaf.setFirstLevelStyle();
			} else {
				this.mKpiNameLeaf.setSecondLevelStyle();
			}
			String menuCode = dataColleague.getMenuCode();
			String optime = dataColleague.getOptime();
			String areaCode = dataColleague.getAreaCode();
			String dateType = dataColleague.getDateType();
			mKpiNameLeaf.setOnClickListener(menuCode, optime, areaCode,
					kpiCode, dateType);
			String kpiDefine = kpiConfig.get("KPI_DEFINE");
			if (kpiDefine != null) {
				this.mKpiNameLeaf.setOnLongClickListener(kpiDefine);
			}
			mKpiNameLeaf.setGroupButtonState(kpiCode,dataColleague);
		}
	}

	public void setColumnType1Args(Object... args) {
		if (this.mTrendLeaf != null) {
			String kpiCode = (String) args[0];
			DataColleague dataColleague = (DataColleague) args[1];
			List<Double> trendData = dataColleague.getTrendData().get(kpiCode);

			this.mTrendLeaf.setTrendData(trendData);

			String menuCode = dataColleague.getMenuCode();
			String optime = dataColleague.getOptime();
			String areaCode = dataColleague.getAreaCode();
			String dateType = dataColleague.getDateType();
			this.mTrendLeaf.setOnClickListener(menuCode, optime, areaCode,
					kpiCode, dateType);
		}
	}

	public void setColumnType0Args(Object... args) {
		if (this.mRelationLeaf != null) {
			String kpiCode = (String) args[0];
			DataColleague dataColleague = (DataColleague) args[1];
			Map<String, String> kpiConfig = dataColleague.getKpiConfigInfo()
					.get(kpiCode);
			if (kpiConfig == null) {
				kpiConfig = new HashMap<String, String>();
			}
			String relationTag = kpiConfig.get("RELATION_TAG");

			if ("1".equals(relationTag)) {
				String dateType = dataColleague.getDateType();
				String areaCode = dataColleague.getAreaCode();
				String showDate = dataColleague.getOptime();
				String menuCode = dataColleague.getMenuCode();
				this.mRelationLeaf.setImageResource(kpiCode, dateType,
						showDate, areaCode, menuCode);
			} else {
				this.mRelationLeaf.removeImageResource();
			}
		}
	}

	public void setColumnTypeArgs(Object... args) {
		String kpiCode = (String) args[0];
		DataColleague dataColleague = (DataColleague) args[1];
		Map<String, String> column = (Map<String, String>) args[2];
		String key = column.get("RELATION_KPIVALUE_COLUMN");
		KpiLeaf leaf = this.kpiLeaves.get(key);
		if (leaf != null) {
			Map<String, String> kpiData = dataColleague.getKpiData().get(
					kpiCode);
			if (kpiData == null) {
				kpiData = new HashMap<String, String>();
			}
			Map<String, String> kpiConfig = dataColleague.getKpiConfigInfo()
					.get(kpiCode);
			if (kpiConfig == null) {
				kpiConfig = new HashMap<String, String>();
			}

			String kpiRule = column.get("KPIVALUE_RULE");
			String kpiUnit = column.get("KPIVALUE_UNIT");
			if ("-1".equals(kpiRule)) {
				kpiRule = kpiConfig.get("KPI_CAL_RULE");
				kpiUnit = kpiConfig.get("KPI_UNIT");
			} else {

			}
			String key_kpiRule = key + "_rule";
			String key_kpiUnit = key + "_unit";
			kpiData.put(key_kpiRule, kpiRule);
			kpiData.put(key_kpiUnit, kpiUnit);
			leaf.setData(kpiData);

			String menuCode = dataColleague.getMenuCode();
			String optime = dataColleague.getOptime();
			String areaCode = dataColleague.getAreaCode();
			String dateType = dataColleague.getDateType();
			leaf.setOnClickListener(menuCode, optime, areaCode, kpiCode,
					dateType);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * common.share.lwg.util.builder.mainkpi.ILinearBuilder#buildAppearance(
	 * java.lang.Object[])
	 */
	@Override
	public void buildAppearance(Object... obj) {
		Map<String, String> kpiConfig = (Map<String, String>) obj[0];
		if (kpiConfig == null) {
			kpiConfig = new HashMap<String, String>();
		}
		String kpiLevel = kpiConfig.get("KPI_LEVEL");
		if ("1".equals(kpiLevel)) {
			setBackgroundResource(R.mipmap.rectangle_light_blue_horizontal_long);
		} else {
			setBackgroundResource(R.mipmap.rectangle_light_blue_horizontal_long);
		}
	}
}
