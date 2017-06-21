
package com.bonc.mobile.common.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sqlcipher.Cursor;
import net.sqlcipher.DatabaseUtils;
import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;

public class DatabaseManager {
    static final String key="db12!@";
    MySQLiteOpenHelper helper;

    public DatabaseManager(Context context) {
        SQLiteDatabase.loadLibs(context);
        helper = new MySQLiteOpenHelper(context);
    }

    public void close() {
        helper.close();
    }

    public Cursor query(String sql, String[] selectionArgs) {
        return helper.getWritableDatabase(key).rawQuery(sql, selectionArgs);
    }

    public List<Map<String, String>> queryForList(String sql, String[] selectionArgs) {
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        Cursor cursor = query(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            String[] colNames = cursor.getColumnNames();
            do {
                Map<String, String> rowData = new HashMap<String, String>();
                int len = colNames.length;
                for (int i = 0; i < len; i++) {
                    rowData.put(colNames[i], cursor.getString(cursor.getColumnIndex(colNames[i])));
                }
                resultList.add(rowData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return resultList;
    }

    public Map<String, String> queryForMap(String sql, String[] selectionArgs) {
        Map<String, String> rowData = new HashMap<String, String>();
        Cursor cursor = query(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            String[] colNames = cursor.getColumnNames();
            int len = colNames.length;
            for (int i = 0; i < len; i++) {
                rowData.put(colNames[i], cursor.getString(cursor.getColumnIndex(colNames[i])));
            }
        }
        cursor.close();
        return rowData;
    }

    public void replace(String table, ContentValues values) {
        helper.getWritableDatabase(key).replace(table, null, values);
    }

    public void replaceList(String table,List<Map<String, String>> list) {
        SQLiteDatabase database=helper.getWritableDatabase(key);
        database.beginTransaction();
        for(Map<String, String> m:list){
            ContentValues cv = new ContentValues();
            for(String key:m.keySet()){
                cv.put(key, m.get(key));
            }
            database.replace(table, null, cv);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public void update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        helper.getWritableDatabase(key).update(table, values, whereClause, whereArgs);
    }

    public long queryNumEntries(String table) {
        return DatabaseUtils.queryNumEntries(helper.getWritableDatabase(key), table);
    }
}
