package com.bonc.mobile.hbmclient.state.view_switcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ViewSwitcher.ViewFactory;

import com.bonc.mobile.hbmclient.R;

public class AnnouncementViewFactory implements ViewFactory {
	LayoutInflater mLayoutInflater;

	public AnnouncementViewFactory(Context context) {
		// TODO Auto-generated constructor stub
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View makeView() {
		// TODO Auto-generated method stub
		return mLayoutInflater.inflate(R.layout.announcement_top_layout, null);
	}

}
