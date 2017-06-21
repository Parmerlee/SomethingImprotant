package com.bonc.mobile.hbmclient.data;

public class ColumnChart {
	private String column_curmon_value;

	private String column_area_desc;

	public String getColumn_curmon_value() {
		return column_curmon_value;
	}

	public void setColumn_curmon_value(String column_curmon_value) {
		this.column_curmon_value = column_curmon_value;
	}

	public String getColumn_area_desc() {
		return column_area_desc;
	}

	public void setColumn_area_desc(String column_area_desc) {
		this.column_area_desc = column_area_desc;
	}

	@Override
	public String toString() {
		return "ColumnChart [column_curmon_value=" + column_curmon_value
				+ ", column_area_desc=" + column_area_desc + "]";
	}

}
