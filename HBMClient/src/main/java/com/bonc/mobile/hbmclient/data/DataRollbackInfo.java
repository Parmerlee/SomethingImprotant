package com.bonc.mobile.hbmclient.data;

import java.util.ArrayList;
import java.util.List;

public class DataRollbackInfo {

	private DataRollbackInfo() {
	}

	private static List<RollbackInfoBean> infoList;

	public static void addRollbackInfo(RollbackInfoBean rollbackInfoBean) {
		if (rollbackInfoBean == null) {
			return;
		}

		if (infoList == null) {
			infoList = new ArrayList<RollbackInfoBean>();
		}

		int len = infoList.size();
		int i = 0;
		for (; i < len; i++) {
			RollbackInfoBean rbb = infoList.get(i);
			if (rbb.getMenuCode().equals(rollbackInfoBean.getMenuCode())) {
				if (rollbackInfoBean.getFlowId() > rbb.getFlowId()) {
					rbb.setFlowId(rollbackInfoBean.getFlowId());
				}
				return;
			}
		}

		if (i == len) {
			infoList.add(rollbackInfoBean);
		}

	}

	// 获取
	public static RollbackInfoBean getInfoBean(String menuCode) {
		if (menuCode == null) {
			return null;
		}

		if (infoList == null) {
			return null;
		}

		int len = infoList.size();

		for (int i = 0; i < len; i++) {
			if (menuCode.equals(infoList.get(i).getMenuCode())) {
				return infoList.get(i);
			}
		}

		return null;

	}

}
