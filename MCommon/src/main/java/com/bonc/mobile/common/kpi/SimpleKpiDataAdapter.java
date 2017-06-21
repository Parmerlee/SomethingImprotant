
package com.bonc.mobile.common.kpi;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bonc.mobile.common.R;

public class SimpleKpiDataAdapter extends BaseAdapter {
    protected Context context;
    protected List<Map<String, String>> header, data;
    protected int type;

    public SimpleKpiDataAdapter(Context context, List<Map<String, String>> header,
            List<Map<String, String>> data) {
        this.context = context;
        this.header = header;
        this.data = data;
    }

    public SimpleKpiDataAdapter(Context context, SimpleKpiDataModel dataModel) {
        this(context, dataModel, 0);
    }

    public SimpleKpiDataAdapter(Context context, SimpleKpiDataModel dataModel, int type) {
        this.context = context;
        this.type = type;
        if (type == 1)
            this.header = dataModel.getLeftHeader();
        else if (type == 2)
            this.header = dataModel.getRightHeader();
        else
            this.header = dataModel.getHeader();
        this.data = dataModel.getData();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleKpiDataRow view;
        if (convertView == null) {
            view = (SimpleKpiDataRow) LayoutInflater.from(context).inflate(
                    R.layout.simple_kpi_data_row, parent, false);
            view.buildDataRow(header);
        } else
            view = (SimpleKpiDataRow) convertView;
        Map<String, String> item = data.get(position);
        view.updateDataRow(header, item);
        return view;
    }

}
