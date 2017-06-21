package com.bonc.mobile.hbmclient.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bonc.mobile.common.User;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.util.HttpUtil;
import com.bonc.mobile.hbmclient.util.MD5;
import com.bonc.mobile.hbmclient.view.PatternView;
import com.bonc.mobile.hbmclient.view.PatternView.Cell;
import com.bonc.mobile.hbmclient.view.PatternView.OnPatternListener;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

/**
 * 修改密码活动窗口
 * 
 * @author liulu
 *
 */
public class Password extends MenuActivity {

	/**
	 * 密码长度限制
	 */
	private static final int SIZE = 4;

	/** 数字过滤 **/
	private static final int MASK = 0X111111;

	private static final int CODE_INPUT_PASSWORD = MASK << 0;// 输入密码提示
	private static final int CODE_INPUT_NEW_PASSWORD = MASK << 1;// 输入新密码
	private static final int CODE_INPUT_VERIFY_PASSWORD = MASK << 2;// 输入新密码确定

	private static final int CODE_ERROR_VERIFY = MASK >> 1;// 确认密码输入不一致

	private static final int CODE_PROCESS = MASK >> 2;// 处理

	private PatternView patternView;

	private ProgressBar progressBar;
	private TextView hint;

	private StringBuffer password;
	private StringBuffer newPassword;

	private Handler handler = new Handler();

	/**
	 * 绘制状态,记录了绘制密码状态/输入新密码状态/确认新密码状态
	 */
	private enum State {
		password, newPass, verPass;
	}

