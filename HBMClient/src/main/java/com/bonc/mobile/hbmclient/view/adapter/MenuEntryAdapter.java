package com.bonc.mobile.hbmclient.view.adapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.AnnounceMgrActivity;
import com.bonc.mobile.hbmclient.activity.BroadActivity;
import com.bonc.mobile.hbmclient.activity.CertQueryActivity;
import com.bonc.mobile.hbmclient.activity.ChannelAnalyzeActivity;
import com.bonc.mobile.hbmclient.activity.DailyReportActivity;
import com.bonc.mobile.hbmclient.activity.DataArriveQueryActivity;
import com.bonc.mobile.hbmclient.activity.DataPresentationActivity;
import com.bonc.mobile.hbmclient.activity.GreatBeginActivity;
import com.bonc.mobile.hbmclient.activity.KPIHomeActivity;
import com.bonc.mobile.hbmclient.activity.KpiTabMainActivity;
import com.bonc.mobile.hbmclient.activity.LoginStatisticsActivity;
import com.bonc.mobile.hbmclient.activity.MainKpiActivity;
import com.bonc.mobile.hbmclient.activity.Password;
import com.bonc.mobile.hbmclient.activity.PortalActivity;
import com.bonc.mobile.hbmclient.activity.RVReportListActivity;
import com.bonc.mobile.hbmclient.activity.SaleBIActivity;
import com.bonc.mobile.hbmclient.activity.SimpleTerminalActivity;
import com.bonc.mobile.hbmclient.activity.UnLoadDataSecondActivity;
import com.bonc.mobile.hbmclient.activity2.BaseCordovaActivity;
import com.bonc.mobile.hbmclient.activity2.BroadBandCordovaActivity;
import com.bonc.mobile.hbmclient.activity2.GEMarketActivity;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.enum_type.MenuEnum;
import com.bonc.mobile.hbmclient.terminal.TerminalGroupActivity;
import com.bonc.mobile.hbmclient.terminal.view.InitialPassword;
import com.bonc.mobile.hbmclient.terminal.view.ScanAnnouncement;
import com.bonc.mobile.saleclient.activity.SaleMainActivity;

import common.share.lwg.util.mediator.proxy_impl.port.GetNewInfo;

public class MenuEntryAdapter {
    private Activity a;
    // private SQLHelper sqlHelper = new SQLHelper();

    private ScanAnnouncement mSA;
    private InitialPassword mIP;
    // private SharedPreferences sharedPreferences;

    public final static String KEY_MENU_CODE = "key_menu_code";

    public MenuEntryAdapter(Activity a, ScanAnnouncement sa,
                            InitialPassword ip, ListView lv, List<Map<String, String>> lv_data) {
        this.a = a;
        this.mSA = sa;
        this.mIP = ip;
        // this.sharedPreferences = a.getSharedPreferences("SETTING",
        // Context.MODE_WORLD_WRITEABLE);
        initialListView(lv, lv_data);
    }

