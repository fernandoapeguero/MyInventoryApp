package com.fernandoapeguero.myinventory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fernandoapeguero.myinventory.data.InventoryContract.InventoryEntrys;
import com.fernandoapeguero.myinventory.data.InventoryDbHelper;

public class ProductActivity extends AppCompatActivity {

    private InventoryDbHelper iDbHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fabIntent = new Intent(ProductActivity.this, AddAProduct.class);
                startActivity(fabIntent);

            }
        });

        iDbHelper = new InventoryDbHelper(ProductActivity.this);

    }

    private void insertDummyData(){

        SQLiteDatabase db = iDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryEntrys.PRODUCT_NAME, " Lasagna en polvo ");
        values.put(InventoryEntrys.PRODUCT_PRICE,   12);
        values.put(InventoryEntrys.PRODUCT_QUANTITY , 20);
        values.put(InventoryEntrys.PRODUCT_WEIGHT, 3);

        long id =  db.insert(InventoryEntrys.TABLE_NAME, null , values);

        if (id == -1){

            throw new IllegalArgumentException( " values failed to be added to databse ");
        } else {

            Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo (){


        SQLiteDatabase db = iDbHelper.getReadableDatabase();

        String[] projection = {
                InventoryEntrys._ID,
                InventoryEntrys.PRODUCT_NAME,
                InventoryEntrys.PRODUCT_PRICE,
                InventoryEntrys.PRODUCT_QUANTITY,
                InventoryEntrys.PRODUCT_WEIGHT,
        };

        Cursor cursor = db.query(
                InventoryEntrys.TABLE_NAME,
                projection,
                null,
                null,null,null,null);
        TextView displayText = (TextView) findViewById(R.id.example_preview);
        displayText.setText("inventory table contains ");
        try {


            int nameColumnIndex = cursor.getColumnIndex(InventoryEntrys.PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntrys.PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntrys.PRODUCT_QUANTITY);
            int weightColumnIndex = cursor.getColumnIndex(InventoryEntrys.PRODUCT_WEIGHT);

            while (cursor.moveToNext()) {

                String productName = cursor.getString(nameColumnIndex);
                int productPrice = cursor.getInt(priceColumnIndex);
                int productQuantity = cursor.getInt(quantityColumnIndex);
                int productWeight = cursor.getInt(weightColumnIndex);


                displayText.append("table info " + InventoryEntrys.TABLE_NAME + " " + productName + " " + productPrice + " " + productQuantity + " " + productWeight);
            }
        } finally {
            cursor.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.insert_dummy_data:
                // insert logic to insert dummy data to inventory database
                insertDummyData();
                displayDatabaseInfo();

                return true;
            case R.id.delete_all:
                // insert logic to deleted all of the product in the database

                return true;
            default:
            return super.onOptionsItemSelected(item);
        }

    }
}
