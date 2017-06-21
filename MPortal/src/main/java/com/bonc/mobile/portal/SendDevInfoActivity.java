package com.bonc.mobile.portal;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bonc.anhuimobile.ac.R;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.activity.BaseDataActivity;
import com.bonc.mobile.common.util.StringUtil;
import com.bonc.mobile.common.util.TextViewUtils;

/***
 * 
 * @author Administrator
 *
 */
public class SendDevInfoActivity extends BaseDataActivity {
	EditText name, phone;
	String imei, imsi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_info);
		setWatermarkImage();
		name = (EditText) findViewById(R.id.name);
		phone = (EditText) findViewById(R.id.phone);
		imei = User.getInstance().imei;
		imsi = User.getInstance().imsi;
		TextViewUtils.setText(this, R.id.imei,
				"IMEI:" + StringUtil.nullToString(imei));
		TextViewUtils.setText(this, R.id.imsi,
				"IMSI:" + StringUtil.nullToString(imsi));
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.send) {
			if (imei == null || imsi == null) {
				showToast("IMEI或IMSI无法获得，请检查手机设置");
				return;
			}
			sendDevInfo();
		}
	}

	void sendDevInfo() {
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("userName", name.getText().toString());
		param.put("mobile", phone.getText().toString());
		new LoadDataTask(this).execute("/hbmLogin/recordUserInfo", param);
	}

	@Override
	protected void bindData(JSONObject result) {
		showToast(result.optString("msg"));
	}

}
