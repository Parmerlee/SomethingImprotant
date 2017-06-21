/**
 * DeleteFocusWebsite
 */
package com.bonc.mobile.hbmclient.state.business_outlets;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.asyn_task.ChannelSummaryAsynTask;
import com.bonc.mobile.hbmclient.asyn_task.ChannelSummaryAsynTask.OnPostListener;
import com.bonc.mobile.hbmclient.common.ActionConstant;

/**
 * @author liweigao
 *
 */
public class DeleteFocusWebsite {
	private Context context;
	private OnDeleteWebsiteListener listener;
	private ChannelSummaryAsynTask mTask;

	public DeleteFocusWebsite(Context c) {
		this.context = c;
	}

	public void setListener(OnDeleteWebsiteListener odwl) {
		this.listener = odwl;
	}

	public void deleteFocusWebsite(final Map<String, String> questMap) {
		mTask = new ChannelSummaryAsynTask(this.context, new OnPostListener() {

			@Override
			public void onPost(String s) {
				try {
					JSONObject jo = new JSONObject(s);
					String flag = jo.optString("flag");
					String message = jo.optString("msg");
					if ("1".equals(flag)) {
						Toast.makeText(context, message, Toast.LENGTH_SHORT)
								.show();
						if (listener != null) {
							listener.onPost();
						}
					} else if ("-1".equals(flag)) {
						Builder builder = new Builder(context);
						message = message + ",是否重新发送?";
						builder.setMessage(message);
						builder.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								deleteFocusWebsite(questMap);
								dialog.dismiss();
							}
						});

						builder.setNegativeButton("取消", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		mTask.execute(
				ActionConstant.GET_BUSINESS_OUTLETS_FOCUS_WEBSITE_DELETE_WEBSITE,
				questMap);
	}

	public interface OnDeleteWebsiteListener {
		void onPost();
	}
}