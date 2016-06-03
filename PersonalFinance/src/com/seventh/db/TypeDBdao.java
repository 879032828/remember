package com.seventh.db;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import com.seventh.db.Type;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TypeDBdao {

	private Context context;
	MyDBOpenHelper dbOpenHelper;

	public TypeDBdao(Context context) {
		this.context = context;
		dbOpenHelper = new MyDBOpenHelper(context);
	}

	/*
	 * 1
	 * 
	 * @param name
	 * 
	 * @param type 0代表支出，1代表收入 根据姓名和收支类型查找对应的所有类型
	 */
	public List<Type> findAllType(String name, String type) {
		List<Type> TypeCount = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from type where name=? and type = ?", new String[] { name, type });
			TypeCount = new ArrayList<Type>();
			while (cursor.moveToNext()) {
				Type type2 = new Type();
				// 获取typeid
				int id = cursor.getInt(cursor.getColumnIndex("typeid"));
				type2.setTypeid(id);
				// 获取收支标识
				int types = cursor.getInt(cursor.getColumnIndex("type"));
				type2.setType(types);
				// 获取收支类型名称
				String typename = cursor.getString(cursor.getColumnIndex("typename"));
				type2.setTypename(typename);
				String nameString = cursor.getString(cursor.getColumnIndex("name"));
				type2.setName(nameString);
				TypeCount.add(type2);
			}
		}
		return TypeCount;
	}

	/*
	 * 2
	 * 
	 * @param types 设置默认值，主要用于初始化类型选择列表时，将默认值存储到数据库中
	 */
	public void setDefault(List<String> types, String type, String name) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			for (String typename : types) {
				db.execSQL("insert into type(type,typename,name) values (?,?,?)", new Object[] { type, typename, name });
			}
			db.close();
		}
	}

	/**
	 * 
	 * 3
	 * 
	 * @param type
	 * @param name
	 * @param typename
	 * @return 判断类型是否存在
	 */
	public Boolean isExist(String type, String name, String typename) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from type where type = ? and name = ? and typename = ?",
					new String[] { type, name, typename });
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
	public void addRecord(String type, String typename, String name) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("insert into type(type,typename,name) values (?,?,?)", new Object[] { type, typename, name });
			db.close();
		}
	}

	/**
	 * @param type
	 * @param typename
	 * @param name
	 *            删除一条记录
	 */
	public void deleteRecord(String type, String typename, String name) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("delete from type where type = ? and typename = ? and name = ?", new Object[] { type, typename, name });
			db.close();
		}
	}
	
	/**
	 * 
	 * 
	 * 
	 * @param TypeCount
	 * @return 获取收入类型的字符串集合
	 */
	public List<String> toListString(List<Type> TypeCount) {
		List<String> typename = new ArrayList<String>();
		for (Type type : TypeCount) {
			typename.add(type.getTypename());
		}
		return typename;
	}

}
