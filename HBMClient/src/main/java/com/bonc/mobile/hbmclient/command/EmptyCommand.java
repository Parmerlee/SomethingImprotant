package com.bonc.mobile.hbmclient.command;

import android.os.Bundle;

/**
 * @author liweigao 空命令可以对命令调用方进行预加载，可以避免判断command是否为null
 */
public class EmptyCommand implements ICommand {

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.command.ICommand#undo()
	 */
	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.command.ICommand#execute(java.lang.Object)
	 */
	@Override
	public void execute(Object arg) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.command.ICommand#execute(android.os.Bundle)
	 */
	@Override
	public void execute(Bundle bundle) {
		// TODO Auto-generated method stub

	}

}
