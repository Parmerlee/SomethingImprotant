/**
 * AreaSelector
 */
package com.bonc.mobile.hbmclient.command.business_outlets;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.JsonUtil;

/**
 * @author liweigao
 *
 */
public class AreaSelector {
	private Context context;
	private List<Map<String, String>> areaInfo;
	private Map<String, String> selectArea;
	private OnAreaSelectListener listener;

	public AreaSelector(Context context) {
		this.context = context;
	}

	public void setOnAreaSelectListener(OnAreaSelectListener oasl) {
		this.listener = oasl;
	}

	public void parseAreaInfo(JSONArray ja_area) {
		if (ja_area == null || "".equals(ja_area) || "null".equals(ja_area)
				|| ja_area.length() <= 0) {

		} else if (ja_area.length() > 0) {
			this.areaInfo = JsonUtil.toDataList(ja_area);
		}
	}

	public void chooseArea() {
		if (this.selectArea != null && this.selectArea.get("AREA_NAME") != null) {
			final String[] areaName = new String[this.areaInfo.size()];
			for (int i = 0; i < this.areaInfo.size(); i++) {
				areaName[i] = this.areaInfo.get(i).get("AREA_NAME");
			}
			Context c  = UIUtil.resolveDialogTheme(this.context);
			new AlertDialog.Builder(c).setTitle(R.string.hint)
					.setItems(areaName, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							selectArea = areaInfo.get(which);
							listener.onAreaSelect(AreaSelector.this);
						}
					}).show();
		} else {

		}

	}

	public void setSelectArea(int index) {
		this.selectArea = areaInfo.get(index);
	}

	public String getAreaName() {
		return this.selectArea.get("AREA_NAME");
	}

	public String getAreaLevel() {
		return this.selectArea.get("AREA_LEVEL");
	}

	public String getAreaCode() {
		if (this.selectArea != null) {
			return this.selectArea.get("AREA_CODE");
		} else {
			return null;
		}

	}

	public interface OnAreaSelectListener {
		void onAreaSelect(AreaSelector selector);
	}
}
