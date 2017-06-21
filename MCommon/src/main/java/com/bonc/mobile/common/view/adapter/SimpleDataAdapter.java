
package com.bonc.mobile.common.view.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bonc.mobile.common.util.StringUtil;

public class SimpleDataAdapter extends SimpleAdapter {

    public SimpleDataAdapter(Context context, List<? extends Map<String, ?>> data,
            int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    @Override
    public void setViewText(TextView v, String text) {
        v.setText(StringUtil.nullToString2(text));
    }

}
