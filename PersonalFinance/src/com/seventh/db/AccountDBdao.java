package com.seventh.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class AccountDBdao {
	private Context context;
	MyDBOpenHelper dbOpenHelper;

	public AccountDBdao(Context context) {
		this.context = context;
		dbOpenHelper = new MyDBOpenHelper(context);
	}

	/**
	 * 添加一条记录
	 */
	public void add(String time, float money, String type, boolean earnings,
			String remark, String name) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			// db.execSQL("insert into account (time,money,type,earnings,remark,name) values (?,?,?,?,?,?)",new
			// Object[]{time,money,type,earnings,remark,name});
			ContentValues values = new ContentValues();
			values.put("time", time);// 交易时间
			values.put("money", money);// 金钱
			values.put("type", type);// 类型
			values.put("earnings", earnings);// 是否收益
			values.put("remark", remark);// 备注
			values.put("name", name);// 用户名
			db.insert("account", null, values); // 组拼sql语句完成的添加的操作
			db.close();
		}

	}

	/**
	 * 删除一条记录
	 */
	public void delete(String accountid) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete("account", "accountid=?", new String[] { accountid });
			db.close();
		}
	}

	/**
	 * 数据库的更改操作
	 */
	public void update(String accountid, String time, float money, String type,
			boolean earnings, String remark) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put("time", time);// 交易时间
			values.put("money", money);// 金钱
			values.put("type", type);// 类型
			values.put("earnings", earnings);// 是否收益
			values.put("remark", remark);// 备注
			db.update("account", values, "accountid=?",
					new String[] { accountid });
			db.close();
		}
	}

	/**
	 * 数据库的查询操作 判断有无该数据
	 */
	public boolean find(String name) {
		boolean result = false;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			// select * from person
			Cursor cursor = db.query("account", null, "name=?",
					new String[] { name }, null, null, null);
			if (cursor.moveToFirst()) {
				result = true;
			}
			cursor.close();
			db.close();
		}
		return result;

	}

	/**
	 * 查询所有信息
	 */
	public List<Account> findAll() {
		List<Account> accounts = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.query("account", null, null, null, null, null,
					null);
			accounts = new ArrayList<Account>();
			while (cursor.moveToNext()) {
				Account account = new Account();
				int id = cursor.getInt(cursor.getColumnIndex("accountid"));
				account.setId(id);
				String name = cursor.getString(cursor.getColumnIndex("name"));
				account.setName(name);
				float money = Float.parseFloat(cursor.getString(cursor
						.getColumnIndex("money")));
				account.setMoney(money);
				String time = cursor.getString(cursor.getColumnIndex("time"));
				account.setTime(time);
				String type = cursor.getString(cursor.getColumnIndex("type"));
				account.setType(type);
				long earnings = cursor.getLong(cursor
						.getColumnIndex("earnings"));
				if (earnings == 0) {
					account.setEarnings(false);
				} else {
					account.setEarnings(true);
				}
				String remark = cursor.getString(cursor
						.getColumnIndex("remark"));
				account.setRemark(remark);
				accounts.add(account);
			}
			cursor.close();
			db.close();
		}
		return accounts;
	}

	/**
	 * 根据用户名查询所有信息
	 */
	public List<Account> findAllByName(String userName) {
		List<Account> accounts = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from account where name=?",
					new String[] { userName });
			accounts = new ArrayList<Account>();
			while (cursor.moveToNext()) {
				Account account = new Account();
				int id = cursor.getInt(cursor.getColumnIndex("accountid"));
				account.setId(id);
				String name = cursor.getString(cursor.getColumnIndex("name"));
				account.setName(name);
				float money = Float.parseFloat(cursor.getString(cursor
						.getColumnIndex("money")));
				account.setMoney(money);
				String time = cursor.getString(cursor.getColumnIndex("time"));
				account.setTime(time);
				String type = cursor.getString(cursor.getColumnIndex("type"));
				account.setType(type);
				long earnings = cursor.getLong(cursor
						.getColumnIndex("earnings"));
				if (earnings == 0) {
					account.setEarnings(false);
				} else {
					account.setEarnings(true);
				}
				String remark = cursor.getString(cursor
						.getColumnIndex("remark"));
				account.setRemark(remark);
				accounts.add(account);
			}
			cursor.close();
			db.close();
		}
		return accounts;
	}

	/**
	 * 根据用户名查询所有收入信息
	 */
	public List<Account> findTotalIntoByName(String userName) {
		List<Account> accounts = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select * from account where earnings=1 and name=?",
					new String[] { userName });
			accounts = new ArrayList<Account>();
			while (cursor.moveToNext()) {
				Account account = new Account();
				int id = cursor.getInt(cursor.getColumnIndex("accountid"));
				account.setId(id);
				String name = cursor.getString(cursor.getColumnIndex("name"));
				account.setName(name);
				float money = Float.parseFloat(cursor.getString(cursor
						.getColumnIndex("money")));
				account.setMoney(money);
				String time = cursor.getString(cursor.getColumnIndex("time"));
				account.setTime(time);
				String type = cursor.getString(cursor.getColumnIndex("type"));
				account.setType(type);
				long earnings = cursor.getLong(cursor
						.getColumnIndex("earnings"));
				if (earnings == 0) {
					account.setEarnings(false);
				} else {
					account.setEarnings(true);
				}
				String remark = cursor.getString(cursor
						.getColumnIndex("remark"));
				account.setRemark(remark);
				accounts.add(account);
			}
			cursor.close();
			db.close();
		}
		return accounts;
	}

	/**
	 * 根据用户名查询所有支出信息
	 */
	public List<Account> findTotalOutByName(String userName) {
		List<Account> accounts = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select * from account where earnings=0 and name=?",
					new String[] { userName });
			accounts = new ArrayList<Account>();
			while (cursor.moveToNext()) {
				Account account = new Account();
				int id = cursor.getInt(cursor.getColumnIndex("accountid"));
				account.setId(id);
				String name = cursor.getString(cursor.getColumnIndex("name"));
				account.setName(name);
				float money = Float.parseFloat(cursor.getString(cursor
						.getColumnIndex("money")));
				account.setMoney(money);
				String time = cursor.getString(cursor.getColumnIndex("time"));
				account.setTime(time);
				String type = cursor.getString(cursor.getColumnIndex("type"));
				account.setType(type);
				long earnings = cursor.getLong(cursor
						.getColumnIndex("earnings"));
				if (earnings == 0) {
					account.setEarnings(false);
				} else {
					account.setEarnings(true);
				}
				String remark = cursor.getString(cursor
						.getColumnIndex("remark"));
				account.setRemark(remark);
				accounts.add(account);
			}
			cursor.close();
			db.close();
		}
		return accounts;
	}

	/**
	 * 根据用户名查询没时间段的账单信息
	 */
	public List<Account> findSomeTimeByName(String userName, String sometime) {
		List<Account> accounts = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select * from account where name=? and time like ?",
					new String[] { userName, sometime });
			accounts = new ArrayList<Account>();
			while (cursor.moveToNext()) {
				Account account = new Account();
				int id = cursor.getInt(cursor.getColumnIndex("accountid"));
				account.setId(id);
				String name = cursor.getString(cursor.getColumnIndex("name"));
				account.setName(name);
				float money = Float.parseFloat(cursor.getString(cursor
						.getColumnIndex("money")));
				account.setMoney(money);
				String time = cursor.getString(cursor.getColumnIndex("time"));
				account.setTime(time);
				String type = cursor.getString(cursor.getColumnIndex("type"));
				account.setType(type);
				long earnings = cursor.getLong(cursor
						.getColumnIndex("earnings"));
				if (earnings == 0) {
					account.setEarnings(false);
				} else {
					account.setEarnings(true);
				}
				String remark = cursor.getString(cursor
						.getColumnIndex("remark"));
				account.setRemark(remark);
				accounts.add(account);
			}
			cursor.close();
			db.close();
		}
		return accounts;
	}

	/**
	 * 查询所有信息
	 */
	public Cursor findAllbyCursor() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			/*
			 * Cursor cursor = db.query("person", null, null, null, null, null,
			 * null);
			 */
			Cursor cursor = db
					.rawQuery(
							"select accountid  as _id ,name ,time ,money ,type ,earnings ,remark  from account",
							null);

			return cursor;
			// 注意了 一定不要把数据库 关闭了
		}
		return null;
	}

	/**
	 * 查询所有总收入
	 */
	public float fillTotalInto(String name) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db
					.rawQuery(
							"select sum(money) as sumvalue from account where earnings=1 and name=?",
							new String[] { name });
			while (cursor.moveToNext()) {
				return cursor.getFloat(cursor.getColumnIndex("sumvalue"));
			}
			cursor.close();
			db.close();
		}
		return 0;
	}

	/**
	 * 查询所有总支出
	 */
	public float fillTotalOut(String name) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db
					.rawQuery(
							"select sum(money) as sumvalue from account where earnings=0 and name=?",
							new String[] { name });
			while (cursor.moveToNext()) {
				return cursor.getFloat(cursor.getColumnIndex("sumvalue"));
			}
			cursor.close();
			db.close();
		}
		return 0;
	}

	/**
	 * 查询今天所有支出
	 */
	public float fillTodayOut(String name, String time) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db
					.rawQuery(
							"select sum(money) as sumvalue from account where earnings=0 and name=? and time=?",
							new String[] { name, time });
			while (cursor.moveToNext()) {
				return cursor.getFloat(cursor.getColumnIndex("sumvalue"));
			}
			cursor.close();
			db.close();
		}
		return 0;
	}

	/**
	 * 查询今天所有收入
	 */
	public float fillTodayInto(String name, String time) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db
					.rawQuery(
							"select sum(money) as sumvalue from account where earnings=1 and name=? and time=?",
							new String[] { name, time });
			while (cursor.moveToNext()) {
				return cursor.getFloat(cursor.getColumnIndex("sumvalue"));
			}
			cursor.close();
			db.close();
		}
		return 0;
	}

	/**
	 * 查询本月所有支出
	 */
	public float fillMonthOut(String name, String time) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db
					.rawQuery(
							"select sum(money) as sumvalue from account where earnings=0 and name=? and time like ?",
							new String[] { name, time });
			while (cursor.moveToNext()) {
				return cursor.getFloat(cursor.getColumnIndex("sumvalue"));
			}
			cursor.close();
			db.close();
		}
		return 0;
	}

	/**
	 * 查询本月所有收入
	 */
	public float fillMonthInto(String name, String time) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db
					.rawQuery(
							"select sum(money) as sumvalue from account where earnings=1 and name=? and time like ?",
							new String[] { name, time });
			while (cursor.moveToNext()) {
				return cursor.getFloat(cursor.getColumnIndex("sumvalue"));
			}
			cursor.close();
			db.close();
		}
		return 0;
	}

	/**
	 * 查询本年所有支出
	 */
	public float fillYearOut(String name, String time) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db
					.rawQuery(
							"select sum(money) as sumvalue from account where earnings=0 and name=? and time like ?",
							new String[] { name, time });
			while (cursor.moveToNext()) {
				return cursor.getFloat(cursor.getColumnIndex("sumvalue"));
			}
			cursor.close();
			db.close();
		}
		return 0;
	}

	/**
	 * 查询本年所有收入
	 */
	public float fillYearInto(String name, String time) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db
					.rawQuery(
							"select sum(money) as sumvalue from account where earnings=1 and name=? and time like ?",
							new String[] { name, time });
			while (cursor.moveToNext()) {
				return cursor.getFloat(cursor.getColumnIndex("sumvalue"));
			}
			cursor.close();
			db.close();
		}
		return 0;
	}
	
	/**
	 * 根据id查询记录信息
	 */
	public Account findInfoById(String accountid) {
		Account account = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from account where accountid=?",
					new String[] { accountid });
			while (cursor.moveToNext()) {
				account = new Account();
				int id = cursor.getInt(cursor.getColumnIndex("accountid"));
				account.setId(id);
				String name = cursor.getString(cursor.getColumnIndex("name"));
				account.setName(name);
				float money = Float.parseFloat(cursor.getString(cursor
						.getColumnIndex("money")));
				account.setMoney(money);
				String time = cursor.getString(cursor.getColumnIndex("time"));
				account.setTime(time);
				String type = cursor.getString(cursor.getColumnIndex("type"));
				account.setType(type);
				long earnings = cursor.getLong(cursor
						.getColumnIndex("earnings"));
				if (earnings == 0) {
					account.setEarnings(false);
				} else {
					account.setEarnings(true);
				}
				String remark = cursor.getString(cursor
						.getColumnIndex("remark"));
				account.setRemark(remark);
			}
			cursor.close();
			db.close();
		}
		return account;
	}
}
