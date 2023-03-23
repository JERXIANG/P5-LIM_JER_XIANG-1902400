package com.example.individualassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteAdapter {
    public static final String MYDATABASE_NAME = "MY_DB";
    public static final String MYDATABASE_TABLE = "Point_Table";
    public static final int MYDATABASE_VERSION = 1;
    public static final String KEY_NAME = "Name";
    public static final String KEY_POINT = "Point";

    private static final String SCRIPT_CREATE_DATABASE = "create table " + MYDATABASE_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + " text not null, " + KEY_POINT + " INTEGER);";
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public SQLiteAdapter(Context c) {
        context = c;
    }

    public SQLiteAdapter openToRead() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return this;
    }

    public SQLiteAdapter openToWrite() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        sqLiteHelper.close();
    }

    public int deleteAll() {
        return sqLiteDatabase.delete(MYDATABASE_TABLE, null, null);
    }

    public int deleteLast() {
        String[] columns = {"id"};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns, null, null, null, null, KEY_POINT + " ASC", "1");

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String whereClause = "id=?";
            String[] whereArgs = {String.valueOf(id)};
            return sqLiteDatabase.delete(MYDATABASE_TABLE, whereClause, whereArgs);
        }
        return -1;
    }

    public long insert(String name, int point) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_POINT, point);
        return sqLiteDatabase.insert(MYDATABASE_TABLE, null, contentValues);
    }

    public String listAllName() {
        String[] columns = new String[]{KEY_NAME};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns, null, null, null, null, KEY_POINT + " DESC");
        String result = "";
        int index_NAME = cursor.getColumnIndex(KEY_NAME);
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            result = result + cursor.getString(index_NAME) + "\n";
        }
        return result;
    }

    public String listAllPoint() {
        String[] columns = new String[]{KEY_POINT};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns, null, null, null, null, KEY_POINT + " DESC");
        String result = "";
        int index_POINT = cursor.getColumnIndex(KEY_POINT);
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            result = result + cursor.getString(index_POINT) + "\n";
        }
        return result;
    }

    public long totalRow() {
        return DatabaseUtils.queryNumEntries(sqLiteDatabase, MYDATABASE_TABLE);
    }

    public int selectLowestRank() {
        String[] columns = new String[]{"MIN(" + KEY_POINT + ")"};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns, null, null, null, null, null);
        int result = 0;
        if (cursor.moveToFirst()) {
            result = cursor.getInt(0);
        }
        return result;

    }

    public class SQLiteHelper extends SQLiteOpenHelper {
        public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SCRIPT_CREATE_DATABASE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
