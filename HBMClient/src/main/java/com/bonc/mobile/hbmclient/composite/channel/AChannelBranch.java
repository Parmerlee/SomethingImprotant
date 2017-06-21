/**
 * AChannelBranch
 */
package com.bonc.mobile.hbmclient.composite.channel;

import java.util.ArrayList;
import java.util.Iterator;

import android.view.View;

/**
 * @author liweigao
 *
 */
public abstract class AChannelBranch implements
		IChannelComposite<IChannelComposite> {
	private ArrayList<IChannelComposite> components = new ArrayList<IChannelComposite>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.IComposite#add(java.lang.Object)
	 */
	@Override
	public void add(IChannelComposite object) {
		this.components.add(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.IComposite#remove(java.lang.Object)
	 */
	@Override
	public void remove(IChannelComposite object) {
		this.components.remove(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.channel.IChannelComposite#setData
	 * (java.lang.Object)
	 */
	@Override
	public void setData(Object data) {
		iteratorSetData(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.channel.IChannelComposite#iteratorSetData
	 * (java.lang.Object)
	 */
	@Override
	public void iteratorSetData(Object data) {
		// setData(data);
		Iterator it = components.iterator();
		while (it.hasNext()) {
			IChannelComposite component = (IChannelComposite) it.next();
			component.setData(data);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.channel.IChannelComposite#dispatchView
	 * (android.view.View)
	 */
	@Override
	public void dispatchView(View view) {
		iteratorDispatchView(view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.channel.IChannelComposite#
	 * iteratorDispatchView(android.view.View)
	 */
	@Override
	public void iteratorDispatchView(View view) {
		// dispatchView(view);
		Iterator it = components.iterator();
		while (it.hasNext()) {
			IChannelComposite component = (IChannelComposite) it.next();
			component.dispatchView(view);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.channel.IChannelComposite#
	 * setOutEnvironment(java.lang.Object)
	 */
	@Override
	public void setOutEnvironment(Object env) {
		iteratorSetOutEnvironment(env);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.channel.IChannelComposite#
	 * iteratorSetOutEnvironment(java.lang.Object)
	 */
	@Override
	public void iteratorSetOutEnvironment(Object env) {
		Iterator it = components.iterator();
		while (it.hasNext()) {
			IChannelComposite component = (IChannelComposite) it.next();
			component.setOutEnvironment(env);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.channel.IChannelComposite#reset()
	 */
	@Override
	public void reset() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.channel.IChannelComposite#iteratorReset
	 * ()
	 */
	@Override
	public void iteratorReset() {
		reset();
		Iterator it = components.iterator();
		while (it.hasNext()) {
			IChannelComposite component = (IChannelComposite) it.next();
			component.iteratorReset();
		}
	}

}
