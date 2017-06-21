package com.bonc.mobile.common.activity;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.widget.TextView;

import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.R;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.util.JsonUtil;
import com.bonc.mobile.common.util.SMTools;

public class UserInfoActivity extends BaseDataActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_user_info);
		this.initView();
		this.loadData();
	}

	@Override
    protected void initView() {
        super.initView();
        TextView view = this.findTextView(R.id.title);
        view.setText("个人信息");
    }

    @Override
    protected void loadData() {
        Map<String, String> params = new LinkedHashMap<String, String>();
        new LoadDataTask(this,Constant.RV_PATH).execute("/sys/privateInfo", params);
    }

    @Override
    protected void bindData(JSONObject object) {
        TextView userName = this.findTextView(R.id.userName);
        userName.setText(User.getInstance().userCode);
        TextView userPhoneNum = this.findTextView(R.id.userPhoneNum);
        userPhoneNum.setText(JsonUtil.optString(object, "USER_MOBILE"));
        TextView userLastLoginTime = this.findTextView(R.id.userLastLoginTime);
        userLastLoginTime.setText(JsonUtil.optString(object, "UPDATE_TIME"));
        SMTools smTools = new SMTools(this);
        TextView deviceIMEI = this.findTextView(R.id.deviceIMEI);
        TextView deviceIMSI = this.findTextView(R.id.deviceIMSI);
        deviceIMEI.setText(smTools.getIMEI());
        deviceIMSI.setText(smTools.getIMSI());
    }

    private TextView findTextView(int id) {
        return (TextView) this.findViewById(id);
    }
}
