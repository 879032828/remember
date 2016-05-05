package com.seventh.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBOpenHelper extends SQLiteOpenHelper {

	public MyDBOpenHelper(Context context) {
		super(context, "PersonalFinanceSystem.db", null, 1);
	}

	// 数据库第一次被创建的时候 调用 
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS person (personid INTEGER primary key autoincrement, name varchar(20) ,possward varchar(10) ,login BOOLEAN)");
		db.execSQL("CREATE TABLE IF NOT EXISTS account (accountid INTEGER primary key autoincrement, time varchar(10) ,money float ,type varchar(20) , earnings BOOLEAN ,remark varchar(50)  ,name varchar(20))");
	}
	
	//修改数据库
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}


}
