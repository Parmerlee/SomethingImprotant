/**
 * ChannelActivity
 */
package com.bonc.mobile.hbmclient.activity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ScrollView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.common.view.FixedHoloDatePickerDialog;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.asyn_task.ChannelSummaryAsynTask;
import com.bonc.mobile.hbmclient.asyn_task.ChannelSummaryAsynTask.OnPostListener;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.composite.channel.RootChannelBranch;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.state.channel.adapter.ALevelState;
import com.bonc.mobile.hbmclient.state.channel.adapter.DetailsMachine;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialog;
import com.bonc.mobile.hbmclient.view.MyDatePickerDialogWithoutDay;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

/**
 * @author liweigao
 *         <p/>
 *         ChannelAnalyzeActivity及以下两个属于渠道
 *
 *         自营厅效益
 */
public class ChannelAnalyzeActivity extends SlideHolderActivity {
    private BusinessDao dao = new BusinessDao();
    private Map questMap = new HashMap<String, String>();
    private List<Map<String, String>> areaInfo;
    private long last_select_time = 0;
    private Calendar calendar;
    private boolean isFirst = true;
    private String areaLevel;
    private Map<String, String> selectArea;
    private ChannelSummaryAsynTask mAsynTask;

    private RootChannelBranch summaryProfitRoot;
    private ExpandableListView mDetailsELV;
    private Button areaSelect, dateSelect, navigator;
    private DatePickerDialog datePicker;
    ScrollView scrollView;

    private final String KEY_DATE = "date";
    private final String KEY_USER_CODE = "userCode";
    private final String KEY_AREA_CODE = "areaCode";

