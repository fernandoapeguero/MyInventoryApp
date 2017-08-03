package com.fernandoapeguero.myinventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fernandoapeguero.myinventory.data.InventoryContract.InventoryEntrys;

/**
 * Created by nico on 8/3/2017.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "inventory.db";
    public static final int DATABASE_VERSION = 1;


    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_INVENTORY_TABLE_DATABASE = "CREATE TABLE " + InventoryEntrys.TABLE_NAME + " ("
                + InventoryEntrys._ID + " PRIMARY KEY AUTOINCREMENT, "
                + InventoryEntrys.PRODUCT_NAME + " TEXT NOT NULL, "
                + InventoryEntrys.PRODUCT_PRICE + " INTEGER DEFAULT 0, "
                + InventoryEntrys.PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryEntrys.PRODUCT_WEIGHT + " INTEGER DEFAULT 0 );";

        db.execSQL(SQL_INVENTORY_TABLE_DATABASE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
