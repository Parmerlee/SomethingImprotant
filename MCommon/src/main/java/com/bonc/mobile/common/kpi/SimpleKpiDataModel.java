
package com.bonc.mobile.common.kpi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.NumberUtil;

public class SimpleKpiDataModel extends BaseKpiDataModel {
    public static final String COL_INFO_TIME = "{'COLUMNTYPE':2,'RELATION_KPIVALUE_COLUMN':'OP_TIME','RELATION_KPIVALUE_COLUMN_NAME':'日期'}";

    List<Map<String, String>> header, data;
    List<Map<String, String>> left_header, right_header;

    public void build(JSONObject allJO, String key_header, String key_data) {
        build(allJO, JsonUtil.toList(allJO.optJSONArray(key_header)),
                JsonUtil.toList(allJO.optJSONArray(key_data)));
    }

    public void build(JSONObject allJO, List<Map<String, String>> header,
            List<Map<String, String>> data) {
        super.build(allJO);
        this.header = header;
        this.data = data;
        left_header = new ArrayList<Map<String, String>>();
        left_header.add(header.get(0));
        right_header = new ArrayList<Map<String, String>>();
        for (int i = 1; i < header.size(); i++)
            right_header.add(header.get(i));
    }

    public void build(JSONObject allJO) {
        build(allJO, "header", "data");
    }

    public List<Map<String, String>> getHeader() {
        return header;
    }

    public List<Map<String, String>> getLeftHeader() {
        return left_header;
    }

    public List<Map<String, String>> getRightHeader() {
        return right_header;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void sort(String key,boolean asc){
        DataComparator c=new DataComparator();
        c.key=key;
        c.asc=asc;
        Collections.sort(data,c);
    }
    
    class DataComparator implements Comparator<Map<String, String>>{
        String key;
        boolean asc;
        @Override
        public int compare(Map<String, String> a, Map<String, String> b) {
            double da=NumberUtil.changeToDouble(a.get(key));
            double db=NumberUtil.changeToDouble(b.get(key));
            return (int)Math.signum(asc?da-db:db-da);
        }
    }
}
