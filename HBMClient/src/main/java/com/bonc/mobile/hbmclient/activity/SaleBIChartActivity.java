package com.bonc.mobile.hbmclient.activity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.bonc.mobile.common.activity.BaseDataActivity;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.NumberUtil;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.view.SimpleListView;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.util.WatermarkImage;

public class SaleBIChartActivity extends BaseDataActivity {
	SimpleListView mList;
	double rank_max1, rank_max2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_salebi_chart);
		initView();
		loadData();
	}

	@Override
	protected void initView() {
		super.initView();
		findViewById(R.id.parent).setBackgroundDrawable(
				WatermarkImage.getWatermarkDrawable());

		this.findViewById(R.id.id_button_logo).setVisibility(View.INVISIBLE);
		TextViewUtils.setText(this, R.id.title,
				getIntent().getStringExtra("title"));
		TextViewUtils.setText(this, R.id.rank_legend1, "■ 目标用户数");
		TextViewUtils.setText(this, R.id.rank_legend2, "■ 历史保有");
		mList = (SimpleListView) findViewById(R.id.list);

	}

	@Override
	protected void loadData() {
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("optime", getIntent().getStringExtra("optime"));
		param.put("areaCode", getIntent().getStringExtra("areaCode"));
		int type = getIntent().getIntExtra("type", 0);
		String stype;
		if (type == 2)
			stype = "loss";
		else if (type == 3)
			stype = "low";
		else
			stype = "" + (type + 1);
		new LoadDataTask(this, Constant.BASE_PATH).execute(
				"/bi/marketing/drillTop/" + stype, param);
	}

	@Override
	protected void bindData(JSONObject result) {
		List<Map<String, String>> data = JsonUtil.toList(JsonUtil.optJSONArray(
				result, "data"));
		rank_max1 = NumberUtil.getMaxNumber(DataUtil.extractValArray(data,
				"TARGET_VALUE"));
		rank_max2 = NumberUtil.getMaxNumber(DataUtil.extractValArray(data,
				"CURMONTH_VALUE"));
		mList.setAdapter(new LabelAdapter(this, data,
				R.layout.sale_bi_chart_label, new String[] { "PRODUCT_NAME" },
				new int[] { R.id.text }));
	}

	class LabelAdapter extends SimpleAdapter {
		public LabelAdapter(Context context, List<Map<String, String>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final Map<String, String> item = (Map<String, String>) getItem(position);
			View v = super.getView(position, convertView, parent);
			ProgressBar rank_bar1 = (ProgressBar) v
					.findViewById(R.id.rank_bar1);
			ProgressBar rank_bar2 = (ProgressBar) v
					.findViewById(R.id.rank_bar2);
			rank_bar1.setMax((int) rank_max1);
			rank_bar1.setProgress(NumberUtil.changeToInt(item
					.get("TARGET_VALUE")));
			rank_bar2.setMax((int) rank_max2);
			rank_bar2.setProgress(NumberUtil.changeToInt(item
					.get("CURMONTH_VALUE")));
			TextViewUtils.setText(v, R.id.rank_num1,
					NumberUtil.format(NumberUtil.changeToDouble(item
							.get("TARGET_VALUE"))));
			TextViewUtils.setText(v, R.id.rank_num2, NumberUtil
					.format(NumberUtil.changeToDouble(item
							.get("CURMONTH_VALUE"))));
			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mList.setHighlight(position, Color.LTGRAY);
					loadProdElement(v.findViewById(R.id.rank_bar1),
							item.get("PRODUCT_CODE"));
				}
			});
			return v;
		}
	}

	void loadProdElement(View v, String queryCode) {
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("areaCode", getIntent().getStringExtra("areaCode"));
		param.put("queryCode", queryCode);
		new LoadPEDataTask(this, v).execute("/bi/marketing/productElement",
				param);
	}

	class LoadPEDataTask extends HttpRequestTask {
		View anchor;

		public LoadPEDataTask(Context context, View anchor) {
			super(context, Constant.BASE_PATH);
			this.anchor = anchor;
		}

		@Override
		protected void handleResult(JSONObject result) {
			String s;
			if (result.isNull("data"))
				s = getString(R.string.no_data);
			else {
				s = JsonUtil.optString(result.optJSONObject("data"),
						"PRODUCT_ELEMENT");
				if (TextUtils.isEmpty(s))
					s = getString(R.string.no_data);
			}
			showInfowin(anchor, s);
		}
	}
}
