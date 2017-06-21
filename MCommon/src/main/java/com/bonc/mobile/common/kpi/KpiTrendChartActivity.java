
package com.bonc.mobile.common.kpi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.view.ACBarChartView;
import com.bonc.mobile.common.view.ACLineChartView;
import com.bonc.mobile.common.view.adapter.SimplePagerAdapter;

public abstract class KpiTrendChartActivity extends BaseSubKpiActivity implements
        View.OnClickListener {
    ViewPager pager;
    View p1, p2, p3;
    protected ACLineChartView line_chart;
    ACBarChartView bar_chart;
    SimpleKpiTitleRow list_title;
    ListView pop_listview;
    String chartCol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kpi_trend_chart);
        initView();
        loadData();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_share) {
            FileUtils.shareScreen(this,true);
            return;
        }
        super.onClick(v);
        if (v.getId() == R.id.front_item) {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        } else if (v.getId() == R.id.next_item) {
            pager.setCurrentItem(pager.getCurrentItem() + 1);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        pager = (ViewPager) findViewById(R.id.pager);
        p1 = View.inflate(this, R.layout.kpi_trend_page_1, null);
        p1.findViewById(R.id.next_item).setOnClickListener(this);
        line_chart = (ACLineChartView) p1.findViewById(R.id.line_chart);
        p2 = View.inflate(this, R.layout.kpi_trend_page_2, null);
        p2.findViewById(R.id.next_item).setOnClickListener(this);
        p2.findViewById(R.id.front_item).setOnClickListener(this);
        bar_chart = (ACBarChartView) p2.findViewById(R.id.bar_chart);
        p3 = View.inflate(this, R.layout.kpi_trend_page_3, null);
        p3.findViewById(R.id.front_item).setOnClickListener(this);
        list_title = (SimpleKpiTitleRow) p3.findViewById(R.id.list_title);
        pop_listview = (ListView) p3.findViewById(R.id.list);
    }

    @Override
    protected void loadData() {
        Map<String, String> param = new LinkedHashMap<String, String>();
        param.put("menuCode", menuCode);
        param.put("optime", optime);
        param.put("areaCode", areaCode);
        param.put("queryCode", kpiCode);
        param.put("dateType", dateType);
        String[] a = getQueryAction().split("\\|");
        new LoadDataTask(this).execute(a[0], param, 0);
        new LoadDataTask(this).execute(a[1], param, 1);
    }

    protected void bindLineChartData(JSONObject result) {
        List<Map<String, String>> colInfo = JsonUtil.toList(result.optJSONArray("showColumn"));
        chartCol = colInfo.get(0).get(KpiConstant.KEY_RELATION_KPIVALUE_COLUMN);
        Map<String, String> kpiInfo = JsonUtil.toMap(result.optJSONObject("kpiInfo"));
        List<Map<String, String>> pre = result.isNull("pre") ? new ArrayList<Map<String, String>>()
                : JsonUtil.toList(result.optJSONArray("pre"));
        List<Map<String, String>> current = JsonUtil.toList(result.optJSONArray("current"));
        String[] labels = KpiDataModel.getTimeLabels(current.size() >= pre.size() ? current : pre);
        double[] currValue = DataUtil.extractValArray(current, chartCol);
        double[] lastValue = DataUtil.extractValArray(pre, chartCol);
        if(!TextUtils.isEmpty(kpiInfo.get("EXT2"))){
            line_chart.setYAxis(kpiInfo.get("EXT2"));
        }
        line_chart.setData(labels, currValue, lastValue, kpiInfo.get(KpiConstant.KEY_KPI_UNIT));
        line_chart.setLineTitle(kpiInfo.get(KpiConstant.KEY_KPI_NAME));
        if (monthQuery) {
            line_chart.setLine1Name("本年");
            line_chart.setLine2Name("上年");
        } else {
            line_chart.setLine1Name("本月");
            line_chart.setLine2Name("上月");
        }
    }

    protected boolean bindBarChartData(JSONObject result) {
        Map<String, String> kpiInfo = JsonUtil.toMap(result.optJSONObject("kpiInfo"));
        List<Map<String, String>> base_data = JsonUtil.toList(result.optJSONArray("base_data"));
        if (base_data.size() == 0)
            return false;
        List<Double> v = DataUtil.extractValList(base_data, chartCol);
        List<String> cat = DataUtil.extractList(base_data, KpiConstant.KEY_AREA_NAME);
        bar_chart.setData(v, cat, kpiInfo.get(KpiConstant.KEY_KPI_UNIT));
        bar_chart.setLineTitle(kpiInfo.get(KpiConstant.KEY_KPI_NAME));
        return true;
    }

    protected void bindListData(JSONObject result) {
        Map<String, String> kpiInfo = JsonUtil.toMap(result.optJSONObject("kpiInfo"));
        SimpleKpiDataModel model = new SimpleKpiDataModel();
        model.build(result, "showColumn", "current");
        model.getHeader().add(0, JsonUtil.toMap(SimpleKpiDataModel.COL_INFO_TIME));
        SimpleKpiDataModel.mergeRuleUnit(model.getHeader(), kpiInfo);
        Collections.reverse(model.getData());
        list_title.buildTitleRow(model.getHeader());
        pop_listview.setAdapter(new SimpleKpiDataAdapter(this, model));
    }

    class LoadDataTask extends HttpRequestTask {
        int requestCode;

        public LoadDataTask(Context context) {
            super(context);
        }

        @Override
        protected String doInBackground(Object... params) {
            requestCode = (Integer) params[2];
            return super.doInBackground(params);
        }

        @Override
        protected void handleResult(JSONObject result) {
            if (requestCode == 0) {
                bindLineChartData(result);
                bindListData(result);
            } else {
                boolean b = bindBarChartData(result);
                pager.setAdapter(new SimplePagerAdapter(b ? Arrays.asList(p1, p2, p3) : Arrays
                        .asList(p1, p3)));
            }
        }
    }
}
