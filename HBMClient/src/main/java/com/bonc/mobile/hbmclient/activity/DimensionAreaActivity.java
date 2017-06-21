/**
 * DimensionAreaActivity
 */
package com.bonc.mobile.hbmclient.activity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.view.FixedHoloDatePickerDialog;
import com.bonc.mobile.common.view.MyDatePickerDialogWithoutDay;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.KpiInfo;
import com.bonc.mobile.hbmclient.data.MenuColumnInfo;
import com.bonc.mobile.hbmclient.terminal.Business;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.FileUtils;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.BarChar4MonArea;
import com.bonc.mobile.hbmclient.view.KpiTitleView;
import com.bonc.mobile.hbmclient.view.ListViewSetting;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialog;
import com.bonc.mobile.hbmclient.view.adapter.KpiAreaLeftAdapter;
import com.bonc.mobile.hbmclient.view.adapter.KpiAreaRightAdapter;

/**
 * @author liweigao
 */
public class DimensionAreaActivity extends BaseActivity {
    // 传递的参数.
    private String areaId; // 用户选择的地区ID 没有则为空
    private String areaName; // 地区名称.
    private String opTime; // 操作日期.
    private String dataType; // 数据类型.
    private String kpiCode; // 指标编码
    private String kpiName; // 指标名称.
    private String complexKey; // 复合维度.

    private KpiInfo kpiInfo = new KpiInfo();// 指标信息.
    private Map<String, MenuColumnInfo> colInfoMap = new HashMap<String, MenuColumnInfo>(); // 菜单列和列的基本信息.
    private String menuCode; // 菜单id
    private List<Map<String, String>> allColumnList; // 所有列.
    private Map<String, String> menuAddInfo; // 菜单附加信息.
    //
    Handler handler = new Handler();

    // 布局控件
    private TextView date_select_new2;// 填写日期,品牌
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

    private Button btn_share;
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

    public final static String KEY_KPI_CODE = "key_kpi_code";
    public final static String KEY_OPTIME = "key_optime";
    public final static String KEY_AREA_CODE = "key_area_code";
    public final static String KEY_DIM_KEY = "key_dim_key";
    public final static String KEY_DATA_TYPE = "key_data_type";
    public final static String KEY_MENU_CODE = "key_menu_code";

