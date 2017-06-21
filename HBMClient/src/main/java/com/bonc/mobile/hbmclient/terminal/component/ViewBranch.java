/**
 * TODO
 */
package com.bonc.mobile.hbmclient.terminal.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.util.LogoutUtil;

/**
 * @author liweigao
 *
 */
public abstract class ViewBranch extends ViewComponent {

	protected ArrayList<ViewComponent> mViewList = new ArrayList<ViewComponent>();
	protected View mView;
	protected JSONObject mData;
	protected String kpi_statistics = "";

	/**
	 * @param c
	 * @param tae
	 */
	public ViewBranch(Context c, TerminalActivityEnum tae) {
		super(c, tae);
		// TODO Auto-generated constructor stub
	}

	public void setView(View view) {
		this.mView = view;
	}

	public void add(ViewComponent vc) {
		this.mViewList.add(vc);
	}

	public void remove(ViewComponent vc) {
		this.mViewList.remove(vc);
	}

	public ViewComponent getChild(int i) {
		return this.mViewList.get(i);
	}

	@Override
	public void setData(JSONObject data) {
		// TODO Auto-generated method stub
		this.mData = data;
	}

	@Override
	public void updateView() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setViewListener() {
		// TODO Auto-generated method stub
	}

	public void iteratorDispatchView() {
		dispatchView();
		Iterator iterator = mViewList.iterator();
		while (iterator.hasNext()) {
			ViewComponent vc = (ViewComponent) iterator.next();
			vc.iteratorDispatchView();
		}
	}

	@Override
	public void iteratorSetData(JSONObject data) {
		// TODO Auto-generated method stub
		// System.out.println("setData:"+data.toString());
		setData(data);
		Iterator iterator = mViewList.iterator();
		while (iterator.hasNext()) {
			ViewComponent vc = (ViewComponent) iterator.next();
			vc.iteratorSetData(data);
		}
	}

	public void iteratorUpdateView() {
		// System.out.println("update");
		updateView();
		Iterator iterator = mViewList.iterator();
		while (iterator.hasNext()) {
			ViewComponent vc = (ViewComponent) iterator.next();
			vc.iteratorUpdateView();
		}
	}

	@Override
	public void iteratorSetViewListener() {
		// TODO Auto-generated method stub
		setViewListener();
		Iterator iterator = mViewList.iterator();
		while (iterator.hasNext()) {
			ViewComponent vc = (ViewComponent) iterator.next();
			vc.iteratorSetViewListener();
		}
	}

	public List<Map<String, String>> getTermData(String name) {
		JSONArray a = mData.optJSONArray(name);
		if (a == null)
			return new ArrayList<Map<String, String>>();
		List<Map<String, String>> list = JsonUtil.toList(a);
		fixKey(list);
		return list;
	}

	public Map<String, String> getOneTermData(String name) {
		List<Map<String, String>> list = getTermData(name);
		if (list == null || list.size() == 0)
			return null;
		return list.get(0);
	}

	void fixKey(List<Map<String, String>> list) {
		for (Map<String, String> map : list) {
			map.put("typeName", map.get("typename"));
			map.put("modelName", map.get("modelname"));
			map.put("typeValue", map.get("typevalue"));
			map.put("zbName", map.get("zbname"));
			map.put("zbValue", map.get("zbvalue"));
			map.put("hbValue", map.get("hbvalue"));
			map.put("tbValue", map.get("tbvalue"));
			map.put("kpiCode", map.get("kpicode"));
		}
	}
}
