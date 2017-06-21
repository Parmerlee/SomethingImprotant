package com.bonc.mobile.hbmclient.command.date_change;

import java.util.Calendar;

import com.bonc.mobile.hbmclient.command.ACommand;
import com.bonc.mobile.hbmclient.observer.data_presentation.DataPresentationObservable;

/**
 * @author liweigao 参数也可以采用泛型进行传递，省掉向下转型，但是需要限制传递的参数类型
 */
public class DateChangeCommand extends ACommand {
	private DataPresentationObservable mDataPresentation;

	public DateChangeCommand(DataPresentationObservable dpo) {
		this.mDataPresentation = dpo;
	}

	@Override
	public void execute(Object arg) {
		// TODO Auto-generated method stub
		this.mDataPresentation.setDate((Calendar) arg);
	}

}
