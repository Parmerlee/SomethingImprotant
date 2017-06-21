package com.bonc.mobile.portal.db;

import android.database.sqlite.SQLiteDatabase;

public interface DatabaseCallBack {

	/**
	 * 回调操作.
	 * 
	 * @param database
	 */
	public boolean doCallBack(SQLiteDatabase database);

}
