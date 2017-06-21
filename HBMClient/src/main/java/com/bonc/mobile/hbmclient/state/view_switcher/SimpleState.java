package com.bonc.mobile.hbmclient.state.view_switcher;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;

public abstract class SimpleState implements State {
	protected AnnouncementSwitcher machine;
	protected View layout;
	protected ImageView previous;
	protected ImageView next;
	protected TextView date;
	protected TextView content;

	private final String TAG = "SimpleState";

	protected void initialWidget() {
		this.previous = (ImageView) this.layout.findViewById(R.id.frontitem);
		this.next = (ImageView) this.layout.findViewById(R.id.nextitem);
		this.date = (TextView) this.layout.findViewById(R.id.textviewdate);
		this.content = (TextView) this.layout
				.findViewById(R.id.textviewcontent);

		this.previous.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				machine.showPreviousAnnouncement();
			}
		});

		this.next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				machine.showNextAnnouncement();
			}
		});
	}

	protected void setAnnouncementData(int index) {
		arbitrationImage(index);
		setText(index);
	}

	private void arbitrationImage(int index) {
		int length = this.machine.getWholeLength();
		if (index == 0) {
			previous.setImageResource(R.mipmap.img_icon_frontitemgray);
		}
		if (index == length - 1) {
			next.setImageResource(R.mipmap.img_icon_nexttitem1);
		}
		if (index < length - 1) {
			next.setImageResource(R.mipmap.img_icon_nexttitem);
		}
		if (index > 0) {
			previous.setImageResource(R.mipmap.img_icon_frontitemgray1);
		}
	}

	private void setText(int index) {
		this.date.setText((this.machine.getDateValue())[index]);
		this.content.setText((this.machine.getContentValue())[index]);
	}
}
