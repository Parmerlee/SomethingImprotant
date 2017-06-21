
package com.bonc.mobile.common.chart;

import java.util.List;
import java.util.Map;

import com.bonc.mobile.common.util.NumberUtil;

/**
 * @author sunwei
 */
public class SimpleChartData {
    public double[] seriesData, seriesData2;
    public String[] cats;
    public List<double[]> seriesDataX;

    public static DataConverter Div10KConverter = new DataConverter() {
        @Override
        public double convert(double v) {
            return v / 10000.0;
        }
    };

    public static DataConverter Div100MConverter = new DataConverter() {
        @Override
        public double convert(double v) {
            return v / 100000000.0;
        }
    };

    public static DataConverter PercentConverter = new DataConverter() {
        @Override
        public double convert(double v) {
            return Double.valueOf(String.format("%.1f", v * 100));
        }
    };

    public static SimpleChartData build(List<Map<String, String>> list, String catCol,
            String valCol, DataConverter converter) {
        SimpleChartData data = new SimpleChartData();
        int len = list.size();
        data.seriesData = new double[len];
        data.cats = new String[len];
        for (int i = 0; i < len; i++) {
            Map<String, String> o = list.get(i);
            data.cats[i] = o.get(catCol);
            double v = NumberUtil.changeToDouble(o.get(valCol));
            if (converter != null)
                data.seriesData[i] = converter.convert(v);
            else
                data.seriesData[i] = v;
        }
        return data;
    }

    public static SimpleChartData build(double[] array, DataConverter converter) {
        return build(array, array, converter);
    }

    public static SimpleChartData build(double[] array1, double[] array2, DataConverter converter) {
        if(converter==null)converter=getConverter(array1);
        SimpleChartData data = new SimpleChartData();
        int len = Math.min(array1.length, array2.length);
        data.seriesData = new double[len];
        data.seriesData2 = new double[len];
        for (int i = 0; i < len; i++) {
            if (converter != null) {
                data.seriesData[i] = converter.convert(array1[i]);
                data.seriesData2[i] = converter.convert(array2[i]);
            } else {
                data.seriesData[i] = array1[i];
                data.seriesData2[i] = array2[i];
            }
        }
        return data;
    }

    public static DataConverter getConverter(double scale) {
        if (scale == 100000000)
            return Div100MConverter;
        else if (scale == 10000)
            return Div10KConverter;
        return null;
    }

    public static DataConverter getConverter(double[] array) {
        return getConverter(NumberUtil.getScale(NumberUtil.getAverage(array)));
    }
}
