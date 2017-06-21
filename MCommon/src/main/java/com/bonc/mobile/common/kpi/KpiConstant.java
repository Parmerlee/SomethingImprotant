
package com.bonc.mobile.common.kpi;

public class KpiConstant {
    public static final String KEY_OP_TIME = "OP_TIME";
    public static final String KEY_KPI_CODE = "KPI_CODE";
    public static final String KEY_KPI_NAME = "KPI_NAME";
    public static final String KEY_AREA_CODE = "AREA_CODE";
    public static final String KEY_AREA_NAME = "AREA_NAME";

    // showColumn
    public static final String KEY_RELATION_KPIVALUE_COLUMN_NAME = "RELATION_KPIVALUE_COLUMN_NAME";
    public static final String KEY_RELATION_KPIVALUE_COLUMN = "RELATION_KPIVALUE_COLUMN";
    public static final String KEY_COLUMN_TYPE = "COLUMNTYPE";
    public static final String KEY_KPIVALUE_RULE = "KPIVALUE_RULE";
    public static final String KEY_KPIVALUE_UNIT = "KPIVALUE_UNIT";

    // kpiInfo
    public static final String KEY_KPI_LEVEL = "KPI_LEVEL";
    public static final String KEY_KPI_CAL_RULE = "KPI_CAL_RULE";
    public static final String KEY_KPI_UNIT = "KPI_UNIT";

    public static final String DATA_TYPE_MONTH = "M";
    public static final String DATA_TYPE_DAY = "D";
    public static final String DEFAULT_CONJUNCTION = "|";
    public static final String NULL_VALUE_INSTEAD = "--";
    public static final String KPI_RULE_STRING = "string";

    public static String getColTitle(String col){
        if("CURDAY_VALUE".equals(col))return "当日值";
        else if("CD_COL".equals(col))return "环比";
        else return col;
    }
}
