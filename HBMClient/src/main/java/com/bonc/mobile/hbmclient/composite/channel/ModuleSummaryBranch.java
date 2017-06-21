/**
 * ModuleSummaryBranch
 */
package com.bonc.mobile.hbmclient.composite.channel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.ChannelAnalyzeActivity;
import com.bonc.mobile.hbmclient.activity.ChannelIncomeAreaActivity;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.NumberUtil;
import com.bonc.mobile.hbmclient.view.KeyValueView;

/**
 * @author liweigao
 * 
 */
public class ModuleSummaryBranch extends AChannelBranch {
	private KeyValueView summary_profit;
	private KeyValueView summary_profitRate;
	private KeyValueView summary_income;
	private KeyValueView summary_cost;
	private KeyValueView income_row1;
	private KeyValueView income_row2;
	private KeyValueView income_row3;
	private KeyValueView cost_row1;
	private KeyValueView cost_row2;
	private KeyValueView cost_row3;
	private TextView summaryName;
	private TextView income_row4;
	private TextView cost_row4;

	public ModuleSummaryBranch() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.channel.AChannelBranch#dispatchView
	 * (android.view.View)
	 */
	@Override
	public void dispatchView(View view) {
		View summaryView = view.findViewById(R.id.id_summary);
		this.summary_profit = (KeyValueView) summaryView
				.findViewById(R.id.id_summary_profit);
		this.summary_profitRate = (KeyValueView) summaryView
				.findViewById(R.id.id_summary_profitRate);
		View part_income = summaryView.findViewById(R.id.id_summary_income);
		View part_cost = summaryView.findViewById(R.id.id_summary_cost);
		this.summary_income = (KeyValueView) part_income
				.findViewById(R.id.id_column1);
		this.income_row1 = (KeyValueView) part_income
				.findViewById(R.id.id_row1);
		this.income_row2 = (KeyValueView) part_income
				.findViewById(R.id.id_row2);
		this.income_row3 = (KeyValueView) part_income
				.findViewById(R.id.id_row3);
		this.income_row4 = (TextView) part_income.findViewById(R.id.id_row4);

		this.summary_cost = (KeyValueView) part_cost
				.findViewById(R.id.id_column1);
		this.cost_row1 = (KeyValueView) part_cost.findViewById(R.id.id_row1);
		this.cost_row2 = (KeyValueView) part_cost.findViewById(R.id.id_row2);
		this.cost_row3 = (KeyValueView) part_cost.findViewById(R.id.id_row3);
		this.cost_row4 = (TextView) part_cost.findViewById(R.id.id_row4);

		this.summaryName = (TextView) summaryView
				.findViewById(R.id.id_summaryName);

		setColor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.channel.AChannelBranch#setData(java
	 * .lang.Object)
	 */
	@Override
	public void setData(Object data) {
		JSONObject jo_data = (JSONObject) data;
		JSONObject jo_simple = jo_data.optJSONObject("data");
		if (jo_simple == null) {
			setNameDefault();
		} else {
			String summary_profit = jo_simple.optString("profit");
			setValue(this.summary_profit, "收益(元)", summary_profit);
			String summary_profitRate = jo_simple.optString("valuen23");
			setValue(this.summary_profitRate, "利润率(%)", summary_profitRate);

			String summary_income = jo_simple.optString("income");
			setValue(this.summary_income, "收入(元)", summary_income);
			JSONObject jo_incomeRank = jo_data.optJSONObject("benfitRank");
			KeyValueView[] incomeRankView = { this.income_row1,
					this.income_row2, this.income_row3 };
			try {
				Map<String, Map<String, String>> benfitRank = JsonUtil
						.toMap2(jo_incomeRank);
				for (int i = 0; i < incomeRankView.length; i++) {
					String key = String.valueOf(i + 1);
					try {
						incomeRankView[i].setTag(benfitRank.get(key)
								.get("code"));
						String income_row_name = benfitRank.get(key)
								.get("name");
						String income_row_value = benfitRank.get(key).get(
								"value");
						setValue(incomeRankView[i], income_row_name,
								income_row_value);
					} catch (Exception e) {
						setValue(incomeRankView[i], "--", "--");
					}
				}
			} catch (Exception e) {
				for (int i = 0; i < incomeRankView.length; i++) {
					setValue(incomeRankView[i], "--", "--");
				}
			}

			String summary_cost = jo_simple.optString("cost");
			setValue(this.summary_cost, "成本(元)", summary_cost);

			JSONObject jo_costRank = jo_data.optJSONObject("costRank");
			KeyValueView[] costRankView = { this.cost_row1, this.cost_row2,
					this.cost_row3 };
			try {
				Map<String, Map<String, String>> costRank = JsonUtil
						.toMap2(jo_costRank);
				for (int i = 0; i < costRankView.length; i++) {
					String key = String.valueOf(i + 1);
					try {
						costRankView[i].setTag(costRank.get(key).get("code"));
						String cost_row_name = costRank.get(key).get("name");
						String cost_row_value = costRank.get(key).get("value");
						setValue(costRankView[i], cost_row_name, cost_row_value);
					} catch (Exception e) {
						setValue(costRankView[i], "--", "--");
					}
				}
			} catch (Exception e) {
				for (int i = 0; i < costRankView.length; i++) {
					setValue(costRankView[i], "--", "--");
				}
			}

			/*
			 * Map<String, Map<String, String>> benfitRank =
			 * JsonUtil.toMap2(jo_data .optJSONObject("benfitRank"));
			 * 
			 * String income_row1_name = benfitRank.get("1").get("name"); String
			 * income_row1_value = benfitRank.get("1").get("value");
			 * setValue(this.income_row1, income_row1_name, income_row1_value);
			 * String income_row2_name = benfitRank.get("2").get("name"); String
			 * income_row2_value = benfitRank.get("2").get("value");
			 * setValue(this.income_row2, income_row2_name, income_row2_value);
			 * String income_row3_name = benfitRank.get("3").get("name"); String
			 * income_row3_value = benfitRank.get("3").get("value");
			 * setValue(this.income_row3, income_row3_name, income_row3_value);
			 * 
			 * String summary_cost = jo_simple.optString("cost");
			 * setValue(this.summary_cost, "成本", summary_cost); Map<String,
			 * Map<String, String>> costRank = JsonUtil.toMap2(jo_data
			 * .optJSONObject("costRank")); String cost_row1_name =
			 * costRank.get("1").get("name"); String cost_row1_value =
			 * costRank.get("1").get("value"); setValue(this.cost_row1,
			 * cost_row1_name, cost_row1_value); String cost_row2_name =
			 * costRank.get("2").get("name"); String cost_row2_value =
			 * costRank.get("2").get("value"); setValue(this.cost_row2,
			 * cost_row2_name, cost_row2_value); String cost_row3_name =
			 * costRank.get("3").get("name"); String cost_row3_value =
			 * costRank.get("3").get("value"); setValue(this.cost_row3,
			 * cost_row3_name, cost_row3_value);
			 */
		}
	}

