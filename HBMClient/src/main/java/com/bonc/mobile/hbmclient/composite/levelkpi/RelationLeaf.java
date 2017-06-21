/**
 * RelationLeaf
 */
package com.bonc.mobile.hbmclient.composite.levelkpi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.MenuSecondActivity;
import com.bonc.mobile.hbmclient.activity.RelationKpiActivity;
import com.bonc.mobile.hbmclient.adapter.levelkpi.DimensionAdapter;

/**
 * @author liweigao
 *
 *         关联类
 */
public class RelationLeaf extends ALevelKpiLeaf {
	private ImageView mImageView;
	private RelativeLayout containerView;

	/**
	 * @param c
	 */
	public RelationLeaf(Context c) {
		super(c);
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		this.containerView = new RelativeLayout(context);
		int pxWidth = this.dimenAdapter
				.fromDPtoPX(DimensionAdapter.RELATION_WIDTH_DP);
		this.containerView
				.setLayoutParams(new RelativeLayout.LayoutParams(
						pxWidth, LayoutParams.FILL_PARENT));
		this.containerView.setGravity(Gravity.CENTER);

		this.mImageView = new ImageView(context);
		this.mImageView.setImageResource(R.mipmap.icon_relation);
		this.containerView.addView(mImageView);
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

	public void setImageResource(final String kpicode, final String dataType,
			final String optime, final String areacode, final String menuCode) {
		this.mImageView.setVisibility(View.VISIBLE);
		this.containerView.setClickable(true);
		this.containerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, RelationKpiActivity.class);
				i.putExtra(RelationKpiActivity.KEY_MAIN_KPI_CODE, kpicode);
				i.putExtra(RelationKpiActivity.KEY_MAIN_KPI_TYPE, dataType);
				i.putExtra(RelationKpiActivity.KEY_OPTIME, optime);
				i.putExtra(RelationKpiActivity.KEY_AREACODE, areacode);
				i.putExtra(RelationKpiActivity.KEY_MENU_CODE, menuCode);
				Activity activity = (Activity) context;
				activity.startActivityForResult(i,
						MenuSecondActivity.REQUEST_CODE_FINISH);
			}
		});
	}

	public void removeImageResource() {
		this.mImageView.setVisibility(View.INVISIBLE);
		this.containerView.setClickable(false);
	}
}
