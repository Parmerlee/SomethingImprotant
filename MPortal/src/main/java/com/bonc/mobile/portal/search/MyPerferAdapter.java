package com.bonc.mobile.portal.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;


import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.bonc.anhuimobile.ac.R;
import com.bonc.mobile.portal.search.SearchAdapter.ViewHolder;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyPerferAdapter extends BaseAdapter {

	Context context;
	List<String> list;

	public MyPerferAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.perfer_item, null);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title = (TextView) convertView
				.findViewById(R.id.perfer_item_title);
		holder.content = (TextView) convertView
				.findViewById(R.id.perfer_item_content);
		// holder.web = (WebView)
		// convertView.findViewById(R.id.perfer_item_web);
		// holder.web.loadUrl("file:///android_asset/jianjie.html");

		// holder.web.getSettings().setCacheMode(
		// WebSettings.LOAD_CACHE_ELSE_NETWORK);

		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		// holder.web.setWebViewClient(new WebViewClient() {
		// @Override
		// public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// // TODO Auto-generated method stub
		// // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
		// // view.loadUrl(url);
		// return true;
		// }
		// });
		holder.title
				.setText(Html.fromHtml(list.get(position).substring(0, 10)));
		holder.content.setText(Html.fromHtml(list.get(position).substring(10)));
		return convertView;
	}

	class ViewHolder {
		// WebView web;
		TextView title, content;
	}

	// private String getStringFromUrl(String murl) {
	// StringBuffer resultData = new StringBuffer();
	// HttpGet httpGet = new HttpGet(murl);
	// HttpClient httpclient = new DefaultHttpClient();
	// // Execute HTTP Get Request
	// HttpResponse response = null;
	// try {
	// response = httpclient.execute(httpGet);
	// } catch (ClientProtocolException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// try {
	// InputStream is = response.getEntity().getContent();
	// //使用缓冲一行行的读入，加速InputStreamReader的速度
	// BufferedReader buffer = new BufferedReader(is);
	// String inputLine = null;
	//
	// while((inputLine = buffer.readLine()) != null){
	// resultData.append(inputLine);
	// resultData.append("\n");
	// }
	// buffer.close();
	// is.close();
	// // urlConn.disconnect();
	// } catch (IllegalStateException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
}
