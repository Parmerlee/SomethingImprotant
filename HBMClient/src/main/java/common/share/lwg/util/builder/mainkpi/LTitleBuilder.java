/**
 * LTitleBuilder
 */
package common.share.lwg.util.builder.mainkpi;

import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.adapter.levelkpi.DimensionAdapter;

/**
 * @author liweigao
 * 
 */
public class LTitleBuilder extends LinearLayout implements
		ILinearBuilder<LTitleBuilder> {
	private DimensionAdapter dimenAdapter;

	/**
	 * @param context
	 */
	public LTitleBuilder(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public LTitleBuilder(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.dimenAdapter = new DimensionAdapter(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see common.share.lwg.util.builder.mainkpi.ILinearBuilder#getProduct()
	 */
	@Override
	public LTitleBuilder getProduct() {
		return this;
	}

	private TextView getKpiNameTextView(String s) {
		TextView tv = new TextView(getContext());
		tv.setText(s);
		tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		tv.setGravity(Gravity.CENTER);
		tv.setTextAppearance(getContext(), R.style.mainkpi_title_style);
		tv.setSingleLine(true);
		return tv;
	}

	private TextView getTextView(String s, float width) {
		int pxWidth = this.dimenAdapter.fromDPtoPX(width);

		TextView tv = new TextView(getContext());
		tv.setText(s);
		tv.setLayoutParams(new LayoutParams(pxWidth, LayoutParams.FILL_PARENT));
		tv.setGravity(Gravity.CENTER);
		tv.setTextAppearance(getContext(), R.style.mainkpi_title_style);
		tv.setSingleLine(true);
		return tv;
	}

	private View getDivider() {
		View divider = new View(getContext());
		divider.setBackgroundResource(R.color.c3bdbd);
		LayoutParams lp = new LayoutParams(2, 16);
		lp.gravity = Gravity.CENTER;
		divider.setLayoutParams(lp);
		return divider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * common.share.lwg.util.builder.mainkpi.ILinearBuilder#buildColumnType(
	 * java.lang.Object[])
	 */
	@Override
	public void buildColumnType(Object... obj) {
		Map<String, String> data = (Map<String, String>) obj[0];
		String columnName = data.get("RELATION_KPIVALUE_COLUMN_NAME");
		addView(getDivider());
		addView(getTextView(columnName, DimensionAdapter.KPI_COLUMN_WIDTH_DP));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * common.share.lwg.util.builder.mainkpi.ILinearBuilder#buildColumnType0
	 * (java.lang.Object[])
	 */
	@Override
	public void buildColumnType0(Object... obj) {
		Map<String, String> data = (Map<String, String>) obj[0];
		String columnName = data.get("RELATION_KPIVALUE_COLUMN_NAME");
		addView(getDivider());
		addView(getTextView(columnName, DimensionAdapter.RELATION_WIDTH_DP));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * common.share.lwg.util.builder.mainkpi.ILinearBuilder#buildColumnType1
	 * (java.lang.Object[])
	 */
	@Override
	public void buildColumnType1(Object... obj) {
		Map<String, String> data = (Map<String, String>) obj[0];
		String columnName = data.get("RELATION_KPIVALUE_COLUMN_NAME");
		addView(getDivider());
		addView(getTextView(columnName, DimensionAdapter.TREND_WIDTH_DP));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * common.share.lwg.util.builder.mainkpi.ILinearBuilder#buildColumnType2
	 * (java.lang.Object[])
	 */
	@Override
	public void buildColumnType2(Object... obj) {
		Map<String, String> data = (Map<String, String>) obj[0];
		String columnName = data.get("RELATION_KPIVALUE_COLUMN_NAME");
		addView(getKpiNameTextView(columnName));

	}

	/* (non-Javadoc)
	 * @see common.share.lwg.util.builder.mainkpi.ILinearBuilder#buildAppearance(java.lang.Object[])
	 */
	@Override
	public void buildAppearance(Object... obj) {
		setBackgroundResource(R.mipmap.icon_title_small_blue);
	}

}
