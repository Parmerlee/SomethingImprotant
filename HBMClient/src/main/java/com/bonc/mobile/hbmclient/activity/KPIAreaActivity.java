package com.bonc.mobile.hbmclient.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.common.view.FixedHoloDatePickerDialog;
import com.bonc.mobile.common.view.MyDatePickerDialogWithoutDay;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.KpiInfo;
import com.bonc.mobile.hbmclient.data.MenuColumnInfo;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.BarChar4MonArea;
import com.bonc.mobile.hbmclient.view.KpiTitleView;
import com.bonc.mobile.hbmclient.view.ListViewSetting;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialog;
import com.bonc.mobile.hbmclient.view.adapter.KpiAreaLeftAdapter;
import com.bonc.mobile.hbmclient.view.adapter.KpiAreaRightAdapter;

public class KPIAreaActivity extends BaseActivity {
    // 传递的参数.
    private String areaId; // 用户选择的地区ID 没有则为空
    private String areaName; // 地区名称.
    private String opTime; // 操作日期.
    private String dataType; // 数据类型.
    private String kpiCode; // 指标编码
    private String kpiName; // 指标名称.
    private String complexKey; // 复合维度.
    private String thirdDimValueId;// 用户选择的第三维度的id
    private String thirdDimValueName;// 用户选择的第三维度名称
    private List<Map<String, String>> menuThirdDimValueList;// 第三维度维值信息.
    private Map<String, String> thirdDimToComplexDim;
    private KpiInfo kpiInfo;// 指标信息.
    private Map<String, MenuColumnInfo> colInfoMap; // 菜单列和列的基本信息.
    private String menuCode; // 菜单id
    private String thirdDimeName;// 第三名称.
    private List<Map<String, String>> allColumnList; // 所有列.
    private Map<String, String> menuAddInfo; // 菜单附加信息.
    //
    Handler handler = new Handler();

    // 布局控件
    private TextView date_select_new2, brand_select_new2;// 填写日期,品牌
    // 品牌数据,上部柱图使用
    private LinearLayout ll_dev_area_bar;
    private Map<String, String> param;
    private String columnString;
    private List<Map<String, String>> areaData; // 地区数据
    private List<Map<String, String>> BarData; // 柱图数据.
    // 标题栏名（只包括右侧滑动部分的指标名称）
    private String[] titleName;
    // 左侧列表
    private ListView area_left_listView;
    // 右侧列表
    private ListView area_right_listView;
    // 列表相应的Adapter
    private KpiAreaLeftAdapter leftAdapter;
    private KpiAreaRightAdapter rightAdapter;
    // 相关滑动用组件
    private ListViewSetting listViewSetting;

    private TextView area_title_name;
    private TextView highestView;
    private TextView lowestView;
    private TextView fluctuateestView;
    private TextView barUnitView;

    private String highest = ""; // 最高
    private String lowest = ""; // 最低
    private String fluctuateest = "";// 波动最大

    private String colKeyList[]; // 列名称.
    private DatePickerDialog mDateDia; // 日期选择框.
    private Calendar calendar; // 操作日期.
    private String orgUnit;// 柱图数据基本单位

    private String barUnit;// 柱图数据显示单位
    private double barDiv;// 柱图比例

    // 竖屏显示柱图区域

    private RelativeLayout area_bar;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_NoTitleBar);
        super.onCreate(savedInstanceState);
        LogUtil.debug("ChangeOr", "重新Oncreat");
        setContentView(R.layout.month_dev_area_new);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.parent);
        rl.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());

        area_bar = (RelativeLayout) findViewById(R.id.ll_dev_area_barzzz);
        area_bar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                return;

