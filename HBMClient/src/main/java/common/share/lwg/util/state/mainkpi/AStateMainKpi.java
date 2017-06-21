/**
 * AStateMainKpi
 */
package common.share.lwg.util.state.mainkpi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.MainKpiActivity;
import com.bonc.mobile.hbmclient.asyn_task.DailyReportAsynTask;
import com.bonc.mobile.hbmclient.command.business_outlets.AreaSelector2;
import com.bonc.mobile.hbmclient.command.business_outlets.AreaSelector2.OnAreaSelectListener;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector2;
import com.bonc.mobile.hbmclient.command.business_outlets.DateSelector2.OnDateSelectListener;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.spring.RecyclerAdapter;
import com.bonc.mobile.hbmclient.spring.TopDetailActivity;
import com.bonc.mobile.hbmclient.util.FileUtils;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.UIUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.BoardScrollView;
import com.bonc.mobile.hbmclient.view.BulletinView;
import com.bonc.mobile.hbmclient.view.Top10HorizontalScrollView;

import common.share.lwg.util.asyntask.QuestAsynTask;
import common.share.lwg.util.mediator.proxy_impl.mainkpi.MainKpiMediator;
import common.share.lwg.util.state.IState;

/**
 * @author liweigao
 */
public abstract class AStateMainKpi implements IState<DateRangeEnum> {
    protected MainKpiActivity machine;
    protected View contentView;
    protected MainKpiMediator mainKpiMediator;
    protected String menuCode;
    protected BusinessDao dao;
    protected Map<String, String> questMap = new HashMap<String, String>();
    protected QuestAsynTask questAsynTask;
    protected AreaSelector2 areaSelector2;
    protected DateSelector2 dateSelector2;

    private BulletinView mBulletinView;
    private Button navigator, share_data;
    private Button dateSelect, areaSelect;
    private TextView titleName;
    private boolean isFirst = true;

    public final static String KEY_MENU_CODE = "menuCode";
    public final static String KEY_AREA_CODE = "areaCode";
    public final static String KEY_OPTIME = "optime";
    public final static String KEY_DATE_TYPE = "dateType";

    private LinearLayout linearLayout;

    public AStateMainKpi(MainKpiActivity machine, String menuCode) {
        this.machine = machine;
        this.menuCode = menuCode;
        this.dao = new BusinessDao();
        this.mainKpiMediator = new MainKpiMediator();
        this.mainKpiMediator.createColleagues(this.machine);
        this.areaSelector2 = new AreaSelector2(machine, "AREA_NAME",
                "AREA_CODE", "AREA_LEVEL");
        this.dateSelector2 = new DateSelector2(machine, getStateFlag());
    }

