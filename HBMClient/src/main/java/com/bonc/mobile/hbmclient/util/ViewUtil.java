package com.bonc.mobile.hbmclient.util;

import android.text.TextPaint;
import android.util.TypedValue;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.view.KpiListRightItemVIew;

public class ViewUtil {
	private volatile static ViewUtil mViewUtil = null;

	private ViewUtil() {

	}

	public static ViewUtil getSingleInstance() {
		if (mViewUtil == null) {
			synchronized (ViewUtil.class) {
				if (mViewUtil == null) {
					mViewUtil = new ViewUtil();
				}
			}
		}
		return mViewUtil;
	}

	public void setTextTitleStyle(TextView tv) {
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
		tv.setTextColor(0xffffffff);
		// TextPaint tp = tv.getPaint();
		// tp.setFakeBoldText(true);
	}

	public void setTextStyleFirstLevel(TextView tv) {
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
		tv.setTextColor(0xff005092);
	}

	public void setTextStyleSecondLevel(TextView tv) {
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tv.setTextColor(0xff373737);
	}

	public void setTextStyleMainKpi(TextView tv) {
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tv.setTextColor(0xff005092);
		TextPaint tp = tv.getPaint();
		tp.setFakeBoldText(true);
	}

	public void setTextStyleRelativeKpi(TextView tv) {
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tv.setTextColor(0xff323844);
	}

	public void setStyleCURelative(KpiListRightItemVIew v) {
		v.setContentColor(0xff323844);
		v.setContentSize(14);
		v.setUnitColor(0xff323844);
		v.setUnitSize(11);
	}

	public void setStyleCUMainKpi(KpiListRightItemVIew v) {
		v.setContentColor(0xff005092);
		v.setContentSize(14);
		v.setUnitColor(0xff3e444f);
		v.setUnitSize(11);
	}

	public void setKLRIVStyle1(KpiListRightItemVIew v) {
		v.setContentColor(0xff005092);
		v.setContentSize(15);
		v.setUnitColor(0xff3e444f);
		v.setUnitSize(11);
	}

	public void setKLRIVStyle2(KpiListRightItemVIew v) {
		v.setContentColor(0xff373737);
		v.setContentSize(14);
		v.setUnitColor(0xff3e444f);
		v.setUnitSize(11);
	}
}
