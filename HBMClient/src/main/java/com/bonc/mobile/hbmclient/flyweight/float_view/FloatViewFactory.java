/**
 * 
 */
package com.bonc.mobile.hbmclient.flyweight.float_view;

import android.app.Application;

/**
 * @author liweigao 共享部分和非共享部分需要在该类里面创建
 */
public class FloatViewFactory {
	private volatile static FloatViewFactory mFloatViewFactory = null;

	private Application mApplication;

	private FloatViewFactory() {
	};

	public static FloatViewFactory getSingleInstance() {
		if (mFloatViewFactory == null) {
			synchronized (FloatViewFactory.class) {
				if (mFloatViewFactory == null) {
					mFloatViewFactory = new FloatViewFactory();
				}
			}
		}
		return mFloatViewFactory;
	}

	public void setApplication(Application app) {
		this.mApplication = app;
	}

	public FloatView createFloatView() {
		return new DPFloatView(mApplication);
	}
}
