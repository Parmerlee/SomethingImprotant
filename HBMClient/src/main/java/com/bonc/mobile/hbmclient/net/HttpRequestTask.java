package com.bonc.mobile.hbmclient.net;

import android.content.Context;

import com.bonc.mobile.hbmclient.common.Constant;

public class HttpRequestTask extends com.bonc.mobile.common.net.HttpRequestTask {

	public HttpRequestTask(Context context) {
		super(context, Constant.SERVER_PATH);
	}

}