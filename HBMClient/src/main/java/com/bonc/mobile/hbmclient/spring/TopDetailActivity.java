package com.bonc.mobile.hbmclient.spring;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.NumSdf;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.BaseActivity;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector2;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.terminal.view.MyProgressBar;
import com.bonc.mobile.hbmclient.terminal.view.MyTextView4Top20;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.FileUtils;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.share.lwg.util.asyntask.QuestAsynTask;
import common.share.lwg.util.state.mainkpi.AStateMainKpi;

public class TopDetailActivity extends BaseActivity implements View.OnClickListener {

    private Button areaButton, dateButton;
    private String[] areaid; // 区域id
    private String areadesc[] = null; // 区域描述

    private Calendar calendar;

    Map<String, String> questMap = new HashMap<String, String>();
    JSONArray curretData, preData;
    boolean isTop10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_detail);
        this.findViewById(android.R.id.content).setBackgroundDrawable(
                WatermarkImage.getWatermarkDrawable());

        calendar = Calendar.getInstance();
        initView();
        loadDate();

    }


    void initView() {
        areaButton = (Button) this.findViewById(R.id.id_button1);
        dateButton = (Button) this.findViewById(R.id.id_button2);
        String str = this.getIntent().getExtras().getString("map");
        LogUtils.logBySys(str);
        try {
            questMap = JsonUtil.toMap(new JSONObject(str));

            areaMap = JsonUtil.toMap(this.getIntent().getExtras().getString("areaMap"));

            isTop10 = Boolean.valueOf(Integer.valueOf(JsonUtil.toMap(this.getIntent().getExtras().getString("itemData")).get("rank")) < 11);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //{optime=20170111, dateType=D, menuCode=60001101, areaCode=HB}
        LogUtils.logBySys(questMap.get("dateType").toString() + ";map:" + questMap.toString());
        questMap.put("menuCode", String.valueOf(questMap.get("menuCode")));
        if (questMap.containsKey("optime")) {
            this.questMap.put("time", questMap.get(AStateMainKpi.KEY_OPTIME));
            this.questMap.remove(AStateMainKpi.KEY_OPTIME);

        }

        String time = questMap.get("time");
//        LogUtils.logBySys("time:" + time);
//        LogUtils.logBySys("year:" + Integer.valueOf(questMap.get("time").substring(0, 4)) + ",month:" + Integer.valueOf(questMap.get("time").substring(5, 6)) + ";day:" + Integer.valueOf(questMap.get("time").substring(7, 8)));
        try {
            //20170115
            calendar.set(Calendar.YEAR, Integer.valueOf(questMap.get("time").substring(0, 4)));
            calendar.set(Calendar.MONTH, Integer.valueOf(questMap.get("time").substring(4, 6)) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(questMap.get("time").substring(6, 8)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtils.logBySys("time222222:" + DateUtil.formatter(calendar.getTime(),
                DateUtil.PATTERN_8));
//        try {
//            if (!TextUtils.isEmpty(questMap.get("time")))
//                dateButton.setText(DateUtil.oneStringToAntherString(questMap.get("time").toString(),
//                        DateUtil.PATTERN_9, "yyyy/MM/dd"));
//            else {
//                dateButton.setText(DateUtil.formatter(calendar.getTime(),
//                        DateUtil.PATTERN_MODEL2_10));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        LogUtils.logBySys("areaMap:" + areaMap.toString());
        areaButton.setText(areaMap.get("AREA_NAME").toString());
        dateButton.setText(DateUtil.oneStringToAntherString(String.valueOf(areaMap.get("time")),
                DateUtil.PATTERN_8, "yyyy/MM/dd"));


        areaId = areaMap.get("AREA_CODE").toString();
        areaButton.setOnClickListener(this);
        dateButton.setOnClickListener(this);

        list_rank = (ListView) findViewById(R.id.list_rank);
        ((TextView) this.findViewById(R.id.textView1)).setText("TOP10套餐累计达到数");

    }

    private ListView list_rank;
    ListViewRankAdapter adapter = new ListViewRankAdapter();

    void loadDate() {

        QuestAsynTask top10Task = new QuestAsynTask(this, "/bi/phased/") {
            @Override
            public void onPost(Object result) {

                LogUtils.logBySys("top10:" + result.toString());
                if (result == null) {
                    return;
                }
//                LogUtils.logBySys(FileUtils.getFromAssets(TopDetailActivity.this, "json.txt"));
//                parseData(result);
                bindDate(result);
            }
        };
        if (this.questMap != null) {

            try {
                this.questMap.put("areaCode", areaId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LogUtils.logBySys(questMap.toString());
        top10Task.execute("goodStart", questMap);


        QuestAsynTask areaTask = new QuestAsynTask(this, "/bi/getAreaList/") {
            @Override
            public void onPost(Object result) {

//                LogUtils.logBySys(result.toString());
//                LogUtils.logBySys("area result:" + result.toString());
//                parseData(result);
                buildAreaDate(result);
            }
        };

        Map<String, String> map = new HashMap<>();
        map.put("areaCode", questMap.get("areaCode"));
        areaTask.execute("general", map);

//        indexCilcked = 1;

    }

    void bindDate(Object result) {
        curretData = new JSONArray();
        preData = new JSONArray();
        try {
            curretData = new JSONObject(result.toString()).getJSONArray(isTop10 ? "top10" : "end10");
            preData = new JSONObject(result.toString()).getJSONArray(isTop10 ? "preTop10" : "preEnd10");
            LogUtils.logBySys("isTop10:" + isTop10);
//            LogUtils.logBySys("curretData:" + DateUtil.oneStringToAntherString(String.valueOf(new JSONObject(result.toString()).getString("time")),
//                    DateUtil.PATTERN_8, "yyyy/MM/dd"));

            dateButton.setText(DateUtil.oneStringToAntherString(String.valueOf(new JSONObject(result.toString()).getString("time")),
                    DateUtil.PATTERN_8, "yyyy/MM/dd"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        list_rank.setAdapter(adapter);
    }

    String areaId;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_button1:
                new AlertDialog.Builder(UIUtil.resolveDialogTheme(this))
                        .setTitle("区域选择")
                        .setItems(areadesc,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface arg0, int which) {
                                        indexCilcked = which;
                                        areaButton.setText(areadesc[which]);
                                        areaId = areaid[which];
//                                        showTip();
                                        loadDate();
                                    }
                                }).create().show();
                break;
            case R.id.id_button2:
                LogUtils.logBySys("time11111:" + DateUtil.formatter(calendar.getTime(),
                        DateUtil.PATTERN_8));
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        questMap.put("time", DateUtil.formatter(calendar.getTime(),
                                DateUtil.PATTERN_8));
                        loadDate();
                    }
                };
                if (Build.VERSION.SDK_INT >= 24) {
                    UIUtil.showDatePickerDialogFor24(this, listener, calendar);
                } else {
                    UIUtil.showDatePickerDialog(this, listener, calendar);
                }
                break;
        }
    }

    Map<String, String> areaMap = new HashMap<>();
    String a[];

    List<Map<String, String>> areaDataList = new ArrayList<>();
    int indexCilcked = -1;

    void buildAreaDate(Object result) {

        try {
            JSONArray areaDataArray = new JSONArray(result.toString());
            areaDataList = JsonUtil.toList(areaDataArray);

            areadesc = new String[areaDataList.size()];
            areaid = new String[areaDataList.size()];
            for (int i = 0; i < areaDataList.size(); i++) {
                areadesc[i] = areaDataList.get(i).get("AREA_NAME");
                areaid[i] = areaDataList.get(i).get("AREA_CODE");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Iterator it = areaMap.entrySet().iterator();
//
//        while (it.hasNext()) {
//            //获得节点
//            Map.Entry entry = (Map.Entry) it.next();
//            //获得节点的key values
//            String key = (String) entry.getKey();
//            String value = (String) entry.getValue();
//
//        }


    }


    // 主界面ListView适配器，内部类
    class ListViewRankAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (curretData == null && preData == null)
                return 0;
            return curretData.length();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(TopDetailActivity.this)
                        .inflate(R.layout.top_rank_goto_item_spring, null);
                holder = new ViewHolder();
                holder.rank_num = (TextView) convertView
                        .findViewById(R.id.rank_num);
                holder.rank_title = (MyTextView4Top20) convertView
                        .findViewById(R.id.rank_title);
                holder.rank_curr_count = (TextView) convertView
                        .findViewById(R.id.rank_curr_count1);
                holder.rank_last_count = (TextView) convertView
                        .findViewById(R.id.rank_last_count2);
                holder.curr_bar = (MyProgressBar) convertView
                        .findViewById(R.id.rank_curr_bar);
                holder.last_bar = (MyProgressBar) convertView
                        .findViewById(R.id.rank_last_bar);
//                holder.img_up_down = (TextView) convertView
//                        .findViewById(R.id.iv_up_down);
//                holder.img_up_down.setVisibility(View.GONE);
                convertView.setTag(holder);
            } else {

                holder = (ViewHolder) convertView.getTag();
            }


            switch (position) {
                case 0:
                    holder.rank_num.setBackgroundResource(R.mipmap.rank01working);
                    break;
                case 1:
                    holder.rank_num.setBackgroundResource(R.mipmap.rank02working);
                    break;
                case 2:
                    holder.rank_num.setBackgroundResource(R.mipmap.rank03working);
                    break;
                default:
                    holder.rank_num.setBackgroundResource(R.mipmap.rank04working);
                    break;
            }

//            <!--android:background="@mipmap/rank01"-->
            holder.rank_num.setText(Integer.valueOf(curretData.optJSONObject(0).optString("RANK")) + position + "");
            holder.rank_title.setText(curretData.optJSONObject(position)
                    .optString("FEE_NAME"));
            int tmp = curretData.optJSONObject(position).optInt("TOTAL_VALUE");
            String stmp = "" + tmp;
            holder.rank_curr_count.setText(NumSdf.changeData(stmp));
            try {
                int s = preData.optJSONObject(position).optInt("TOTAL_VALUE");
                stmp = "" + s;
                holder.rank_last_count.setText(NumSdf.changeData(stmp));
                double d = preData.optJSONObject(position).optDouble("PECENT");
                int i = (int) d;
//                holder.last_bar.setProgress(i, getProcessText(preData.optJSONObject(position).optString("PECENT")));
                holder.last_bar.setProgress(i, "" + i + "%");
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                holder.last_bar.setProgress(0, "--");
                holder.rank_last_count.setText("--");
            }
//            holder.curr_bar
//                    .setProgress((int) (curretData.optJSONObject(position)
//                            .optDouble("PECENT") * 100), getProcessText((curretData.optJSONObject(position)
//                            .optString("PECENT"))));
            holder.curr_bar
                    .setProgress((int) (curretData.optJSONObject(position)
                            .optDouble("PECENT")), "" + (int) (curretData.optJSONObject(position)
                            .optDouble("PECENT")) + "%");

//                String ranking_changes = curretData.optJSONObject(position).optString(
//                        TerminalConfiguration.TANK_CHANGE);

//                // 判断是排名 上升 下降 新进 持平
//                if (ranking_changes == null || "".equals(ranking_changes)
//                        || "*".equals(ranking_changes)) {
//                    holder.img_up_down.setBackgroundResource(R.mipmap.ranknew);
//                    holder.img_up_down.setText("");
//                } else {
//                    try {
//                        ranking_changes = ranking_changes.trim();
//                        int rcInt = Integer.valueOf(ranking_changes);
//                        String schangvalue = null;
//                        int tmp2 = 0;
//                        if (rcInt < 0) {
//                            schangvalue = ranking_changes.substring(1,
//                                    ranking_changes.length());
//                            tmp2 = Integer.valueOf(schangvalue);
//                        }
//
//                        if (rcInt == 0) {
//                            holder.img_up_down.setVisibility(View.INVISIBLE);
//                        } else if (rcInt > 0) {
//                            holder.img_up_down.setVisibility(View.VISIBLE);
//                            holder.img_up_down.setText("-" + rcInt);
//                            holder.img_up_down
//                                    .setBackgroundResource(R.mipmap.rankdown);
//                        } else if (rcInt < 0) {
//                            holder.img_up_down.setVisibility(View.VISIBLE);
//                            holder.img_up_down.setText("+" + tmp);
//                            holder.img_up_down
//                                    .setBackgroundResource(R.mipmap.rankup);
//                        }
//                    } catch (Exception e) {
//                        holder.img_up_down.setVisibility(View.INVISIBLE);
//                        e.printStackTrace();
//                    }
//            }

            return convertView;
        }

        class ViewHolder {
            public TextView rank_num;// 排名图片
            public MyTextView4Top20 rank_title;// 品牌名称
            public TextView rank_curr_count;// 当月销量
            public TextView rank_last_count;// 上月销量
            public MyProgressBar curr_bar;// 当月占比
            public MyProgressBar last_bar;// 上月占比
            public TextView img_up_down;// 上升or下降
        }

        private String getProcessText(String str) {
            LogUtils.logBySys("AAA:" + str);
            String string = null;
            if (TextUtils.equals(str, "0") || TextUtils.equals(str, "0.0")) {
                return "0";
            }
            Float i = Float.valueOf(str);
//            new DecimalFormat("##0.00").format(i * 100);
            return String.valueOf(new DecimalFormat("##0.0").format(i * 100)) + "%";
        }
    }
}