	@SuppressWarnings("rawtypes")
	private Enum state = State.password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.password);
		patternView = (PatternView) findViewById(R.id.lockPattern);
		progressBar = (ProgressBar) findViewById(R.id.progress);
		hint = (TextView) findViewById(R.id.login_hint);
		// 登陆界面自适应判断----------新加的代码
		// start
		updateMessage(CODE_INPUT_PASSWORD);

		OnPatternListener opl = new PatternListener();
		patternView.setOnPatternListener(opl);

		super.onCreate(savedInstanceState);
	}

	/**
	 * 更新界面提示信息
	 */
	private void updateMessage(int type) {
		switch (type) {
		case CODE_INPUT_PASSWORD:
			hint.setText(R.string.pass_input_pass);
			progressBar.setVisibility(View.INVISIBLE);
			break;
		case CODE_INPUT_NEW_PASSWORD:
			hint.setText(R.string.pass_input_new_pass);
			progressBar.setVisibility(View.INVISIBLE);
			break;
		case CODE_INPUT_VERIFY_PASSWORD:
			hint.setText(R.string.pass_input_ver_pass);
			progressBar.setVisibility(View.INVISIBLE);
			break;
		case CODE_ERROR_VERIFY:
			hint.setText(R.string.pass_error_verify);
			progressBar.setVisibility(View.INVISIBLE);
			break;
		case CODE_PROCESS:
			hint.setText(R.string.pass_process);
			progressBar.setVisibility(View.VISIBLE);
			break;
		}

	}

	/**
	 * 重置绘图界面
	 * 
	 * @param messageCode
	 *            重置后显示何种提示信息
	 */
	private void resetPattern(final int messageCode) {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				updateMessage(messageCode);

				// 还原绘制界面
				patternView.clearPattern();
				patternView.enableInput();
			}
		}, 1000);
	}

	/**
	 * 与服务器通信修改密码
	 */
	private void ChangePassword() {

		new Thread() {
			public void run() {
				// DataParse up = new DataParse();
				// final String result = up.chagePassword(id, new
				// MD5().getMD5ofStr(password.toString()) , new
				// MD5().getMD5ofStr(newPassword.toString()));
				Map<String, String> param = new HashMap<String, String>();
				param.put(
						"menuCode",
						getIntent().getStringExtra(
								MenuEntryAdapter.KEY_MENU_CODE));
				param.put("loginpwdold",
						new MD5().getMD5ofStr(password.toString()));
				param.put("loginpwdnew",
						new MD5().getMD5ofStr(newPassword.toString()));
				final String result = HttpUtil.sendRequest(
						ActionConstant.MODIFY_PASSWORD, param);

				handler.post(new Runnable() {

					@Override
					public void run() {

						// 如果服务器返回内容为null,告知用户.
						if (result == null || "".equals(result)) {
							AlertDialog.Builder b = new AlertDialog.Builder(
									Password.this);
							b.setTitle(R.string.hint);
							b.setMessage(R.string.error_net);
							b.setPositiveButton(R.string.sure, null);
							b.show();
							Password.this.state = Password.State.password;
							resetPattern(CODE_INPUT_PASSWORD);
							return;
						}
						// 修改密码成功

						try {
							JSONObject jsonObject = new JSONObject(result);

							String updateflag = jsonObject
									.optString("updateflag");

							if ("1".equals(updateflag)) {

								User.getInstance().setPassword(
										new MD5().getMD5ofStr(newPassword
												.toString()));
								AlertDialog.Builder b = new AlertDialog.Builder(
										Password.this);
								b.setTitle(R.string.hint);
								b.setMessage(R.string.pass_success);
								b.setPositiveButton(
										R.string.sure,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												finish();
											}
										});
								b.show();
							} else {
								AlertDialog.Builder b = new AlertDialog.Builder(
										Password.this);
								b.setTitle(R.string.hint);
								b.setMessage(R.string.pass_error_pass);
								b.setPositiveButton(R.string.sure, null);
								b.show();
								Password.this.state = Password.State.password;
								resetPattern(CODE_INPUT_PASSWORD);
							}

						} catch (JSONException e) {
							AlertDialog.Builder b = new AlertDialog.Builder(
									Password.this);
							b.setTitle(R.string.hint);
							b.setMessage(R.string.pass_error_pass);
							b.setPositiveButton(R.string.sure, null);
							b.show();
							Password.this.state = Password.State.password;
							resetPattern(CODE_INPUT_PASSWORD);
							e.printStackTrace();
						}

					}
				});
			};
		}.start();
	}

	private class PatternListener implements OnPatternListener {

		@Override
		public void onPatternStart() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPatternCleared() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPatternCellAdded(List<Cell> pattern) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPatternDetected(List<Cell> pattern) {
			// TODO Auto-generated method stub

			// 禁止触摸
			patternView.disableInput();

			if (pattern != null) {
				if (state == State.password) {
					password = new StringBuffer();
					for (Cell cell : pattern) {
						password.append(cell.getRow() * 3 + cell.getColumn()
								+ 1);
					}

					state = State.newPass;

					resetPattern(CODE_INPUT_NEW_PASSWORD);
				} else if (state == State.newPass) {
					newPassword = new StringBuffer();
					for (Cell cell : pattern) {
						newPassword.append(cell.getRow() * 3 + cell.getColumn()
								+ 1);
					}

					// 密码长度验证
					if (newPassword.toString().length() < SIZE) {
						AlertDialog.Builder b = new AlertDialog.Builder(
								Password.this);
						b.setTitle(R.string.hint);
						b.setMessage(R.string.pass_error_length);
						b.setPositiveButton(R.string.sure, null);
						b.show();
						Password.this.state = State.password;
						resetPattern(CODE_INPUT_PASSWORD);
						return;
					}

					state = State.verPass;
					resetPattern(CODE_INPUT_VERIFY_PASSWORD);
				} else if (state == State.verPass) {
					StringBuffer sb = new StringBuffer();
					for (Cell cell : pattern) {
						sb.append(cell.getRow() * 3 + cell.getColumn() + 1);
					}

					// 新密码确认
					if (sb.toString().equals(newPassword.toString())) {
						updateMessage(CODE_PROCESS);
						ChangePassword();

					} else {
						state = State.password;
						updateMessage(CODE_ERROR_VERIFY);
						resetPattern(CODE_INPUT_PASSWORD);
						return;

					}
				}
			}

		}
	}
}