//                if (BarData == null || BarData.size() < 1) {
//                    Toast.makeText(KPIAreaActivity.this, "没有柱图相关数据",
//                            Toast.LENGTH_LONG).show();
//
//                    return;
//                }
//
//                Intent intent = new Intent(KPIAreaActivity.this,
//                        BarLandActivity.class);
//                intent.putExtra("BarData", (Serializable) BarData);
//                intent.putExtra("unit", barUnit);
//                intent.putExtra("div", barDiv);
//                startActivity(intent);

            }
        });
        Intent intent = getIntent();
        areaId = intent.getStringExtra("areaId");
        areaName = intent.getStringExtra("areaName");
        opTime = intent.getStringExtra("opTime");
        dataType = intent.getStringExtra("dataType");
        kpiCode = intent.getStringExtra("kpiCode");

        kpiName = intent.getStringExtra("kpiName");
        complexKey = intent.getStringExtra("complexKey");
        thirdDimValueId = intent.getStringExtra("thirdDimValueId");
        thirdDimValueName = intent.getStringExtra("thirdDimValueName");
        menuCode = intent.getStringExtra("menuCode");
        thirdDimeName = intent.getStringExtra(thirdDimeName);
        kpiInfo = (KpiInfo) intent.getSerializableExtra("kpiInfo");
        colInfoMap = (Map<String, MenuColumnInfo>) intent
                .getSerializableExtra("colInfoMap");
        menuThirdDimValueList = (List<Map<String, String>>) intent
                .getSerializableExtra("menuThirdDimValueList");
        thirdDimToComplexDim = (Map<String, String>) intent
                .getSerializableExtra("thirdDimToComplexDim");
        allColumnList = (List<Map<String, String>>) intent
                .getSerializableExtra("allColumnList");

        if (menuCode == null || "".equals(menuCode)) {
            LogUtil.error(this.getClass().toString(), "传入的菜单为空!");
            finish();
            return;
        }

        menuAddInfo = new BusinessDao().getMenuAddInfo(menuCode);

        initWidget();
        initData();

    }

    public void initWidget() {

        String highdesc = menuAddInfo.get("highestDesc");
        if (highdesc == null) {
            highdesc = "";
        }

        TextView highestTv = (TextView) findViewById(R.id.highest_tv);
        highestTv.setText(highdesc + "最高");

        TextView lowestTv = (TextView) findViewById(R.id.lowest_tv);
        lowestTv.setText(highdesc + "最低");

        area_title_name = (TextView) findViewById(R.id.area_title);
        area_title_name.setText(areaName + ">" + kpiName);

        date_select_new2 = (TextView) findViewById(R.id.date_select_new2);
        date_select_new2.setOnClickListener(this);

        if (opTime != null && !"".equals(opTime)) {

            if (dataType.equals(Constant.DATA_TYPE_DAY)) {
                date_select_new2.setText(DateUtil.oneStringToAntherString(
                        opTime, DateUtil.PATTERN_8, "yyyy/MM/dd"));
                calendar = DateUtil.getCalendar(opTime, DateUtil.PATTERN_8);
            } else {
                date_select_new2.setText(DateUtil.oneStringToAntherString(
                        opTime, DateUtil.PATTERN_6, "yyyy/MM"));
                calendar = DateUtil.getCalendar(opTime, DateUtil.PATTERN_6);
            }
        }

        brand_select_new2 = (TextView) findViewById(R.id.brand_select_new2);
        // brand_select_new2.setOnClickListener(this);

        if (thirdDimValueId == null || "".equals(thirdDimValueId)) { // 不存在第三维度
            brand_select_new2.setVisibility(View.GONE);
        } else {
            brand_select_new2.setVisibility(View.VISIBLE);
            brand_select_new2.setText(thirdDimValueName);
        }

        highestView = (TextView) findViewById(R.id.highest_area);
        lowestView = (TextView) findViewById(R.id.lowest_area);
        fluctuateestView = (TextView) findViewById(R.id.biggest_area);
        barUnitView = (TextView) findViewById(R.id.tv_w_unit);
        area_left_listView = (ListView) findViewById(R.id.mon_area_left_listview);
        View footer = LayoutInflater.from(this).inflate(
                R.layout.list_foot_view, null);
        area_left_listView.addFooterView(footer, null, false);
        area_right_listView = (ListView) findViewById(R.id.mon_area_right_listview);
        View footer2 = LayoutInflater.from(this).inflate(
                R.layout.list_foot_view, null);
        area_right_listView.addFooterView(footer2, null, false);
        listViewSetting = new ListViewSetting();

        titleName = new String[allColumnList.size() + 1];
        colKeyList = new String[allColumnList.size() + 1];
        titleName[0] = getResources().getString(R.string.mon_area_left_title);
        colKeyList[0] = getResources().getString(R.string.mon_area_left_key);
        columnString = "";
        int len = allColumnList.size();
        for (int i = 0; i < len; i++) {

            if (!"".equals(columnString)) {
                columnString += Constant.DEFAULT_CONJUNCTION;
            }
            String col = allColumnList.get(i).get("col")
                    .toLowerCase(Locale.CHINA);
            colKeyList[i + 1] = col;
            columnString += col;
            titleName[i + 1] = allColumnList.get(i).get("colname")
                    .toLowerCase(Locale.CHINA);

        }
        if (allColumnList.size() >= 0 && colInfoMap != null) {
            MenuColumnInfo cinfo = colInfoMap.get(allColumnList.get(0)
                    .get("col").toLowerCase(Locale.CHINA));
            if (cinfo != null) {
                if ("-1".equals(cinfo.getColRule()) && kpiInfo != null) {
                    orgUnit = kpiInfo.getKpiUnit();
                } else {
                    orgUnit = cinfo.getColUnit();
                }

            }

        }

        area_left_listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                if (listViewSetting.leftTouchFlag == 1) {

                    try {
                        int showAreaLevel = Integer.parseInt(menuAddInfo
                                .get("showAreaLevel"));
                        int currAreaLevel = Integer.parseInt(areaData.get(arg2)
                                .get("area_level"));

                        if (currAreaLevel < showAreaLevel) {
                            Intent intent1 = new Intent(KPIAreaActivity.this,
                                    KPIAreaActivity.class);
                            intent1.putExtra("areaId",
                                    areaData.get(arg2).get("area_code"));
                            intent1.putExtra("areaName", areaData.get(arg2)
                                    .get("area_name"));
                            intent1.putExtra("opTime", opTime);
                            intent1.putExtra("dataType", dataType);
                            intent1.putExtra("kpiCode", kpiCode);
                            intent1.putExtra("kpiName", kpiName);
                            intent1.putExtra("menuCode", menuCode);
                            intent1.putExtra("kpiInfo", kpiInfo);
                            intent1.putExtra("allColumnList",
                                    (Serializable) allColumnList);
                            intent1.putExtra("colInfoMap",
                                    (Serializable) colInfoMap);
                            if (complexKey != null && !"".equals(complexKey)) { // 如果事按第三维度展开.
                                // 则
                                intent1.putExtra("complexKey", complexKey);
                            } else {
                                // 攒在第三维度
                                intent1.putExtra("thirdDimValueId",
                                        thirdDimValueId);
                                intent1.putExtra("thirdDimeName", thirdDimeName);
                                intent1.putExtra("thirdDimValueName",
                                        brand_select_new2.getText());
                                intent1.putExtra("menuThirdDimValueList",
                                        (Serializable) menuThirdDimValueList);
                                intent1.putExtra("thirdDimToComplexDim",
                                        (Serializable) thirdDimToComplexDim);
                            }

                            startActivity(intent1);

                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(KPIAreaActivity.this,
                            KPIPeriodActivity.class);
                    intent.putExtra("areaId",
                            areaData.get(arg2).get("area_code"));
                    intent.putExtra("areaName",
                            areaData.get(arg2).get("area_name"));
                    intent.putExtra("opTime", opTime);
                    intent.putExtra("dataType", dataType);
                    intent.putExtra("kpiCode", kpiCode);
                    intent.putExtra("kpiName", kpiName);
                    intent.putExtra("menuCode", menuCode);
                    intent.putExtra("kpiInfo", kpiInfo);
                    intent.putExtra("allColumnList",
                            (Serializable) allColumnList);
                    intent.putExtra("colInfoMap", (Serializable) colInfoMap);
                    if (thirdDimValueId == null) { // 如果事按第三维度展开. 则
                        intent.putExtra("complexKey", complexKey);
                    } else {
                        // 攒在第三维度
                        intent.putExtra("complexKey",
                                thirdDimToComplexDim.get(thirdDimValueId));
                        intent.putExtra("thirdDimValueName",
                                brand_select_new2.getText());
                    }
                    // closePopWindow();
                    startActivity(intent);
                }
            }
        });
        area_right_listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (kpiInfo == null)
                    return;
                if (listViewSetting.leftTouchFlag == 0) {
                    Intent intent = new Intent(KPIAreaActivity.this,
                            KPIPeriodActivity.class);
                    intent.putExtra("areaId",
                            areaData.get(arg2).get("area_code"));
                    intent.putExtra("areaName",
                            areaData.get(arg2).get("area_name"));
                    intent.putExtra("opTime", opTime);
                    intent.putExtra("dataType", dataType);
                    intent.putExtra("kpiCode", kpiCode);
                    intent.putExtra("kpiName", kpiName);
                    intent.putExtra("menuCode", menuCode);
                    intent.putExtra("kpiInfo", kpiInfo);
                    intent.putExtra("allColumnList",
                            (Serializable) allColumnList);
                    intent.putExtra("colInfoMap", (Serializable) colInfoMap);
                    if (thirdDimValueId == null) { // 如果事按第三维度展开. 则
                        intent.putExtra("complexKey", complexKey);
                    } else {
                        // 攒在第三维度
                        intent.putExtra("complexKey",
                                thirdDimToComplexDim.get(thirdDimValueId));
                        intent.putExtra("thirdDimValueName",
                                brand_select_new2.getText());
                    }
                    // closePopWindow();
                    startActivity(intent);
                }
            }
        });
    }

    public void initData() {

        showDialog(LOADING_DIALOG);
        if (!isNetWorkOk()) {
            removeDialog(LOADING_DIALOG);
            return;
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                // 获得数据
                if (!getDataRemote()) {
                    nullDataRemind();
                    return;
                }

                // 开始布局
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (titleName != null && titleName.length != 0) {
                            changeTitle(titleName);

                        }
                        // TODO Auto-generated method stub

                        if (areaData == null || areaData.size() == 0) {
                            nullDataRemind();
                            return;
                        }

                        // 全省最高 设置字号
                        if (highest != null) {
                            if (highest.length() == 5)
                                highestView.setTextSize(18);
                            if (highest.length() == 6)
                                highestView.setTextSize(16);
                            if (highest.length() > 6)
                                highestView.setTextSize(14);
                        }
                        // 全省最低 设置字号
                        if (lowest != null) {
                            if (lowest.length() == 5)
                                lowestView.setTextSize(18);
                            if (lowest.length() == 6)
                                lowestView.setTextSize(16);
                            if (lowest.length() > 6)
                                lowestView.setTextSize(14);
                        }
                        // 波动最大 设置字号
                        /*
                         * if(fluctuateest!=null){ if(fluctuateest.length()==5)
						 * fluctuateestView.setTextSize(18);
						 * if(fluctuateest.length()==6)
						 * fluctuateestView.setTextSize(16);
						 * if(fluctuateest.length()>6)
						 * fluctuateestView.setTextSize(14); }
						 */
                        // 设置上部最高、最低和波动最大的内容
                        highestView.setText(highest);
                        lowestView.setText(lowest);
                        // fluctuateestView.setText(fluctuateest);

                        leftAdapter = new KpiAreaLeftAdapter(
                                KPIAreaActivity.this, areaData, colKeyList);
                        rightAdapter = new KpiAreaRightAdapter(
                                KPIAreaActivity.this, areaData, kpiInfo,
                                colKeyList, colInfoMap);
                        area_left_listView.setAdapter(leftAdapter);
                        area_right_listView.setAdapter(rightAdapter);
                        leftAdapter.notifyDataSetChanged();
                        rightAdapter.notifyDataSetChanged();
                        // 柱图区域视图 by ZZZ 4/8
                        ll_dev_area_bar = (LinearLayout) findViewById(R.id.ll_dev_area_bar);
                        ll_dev_area_bar.removeAllViews();
                        BarChar4MonArea barChart = new BarChar4MonArea(
                                KPIAreaActivity.this, BarData, orgUnit);
                        View view = barChart.getView();
                        ll_dev_area_bar.addView(view);
                        ll_dev_area_bar.invalidate();
                        barUnit = barChart.getUnit();
                        barDiv = barChart.getUnitDiv();
                        barUnitView.setText(barUnit);

                        listViewSetting.setListViewOnTouchAndScrollListener(
                                area_left_listView, area_right_listView);
                        removeDialog(LOADING_DIALOG);
                    }
                });
            }
        }).start();
    }

    /**
     * 远程查询数据.
     */
    public boolean getDataRemote() {

        if (param == null) {
            param = new HashMap<String, String>();
            if (areaId != null && !"".equals(areaId)) {
                param.put("areacode", areaId);
            }
            param.put("cols", columnString);
            param.put("kpicodes", kpiCode);
            param.put("datatype", dataType);
            param.put("datatable",
                    new BusinessDao().getMenuColDataTable(menuCode));
            param.put("isexpand", "1");
        }

        if (thirdDimValueId == null || "".equals(thirdDimValueId)) {
            param.put("dimkey", complexKey);
        } else {
            param.put("dimkey", thirdDimToComplexDim.get(thirdDimValueId));
        }

        param.put("optime", opTime);

        String json_reply = null;
        try {
            String action = menuCode.equals(ChannelMainActivity.MENU_CODE) ? ActionConstant.CUSTOMER_RETENTION_KPI
                    : ActionConstant.KPI_AREA_DATA;
            json_reply = HttpUtil.sendRequest(action, param);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (json_reply == null || "".equals(json_reply)) {
            return false;
        }

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(json_reply);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        if (jsonArray == null || json_reply.length() == 0) {
            return false;
        }

        int len = jsonArray.length();

        if (areaData == null) {
            areaData = new ArrayList<Map<String, String>>();
        } else {
            areaData.clear();
        }

        if (BarData == null) {
            BarData = new ArrayList<Map<String, String>>();
        } else {
            BarData.clear();
        }

        Double high = -99999999999.0, low = 99999999999.0, fluct = 0d;

        // 先这么写 后期考虑改成配置。
        String fluctkey = menuAddInfo.get("fluctuatestColumn");

        if (fluctkey != null) {
            fluctkey = fluctkey.toLowerCase(Locale.CHINA);
        } else {
            fluctkey = "";
        }

        String highestColumn = menuAddInfo.get("highestColumn");

        if (highestColumn != null) {
            highestColumn = highestColumn.toLowerCase(Locale.CHINA);
        } else {
            highestColumn = "";
        }

        // 取数据.
        for (int i = 0; i < len; i++) {
            Map<String, String> dataMap = new HashMap<String, String>();

            String currareacode = jsonArray.optJSONObject(i).optString(
                    "area_code");

            if (currareacode.equals(areaId)) {
                continue;
            }

            String value = jsonArray.optJSONObject(i).optString(colKeyList[1]);
            // 值
            int collen = colKeyList.length;
            for (int m = 0; m < collen; m++) {
                dataMap.put(colKeyList[m], jsonArray.optJSONObject(i)
                        .optString(colKeyList[m]));
            }

            dataMap.put("area_code", currareacode);
            dataMap.put("area_name",
                    jsonArray.optJSONObject(i).optString("area_name"));
            dataMap.put("area_level",
                    jsonArray.optJSONObject(i).optString("area_level"));

            areaData.add(dataMap);

            if (!areaId.equals(currareacode)) {
                Map<String, String> barMap = new HashMap<String, String>();
                barMap.put("title",
                        jsonArray.optJSONObject(i).optString(colKeyList[0])); // 地区名称
                barMap.put("value", value);
                BarData.add(barMap);
            }

            String highvalue = jsonArray.optJSONObject(i).optString(
                    highestColumn);

            try {
                if (Double.parseDouble(highvalue) > high) {
                    high = Double.parseDouble(highvalue);
                    highest = jsonArray.optJSONObject(i).getString("area_name");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (Double.parseDouble(highvalue) < low) {
                    low = Double.parseDouble(highvalue);
                    lowest = jsonArray.optJSONObject(i).getString("area_name");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 环比、同比有时候传过来的值不是double,是‘-’的问题要做判断，否则会造成严重崩溃问题
            // 如果环比值最大，这波动最大

			/*
             * try {
			 * 
			 * String f = jsonArray.optJSONObject(i).getString(fluctkey);
			 * 
			 * if(Math.abs(NumberUtil.changeToDouble(f)) > fluct) {
			 * 
			 * fluctuateest =
			 * jsonArray.optJSONObject(i).getString(colKeyList[0]); } } catch
			 * (Exception e) { e.printStackTrace(); }
			 */

        }

        return true;
    }

    // 变更标题栏内容
    private void changeTitle(String[] namesRight) {
        // TODO Auto-generated method stub
        TextView tv = (TextView) findViewById(R.id.mon_area_title);
        tv.setText(namesRight[0]);
        LinearLayout tl = (LinearLayout) findViewById(R.id.title_lineralayout);
        tl.setBackgroundResource(R.drawable.glay_list_title);
        LinearLayout layRightTitle = (LinearLayout) findViewById(R.id.lineralayout_right_title);
        String str[] = new String[namesRight.length - 1];
        for (int i = 0; i < str.length; i++) {
            str[i] = namesRight[i + 1];
        }

        final KpiTitleView ret = new KpiTitleView(KPIAreaActivity.this,
                R.layout.title_right_sortitem, str, 1, getWindowManager()
                .getDefaultDisplay().getWidth()
                - this.getResources().getDimension(
                R.dimen.zhl_left_column_width));

        layRightTitle.removeAllViews();
        layRightTitle.addView(ret);

        ret.setAllBackgroundByID(R.drawable.glay_list_title);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, ret.getItemH());// (LayoutParams)
        // ret.getLayoutParams();
        ret.setLayoutParams(lp);
        ret.setOnChildClickListener(new KpiTitleView.onChildClickListener() {
            @Override
            public void onClickChild(int childIndex, int selectIndex,
                                     int selectStatic) {
                // Toast.makeText(KPIAreaActivity.this, colKeyList[childIndex],
                // Toast.LENGTH_SHORT).show();

                if (childIndex + 1 >= colKeyList.length) {
                    return;
                }

                int state = KpiTitleView.TITLE_SORT_UP;

                if (childIndex == selectIndex) {
                    if (selectStatic == KpiTitleView.TITLE_SORT_UP) {
                        state = KpiTitleView.TITLE_SORT_DOWN;
                    } else {
                        state = KpiTitleView.TITLE_SORT_UP;
                    }
                }

                double default_max_value = 99999999999999999L;
                double default_min_value = -9999999999999999L;

                String col = colKeyList[childIndex + 1];

                int len = areaData.size();

                if (state == KpiTitleView.TITLE_SORT_UP) {
                    // 升序
                    for (int i = 0; i < len; i++) {

                        for (int k = i + 1; k < len; k++) {

                            double value1 = default_max_value;
                            try {
                                value1 = Double.parseDouble(areaData.get(i)
                                        .get(col));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            double value2 = default_max_value;
                            try {
                                value2 = Double.parseDouble(areaData.get(k)
                                        .get(col));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // 如果小于 则交换.
                            if (value2 < value1) {
                                Map<String, String> tempMap = new HashMap<String, String>();
                                tempMap = areaData.get(i);
                                areaData.set(i, areaData.get(k));
                                areaData.set(k, tempMap);

                            }
                        }
                    }
                } else {
                    // 降序
                    for (int i = 0; i < len; i++) {
                        double value1 = default_min_value;
                        try {
                            value1 = Double.valueOf(areaData.get(i).get(col));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        for (int k = i + 1; k < len; k++) {

                            double value2 = default_min_value;
                            try {
                                value2 = Double.valueOf(areaData.get(k)
                                        .get(col));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // 如果小于 则交换.
                            if (value2 > value1) {
                                Map<String, String> tempMap = new HashMap<String, String>();
                                tempMap = areaData.get(i);
                                areaData.set(i, areaData.get(k));
                                areaData.set(k, tempMap);

                            }
                        }
                    }
                }

                leftAdapter.refresh();
                rightAdapter.refresh();
                ret.setSelectSortIndex(childIndex);// 将当前指标选为被选中项
                ret.setSelectStatics(state);
                ret.refresh();
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.date_select_new2:
                showDatePicker();
                break;
            case R.id.brand_select_new2:
                int tdvl = menuThirdDimValueList.size();
                final String[] thirdDimValueList = new String[tdvl];

                for (int i = 0; i < tdvl; i++) {
                    thirdDimValueList[i] = menuThirdDimValueList.get(i).get(
                            "dimvaluename");
                }

                new AlertDialog.Builder(KPIAreaActivity.this)
                        .setTitle("")
                        .setItems(thirdDimValueList,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int which) {
                                        thirdDimValueId = menuThirdDimValueList
                                                .get(which).get("dimvalueid");
                                        brand_select_new2
                                                .setText(thirdDimValueList[which]);

                                        initData();
                                    }
                                }).create().show();
                break;

        }
    }

    public void showtip(String message) {
        toast(KPIAreaActivity.this, message);
    }

    public void nullDataRemind() {

        // toast(KPIHomeActivity.this, getString(R.string.no_data));
        handler.post(new Runnable() {
            @Override
            public void run() {
                removeDialog(LOADING_DIALOG);
                Toast.makeText(KPIAreaActivity.this,
                        getString(R.string.no_data), Toast.LENGTH_SHORT).show();

				/*
                 * if(highestView!=null) { highestView.setText("");
				 * highestView.invalidate(); }
				 */
                areaData = new ArrayList<Map<String, String>>();
                // colKeyList = new String[]{};
                // kpiInfo = null;
                // colInfoMap = new HashMap<String,MenuColumnInfo>();
                leftAdapter = new KpiAreaLeftAdapter(KPIAreaActivity.this,
                        areaData, colKeyList);
                rightAdapter = new KpiAreaRightAdapter(KPIAreaActivity.this,
                        areaData, kpiInfo, colKeyList, colInfoMap);
                if (area_left_listView != null)
                    area_left_listView.setAdapter(leftAdapter);
                if (area_right_listView != null)
                    area_right_listView.setAdapter(rightAdapter);
                leftAdapter.notifyDataSetChanged();
                rightAdapter.notifyDataSetChanged();
                if (ll_dev_area_bar != null)
                    ll_dev_area_bar.removeAllViews();
                highestView.setText("");
                lowestView.setText("");
                fluctuateestView.setText("");
                barUnitView.setText("");
                /*
                 * if(lowestView!=null)lowestView.setText("");
				 * if(fluctuateestView!=null)fluctuateestView.setText("");
				 */

            }
        });

    }

    /**
     * 显示日期选择.
     */
    public void showDatePicker() {

        if (mDateDia != null && mDateDia.isShowing()) {
            mDateDia.dismiss();
        }

        OnDateSetListener listener = new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                if (dataType.equals(Constant.DATA_TYPE_DAY)) {
                    date_select_new2.setText(DateUtil.formatter(
                            calendar.getTime(), "yyyy/MM/dd"));
                    opTime = DateUtil.formatter(calendar.getTime(),
                            DateUtil.PATTERN_8);

                } else {
                    date_select_new2.setText(DateUtil.formatter(
                            calendar.getTime(), "yyyy/MM"));
                    opTime = DateUtil.formatter(calendar.getTime(),
                            DateUtil.PATTERN_6);
                }

                initData();

            }
        };
        mDateDia = new MyDatePickerDialog(new ContextThemeWrapper(this,
                // android.R.style.Widget_Material_NumberPicker
                android.R.style.Theme_Holo_Light_Dialog), listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        if (!dataType.equals(Constant.DATA_TYPE_DAY)) {
            mDateDia = new MyDatePickerDialogWithoutDay(this, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
        if (Build.VERSION.SDK_INT >= 24) {
            final Context themedContext = new ContextThemeWrapper(this,
                    // android.R.style.Widget_Material_NumberPicker
                    android.R.style.Theme_Holo_Light_Dialog);
            mDateDia = new FixedHoloDatePickerDialog(themedContext,
                    listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            ((FixedHoloDatePickerDialog) mDateDia).setHasNoDay(!dataType.equals(Constant.DATA_TYPE_DAY));
        }

        mDateDia.setTitle("请选择时间");
        mDateDia.show();
    }

	/*
	 * public boolean onKeyDown(int keyCode, KeyEvent event) { // TODO
	 * Auto-generated method stub closePopWindow(); return
	 * super.onKeyDown(keyCode, event); } PopupWindow pp; int popShowCount = 0;
	 *//**
     * 关闭当前显示的pop
     */
	/*
	 * protected void closePopWindow() { // TODO Auto-generated method stub
	 * if(pp!=null&&pp.isShowing()) { popShowCount=0; pp.dismiss();
	 * 
	 * } } protected void showPopWinDow(String leftValue, View view) { // TODO
	 * Auto-generated method stub if(pp!=null) { if(pp.isShowing()) {
	 * pp.dismiss();
	 * 
	 * } } else { pp = new
	 * PopupWindow(getLayoutInflater().inflate(R.layout.popup_window, null),
	 * LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	 * 
	 * } TextView v = (TextView)pp.getContentView().findViewById(R.id.pop_text);
	 * v.setText(leftValue); int screenWidth=
	 * getWindowManager().getDefaultDisplay().getWidth(); int popMaxW = (int)
	 * (screenWidth-(KPIAreaActivity.this.getResources().getDimension(R.dimen.
	 * zhl_left_column_width))); if(leftValue.length()*15>popMaxW) {
	 * pp.setWidth(popMaxW); } pp.showAsDropDown(view, (int)
	 * (KPIAreaActivity.this
	 * .getResources().getDimension(R.dimen.zhl_left_column_width)), (int)
	 * -(KPIAreaActivity
	 * .this.getResources().getDimension(R.dimen.zhl_item_height)));
	 * popShowCount+=1; Handler hd=(new Handler()); hd.postDelayed(new
	 * Runnable(){
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub
	 * popShowCount-=1; if(popShowCount==0) { closePopWindow(); } }}, 3000); }
	 */
}
