package com.yoursh.dfgden.yorsh.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.yoursh.dfgden.yorsh.database.tables.PlayerTable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "index_records";
    public static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PlayerTable.onCreate(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PlayerTable.onUpgrade(db);
        onCreate(db);
    }
}
