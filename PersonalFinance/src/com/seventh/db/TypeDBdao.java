package com.seventh.db;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import com.seventh.db.Type;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TypeDBdao {

	private Context context;
	MyDBOpenHelper dbOpenHelper;
	
	public TypeDBdao(Context context){
		this.context = context;
		dbOpenHelper = new MyDBOpenHelper(context);
	}
	
	public void addNewType(List<Type> types, String name){
		
	}
	
	
	/**
	 * @param name
	 * @param type
	 * 根据姓名和收支类型查找对应的所有类型
	 */
	public void findAllType(String name, Boolean type){
		List<Type> TypeCount = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from type where name=? and type = true", new String[] { name});
			TypeCount = new ArrayList<Type>();
			while(cursor.moveToNext()){
				Type type2 = new Type();
				int id = cursor.getInt(cursor.getColumnIndex("typeid"));
				type2.setTypeid(id);
				Boolean types = cursor.get
			}
		}
	}
	
}
