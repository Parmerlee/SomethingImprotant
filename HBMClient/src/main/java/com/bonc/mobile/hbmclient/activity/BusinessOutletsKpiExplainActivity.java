/**
 * BusinessOutletsKpiExplainActivity
 */
package com.bonc.mobile.hbmclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.state.business_outlets.UserPermissionVarify;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.adapter.BusinessOutletsKpiExplainAdapter;

/**
 * @author liweigao
 *
 */
public class BusinessOutletsKpiExplainActivity extends BaseActivity {
	private ExpandableListView listview;
	private UserPermissionVarify varify;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.activity.BaseActivity#onCreate(android.os.Bundle
	 * )
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business_outlets_kpi_explain_activity);
		findViewById(R.id.root).setBackgroundDrawable(
				WatermarkImage.getWatermarkDrawable());
		TextView title = (TextView) this.findViewById(R.id.titleName);
		title.setText("指标口径");
		listview = (ExpandableListView) this.findViewById(R.id.listview);
		View emptyView = this.findViewById(R.id.emptyView);
		listview.setEmptyView(emptyView);

		this.varify = new UserPermissionVarify(this) {

			@Override
			public void validateUser(JSONObject jo) {
				try {
					JSONArray ja = jo.optJSONArray("data");
					List<Map<String, String>> data = JsonUtil.toDataList(ja);
					if (data == null) {
						data = new ArrayList<Map<String, String>>();
					}
					listview.setAdapter(new BusinessOutletsKpiExplainAdapter(
							data));
				} catch (Exception e) {
					Toast.makeText(BusinessOutletsKpiExplainActivity.this,
							"数据加载异常", Toast.LENGTH_SHORT).show();
				}
			}
		};
		this.varify.questData(ActionConstant.GET_BUSINESS_OUTLETS_KPI_EXPLAIN,
				new HashMap<String, String>());
	}

}
