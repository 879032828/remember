package com.seventh.db;

import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PersonDBdao {
	private Context context;
	MyDBOpenHelper dbOpenHelper;

	public PersonDBdao(Context context) {
		this.context = context;
		dbOpenHelper = new MyDBOpenHelper(context);
	}
	
	/**
	 * 添加一条记录
	 */
	public void add(String name, String pwd) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			// db.execSQL("insert into person (name,age) values (?,?)",new Object[]{name,age});
			// db.execSQL("insert into person ",null) // 不合法的sql语句
			ContentValues values = new ContentValues();
			values.put("name", name);
			values.put("possward", pwd);
			values.put("login", false);
			// 如果 contentvalues为空
			db.insert("person", null, values); // 组拼sql语句完成的添加的操作
			
			// insert into person name values (NULL) ;
			db.close();
		}

	}

	/**
	 * 删除一条记录
	 */
	public void delete(String name) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete("person", "name=?", new String[] { name });
			db.close();
		}
	}

	/**
	 * 数据库的更改操作
	 * 帐户名 密码
	 */
	public void update(String name, String newname, String newpwd) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put("name", newname);
			values.put("possward", newpwd);
			values.put("login", false);
			db.update("person", values, "name=?", new String[] { name });
			db.close();
		}
	}
	/**
	 * 数据库的更改操作
	 * 登录记录
	 */
	public void updateLoginOK(String name) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put("login", true);
			db.update("person", values, "name=?", new String[] { name });
			db.close();
		}
	}
	
	/**
	 * 数据库的更改操作
	 * 登录记录
	 */
	public void updateLoginCancel(String name) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put("login", false);
			db.update("person", values, "name=?", new String[] { name });
			db.close();
		}
	}

	/**
	 * 数据库的查询操作 判断有无该数据
	 * 判断用户是否存在
	 */
	public boolean find(String name) {
		boolean result = false;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			// select * from person
			Cursor cursor = db.query("person", null, "name=?",
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
	 * 数据库的查询操作 判断有无该数据
	 * 判断登录结果
	 */
	public boolean findLogin(String name,String pwd) {
		boolean result = false;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			// select * from person
			Cursor cursor = db.query("person", null, "name=? and possward=?",
					new String[] { name ,pwd}, null, null, null);
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
	public List<Person> findAll() {
		List<Person> persons = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.query("person", null, null, null, null, null,
					null);
			persons = new ArrayList<Person>();
			while (cursor.moveToNext()) {
				Person person = new Person();
				String name = cursor.getString(cursor.getColumnIndex("name"));
				person.setName(name);
				long login = cursor.getLong(cursor.getColumnIndex("login"));
				if(login==0){
					person.setLogin(false);
				}else{
					person.setLogin(true);
				}
				
				persons.add(person);
			}
			cursor.close();
			db.close();
		}
		return persons;
	}


	/**
	 * 查询所有信息
	 */
	public Cursor findAllbyCursor() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			/*Cursor cursor = db.query("person", null, null, null, null, null,
					null);*/
			Cursor cursor = db.rawQuery("select personid as _id,name from person", null);
			cursor.close(); //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
			return cursor;
			
			// 注意了  一定不要把数据库 关闭了 
			}
		return null;
		
	}
	
	public Person findLoginOk() {
		Person person = null; 
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select personid as _id,name from person where login=1", null);
			if (cursor.moveToNext()) {
				person = new Person();
				String name = cursor.getString(cursor.getColumnIndex("name"));
				person.setName(name);
				person.setLogin(true);
			}
			cursor.close();
			db.close();
		}
		return person;
	}

}
