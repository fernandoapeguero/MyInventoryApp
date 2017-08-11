package com.fernandoapeguero.myinventory;

import android.content.ContentValues;
import android.content.Intent;
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

public class AddAProduct extends AppCompatActivity {


    @BindView(R.id.product_name_editfield)
    EditText editProductName;
    @BindView(R.id.product_price_editfield) EditText editProductPrice;
    @BindView(R.id.product_quantity_editfield) EditText editProductQuantity;
    @BindView(R.id.product_weight_editfield) EditText editProductWeight;


    private Uri mCurrentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        mCurrentUri = intent.getData();

        if (mCurrentUri == null){

            setTitle("Add a Product");

            invalidateOptionsMenu();
        } else {
            setTitle("Edit a Product");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_menu, menu);

        return true;
    }


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


private void saveProduct(){

    String productName = editProductName.getText().toString().trim();
    String productPrice = editProductPrice.getText().toString().trim();
    String productQuantity = editProductQuantity.getText().toString().trim();
    String productWeight = editProductWeight.getText().toString().trim();

    if (mCurrentUri == null && TextUtils.isEmpty(productName) && TextUtils.isEmpty(productPrice) &&
            TextUtils.isEmpty(productQuantity) && TextUtils.isEmpty(productWeight)){return;}

    int price = 0 ;
    int quantity = 0 ;
    int weight = 0 ;

 if (!TextUtils.isEmpty(productPrice)){

     price = Integer.parseInt(productPrice);
 }

 if (!TextUtils.isEmpty(productQuantity)){

     quantity = Integer.parseInt(productQuantity);
 }

 if (!TextUtils.isEmpty(productWeight)){

     weight = Integer.parseInt(productWeight);


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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.im_done:
                saveProduct();
                finish();
                // write the logic to save the inventory data for the product
                return true;
            case R.id.delete_product:
                deleteProduct();
                // write logic to deleted specific product from the inventory
                return true;
            default:

                return super.onOptionsItemSelected(item);

        }

    }
}
