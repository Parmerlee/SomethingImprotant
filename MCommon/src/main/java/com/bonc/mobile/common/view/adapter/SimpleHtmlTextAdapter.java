
package com.bonc.mobile.common.view.adapter;

import java.util.*;

import android.content.*;
import android.text.*;
import android.widget.*;

public class SimpleHtmlTextAdapter extends SimpleAdapter {

    public SimpleHtmlTextAdapter(Context context, List<? extends Map<String, ?>> data,
            int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    @Override
    public void setViewText(TextView v, String text) {
        v.setText(Html.fromHtml(text));
    }

}