    private BusinessDao dao = new BusinessDao();

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_NoTitleBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.month_dev_area_new);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.parent);
        rl.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());
        area_bar = (RelativeLayout) findViewById(R.id.ll_dev_area_barzzz);
        area_bar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                if (BarData == null || BarData.size() < 1) {
//                    Toast.makeText(DimensionAreaActivity.this, "没有柱图相关数据",
//                            Toast.LENGTH_LONG).show();
//
//                    return;
//                }
//
//                Intent intent = new Intent(DimensionAreaActivity.this,
//                        BarLandActivity.class);
//                intent.putExtra("BarData", (Serializable) BarData);
//                intent.putExtra("unit", barUnit);
//                intent.putExtra("div", barDiv);
//                startActivity(intent);

            }
        });
        Intent intent = getIntent();
        areaId = intent.getStringExtra(KEY_AREA_CODE);
        opTime = intent.getStringExtra(KEY_OPTIME);
        dataType = intent.getStringExtra(KEY_DATA_TYPE);
        kpiCode = intent.getStringExtra(KEY_KPI_CODE);
        complexKey = intent.getStringExtra(KEY_DIM_KEY);
        menuCode = intent.getStringExtra(KEY_MENU_CODE);

        Map<String, String> kpiinfomap = Business.getKpiInfo(kpiCode);
        LogUtils.logBySys("kpiinfomap:" + kpiinfomap.toString());
        kpiName = kpiinfomap.get("KPI_NAME");
        kpiInfo.setKpiCode(kpiCode);
        kpiInfo.setMenuCode(menuCode);
        kpiInfo.setKpiName(kpiName);
        kpiInfo.setKpiUnit(kpiinfomap.get("KPI_UNIT"));
        kpiInfo.setKpiRule(kpiinfomap.get("KPI_CAL_RULE"));
        kpiInfo.setKpiDefine(kpiinfomap.get("KPI_DEFINE"));

        allColumnList = dao.getMenuAllColumnList(menuCode);
        int len = allColumnList.size();
        for (int i = 0; i < len; i++) {
            String col = allColumnList.get(i).get("col")
                    .toLowerCase(Locale.CHINA);

            MenuColumnInfo colInfo = new MenuColumnInfo();
            colInfo.setColKey(col);
            colInfo.setColName(allColumnList.get(i).get("colname")
                    .toLowerCase(Locale.CHINA));
            colInfo.setColRule(allColumnList.get(i).get("rule")
                    .toLowerCase(Locale.CHINA));
            colInfo.setColUnit(allColumnList.get(i).get("unit")
                    .toLowerCase(Locale.CHINA));
            colInfo.setMenuCode(allColumnList.get(i).get("menucode")
                    .toLowerCase(Locale.CHINA));
            this.colInfoMap.put(col, colInfo);

        }

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
        areaName = Business.getAreaName(areaId);
        area_title_name.setText(areaName + ">" + kpiName.trim());

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

        findViewById(R.id.brand_select_new2).setVisibility(View.GONE);

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
                            Intent intent1 = new Intent(
                                    DimensionAreaActivity.this,
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

                            intent1.putExtra("complexKey", complexKey);

                            startActivity(intent1);

                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(DimensionAreaActivity.this,
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
                    intent.putExtra("complexKey", complexKey);

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
                    Intent intent = new Intent(DimensionAreaActivity.this,
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

                    intent.putExtra("complexKey", complexKey);

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
                        // 设置上部最高、最低和波动最大的内容
                        highestView.setText(highest);
                        lowestView.setText(lowest);

                        leftAdapter = new KpiAreaLeftAdapter(
                                DimensionAreaActivity.this, areaData,
                                colKeyList);
                        rightAdapter = new KpiAreaRightAdapter(
                                DimensionAreaActivity.this, areaData, kpiInfo,
                                colKeyList, colInfoMap);
                        area_left_listView.setAdapter(leftAdapter);
                        area_right_listView.setAdapter(rightAdapter);
                        leftAdapter.notifyDataSetChanged();
                        rightAdapter.notifyDataSetChanged();
                        // 柱图区域视图 by ZZZ 4/8
                        ll_dev_area_bar = (LinearLayout) findViewById(R.id.ll_dev_area_bar);
                        ll_dev_area_bar.removeAllViews();
                        BarChar4MonArea barChart = new BarChar4MonArea(
                                DimensionAreaActivity.this, BarData, orgUnit);
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
            param.put("menuCode", this.menuCode);
            param.put("isexpand", "1");
        }

        param.put("dimkey", complexKey);

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

        final KpiTitleView ret = new KpiTitleView(DimensionAreaActivity.this,
                R.layout.title_right_sortitem, str, 1, getWindowManager()
                .getDefaultDisplay().getWidth()
                - this.getResources().getDimension(
                R.dimen.zhl_left_column_width));

        layRightTitle.removeAllViews();
        layRightTitle.addView(ret);

        ret.setAllBackgroundByID(R.drawable.glay_list_title);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, ret.getItemH());

        ret.setLayoutParams(lp);
        ret.setOnChildClickListener(new KpiTitleView.onChildClickListener() {
            @Override
            public void onClickChild(int childIndex, int selectIndex,
                                     int selectStatic) {

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
        }
    }

    public void showtip(String message) {
        toast(DimensionAreaActivity.this, message);
    }

    public void nullDataRemind() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                removeDialog(LOADING_DIALOG);
                Toast.makeText(DimensionAreaActivity.this,
                        getString(R.string.no_data), Toast.LENGTH_SHORT).show();

                areaData = new ArrayList<Map<String, String>>();

                leftAdapter = new KpiAreaLeftAdapter(
                        DimensionAreaActivity.this, areaData, colKeyList);
                rightAdapter = new KpiAreaRightAdapter(
                        DimensionAreaActivity.this, areaData, kpiInfo,
                        colKeyList, colInfoMap);
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

        mDateDia = new MyDatePickerDialog(this, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        if (!dataType.equals(Constant.DATA_TYPE_DAY)) {
            mDateDia = new MyDatePickerDialogWithoutDay(this, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
        if (Build.VERSION.SDK_INT >= 24) {


            final Context themedContext = new ContextThemeWrapper(this,
                    // android.R.style.Widget_Material_NumberPicker
                    android.R.style.Theme_Holo_Light_Dialog);

            mDateDia = new FixedHoloDatePickerDialog(themedContext, listener, calendar.get(Calendar.YEAR), calendar
                    .get(Calendar.MONTH), calendar
                    .get(Calendar.DAY_OF_MONTH));

            ((FixedHoloDatePickerDialog) mDateDia).setHasNoDay(!dataType.equals(Constant.DATA_TYPE_DAY));

//            String title = DateUtils.formatDateTime(this,
//                    calendar.getTimeInMillis(),
//                    DateUtils.FORMAT_SHOW_DATE
//                            | DateUtils.FORMAT_SHOW_WEEKDAY
//                            | DateUtils.FORMAT_SHOW_YEAR
//                            | DateUtils.FORMAT_ABBREV_MONTH
//                            | DateUtils.FORMAT_ABBREV_WEEKDAY);
//            if (!dataType.equals(Constant.DATA_TYPE_DAY))
//                title = calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月";


        }
        mDateDia.setTitle("请选择时间");
        mDateDia.show();
    }
}
