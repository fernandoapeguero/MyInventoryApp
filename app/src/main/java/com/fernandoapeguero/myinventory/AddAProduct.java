package com.fernandoapeguero.myinventory;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.fernandoapeguero.myinventory.data.InventoryContract.InventoryEntrys;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.Integer.parseInt;

public class AddAProduct extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    @BindView(R.id.product_name_editfield)
    EditText editProductName;
    @BindView(R.id.product_price_editfield) EditText editProductPrice;
    @BindView(R.id.product_quantity_editfield) EditText editProductQuantity;
    @BindView(R.id.product_weight_editfield) EditText editProductWeight;


    private Uri mCurrentUri;

    private static final int EDIT_INIT_LOADER = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        ButterKnife.bind(this);

        // getting data from the uri pass from the activity to set the single item on the activity with the correct information
        Intent intent = getIntent();

        mCurrentUri = intent.getData();

        if (mCurrentUri == null){

            setTitle("Add a Product");

            invalidateOptionsMenu();
        } else {
            setTitle("Edit a Product");

            getLoaderManager().initLoader(EDIT_INIT_LOADER,null,this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_menu, menu);

        return true;
    }

    /**
     * delete a single item out of the table
     */

    private void deleteProduct(){

        if (mCurrentUri != null){

         int rowDeleted  =   getContentResolver().delete(mCurrentUri,null,null);

            if (rowDeleted == 0){

                Toast.makeText(this, "Error deleting product", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, "Product have been deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * save information for the new product in the table
     */
    private void saveProduct(){

    String productName = editProductName.getText().toString().trim();
    String productPrice = editProductPrice.getText().toString().trim();
    String productQuantity = editProductQuantity.getText().toString().trim();
    String productWeight = editProductWeight.getText().toString().trim();

    if (mCurrentUri == null && TextUtils.isEmpty(productName) && TextUtils.isEmpty(productPrice) &&
            TextUtils.isEmpty(productQuantity) && TextUtils.isEmpty(productWeight)){return;}

   double price = 0 ;
    int quantity = 0 ;
    int weight = 0 ;

 if (!TextUtils.isEmpty(productPrice)){
       price =  Double.parseDouble(productPrice);
 }

 if (!TextUtils.isEmpty(productQuantity)){

     quantity = parseInt(productQuantity);
 }

 if (!TextUtils.isEmpty(productWeight)){

     weight = parseInt(productWeight);

 }

    ContentValues values = new ContentValues();
    values.put(InventoryEntrys.PRODUCT_NAME , productName);
    values.put(InventoryEntrys.PRODUCT_PRICE, price);
    values.put(InventoryEntrys.PRODUCT_QUANTITY, quantity);
    values.put(InventoryEntrys.PRODUCT_WEIGHT, weight);

 if (mCurrentUri == null){
     getContentResolver().insert(InventoryEntrys.CONTENT_URI,values);

 } else {

     getContentResolver().update(mCurrentUri,values,null,null);
 }

}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentUri == null){

            MenuItem menuItem = menu.findItem(R.id.delete_product);
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.im_done:
                saveProduct();
                finish();
                // write the logic to save the inventory data for the product
                return true;
            case R.id.delete_product:
                deleteProduct();
                finish();
                // write logic to deleted specific product from the inventory
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

        return new CursorLoader(this,mCurrentUri,projection,null,null,null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1){
            return;
        }

        if (cursor.moveToFirst()){

            int nameColumnIndex = cursor.getColumnIndex(InventoryEntrys.PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntrys.PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntrys.PRODUCT_QUANTITY);
            int weightColumnIndex = cursor.getColumnIndex(InventoryEntrys.PRODUCT_WEIGHT);


            String productName = cursor.getString(nameColumnIndex);
            int productPrice = cursor.getInt(priceColumnIndex);
            int productQuantity = cursor.getInt(quantityColumnIndex);
            int productWeight = cursor.getInt(weightColumnIndex);


            editProductName.setText(productName);
            editProductPrice.setText(Integer.toString(productPrice));
            editProductQuantity.setText(Integer.toString(productQuantity));
            editProductWeight.setText(Integer.toString(productWeight));
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        editProductName.setText("");
        editProductPrice.setText("");
        editProductQuantity.setText("");
        editProductWeight.setText("");

    }
}