    /*
     * (non-Javadoc)
     *
     * @see
     * com.bonc.mobile.hbmclient.activity.SlideHolderActivity#onCreate(android
     * .os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String menuCode = intent.getStringExtra(MenuEntryAdapter.KEY_MENU_CODE);
        setMainContent(menuCode, R.layout.channel_analyze_root);

        findViewById(R.id.id_parent).setBackgroundDrawable(
                WatermarkImage.getWatermarkDrawable());
        String menuName = dao.getMenuName(menuCode);
        TextView title = (TextView) this.findViewById(R.id.logo_word_mon_dev);
        title.setText(menuName);
        this.navigator = (Button) this.findViewById(R.id.id_navigator);
        this.navigator.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                slideHolder.toggle();
            }
        });

        scrollView = (ScrollView) findViewById(R.id.id_scrollView);
        this.summaryProfitRoot = new RootChannelBranch();

        View channel_details = this.findViewById(R.id.id_details);
        this.mDetailsELV = (ExpandableListView) channel_details
                .findViewById(R.id.id_details_listview);
        this.mDetailsELV.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if ("4".equals(areaLevel)) {
                    return true;
                }
                Map<String, String> m = (Map<String, String>) mDetailsELV
                        .getExpandableListAdapter().getChild(groupPosition,
                                childPosition);
                Intent intent = new Intent(ChannelAnalyzeActivity.this,
                        ChannelIncomeAreaActivity.class);
                intent.putExtra(ChannelIncomeAreaActivity.OPTIME,
                        (String) questMap.get(KEY_DATE));
                intent.putExtra(ChannelIncomeAreaActivity.AREA_CODE,
                        (String) questMap.get(KEY_AREA_CODE));
                intent.putExtra(ChannelIncomeAreaActivity.KPI_CODE,
                        m.get(ALevelState.KEY_VALUE_KEY));
                startActivity(intent);
                return false;
            }
        });

        this.mDetailsELV.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                doScroll();
            }
        });
        findViewById(R.id.id_profit).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChannelAnalyzeActivity.this,
                        ChannelIncomeAreaActivity.class);
                intent.putExtra(ChannelIncomeAreaActivity.OPTIME,
                        (String) questMap.get(KEY_DATE));
                intent.putExtra(ChannelIncomeAreaActivity.AREA_CODE,
                        (String) questMap.get(KEY_AREA_CODE));
                intent.putExtra(ChannelIncomeAreaActivity.KPI_CODE, "valuen23");
                startActivity(intent);
            }
        });

        this.areaSelect = (Button) this.findViewById(R.id.id_button1);
        this.areaSelect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                chooseArea();
            }
        });
        this.dateSelect = (Button) this.findViewById(R.id.id_button2);
        this.dateSelect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                chooseDate();
            }
        });

        initialQuestArgs();
        questServer();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MenuSecondActivity.REQUEST_CODE_FINISH
                && resultCode == RESULT_OK) {
            finish();
        }
    }

    private void initialQuestArgs() {
        String userCode = dao.getUserInfo().get("usercode");
        String date = null;
        String areaCode = null;
        questMap.put(KEY_DATE, date);
        questMap.put(KEY_USER_CODE, userCode);
        questMap.put(KEY_AREA_CODE, areaCode);
        questMap.put("menuCode",
                getIntent().getStringExtra(MenuEntryAdapter.KEY_MENU_CODE));
    }

    private void questServer() {
        this.mAsynTask = new ChannelSummaryAsynTask(this, new OnPostListener() {

            @Override
            public void onPost(String s) {
                setData(s);
            }
        });
        this.mAsynTask.execute(ActionConstant.GET_CHANNEL_ANALYZE_DATA,
                questMap);
    }

    private void setData(String result) {
        try {
            JSONObject jo = null;
            try {
                jo = new JSONObject(result);
                String validate = jo.optString("validate", "0");
                if ("1".equals(validate)) {
                    validateUser(jo);
                } else {
                    invalidateUser(jo);
                }
            } catch (Exception e) {
                reSetData();
            }
        } catch (Exception e) {
            Toast.makeText(this, "数据加载异常", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        }, 200);
    }

    private void reSetData() {
        setAreaName();
        setShowDate(null);
        this.summaryProfitRoot.setAreaLevel(null);
        this.summaryProfitRoot.dispatchView(getWindow().getDecorView());
        this.summaryProfitRoot.iteratorReset();
        DetailsMachine machine = new DetailsMachine(null, null);
        SimpleExpandableListAdapter sela = new SimpleExpandableListAdapter(
                this, machine.getGroupData(), machine.getGroupLayout(),
                machine.getGroupFrom(), machine.getGroupTo(),
                machine.getChildData(), machine.getChildLayout(),
                machine.getChildFrom(), machine.getChildTo());
        this.mDetailsELV.setAdapter(sela);
    }

    private void invalidateUser(JSONObject jo) {
        /*
         * setAreaName(); setShowDate(null);
		 * this.summaryProfitRoot.setAreaLevel(null);
		 * this.summaryProfitRoot.dispatchView(getWindow().getDecorView());
		 * this.summaryProfitRoot.iteratorReset(); DetailsMachine machine = new
		 * DetailsMachine(null, null); SimpleExpandableListAdapter sela = new
		 * SimpleExpandableListAdapter( this, machine.getGroupData(),
		 * machine.getGroupLayout(), machine.getGroupFrom(),
		 * machine.getGroupTo(), machine.getChildData(),
		 * machine.getChildLayout(), machine.getChildFrom(),
		 * machine.getChildTo()); this.mDetailsELV.setAdapter(sela);
		 */
        reSetData();
        String msg = jo.optString("msg");
        if (msg == null || "".equals(msg) || "null".equals(msg)) {
            msg = "该用户无权访问此界面";
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void validateUser(JSONObject jo) {
        JSONArray ja_area = jo.optJSONArray("areaInfo");
        if (ja_area == null || "".equals(ja_area) || "null".equals(ja_area)
                || ja_area.length() <= 0) {

        } else if (ja_area.length() > 0) {
            this.areaInfo = JsonUtil.toDataList(ja_area);
            if (isFirst) {
                this.selectArea = this.areaInfo.get(0);
                setAreaName();
                isFirst = false;
            } else {

            }
        }
        if (this.calendar == null) {
            setShowDate(jo.optString("date"));
        }

        JSONObject jo_data = jo.optJSONObject("data");
        areaLevel = this.selectArea.get("AREA_LEVEL");
        this.summaryProfitRoot.setAreaLevel(areaLevel);
        this.summaryProfitRoot.dispatchView(getWindow().getDecorView());
        this.summaryProfitRoot.iteratorReset();
        this.summaryProfitRoot.setOutEnvironment(this);
        this.summaryProfitRoot.setData(jo);
        DetailsMachine machine = new DetailsMachine(jo_data, areaLevel);
        SimpleExpandableListAdapter sela = new SimpleExpandableListAdapter(
                this, machine.getGroupData(), machine.getGroupLayout(),
                machine.getGroupFrom(), machine.getGroupTo(),
                machine.getChildData(), machine.getChildLayout(),
                machine.getChildFrom(), machine.getChildTo());
        this.mDetailsELV.setAdapter(sela);
    }

    public String getOptime() {
        return DateUtil.oneStringToAntherString(this.dateSelect.getText()
                        .toString(), DateRangeEnum.MONTH.getDateShowPattern(),
                DateRangeEnum.MONTH.getDateServerPattern());
    }

    public String getAreaCode() {
        return this.selectArea.get("AREA_CODE");
    }

    public String getAreaName() {
        return this.selectArea.get("AREA_NAME");
    }

    public String getAreaLevel() {
        return this.areaLevel;
    }

    public void clickIncomeDetails() {
        if ("4".equals(areaLevel)) {
            if (this.mDetailsELV.isGroupExpanded(1)) {
                this.mDetailsELV.collapseGroup(1);
            } else {
                this.mDetailsELV.expandGroup(1);
            }
        } else {
            if (this.mDetailsELV.isGroupExpanded(0)) {
                this.mDetailsELV.collapseGroup(0);
            } else {
                this.mDetailsELV.expandGroup(0);
            }
        }
        doScroll();
    }

    public void clickCostDetails() {
        if ("4".equals(areaLevel)) {
            if (this.mDetailsELV.isGroupExpanded(2)) {
                this.mDetailsELV.collapseGroup(2);
            } else {
                this.mDetailsELV.expandGroup(2);
            }
        } else {
            if (this.mDetailsELV.isGroupExpanded(1)) {
                this.mDetailsELV.collapseGroup(1);
            } else {
                this.mDetailsELV.expandGroup(1);
            }
        }
        doScroll();
    }

    public void doScroll() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                View child = scrollView.getChildAt(0);
                scrollView.smoothScrollTo(0, child.getMeasuredHeight()
                        - scrollView.getHeight());
            }
        }, 500);
    }

    private void setShowDate(String date) {
        if (date == null || "".equals(date) || "null".equals(date)) {
            if (this.calendar == null) {
                date = DateRangeEnum.MONTH.getDateSpecified(-1,
                        DateRangeEnum.MONTH.getDateServerPattern());
            } else {
                date = DateUtil.formatter(this.calendar.getTime(),
                        DateRangeEnum.MONTH.getDateServerPattern());
            }
        }
        questMap.put(KEY_DATE, date);
        this.dateSelect.setText(DateUtil.oneStringToAntherString(date,
                DateRangeEnum.MONTH.getDateServerPattern(),
                DateRangeEnum.MONTH.getDateShowPattern()));
    }

    private void setAreaName() {
        String areaName = null;
        String areaCode = null;
        if (this.selectArea == null) {
            areaName = "--";
        } else {
            areaName = this.selectArea.get("AREA_NAME");
            if (areaName == null) {
                areaName = "--";
            } else {

            }
            areaCode = this.selectArea.get("AREA_CODE");
            questMap.put(KEY_AREA_CODE, areaCode);
        }
        this.areaSelect.setText(areaName);
    }

    protected void chooseDate() {
        if (this.selectArea != null && this.selectArea.get("AREA_NAME") != null) {
            if (datePicker != null && datePicker.isShowing()) {
                datePicker.dismiss();
            }
            if (this.calendar == null) {
                this.calendar = DateUtil.getCalendar(this.dateSelect.getText()
                        .toString(), DateRangeEnum.MONTH.getDateShowPattern());
            } else {

            }
            // datePicker = new MyDatePickerDialog(this, new OnDateSetListener()
            // {
            // @Override
            // public void onDateSet(DatePicker view, int year,
            // int monthOfYear, int dayOfMonth) {
            // // 两秒之内的操作不能重复
            // long current_time = System.currentTimeMillis();
            // if (current_time - getTime() < 2000) {
            // return;
            // }
            //
            // setTime(current_time);
            //
            // calendar.set(year, monthOfYear, dayOfMonth);
            // String date = DateUtil.formatter(calendar.getTime(),
            // DateRangeEnum.MONTH.getDateServerPattern());
            // setShowDate(date);
            // questServer();
            // }
            // }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            // calendar.get(Calendar.DAY_OF_MONTH));

            OnDateSetListener listener = new OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    // 两秒之内的操作不能重复
                    long current_time = System.currentTimeMillis();
                    if (current_time - getTime() < 2000) {
                        return;
                    }

                    setTime(current_time);

                    calendar.set(year, monthOfYear, dayOfMonth);
                    String date = DateUtil.formatter(
                            calendar.getTime(),
                            DateRangeEnum.MONTH.getDateServerPattern());
                    setShowDate(date);
                    questServer();
                }
            };

            if (Build.VERSION.SDK_INT >= 24) {


                final Context themedContext = new ContextThemeWrapper(this,
                        // android.R.style.Widget_Material_NumberPicker
                        android.R.style.Theme_Holo_Light_Dialog);

                datePicker = new FixedHoloDatePickerDialog(themedContext, listener
                        , calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));

                ((FixedHoloDatePickerDialog) datePicker).setHasNoDay(true);

            } else
                datePicker = new MyDatePickerDialogWithoutDay(this, listener
                        , calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));

            datePicker.setTitle("请选择时间");
            datePicker.show();
        } else {

        }
    }

    protected void chooseArea() {
        if (this.selectArea != null && this.selectArea.get("AREA_NAME") != null) {
            final String[] areaName = new String[this.areaInfo.size()];
            for (int i = 0; i < this.areaInfo.size(); i++) {
                areaName[i] = this.areaInfo.get(i).get("AREA_NAME");
            }

            Context c = UIUtil.resolveDialogTheme(this);
            new AlertDialog.Builder(c).setTitle(R.string.hint)
                    .setItems(areaName, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectArea = areaInfo.get(which);
                            setAreaName();
                            questServer();
                        }
                    }).show();
        } else {

        }

    }

    private synchronized void setTime(long time) {
        this.last_select_time = time;
    }

    private synchronized long getTime() {
        return last_select_time;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.bonc.mobile.hbmclient.activity.MenuActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        if (this.mAsynTask.isCancelled()) {

        } else {
            this.mAsynTask.cancel(true);
        }
        super.onDestroy();
    }

}
