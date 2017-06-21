/**
 * AChannelLeaf
 */
package com.bonc.mobile.hbmclient.composite.channel;

import android.view.View;

/**
 * @author liweigao
 *
 */
public abstract class AChannelLeaf implements
		IChannelComposite<IChannelComposite> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.IComposite#add(java.lang.Object)
	 */
	@Override
	public void add(IChannelComposite object) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.IComposite#remove(java.lang.Object)
	 */
	@Override
	public void remove(IChannelComposite object) {
		throw new UnsupportedOperationException();
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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.channel.IChannelComposite#
	 * iteratorDispatchView(android.view.View)
	 */
	@Override
	public void iteratorDispatchView(View view) {
		dispatchView(view);
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
		setData(data);
	}

}
