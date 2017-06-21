package com.bonc.mobile.hbmclient.terminal.view;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.terminal.TerminalHomePageDataLoad;

/**
 * 
 * @author ZZZ
 *
 */
public class Top10View extends LinearLayout implements
		TerminalHomeRefreshInterface {
	private RelativeLayout relativeLayout;
	public LinearLayout ll_view, linearLayout, ll_rankingChanges;
	List<Map<String, String>> ranklist;
	public View view = null;
	public int width;
	private String changes;
	private Handler handler = new Handler();

	public Top10View(Context context, AttributeSet attrs) {
		super(context, attrs);
		TerminalRefreshListener.register(this);
		initView();
	}

	public Top10View(Context context, int width) {
		super(context);
		this.width = width;
		initView();
		TerminalRefreshListener.register(this);
	}

	public void initView() {
		ranklist = TerminalHomePageDataLoad.getRanking();
		if (ranklist == null || ranklist.size() == 0
				|| (ranklist.size() > 0 && ranklist.size() < 10)) {
			this.buildFakeData(ranklist);
		}
		LayoutInflater inflater = LayoutInflater.from(getContext());
		ll_view = (LinearLayout) inflater.inflate(R.layout.top_rank, this);
		for (int i = 0; i < 10; i++) {
			View view0 = inflater.inflate(R.layout.top_rank_item, null);
			// 最外层布局
			linearLayout = (LinearLayout) view0
					.findViewById(R.id.ll_top_rank_item);
			// 第二层
			relativeLayout = (RelativeLayout) view0
					.findViewById(R.id.rl_top_rank_item);
			int x = (width - 20) / 3;
			LayoutParams params_rl = new LayoutParams(
					x, RelativeLayout.LayoutParams.WRAP_CONTENT);
			LayoutParams params_ll = new LayoutParams(
					x + 10,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			if (i == 9)
				params_ll = new LayoutParams(x,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
			linearLayout.setLayoutParams(params_ll);
			relativeLayout.setLayoutParams(params_rl);
			TextView tv_num = (TextView) view0.findViewById(R.id.tv_num);
			tv_num.setText(i + 1 + "");//
			TextView tv_top10 = (TextView) view0.findViewById(R.id.tv_top10);
			if (i == 0)
				tv_top10.setVisibility(View.VISIBLE);
			MyTextView rank_name = (MyTextView) view0
					.findViewById(R.id.rank_name);
			String rankName = ranklist.get(i).get("model");
			rank_name.setTextSize((float) 11);
			rank_name.setText(rankName);
			TextView rankingChanges = (TextView) view0
					.findViewById(R.id.rankingChanges);
			changes = ranklist.get(i).get("rankingChanges");
			ll_rankingChanges = (LinearLayout) view0
					.findViewById(R.id.ll_rankingChanges);
			// 判断是排名 上升 下降 新进 持平
			if (changes == null || "".equals(changes) || "*".equals(changes)) {
				ll_rankingChanges.setBackgroundResource(R.mipmap.ranknew);
				rankingChanges.setText("");
			} else {
				try {
					int rcInt = Integer.valueOf(changes.trim());
					String schangvalue = null;
					int tmp = 0;
					if (rcInt < 0) {
						schangvalue = changes.substring(1, changes.length());
						tmp = Integer.valueOf(schangvalue);
					}
					if (rcInt == 0) {
						ll_rankingChanges.setVisibility(View.INVISIBLE);
					} else if (rcInt > 0) {
						rankingChanges.setText("-" + rcInt);
						ll_rankingChanges
								.setBackgroundResource(R.mipmap.rankdown);
					} else if (rcInt < 0) {
						rankingChanges.setText("+" + tmp);
						ll_rankingChanges
								.setBackgroundResource(R.mipmap.rankup);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			TextView ljz = (TextView) view0.findViewById(R.id.top10_ljz);
			String ljzStr;
			DecimalFormat digits = new DecimalFormat("0.0");
			ljzStr = ranklist.get(i).get("ljz");
			ljz.setText(ljzStr);
			// if(i!=9&&ljzStr.length()>6){
			if (ljzStr.length() > 6) {
				if (ljzStr.length() == 6)
					ljzStr = digits.format(Float.parseFloat(ljzStr) / 10000.00)
							+ "万";
				if (ljzStr.length() >= 7) {
					ljzStr = digits.format(Float.parseFloat(ljzStr) / 10000)
							+ "万";
				}
				ljz.setText(ljzStr);
			}
			// if(i==9&&ljzStr.length()==4)
			// tv_num.setTextSize(23);
			// double tmp = NumberUtil.changeToDouble(ljzStr);
			// if(i==9&&tmp>10000){
			// tv_num.setTextSize(23);
			// if(ljzStr.length()==5)
			// ljzStr =digits.format(Float.parseFloat(ljzStr)/10000.00)+"万";
			// if(ljzStr.length()>=6)
			// //ljzStr=(Integer.parseInt(ljzStr)/10000)+"万";
			// ljzStr=(digits.format(Float.parseFloat(ljzStr)/10000))+"万";
			// ljz.setText(ljzStr);
			// }
			TextView ljz_zb = (TextView) view0.findViewById(R.id.top10_ljz_zb);
			ljz_zb.setText(ranklist.get(i).get("ljzZb"));
			ll_view.addView(view0);
		}
	}

	public View getView() {
		return ll_view;
	}

	@Override
	public void refresh() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Top10View.this.removeAllViews();
				initView();
				invalidate();
			}
		});
	}

	private void buildFakeData(List<Map<String, String>> list) {

		for (int i = list.size(); i < 10; i++) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("model", "---");
			m.put("changes", "");
			m.put("ljz", "");
			m.put("ljzZb", "");
			list.add(m);
		}

	}
}
