package com.bonc.mobile.hbmclient.activity;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.kpi.FileReportActivity;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.FileUtils;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.SlideHolder;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

public class RVReportListActivity extends FileReportActivity implements
        OnItemClickListener {

    protected SlideHolder slideHolder;
    protected ExpandableListView side_menu;
    protected FrameLayout main_content;

    BusinessDao business = new BusinessDao();

    String time = null;

    String text;
    Date stime, etime;

    @Override
    protected void doInit() {
        setContentView(R.layout.activity_slideholder);
        initSlideHolder();
        initView();
        loadData();
    }


    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        UIUtil.showGuideWindow(this, findViewById(android.R.id.content), "guide.ribao",
                new int[]{R.mipmap.ribao_reflesh});
    }

    private void initSlideHolder() {
        slideHolder = (SlideHolder) findViewById(R.id.slideHolder);
        slideHolder.setEnabled(false);
        side_menu = (ExpandableListView) findViewById(R.id.id_sidemenu);
        this.side_menu.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true;
            }
        });
        main_content = (FrameLayout) findViewById(R.id.main_content);
        main_content.setBackgroundDrawable(WatermarkImage
                .getWatermarkDrawable());
        main_content.removeAllViews();
        View v = View.inflate(this, R.layout.activity_report_list, null);
        main_content.addView(v);

    }

    @Override
    protected BaseConfigLoader getConfigLoader() {
        return new RVLoader(this);
    }

    @Override
    protected String getQueryAction() {
        return "/textReport/daily";
    }

    @Override
    protected String getBasePath() {
        return Constant.BASE_PATH;
    }

    @Override
    protected void initBaseData() {
        menuCode = getIntent().getStringExtra(MenuEntryAdapter.KEY_MENU_CODE);
    }

    @Override
    protected void initView() {

        super.initView();

        isNeedReflesh = true;
        if (isNeedReflesh) {
            this.findViewById(R.id.list).setVisibility(View.GONE);
            this.findViewById(R.id.layout_pulltorefresh).setVisibility(View.VISIBLE);
            refreshLayout = (PullToRefreshLayout) this.findViewById(R.id.layout_pulltorefresh);
            mList = (ListView) this.findViewById(R.id.list_pulltorefresh);
        }

        refreshLayout.setCanRefresh(false);
        //hide the more cilck
        ((View) this.findViewById(com.bonc.mobile.common.R.id.button_more).getParent()).setVisibility(View.GONE);

        findViewById(R.id.id_share).setVisibility(View.VISIBLE);

        findViewById(R.id.id_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtils.shareScreen(RVReportListActivity.this);
            }
        });


        TextViewUtils.setText(this, R.id.title, business.getMenuName(menuCode));

        findViewById(R.id.id_button_logo).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        slideHolder.toggle();
                    }
                });
        findViewById(R.id.search_iv).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // slideHolder.toggle();

                Log.d("AAA", "t:" + time);
                // date参数为最新一条消息的日期
                showSearchDialog(time == null ? new Date() : DateUtil.getDate(
                        time, DateUtil.PATTERN_9));

            }
        });

        new MenuEntryAdapter(this, side_menu, menuCode);
        stime = null;
        text = null;
        etime = null;
        page = 1;
        mList.setOnItemClickListener(this);

        LogUtils.toast(RVReportListActivity.this, refreshLayout != null);
        if (refreshLayout != null) {
            refreshLayout.setRefreshListener(new BaseRefreshListener() {
                @Override
                public void refresh() {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 结束加载更多
                            refreshLayout.finishRefresh();
                        }
                    }, 200);
                }

                @Override
                public void loadMore() {


                    RVReportListActivity.this.findViewById(com.bonc.mobile.common.R.id.button_more).performClick();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 结束加载更多
                            refreshLayout.finishLoadMore();
                        }
                    }, 500);
                }
            });
        }
    }

    @Override
    protected void onSearch(String text, Date start, Date end) {
        if (start != null && end != null) {
            String s = DateUtil.formatter(start, DateUtil.PATTERN_MODEL2_10);
            String e = DateUtil.formatter(end, DateUtil.PATTERN_MODEL2_10);

            int b = s.compareTo(e);
            if (b > 0) {
                showToast("日期选择错误");
                return;
            }
        }
        page = 1;
        this.text = text;
        stime = start;
        etime = end;
        mData.clear();
        loadData();
    }

    @Override
    public void onBackPressed() {

        if (slideHolder.isOpened()) {
            slideHolder.close();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void loadData() {

        Map<String, String> param = new LinkedHashMap<String, String>();

        param.put("menuCode", menuCode);
        param.put("page", String.valueOf(page));

        param.put("clickCode", menuCode);
        param.put("appType", "BI_ANDROID");
        param.put("clickType", "MENU");
        // param.put("userCode", "");aa

        if (text != null)
            param.put("title", text);
        param.put(
                "stime",
                stime == null ? null : DateUtil.formatter(stime,
                        DateUtil.PATTERN_9));
        param.put(
                "etime",
                etime == null ? null : DateUtil.formatter(etime,
                        DateUtil.PATTERN_9));

        new LoadDataTask(this, Constant.BASE_PATH + "/bi").execute(
                getQueryAction(), param);

    }

    SimpleAdapter adapter;

    @Override
    protected void bindData(JSONObject result) {

        List<Map<String, String>> data;
        try {
            data = JsonUtil.toList(JsonUtil.optJSONArray(
                    result.getJSONObject("data"), "rows"));

            if (data.size() == 0) {
                showToast(getString(R.string.no_more_data));
                if (adapter != null)
                    adapter.notifyDataSetChanged();
                return;
            }
            time = data.get(0).get("OP_TIME");

            for (Map<String, String> m : data) {
                m.put("title", m.get("TITLE"));
            }
            mData.addAll(data);
            if (adapter != null)
                adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (adapter == null)
            adapter = new SimpleAdapter(this, mData, R.layout.report_list_item,
                    new String[]{"title"}, new int[]{R.id.text});
        mList.setAdapter(adapter);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        Map<String, String> m = (Map<String, String>) mList.getAdapter()
                .getItem(position);

        Intent intent = new Intent(RVReportListActivity.this,
                BIHtmlReportActivity_reflesh.class);
        intent.putExtra("title", m.get("TITLE"));
        intent.putExtra("content", m.get("HTML_CONTENT"));
        intent.putExtra("id", m.get("ID"));
        startActivity(intent);
    }

    public class RVLoader extends BaseConfigLoader {

        protected RVLoader(Context context) {
            super(context);
        }

        @Override
        public String getMenuName(String menuCode) {
            return business.getMenuName(menuCode);
        }

    }

}