    private void initialListView(ListView lv,
                                 final List<Map<String, String>> lv_data) {
        // 绘制Adapter并展示
        lv.setAdapter(new ListViewHomeAdapter(lv_data));
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                String type = lv_data.get(position).get("menuType");
                String menuCode = lv_data.get(position).get("menuCode");
                /*
                 * SharedPreferences.Editor editor = sharedPreferences.edit();
				 * editor.putInt(GroupColleague.KEY_PRE + menuCode, 1);
				 * editor.commit();
				 */
                GetNewInfo.getSingleInstance().insertScan(menuCode);

                if ("2".equalsIgnoreCase(type)) // 终端类业务
                {
                    Intent intent = new Intent(a, TerminalGroupActivity.class);
                    // Intent intent = new Intent(a,
                    // RVReportListActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                    a.overridePendingTransition(0, 0);
                } else if ("1".equalsIgnoreCase(type)) {
                    // 财务月报 宽带 流量,kpi类业务.
                    List<Map<String, String>> sencodeMenuList = new BusinessDao()
                            .getMenuSecond(menuCode);

                    // 如果有二级菜单 则转向二级菜单页面 . 否则转向指标显示的首页.
                    if (sencodeMenuList != null && sencodeMenuList.size() > 0) {
                        Intent i = new Intent(a, UnLoadDataSecondActivity.class);
                        i.putExtra(KEY_MENU_CODE, menuCode);
                        a.startActivity(i);
                        a.overridePendingTransition(0, 0);

                    } else {
                        Intent intent = new Intent(a, KPIHomeActivity.class);
                        intent.putExtra(KEY_MENU_CODE, menuCode);
                        a.startActivity(intent);
                        a.overridePendingTransition(0, 0);
                    }

                } else if ("3".equalsIgnoreCase(type)) {
                    // 特殊权限查看登录记录.
                    Intent intent = new Intent(a, LoginStatisticsActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                    a.overridePendingTransition(0, 0);
                } else if ("4".equalsIgnoreCase(type)) {
                    // 管理员特殊权限恢复用户到原始密码.

                    // if (!AppConstant.SEC_ENH) {
                    // Intent intent = new Intent(a, GEMarketActivity.class);
                    // intent.putExtra(KEY_MENU_CODE, menuCode);
                    // a.startActivity(intent);
                    // } else {
                    mIP.setMenuCode(menuCode);
                    mIP.showConfigGoBackP();
                    // }
                } else if ("5".equalsIgnoreCase(type)) {
                    Intent intent = new Intent(a, AnnounceMgrActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                } else if ("6".equalsIgnoreCase(type)) {
                    // 修改密码
                    Intent intent = new Intent(a, Password.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                    a.overridePendingTransition(0, 0);
                } else if ("7".equalsIgnoreCase(type)) {
                    Intent intent = new Intent(a, CertQueryActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                    a.overridePendingTransition(0, 0);
                } else if ("8".equalsIgnoreCase(type)) {
                    Intent intent = new Intent(a, DataArriveQueryActivity.class);
                    // if (!AppConstant.SEC_ENH) {
                    // intent = new Intent(a, GEMarketActivity.class);
                    // }
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                    a.overridePendingTransition(0, 0);
                } else if ("9".equalsIgnoreCase(type)) {
                    // 数据简报界面
                    Intent intent = new Intent(a,
                            DataPresentationActivity.class);
                    // if (!AppConstant.SEC_ENH) {
                    // intent = new Intent(a, BroadBandCordovaActivity.class);
                    // }
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                    a.overridePendingTransition(0, 0);
                } else if ("10".equalsIgnoreCase(type)) {
                    // 4G简报
                    Intent intent = new Intent(a, DailyReportActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                    a.overridePendingTransition(0, 0);
                } else if ("11".equalsIgnoreCase(type)) {
                    // 终端类界面
                    Intent intent = new Intent(a, SimpleTerminalActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                    a.overridePendingTransition(0, 0);
                } else if ("12".equalsIgnoreCase(type)) {
                    // 新界面，进入显示页面 收入类
                    // System.out.println("menuCode:" + menuCode);
                    Intent intent = new Intent(a, MainKpiActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                    a.overridePendingTransition(0, 0);
                } else if ("13".equalsIgnoreCase(type)) {
                    // 有二级菜单的类型， 进入二级菜单界面
                    Intent intent = new Intent(a,
                            UnLoadDataSecondActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                    a.overridePendingTransition(0, 0);
                } else if ("14".equalsIgnoreCase(type)) {
                    // 渠道
                    Intent intent = new Intent(a, ChannelAnalyzeActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                    a.overridePendingTransition(0, 0);
                } else if ("15".equalsIgnoreCase(type)) { // 业务量类
                    Intent intent = new Intent(a, SaleBIActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                    a.overridePendingTransition(0, 0);
                } else if ("16".equalsIgnoreCase(type)) { // 地市日报
                    Intent intent = new Intent(a, RVReportListActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                    a.overridePendingTransition(0, 0);
                } else if ("17".equalsIgnoreCase(type)) {
                    Intent intent = new Intent(a, SaleMainActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                } else if ("18".equalsIgnoreCase(type)) {
                    // 新菜单  流量运营 2016年12月22日17:38:37
                    Intent intent = new Intent(a, KpiTabMainActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                } else if ("27".equalsIgnoreCase(type)) {
                    // 家庭宽带
                    Intent intent = new Intent(a, BroadActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                } else {

                    Intent intent = new Intent(a, getTargetClass(a, type,
                            menuCode));
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                }
            }
        });
    }

    private class ListViewHomeAdapter extends BaseAdapter {

        private List<Map<String, String>> outmoduleList;
        private GetNewInfo newInfo = GetNewInfo.getSingleInstance();

        public ListViewHomeAdapter(List<Map<String, String>> outmoduleList) {
            this.outmoduleList = outmoduleList;
        }

        @Override
        public int getCount() {
            return outmoduleList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(a).inflate(
                        R.layout.home_list_item, null);
                holder = new ViewHolder();
                holder.ll_item_logo = (LinearLayout) convertView
                        .findViewById(R.id.ll_item_logo);
                holder.textView1 = (TextView) convertView
                        .findViewById(R.id.home_item_tv1);
                holder.textView2 = (TextView) convertView
                        .findViewById(R.id.home_item_tv2);
                convertView.setTag(holder);
            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            int menuCode = 0;

            try {
                menuCode = Integer.parseInt(outmoduleList.get(position).get(
                        "menuCode"));
            } catch (Exception e) {
                e.printStackTrace();
                menuCode = 0;
            }
            LogUtils.logBySys("red menucode1:" + menuCode + ";new :" + this.newInfo.getAllNewMenu());
            if (this.newInfo.isNew(String.valueOf(menuCode))) {
                LogUtils.logBySys("red menucode2:" + menuCode + ";new :" + this.newInfo.getAllNewMenu());

                convertView.findViewById(R.id.ic_new).setVisibility(
                        View.VISIBLE);
            } else {
                convertView.findViewById(R.id.ic_new).setVisibility(
                        View.GONE);
            }
            /*
			 * String menuChange =
			 * outmoduleList.get(position).get("menuChange");
			 * if("1".equals(menuChange)) { int isNew =
			 * sharedPreferences.getInt(GroupColleague.KEY_PRE + menuCode, 0);
			 * if(isNew == 0) {
			 * convertView.findViewById(R.id.ic_new).setVisibility
			 * (View.VISIBLE); } else {
			 * 
			 * } } else {
			 * 
			 * }
			 */
            int imageid = MenuEnum.getIdIcon(menuCode);
            holder.ll_item_logo.setBackgroundResource(imageid);
            holder.textView1.setText(outmoduleList.get(position)
                    .get("menuName"));
            holder.textView2.setText(outmoduleList.get(position).get(
                    "menuExpalin"));
            return convertView;
        }

        private class ViewHolder {
            public LinearLayout ll_item_logo;// 更换模块图片
            public TextView textView1;// 模块名称
            public TextView textView2;// 模块介绍
        }
    }

    // 侧边栏导航
    public MenuEntryAdapter(Activity a, ExpandableListView elv, String menuCode) {
        this.a = a;
        initialExpandableListView(elv, menuCode);
    }

    private void initialExpandableListView(ExpandableListView elv,
                                           String menuCode) {
        SideMenuAdapter adapter = new SideMenuAdapter(a, menuCode);
        elv.setAdapter(adapter);
        int length = adapter.getGroupCount();
        for (int i = 0; i < length; i++) {
            elv.expandGroup(i);
        }
        elv.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                List<List<Map<String, String>>> childData = ((SideMenuAdapter) parent
                        .getExpandableListAdapter()).getChildData();
                String type = childData.get(groupPosition).get(childPosition)
                        .get("menuType");
                String menuCode = childData.get(groupPosition)
                        .get(childPosition).get("menuCode");

                if ("2".equalsIgnoreCase(type)) // 终端
                {
                    Intent intent = new Intent(a, TerminalGroupActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                    a.overridePendingTransition(0, 0);
                } else if ("1".equalsIgnoreCase(type)) {

                    List<Map<String, String>> sencodeMenuList = new BusinessDao()
                            .getMenuSecond(menuCode);

                    // 如果有二级菜单 则转向二级菜单页面 . 否则转向指标显示的首页.
                    if (sencodeMenuList != null && sencodeMenuList.size() > 0) {
                        Intent intent = new Intent(a,
                                UnLoadDataSecondActivity.class);
                        intent.putExtra(KEY_MENU_CODE, menuCode);
                        a.startActivity(intent);
                        a.overridePendingTransition(0, 0);

                    } else {
                        Intent intent = new Intent(a, KPIHomeActivity.class);
                        intent.putExtra(KEY_MENU_CODE, menuCode);
                        a.startActivity(intent);
                        a.overridePendingTransition(0, 0);
                    }

                } else if ("3".equalsIgnoreCase(type)) // 特殊权限查看登录记录.
                {
                    Intent intent = new Intent(a, LoginStatisticsActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                    a.overridePendingTransition(0, 0);
                } else if ("4".equalsIgnoreCase(type)) {
                    Intent intent = new Intent(a, PortalActivity.class);
                    intent.setAction(PortalActivity.ACTION_SHOW_INITIAL_PASSWORD);
                    a.startActivity(intent);
                    a.overridePendingTransition(0, 0);
                } else if ("5".equalsIgnoreCase(type)) {
                    // 查看公告
                    Intent intent = new Intent(a, PortalActivity.class);
                    intent.setAction(PortalActivity.ACTION_SHOW_BOARD);
                    a.startActivity(intent);
                    a.overridePendingTransition(0, 0);
                } else if ("6".equalsIgnoreCase(type)) {
                    // 修改密码
                    Intent intent = new Intent(a, Password.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                } else if ("7".equalsIgnoreCase(type)) {
                    Intent intent = new Intent(a, CertQueryActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                } else if ("8".equalsIgnoreCase(type)) {

//					Intent intent = new Intent(a, DataArriveQueryActivity.class);
                    Intent intent = new Intent(a, GreatBeginActivity.class);

                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);

                } else if ("9".equalsIgnoreCase(type)) {
                    Intent intent = new Intent(a,
                            DataPresentationActivity.class);

                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                } else if ("10".equalsIgnoreCase(type)) {
                    Intent intent = new Intent(a, DailyReportActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                } else if ("11".equalsIgnoreCase(type)) {
                    // 终端类界面
                    Intent intent = new Intent(a, SimpleTerminalActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                } else if ("12".equalsIgnoreCase(type)) {
                    // 新界面，进入显示页面
                    Intent intent = new Intent(a, MainKpiActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                } else if ("13".equalsIgnoreCase(type)) {// 竞争类
                    // 有二级菜单的类型，进入二级菜单界面
                    Intent intent = new Intent(a,
                            UnLoadDataSecondActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                } else if ("14".equalsIgnoreCase(type)) {
                    // 渠道
                    Intent intent = new Intent(a, ChannelAnalyzeActivity.class);
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                } else if ("27".equalsIgnoreCase(type)) {
                    // 家庭宽带
                    Intent intent = new Intent(a, BroadActivity.class);

                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                } else {
                    Intent intent = new Intent(a, getTargetClass(a, type,
                            menuCode));
                    intent.putExtra(KEY_MENU_CODE, menuCode);
                    a.startActivity(intent);
                }

                System.out.println("type:" + type);
                finishActivityBefore();
                return true;
            }
        });
    }

    /**
     * *
     * 根据目标menuType来获取对应的目标Class 2016年5月27日13:17:30
     *
     * @param context  上下文 UnLoadDataSecondActivity类的type=1
     *                 与Adapter的Type1=1目标class不一致 以此来区分
     * @param menuType
     * @param menuCode 判断有无2级菜单专用字段 目前仅type=1 才用
     * @return targetClass
     */
    public static Class getTargetClass(Context context, String menuType,
                                       String menuCode) {
        Class targetClass = null;
        System.out.println("menuType:" + menuType);
        int type = Integer.valueOf(menuType);

        switch (type) {

            case 1:

                if (context instanceof UnLoadDataSecondActivity) {
                    targetClass = KPIHomeActivity.class;
                    break;
                }
                // 财务月报 宽带 流量,kpi类业务.
                List<Map<String, String>> sencodeMenuList = new BusinessDao()
                        .getMenuSecond(menuCode);

                // 如果有二级菜单 则转向二级菜单页面 . 否则转向指标显示的首页.
                if (sencodeMenuList != null && sencodeMenuList.size() > 0) {

                    targetClass = UnLoadDataSecondActivity.class;

                } else {

                    targetClass = KPIHomeActivity.class;
                }
                break;
            case 2:
                // 终端类业务
                targetClass = TerminalGroupActivity.class;
                break;

            case 3:
                // 特殊权限查看登录记录.
                targetClass = LoginStatisticsActivity.class;
                break;

            case 5:
                // 公告查看（未使用）
                targetClass = AnnounceMgrActivity.class;
                break;
            case 6:
                // 修改密码
                targetClass = Password.class;
                break;

            case 7:
                // 待确认
                targetClass = CertQueryActivity.class;
                break;

            case 8:
                // 接口数据监控
                targetClass = DataArriveQueryActivity.class;
                break;

            case 9:
                // 数据简报
                targetClass = DataPresentationActivity.class;
                break;

            case 10:
                // 日报
                targetClass = DailyReportActivity.class;
                break;

            case 11:
                // 终端类
                targetClass = SimpleTerminalActivity.class;
                break;

            case 12:
                // 新界面，进入显示页面
                targetClass = MainKpiActivity.class;
                break;

            case 13:
                // 竞争类
                // 有二级菜单的类型，进入二级菜单界面
                targetClass = UnLoadDataSecondActivity.class;
                break;

            case 14:
                // 渠道类
                targetClass = ChannelAnalyzeActivity.class;
                break;
            case 15:
                // 业务量类
                targetClass = SaleBIActivity.class;
                break;
            case 16:
                // 地市日报
                targetClass = RVReportListActivity.class;
                break;

            case 17:
                // ???
                targetClass = SaleMainActivity.class;
                break;
            case 18:
                // 新菜单 集合类
                targetClass = KpiTabMainActivity.class;
                // System.out.println("AAAAAAAAAAAAAAAAA");
                break;
            case 27:
                // 家宽
                targetClass = BroadActivity.class;
                // targetClass = CordovaActivity.class;
                break;
            // 新版政企
            case 29:
                // targetClass = GEMarketActivity.class;
                targetClass = BaseCordovaActivity.class;
                break;

            // 新版家宽
            case 30:
                // targetClass = BroadBandCordovaActivity.class;
                targetClass = BaseCordovaActivity.class;
                break;
            default:
                break;
        }

        return targetClass;
    }

    private void finishActivityBefore() {
        a.setResult(Activity.RESULT_OK);
        a.finish();
    }
}
