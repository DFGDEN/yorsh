package com.yoursh.dfgden.yorsh.database.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


import com.yoursh.dfgden.yorsh.database.DBContentProvider;
import com.yoursh.dfgden.yorsh.models.PlayerModel;

/**
 * Created by dfgden on 21.6.16.
 */
public class PlayerTable {

    public static final String NAME = "PlayerTable";

    public static final String PLAYER_NAME = "player_name";
    public static final String PLAYER_CURRENT_POINTS = "player_current_points";
    public static final String PLAYER_ICON_ID = "player_icon_id";
    public static final String PLAYER_TURN_ICON_ID = "player_turn_icon_id";
    public static final String PLAYER_ALL_POINTS = "player_all_points";
    public static final String PLAYER_SECRET_KEY = "player_secret_points";


    public static final Uri CONTENT_URI = DBContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(NAME).build();

    public static final String CREATE_TABLE_REQUEST = "create table " + NAME + "("
            + "_id integer primary key autoincrement,"
            + PLAYER_CURRENT_POINTS + " integer,"
            + PLAYER_ICON_ID + " text,"
            + PLAYER_TURN_ICON_ID + " text,"
            + PLAYER_NAME + " text,"
            + PLAYER_ALL_POINTS + " integer,"
            + PLAYER_SECRET_KEY + " text"
            + ");";

    public PlayerTable() {
    }

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_REQUEST);
    }

    public static void onUpgrade(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_REQUEST + "'");
    }

    public static ContentValues createContentValues(PlayerModel musicContainer) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER_CURRENT_POINTS,musicContainer.getPoints());
        contentValues.put(PLAYER_ICON_ID,musicContainer.getIconContentName());
        contentValues.put(PLAYER_TURN_ICON_ID,musicContainer.getIconContentTurnName());
        contentValues.put(PLAYER_NAME, musicContainer.getName());
        contentValues.put(PLAYER_ALL_POINTS, musicContainer.getName());
        contentValues.put(PLAYER_SECRET_KEY, musicContainer.getSecretKey());
        return contentValues;
    }



}