    private void setListener() {
        this.dateSelector2.setWithoutDay(machine.hasNoDay);

        this.areaSelector2.setOnAreaSelectListener(new OnAreaSelectListener() {

            @Override
            public void onAreaSelect(AreaSelector2 areaSelector2) {
                String areaName = areaSelector2.getAreaName();
                areaSelect.setText(areaName);
                questData();
            }
        });
        this.areaSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                areaSelector2.chooseArea();
            }
        });
        this.dateSelector2.setOnDateSelectListener(new OnDateSelectListener() {

            @Override
            public void onDateSelect(DateSelector2 ds) {
                String showDate = ds.getShowDate();
                dateSelect.setText(showDate);
                questData();
            }
        });
        this.dateSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dateSelector2.chooseDate();
            }
        });
        this.navigator.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                machine.slideHolder.toggle();
            }
        });
        this.share_data.setVisibility(View.VISIBLE);
        this.share_data.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FileUtils.shareScreen(machine);
            }
        });
    }

    RelativeLayout rootRelativeLayout;

    /*
     * (non-Javadoc)
     *
     * @see common.share.lwg.util.state.IState#enter()
     */
    @Override
    public void enter() {

//        LogUtils.toast(this.machine, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//        Toast.makeText(this.machine, "AAAAA", Toast.LENGTH_LONG).show();
        LogUtils.logBySys("menuCode:" + menuCode + "activity:" + machine.getClass().getName());
        if (this.contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.machine);
            this.contentView = inflater.inflate(UIUtil.isNeedNew(menuCode) ? R.layout.main_kpi_activity_spring : R.layout.main_kpi_activity,
                    null);


            if (UIUtil.isNeedNew(menuCode)) {


                //开门红转悠layout
                try {
                    linearLayout = (LinearLayout) contentView.findViewById(R.id.listview_layout);
                    recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView_one);
                    recyclerView2 = (RecyclerView) contentView.findViewById(R.id.recyclerView_two);
                    initRecyleView();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String parentMenuCode = dao.getParentMenuCode(this.menuCode);
            this.machine.setMainContent(parentMenuCode, contentView);
            dispatchView();
            firstEnter();
        } else {
            this.machine.setMainContent(contentView);
        }

    }


    Top10HorizontalScrollView mTop10HorizontalScrollView;
    ImageView iv_arrow_top10;

    RecyclerView recyclerView, recyclerView2;


    void initRecyleView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(machine);
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView2.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(machine);
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        recyclerView2.setLayoutManager(linearLayoutManager);

    }


    protected void firstEnter() {
        if (machine.getIntent().getBooleanExtra("hide_head", false)) {
            contentView.findViewById(R.id.id_mainkpi_title).setVisibility(
                    View.GONE);
        }
        this.questMap.put(AStateMainKpi.KEY_MENU_CODE, this.menuCode);
        this.questMap.put(AStateMainKpi.KEY_DATE_TYPE, getStateFlag()
                .getDateFlag());
        this.questData();
    }

    protected void setFirstArea() {
        this.areaSelector2.setSelectArea(0);
        String areaName = this.areaSelector2.getAreaName();
        this.areaSelect.setText(areaName);
    }

    protected void setFirstDate(String date) {
        if (date != null && date.length() == 8) {
            DataUtil.saveSetting(
                    PreferenceManager.getDefaultSharedPreferences(this.machine),
                    "kpi_last_date", date);
            DataUtil.saveSetting(
                    PreferenceManager.getDefaultSharedPreferences(this.machine),
                    "kpi_cur_date", date);
        }
        this.dateSelector2.setDate(date);
        String showDate = this.dateSelector2.getShowDate();
        this.dateSelect.setText(showDate);
    }

    public void questData() {
        this.questAsynTask = new QuestAsynTask(this.machine) {

            @Override
            public void onPost(Object result) {
                parseData(result);
            }

        };
        this.questMap.put(AStateMainKpi.KEY_AREA_CODE,
                this.areaSelector2.getAreaCode());
        this.questMap.put(AStateMainKpi.KEY_OPTIME,
                this.dateSelector2.getServerDate());
        if (dateSelector2.getServerDate() != null
                && dateSelector2.getServerDate().length() == 8)
            DataUtil.saveSetting(
                    PreferenceManager.getDefaultSharedPreferences(this.machine),
                    "kpi_cur_date", dateSelector2.getServerDate());
        if (this.machine != null)
            this.questAsynTask.execute(ActionConstant.GET_MAIN_KPI_DATA,
                    questMap);


//        if (UIUtil.isNeedNew(menuCode))
//            parseTop10Date(FileUtils.getFromAssets(machine, "json.txt"));
    }

    private void parseData(Object result) {
        try {
            JSONObject resultJO = null;
            try {
                resultJO = new JSONObject((String) result);
            } catch (Exception e) {
                Toast.makeText(machine, "没有数据返回", Toast.LENGTH_SHORT).show();
                resultJO = new JSONObject();
            }

            JSONArray areaJA = resultJO.optJSONArray("authArea");
            if (areaJA == null) {
                areaJA = new JSONArray();
            }
            List<Map<String, String>> areaInfo = JsonUtil.toDataList(areaJA);
            this.areaSelector2.setAreaInfo(areaInfo);
            if (isFirst) {
                if (mBulletinView != null) {
                    new DailyReportAsynTask(mBulletinView, "点击查看文字说明")
                            .execute();
                }
                setFirstArea();
                String date = resultJO.getString("optime");
                setFirstDate(date);
                isFirst = false;
            } else {

            }

            if (UIUtil.isNeedNew(menuCode)) {
                reSizeLinearLayout(resultJO);
            }
            this.mainKpiMediator.setData(resultJO);
        } catch (Exception e) {
            Toast.makeText(machine, "数据加载异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        QuestAsynTask top10Task = new QuestAsynTask(this.machine, "/bi/phased/") {
            @Override
            public void onPost(Object result) {

                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    if (!jsonObject.getBoolean("flag")) {
                        Toast.makeText(machine, jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        parseTop10Date(result.toString());
                        LogUtils.toast(machine, jsonObject.getString("msg"));
                    }
                } catch (Exception e) {
//                    Toast.makeText(machine, "数据加载异常", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        };
        if (this.questMap != null) {
            this.questMap.put("time", this.dateSelector2.getServerDate());
            this.questMap.remove(AStateMainKpi.KEY_OPTIME);
            questMap.put("menuCode", String.valueOf(questMap.get("menuCode")));
        }
        if (this.machine != null && UIUtil.isNeedNew(menuCode))
            top10Task.execute("goodStart", questMap);
    }

    Map<String, String> areaMap = new HashMap<>();

    private void parseTop10Date(String result) {

        List<Map<String, String>> data = buildRecyleDate(result, "top10");
        RecyclerAdapter adapter = new RecyclerAdapter(machine, data);
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.setAdapter(adapter);
        adapter.setmOnItemClickListener(listener);
        adapter = new RecyclerAdapter(machine, buildRecyleDate(result, "end10"));
        recyclerView2.setVisibility(View.VISIBLE);
        recyclerView2.setAdapter(adapter);
        adapter.setmOnItemClickListener(listener);
        recyclerView.setOnScrollListener(scrollListener);
        recyclerView2.setOnScrollListener(scrollListener);

        boardScrollView = (BoardScrollView) machine.findViewById(R.id.scroll_spring);

        boardScrollView.fullScroll(ScrollView.FOCUS_UP);

        startArrowAni();

    }

    BoardScrollView boardScrollView;
    ImageView iv_arrow_down;
    AlphaAnimation alphaAnimation;

    protected void startArrowAni() {

        iv_arrow_down = (ImageView) machine
                .findViewById(R.id.id_arrow_down);
//        Animation ani_alpha_change = AnimationUtils.loadAnimation(machine,
//                R.anim.alpha_change);
//        ani_alpha_change.setRepeatCount(Integer.MAX_VALUE);

        alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setRepeatCount(Integer.MAX_VALUE);
        alphaAnimation.setDuration(1200);
        alphaAnimation.setRepeatMode(Animation.RESTART);

        if (this.boardScrollView.getContentView().getMeasuredHeight() <= this.boardScrollView
                .getScrollY() + this.boardScrollView.getHeight()) {
            iv_arrow_down.setVisibility(View.INVISIBLE);
            iv_arrow_down.clearAnimation();
        } else {
            iv_arrow_down.setVisibility(View.VISIBLE);
            iv_arrow_down.startAnimation(alphaAnimation);
        }
        boardScrollView.setOnBorderListener(new BoardScrollView.OnBorderListener() {
            @Override
            public void onBottom() {
                iv_arrow_down.setVisibility(View.INVISIBLE);
                iv_arrow_down.clearAnimation();
            }

            @Override
            public void onNotBottom() {
                iv_arrow_down.startAnimation(alphaAnimation);
                iv_arrow_down.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTop() {

            }
        });
    }

    private List<Map<String, String>> buildRecyleDate(String result, String keyName) {
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray array = jsonObject.getJSONArray(keyName);
            if (array.length() == 0) {
                for (int j = 0; j < 10; j++) {
                    map = new HashMap<>();
                    map.put("top10", keyName.substring(0, 1).toUpperCase() + keyName.substring(1));
                    map.put("rank", "--");
                    map.put("modelName", "--");
                    map.put("value1", "--");
                    map.put("value2", "--");
                    map.put("value3", "--");
                    map.put("clickable", "false");
                    data.add(map);
                }
            }
            areaMap = JsonUtil.toMap((JSONObject) jsonObject.getJSONArray("areaCode").get(0));
            areaMap.put("time", jsonObject.getString("time").toString());
            for (int i = 0; i < array.length(); i++) {
                map = new HashMap<>();
                JSONObject obj = (JSONObject) array.get(i);
                if ((!obj.has("rank")) && obj.toString().isEmpty()) {
                    map.put("top10", keyName.substring(0, 1).toUpperCase() + keyName.substring(1));
                    map.put("rank", "--");
                    map.put("modelName", "--");
                    map.put("value1", "--");
                    map.put("value2", "--");
                    map.put("value3", "--");
                    map.put("clickable", "false");

                    data.add(map);
                } else {
                    map.put("top10", keyName.substring(0, 1).toUpperCase() + keyName.substring(1));
                    map.put("rank", obj.getString("RANK"));
                    map.put("modelName", obj.getString("FEE_NAME"));
                    map.put("value1", obj.getString("TOTAL_VALUE"));
                    map.put("value2", obj.getString("DAILY_VALUE"));
                    map.put("value3", obj.getString("TRANS_VALUE"));
                    map.put("clickable", "true");
//                    map.put("firstRank", ((JSONObject) array.get(0)).getString("RANK"));
                    data.add(map);
                }
            }
            LogUtils.logBySys(array.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            for (int j = 0; j < 10; j++) {
                map = new HashMap<>();
                map.put("top10", keyName.substring(0, 1).toUpperCase() + keyName.substring(1));
                map.put("rank", "--");
                map.put("modelName", "--");
                map.put("value1", "--");
                map.put("value2", "--");
                map.put("value3", "--");
                map.put("clickable", "false");
                data.add(map);
            }
        }
        return data;
    }

    RecyclerAdapter.OnRecyclerViewItemClickListener listener = new RecyclerAdapter.OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View view, Map data) {
            if (!TextUtils.equals(data.get("clickable").toString(), "true")) {
                Toast.makeText(machine, "该排名暂无数据，请查看其它排名", Toast.LENGTH_LONG).show();
                return;
            }
            LogUtils.toast(machine, "AAA:" + data.toString());
            Intent intent = new Intent(machine, TopDetailActivity.class);
            intent.putExtra("map", questMap.toString());
            intent.putExtra("areaMap", areaMap.toString());
            intent.putExtra("itemData", data.toString());
            machine.startActivity(intent);

        }
    };
    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerview, int dx, int dy) {
            super.onScrolled(recyclerview, dx, dy);
            if (dy != 0) {
                return;
            }
            ViewGroup view = (ViewGroup) recyclerview.getParent();
            View left = view.getChildAt(0);
            View right = view.getChildAt(2);

            right.startAnimation(alphaAnimation);

            //true 表示还能向右滑动
            if (recyclerview.canScrollHorizontally(1)) {

                right.setVisibility(View.VISIBLE);
                right.startAnimation(alphaAnimation);
            } else {
                right.setVisibility(View.INVISIBLE);
                right.clearAnimation();

            }
            if (recyclerview.canScrollHorizontally(-1)) {
                left.setVisibility(View.VISIBLE);
            } else {
                left.setVisibility(View.INVISIBLE);
            }
        }
    };


    private void reSizeLinearLayout(JSONObject resultJO) {

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();

        //15
        int num = resultJO.optJSONObject("kpiInfo").length();
        LogUtils.logBySys("kpiInfo num;" + num);
        //3
        num += resultJO.optJSONArray("composite").length();
        LogUtils.logBySys("composite num;" + num);
        params.height = com.bonc.mobile.common.util.UIUtil.fromDPtoPX(machine, 50 * num);
        linearLayout.setLayoutParams(params);
    }

    private void dispatchView() {
        if (!machine.getIntent().getBooleanExtra("hide_head", false)) {
            contentView.findViewById(R.id.root).setBackgroundDrawable(
                    WatermarkImage.getWatermarkDrawable());
        }
        this.navigator = (Button) contentView.findViewById(R.id.id_navigator);
        share_data = (Button) contentView.findViewById(R.id.id_share);
        this.titleName = (TextView) contentView
                .findViewById(R.id.logo_word_mon_dev);
        this.areaSelect = (Button) contentView.findViewById(R.id.id_button1);
        this.dateSelect = (Button) contentView.findViewById(R.id.id_button2);

        Map<String, String> map = dao.getMenuStateInfo(menuCode);
        String menuName = map.get("MENU_NAME");
        this.titleName.setText(menuName);
        String showDailyReport = map.get("show_daily_report");
        if ("1".equals(showDailyReport)) {
            RelativeLayout rl = (RelativeLayout) contentView
                    .findViewById(R.id.id_mainkpi_title);
            LayoutParams lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp.rightMargin = machine.getResources().getDimensionPixelSize(
                    R.dimen.daily_report_right_margin);
            String parentMenuCode = map.get("PARENT_MENU_CODE");
            mBulletinView = new BulletinView(machine, parentMenuCode);
            rl.addView(mBulletinView, lp);
        }

        setListener();
    }

    /*
     * (non-Javadoc)
     *
     * @see common.share.lwg.util.state.IState#changeState()
     */
    @Override
    public void changeState() {
        // TODO Auto-generated method stub

    }

}
