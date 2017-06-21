package com.bonc.mobile.hbmclient.terminal.subactivity;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.common.view.FixedHoloDatePickerDialog;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.terminal.view.MyProgressBar;
import com.bonc.mobile.hbmclient.terminal.view.MyTextView4Top20;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialog;

/**
 * @author ZZZ
 */
public class TopRankActivityGoTo extends BaseTerminalActivity {
    private ListView list_rank;
    private String[] areaid; // 区域id
    private String[] areadesc; // 区域描述
    private TextView textView1; // title
    private Button areaSelect; // 选择的地市文字描述.
    private Button dateSelect; // 时间选择
    private ListViewRankAdapter adapter = new ListViewRankAdapter();
    private JSONArray data; // 数据
    private ProgressDialog pDialog; // 提示框
    private DatePickerDialog pickerDialog;
    private Calendar calendar;
    private BusinessDao businessdao = new BusinessDao();
    private Handler handler = new Handler();

    private Map<String, String> userinfo = businessdao.getUserInfo(); // 区域用户信息
    private String areaId;
    private String areaName;
    private String date;
    private String dataType;
    private final String indTag = "091"; // ranking.tag
    private final static String TAG = "TopRankActivityGoTo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_rank_goto);
        LinearLayout ll = (LinearLayout) findViewById(R.id.id_top_rank_goto);
        ll.setBackgroundDrawable(WatermarkImage.getWatermarkDrawable());
        this.dataType = this.mTerminalActivityEnum.getDateRange().getDateFlag();
        list_rank = (ListView) findViewById(R.id.list_rank);
        View footer = LayoutInflater.from(this).inflate(
                R.layout.list_foot_view, null);
        this.list_rank.addFooterView(footer, null, false);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setText(this.intent.getExtras().getString(
                TerminalConfiguration.TITLE_BIG));
        areaSelect = (Button) findViewById(R.id.area_select);

        dateSelect = (Button) findViewById(R.id.date_select);
        // 日期设置
        calendar = (Calendar) this.mTerminalActivityEnum.getCalendar().clone();
        mTerminalActivityEnum = (TerminalActivityEnum) intent.getExtras().get(
                TerminalConfiguration.KEY_ACTIVITY_ENUM);
        date = DateUtil.formatter(calendar.getTime(), mTerminalActivityEnum
                .getDateRange().getDateServerPattern());
        dateSelect.setText(DateUtil.formatter(calendar.getTime(),
                mTerminalActivityEnum.getDateRange().getDateShowPattern()));

        areaId = userinfo.get("areaId");
        List<Map<String, String>> areaList = businessdao.getAreaInfo(areaId);
        if (areaList != null) {

            areadesc = new String[areaList.size()];
            areaid = new String[areaList.size()];
            int i;
            for (i = 0; i < areaList.size(); i++) {
                areadesc[i] = areaList.get(i).get("areaName");
                areaid[i] = areaList.get(i).get("areaCode");
            }
            areaSelect.setText(areadesc[0]);
        }

