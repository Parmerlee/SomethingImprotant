/**
 * TODO
 */
package com.bonc.mobile.hbmclient.terminal;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.enum_type.TerminalActivityEnum;
import com.bonc.mobile.hbmclient.terminal.component.RootFleeGoodsViewBranch;
import com.bonc.mobile.hbmclient.view.Top10HorizontalScrollView;

/**
 * @author liweigao
 *
 */
public class FleeGoodsMonthActivity extends SimpleTerminalActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flee_goods_month_layout);
		this.mTerminalActivityEnum = TerminalActivityEnum.FG_MONTH_ACTIVITY;
		this.mRootView = new RootFleeGoodsViewBranch(this,
				mTerminalActivityEnum);
		this.mRootView.setView(getWindow().getDecorView());
		this.mRootView.iteratorDispatchView();

		mDateSelect = (Button) this.findViewById(R.id.id_date_select);
		this.initialDateSelectListener(mDateSelect);

		initialBoardScrollView();
		initialTop10ScrollView();

		// 137代表窜货月
		// this.mFirstLoadDataAsynTask = new FirstLoadDataAsynTask(this);
		// this.mFirstLoadDataAsynTask.execute(new String
		// []{TerminalConfiguration.KEY_MENU_CODE_FG_MONTH,mTerminalActivityEnum.getMenuCode(),TerminalConfiguration.KEY_MENU_CODE_FG_MONTH_2});
		initArea();
		loadData();

	}

	@Override
	protected void startArrowAni() {
		// TODO Auto-generated method stub
		ImageView iv_arrow_left = (ImageView) this
				.findViewById(R.id.id_arrow_left);
		Animation ani_alpha_change = AnimationUtils.loadAnimation(this,
				R.anim.alpha_change);
		iv_arrow_left.startAnimation(ani_alpha_change);
		if (this.mBoardScrollView.getContentView().getMeasuredHeight() <= this.mBoardScrollView
				.getScrollY() + this.mBoardScrollView.getHeight()) {
			iv_arrow_down.setVisibility(View.INVISIBLE);
		} else {
			iv_arrow_down.setVisibility(View.VISIBLE);
			iv_arrow_down.startAnimation(ani_alpha_change);
		}
	}

	@Override
	protected void initialTop10ScrollView() {
		// TODO Auto-generated method stub
		this.mTop10HorizontalScrollView = (Top10HorizontalScrollView) this
				.findViewById(R.id.id_ranking_view);
		this.iv_arrow_top10 = (ImageView) this
				.findViewById(R.id.id_arrow_top10);
		this.mTop10HorizontalScrollView
				.setOnBorderListener(new Top10HorizontalScrollView.OnBorderListener() {

					@Override
					public void onLeft() {
						// TODO Auto-generated method stub
						iv_arrow_top10.setVisibility(View.VISIBLE);
					}

					@Override
					public void onNotBorder() {
						// TODO Auto-generated method stub
						iv_arrow_top10.setVisibility(View.VISIBLE);
					}

					@Override
					public void onRight() {
						// TODO Auto-generated method stub
						iv_arrow_top10.setVisibility(View.INVISIBLE);
					}

				});
	}

}
