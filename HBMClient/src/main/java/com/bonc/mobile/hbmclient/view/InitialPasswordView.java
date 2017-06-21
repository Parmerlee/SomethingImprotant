package com.bonc.mobile.hbmclient.view;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.common.LoginConstant;
import com.bonc.mobile.hbmclient.util.HttpUtil;

public class InitialPasswordView {
	private Context context;
	private View gobackp_view;

	public InitialPasswordView(Context c) {
		this.context = c;
	}

	public void showConfigGoBackP() {
		LayoutInflater li = LayoutInflater.from(context);
		gobackp_view = li.inflate(R.layout.goback_pass, null);

		new AlertDialog.Builder(context)
				.setPositiveButton("重置", sendGoBackPass).setTitle("恢复该用户原始密码")
				.setView(gobackp_view).show();
	}

	// 发送消息到服务器进行密码重置
	private OnClickListener sendGoBackPass = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			String acountString = ((EditText) (gobackp_view
					.findViewById(R.id.gbp4_input))).getText().toString();
			if (acountString.length() < 1) {
				Toast.makeText(context, "请输入4A账号", Toast.LENGTH_LONG).show();
				showConfigGoBackP();
			} else {
				Map<String, String> param = new HashMap<String, String>();

				param.put("modifyusercode", acountString);

				// 发送到服务器进行账号确认
				String resultString = HttpUtil.sendRequest(
						ActionConstant.RESET_BY_ADMIN, param);

				if (resultString == null || "".equals(resultString)) {
					Toast.makeText(context, "发送失败!", Toast.LENGTH_LONG).show();
				} else {

					try {
						JSONObject jsonObject = new JSONObject(resultString);

						String resetflag = jsonObject.optString("resetflag");
						if (LoginConstant.RESULT_NORMAL.equals(resetflag)) {
							Toast.makeText(context, "重置成功!该用户密码恢复到初始密码",
									Toast.LENGTH_LONG).show();
						} else if (LoginConstant.LOGIN_WRONG_ACCOUNT
								.equals(resetflag)) {
							Toast.makeText(context, "填写不正确!请重填！",
									Toast.LENGTH_LONG).show();
							showConfigGoBackP();

						} else if (LoginConstant.LOGIN_WRONG_ALL
								.equals(resetflag)) {

							Toast.makeText(context, "填写错误，请重新输入!",
									Toast.LENGTH_LONG).show();
							showConfigGoBackP();

						} else {
							Toast.makeText(context, "该账号未注册!请确认后重填",
									Toast.LENGTH_LONG).show();
							showConfigGoBackP();
						}

					} catch (Exception e) {
						Toast.makeText(context, "发送失败!", Toast.LENGTH_LONG)
								.show();
						e.printStackTrace();
					}
				}

			}
		}

	};
}
