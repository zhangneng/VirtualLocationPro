package cn.com.cmplatform.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserService {

	private UserDatabaseHelper dbHelper;

	public UserService(Context context) {
		dbHelper = new UserDatabaseHelper(context);
	}

	// 鐢ㄦ埛鐧诲綍
	public boolean login(String username, String password) {
		
		
		SQLiteDatabase sdb = dbHelper.getReadableDatabase();
		String sql = "select * from user where username=? and password=?";
		Cursor cursor = sdb.rawQuery(sql, new String[] { username, password });
		if (cursor.moveToFirst()) {
			cursor.close();
			sdb.close();
			return true;
		}
		sdb.close();
		return false;
	}

	// 鐢ㄦ埛娉ㄥ唽
	public boolean register(User user) {
		SQLiteDatabase sdb = dbHelper.getReadableDatabase();
		String sql = "insert into user(username,password) values(?,?)";
		Object obj[] = { user.getUsername(), user.getPassword() };
		sdb.execSQL(sql, obj);
		sdb.close();
		return true;
	}

	// 鍒嗛〉鍙栨暟鎹�
	public List<User> getScrollData(Integer start, Integer size) {
		String sql = "select id, username,password from user limit ?,?";
		SQLiteDatabase sdb = dbHelper.getReadableDatabase();
		Cursor cursor = sdb.rawQuery(sql, new String[] { String.valueOf(start),
				String.valueOf(size) });
		List<User> userList = new ArrayList<User>();
		User user = null;
		while (cursor.moveToNext()) {
			int id = cursor.getInt(0);
			String name = cursor.getString(1);
			String password = cursor.getString(2);
			user = new User(name, password);
			user.setId(id);
			userList.add(user);
			Log.i("TAG", name);
		}
		cursor.close();
		sdb.close();
		return userList;
	}

	public boolean delete(int userId) {
		try {
			SQLiteDatabase sdb = dbHelper.getReadableDatabase();
			String sql = "delete from user where id=?";
			sdb.execSQL(sql, new Object[] { userId });
			sdb.close();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
}
