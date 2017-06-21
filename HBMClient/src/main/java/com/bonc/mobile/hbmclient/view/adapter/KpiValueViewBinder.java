package com.bonc.mobile.hbmclient.view.adapter;

import android.text.Html;
import android.view.View;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter;
import com.bonc.mobile.hbmclient.data.ColumnDataFilter.ColumnDisplyInfo;

/**
 * @author sunwei
 */
public class KpiValueViewBinder implements ViewBinder {
	String rule, unit, prefix;

	public KpiValueViewBinder(String rule, String unit) {
		this(rule, unit, "");
	}

	public KpiValueViewBinder(String rule, String unit, String prefix) {
		this.rule = rule;
		this.unit = unit;
		this.prefix = prefix;
	}

	@Override
	public boolean setViewValue(View view, Object o, String text) {
		if (view.getId() == R.id.index_title || view.getId() == R.id.topn_title) {
			if (text == null || text.length() == 0) {
				((TextView) view).setText("--");
				return true;
			}
		} else if (view.getId() == R.id.index_value
				|| view.getId() == R.id.topn_value) {
			if (text == null || text.length() == 0) {
				((TextView) view).setText("--");
				return true;
			}
			ColumnDisplyInfo cdi = ColumnDataFilter.getInstance().doFilter(
					rule, unit, text);
			((TextView) view).setText(Html.fromHtml(prefix
					+ "<font color='red'>" + cdi.getValue() + "</font>"
					+ cdi.getUnit()));
			return true;
		}
		return false;
	}

}
