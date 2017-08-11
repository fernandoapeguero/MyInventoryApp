package com.fernandoapeguero.myinventory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.ButterKnife;

public class AddAProduct extends AppCompatActivity {



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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.im_done:
                // write the logic to save the inventory data for the product
                return true;
            case R.id.delete_product:
                // write logic to deleted specific product from the inventory
                return true;
            default:

                return super.onOptionsItemSelected(item);

        }

    }
}
