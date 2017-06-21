
package com.bonc.mobile.common.activity;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupWindow;

import com.bonc.mobile.common.R;
import com.bonc.mobile.common.data.BaseConfigLoader;
import com.bonc.mobile.common.net.HttpRequestTask;
import com.bonc.mobile.common.util.TextViewUtils;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.common.view.DataChooseButton;
import com.bonc.mobile.common.view.DatePickerButton;
import com.bonc.mobile.common.view.DataChooseButton.OnDataChooseListener;

/**
 * @author sunwei
 */
public class BaseDataActivity extends BaseActivity {
    protected String menuCode;
    protected DatePickerButton date_button;
    protected DataChooseButton area_button;
    protected PopupWindow info_window;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBaseData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (info_window != null)
            info_window.dismiss();
    }

    protected void initBaseData() {
        menuCode = getIntent().getStringExtra(BaseConfigLoader.KEY_MENU_CODE);
    }

    protected void initView() {
        setWatermarkImage();
        initDateSelect();
        initAreaSelect();
    }

    protected void resetView() {
    }

    protected void loadData() {
        resetView();
    }

    protected void bindData(JSONObject result) {
    }

    protected void bindData(JSONArray result) {
    }

    protected void initDateSelect() {
        date_button = (DatePickerButton) findViewById(R.id.date_button);
        if (date_button != null) {
            date_button.setOnDateSetListener(new OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    loadData();
                }
            });
        }
    }

    protected void initAreaSelect() {
        area_button = (DataChooseButton) findViewById(R.id.area_button);
        if (area_button!= null){
            area_button.setOnDataChooseListener(new OnDataChooseListener() {
                @Override
                public void onChoose(Map<String, String> item) {
                    loadData();
                }
            });
        }
    }

    protected class LoadDataTask extends HttpRequestTask {
        public LoadDataTask(Context context) {
            super(context);
        }

        public LoadDataTask(Context context, String basePath) {
            super(context, basePath);
        }

        @Override
        protected void handleResult(JSONObject result) {
            bindData(result);
        }

        @Override
        protected void handleResult(JSONArray result) {
            bindData(result);
        }
    }

    protected void showDataError() {
        showToast(getString(R.string.data_error));
    }

    protected void showNoData() {
        showToast(getString(R.string.no_data));
    }

    protected void showInfowin(View anchor, String text){
        if (text == null)
            return;
        View v = View.inflate(this, R.layout.info_window, null);
        TextViewUtils.setText(v, R.id.text, text);
        if (info_window != null)
            info_window.dismiss();
        info_window = UIUtil.showInfoWindow(this, v, anchor);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                info_window.dismiss();
            }
        }, 3000);
    }
}
