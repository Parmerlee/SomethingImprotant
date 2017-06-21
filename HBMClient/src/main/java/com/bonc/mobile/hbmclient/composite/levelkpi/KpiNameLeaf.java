/**
 * KpiNameLeaf
 */
package com.bonc.mobile.hbmclient.composite.levelkpi;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.DateUtil;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.asyn_task.NoticeAsynTask;
import com.bonc.mobile.hbmclient.util.MenuUtil;
import com.bonc.mobile.hbmclient.util.StringUtil;
import com.bonc.mobile.hbmclient.util.ViewUtil;

import common.share.lwg.util.builder.mainkpi.LKpiRowBuilder;
import common.share.lwg.util.mediator.proxy_impl.mainkpi.DataColleague;

/**
 * @author liweigao
 * 
 */
public class KpiNameLeaf extends ALevelKpiLeaf {
	private View containerView;
	private TextView mTextView;
	private ViewUtil mViewUtil = ViewUtil.getSingleInstance();

	private ImageView mStatus;

	Context context;

	/**
	 * @param c
	 */
	public KpiNameLeaf(Context c) {
		super(c);
		context = c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiLeaf#constructView
	 * ()
	 */
	@Override
	public void constructView() {
		LayoutInflater li = LayoutInflater.from(context);
		this.containerView = li.inflate(
				R.layout.layout_main_kpi_chart_item_left, null);
		this.mTextView = (TextView) containerView.findViewById(R.id.id_text);
		mStatus = (ImageView) containerView.findViewById(R.id.id_status);
		this.mTextView.setMaxLines(3);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiLeaf#getView()
	 */
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return this.containerView;
	}

	public void setText(String s) {
		s = StringUtil.nullToString(s);
		s = s.replace("[n]", "\n");
		this.mTextView.setText(s);
	}

	public void setBackground(int res) {
		this.containerView.setBackgroundResource(res);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiLeaf#setFirstLevelStyle
	 * ()
	 */
	@Override
	public void setFirstLevelStyle() {
		mViewUtil.setTextStyleFirstLevel(this.mTextView);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiLeaf#
	 * setSecondLevelStyle()
	 */
	@Override
	public void setSecondLevelStyle() {
		mViewUtil.setTextStyleSecondLevel(this.mTextView);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiLeaf#setMainKpiStyle
	 * ()
	 */
	@Override
	public void setMainKpiStyle() {
		this.mViewUtil.setTextStyleMainKpi(mTextView);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiLeaf#
	 * setRelationKpiStyle()
	 */
	@Override
	public void setRelationKpiStyle() {
		mViewUtil.setTextStyleRelativeKpi(mTextView);
	}

	public void setOnClickListener(final String menuCode, final String optime,
			final String areaCode, final String kpiCode, final String dataType) {
		this.containerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MenuUtil.startKPIAreaActivity(context, menuCode, optime,
						areaCode, kpiCode, dataType);
			}
		});
	}

	public void setOnLongClickListener(final String kpiDefine) {
		this.containerView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				new NoticeAsynTask(containerView, kpiDefine).execute();
				return true;
			}
		});
	}

	public void setGroupButtonState(final String kpiCode,
			final DataColleague dataColleague) {

		mStatus.setImageResource(dataColleague.isSecondLevelShow(kpiCode) ? R.mipmap.up
				: R.mipmap.down);

		// Log.d("AAA", "dataColleague.isSecondLevelShow(kpiCode):"
		// + (dataColleague.isSecondLevelShow(kpiCode)));
		boolean b = false;
		try {
			b = !dataColleague.levelMap.get(kpiCode).isEmpty();
		} catch (Exception e) {
			// TODO: handle exception
			b = false;
		}

		mStatus.setVisibility(b ? View.VISIBLE : View.GONE);

		mStatus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				int location = dataColleague.getPosOnListView(kpiCode);
				Log.d("AAAAA", "location:" + location);
				// Toast.makeText(context, "AAAAAAAAA",
				// Toast.LENGTH_SHORT).show();

				DataUtil.saveSetting(
						PreferenceManager.getDefaultSharedPreferences(context),
						"kpiCodeLocation", location);

				dataColleague.showSecondLevelKpi(kpiCode,
						dataColleague.isSecondLevelShow(kpiCode));
				// Class<T> l = context.getClass();
				// Log.d("AAAA","name:"+context.getClass().getName());
				//
				// ;
				//
				// // getScrollview(v);
				// Log.d("AAAA","name:"+mStatus.getParent().getParent().getParent().getParent().getClass().getName());
				//
				// int [] location = new int[2];
				// v.getLocationOnScreen(location);
				//
				// Log.d("AAAA", "Y:"+location[1]);
				// View view =
				// (LKpiRowBuilder)mStatus.getParent().getParent().getParent().getParent();
				// view.scrollTo(location[0], location[1]);
				// view.s
				// // ()

			}

		});

	}
	// private View getScrollview(View v) {
	// // TODO Auto-generated method stub
	// Log.d("AAAAA", "parent:"+checkParent(v));
	// while(!TextUtils.equals(checkParent(v),"ScrollView")){
	// v = (View)v.getParent();
	//
	// }
	// return v;
	// }
	//
	// String checkParent(View v){
	// return v.getParent().getClass().getName();
	// }
}
