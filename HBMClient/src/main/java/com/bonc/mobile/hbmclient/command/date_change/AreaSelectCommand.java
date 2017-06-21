/**
 * AreaSelectCommand
 */
package com.bonc.mobile.hbmclient.command.date_change;

import com.bonc.mobile.hbmclient.adapter.IAdapter;
import com.bonc.mobile.hbmclient.command.ACommand;

/**
 * @author liweigao
 *
 */
public class AreaSelectCommand extends ACommand {
	private IAdapter adapter;

	public AreaSelectCommand(IAdapter adapter) {
		this.adapter = adapter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.command.ACommand#execute(java.lang.Object)
	 */
	@Override
	public void execute(Object arg) {
		// TODO Auto-generated method stub
		this.adapter.adapt(this, arg);
	}

}