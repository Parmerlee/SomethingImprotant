/**
 * TODO
 */
package com.bonc.mobile.hbmclient.enum_type;

import java.util.Calendar;
import java.util.Date;

import android.view.View;

import com.bonc.mobile.hbmclient.activity.SimpleTerminalActivity;
import com.bonc.mobile.hbmclient.terminal.FleeGoodsMonthActivity;
import com.bonc.mobile.hbmclient.terminal.PurchaseSellStockDayActivity;
import com.bonc.mobile.hbmclient.terminal.PurchaseSellStockMonthActivity;
import com.bonc.mobile.hbmclient.terminal.TerminalConfiguration;
import com.bonc.mobile.hbmclient.terminal.UnpackDayActivity;
import com.bonc.mobile.hbmclient.terminal.UnpackMonthActivity;
import com.bonc.mobile.hbmclient.terminal.UnsaleDayActivity;
import com.bonc.mobile.hbmclient.terminal.UnsaleMonthActivity;
import com.bonc.mobile.hbmclient.util.DateUtil;

/**
 * @author liweigao
 * 
 */
public enum TerminalActivityEnum {
	PSS_DAY_ACTIVITY(1, PurchaseSellStockDayActivity.class, DateRangeEnum.DAY,
			TerminalConfiguration.KEY_MENU_CODE_TOP20_PSS_DAY), PSS_MONTH_ACTIVITY(
			1, PurchaseSellStockMonthActivity.class, DateRangeEnum.MONTH,
			TerminalConfiguration.KEY_MENU_CODE_TOP20_PSS_MONTH), UNSALE_DAY_ACTIVITY(
			2, UnsaleDayActivity.class, DateRangeEnum.DAY,
			TerminalConfiguration.KEY_MENU_CODE_TOP20_UNSALE_DAY), UNSALE_MONTH_ACTIVITY(
			2, UnsaleMonthActivity.class, DateRangeEnum.MONTH,
			TerminalConfiguration.KEY_MENU_CODE_TOP20_UNSALE_MONTH), UNPACK_DAY_ACTIVITY(
			3, UnpackDayActivity.class, DateRangeEnum.DAY,
			TerminalConfiguration.KEY_MENU_CODE_TOP20_UNPACK_DAY), UNPACK_MONTH_ACTIVITY(
			3, UnpackMonthActivity.class, DateRangeEnum.MONTH,
			TerminalConfiguration.KEY_MENU_CODE_TOP20_UNPACK_MONTH), FG_MONTH_ACTIVITY(
			4, FleeGoodsMonthActivity.class, DateRangeEnum.MONTH,
			TerminalConfiguration.KEY_MENU_CODE_TOP20_FG_MONTH),

	MARKET_DAY_ACTIVITY(0, SimpleTerminalActivity.class, DateRangeEnum.DAY,
			TerminalConfiguration.KEY_MENU_CODE_TA_DAY), MARKET_MONTH_ACTIVITY(
			0, SimpleTerminalActivity.class, DateRangeEnum.MONTH,
			TerminalConfiguration.KEY_MENU_CODE_TA_DAY);

	private View mView;
	private final Class<?> mClass;
	private final int position;
	private final DateRangeEnum dateRange;
	private final String menu_code;
	String areaCode;

	private TerminalActivityEnum(int position, Class<?> mClass,
			DateRangeEnum dateRange, String menu_code) {
		this.position = position;
		this.mClass = mClass;
		this.dateRange = dateRange;
		this.menu_code = menu_code;
	}

	public String getMenuCode() {
		return this.menu_code;
	}

	public DateRangeEnum getDateRange() {
		return this.dateRange;
	}

	public void setView(View view) {
		this.mView = view;
	}

	public View getView() {
		return this.mView;
	}

	public String getOPtime() {
		Date date = ((Calendar) this.mView.getTag()).getTime();
		return DateUtil.formatter(date, this.dateRange.getDateServerPattern());
	}

	public Calendar getCalendar() {
		return (Calendar) this.mView.getTag();
	}

	public Class<?> getIntentClass() {
		return this.mClass;
	}

	public int getPosition() {
		return this.position;
	}

	public void setMaxDate(String max) {
		this.dateRange.setMaxDate(max);
	}

	public void setMinDate(String min) {
		this.dateRange.setMinDate(min);
	}

	public String getMaxDate() {
		return this.dateRange.getMaxDate();
	}

	public String getMinDate() {
		return this.dateRange.getMinDate();
	}

	public boolean isDateValid(Calendar c) {
		// return this.dateRange.isDateValid(c);
		return true;
	}

	public static void clearAllView() {
		for (TerminalActivityEnum tae : TerminalActivityEnum.values()) {
			tae.setView(null);
		}
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public void setDate(String optime) {
		switch (getDateRange()) {
		case DAY:
			Calendar c1 = DateUtil.getCalendar(optime,
					DateRangeEnum.DAY.getDateServerPattern());
			getView().setTag(c1);
			break;
		case MONTH:
			Calendar c2 = DateUtil.getCalendar(optime,
					DateRangeEnum.MONTH.getDateServerPattern());
			getView().setTag(c2);
			break;
		}
		setMaxDate(optime);
	}

}
