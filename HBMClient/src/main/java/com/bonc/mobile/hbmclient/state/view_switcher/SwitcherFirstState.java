package com.bonc.mobile.hbmclient.state.view_switcher;

public class SwitcherFirstState extends SimpleState {

	public SwitcherFirstState(AnnouncementSwitcher machine) {
		// TODO Auto-generated constructor stub
		this.machine = machine;
		this.layout = machine.getCurrentView();
		initialWidget();
	}

	@Override
	public void entry(int index) {
		// TODO Auto-generated method stub
		this.setAnnouncementData(index);
	}

	@Override
	public void out() {
		// TODO Auto-generated method stub
		this.machine.setState(this.machine.getSecondState());
	}

}
