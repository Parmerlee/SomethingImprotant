package com.bonc.mobile.remoteview.activity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bonc.mobile.common.activity.BaseListDataActivity;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.common.util.StringUtil;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.remoteview.R;
import com.bonc.mobile.remoteview.common.ConfigLoader;
import com.bonc.mobile.remoteview.common.Constant;
import com.bonc.mobile.remoteview.common.RemoteUtil;
import com.mobeta.android.dslv.DragSortListView;

public class ChannelMgrActivity extends BaseListDataActivity {
    static final String KEY_FLOW_ID = "FLOW_ID";
    List<Map<String, String>> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_mgr);
        initView();
        loadData();
        RemoteUtil.getInstance().addActivity(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (MyUtils.doInfilter(this)) {

            if (!MyUtils.isBackground(this)) {
                MyUtils.startProtal(this);
                RemoteUtil.getInstance().exit();
            }
        }
    }

    @Override
    protected void initView() {
        super.initView();
        TextViewUtils.setText(this, R.id.title, ConfigLoader.getInstance(this)
                .getMenuName(menuCode));
        ((DragSortListView) mList)
                .setDropListener(new DragSortListView.DropListener() {
                    @Override
                    public void drop(int from, int to) {
                        onDragDrop(from, to);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.add:
                addOrModifyChannel(null);
                break;
            case R.id.delete:
                deleteChannel();
                break;
            case R.id.kpi_mgr:
                startKpiMgr();
                break;
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        showGuide();
    }

    void showGuide() {
        UIUtil.showGuideWindow(this, findViewById(R.id.root), "guide.ch",
                new int[]{R.mipmap.guide_ch_1, R.mipmap.guide_ch_2,
                        R.mipmap.guide_ch_3, R.mipmap.guide_ch_4});
    }

    @Override
    protected void loadData() {
        Map<String, String> param = new LinkedHashMap<String, String>();
        param.put("appType", Constant.APP_TYPE);
        LoadDataTask t = new LoadDataTask(this);
        t.setRetAsArray(true);
        t.execute("/custmenu", param);
    }

    @Override
    protected void bindData(JSONArray result) {
        List<Map<String, String>> data = JsonUtil.toList(result);
        mData = data;
        mList.setAdapter(new ChannelListAdapter(this, data,
                R.layout.channel_list_item, new String[]{"MENU_NAME"},
                new int[]{R.id.text}));
        ConfigLoader.getInstance(this).loadChannel(mData);
    }

    class ChannelListAdapter extends SimpleAdapter {
        public ChannelListAdapter(Context context,
                                  List<? extends Map<String, ?>> data, int resource,
                                  String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            final Map<String, String> m = (Map<String, String>) getItem(position);
            View v = super.getView(position, convertView, parent);
            v.findViewById(R.id.text).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    addOrModifyChannel(m);
                }
            });
            RadioButton check_state = (RadioButton) v
                    .findViewById(R.id.check_state);
            check_state
                    .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
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

    void addOrModifyChannel(final Map<String, String> m) {
        final View dialog_view = View.inflate(this, R.layout.add_channel, null);
        TextViewUtils.setText(dialog_view, R.id.inputbox_message,
                getText(R.string.msg_channel_name));
        final RadioGroup date_type = (RadioGroup) dialog_view
                .findViewById(R.id.date_type);
        int titleId;
        if (m != null) {
            date_type.setVisibility(View.GONE);
            TextViewUtils.setText(dialog_view, R.id.inputbox_text,
                    m.get("MENU_NAME").replace("(日)", "").replace("(月)", ""));
            titleId = R.string.modify_channel;
        } else
            titleId = R.string.add_channel;
        UIUtil.showAlertDialogBy(this, getString(titleId), dialog_view,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView text = (TextView) dialog_view
                                .findViewById(R.id.inputbox_text);
                        String name = text.getText().toString();
                        if (name.length() == 0) {
                            showToast("频道名称不能为空");
                            return;
                        }
                        if (m == null) {
                            doAddOrModify(null, name, getDateType(date_type
                                    .getCheckedRadioButtonId()));
                        } else {
                            doAddOrModify(m.get(KEY_FLOW_ID), name, null);
                        }
                    }
                }, null);
    }

    String getDateType(int id) {
        String t = "D";
        if (id == R.id.ch2)
            t = "M";
        else if (id == R.id.ch3)
            t = "H";
        return t;
    }

    void doAddOrModify(String id, String name, String datetype) {
        String action;
        Map<String, String> param = new LinkedHashMap<String, String>();
        if (id == null) {
            action = "/custmenu/saveMenu";
            param.put("dateType", datetype);
        } else {
            action = "/custmenu/modMenuName";
            param.put("flowId", id);
        }
        param.put("menuName", name);
        param.put("appType", Constant.APP_TYPE);
        new ChannelActionTask(this).execute(action, param);
    }

    void deleteChannel() {
        final int pos = mList.getCheckedItemPosition();
        if (pos == -1) {
            showToast("请选择要删除的频道");
            return;
        }
        UIUtil.showAlertDialog(this, "确定删除选择的频道?",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doDelete(mData.get(pos).get(KEY_FLOW_ID));
                    }
                }, null);
    }

    void doDelete(String flowIds) {
        Map<String, String> param = new LinkedHashMap<String, String>();
        param.put("appType", Constant.APP_TYPE);
        param.put("flowIds", flowIds);
        new ChannelActionTask(this).execute("/custmenu/deleteMenu", param);
    }

    void startKpiMgr() {
        int pos = mList.getCheckedItemPosition();
        if (pos == -1) {
            showToast("请选择要管理的频道");
            return;
        }
        Intent intent = new Intent(this, ChannelKpiMgrActivity.class);
        intent.putExtra(ChannelKpiMgrActivity.CH_CODE,
                mData.get(pos).get("MENU_CODE"));
        startActivity(intent);
    }

    void doSort(String flowIds) {
        Map<String, String> param = new LinkedHashMap<String, String>();
        param.put("appType", Constant.APP_TYPE);
        param.put("flowIds", flowIds);
        ChannelActionTask t = new ChannelActionTask(this);
        t.refresh = false;
        t.execute("/custmenu/modMenuOrder", param);
    }

    class ChannelActionTask extends HttpRequestTask {
        boolean refresh = true;

        public ChannelActionTask(Context context) {
            super(context);
        }

        @Override
        protected void handleResult(JSONObject result) {
            boolean flag = result.optBoolean("flag");
            showToast(result.optString("msg"));
            if (flag && refresh) {
                loadData();
            }
        }
    }

    void onDragDrop(int from, int to) {
        if (from != to) {
            Map<String, String> item = mData.get(from);
            mData.remove(item);
            mData.add(to, item);
            ((BaseAdapter) mList.getAdapter()).notifyDataSetChanged();
            ((DragSortListView) mList).moveCheckState(from, to);
            ConfigLoader.getInstance(this).loadChannel(mData);
            doSort(StringUtil.join(DataUtil.extractList(mData, KEY_FLOW_ID)));
        }
    }
}
