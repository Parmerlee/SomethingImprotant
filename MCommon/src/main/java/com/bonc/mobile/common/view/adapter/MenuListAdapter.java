
package com.bonc.mobile.common.view.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.bonc.mobile.common.data.BaseConfigLoader;

public class MenuListAdapter extends SimpleAdapter {
    BaseConfigLoader configLoader;

    public MenuListAdapter(Context context, List<? extends Map<String, ?>> data, int resource,
            String[] from, int[] to, BaseConfigLoader configLoader) {
        super(context, data, resource, from, to);
        this.configLoader = configLoader;
    }

    @Override
    public void setViewImage(ImageView v, String value) {
        v.setImageResource(configLoader.getMenuIcon(value));
    }

}
