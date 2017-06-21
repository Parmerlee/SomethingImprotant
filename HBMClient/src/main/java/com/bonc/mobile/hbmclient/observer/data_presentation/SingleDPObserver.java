/**
 * 
 */
package com.bonc.mobile.hbmclient.observer.data_presentation;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.observer.IObservable;
import com.bonc.mobile.hbmclient.util.FirstScanUtil;
import com.bonc.mobile.hbmclient.view.adapter.SingleDataPresentationAdapter;

/**
 * @author liweigao
 *
 */
public class SingleDPObserver extends TemplateDataPresentationObserver {
	private SingleDPObservable mObservable;

	private Dialog dialog;
	private TextView title;
	private ListView content;
	private Button close;
	private TextView emptyView;
	private ImageView mFloatView;
	private ProgressBar mProgressBar;

	private FirstScanUtil mFSU = FirstScanUtil.getSingleInstance();

	/**
	 * @param c
	 */
	public SingleDPObserver(IObservable dp, Context c, ImageView iv) {
		super(c);
		this.mObservable = (SingleDPObservable) dp;
		this.mObservable.registerObserver(this);

		this.mFloatView = iv;
		initialWidget();
	}

	public boolean isDialogShowing() {
		return this.dialog.isShowing();
	}

	public void hideView() {
		if (this.dialog.isShowing()) {
			this.dialog.hide();
		}
	}

	public void unHideView() {
		this.dialog.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.observer.data_presentation.
	 * TemplateDataPresentationObserver#updateView()
	 */
	@Override
	protected void updateView() {
		if (mFSU.isFirstScan()) {
			if (this.mObservable.getChildData().size() <= 0) {

			} else {
				if (this.dialog.isShowing()) {

				} else {
					this.dialog.show();
				}

				this.title.setText(this.mObservable.getTitle());
				SingleDataPresentationAdapter sdpa = new SingleDataPresentationAdapter(
						this.mObservable.getChildData(), context);
				this.content.setAdapter(sdpa);
			}
		} else {
			if (this.dialog.isShowing()) {

			} else {
				this.dialog.show();
			}
			endProgressDialog();

			this.title.setText(this.mObservable.getTitle());
			SingleDataPresentationAdapter sdpa = new SingleDataPresentationAdapter(
					this.mObservable.getChildData(), context);
			this.content.setAdapter(sdpa);
		}
	}

	@Override
	protected void showProgressDialog() {
		// TODO Auto-generated method stub
		if (mFSU.isFirstScan()) {

		} else {
			if (this.dialog.isShowing()) {

			} else {
				this.dialog.show();
			}
			this.emptyView.setText("");
			this.mProgressBar.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void endProgressDialog() {
		// TODO Auto-generated method stub
		if (this.dialog.isShowing()) {
			this.mProgressBar.setVisibility(View.GONE);
		}
		this.emptyView.setText("暂时没有简报数据");
	}

	@Override
	protected void showException() {
		// TODO Auto-generated method stub
		if (FirstScanUtil.getSingleInstance().isFirstScan()) {

		} else {
			updateView();
		}
	}

	@Override
	protected void showNoData() {
		// TODO Auto-generated method stub
		updateView();
	}

	private void initialWidget() {
		this.dialog = new Dialog(context, R.style.custom_dialog);
		this.dialog.setContentView(R.layout.single_data_presentation_layout);
		this.dialog.setCancelable(true);
		this.dialog.setCanceledOnTouchOutside(true);
		this.dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
		this.dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				mFloatView.setVisibility(View.VISIBLE);
			}
		});

		this.title = (TextView) dialog.findViewById(R.id.id_title);
		this.content = (ListView) dialog.findViewById(R.id.id_listview);
		this.emptyView = (TextView) dialog.findViewById(R.id.id_emptyview);
		this.content.setEmptyView(emptyView);
		this.mProgressBar = (ProgressBar) dialog
				.findViewById(R.id.id_progressbar);
		this.close = (Button) dialog.findViewById(R.id.id_close);
		this.close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}

}
