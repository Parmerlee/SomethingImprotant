/**
 * 
 */
package com.bonc.mobile.hbmclient.observer;

import java.util.ArrayList;

/**
 * @author liweigao
 *
 */
public abstract class AObservable implements IObservable {

	private ArrayList<IObserver> mObserverList = new ArrayList<IObserver>();
	private boolean changed = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.observer.Subject#registerObserver(com.bonc.
	 * mobile.hbmclient.observer.Observer)
	 */
	@Override
	public void registerObserver(IObserver o) {
		// TODO Auto-generated method stub
		this.mObserverList.add(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.observer.Subject#removeObserver(com.bonc.mobile
	 * .hbmclient.observer.Observer)
	 */
	@Override
	public void removeObserver(IObserver o) {
		// TODO Auto-generated method stub
		int index = mObserverList.indexOf(o);
		if (index >= 0) {
			mObserverList.remove(index);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.observer.Subject#notifyObservers()
	 */
	@Override
	public void notifyObservers() {
		// TODO Auto-generated method stub
		if (changed) {
			for (int i = 0; i < mObserverList.size(); i++) {
				IObserver o = mObserverList.get(i);
				o.update();
			}
			changed = false;
		}

	}

	/*
	 * @Override public void notifyObservers() { // TODO Auto-generated method
	 * stub notifyObservers(null); }
	 */

	@Override
	public void notifyObservers(Object arg) {
		// TODO Auto-generated method stub
		if (changed) {
			for (int i = 0; i < mObserverList.size(); i++) {
				IObserver o = mObserverList.get(i);
				o.update(arg);
			}
			changed = false;
		}
	}

	@Override
	public void setChanged() {
		// TODO Auto-generated method stub
		this.changed = true;
	}

	@Override
	public void clearChanged() {
		// TODO Auto-generated method stub
		this.changed = false;
	}

	@Override
	public boolean hasChanged() {
		// TODO Auto-generated method stub
		return this.changed;
	}

}
