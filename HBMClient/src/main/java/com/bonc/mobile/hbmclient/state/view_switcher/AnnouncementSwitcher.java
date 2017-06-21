package com.bonc.mobile.hbmclient.state.view_switcher;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.AnnouncementTool;
import com.bonc.mobile.hbmclient.observer.listener.SimpleGestureListener;
import com.bonc.mobile.hbmclient.util.DateUtil;

public class AnnouncementSwitcher extends ViewSwitcher {
	private State mSwitcherFirstState;
	private State mSwitcherSecondState;
	private State state;

	private Context mContext;

	private int indexShow = 0;
	private int[] idValue;
	private String[] dateValue;
	private String[] contentValue;
	private String[] statusValue;
	private int notReadNum = 0;
	private int wholeLength = 0;

	private boolean showToast = false;

	private AnnouncementTool mAnnouncementTool;

	private GestureDetector mGestureDetector;

	public AnnouncementSwitcher(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public AnnouncementSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		setFactory(new AnnouncementViewFactory(context));

		mSwitcherFirstState = new SwitcherFirstState(this);
		mSwitcherSecondState = new SwitcherSecondState(this);
		this.state = this.mSwitcherFirstState;
		SimpleGestureListener sgl = new SimpleGestureListener();
		sgl.setOnGestureFlingListener(new SimpleGestureListener.GestureFlingCallBack() {

			@Override
			public void onFlingRight() {
				showPreviousAnnouncement();
			}

			@Override
			public void onFlingLeft() {
				showNextAnnouncement();
			}
		});
		this.mGestureDetector = new GestureDetector(context, sgl);
		setLongClickable(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return mGestureDetector.onTouchEvent(ev);
	}

	public void setShowToast(boolean flag) {
		this.showToast = flag;
	}

	public void setAnnouncement(AnnouncementTool an) {
		this.mAnnouncementTool = an;
	}

	public void setState(State state) {
		this.state = state;
	}

	public State getFirstState() {
		return this.mSwitcherFirstState;
	}

	public State getSecondState() {
		return this.mSwitcherSecondState;
	}

	public String[] getDateValue() {
		return this.dateValue;
	}

	public String[] getContentValue() {
		return this.contentValue;
	}

	public void setData(List<Map<String, String>> data) {
		this.wholeLength = data.size();
		this.idValue = new int[this.wholeLength];
		this.dateValue = new String[this.wholeLength];
		this.contentValue = new String[this.wholeLength];
		this.statusValue = new String[this.wholeLength];
		for (int i = 0; i < this.wholeLength; i++) {
			statusValue[i] = data.get(i).get("MessageStatus");
			String createdate = data.get(i).get("Create_date");
			dateValue[i] = DateUtil.oneStringToAntherString(createdate,
					DateUtil.PATTERN_8, "yyyy年MM月dd日");
			if ("0".equals(statusValue[i])) {
				notReadNum++;
				dateValue[i] += "(未读)";
			}
			contentValue[i] = data.get(i).get("Message");
			idValue[i] = Integer.parseInt(data.get(i).get("MessageId"));
		}
		showInitialAnnouncement();
		toastNotReadAnnouncement();
	}

	public int getWholeLength() {
		return this.wholeLength;
	}

	// 显示初始界面
	public void showInitialAnnouncement() {
		this.indexShow = 0;
		this.state.entry(this.indexShow);
		mAnnouncementTool.updateDB(idValue[this.indexShow]);
	}

	// 显示下一条公告
	public void showNextAnnouncement() {
		if (this.indexShow < this.wholeLength - 1) {
			this.indexShow++;
			setInAnimation(mContext, R.anim.switcher_next_in);
			setOutAnimation(mContext, R.anim.switcher_next_out);
			showNext();
			mAnnouncementTool.updateDB(idValue[this.indexShow]);
		}
	}

	// 显示上一条公告
	public void showPreviousAnnouncement() {
		if (this.indexShow > 0) {
			this.indexShow--;
			setInAnimation(mContext, R.anim.switcher_previous_in);
			setOutAnimation(mContext, R.anim.switcher_previous_out);
			showNext();
		}
	}

	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		this.state.out();
		this.state.entry(this.indexShow);
		super.showNext();
	}

	private void toastNotReadAnnouncement() {
		if (this.notReadNum > 0) {
			if (showToast) {
				Toast.makeText(mContext, "您有" + this.notReadNum + "条未读公告",
						Toast.LENGTH_SHORT).show();
				this.notReadNum = 0;
			}
		}
	}

	public void setHeight(int h) {
		getLayoutParams().height = h;
	}
}
