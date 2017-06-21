package com.bonc.mobile.hbmclient.observer;

/**
 * @author liweigao
 *         observable还可以有setChanged()方法和clearChanged()方法，以及hasChanged()方法 。
 *         这3个方法可以提供是否有刷新必要的判断
 */
public interface IObservable {
	public void registerObserver(IObserver o);

	public void removeObserver(IObserver o);

	public void notifyObservers();

	public void notifyObservers(Object arg);

	public void setChanged();

	public void clearChanged();

	public boolean hasChanged();
}
