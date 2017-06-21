/**
 * ListViewState
 */
package common.share.lwg.util.state.kpilist;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.view.ListViewSetting;

import common.share.lwg.util.mediator.proxy_impl.mainkpi.MainKpiMediator;
import common.share.lwg.util.mediator.proxy_impl.mainkpi.ViewColleague;

/**
 * @author liweigao
 *
 */
public class ListViewState extends AKpiList {
	private ListView lvLeft;
	private ListView lvRight;
	private LVLeftAdapter leftAdapter;
	private LVRightAdapter rightAdapter;

	/**
	 * @param machine
	 */
	public ListViewState(ViewColleague machine) {
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
		this.lvLeft = new ListView(activity);
		setLV(lvLeft);
		this.lvRight = new ListView(activity);
		setLV(lvRight);
		containerLeft.addView(lvLeft, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		containerRight.addView(lvRight, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		ListViewSetting mListViewSetting = new ListViewSetting();
		mListViewSetting.setListViewOnTouchAndScrollListener(this.lvLeft,
				this.lvRight);
	}

	private void setLV(ListView listView) {
		LayoutInflater inflater = LayoutInflater.from(listView.getContext());
		View v = inflater.inflate(R.layout.divider_hor, null);
		listView.addFooterView(v, null, false);
		listView.setVerticalScrollBarEnabled(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see common.share.lwg.util.state.kpilist.AKpiList#updateView()
	 */
	@Override
	protected void updateView() {
		int location = getInt(this.machine.getActivity(), "kpiCodeLocation");

		// DataUtil.saveSetting(PreferenceManager
		// .getDefaultSharedPreferences(this.machine.getActivity()),
		// "kpiCodeLocation", -1);

		MainKpiMediator mediator = (MainKpiMediator) this.machine.getMediator();
		this.leftAdapter = new LVLeftAdapter(mediator.getDataColleague());
		this.lvLeft.setAdapter(leftAdapter);

		// this.lvLeft.scrollTo(0, 100);

		this.rightAdapter = new LVRightAdapter(mediator.getDataColleague());
		this.lvRight.setAdapter(rightAdapter);

		// Toast.makeText(this.machine.getActivity(), "location:" + location, 0)
		// .show();
		if (location != -1) {
			this.lvLeft.smoothScrollToPositionFromTop(Math.max(location, 0), 0,
					1000);

			this.lvRight.smoothScrollToPositionFromTop(Math.max(location, 0),
					0, 1000);

			this.lvLeft.setSelection(Math.max(location, 0));

			this.lvRight.setSelection(Math.max(location, 0));
		}
	}

	private int getInt(Context context, String key) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(
				key, -1);
	}
}
