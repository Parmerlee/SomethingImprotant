/**
 * UserPermissionVarify
 */
package com.bonc.mobile.hbmclient.state.business_outlets;

import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.asyn_task.ChannelSummaryAsynTask;
import com.bonc.mobile.hbmclient.asyn_task.ChannelSummaryAsynTask.OnPostListener;

/**
 * @author liweigao
 *
 */
public abstract class UserPermissionVarify {
	private Context context;
	private ChannelSummaryAsynTask mTask;

	public UserPermissionVarify(Context c) {
		this.context = c;
	}

	public void questData(String action, Map<String, String> args) {
		this.mTask = new ChannelSummaryAsynTask(context, new OnPostListener() {
			@Override
			public void onPost(String s) {
				checkResult(s);
			}
		});
		this.mTask.execute(action, args);
	}

	private void checkResult(String result) {
		try {
			JSONObject jo = null;
			try {
				jo = new JSONObject(result);
				String validate = jo.optString("validate", "0");
				if ("1".equals(validate)) {
					validateUser(jo);
				} else {
					invalidateUser(jo);
				}
			} catch (Exception e) {
				Toast.makeText(context, "权限验证失败", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Toast.makeText(context, "数据加载异常", Toast.LENGTH_SHORT).show();
		}
	}

	private void invalidateUser(JSONObject jo) {
		String msg = jo.optString("msg");
		if (msg == null || "".equals(msg) || "null".equals(msg)) {
			msg = "该用户无权访问此界面";
		}
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	abstract public void validateUser(JSONObject jo);

}
