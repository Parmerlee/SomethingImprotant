package com.bonc.mobile.hbmclient.activity;

import java.util.List;

import com.bonc.mobile.common.util.MyUtils;
import com.bonc.mobile.hbmclient.view.PatternView;
import com.bonc.mobile.hbmclient.view.PatternView.Cell;

import android.app.Activity;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

public class PatternActivity extends Activity {
	protected PatternView patternView;
	protected TextView hint;
	protected Handler handler = new Handler();

	protected String getPassword(List<Cell> pattern) {
		StringBuffer sb = new StringBuffer();
		for (Cell cell : pattern) {
			sb.append(cell.getRow() * 3 + cell.getColumn() + 1);
		}
		return sb.toString();
	}

	protected void resetPattern() {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				patternView.clearPattern();
				patternView.enableInput();
			}
		}, 1000);
	}

	protected void showToast(String toastMsg) {
		Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// startArrowAni();
		if (MyUtils.doInfilter(PatternActivity.this))
			if (!MyUtils.isBackground(this)) {
				MyUtils.startProtal(this);
			}
	}

}
