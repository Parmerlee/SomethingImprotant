
package com.bonc.mobile.common.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.View;
import android.widget.ImageView;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.data.DatabaseManager;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.common.view.adapter.SimplePagerAdapter;

public abstract class AnnounceActivity extends BaseActivity {
    protected ViewPager pager;
    protected ImageView front_item, next_item;
    protected boolean showUnread;
    protected List<Map<String, String>> mData;
    protected DatabaseManager dbMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbMan = new DatabaseManager(this);
        showUnread = getIntent().getBooleanExtra("showUnread", false);
        setContentView(R.layout.activity_announce);
        initView();
        bindData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbMan.close();
    }

    protected void initView() {
        UIUtil.setWindowLayout(getWindow(), 0.95f, 0.6f);
        pager = (ViewPager) findViewById(R.id.pager);
        front_item = (ImageView) findViewById(R.id.front_item);
        next_item = (ImageView) findViewById(R.id.next_item);
    }

    protected void bindData() {
        List<Map<String, String>> data = getConfigLoader().getAnnounce();
        Map<String, String> announceStatus = getConfigLoader().loadAnnounceStatus(dbMan);
        if (showUnread) {
            List<Map<String, String>> unread = new ArrayList<Map<String, String>>();
            for (Map<String, String> m : data) {
                if (!announceStatus.containsKey(m.get(BaseConfigLoader.KEY_FLOW_ID))
                        && beforeExpire(m.get("NOTICE_EXPIRE_DATE")))
                    unread.add(m);
            }
            data = unread;
        }
        mData = data;
        if (data.size() == 0) {
            if (!showUnread)
                showToast(getString(R.string.no_data));
            finish();
            return;
        }
        List<View> viewList = new ArrayList<View>();
        for (Map<String, String> m : data) {
            View v = View.inflate(this, R.layout.announce_pager_item, null);
            String create_date = DateUtil.oneStringToAntherString(m.get("NOTICE_PUBLISH_DATE"),
                    DateUtil.PATTERN_8, "yyyy年MM月dd日");
            if (!announceStatus.containsKey(m.get(BaseConfigLoader.KEY_FLOW_ID)))
                create_date += "(未读)";
            TextViewUtils.setText(v, R.id.msg_date, create_date);
            TextViewUtils.setText(v, R.id.text, m.get("MSG"));
            viewList.add(v);
        }
        pager.setAdapter(new SimplePagerAdapter(viewList));
        pager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                updateAnnounceStatus(position);
                updateIcon(position);
            }
        });
        updateAnnounceStatus(0);
        updateIcon(0);
    }

    boolean beforeExpire(String s) {
        Date date = DateUtil.getDate(s, DateUtil.PATTERN_8);
        if (date == null)
            return true;
        return new Date().before(date);
    }

    void updateAnnounceStatus(int position) {
        getConfigLoader().updateAnnounceStatus(dbMan,
                mData.get(position).get(BaseConfigLoader.KEY_FLOW_ID));
    }

    void updateIcon(int position) {
        front_item.setImageResource(position == 0 ? R.mipmap.icon_frontitem
                : R.mipmap.icon_frontitem1);
        next_item.setImageResource(position == mData.size() - 1 ? R.mipmap.icon_nexttitem
                : R.mipmap.icon_nexttitem1);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.front_item) {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        } else if (v.getId() == R.id.next_item) {
            pager.setCurrentItem(pager.getCurrentItem() + 1);
        }
    }

    protected abstract BaseConfigLoader getConfigLoader();
}
