
package com.bonc.mobile.common.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.SimpleExpandableListAdapter;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.TextViewUtils;

public abstract class AnnounceListActivity extends BaseDataActivity {
    protected ExpandableListView mList;
    protected List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
    protected List<List<Map<String, String>>> childrenData = new ArrayList<List<Map<String, String>>>();
    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce_list);
        initView();
        loadData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.button_more) {
            loadData();
        }
    }

    @Override
    protected void initView() {
        super.initView();
        TextViewUtils.setText(this, R.id.title, "公告查看");
        mList = (ExpandableListView) findViewById(R.id.list);
        mList.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition,
                    long id) {
                return true;
            }
        });
    }

    @Override
    protected void loadData() {
        Map<String, String> param = new LinkedHashMap<String, String>();
        param.put("page", String.valueOf(page));
        new LoadDataTask(this).execute(getAction(), param);
    }

    protected abstract String getAction();
    
    @Override
    protected void bindData(JSONObject result) {
        page = result.optInt("page");
        JSONArray data = result.optJSONArray("data");
        if (data == null) {
            showToast(getString(R.string.no_more_data));
            return;
        }
        for (int i = 0; i < data.length(); i++) {
            Map<String, List<Map<String, String>>> m = JsonUtil.toMap2(data.optString(i));
            Map<String, String> g = new HashMap<String, String>();
            g.put("date", new ArrayList<String>(m.keySet()).get(0));
            groupData.add(g);
            childrenData.addAll(m.values());
        }
        SimpleExpandableListAdapter adapter = new AnnounceListAdapter(this, groupData,
                R.layout.announce_list_title, new String[] {
                    "date"
                }, new int[] {
                    R.id.text
                }, childrenData, R.layout.announce_list_item, new String[] {
                    "MSG"
                }, new int[] {
                    R.id.text
                });
        mList.setAdapter(adapter);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            mList.expandGroup(i);
        }
    }

    class AnnounceListAdapter extends SimpleExpandableListAdapter {
        public AnnounceListAdapter(Context context, List<? extends Map<String, ?>> groupData,
                int groupLayout, String[] groupFrom, int[] groupTo,
                List<? extends List<? extends Map<String, ?>>> childData, int childLayout,
                String[] childFrom, int[] childTo) {
            super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout,
                    childFrom, childTo);
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
            View v = super.getChildView(groupPosition, childPosition, isLastChild, convertView,
                    parent);
            TextViewUtils.setText(v, R.id.id_number, String.valueOf(childPosition + 1));
            return v;
        }
    }
}
