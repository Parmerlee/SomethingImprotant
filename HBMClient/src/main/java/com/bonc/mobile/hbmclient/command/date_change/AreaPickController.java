/**
 * AreaPickController
 */
package com.bonc.mobile.hbmclient.command.date_change;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.bonc.mobile.hbmclient.command.ICommand;
import com.bonc.mobile.hbmclient.data.BusinessDao;

/**
 * @author liweigao
 * 
 */
public class AreaPickController {
	private BusinessDao dao = new BusinessDao();
	private Context context;
	private ICommand slot;
	private String areacode;
	private String areaName;
	private List<Map<String, String>> areaInfoList;
	private int areaLevel;
	private String menuCode;

	private AlertDialog mAreaDialog;

	public AreaPickController(Context c, String menuCode) {
		this.context = c;
		this.menuCode = menuCode;
	}

	public String getAreaName() {
		return this.areaName;
	}

	public String getAreaCode() {
		return this.areacode;
	}

	public void initialAreaInfo() {
		Map<String, String> menuAddInfo = dao.getMenuAddInfo(this.menuCode);
		int level = 4;
		try {
			level = Integer.parseInt(menuAddInfo.get("showAreaLevel"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 查询用户信息
		Map<String, String> userInfo = dao.getUserInfo();
		Map<String, String> areaBaseInfo = dao.getAreaBaseInfo(userInfo
				.get("areaId"));
		int userAreaLevel = Integer.parseInt(areaBaseInfo.get("areaLevel"));

		if (level > userAreaLevel) {
			areaInfoList = dao.getAreaInfo(userInfo.get("areaId"));
		} else {
			areaInfoList = new ArrayList<Map<String, String>>();
			areaInfoList.add(areaBaseInfo);
		}
		areacode = areaInfoList.get(0).get("areaCode");
		areaName = areaInfoList.get(0).get("areaName");

		try {
			areaLevel = Integer.parseInt(areaInfoList.get(0).get("areaLevel"));
		} catch (Exception e) {
			e.printStackTrace();
			areaLevel = -1;
		}
	}

	public void setCommand(ICommand c) {
		this.slot = c;
	}

	public void pickArea() {
		int len = areaInfoList.size();
		final String[] areaNameList = new String[len];

		for (int i = 0; i < len; i++) {
			areaNameList[i] = areaInfoList.get(i).get("areaName");
		}

		// TODO Auto-generated method stub
		if (mAreaDialog == null) {
			mAreaDialog = new AlertDialog.Builder(this.context)
					.setTitle("区域选择")
					.setItems(areaNameList,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int which) {
									areaName = areaNameList[which];
									areacode = areaInfoList.get(which).get(
											"areaCode");
									mAreaDialog.dismiss();
									executeCommand();
								}
							}).create();
		}

		if (mAreaDialog.isShowing()) {
			mAreaDialog.dismiss();
		}

		mAreaDialog.show();

	}

	private void executeCommand() {
		slot.execute(areacode);
	}
}
