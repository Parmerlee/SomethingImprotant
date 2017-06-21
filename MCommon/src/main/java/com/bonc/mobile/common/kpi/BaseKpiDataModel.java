
package com.bonc.mobile.common.kpi;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.bonc.mobile.common.util.DataUtil;


public class BaseKpiDataModel {
    protected String menuCode;
    protected String dateType;
    protected String optime;

    public void build(JSONObject allJO) {
        this.dateType = allJO.optString("dateType");
        this.menuCode = allJO.optString("menuCode");
        this.optime = allJO.optString("optime");
    }

    public String getMenuCode() {
        return menuCode;
    }

    public String getDateType() {
        return dateType;
    }

    public String getOpTime() {
        return optime;
    }

    public static void mergeRuleUnit(List<Map<String, String>> colInfo, Map<String, String> kpiInfo) {
        for (Map<String, String> info : colInfo) {
            if ("-1".equals(info.get(KpiConstant.KEY_KPIVALUE_RULE))) {
                info.put(KpiConstant.KEY_KPIVALUE_RULE, kpiInfo.get(KpiConstant.KEY_KPI_CAL_RULE));
                info.put(KpiConstant.KEY_KPIVALUE_UNIT, kpiInfo.get(KpiConstant.KEY_KPI_UNIT));
            }
        }
    }
    
    public static String[] getTimeLabels(List<Map<String, String>> data) {
        List<String> time_lst = DataUtil.extractList(data, KpiConstant.KEY_OP_TIME);
        String[] ret = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            String t = time_lst.get(i);
            ret[i] = t.length() > 6 ? t.substring(6, 8) : t;
        }
        return ret;
    }
}
