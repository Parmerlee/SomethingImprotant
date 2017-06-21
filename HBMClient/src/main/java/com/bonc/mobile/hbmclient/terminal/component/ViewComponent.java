/**
 * TODO
 */
package com.bonc.mobile.hbmclient.terminal.component;

import org.json.JSONObject;

import android.content.Context;
import android.view.View;

import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;

/**
 * @author liweigao
 * 
 */
public abstract class ViewComponent {
	protected Context context;
	protected TerminalActivityEnum mTerminalActivityEnum;

	public ViewComponent(Context c, TerminalActivityEnum tae) {
		this.context = c;
		this.mTerminalActivityEnum = tae;
	}

	public void add(ViewComponent vc) {
		throw new UnsupportedOperationException();
	}

	public void remove(ViewComponent vc) {
		throw new UnsupportedOperationException();
	}

	public ViewComponent getChild(int i) {
		throw new UnsupportedOperationException();
	}

	public void setView(View view) {
		throw new UnsupportedOperationException();
	}

	public void dispatchView() {
		throw new UnsupportedOperationException();
	}

	public void setData(JSONObject data) {
		// 数据加载处
	}

	public void updateView() {
		throw new UnsupportedOperationException();
	}

	public void setViewListener() {
		throw new UnsupportedOperationException();
	}

	public void iteratorDispatchView() {
		throw new UnsupportedOperationException();
	}

	public void iteratorSetData(JSONObject data) {
		throw new UnsupportedOperationException();
	}

	public void iteratorUpdateView() {
		throw new UnsupportedOperationException();
	}

	public void iteratorSetViewListener() {
		throw new UnsupportedOperationException();
	}

}
