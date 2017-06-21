/**
 * 
 */
package com.bonc.mobile.hbmclient.observer.data_presentation;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.bonc.mobile.hbmclient.enum_type.ProcedureEnum;
import com.bonc.mobile.hbmclient.observer.AObserver;

/**
 * @author liweigao
 *
 */
public abstract class TemplateDataPresentationObserver extends AObserver {
	private ProgressDialog mProgressDialog;

	protected Context context;

	public TemplateDataPresentationObserver(Context c) {
		this.context = c;
	}

	@Override
	public void update(Object arg) {
		// TODO Auto-generated method stub
		switch ((ProcedureEnum) arg) {
		case START:
			showProgressDialog();
			break;
		case END:
			updateView();
			break;
		case EXCEPTION:
			showException();
			break;
		case STATIC_END:
			showStaticEnd();
			break;
		default:

			break;
		}
	}

	abstract protected void updateView();

	protected void showStaticEnd() {
	}

	protected void showProgressDialog() {
		if (this.context != null)
			this.mProgressDialog = ProgressDialog.show(this.context, "提示",
					"数据加载中...", true, false);
	}

	protected void endProgressDialog() {
		if (this.context != null && this.mProgressDialog.isShowing()) {
			this.mProgressDialog.dismiss();
		}
	}

	protected void showException() {
		Toast.makeText(this.context, "数据加载异常！", Toast.LENGTH_SHORT).show();
		endProgressDialog();
	}

	protected void showNoData() {
		Toast.makeText(this.context, "没有数据", Toast.LENGTH_SHORT).show();
	}
}
