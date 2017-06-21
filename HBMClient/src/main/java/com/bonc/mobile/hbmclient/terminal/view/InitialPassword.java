package com.bonc.mobile.hbmclient.terminal.view;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.DES;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.common.LoginConstant;

public class InitialPassword {
	private Activity a;
	private View gobackp_view;
	String menuCode;

	public InitialPassword(Activity a) {
		this.a = a;
	}

	public void showConfigGoBackP() {
		LayoutInflater li = LayoutInflater.from(a);
		gobackp_view = li.inflate(R.layout.goback_pass, null);

		new AlertDialog.Builder(a).setPositiveButton("重置", sendGoBackPass)
				.setTitle("恢复该用户原始密码").setView(gobackp_view).show();
	}

	public void setMenuCode(String c) {
		menuCode = c;
	}

	class MyTask extends HttpRequestTask {
		public MyTask(Context context) {
			super(context, Constant.BASE_PATH);
		}

		@Override
		protected void handleResult(JSONObject result) {
			boolean flag = result.optBoolean("flag");
			if (flag) {
				Toast.makeText(a, "重置成功!该用户密码恢复到初始密码", Toast.LENGTH_LONG)
						.show();

			} else {
				Toast.makeText(a, "重置失败!请确认后重填", Toast.LENGTH_LONG).show();
				showConfigGoBackP();
			}
		}
	}

	// 发送消息到服务器进行密码重置
	private OnClickListener sendGoBackPass = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			String acountString = ((EditText) (gobackp_view
					.findViewById(R.id.gbp4_input))).getText().toString();
			if (acountString.length() < 1) {
				Toast.makeText(a, "请输入4A账号", Toast.LENGTH_LONG).show();
				showConfigGoBackP();
			} else {
				Map<String, String> param = new HashMap<String, String>();
				param.put("user4ACode", acountString);
				param.put("clickCode", menuCode);
				param.put("clickType", "MENU");
				param.put("appType", "BI_ANDROID");
				DES.encrypt(param);
				new MyTask(a).execute("/sysUser/inital/loginPWD", param);
			}
		}

	};
}
