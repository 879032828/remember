package com.seventh.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBOpenHelper extends SQLiteOpenHelper {

	public MyDBOpenHelper(Context context) {
		super(context, "remember.db", null, 1);
	}

	// ���ݿ��һ�α�������ʱ�� ���� 
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS person (personid INTEGER primary key autoincrement, name varchar(20) ,possward varchar(10) ,login BOOLEAN)");
		db.execSQL("CREATE TABLE IF NOT EXISTS account (accountid INTEGER primary key autoincrement, time varchar(10) ,money float ,type varchar(20) , earnings INTEGER ,remark varchar(50),name varchar(20))");
		db.execSQL("CREATE TABLE IF NOT EXISTS type (typeid INTEGER primary key autoincrement, type INTEGER, typename varchar(20), name varchar(20))");
		db.execSQL("CREATE TABLE IF NOT EXISTS budget (budgetid INTEGER primary key autoincrement, budget float, time varchar(10), name varchar(20))");
	}
	
	//�޸����ݿ�
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}


}
