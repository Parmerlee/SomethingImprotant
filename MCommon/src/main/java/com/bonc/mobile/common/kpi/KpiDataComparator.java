
package com.bonc.mobile.common.kpi;

import java.util.Comparator;
import java.util.Map;

import com.bonc.mobile.common.util.NumberUtil;

public class KpiDataComparator implements Comparator<Map<String, String>> {
    String key;
    int direction;

    public KpiDataComparator(String key, int direction) {
        this.key = key;
        this.direction = direction;
    }

    @Override
    public int compare(Map<String, String> lhs, Map<String, String> rhs) {
        double d1 = NumberUtil.changeToDouble(lhs.get(key));
        double d2 = NumberUtil.changeToDouble(rhs.get(key));
        if (d1 == d2)
            return 0;
        else if (direction == 1)
            return d1 < d2 ? -1 : 1;
        else
            return d1 < d2 ? 1 : -1;
    }

}
