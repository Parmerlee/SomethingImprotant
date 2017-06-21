package com.bonc.mobile.hbmclient.state.view_switcher;

public class SwitcherSecondState extends SimpleState {

	public SwitcherSecondState(AnnouncementSwitcher machine) {
		// TODO Auto-generated constructor stub
		this.machine = machine;
		this.layout = machine.getNextView();
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
		this.machine.setState(this.machine.getFirstState());
	}

}
