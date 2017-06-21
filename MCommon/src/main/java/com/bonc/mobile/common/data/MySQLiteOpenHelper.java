package com.bonc.mobile.common.data;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.content.Context;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public MySQLiteOpenHelper(Context context) {
        super(context, "fdata.db", null, 1);
        context.getDatabasePath("edata.db").delete();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE IF NOT EXISTS announce_status(flow_id varchar(20) unique,status varchar(20))");
        db.execSQL("CREATE TABLE IF NOT EXISTS mp_announce(OPER_ID varchar(20),MSG varchar(200),FLOW_ID varchar(20) unique,NOTICE_PUBLISH_DATE varchar(20),STATUS varchar(20),OPER_NAME varchar(20),TYPE varchar(20),NOTICE_EXPIRE_DATE varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
