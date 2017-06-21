package com.bonc.mobile.portal;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.bonc.anhuimobile.ac.R;
import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.activity.BaseActivity;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.DataUtil;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.PackageUtil;
import com.bonc.mobile.common.util.PhoneUtil;
import com.bonc.mobile.portal.common.AppManager;

/**
 * @author sunwei
 */
public class BasePortalActivity extends BaseActivity implements
		OnItemClickListener {
	protected AppManager manager = AppManager.getInstance();
	List<Map<String, String>> authList;
	String admin_phone;

	public void onClick(View v) {
		if (v.getId() == R.id.login_stat) {
			Intent intent = new Intent(this, MPLoginStatActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.chgpwd) {
			Intent intent = new Intent(this, PasswordActivity.class);
			startActivity(intent);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			loadAppList();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final Map<String, String> info = manager.getAppInfo(position);
		boolean hasAuth = Boolean.parseBoolean(info
				.get(AppManager.KEY_HAS_AUTH));
		String channel = info.get(AppManager.KEY_VALI_CHANNEL);
		boolean inAuth = inAuthList(channel);
		if (hasAuth && inAuth) {
			launchApp(info);
		} else if (inAuth) {
			DialogInterface.OnClickListener l = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == DialogInterface.BUTTON_POSITIVE) {
						PhoneUtil.call(BasePortalActivity.this, admin_phone);
					} else {
						String body = "需管理员分配" + info.get(AppManager.KEY_NAME)
								+ "的权限，4A账号" + User.getInstance().userCode;
						PhoneUtil.sendSMS(BasePortalActivity.this, admin_phone,
								body);
					}
				}
			};
			new AlertDialog.Builder(this).setTitle(R.string.hint)
					.setMessage(getString(R.string.msg_no_auth) + admin_phone)
					.setPositiveButton("呼叫", l).setNeutralButton("短信", l)
					.setNegativeButton(R.string.cancel, null).show();
		} else {
			Intent intent = new Intent(this, RegisterActivity.class);
			intent.putExtra("channel", channel);
			intent.putExtra("login_msg", info.get("LOGIN_MSG"));
			startActivityForResult(intent, 100);
		}
	}

	protected void loadAppList() {
		Map<String, String> param = new LinkedHashMap<String, String>();
		new LoadAppListTask(this).execute("/sysResource/appList", param);
	}

	protected void bindAppList(JSONObject result) {
		manager.load(result.optJSONArray("app_list").toString());
		authList = JsonUtil.toList(result.optJSONArray("authList"));
		admin_phone = result.optString("admin_phone");
	}

	class LoadAppListTask extends HttpRequestTask {
		public LoadAppListTask(Context context) {
			super(context);
		}

		@Override
		protected void handleResult(JSONObject result) {
			bindAppList(result);
		}
	}

	class LoadAppIconTask extends HttpRequestTask {
		public LoadAppIconTask(Context context) {
			super(context);
			showDialog = false;
		}

		@Override
		protected String doInBackground(Object... params) {
			AppManager.getInstance().loadIcons(
					DataUtil.extractList(AppManager.getInstance().getAppList(),
							AppManager.KEY_ICON));
			return "{}";
		}

		@Override
		protected void handleResult(JSONObject result) {
			refreshIcon();
		}
	}

	protected void refreshIcon() {
	}

	class PortalAdaper extends SimpleAdapter {
		public PortalAdaper(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public void setViewImage(ImageView v, String value) {
			v.setImageDrawable(manager.getAppIcon(value));
		}
	}

	void launchApp(final Map<String, String> app_info) {
		String pkg = app_info.get(AppManager.KEY_PACKAGE);
		if (PackageUtil.isPackageInstalled(this, pkg))
			AppManager.startApp(this, app_info.get(AppManager.KEY_ACTION));
		else {
			Intent intent = new Intent(this, AppDetailActivity.class);
			intent.putExtra("pkg", pkg);
			startActivity(intent);
		}
	}

	boolean inAuthList(String channel) {
		for (Map<String, String> m : authList) {
			if (m.get("USER_4A_CHANNEL").equals(channel))
				return true;
		}
		return false;
	}
}
