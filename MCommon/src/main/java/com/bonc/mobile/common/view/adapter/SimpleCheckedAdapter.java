
package com.bonc.mobile.common.view.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SimpleAdapter;

import com.bonc.mobile.common.R;

public class SimpleCheckedAdapter extends SimpleAdapter {
    ListView mList;

    public SimpleCheckedAdapter(Context context, List<? extends Map<String, ?>> data, int resource,
            String[] from, int[] to, ListView listView) {
        super(context, data, resource, from, to);
        mList = listView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        CheckBox check_state = (CheckBox) v.findViewById(R.id.check_state);
        check_state.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mList.setItemChecked(position, isChecked);
            }
        });
        return v;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
