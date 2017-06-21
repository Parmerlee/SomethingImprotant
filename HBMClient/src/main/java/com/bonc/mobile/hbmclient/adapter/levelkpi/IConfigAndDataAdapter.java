/**
 * IConfigAndDataAdapter
 */
package com.bonc.mobile.hbmclient.adapter.levelkpi;

import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.bonc.mobile.hbmclient.visitor.IConfig;

/**
 * @author liweigao
 *
 */
public interface IConfigAndDataAdapter extends IConfig {
	void setConfig(IConfig config);

	void setBackData(String result) throws JSONException;

	void handle() throws JSONException;

	List<Map<String, String>> getKpiData();

	Map<String, List<Double>> getTrendMap();

	List<String> getGroupList();

	List<List<Map<String, String>>> getKpiList();

	String getShowTime();
}
