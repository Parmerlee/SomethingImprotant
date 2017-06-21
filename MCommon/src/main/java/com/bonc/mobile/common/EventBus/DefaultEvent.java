package com.bonc.mobile.common.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * EventBus 目前 门户专用
 * 
 * @author Administrator
 * @date 2016年10月26日16:23:28
 */
public class DefaultEvent {

	private String mMsg;

	private Map mMap;

	public static final String TAG_USERCODE = "user4ACode";

	public static final String TAG_USERPHONE = "userPhone";

	public Map getmMap() {
		return mMap;
	}

	public void setmMap(Map mMap) {
		this.mMap = mMap;
	}

	public DefaultEvent(String msg) {
		// TODO Auto-generated constructor stub
		mMsg = msg;
	}

	public DefaultEvent() {
		// TODO Auto-generated constructor stub
	}

	public void setMsg(String mMsg) {
		this.mMsg = mMsg;
	}

	/***
	 * msg:数据格式转化异常 不拿异常信息； log 单纯开始记录日志
	 * 
	 * @return
	 */
	public String getMsg() {
		return mMsg;
	}

	/***
	 * 单独为RegisterActivity使用
	 * @param userCode
	 * @param phone
	 */
	public void buildUser(String userCode, String phone) {
		mMap = new HashMap<String, String>();
		mMap.put(TAG_USERCODE, userCode);
		mMap.put(TAG_USERPHONE, phone);
	}
}
