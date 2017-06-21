/**
 * KpiBranch
 */
package com.bonc.mobile.hbmclient.composite.levelkpi;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.bonc.mobile.hbmclient.util.MenuUtil;

/**
 * @author liweigao
 *
 */
public class KpiBranch extends ALevelKpiBranch {
	private String[] colKey;
	private LinearLayout containerView;

	/**
	 * @param c
	 */
	public KpiBranch(Context c, String[] colkey) {
		super(c);
		this.colKey = colkey;
		for (int i = 1; i < colKey.length; i++) {
			String key = colKey[i];
			add(new KpiLeaf(context, key));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiBranch#constructView
	 * ()
	 */
	@Override
	public void constructView() {
		this.containerView = new LinearLayout(context);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.FILL_PARENT);
		this.containerView.setLayoutParams(lp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiBranch#getView()
	 */
	@Override
	public View getView() {
		return this.containerView;
	}

	public void setBackground(int res) {
		this.containerView.setBackgroundResource(res);
	}

	public void setOnClickListener(final String menuCode, final String optime,
			final String areaCode, final String kpiCode, final String dataType) {
		this.containerView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MenuUtil.startKPITimeActivity(context, menuCode, optime,
						areaCode, kpiCode, dataType);
			}
		});
	}
}
