
package com.bonc.mobile.common.kpi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bonc.mobile.common.util.JsonUtil;
import com.google.gson.reflect.TypeToken;

public class KpiDataModel extends BaseKpiDataModel {
    public static final String COL_INFO_LEFT_KPI = "[{'COLUMNTYPE':2,'RELATION_KPIVALUE_COLUMN_NAME':'指标名称'}]";
    public static final String COL_INFO_LEFT_AREA = "[{'COLUMNTYPE':2,'RELATION_KPIVALUE_COLUMN_NAME':'地区'}]";
    public static final String COL_INFO_LEFT_TIME = "[{'COLUMNTYPE':2,'RELATION_KPIVALUE_COLUMN_NAME':'日期'}]";

    int type = 0, isGroup, areaType;
    String areaCode, kpiCode;
    List<Map<String, String>> columnInfoLeft, columnInfoRight;
    Map<String, Map<String, String>> kpiConfigInfo;
    Map<String, Map<String, String>> kpiData;
    List<Map<String, String>> kpiDataList;
    Map<String, List<String>> trendData;
    List<String> compositeList;
    List<Map<String, Object>> compositeGroup;
    List<Map<String, String>> areaInfo;

    public void build(JSONObject allJO) {
        super.build(allJO);
        buildBaseInfo(allJO);
        buildKpiData(allJO);
        buildTrendData(allJO);
        buildAreaInfo(allJO);
        buildSpecial();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKpiCode() {
        return kpiCode;
    }

    public void setKpiCode(String kpiCode) {
        this.kpiCode = kpiCode;
    }

    public void setColInfoLeft(String json) {
        columnInfoLeft = JsonUtil.toList(json);
    }

    public void syncKpiData(int from_type) {
        if (from_type != 0) {
            compositeList = new ArrayList<String>();
            kpiData = new HashMap<String, Map<String, String>>();
            for (Map<String, String> m : kpiDataList) {
                String key = m.get(from_type == 1 ? KpiConstant.KEY_AREA_CODE
                        : KpiConstant.KEY_OP_TIME);
                compositeList.add(key);
                kpiData.put(key, m);
            }
        } else {

        }
    }

    void buildBaseInfo(JSONObject allJO) {
        columnInfoLeft = JsonUtil.toList(COL_INFO_LEFT_KPI);
        columnInfoRight = JsonUtil.toList(JsonUtil.optJSONArray(allJO, "showColumn"));
        if (type != 0) {
            kpiConfigInfo = new HashMap<String, Map<String, String>>();
            Map<String, String> m = JsonUtil.toMap(allJO.optJSONObject("kpiInfo"));
            kpiConfigInfo.put(m.get(KpiConstant.KEY_KPI_CODE), m);
        } else {
            kpiConfigInfo = JsonUtil.fromJson(allJO.optJSONObject("kpiInfo").toString(),
                    new TypeToken<Map<String, Map<String, String>>>() {
                    }.getType());
        }
    }

    void buildKpiData(JSONObject allJO) {
        if (type != 0)
            buildKpiDataType1(allJO);
        else
            buildKpiDataType0(allJO);
        syncKpiData(type);
    }

    void buildKpiDataType0(JSONObject allJO) {
        Type t = new TypeToken<Map<String, Map<String, String>>>() {
        }.getType();
        kpiData = JsonUtil.fromJson(allJO.optJSONObject("base_data").toString(), t);
        isGroup = allJO.optInt("isGroup");
        if (isGroup == 1) {
            t = new TypeToken<List<Map<String, Object>>>() {
            }.getType();
            compositeGroup = JsonUtil.fromJson(
                    JsonUtil.optJSONArray(allJO, "composite").toString(), t);
        } else {
            t = new TypeToken<List<String>>() {
            }.getType();
            compositeList = JsonUtil.fromJson(JsonUtil.optJSONArray(allJO, "composite").toString(),
                    t);
        }
        filterNulDataKpi();
    }

    void buildKpiDataType1(JSONObject allJO) {
        kpiDataList = JsonUtil.toList(JsonUtil.optJSONArray(allJO, "base_data"));
        isGroup = 0;
    }

    void filterNulDataKpi() {
        if (isGroup == 1) {

        } else {
            Iterator<String> it = compositeList.iterator();
            while (it.hasNext()) {
                String kpiCode = it.next();
                if (!kpiData.containsKey(kpiCode))
                    it.remove();
            }
        }
    }

    void buildTrendData(JSONObject allJO) {
        if (allJO.has("trend_data")) {
            trendData = JsonUtil.fromJson(allJO.optJSONObject("trend_data").toString(),
                    new TypeToken<Map<String, List<String>>>() {
                    }.getType());
        } else
            trendData = Collections.EMPTY_MAP;
    }

    void buildAreaInfo(JSONObject allJO) {
        JSONArray ja_area;
        if (allJO.has("authArea")) {
            areaType = 1;
            ja_area = JsonUtil.optJSONArray(allJO, "authArea");
            this.areaCode = allJO.optString("areaCode");
        } else if (allJO.has("selectList")) {
            areaType = 2;
            ja_area = JsonUtil.optJSONArray(allJO, "selectList");
            this.areaCode = allJO.optString("currentCode");
        } else {
            areaType = 0;
            ja_area = new JSONArray();
            this.areaCode = JsonUtil.optString(allJO, "areaCode");
        }
        areaInfo = JsonUtil.toList(ja_area);
    }

    void buildSpecial() {
        for (Map<String, String> m : kpiConfigInfo.values()) {
            if ("GB".equals(m.get(KpiConstant.KEY_KPI_UNIT))) {
                String rule = m.get(KpiConstant.KEY_KPI_CAL_RULE);
                m.put(KpiConstant.KEY_KPI_CAL_RULE, "GB" + ("HB".equals(areaCode) ? "1" : "2")
                        + rule);
            }
        }
    }

    public int isGroup() {
        return isGroup;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public List<Map<String, String>> getColumnInfoLeft() {
        return columnInfoLeft;
    }

    public List<Map<String, String>> getColumnInfoRight() {
        return columnInfoRight;
    }

    public String getColumnInfo(int location, String key) {
        return getColumnInfoRight().get(location).get(key);
    }

    public String getColumnKey(int location) {
        return getColumnInfo(location, KpiConstant.KEY_RELATION_KPIVALUE_COLUMN);
    }

    public Map<String, Map<String, String>> getKpiConfigInfo() {
        return kpiConfigInfo;
    }

    public Map<String, String> getKpiConfig(String kpiCode) {
        return getKpiConfigInfo().get(kpiCode);
    }

    public String getKpiRule(String kpiCode) {
        return getKpiConfig(kpiCode).get(KpiConstant.KEY_KPI_CAL_RULE);
    }

    public Map<String, Map<String, String>> getKpiData() {
        return kpiData;
    }

    public Map<String, String> getKpiData(String kpiCode) {
        return getKpiData().get(kpiCode);
    }

    public List<Map<String, String>> getKpiDataList() {
        return kpiDataList;
    }

    public Map<String, List<String>> getTrendData() {
        return trendData;
    }

    public List<String> getTrendData(String kpiCode) {
        if (KpiConstant.KPI_RULE_STRING.equals(getKpiRule(kpiCode)))
            return null;
        return getTrendData().get(kpiCode);
    }

    public List<String> getCompositeList() {
        return compositeList;
    }

    public List<Map<String, Object>> getCompositeGroup() {
        return compositeGroup;
    }

    public List<String> getGroupKpiCodeList(int position) {
        Map<String, Object> group = getCompositeGroup().get(position);
        return (List<String>) group.get("KPI_CODE_LIST");
    }

    public List<Map<String, String>> getAreaInfo() {
        return areaInfo;
    }

    public String[] getAreaKey() {
        String[] a = null;
        switch (areaType) {
            case 1:
                a = new String[] {
                        "AREA_NAME", "AREA_CODE"
                };
                break;
            case 2:
                a = new String[] {
                        "NAME", "CODE"
                };
                break;
        }
        return a;
    }
}
