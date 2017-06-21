/**
 * ALevelKpiBranch
 */
package com.bonc.mobile.hbmclient.composite.levelkpi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author liweigao
 *
 */
public abstract class ALevelKpiBranch implements
		ILevelKpiComponent<ILevelKpiComponent, Map<String, String>> {
	private ArrayList<ILevelKpiComponent> components = new ArrayList<ILevelKpiComponent>();
	protected Context context;

	public ALevelKpiBranch(Context c) {
		this.context = c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.IComposite#add(java.lang.Object)
	 */
	@Override
	public void add(ILevelKpiComponent object) {
		// TODO Auto-generated method stub
		this.components.add(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.IComposite#remove(java.lang.Object)
	 */
	@Override
	public void remove(ILevelKpiComponent object) {
		this.components.remove(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.levelkpi.ILevelKpiComponent#constructView
	 * ()
	 */
	@Override
	public void constructView() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiComponent#setHeight
	 * ()
	 */
	@Override
	public void setHeight() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiComponent#setWidth
	 * ()
	 */
	@Override
	public void setWidth() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.levelkpi.ALevelKpiComponent#getView()
	 */
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.levelkpi.ILevelKpiComponent#setData
	 * (java.lang.Object)
	 */
	@Override
	public void setData(Map<String, String> data) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ILevelKpiComponent#
	 * iteratorSetData(java.lang.Object)
	 */
	@Override
	public void iteratorSetData(Map<String, String> data) {
		setData(data);
		Iterator iterator = components.iterator();
		while (iterator.hasNext()) {
			ILevelKpiComponent component = (ILevelKpiComponent) iterator.next();
			component.iteratorSetData(data);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ILevelKpiComponent#
	 * iteratorConstructView()
	 */
	@Override
	public void iteratorConstructView() {
		constructView();
		Iterator iterator = components.iterator();
		while (iterator.hasNext()) {
			ILevelKpiComponent component = (ILevelKpiComponent) iterator.next();
			component.iteratorConstructView();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ILevelKpiComponent#
	 * iteratorGetView()
	 */
	@Override
	public View iteratorGetView() {
		ViewGroup vg = (ViewGroup) getView();
		Iterator iterator = components.iterator();
		while (iterator.hasNext()) {
			ILevelKpiComponent component = (ILevelKpiComponent) iterator.next();
			vg.addView(component.iteratorGetView());
		}
		return vg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ILevelKpiComponent#
	 * setFirstLevelStyle()
	 */
	@Override
	public void setFirstLevelStyle() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ILevelKpiComponent#
	 * setSecondLevelStyle()
	 */
	@Override
	public void setSecondLevelStyle() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ILevelKpiComponent#
	 * iteratorSetFirstLevelStyle()
	 */
	@Override
	public void iteratorSetFirstLevelStyle() {
		setFirstLevelStyle();
		Iterator iterator = components.iterator();
		while (iterator.hasNext()) {
			ILevelKpiComponent component = (ILevelKpiComponent) iterator.next();
			component.iteratorSetFirstLevelStyle();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ILevelKpiComponent#
	 * iteratorSetSecondLevelStyle()
	 */
	@Override
	public void iteratorSetSecondLevelStyle() {
		setSecondLevelStyle();
		Iterator iterator = components.iterator();
		while (iterator.hasNext()) {
			ILevelKpiComponent component = (ILevelKpiComponent) iterator.next();
			component.iteratorSetSecondLevelStyle();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ILevelKpiComponent#
	 * setMainKpiStyle()
	 */
	@Override
	public void setMainKpiStyle() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ILevelKpiComponent#
	 * iteratorSetMainKpiStyle()
	 */
	@Override
	public void iteratorSetMainKpiStyle() {
		setMainKpiStyle();
		Iterator iterator = components.iterator();
		while (iterator.hasNext()) {
			ILevelKpiComponent component = (ILevelKpiComponent) iterator.next();
			component.iteratorSetMainKpiStyle();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ILevelKpiComponent#
	 * setRelationKpiStyle()
	 */
	@Override
	public void setRelationKpiStyle() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ILevelKpiComponent#
	 * iteratorSetRelationKpiStyle()
	 */
	@Override
	public void iteratorSetRelationKpiStyle() {
		setRelationKpiStyle();
		Iterator iterator = components.iterator();
		while (iterator.hasNext()) {
			ILevelKpiComponent component = (ILevelKpiComponent) iterator.next();
			component.iteratorSetRelationKpiStyle();
		}
	}

}
