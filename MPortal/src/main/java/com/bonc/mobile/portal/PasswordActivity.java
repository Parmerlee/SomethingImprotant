package com.bonc.mobile.portal;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bonc.anhuimobile.ac.R;
import com.bonc.mobile.common.User;
import com.bonc.mobile.common.EventBus.DefaultEvent;
import com.bonc.mobile.common.activity.PatternViewActivity;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.common.util.MD5;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.common.view.PatternView;
import com.bonc.mobile.common.view.PatternView.Cell;
import com.bonc.mobile.common.view.PatternView.OnPatternListener;
import com.bonc.mobile.portal.common.AppManager;

//import de.greenrobot.event.EventBus;
import org.greenrobot.eventbus.EventBus;
/**
 * @author sunwei
 */
public class PasswordActivity extends PatternViewActivity {
	private String password, newPassword;
	boolean setPwd;

	private enum State {
		password, newPass, verPass;
	}

	@SuppressWarnings("rawtypes")
	private Enum state;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);
		hint = (TextView) findViewById(R.id.hint);
		setPwd = getIntent().getBooleanExtra("set", false);
		if (setPwd) {
			state = State.newPass;
			hint.setText(R.string.pass_input_new_pass);
		} else {
			state = State.password;
			hint.setText(R.string.pass_input_pass);
		}
		patternView = (PatternView) findViewById(R.id.lockPattern);
		patternView.setOnPatternListener(new OnPatternListener() {

			@Override
			public void onPatternStart() {
			}

			@Override
			public void onPatternDetected(List<Cell> pattern) {
				patternView.disableInput();
				if (state == State.password) {
					password = getPassword(pattern);
					state = State.newPass;
					hint.setText(R.string.pass_input_new_pass);
				} else if (state == State.newPass) {
					newPassword = getPassword(pattern);
					if (newPassword.length() < 4) {
						hint.setText(R.string.pass_error_length);
					} else {
						state = State.verPass;
						hint.setText(R.string.pass_input_ver_pass);
					}
				} else {
					if (newPassword.equals(getPassword(pattern))) {
						hint.setText(R.string.pass_process);
						changePassword();
					} else {
						state = State.newPass;
						hint.setText(R.string.pass_error_verify);
					}
				}
				resetPattern();
			}

			@Override
			public void onPatternCleared() {
			}

			@Override
			public void onPatternCellAdded(List<Cell> pattern) {
			}
		});

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) patternView.getLayoutParams();
//		params.setMargins(0, UIUtil.fromDPtoPX(this, UIUtil.getDisplayHeight(this) / 4), 0, 0);
		params.setMargins(0,  UIUtil.getDisplayHeight(this) / 4, 0, 0);
		patternView.setLayoutParams(params);

		params = (RelativeLayout.LayoutParams) hint.getLayoutParams();
//		params.setMargins(UIUtil.fromDPtoPX(this, UIUtil.getDisplayWidth(this) / 20), UIUtil.fromDPtoPX(this, UIUtil.getDisplayHeight(this) / 8), 0, 0);
		params.setMargins( UIUtil.getDisplayWidth(this) / 20,  UIUtil.getDisplayHeight(this) / 8, 0, 0);
		hint.setLayoutParams(params);

		LogUtils.logBySys("width:"+UIUtil.getDisplayWidth(this));
	}

	private void changePassword() {
		String action;
		Map<String, String> param = new LinkedHashMap<String, String>();
		if (setPwd) {
			action = "/hbmLogin/setPwd";
			param.put("pwd", new MD5().getMD5ofStr(newPassword));
		} else {
			action = "/hbmLogin/modPwd";
			param.put("oldPwd", new MD5().getMD5ofStr(password));
			param.put("newPwd", new MD5().getMD5ofStr(newPassword));
		}
		new LoadDataTask(this).execute(action, param);
	}

	class LoadDataTask extends HttpRequestTask {
		public LoadDataTask(Context context) {
			super(context);
		}

		@Override
		protected void handleResult(JSONObject result) {
			boolean flag = result.optBoolean("flag");
			showToast(result.optString("msg"));
			if (flag) {
				User.getInstance().password = new MD5()
						.getMD5ofStr(newPassword);
				setResult(RESULT_OK);
				finish();
			} else {
				EventBus.getDefault().post(new DefaultEvent("log"));
				state = State.password;
				hint.setText(R.string.pass_input_pass);
			}
		}
	}
}
