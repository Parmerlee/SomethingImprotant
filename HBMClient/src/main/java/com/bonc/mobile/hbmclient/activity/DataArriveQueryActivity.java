package com.bonc.mobile.hbmclient.activity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.UIUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

/**
 * 接口数据监控
 *
 * @author Lenevo
 */
public class DataArriveQueryActivity extends SlideHolderActivity {
    private ListView mListView;
    private Button dateSelect;
    private Button dateRangeSelect;
    private String optimeDay, optimeMonth;
    private Calendar calendar;
    private JSONArray mJSONArray, mJSONArrayDay, mJSONArrayMonth;
    boolean monthQurey;

    private final String KEY_OPTIME = "dateTime";
    private final String KEY_QUERYTYPE = "queryType";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainContent(
                getIntent().getStringExtra(MenuEntryAdapter.KEY_MENU_CODE),
                R.layout.data_arrive_query);
        LinearLayout ll = (LinearLayout) findViewById(R.id.parent);
        ll.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());

        Button navigator = (Button) this.findViewById(R.id.id_button_logo);
        navigator.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                slideHolder.toggle();
            }
        });

        this.mListView = (ListView) this.findViewById(R.id.id_cert_list);
        View header = findViewById(R.id.data_list_header);
        header.setBackgroundDrawable(getResources().getDrawable(
                R.mipmap.glay_list_title_bar));
        View footer = LayoutInflater.from(this).inflate(
                R.layout.list_foot_view, null);
        this.mListView.addFooterView(footer, null, false);
        this.dateSelect = (Button) this.findViewById(R.id.id_date_select);
        this.dateSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate();
            }
        });
        dateRangeSelect = (Button) this.findViewById(R.id.id_date_range_select);
        dateRangeSelect.setText(R.string.tag_day);

        dateRangeSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                monthQurey = !monthQurey;
                if (monthQurey) {
                    dateRangeSelect.setText(R.string.tag_month2);
                    setData(mJSONArrayMonth);
                } else {
                    dateRangeSelect.setText(R.string.tag_day);
                    setData(mJSONArrayDay);
                }
            }
        });
        calendar = Calendar.getInstance();
        initData();
    }


    void chooseDate() {


        if (monthQurey) {
            UIUtil.showDatePickerDialogWithoutDay(this,
                    new OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            calendar.set(year, monthOfYear, dayOfMonth);
                            initData();
                        }
                    }, calendar);
        } else {

            UIUtil.showDatePickerDialog(this, new OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    calendar.set(year, monthOfYear, dayOfMonth);
                    initData();
                }
            }, calendar);
        }
    }

    private void nullDataRemind() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                removeDialog(LOADING_DIALOG);
                Toast.makeText(DataArriveQueryActivity.this,
                        getString(R.string.no_data), Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void initData() {

        showDialog(LOADING_DIALOG);
        new Thread(new Runnable() {

            @Override
            public void run() {
                // 获得数据
                if (!getDataRemote()) {
                    nullDataRemind();
                    return;
                }

                // 开始布局
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setData();
                        removeDialog(LOADING_DIALOG);
                    }
                });
            }
        }).start();
    }

    void setData() {
        ListAdapter listAdapter = new ListAdapter(DataArriveQueryActivity.this,
                mJSONArray);
        mListView.setAdapter(listAdapter);
        if (monthQurey) {
            dateSelect.setText(DateUtil.oneStringToAntherString(optimeMonth,
                    DateUtil.PATTERN_9, "yyyy/MM"));
        } else {
            dateSelect.setText(DateUtil.oneStringToAntherString(optimeDay,
                    DateUtil.PATTERN_9, "yyyy/MM/dd"));
        }
    }

    void setData(JSONArray a) {
        if (a != null) {
            mJSONArray = a;
            setData();
        } else {
            initData();
        }
    }

    private boolean getDataRemote() {
        Map<String, String> param = new HashMap<String, String>();
        if (monthQurey) {
            optimeMonth = DateUtil.formatter(calendar.getTime(),
                    DateUtil.PATTERN_9);
            param.put(KEY_OPTIME, optimeMonth);
            param.put(KEY_QUERYTYPE, "M");
        } else {
            optimeDay = DateUtil.formatter(calendar.getTime(),
                    DateUtil.PATTERN_9);
            param.put(KEY_OPTIME, optimeDay);
            param.put(KEY_QUERYTYPE, "D");
        }
        String json_reply = null;
        try {
            param.put("menuCode",
                    getIntent().getStringExtra(MenuEntryAdapter.KEY_MENU_CODE));
            json_reply = HttpUtil.sendRequest(
                    ActionConstant.QUERY_ETL_DETAIL_INFO, param);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (json_reply == null || "".equals(json_reply)) {
            return false;
        }
        try {

            mJSONArray = new JSONArray(json_reply);
            if (monthQurey)
                mJSONArrayMonth = mJSONArray;
            else
                mJSONArrayDay = mJSONArray;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private class ListAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;
        private JSONArray jsonArray;

        public ListAdapter(Context c, JSONArray json) {
            mContext = c;
            mInflater = LayoutInflater.from(c);
            jsonArray = json;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return jsonArray.length();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.data_list_item, null);
            }
            TextView tv_if_name = (TextView) convertView
                    .findViewById(R.id.id_if_name);
            TextView tv_if_type = (TextView) convertView
                    .findViewById(R.id.id_if_type);
            TextView tv_data_date = (TextView) convertView
                    .findViewById(R.id.id_data_date);
            TextView tv_data_time = (TextView) convertView
                    .findViewById(R.id.id_data_time);
            TextView tv_real_time = (TextView) convertView
                    .findViewById(R.id.id_real_time);
            TextView tv_is_delay = (TextView) convertView
                    .findViewById(R.id.id_is_delay);
            TextView tv_batchNumber = (TextView) convertView
                    .findViewById(R.id.id_batchNumber);
            TextView tv_repeatNumber = (TextView) convertView
                    .findViewById(R.id.id_repeatNumber);
            if (position % 2 == 0) {
                convertView.setBackgroundColor(mContext.getResources()
                        .getColor(R.color.zeven_list_color));
            } else {
                convertView.setBackgroundColor(mContext.getResources()
                        .getColor(R.color.zodd_list_color));
            }
            JSONObject jo;
            try {
                jo = jsonArray.getJSONObject(position);
                tv_if_name.setText(jo.optString("interFaceName"));
                tv_if_type.setText(jo.optString("dateType"));
                tv_data_date.setText(jo.optString("interFaceTime"));
                tv_data_time.setText(jo.optString("planTime"));
                tv_real_time.setText(jo.optString("etlTime"));
                tv_is_delay.setText(jo.optString("isLater"));
                if ("是".equals(jo.optString("isLater")))
                    tv_is_delay.setTextColor(Color.RED);
                else
                    tv_is_delay.setTextColor(Color.BLACK);
                tv_batchNumber.setText(jo.optString("batchNumber"));
                tv_repeatNumber.setText(jo.optString("repeatNum"));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return convertView;
        }
    }

}
