/**
 * ExpandableListViewState
 */
package common.share.lwg.util.state.kpilist;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.view.MyExpandableListView;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.MainKpiActivity;
import com.bonc.mobile.hbmclient.util.UIUtil;
import com.bonc.mobile.hbmclient.view.ListViewSetting;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

import common.share.lwg.util.mediator.proxy_impl.mainkpi.MainKpiMediator;
import common.share.lwg.util.mediator.proxy_impl.mainkpi.ViewColleague;

/**
 * @author liweigao
 */
public class ExpandableListViewState extends AKpiList {
    private ExpandableListView elvLeft;
    private ExpandableListView elvRight;
    private ELVLeftAdapter leftAdapter;
    private ELVRightAdapter rightAdapter;

    /**
     * @param machine
     */
    public ExpandableListViewState(ViewColleague machine) {
        super(machine);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     *
     * @see common.share.lwg.util.state.kpilist.AKpiList#addView()
     */
    @Override
    protected void addView() {
        Activity activity = this.machine.getActivity();
        RelativeLayout containerLeft = (RelativeLayout) activity
                .findViewById(R.id.listContainerLeft);
        RelativeLayout containerRight = (RelativeLayout) activity
                .findViewById(R.id.listContainerRight);

        String meunCode = ((MainKpiActivity) activity).getMenuCode();

        LogUtils.toast(activity, "menuCode:" + meunCode);
        LogUtils.logBySys("menuCode:" + meunCode + "activity:" + activity.getClass().getName());

//        if (UIUtil.isNeedNew(meunCode)) {
//            this.elvLeft = new MyExpandableListView(activity);
//            this.elvRight = new MyExpandableListView(activity);
//        } else {
            this.elvLeft = new ExpandableListView(activity);
            this.elvRight = new ExpandableListView(activity);
//        }
        setELV(elvLeft);
        setELV(elvRight);
        containerLeft.addView(elvLeft, LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        containerRight.addView(elvRight, LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        ListViewSetting mListViewSetting = new ListViewSetting();
        mListViewSetting.setListViewOnTouchAndScrollListener(this.elvLeft,
                this.elvRight);
    }

    private void setELV(ExpandableListView elv) {
        elv.setGroupIndicator(null);
        LayoutInflater inflater = LayoutInflater.from(elv.getContext());
        View v = inflater.inflate(R.layout.divider_hor, null);
        elv.addFooterView(v, null, false);
        elv.setVerticalScrollBarEnabled(false);
    }

    //展开所有的Group
    private void expandELV(ExpandableListView elv,
                           BaseExpandableListAdapter adapter) {
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            elv.expandGroup(i);
        }
    }

    private int getInt(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(
                key, -1);
    }

    /*
     * (non-Javadoc)
     *
     * @see common.share.lwg.util.state.kpilist.AKpiList#updateView()
     */
    @Override
    protected void updateView() {
        MainKpiMediator mediator = (MainKpiMediator) this.machine.getMediator();
        this.leftAdapter = new ELVLeftAdapter(mediator.getDataColleague());
        this.elvLeft.setAdapter(leftAdapter);
        expandELV(elvLeft, leftAdapter);
        blockGroupClick(elvLeft);

        this.rightAdapter = new ELVRightAdapter(mediator.getDataColleague());
        this.elvRight.setAdapter(rightAdapter);
        expandELV(elvRight, rightAdapter);

        blockGroupClick(elvRight);
        int location = getInt(this.machine.getActivity(), "kpiCodeLocation");

        if (location != -1) {
            this.elvLeft.smoothScrollToPositionFromTop(Math.max(location, 0),
                    0, 1000);

            this.elvRight.smoothScrollToPositionFromTop(Math.max(location, 0),
                    0, 1000);

            this.elvLeft.setSelection(Math.max(location, 0));

            this.elvRight.setSelection(Math.max(location, 0));
        }
    }

    //屏蔽group的点击事件
    private void blockGroupClick(ExpandableListView elv) {
        elv.setOnGroupClickListener(new OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true;
            }
        });
    }

}
