package com.yoursh.dfgden.yorsh.database;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.yoursh.dfgden.yorsh.database.listeners.GetPlayersListener;
import com.yoursh.dfgden.yorsh.database.tables.PlayerTable;
import com.yoursh.dfgden.yorsh.models.PlayerModel;

import java.util.ArrayList;


public class DataProvider {

    private ContentResolver mContentResolver;
    private static volatile DataProvider mInstance;
    private Context context;

    public static boolean initInstance(Context context) {
        DataProvider localInstance = mInstance;
        if (localInstance == null) {
                localInstance = mInstance;
                if (localInstance == null) {
                    mInstance = localInstance = new DataProvider(context);
            }
        }
        DataProvider dataProvider = localInstance;
        if (dataProvider != null) {
            return true;
        } else
            return false;
    }

    public static DataProvider getInstance() {
        return mInstance;
    }


    private DataProvider(Context context) {
        this.context = context;
        this.mContentResolver = context.getContentResolver();
    }


    public long savePlayer(PlayerModel playerModel) {
        ContentValues contentValues = PlayerTable.createContentValues(playerModel);
        return saveValues(contentValues, PlayerTable.CONTENT_URI);
    }

    public void getPlayers(final GetPlayersListener getPlayersListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<PlayerModel> playerModels = new ArrayList<>();
                String request = "select * FROM " + PlayerTable.NAME;
                Cursor cursor = mContentResolver.query(DBContentProvider.BASE_CONTENT_URI, null, request, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    int idPlayerName = cursor.getColumnIndex(PlayerTable.PLAYER_NAME);
                    int idPlayerCurrentPoint = cursor.getColumnIndex(PlayerTable.PLAYER_CURRENT_POINTS);
                    int idPlayerAllPoint = cursor.getColumnIndex(PlayerTable.PLAYER_ALL_POINTS);
                    int idPhotoSecretKey = cursor.getColumnIndex(PlayerTable.PLAYER_SECRET_KEY);
                    int idIconId = cursor.getColumnIndex(PlayerTable.PLAYER_ICON_ID);

                    do {
                        PlayerModel playerModel = new PlayerModel(
                                cursor.getString(idPlayerName),
                                cursor.getString(idIconId),
                                cursor.getInt(idPlayerCurrentPoint),
                                cursor.getInt(idPlayerAllPoint),
                                cursor.getString(idPhotoSecretKey)
                        );
                        playerModels.add(playerModel);
                    } while (cursor.moveToNext());
                    getPlayersListener.getPlayers(playerModels);
                }

                cursor.close();
            }
        }).start();
    }










    public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string) {
        if (c != null && string != null) {
            try {
                return Enum.valueOf(c, string.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
            }
        }
        return null;
    }

    private long saveValues(ContentValues contentValues, Uri contentUri) {
        Uri saveUri = mContentResolver.insert(contentUri, contentValues);
        return ContentUris.parseId(saveUri);
    }

}
