package com.bonc.mobile.hbmclient.activity;

import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.net.HttpRequestTask;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.NumberUtil;
import com.bonc.mobile.hbmclient.util.TextViewUtils;
import com.bonc.mobile.hbmclient.util.WatermarkImage;

public class ChannelIncomeAreaActivity extends BaseActivity {
	public final static String OPTIME = "dataTime";
	public final static String AREA_CODE = "areaCode";
	public final static String KPI_CODE = "kpiCode";

	protected String optime, areaCode, attrType;
	View header;
	protected ListView mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel_income_area);
		initBaseData();
		initView();
		loadData();
	}

	protected void initBaseData() {
		Intent intent = getIntent();
		optime = intent.getStringExtra(OPTIME);
		areaCode = intent.getStringExtra(AREA_CODE);
		attrType = intent.getStringExtra(KPI_CODE);
	}

	protected void initView() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.parent);
		ll.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());
		header = findViewById(R.id.header);
		mList = (ListView) findViewById(R.id.list);
	}

	protected void loadData() {
		mList.setAdapter(null);
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		param.put("date", optime);
		param.put("areaCode", areaCode);
		param.put("attr", attrType);
		new LoadDataTask(this).execute("palmChannel_getChannelDetailByAttr.do",
				param);
	}

	protected void bindData(JSONObject result) {
		Map<String, String> m = JsonUtil.toMap(result.optString("header"));
		TextViewUtils.setText(header, R.id.area, m.get("area"));
		TextViewUtils.setText(header, R.id.attr_value, m.get("attrValue"));
		TextViewUtils.setText(header, R.id.rank, m.get("rank"));
		List<Map<String, String>> list = JsonUtil.toList(result
				.optString("data"));
		if (list == null)
			return;
		formatData(list);
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.chi_area_list_item, new String[] { "area",
						"attrValue", "rank" }, new int[] { R.id.area,
						R.id.attr_value, R.id.rank });
		mList.setAdapter(adapter);
	}

	void formatData(List<Map<String, String>> list) {
		for (Map<String, String> m : list) {
			NumberFormat fmt;
			if ("valuen23".equals(m.get("attrTypeCode"))) {
				fmt = NumberFormat.getPercentInstance();
			} else {
				fmt = NumberFormat.getNumberInstance();
			}
			fmt.setMinimumFractionDigits(2);
			m.put("attrValue",
					fmt.format(NumberUtil.changeToDouble(m.get("attrValue"))));
		}
	}

	protected class LoadDataTask extends HttpRequestTask {
		public LoadDataTask(Context context) {
			super(context);
		}

		@Override
		protected void handleResult(JSONObject result) {
			bindData(result);
		}

	}
}
