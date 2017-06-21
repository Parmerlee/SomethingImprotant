/**
 * IChannelComposite
 */
package com.bonc.mobile.hbmclient.composite.channel;

import android.view.View;

import com.bonc.mobile.hbmclient.composite.IComposite;

/**
 * @author liweigao
 *
 */
public interface IChannelComposite<T> extends IComposite<T> {
	void dispatchView(View view);

	void setData(Object data);

	void iteratorDispatchView(View view);

	void iteratorSetData(Object data);

	void setOutEnvironment(Object env);

	void iteratorSetOutEnvironment(Object env);

	void reset();

	void iteratorReset();
}
