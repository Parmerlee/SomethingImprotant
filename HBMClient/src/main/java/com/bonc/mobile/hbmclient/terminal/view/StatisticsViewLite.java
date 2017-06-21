package com.bonc.mobile.hbmclient.terminal.view;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.util.LogUtil;

public class StatisticsViewLite extends LinearLayout {
	protected static int LAYOUT_ID = R.layout.statistic_lite_v2;
	protected String[] mKeyNames = new String[4];
	protected String[] mKeyValues = new String[4];
	protected List<View> mKeyNameViews;
	protected List<View> mKeyValueViews;

	private int mBaseID = LAYOUT_ID;

	public StatisticsViewLite(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public StatisticsViewLite(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	protected void init() {
		inflate(getContext(), mBaseID, this);
		getChildCount();
		setOrientation(VERTICAL);
		mKeyNameViews = new ArrayList<View>();
		mKeyValueViews = new ArrayList<View>();
		archiveViewsByTag(this);
		LogUtil.info("length",
				mKeyNameViews.size() + " " + mKeyValueViews.size());
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		// getLayoutParams().height=LayoutParams.WRAP_CONTENT;
		getLayoutParams().height = LayoutParams.WRAP_CONTENT;
		;
	}

	public int getBaseID() {
		return mBaseID;
	}

	protected void setBaseID(int mBaseID) {
		this.mBaseID = mBaseID;
	}

	/** 遍历整个视图并将符合tag的keyView或valueView 归档存储 */
	protected boolean archiveViewsByTag(ViewGroup group) {
		int lengh = group.getChildCount();
		int cc = 0;
		for (int i = 0; i < lengh; i++) {
			if (group.getChildAt(i).getTag() == null) {
				if (group.getChildAt(i) instanceof ViewGroup) {
					archiveViewsByTag((ViewGroup) group.getChildAt(i));
				}
			} else if (group.getChildAt(i).getTag().equals("key")) {
				mKeyNameViews.add(group.getChildAt(i));
				cc++;
			} else if (group.getChildAt(i).getTag().equals("value")) {
				mKeyValueViews.add(group.getChildAt(i));
			}
		}
		return false;
	}

	/** 向各个值的标签填充字符串 */
	public void setKeyNames(String[] strings) {
		for (int i = 0; i < mKeyNames.length; i++) {
			mKeyNames[i] = strings[i];
			View v = mKeyNameViews.get(i);
			try {
				TextView tv = (TextView) v;
				tv.setText(strings[i]);
			} catch (ClassCastException e) {
				// TODO: handle exception
				LogUtil.error("Tagged View  class error",
						"View tagged \"key\" must be a TextView or its subclass");
			}
		}
	}

	/** 向各个值填充数据 */
	public void setKeyValues(String[] strings) {
		for (int i = 0; i < mKeyValues.length; i++) {
			mKeyValues[i] = strings[i];
			View v = mKeyValueViews.get(i);
			try {
				if (v instanceof NumberCounterView) {

					NumberCounterView nv = (NumberCounterView) v;
					nv.setNumberByString(strings[i]);
				} else if (v instanceof NumberCounterViewMini) {
					NumberCounterViewMini nv = (NumberCounterViewMini) v;
					nv.setNumberByString(strings[i]);
				} else {
					TextView tv = (TextView) v;
					String nums = "";
					double num = 0;
					if (strings[i] == null || strings[i].equals("-")) {
						nums = "--";
						tv.setText(nums);
						tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					} else if (strings[i].equals("∞")) {
						nums = "∞";
						tv.setText(nums);
						tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					} else {
						num = Double.parseDouble("".equals(strings[i]) ? "0"
								: strings[i]);
						num *= 100;
						NumberFormat format = NumberFormat.getInstance();
						format.setGroupingUsed(false);
						format.setMaximumFractionDigits(0);
						nums = format.format(num);
						if (nums.length() >= 4) {
							tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
						} else {
							tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
						}
						tv.setText(nums + "% ");
						if (num > 0) {
							tv.setCompoundDrawablesWithIntrinsicBounds(0, 0,
									R.mipmap.triangle_upward, 0);
						} else {
							tv.setCompoundDrawablesWithIntrinsicBounds(0, 0,
									R.mipmap.triangle_downward, 0);
						}
					}

				}
			} catch (ClassCastException e) {
				// TODO: handle exception
				LogUtil.error("Tagged View  class error",
						"View tagged \"value\" must be a TextView or a NumberCounterView");
			}
		}
	}

	public void setAllClassifier(String classifier) {
		for (View v : mKeyValueViews) {
			if (v instanceof NumberCounterView) {
				if (v.getId() != R.id.statistic_month_resist_label_view
						&& v.getId() != R.id.statistic_month_resist_days_view) {
					((NumberCounterView) v).setUnitString(classifier);
				}
			} else if (v instanceof NumberCounterViewMini) {
				if (v.getId() != R.id.statistic_month_resist_days_view) {
					((NumberCounterViewMini) v).setUnitString(classifier);
				}

			}
		}
	}

	public void setSupportClassifier(String classifier) {
		for (View v : mKeyValueViews) {
			if (v instanceof NumberCounterView) {
				if (v.getId() == R.id.statistic_month_resist_days_view) {
					((NumberCounterView) v).setUnitString(classifier);
				}
			}
		}
	}

	public int getCurrentNameArrayLength() {
		if (mKeyNames == null) {
			return 0;
		} else
			return mKeyNames.length;
	}

	public int getCurrentValueArrayLength() {
		if (mKeyValues == null) {
			return 0;
		} else
			return mKeyValues.length;
	}
}
