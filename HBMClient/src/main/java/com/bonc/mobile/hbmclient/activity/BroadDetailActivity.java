package com.bonc.mobile.hbmclient.activity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.common.Constant;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.kpi.SimpleKpiDataAdapter;
import com.bonc.mobile.common.kpi.SimpleKpiDataModel;
import com.bonc.mobile.common.kpi.SimpleKpiDataRow;
import com.bonc.mobile.common.kpi.SimpleKpiDataView;
import com.bonc.mobile.common.kpi.TableReportActivity;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.ChangePageDialog.OnAddressCListener;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.WatermarkImage;
import com.bonc.mobile.hbmclient.view.adapter.MenuEntryAdapter;

public class BroadDetailActivity extends TableReportActivity implements
		OnClickListener {

	Activity activity;
	int page = 1;
	String optime = null;

	TextView mTv_Page1, mTv_Page2, mTv_search;

	ImageView mIv_up, mIv_down, mIv_search;

	View view_input, view_buttom;

	/***
	 * 自定义的左边宽度 3个汉字
	 */
	private int INT_LEFT_WIDTH;

	int mPage_MAX;

	@Override
	protected SimpleKpiDataModel buildDataModel(JSONObject result) {
		SimpleKpiDataModel model = new SimpleKpiDataModel();
		model.build(result, "showColumn", "data");
		return model;
	}

	protected int getContentRes() {
		return R.layout.activity_broad_detail;
	}

	@Override
	protected void initView() {
		findViewById(android.R.id.content).setBackgroundDrawable(
				WatermarkImage.getWatermarkDrawable());
		initDateSelect();
		initAreaSelect();
		activity = this;
		dataView = (SimpleKpiDataView) findViewById(R.id.data_view);

		dataView.getLeftList().setOnItemClickListener(this);
		dataView.getRightList().setOnItemClickListener(this);

		INT_LEFT_WIDTH = (int) (getResources().getDimensionPixelSize(
				R.dimen.kpi_data_col_width) / 2.5);

		// 调整里面字体占有的宽度------start
		ViewGroup parent = (ViewGroup) dataView.getLeftKpiTitle().getParent();
		LayoutParams params = parent.getLayoutParams();

		params.width = INT_LEFT_WIDTH;

		// dataView.requestLayout();
		// 调整里面字体占有的宽度------end

		optime = getIntent().getStringExtra("opTime");
		TextViewUtils.setText(activity, R.id.title,
				getIntent().getStringExtra("title"));

		mTv_Page1 = (TextView) activity.findViewById(R.id.wheel_button1);
		// mTv_Page1.setOnClickListener((OnClickListener) activity);
		mTv_Page1.setVisibility(View.VISIBLE);

		mTv_Page2 = (TextView) activity.findViewById(R.id.wheel_button2);
		// mTv_Page1.setOnClickListener((OnClickListener) activity);
		mTv_Page2.setVisibility(View.VISIBLE);

		activity.findViewById(R.id.wheel_button).setVisibility(View.VISIBLE);

		mTv_search = (TextView) activity.findViewById(R.id.search_button);
		mTv_search.setOnClickListener((OnClickListener) activity);
		// mTv_search.setVisibility(View.VISIBLE);

		mIv_up = (ImageView) activity.findViewById(R.id.page_up);
		mIv_up.setOnClickListener(this);
		mIv_up.setVisibility(View.VISIBLE);

		mIv_down = (ImageView) activity.findViewById(R.id.page_down);
		mIv_down.setOnClickListener(this);
		mIv_down.setVisibility(View.VISIBLE);

		mIv_search = (ImageView) this.findViewById(R.id.search);
		mIv_search.setOnClickListener(this);
		mIv_search.setVisibility(View.VISIBLE);

		activity.findViewById(R.id.area_button).setVisibility(View.GONE);

		date_button.setText(DateUtil.oneStringToAntherString(optime,
				DateUtil.PATTERN_8, "yyyy/MM/dd"));
		date_button.setDate(DateUtil.getDate(optime, DateUtil.PATTERN_8));
		// bindData(new JSONObject());
	}

	List<String> list_page = new ArrayList<String>();

	@Override
	protected void bindData(JSONObject result) {

		// isSearch = false;
		if (result.optBoolean("flag")) {
			// firstQuery = false;
			if (hasDateArea()) {
				renderButtons(result);
			}
			SimpleKpiDataModel model = buildDataModel(result);
			dataView.setModel(model);
			dataView.updateView(getDataAdapter2(model, 1),
					getDataAdapter(model, 2));
			if (model.getData().size() == 0)
				showNoData();
		} else
			showToast(result.optString("msg"));

		try {
			if (!result.optBoolean("flag")) {
				activity.findViewById(R.id.data_view).setVisibility(View.GONE);
				TextView tv = (TextView) activity.findViewById(R.id.data_no);
				tv.setText(isSearch ? "暂未搜索到结果" : "今日数据暂无发布");
				tv.setVisibility(View.VISIBLE);
				mTv_Page2.setText("/" + 1 + "页");
				mPage_MAX = 1;
				return;
			}

			// 调整左侧title的宽度
			dataView.bulidLfetTilteWithWidth(INT_LEFT_WIDTH);
			activity.findViewById(R.id.data_view).setVisibility(View.VISIBLE);
			activity.findViewById(R.id.data_no).setVisibility(View.GONE);
			// date
			optime = result.getString("optime");
			date_button.setDate(DateUtil.getDate(optime, DateUtil.PATTERN_8));
			date_button.setText(DateUtil.oneStringToAntherString(optime,
					DateUtil.PATTERN_8, "yyyy/MM/dd"));

			page = Integer.valueOf(result.getString("curpage"));

			// System.out.println("page:" + page);
			mPage_MAX = Integer.valueOf(result.getString("totalpage"));
			list_page.clear();
			for (int i = 0; i < Integer.valueOf(result.getInt("totalpage")); i++) {
				list_page.add(String.valueOf(i + 1));
			}

			mTv_Page1.setText(result.getString("curpage"));
			mTv_Page2.setText("/" + mPage_MAX + "页");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 手动修改左边listview占有的宽度，与上面左边字体宽度配合 使文字居中显示----start
	protected SimpleKpiDataAdapter getDataAdapter2(SimpleKpiDataModel model,
			int type) {
		return new TableKpiDataAdapter(this, model, type);
	}

	protected class TableKpiDataAdapter extends SimpleKpiDataAdapter {
		public TableKpiDataAdapter(Context context,
				SimpleKpiDataModel dataModel, int type) {
			super(context, dataModel, type);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SimpleKpiDataRow v = (SimpleKpiDataRow) super.getView(position,
					convertView, parent);
			v.setColumnWidth(INT_LEFT_WIDTH);
			return v;
		}
	}

	// ------end

	String content;
	EditText ed_page;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		// if (v.getId() == R.id.wheel_button) {
		//
		// ChangePageDialog mChangeAddressDialog = new ChangePageDialog(
		// activity, list_page);
		// mChangeAddressDialog.setAddress(String.valueOf(page));
		// if (list_page != null && list_page.size() > 0)
		// mChangeAddressDialog.show();
		//
		// mChangeAddressDialog.setAddresskListener(new OnAddressCListener() {
		//
		// @Override
		// public void onClick(String province) {
		// page = Integer.valueOf(province);
		// loadData();
		// return;
		// }
		// });
		//
		// }
		// if (v.getId() == R.id.search_button) {
		// PageDialog dialog = new PageDialog(activity);
		// dialog.show();
		// }

		switch (v.getId()) {
		case R.id.wheel_button:
			// case R.id.search:

			// ChangePageDialog mChangeAddressDialog = new ChangePageDialog(
			// activity, list_page);
			// mChangeAddressDialog.setAddress(String.valueOf(page));
			// if (list_page != null && list_page.size() > 0)
			// mChangeAddressDialog.show();
			//
			// mChangeAddressDialog.setAddresskListener(new OnAddressCListener()
			// {
			//
			// @Override
			// public void onClick(String province) {
			// page = Integer.valueOf(province);
			// loadData();
			// return;
			// }
			// });
			ed_page = (EditText) activity.findViewById(R.id.broad_edit);
			view_input = (View) ed_page.getParent();
			view_input.setVisibility(View.VISIBLE);

			activity.findViewById(R.id.broad_edit_ok).setOnClickListener(
					(OnClickListener) activity);
			ed_page.performClick();
			ed_page.requestFocus();

			ed_page.setText("" + page);
			changeImm();
			view_buttom = (View) v.getParent();
			view_buttom.setVisibility(View.GONE);
			break;

		case R.id.broad_edit_ok:
			int page2 = Integer.valueOf(ed_page.getText().toString());
			if (page2 > mPage_MAX || page2 < 1) {
				Toast.makeText(activity, "不正确页数", 1).show();
			} else {
				page = page2;
				loadData();
			}

			view_input.setVisibility(View.GONE);
			view_buttom.setVisibility(View.VISIBLE);

			closeImm();
			break;
		case R.id.search_button:
			PageDialog dialog = new PageDialog(activity);
			dialog.show();
			break;
		case R.id.page_up:
			page = page + 1;
			if (Math.min(page - 1, mPage_MAX) == mPage_MAX) {
				page = mPage_MAX;
				Toast.makeText(activity, "当前已经是最后一页", 1).show();
				return;
			}
			mDoSearch = true;
			loadData();

			break;

		case R.id.page_down:
			page = page - 1;
			mDoSearch = true;
			if (Math.max(page + 1, 1) == 1) {
				page = 1;
				Toast.makeText(activity, "当前已经是第一页", 1).show();
				return;
			}
			loadData();
			break;
		case R.id.search:
			new AlertDialog.Builder(this)
					.setTitle("请输入搜索内容")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(getEdittext())
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									isSearch = true;

									// EditText ed = (EditText) activity
									// .findViewById(R.id.myEditText);
									content = edit.getText().toString();
									if (TextUtils.isEmpty(content)) {
										Toast.makeText(activity, "搜索内容不能为空",
												Toast.LENGTH_SHORT).show();
									} else {
										mDoSearch = false;
										page = 1;
										loadData();
									}
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}
							}).show();
			break;
		default:
			break;
		}
	}

	EditText edit;
	boolean mDoSearch = false;

	private EditText getEdittext() {
		// TODO Auto-generated method stub
		edit = new EditText(activity);
		edit.setId(R.id.myEditText);
		edit.setHint("请输入搜索信息");
		edit.setSingleLine();
		return edit;
	}

	@Override
	protected BaseConfigLoader getConfigLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getQueryAction() {
		// TODO Auto-generated method stub
		if (isSearch) {
			return "/bi/hbmobile/network/searchResults";
		} else
			return "/bi/hbmobile/network/getDetailInfo";
	}

	boolean isSearch = false;

	@Override
	protected void loadData() {

		// if (first) {
		System.out.println("aaaaaaaaaaaaa");
		optime = DateUtil.formatter(date_button.getDate(), DateUtil.PATTERN_8);

		// }
		// isSearch = true;

		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("menuCode",
				getIntent().getStringExtra(MenuEntryAdapter.KEY_MENU_CODE));
		if (isSearch) {
			// page = 1;
			// System.out.println("content:" + content);
			// if (mDoSearch) {
			param.put("page", String.valueOf(page));
			// } else {
			// param.put("page", String.valueOf(1));
			// }
			param.put("keyWord", content);
			param.put("selectedTime", optime);
		} else {

			param.put("page", String.valueOf(page));
			param.put("sortBy", null);
			param.put("sTime", optime);
			param.put("eTime", optime);
		}
		// http://192.168.1.114:9080

		if (isSearch) {
			new LoadDataTask(this, Constant.BASE_PATH).execute(
					getQueryAction(), param);
		} else {
			new LoadDataTask(this, Constant.BASE_PATH).execute(
					getQueryAction(), param);
		}

	}

	class PageDialog extends Dialog implements OnClickListener {

		public PageDialog(Context context) {
			// super(context);
			super(context, R.style.ShareDialog);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.page_dialog);
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			dismiss();
		}

	}

	private void changeImm() {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		// InputMethodManager imm = (InputMethodManager) activity
		// .getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

	}

	void openImm() {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(dataView, InputMethodManager.SHOW_FORCED);
	}

	void closeImm() {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(BroadDetailActivity.this.getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
