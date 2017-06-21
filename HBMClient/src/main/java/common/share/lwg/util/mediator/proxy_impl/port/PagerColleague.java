/**
 * PagerColleague
 */
package common.share.lwg.util.mediator.proxy_impl.port;

import java.security.spec.MGF1ParameterSpec;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.terminal.view.InitialPassword;
import com.bonc.mobile.hbmclient.terminal.view.ScanAnnouncement;
import com.bonc.mobile.hbmclient.util.LogUtil;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

import common.share.lwg.util.mediator.proxy_impl.IColleague;

/**
 * @author liweigao
 * 
 */
public class PagerColleague extends APortColleague {
	private ViewPager viewPager;
	private ScanAnnouncement mScanAnnouncement;
	// 重置密码
	private InitialPassword mInitialPassword;

	/**
	 * @param activity
	 */
	public PagerColleague(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * common.share.lwg.util.mediator.proxy_impl.port.APortColleague#create()
	 */
	@Override
	public void create() {
		// 公告
		this.mScanAnnouncement = new ScanAnnouncement(this.activity);
		// 重置密码
		this.mInitialPassword = new InitialPassword(this.activity);
	}

	public void setMenuGroupInfo(List<Map<String, String>> menuGroupInfo) {
		viewPager = (ViewPager) activity.findViewById(R.id.pager);
		viewPager.setAdapter(new MyPagerAdapter(menuGroupInfo));
		MyPagerOnPageChangeListener pagerListener = new MyPagerOnPageChangeListener(
				menuGroupInfo.size());
		viewPager.setOnPageChangeListener(pagerListener);
	}

	public void focusView(int index) {
		viewPager.setCurrentItem(index);
	}

	private class MyPagerOnPageChangeListener implements OnPageChangeListener {
		private int length;

		public MyPagerOnPageChangeListener(int length) {
			this.length = length;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int i) {
			if (i < length) {
				portMediator.focusView(PagerColleague.this, i);
			}
		}
	}

	private class MyPagerAdapter extends PagerAdapter {
		private List<Map<String, String>> menuGroupInfo;

		public MyPagerAdapter(List<Map<String, String>> menuGroupInfo) {
			this.menuGroupInfo = menuGroupInfo;
		}

		@Override
		public void destroyItem(View v, int position, Object obj) {
			ViewPager pager = (ViewPager) v;
			pager.removeView((View) obj);
		}

		@Override
		public void finishUpdate(View v) {
		}

		@Override
		public int getCount() {
			return menuGroupInfo.size();
		}

		@Override
		public Object instantiateItem(View v, int position) {
			
			ViewPager pager = (ViewPager) v;
			Context c = v.getContext();
			LayoutInflater inflater = LayoutInflater.from(c);
			View onePage = inflater.inflate(R.layout.menu_first_list_layout,
					viewPager, false);
			ListView lv = (ListView) onePage.findViewById(R.id.simple_list);
			pager.addView(onePage);
			
			String kind = menuGroupInfo.get(position).get("GROUP_KIND");
			
			final List<Map<String, String>> outmoduleList = new BusinessDao()
					.getMenuFisrtByKind(kind);

			new MenuEntryAdapter(activity, mScanAnnouncement, mInitialPassword,
					lv, outmoduleList);

			return onePage;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

	}

	public void showBoard(boolean flag) {
		this.mScanAnnouncement.showBulletinBoard(flag);
	}

	public void actionShowBoard() {
		viewPager.setCurrentItem(get_SM_Position());
		mScanAnnouncement.showBulletinBoard(true);
	}

	public void actionPassword() {
		viewPager.setCurrentItem(get_SM_Position());
		mInitialPassword.showConfigGoBackP();
	}

	private int get_SM_Position() {
		BusinessDao dao = new BusinessDao();
		List<Map<String, String>> data = dao.getMenuGroupInfo();
		for (int i = 0; i < data.size(); i++) {
			String groupKind = data.get(i).get("GROUP_KIND");
			if ("C".equals(groupKind)) {
				return i;
			} else {

			}
		}
		return 0;
	}
}