        OnDateSetListener listener = new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendar.set(year, monthOfYear, dayOfMonth);
                String newDateString = DateUtil.formatter(calendar.getTime(),
                        mTerminalActivityEnum.getDateRange()
                                .getDateServerPattern());
                if (newDateString.equals(date)) {
                    return;
                }
                // 判断所选择日期是否有数据
                /*
                 * if
				 * (!DataMessageControl.getInstance().isDateInvalid(calendar)) {
				 * Toast.makeText(TopRankActivityGoTo.this, "无该日期数据!",
				 * Toast.LENGTH_LONG).show(); return; }
				 */
                dateSelect.setText(DateUtil.formatter(calendar.getTime(),
                        mTerminalActivityEnum.getDateRange()
                                .getDateShowPattern()));
                date = newDateString;
                pickerDialog.dismiss();
                showTip();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        configMap.put(TerminalConfiguration.KEY_OPTIME, date);
                        getSource();
                        refresh();
                    }
                }).start();
            }
        };
        if (Build.VERSION.SDK_INT >= 24) {
            pickerDialog = new FixedHoloDatePickerDialog(this, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
        } else
            pickerDialog = new MyDatePickerDialog(this, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

        dateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                pickerDialog.show();
            }
        });
        areaSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Map<String, String>> areaList = businessdao
                        .getAreaInfo(areaId);
                if (areaList.size() == 1) {
                    areaId = userinfo.get("areaId");
                    areaList = businessdao.getAreaInfo(areaId);
                    areaSelect.setText(areaList.get(0).get("areaName"));
                    showTip();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            configMap.put(TerminalConfiguration.KEY_AREA_CODE,
                                    areaId);
                            getSource();
                            refresh();
                        }
                    }).start();
                } else {
                    if (areaList != null) {
                        areadesc = new String[areaList.size()];
                        areaid = new String[areaList.size()];
                        int i;
                        for (i = 0; i < areaList.size(); i++) {
                            areadesc[i] = areaList.get(i).get("areaName");
                            areaid[i] = areaList.get(i).get("areaCode");
                        }
                        // areaSelect.setText(areadesc[0]);
                    }
                    new AlertDialog.Builder(UIUtil.resolveDialogTheme(TopRankActivityGoTo.this))
                            .setTitle("区域选择")
                            .setItems(areadesc,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface arg0, int which) {
                                            areaSelect.setText(areadesc[which]);
                                            areaId = areaid[which];
                                            showTip();
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    configMap
                                                            .put(TerminalConfiguration.KEY_AREA_CODE,
                                                                    areaId);
                                                    getSource();
                                                    refresh();
                                                }
                                            }).start();
                                        }
                                    }).create().show();

                }

            }
        });
        showTip();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getSource();
                refresh();
            }
        }).start();
        LogUtil.debug(TAG, "onCreate ------------->");
    }

    // 获取list数据源
    public void getSource() {
        String action = intent.getExtras().getString(
                TerminalConfiguration.KEY_ACTION);
        if (this.configMap == null) {
            this.configMap = (Map<String, String>) intent.getExtras()
                    .getSerializable(TerminalConfiguration.KEY_MAP);
        } else {

        }
        String result = HttpUtil.sendRequest(action, this.configMap);
        try {
            data = new JSONArray(result);
            LogUtils.logBySys("data:" + data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 实时查询 刷新数据
    public void refresh() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                list_rank.setAdapter(adapter);
                pDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        });

    }

    // 主界面ListView适配器，内部类
    class ListViewRankAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (data == null)
                return 0;
            return data.length();
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
                convertView = LayoutInflater.from(TopRankActivityGoTo.this)
                        .inflate(R.layout.top_rank_goto_item, null);
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
                holder.img_up_down = (TextView) convertView
                        .findViewById(R.id.iv_up_down);
                convertView.setTag(holder);
            } else {

                holder = (ViewHolder) convertView.getTag();
            }
            holder.rank_num.setText(position + 1 + "");

            switch (position) {
                case 0:
                    holder.rank_num.setBackgroundResource(R.mipmap.rank01);
                    break;
                case 1:
                    holder.rank_num.setBackgroundResource(R.mipmap.rank02);
                    break;
                case 2:
                    holder.rank_num.setBackgroundResource(R.mipmap.rank03);
                    break;
                default:
                    holder.rank_num.setBackgroundResource(R.mipmap.rank04);
                    break;
            }

            switch (mTerminalActivityEnum.getDateRange()) {
                case DAY:
                    holder.rank_title.setText(data.optJSONObject(position)
                            .optString(TerminalConfiguration.TERM_TYPE_NAME));
                    int tmp = data.optJSONObject(position).optInt(
                            TerminalConfiguration.CURDAY_VALUE);
                    String stmp = "" + tmp;
                    holder.rank_curr_count.setText(stmp);
                    try {
                        int s = data.optJSONObject(position).optInt(
                                TerminalConfiguration.PM_CURDAY_VALUE);
                        stmp = "" + s;
                        holder.rank_last_count.setText(stmp);
                        double d = data.optJSONObject(position).optDouble(
                                TerminalConfiguration.PM_CURDAY_VALUE_DR) * 100;
                        int i = (int) d;
                        holder.last_bar.setProgress(i);
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    holder.curr_bar
                            .setProgress((int) (data.optJSONObject(position)
                                    .optDouble(
                                            TerminalConfiguration.CURDAY_VALUE_DR) * 100));
                    break;
                case MONTH:
                    holder.rank_title.setText(data.optJSONObject(position)
                            .optString(TerminalConfiguration.TERM_TYPE_NAME));
                    int tmp1 = data.optJSONObject(position).optInt(
                            TerminalConfiguration.CURMONTH_VALUE);
                    String stmp1 = "" + tmp1;
                    holder.rank_curr_count.setText(stmp1);
                    try {
                        int s = data.optJSONObject(position).optInt(
                                TerminalConfiguration.PREMONTH_VALUE);
                        stmp1 = "" + s;
                        holder.rank_last_count.setText(stmp1);
                        int i = (int) (data.optJSONObject(position).optDouble(
                                TerminalConfiguration.PREMONTH_VALUE_DR) * 100);
                        holder.last_bar.setProgress(i);

                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    holder.curr_bar
                            .setProgress((int) (data
                                    .optJSONObject(position)
                                    .optDouble(
                                            TerminalConfiguration.CURMONTH_VALUE_DR) * 100));
                    break;
            }

            String ranking_changes = data.optJSONObject(position).optString(
                    TerminalConfiguration.TANK_CHANGE);

            // 判断是排名 上升 下降 新进 持平
            if (ranking_changes == null || "".equals(ranking_changes)
                    || "*".equals(ranking_changes)) {
                holder.img_up_down.setBackgroundResource(R.mipmap.ranknew);
                holder.img_up_down.setText("");
            } else {
                try {
                    ranking_changes = ranking_changes.trim();
                    int rcInt = Integer.valueOf(ranking_changes);
                    String schangvalue = null;
                    int tmp = 0;
                    if (rcInt < 0) {
                        schangvalue = ranking_changes.substring(1,
                                ranking_changes.length());
                        tmp = Integer.valueOf(schangvalue);
                    }

                    if (rcInt == 0) {
                        holder.img_up_down.setVisibility(View.INVISIBLE);
                    } else if (rcInt > 0) {
                        holder.img_up_down.setVisibility(View.VISIBLE);
                        holder.img_up_down.setText("-" + rcInt);
                        holder.img_up_down
                                .setBackgroundResource(R.mipmap.rankdown);
                    } else if (rcInt < 0) {
                        holder.img_up_down.setVisibility(View.VISIBLE);
                        holder.img_up_down.setText("+" + tmp);
                        holder.img_up_down
                                .setBackgroundResource(R.mipmap.rankup);
                    }
                } catch (Exception e) {
                    holder.img_up_down.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
            return convertView;
        }
    }

    private class ViewHolder {
        public TextView rank_num;// 排名图片
        public MyTextView4Top20 rank_title;// 品牌名称
        public TextView rank_curr_count;// 当月销量
        public TextView rank_last_count;// 上月销量
        public MyProgressBar curr_bar;// 当月占比
        public MyProgressBar last_bar;// 上月占比
        public TextView img_up_down;// 上升or下降
    }

    // 提示
    public void showTip() {
        pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setTitle("提示");
        pDialog.setMessage("数据加载中,请稍候");
        pDialog.setIndeterminate(false);
        // pDialog.setCancelable(false);
        pDialog.show();
    }

    ;

    /**
     * 从当前Dialog中查找DatePicker子控件
     *
     * @param group
     * @return
     */
    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }
}// 结束TopRankActivity_GoTo类
