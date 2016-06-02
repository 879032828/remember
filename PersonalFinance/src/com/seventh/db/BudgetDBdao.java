package com.seventh.db;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import com.seventh.db.Type;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BudgetDBdao {

	private Context context;
	MyDBOpenHelper dbOpenHelper;

	public BudgetDBdao(Context context) {
		this.context = context;
		dbOpenHelper = new MyDBOpenHelper(context);
	}

	public Budget findBudget(String name) {
		Budget budget = new Budget();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from budget where name = ?", new String[] { name });
			while (cursor.moveToNext()) {
				budget.setBudgetid(cursor.getInt(cursor.getColumnIndex("budgetid")));
				budget.setBudget(cursor.getFloat(cursor.getColumnIndex("budget")));
				budget.setName(cursor.getString(cursor.getColumnIndex("name")));
				budget.setTime(cursor.getString(cursor.getColumnIndex("time")));
			}
		}
		return budget;
	}

	/**
	 * @param time
	 * @param name
	 * @return 根据时间和名称寻找是否存在预算余额
	 */
	public Boolean isExistBudget(String name) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from budget where name = ?", new String[] { name });
			if (cursor.getCount() != 0) {
				db.close();
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * @param type
	 * @param typename
	 * @param name
	 *            添加一条记录
	 */
	public void addBudget(float budget, String name) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("insert into budget(budget,name) values (?,?)", new Object[] { budget, name });
			db.close();
		}
	}

}
