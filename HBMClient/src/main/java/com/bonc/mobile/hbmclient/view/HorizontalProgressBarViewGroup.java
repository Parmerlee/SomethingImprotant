package com.bonc.mobile.hbmclient.view;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.bonc.mobile.hbmclient.util.NumberUtil;
import com.bonc.mobile.hbmclient.util.StringUtil;

/**
 * @author liweigao
 *
 */
public class HorizontalProgressBarViewGroup extends LinearLayout {
	private int CHILD_NUM = 5;
	private Context context;
	private SingleHorizontalProgressBarItem[] childView;

	public HorizontalProgressBarViewGroup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public HorizontalProgressBarViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void dispatchView() {
		childView = new SingleHorizontalProgressBarItem[CHILD_NUM];
		for (int i = 0; i < CHILD_NUM; i++) {
			childView[i] = new SingleHorizontalProgressBarItem(this.context);
			this.addView(childView[i].getView());
		}

		for (int i = 0; i < CHILD_NUM; i++) {
			this.childView[i].dispatchView();
		}
	}

	public void dispatchView(int childnum) {
		CHILD_NUM = childnum;
		dispatchView();
	}

	public void setData(List<Map<String, String>> data) {
		/*
		 * for(int i=0;i<CHILD_NUM;i++) { this.childView[i].setLeftMark("销售新机");
		 * this.childView[i].setLeftValue("100");
		 * this.childView[i].setProgress(50);
		 * this.childView[i].setPbLeft("30%");
		 * this.childView[i].setPbRight("70%");
		 * this.childView[i].setRightMark("销售库存");
		 * this.childView[i].setRightValue("20万台"); }
		 */
		this.setDefaultTextAndValue();

		if (data == null || data.size() == 0) {
			return;
		}

		int len = data.size();
		for (int i = 0; i < len && i < CHILD_NUM; i++) {
			Map<String, String> tempMap = data.get(i);
			if (tempMap == null || tempMap.size() == 0) {
				// this.setDefaultTextAndValue(i);
				continue;
			}
			this.childView[i].setLeftMark(tempMap.get("leftName"));
			this.childView[i].setRightMark(tempMap.get("rightName"));
			String left = tempMap.get("pbLeft");
			String right = tempMap.get("pbRight");
			if (!StringUtil.isNull(left)) {
				int leftValue = NumberUtil.changeToInt(left);
				int rightValue = NumberUtil.changeToInt(right);
				boolean isNumberValid = true;
				if (leftValue == 0 && rightValue == 0) {
					isNumberValid = false;
				}
				if (isNumberValid) {
					this.childView[i].setLeftValue(tempMap.get("leftValue"));
					this.childView[i].setRightValue(tempMap.get("rightValue"));
					this.childView[i].setProgress(leftValue);
					this.childView[i].setPbLeft(left + "%");
					this.childView[i].setPbRight((100 - leftValue) + "%");
				}
				// this.childView[i].setPbRight(tempMap.get("pbRight")+"%");
			}
		}
	}

	private void setDefaultTextAndValue() {
		for (int i = 0; i < this.childView.length; i++) {
			// this.childView[i].setLeftMark("--");
			this.childView[i].setLeftValue("--");
			this.childView[i].setProgress(0);
			this.childView[i].setPbLeft("--%");
			this.childView[i].setPbRight("--%");
			// this.childView[i].setRightMark("--");
			this.childView[i].setRightValue("--");
		}
	}

	public void updateView() {
		for (int i = 0; i < CHILD_NUM; i++) {
			this.childView[i].updateView();
		}
	}
}
