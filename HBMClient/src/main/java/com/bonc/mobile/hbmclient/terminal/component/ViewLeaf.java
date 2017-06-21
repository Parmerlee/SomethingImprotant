/**
 * TODO
 */
package com.bonc.mobile.hbmclient.terminal.component;

import android.content.Context;
import android.view.View;

import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;

/**
 * @author liweigao
 * 
 */
public abstract class ViewLeaf extends ViewComponent {
	protected View mView;

	/**
	 * @param c
	 * @param tae
	 */
	public ViewLeaf(Context c, TerminalActivityEnum tae) {
		super(c, tae);
		// TODO Auto-generated constructor stub
	}

}
