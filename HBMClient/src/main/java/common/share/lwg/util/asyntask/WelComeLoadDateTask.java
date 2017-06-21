package common.share.lwg.util.asyntask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.R;
import com.bonc.mobile.common.net.HttpUtil;
import com.bonc.mobile.common.util.DES;
import com.bonc.mobile.hbmclient.activity.PortalActivity;
import com.bonc.mobile.hbmclient.common.Constant;
import com.bonc.mobile.hbmclient.data.DataLoader;
import com.bonc.mobile.hbmclient.data.DatabaseCallBack;
import com.bonc.mobile.hbmclient.data.SQLHelper;
import com.bonc.mobile.hbmclient.util.JsonUtil;

public abstract class WelComeLoadDateTask extends
		AsyncTask<Object, Integer, String> {

	private ProgressDialog mProgressDialog;
	private Context context;
	private SQLHelper sqlHelper = new SQLHelper();
	private DataLoader dataLoader = new DataLoader();
	private String msg;

	/**
	 * 判断请求flag，０为默认情况，１为　ＯＫ, 2 为 加载菜单资源错误 ，３　为　初始化配置信息错误
	 **/
	public int flag;

	public WelComeLoadDateTask(Context context) {
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected String doInBackground(Object... params) {
		String result = null;

		try {
			result = DES.decrypt2(HttpUtil.sendRequest((String) params[0],
					(String) params[1], (Map<String, String>) params[2]));

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!TextUtils.isEmpty(checkResult(result)))
			return msg;

		checkResult_config(result);

		if (flag > 1)
			return checkResult_config(result);

		Map<String, String> param = new HashMap<String, String>();
		param.put("appType", "ANDROID_BI");
		try {
			// result = DES.decrypt2(HttpUtil.sendRequest(Constant.BASE_PATH,
			// "/sysResource", param));
			// if (!AppConstant.SEC_ENH) {
			//
			result = DES.decrypt2(HttpUtil.sendRequest(Constant.BASE_PATH,
					"/sysResource/getMenuResource", param));

			// result = DES.decrypt2(HttpUtil.sendRequest(
			// "http://192.168.1.114:9080",
			// "/sysResource/getMenuResource", param));
			// }
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!TextUtils.isEmpty(checkResult(result)))
			return msg;

		return checkResult_sys(result);
	}

	private String checkResult_config(String result) {
		String initResult = "";

		try {
			JSONObject data = new JSONObject(result);
			String reset_db = data.getString("RESET_DB");
			String reset_conf = data.getString("RESET_CONF");

			if (!sqlHelper.isDBFileExists() || "1".equals(reset_db)) {
				sqlHelper.deleteDbIfExists();
				sqlHelper.useEmptyDB();
				boolean re = dataLoader.initConfTableData("0");
				if (re == false) {
					initResult = "1";
				}
			} else if ("1".equals(reset_conf)) {
				if (dataLoader.deleteConfTableData()) {
					boolean re = dataLoader.initConfTableData("0");
					if (re == false) {
						initResult = "1";
					}
				} else {
					initResult = "1";
				}
			}
			if (("1".equals(reset_db) || "1".equals(reset_conf))
					&& initResult.length() == 0) {

				HttpUtil.sendRequest(Constant.BASE_PATH,
						"/sysResource/resetConfigSuc",
						new HashMap<String, String>());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			initResult = "1";
		}
		if (TextUtils.equals("1", initResult)) {
			result = "初始化配置信息错误";
			flag = 3;
			return result;
		} else {
			result = "OK";
			flag = 1;
		}
		return result;
	}

	private String checkResult_sys(String result) {
		try {
			final JSONObject json = new JSONObject(result);
			if (json.optBoolean("flag")) {
				final JSONArray data = json.optJSONArray("data");
				sqlHelper.doDBOperateWithTransation(new DatabaseCallBack() {
					@Override
					public boolean doCallBack(SQLiteDatabase database) {
						database.delete("menu_info", null, null);
						database.delete("menu_add_info", null, null);
						for (int i = 0; i < data.length(); i++) {
							JSONObject jo = data.optJSONObject(i);
							ContentValues cv = new ContentValues();
							cv.put("menu_name",
									JsonUtil.optString(jo, "MENU_NAME"));
							cv.put("menu_order",
									JsonUtil.optString(jo, "MENU_ORDER"));
							cv.put("menu_explain",
									JsonUtil.optString(jo, "MENU_EXPLAIN"));
							cv.put("view_dim_flag",
									JsonUtil.optString(jo, "VIEW_DIM_FLAG"));
							cv.put("parent_menu_code",
									JsonUtil.optString(jo, "PARENT_MENU_CODE"));
							cv.put("menu_type",
									JsonUtil.optString(jo, "MENU_TYPE"));
							cv.put("menu_code",
									JsonUtil.optString(jo, "MENU_CODE"));
							cv.put("menu_level",
									JsonUtil.optString(jo, "MENU_LEVEL"));
							cv.put("data_table", JsonUtil.optString(jo,
									"MENU_REF_DATA_TABLE"));
							cv.put("menu_kind",
									JsonUtil.optString(jo, "MENU_KIND"));
							cv.put("date_type",
									JsonUtil.optString(jo, "DATE_TYPE"));
							cv.put("data_type",
									JsonUtil.optString(jo, "DATE_TYPE"));
							database.insert("menu_info", null, cv);
							cv = new ContentValues();
							cv.put("menu_code",
									JsonUtil.optString(jo, "MENU_CODE"));
							cv.put("show_thrend_flag",
									JsonUtil.optString(jo, "SHOW_THREND_FLAG"));
							cv.put("show_area_level",
									JsonUtil.optString(jo, "SHOW_AREA_LEVEL"));
							cv.put("fluctuatest_column", JsonUtil.optString(jo,
									"FLUCTUATEST_COLUMN"));
							cv.put("highest_column",
									JsonUtil.optString(jo, "HIGHEST_COLUMN"));
							cv.put("fluctuatest_desc",
									JsonUtil.optString(jo, "FLUCTUATEST_DESC"));
							cv.put("highest_desc",
									JsonUtil.optString(jo, "HIGHEST_DESC"));
							cv.put("show_top5_flag",
									JsonUtil.optString(jo, "SHOW_TOP5_FLAG"));
							cv.put("top5_order_cols",
									JsonUtil.optString(jo, "TOP5_ORDER_COLS"));
							cv.put("top5_order_names",
									JsonUtil.optString(jo, "TOP5_ORDER_NAMES"));
							cv.put("img_id", JsonUtil.optString(jo, "IMG_ID"));
							cv.put("show_daily_report",
									JsonUtil.optString(jo, "SHOW_DAILY_REPORT"));
							cv.put("MENU_CHANGE",
									JsonUtil.optString(jo, "MENU_CHANGE"));
							cv.put("RELATION_TAG",
									JsonUtil.optString(jo, "RELATION_TAG"));
							cv.put("startTime",
									JsonUtil.optString(jo, "START_TIME"));
							cv.put("endTime",
									JsonUtil.optString(jo, "END_TIME"));
							database.insert("menu_add_info", null, cv);
						}
						return true;
					}
				});
				result = "OK";
				flag = 1;
			} else {
				result = json.optString("msg");

				flag = 2;
				return result;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog = ProgressDialog.show(context,
				context.getString(R.string.hint),
				context.getString(R.string.loading_data));
	}

	@Override
	protected void onPostExecute(String result) {
		if (this.mProgressDialog != null) {
			if (this.mProgressDialog.isShowing()) {
				if (this.context != null)
					this.mProgressDialog.dismiss();
			}
		}
		this.mProgressDialog = null;

		if (flag == 1 || flag == 0) {

			Intent intent = null;
			intent = new Intent(context, PortalActivity.class);
			context.startActivity(intent);
			((Activity) context).finish();

		}
		onPost(result, flag);
	}

	String checkResult(String result) {
		if (!AppConstant.SEC_ENH) {
			System.out.println(result);
		}
		if (result == null) {
			msg = context.getString(R.string.network_error);
			flag = 4;
			return msg;
		} else if (result.length() == 0) {
			msg = context.getString(R.string.data_error);
			flag = 4;
			return msg;
		} else {
			return "";
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	/**
	 * 判断请求flag，０为默认情况，１为　ＯＫ, 2 为 加载菜单资源错误 ，３　is　初始化配置信息错误
	 **/
	abstract public void onPost(String result, int flag);

}
