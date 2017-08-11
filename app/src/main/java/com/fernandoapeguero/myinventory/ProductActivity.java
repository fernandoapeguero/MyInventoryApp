package com.fernandoapeguero.myinventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fernandoapeguero.myinventory.data.InventoryContract.InventoryEntrys;
import com.fernandoapeguero.myinventory.data.InventoryCursorAdapter;

public class ProductActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{



    private static final int INVENTORY_LOADER = 1;

    private InventoryCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        ListView listviewProduct = (ListView) findViewById(R.id.list_view);

        mCursorAdapter = new InventoryCursorAdapter(this,null);

        listviewProduct.setAdapter(mCursorAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fabIntent = new Intent(ProductActivity.this, AddAProduct.class);

                startActivity(fabIntent);

            }
        });

        listviewProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent listItent = new Intent(ProductActivity.this, ProductOrder.class);

                Uri currentUri = ContentUris.withAppendedId(InventoryEntrys.CONTENT_URI,id);

                listItent.setData(currentUri);

                startActivity(listItent);
            }
        });

         getLoaderManager().initLoader(INVENTORY_LOADER,null,this);

    }

    private void insertDummyData(){



        ContentValues values = new ContentValues();
        values.put(InventoryEntrys.PRODUCT_NAME, " cheese cake  ");
        values.put(InventoryEntrys.PRODUCT_PRICE,   12);
        values.put(InventoryEntrys.PRODUCT_QUANTITY , 20);
        values.put(InventoryEntrys.PRODUCT_WEIGHT, 3);

        Uri newUri = getContentResolver().insert(InventoryEntrys.CONTENT_URI,values);

    }

    private void deleteAllProducts(){


        getContentResolver().delete(InventoryEntrys.CONTENT_URI,null,null);


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

                return true;
            case R.id.delete_all:
                deleteAllProducts();
                // insert logic to deleted all of the product in the database

                return true;
            default:
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                InventoryEntrys._ID,
                InventoryEntrys.PRODUCT_NAME,
                InventoryEntrys.PRODUCT_PRICE,
                InventoryEntrys.PRODUCT_QUANTITY,
                InventoryEntrys.PRODUCT_WEIGHT
        };
        return new CursorLoader(this,InventoryEntrys.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mCursorAdapter.swapCursor(null);
    }
}
