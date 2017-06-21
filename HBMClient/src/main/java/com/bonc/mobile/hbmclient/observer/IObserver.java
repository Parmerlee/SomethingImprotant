package com.bonc.mobile.hbmclient.observer;

/**
 * @author liweigao
 *
 */
public interface IObserver {
	public void update();

	public void update(IObservable o);

	public void update(IObservable o, Object arg);

	public void update(Object arg);
}