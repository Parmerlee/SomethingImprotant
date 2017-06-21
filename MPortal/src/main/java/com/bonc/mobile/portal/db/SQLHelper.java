package com.bonc.mobile.portal.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bonc.mobile.common.util.LogUtils;
import com.bonc.mobile.portal.common.ProtalApp;

/**
 * @author tengshibo 数据库查询帮助类. 把查询结果进行了封装. dd 另外就是省去了关闭数据库连接的操作,更安全。
 *         同时也提供了SQLiteDatabase 类 . 请使用后务必关闭连接, 否则可能会造成严重的后果.
 */

public class SQLHelper {

	private SQLiteDatabase database;
	private String path = Constant.LOADER + Constant.DB_PATH;
	private String folder = Constant.LOADER + Constant.DB_PATH_FOLDER;
	private static Lock lock = new ReentrantLock();

	// 默认构造函数.
	public SQLHelper() {

	}

	public boolean deleteTableData(String table) {
		recFile();
		this.database = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READWRITE);
		String sql = "delete from " + table;
		this.database.execSQL(sql);
		this.database.close();
		desFile();
		return true;
	}

	/**
	 * 提供对数据库毁掉操作的方法. 无需关心数据库的关闭打开问题.
	 * 
	 * @param callBack
	 */
	public boolean doDBOperateWithTransation(DatabaseCallBack callBack) {
		boolean flag = false;
		recFile();
		try {
			database = SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READWRITE);
			database.beginTransaction();
			flag = callBack.doCallBack(database);
			if (flag) {
				database.setTransactionSuccessful();
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			if (database != null) {
				database.endTransaction();
				database.close();
			}
		}

		desFile();
		return flag;
	}

	/**
	 * 
	 * @param sql
	 *            拼写好的语句.
	 * @param selectionArgs
	 *            条件参数
	 * @return 将查询结果封装到list里面 示例:List<Map<String,String>> dataList = new
	 *         SQLHelper(this).queryForList(
	 *         "select MODULE_DESC as mdesc from module where id = ?", new
	 *         String[]{"45"}); for(Map map : dataList) { String mdesc =
	 *         map.get("mdesc"); }
	 */
	public List<Map<String, String>> queryForList(final String sql,
			final String[] selectionArgs) {
		this.recFile();
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		try {
			this.database = SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READONLY);
			Cursor cursor = database.rawQuery(sql, selectionArgs);
			if (cursor.moveToFirst()) {
				String[] colNames = cursor.getColumnNames();
				do {
					Map<String, String> rowData = new HashMap<String, String>();
					int len = colNames.length;
					for (int i = 0; i < len; i++) {
						rowData.put(colNames[i], cursor.getString(cursor
								.getColumnIndex(colNames[i])));
					}

					resultList.add(rowData);

				} while (cursor.moveToNext());

			}

			if (cursor != null)
				cursor.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null) {
				database.close();
			}
		}
		this.desFile();

		return resultList;

	}

	/**
	 * 
	 * @param sql
	 *            sql语句
	 * @param selectionArgs
	 *            参数
	 * @return Map对象 封装单行记录 如果查询结果有多行 则返回第一行 示例: Map map = new
	 *         SQLHelper(this).queryForMap
	 *         ("select MODULE_DESC as mdesc from module where id = ?", new
	 *         String[]{"45"}); String mdesc = map.get("mdesc");
	 */
	public Map<String, String> queryForMap(String sql, String[] selectionArgs) {
		Map<String, String> rowData = null;
		this.recFile();
		try {
			this.database = SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READONLY);
			// LogUtil.info("queryForMap===", sql);
			Cursor cursor = database.rawQuery(sql, selectionArgs);
			if (cursor.moveToFirst()) {
				String[] colNames = cursor.getColumnNames();
				rowData = new HashMap<String, String>();
				int len = colNames.length;
				for (int i = 0; i < len; i++) {
					String colName = colNames[i];
					rowData.put(colName,
							cursor.getString(cursor.getColumnIndex(colName)));
				}
			}

			if (cursor != null)
				cursor.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null) {
				database.close();
			}
		}
		this.desFile();

		return rowData;

	}

	/**
	 * 将查询结果封装成一个bean对象.有两点要求 1. 查询结果的列名称在不区分大小写的情况下必须和属性名称保持一致. 2. java
	 * bean里面属属性的set方法 必须是标准的 如: setMethod();
	 * 
	 * @param sql
	 *            sql语句
	 * @param selectionArgs
	 *            参数
	 * @param c
	 *            要转化的对象的class对象
	 * @return 返回转换后的对象.
	 */
	@SuppressWarnings("rawtypes")
	public Object queryForObject(String sql, String[] selectionArgs, Class c) {
		this.recFile();
		this.database = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		// LogUtil.info("queryForObject===", sql);
		Object object = null;
		Cursor cursor = database.rawQuery(sql, selectionArgs);

		if (cursor.moveToFirst()) {
			String[] colNames = cursor.getColumnNames();

			try {

				object = c.newInstance();
				Method[] methods = c.getDeclaredMethods();

				for (Method method : methods) {
					if (method.getName().startsWith("set")) {
						for (String key : colNames) {
							if (("set" + key)
									.equalsIgnoreCase(method.getName())) {

								Class type = (method.getParameterTypes())[0];
								String value = cursor.getString(cursor
										.getColumnIndex(key));

								if (type == int.class || type == Integer.class) {
									method.invoke(object,
											Integer.valueOf(value));
									break;
								}

								if (type == float.class || type == Float.class) {
									method.invoke(object, Float.valueOf(value));
									break;
								}

								if (type == Double.class
										|| type == double.class) {
									method.invoke(object, Double.valueOf(value));
									break;
								}

								method.invoke(object, value);

							}
						}
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (cursor != null)
			cursor.close();
		if (database != null)
			database.close();
		this.desFile();
		return object;
	}

	/**
	 * 插入数据
	 * 
	 * @param table
	 * @param nullColumnHack
	 * @param values
	 * @return
	 */
	public long insert(String table, ContentValues values) {
		this.recFile();

		long result = 0;
		try {
			this.database = SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READWRITE);
			database.beginTransaction();
			result = database.insert(table, null, values);
			database.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null) {
				database.endTransaction();
				database.close();
			}
		}
		this.desFile();

		return result;
	}

	/**
	 * 批量插入
	 * 
	 * @param table
	 * @param valueList
	 * @return
	 */
	public void batchInsert(String table, List<ContentValues> valueList) {

		if (valueList == null || valueList.size() == 0) {
			return;
		}
		this.recFile();

		int len = valueList.size();
		try {
			this.database = SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READWRITE);
			database.beginTransaction();

			for (int i = 0; i < len; i++) {
				database.insert(table, null, valueList.get(i));
			}
			database.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null) {
				database.endTransaction();
				database.close();
			}
		}

		this.desFile();

	}

	/**
	 * 删除数据库文件.
	 * 
	 * @return
	 */
	public boolean deleteDbIfExists() {
		String path = Constant.LOADER + Constant.DB_PATH;
		if (!Constant.LOADER.endsWith(File.separator)
				&& !Constant.DB_PATH.startsWith(File.separator)) {
			path = Constant.LOADER + File.separator + Constant.DB_PATH;
		}

		File mFile = new File(path);
		if (mFile.exists()) {
			mFile.delete();
		}

		return true;
	}

	public void createEmptyDbFile() {
		String path = Constant.LOADER + Constant.DB_PATH;
		if (!Constant.LOADER.endsWith(File.separator)
				&& !Constant.DB_PATH.startsWith(File.separator)) {
			path = Constant.LOADER + File.separator + Constant.DB_PATH;
		}

		File mFile = new File(path);
		if (mFile.exists()) {
			mFile.delete();
		}
		mFile.mkdirs();
	}

	// 执行更新.
	public void update(String sql, Object[] bindArgs) {

		this.checkDBFile();
		this.recFile();

		try {
			this.database = SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READWRITE);
			database.beginTransaction();
			database.execSQL(sql, bindArgs);
			database.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null) {
				database.endTransaction();
				database.close();
			}
		}

		this.desFile();
	}

	// 执行sql(Execute a single SQL statement that is NOT a SELECT or any other
	// SQL statement that returns data. )
	public void execute(String sql) {

		this.checkDBFile();
		this.recFile();

		try {
			this.database = SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READWRITE);
			database.beginTransaction();
			database.execSQL(sql);
			database.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null) {
				database.endTransaction();
				database.close();
			}
		}

		this.desFile();
	}

	public synchronized void desFile() {
		// 加锁
		checkDBFile();
		if (Constant.ENCRYPT_DATABASE) {
			try {
				RandomAccessFile rAFile = new RandomAccessFile(path, "rw");
				rAFile.seek(19);
				rAFile.writeByte(5);
				rAFile.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 数据库操作解锁.
		lock.unlock();
	}

	public synchronized void recFile() {
		// 数据库操作解锁.
		lock.lock();
		checkDBFile();
		if (Constant.ENCRYPT_DATABASE) {
			try {
				RandomAccessFile rAFile = new RandomAccessFile(path, "rw");
				rAFile.seek(19);
				rAFile.writeByte(1);
				rAFile.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isDBFileExists() {
		LogUtils.debug(getClass(), path);
		return new File(path).exists();
	}

	/**
	 * 检查数据库文件.
	 */
	private void checkDBFile() {
		File mFile = new File(path);
		if (!mFile.exists()) {
			Context c = ProtalApp.getInstance().getApplicationContext();
			SharedPreferences sp = c.getSharedPreferences("permissiontime",
					Context.MODE_WORLD_WRITEABLE);
			Editor editor = sp.edit();
			editor.putString("permissiontime", "0");
			editor.commit();
			try {
				InputStream input = ProtalApp.getInstance().getResources()
						.getAssets().open("data/ErrorLogDB.db");
				FileUtils.copyFile(input, mFile);
				input.close();
			} catch (Exception e) {
				new Throwable("copy db file failed ");
			}
		}
	}

	public boolean useEmptyDB() {
		try {
			File mFile = new File(path);
			if (mFile.exists()) {
				mFile.delete();
				InputStream input = ProtalApp.getInstance().getResources()
						.getAssets().open("data/ErrorLogDB.db");
				FileUtils.copyFile(input, mFile);
				input.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public void useNewDbFile(File newFile) {
		File mFile = new File(path);
		if (mFile.exists()) {
			mFile.delete();
		}
		if (!mFile.exists()) {
			try {
				InputStream input = new FileInputStream(newFile);
				FileUtils.copyFile(input, mFile);
				input.close();
				newFile.delete();
			} catch (Exception e) {
				new Throwable("copy db file failed ");
			}
		}
	}

	public String getDbFileFolder() {
		return this.folder;
	}
}