package com.bonc.mobile.hbmclient.command;

import android.os.Bundle;

/**
 * @author liweigao
 *
 */
public interface ICommand {
	public void execute();

	public void execute(Object arg);

	public void execute(Bundle bundle);

	public void undo();
}
