package com.fernandoapeguero.myinventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fernandoapeguero.myinventory.data.InventoryContract.InventoryEntrys;

/**
 * Created by nico on 8/4/2017.
 */

public class InventoryProvider extends ContentProvider {
    private static final String LOG_TAG = InventoryProvider.class.getName();

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private InventoryDbHelper iDbHelper;

    private static final int PRODUCT = 1000;
    private static final int PRODUCT_ID = 1001;

    @Override
    public boolean onCreate() {

        iDbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    static {
          // SETTING THE URI FOR ALL PRODUCTS AND ADDING IT TO THE URIMATCHER
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY,PRODUCT);

        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY + "/#" , PRODUCT_ID);

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = iDbHelper.getReadableDatabase();

        Cursor cursor = null;

        int match = sUriMatcher.match(uri);
        switch (match){
            case PRODUCT:

               cursor = db.query(InventoryEntrys.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case PRODUCT_ID:

                selection = InventoryEntrys._ID + " = ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(InventoryEntrys.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
               throw  new IllegalArgumentException("Cursor can't query unknow uri" + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        int match = sUriMatcher.match(uri);

        switch (match){
            case PRODUCT:
               return InventoryEntrys.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return InventoryEntrys.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);

        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int match = sUriMatcher.match(uri);
        switch (match){
            case PRODUCT:
                // insert method for inserting the values into the database
                saveInventory(uri,values);
                break;
            default:
                throw new IllegalArgumentException("insertion is not supported for " + uri);
        }
   getContext().getContentResolver().notifyChange(uri,null);
        return null;
    }

    private Uri saveInventory(Uri uri,ContentValues values){



        SQLiteDatabase db = iDbHelper.getWritableDatabase();

       long id =  db.insert(InventoryEntrys.TABLE_NAME,null,values);

         if (id == -1 ){

             Log.e(LOG_TAG, "failed to insert row for " + uri);
         }

         getContext().getContentResolver().notifyChange(uri,null);
     return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = iDbHelper.getWritableDatabase();

        int deletedRows;


        int match = sUriMatcher.match(uri);

        switch (match){
            case PRODUCT:

               deletedRows = db.delete(InventoryEntrys.TABLE_NAME,selection,selectionArgs);
                break;
            case PRODUCT_ID:

                selection = InventoryEntrys._ID + " = ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                deletedRows = db.delete(InventoryEntrys.TABLE_NAME,selection,selectionArgs);
               break;
                default:
                    throw new IllegalArgumentException("can not match uri " + uri);
        }



        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = iDbHelper.getWritableDatabase();

       int updatedRows =  db.update(InventoryEntrys.TABLE_NAME,values,selection,selectionArgs);

        if (updatedRows != 0){

            db.update(InventoryEntrys.TABLE_NAME,values,selection,selectionArgs);

            getContext().getContentResolver().notifyChange(uri,null);
        }


        return updatedRows;
    }
}
