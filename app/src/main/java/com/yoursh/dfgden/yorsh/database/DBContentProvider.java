package com.yoursh.dfgden.yorsh.database;


import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.yoursh.dfgden.yorsh.database.tables.PlayerTable;

import java.util.ArrayList;

public class DBContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.yoursh.dfgden.yorsh";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private DBHelper mOpenHelper;
    private final UriMatcher mUriMather = buildUriMather();
    private static final int PLAYER = 1;

    @Override
    public boolean onCreate() {
        mOpenHelper = new DBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        return db.rawQuery(selection, null);
    }

    @Override
    public String getType(Uri uri) {

        final int match = mUriMather.match(uri);
        return getTableName(match);
    }

    private String getTableName(int uriType){
        switch (uriType){
            case PLAYER:
                return PlayerTable.NAME;
            default:
                return null;
        }
    }


    private UriMatcher buildUriMather(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, PlayerTable.NAME, PLAYER);
        return matcher;
    }

    @Override
    public synchronized ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {

        ContentProviderResult[] contentProviderResults = super.applyBatch(operations);
        return contentProviderResults;
    }


    @Override
    public synchronized Uri insert(Uri uri, ContentValues values) {
        final String tableName = getTableName(mUriMather.match(uri));

        if(!tableName.isEmpty() && values != null){
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            final long id = db.insert(tableName, null, values);

            final Uri contentUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(contentUri, null);
            return contentUri;
        }
        else {
            throw new IllegalArgumentException("Unknown uri " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SelectionBuilder selectionBuilder = buildSimpleSelection(uri);

        int retVal = selectionBuilder.where(selection, selectionArgs).delete(db);
        getContext().getContentResolver().notifyChange(uri, null);

        return retVal;
    }

    public SelectionBuilder buildSimpleSelection(Uri uri){
        final SelectionBuilder selectionBuilder = new SelectionBuilder();
        final int match = mUriMather.match(uri);
        return selectionBuilder.table(getTableName(match));
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String tableName = getTableName(mUriMather.match(uri));
        return db.update(tableName, values, selection, selectionArgs);
    }
}
