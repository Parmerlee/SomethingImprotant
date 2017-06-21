/**
 * ALevelKpiComponent
 */
package com.bonc.mobile.hbmclient.composite.levelkpi;

import android.view.View;

import com.bonc.mobile.hbmclient.composite.IComposite;

/**
 * @author liweigao
 *
 */
public interface ILevelKpiComponent<T, D> extends IComposite<T> {
	void constructView();

	void setHeight();

	void setWidth();

	View getView();

	void setData(D data);

	void setFirstLevelStyle();

	void setSecondLevelStyle();

	void setMainKpiStyle();

	void setRelationKpiStyle();

	void iteratorSetFirstLevelStyle();

	void iteratorSetSecondLevelStyle();

	void iteratorSetMainKpiStyle();

	void iteratorSetRelationKpiStyle();

	void iteratorSetData(D data);

	void iteratorConstructView();

	View iteratorGetView();
}
