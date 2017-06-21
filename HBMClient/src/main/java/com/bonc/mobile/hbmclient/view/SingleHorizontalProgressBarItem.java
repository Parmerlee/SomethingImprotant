/**
 * TODO
 */
package com.bonc.mobile.hbmclient.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

/**
 * @author liweigao
 * 
 */
public class SingleHorizontalProgressBarItem {
	private View mView;

	private TextView leftMarkTV;
	private TextView leftValueTV;
	private ProgressBar mProgressBar;
	private TextView pbLeftTV;
	private TextView pbRightTV;
	private TextView rightMarkTV;
	private TextView rightValueTV;

	private String leftMark;
	private String leftValue;
	private int progress;
	private String pbLeft;
	private String pbRight;
	private String rightMark;
	private String rightValue;

	/**
	 * @param c
	 * @param tae
	 */
	public SingleHorizontalProgressBarItem(Context context) {
		// TODO Auto-generated constructor stub
		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mView = li.inflate(R.layout.single_horizontal_progressbar_layout,
				null);
	}

	public View getView() {
		return this.mView;
	}

	public void dispatchView() {
		// TODO Auto-generated method stub
		this.leftMarkTV = (TextView) this.mView.findViewById(R.id.id_left_mark);
		this.leftValueTV = (TextView) this.mView
				.findViewById(R.id.id_left_value);
		this.mProgressBar = (ProgressBar) this.mView
				.findViewById(R.id.id_progressbar);
		this.pbLeftTV = (TextView) this.mView.findViewById(R.id.id_pb_left);
		this.pbRightTV = (TextView) this.mView.findViewById(R.id.id_pb_right);
		this.rightMarkTV = (TextView) this.mView
				.findViewById(R.id.id_right_mark);
		this.rightValueTV = (TextView) this.mView
				.findViewById(R.id.id_right_value);
	}

	public void setLeftMark(String s) {
		this.leftMark = s;
	}

	public void setLeftValue(String s) {
		this.leftValue = s;
	}

	public void setProgress(int p) {
		this.progress = p;
	}

	public void setPbLeft(String s) {
		this.pbLeft = s;
	}

	public void setPbRight(String s) {
		this.pbRight = s;
	}

	public void setRightMark(String s) {
		this.rightMark = s;
	}

	public void setRightValue(String s) {
		this.rightValue = s;
	}

	public void updateView() {
		this.leftMarkTV.setText(leftMark);
		this.leftValueTV.setText(leftValue);
		this.mProgressBar.setProgress(progress);
		this.pbLeftTV.setText(pbLeft);
		this.pbRightTV.setText(pbRight);
		this.rightMarkTV.setText(rightMark);
		this.rightValueTV.setText(rightValue);
	}

}