	private void setNameDefault() {
		setValue(this.summary_profit, "收益", "--");
		setValue(this.summary_profitRate, "利润率", "--");
		setValue(this.summary_income, "收入", "--");
		setValue(this.summary_cost, "成本", "--");
		setValue(this.income_row1, "--", "--");
		setValue(this.income_row2, "--", "--");
		setValue(this.income_row3, "--", "--");
		setValue(this.cost_row1, "--", "--");
		setValue(this.cost_row2, "--", "--");
		setValue(this.cost_row3, "--", "--");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.channel.AChannelBranch#reset()
	 */
	@Override
	public void reset() {
		setNameDefault();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.channel.AChannelBranch#setOutEnvironment
	 * (java.lang.Object)
	 */
	@Override
	public void setOutEnvironment(Object env) {
		final ChannelAnalyzeActivity caa = (ChannelAnalyzeActivity) env;

		this.summaryName.setText(caa.getAreaName());
		this.summary_profit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(),
						ChannelIncomeAreaActivity.class);
				i.putExtra(ChannelIncomeAreaActivity.KPI_CODE, "profit");
				i.putExtra(ChannelIncomeAreaActivity.OPTIME, caa.getOptime());
				i.putExtra(ChannelIncomeAreaActivity.AREA_CODE,
						caa.getAreaCode());
				v.getContext().startActivity(i);
			}
		});

		this.summary_profitRate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(),
						ChannelIncomeAreaActivity.class);
				i.putExtra(ChannelIncomeAreaActivity.KPI_CODE, "valuen23");
				i.putExtra(ChannelIncomeAreaActivity.OPTIME, caa.getOptime());
				i.putExtra(ChannelIncomeAreaActivity.AREA_CODE,
						caa.getAreaCode());
				v.getContext().startActivity(i);
			}
		});

		this.summary_income.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(),
						ChannelIncomeAreaActivity.class);
				i.putExtra(ChannelIncomeAreaActivity.KPI_CODE, "income");
				i.putExtra(ChannelIncomeAreaActivity.OPTIME, caa.getOptime());
				i.putExtra(ChannelIncomeAreaActivity.AREA_CODE,
						caa.getAreaCode());
				v.getContext().startActivity(i);
			}
		});

		this.summary_cost.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(),
						ChannelIncomeAreaActivity.class);
				i.putExtra(ChannelIncomeAreaActivity.KPI_CODE, "cost");
				i.putExtra(ChannelIncomeAreaActivity.OPTIME, caa.getOptime());
				i.putExtra(ChannelIncomeAreaActivity.AREA_CODE,
						caa.getAreaCode());
				v.getContext().startActivity(i);
			}
		});

		KeyValueView[] incomeRankView = { this.income_row1, this.income_row2,
				this.income_row3 };
		for (int i = 0; i < incomeRankView.length; i++) {
			setBRClick(caa, incomeRankView[i]);
		}
		KeyValueView[] costRankView = { this.cost_row1, this.cost_row2,
				this.cost_row3 };
		for (int i = 0; i < costRankView.length; i++) {
			setBRClick(caa, costRankView[i]);
		}

		this.income_row4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				caa.clickIncomeDetails();
			}
		});

		this.cost_row4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				caa.clickCostDetails();
			}
		});
	}

	void setBRClick(final ChannelAnalyzeActivity caa, View v) {
		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String code = (String) v.getTag();
				if (code == null)
					return;
				Intent i = new Intent(v.getContext(),
						ChannelIncomeAreaActivity.class);
				i.putExtra(ChannelIncomeAreaActivity.KPI_CODE, code);
				i.putExtra(ChannelIncomeAreaActivity.OPTIME, caa.getOptime());
				i.putExtra(ChannelIncomeAreaActivity.AREA_CODE,
						caa.getAreaCode());
				v.getContext().startActivity(i);
			}
		});
	}

	private void setColor() {
		this.summary_profit.setKeyColor(R.color.ff646464);
		this.summary_profit.setValueColor(R.color.ffda0a00);
		this.summary_profitRate.setKeyColor(R.color.ff646464);
		this.summary_profitRate.setValueColor(R.color.ff0a7ddd);
		this.summary_income.setKeyColor(R.color.ff646464);
		this.summary_income.setValueColor(R.color.ffda0a00);
		this.summary_cost.setKeyColor(R.color.ff646464);
		this.summary_cost.setValueColor(R.color.ff0a7ddd);
		this.income_row1.setKeyColor(R.color.ff363636);
		this.income_row1.setValueColor(R.color.ffda0a00);
		this.income_row2.setKeyColor(R.color.ff363636);
		this.income_row2.setValueColor(R.color.ffda0a00);
		this.income_row3.setKeyColor(R.color.ff363636);
		this.income_row3.setValueColor(R.color.ffda0a00);
		this.cost_row1.setKeyColor(R.color.ff363636);
		this.cost_row1.setValueColor(R.color.ff0a7ddd);
		this.cost_row2.setKeyColor(R.color.ff363636);
		this.cost_row2.setValueColor(R.color.ff0a7ddd);
		this.cost_row3.setKeyColor(R.color.ff363636);
		this.cost_row3.setValueColor(R.color.ff0a7ddd);
	}

	private void setValue(KeyValueView v, String name, String value) {
		name += ":";
		v.setKeyText(name);
		if (value == null || "".equals(value) || "null".equals(value)) {
			value = "--";
		} else if (!"--".equals(value)) {
			value = NumberFormat.getNumberInstance().format(
					NumberUtil.changeToDouble(value));
		}
		if (name.startsWith("利润率")) {
			try {
				value = new DecimalFormat("0.00").format(Double
						.parseDouble(value) * 100);
			} catch (Exception e) {

			}
		} else if (name.startsWith("收益") || name.startsWith("收入")
				|| name.startsWith("成本")) {

		} else {
			value += "元";
		}
		v.setValueText(value);
	}
}
