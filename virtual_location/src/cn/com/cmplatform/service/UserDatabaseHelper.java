package cn.com.cmplatform.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {
	static String name="user.db";
	static int dbVersion=1;
	public UserDatabaseHelper(Context context) {
		super(context, name, null,dbVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql="create table user(id integer primary key autoincrement,username varchar(20),password varchar(20))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
