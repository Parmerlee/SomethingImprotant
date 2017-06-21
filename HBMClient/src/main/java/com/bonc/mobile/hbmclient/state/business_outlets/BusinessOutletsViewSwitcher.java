/**
 * BusinessOutletsViewSwitcher
 */
package com.bonc.mobile.hbmclient.state.business_outlets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import com.bonc.mobile.hbmclient.R;

/**
 * @author liweigao
 *
 */
public class BusinessOutletsViewSwitcher extends ViewSwitcher {
	private IBOState state, mInfoSummaryState, mFocusWebsiteState;
	private boolean firstInfoSummaryState = true;
	private boolean firstFocusWebsiteState = true;
	private String menuCode;

	/**
	 * @param context
	 * @param attrs
	 */
	public BusinessOutletsViewSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mInfoSummaryState = new InfoSummaryState(this);
		this.mFocusWebsiteState = new FocusWebsiteState(this);
		this.state = mInfoSummaryState;
	}

	public void initialSwitcherView(String menuCode) {
		this.menuCode = menuCode;
		ViewGroup.LayoutParams lp = new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		addView(this.mInfoSummaryState.getViewContainer(), 0, lp);
		addView(this.mFocusWebsiteState.getViewContainer(), 1, lp);
		this.state.firstEnter();
		this.firstInfoSummaryState = false;
	}

	/**
	 * @return the menuCode
	 */
	public String getMenuCode() {
		return menuCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ViewAnimator#showNext()
	 */
	@Override
	public void showNext() {
		setInAnimation(getContext(), R.anim.switcher_next_in);
		setOutAnimation(getContext(), R.anim.switcher_next_out);
		super.showNext();
		this.state = this.mFocusWebsiteState;
		if (this.firstFocusWebsiteState) {
			this.state.firstEnter();
			this.firstFocusWebsiteState = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ViewAnimator#showPrevious()
	 */
	@Override
	public void showPrevious() {
		setInAnimation(getContext(), R.anim.switcher_previous_in);
		setOutAnimation(getContext(), R.anim.switcher_previous_out);
		super.showPrevious();
		this.state = this.mInfoSummaryState;
		if (this.firstInfoSummaryState) {
			this.state.firstEnter();
			this.firstInfoSummaryState = false;
		}
	}

	public void onActivityResult(int code) {
		this.mFocusWebsiteState.onActivityResult(code);
	}
}
