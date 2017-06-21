/**
 * ALevelKpiLeaf
 */
package com.bonc.mobile.hbmclient.composite.levelkpi;

import java.util.Map;

import android.content.Context;
import android.view.View;

import com.bonc.mobile.hbmclient.adapter.levelkpi.DimensionAdapter;

/**
 * @author liweigao
 *
 */
public abstract class ALevelKpiLeaf implements
		ILevelKpiComponent<ILevelKpiComponent, Map<String, String>> {
	protected Context context;
	protected DimensionAdapter dimenAdapter;

	public ALevelKpiLeaf(Context c) {
		this.context = c;
		this.dimenAdapter = new DimensionAdapter(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.IComposite#add(java.lang.Object)
	 */
	@Override
	final public void add(ILevelKpiComponent object) {
		throw new UnsupportedOperationException("叶子不应该调用此add操作");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.IComposite#remove(java.lang.Object)
	 */
	@Override
	final public void remove(ILevelKpiComponent object) {
		throw new UnsupportedOperationException("叶子不应该调用此remove操作");
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
		// TODO Auto-generated method stub
		setData(data);
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.levelkpi.ILevelKpiComponent#
	 * iteratorGetView()
	 */
	@Override
	public View iteratorGetView() {
		// TODO Auto-generated method stub
		return getView();
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
	}

}
