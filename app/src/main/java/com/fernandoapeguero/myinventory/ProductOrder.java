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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fernandoapeguero.myinventory.data.InventoryContract.InventoryEntrys;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductOrder extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ORDER_PRODUCT_LOADER = 1;
    @BindView(R.id.product_name_order)
    TextView nameOrder;
    @BindView(R.id.quantity_order_textview)
    TextView quantityOrder;
    @BindView(R.id.product_price_order_textview)
    TextView priceOrder;
    @BindView(R.id.weight_order_textview)
    TextView weightOrder;
    @BindView(R.id.summarize_order)
    TextView summarize_textview;
    @BindView(R.id.order_now)
    Button orderNow;
    @BindView(R.id.minus_button)
    Button minusButton;
    @BindView(R.id.plus_button)
    Button plusButton;
    @BindView(R.id.quantity_live_textview)
    TextView liveQuantityTextview;
    private int productQuantity;
    private double productPrice;
    private int productWeight;
    private int myOrderQuantity = 0;
    private Uri mCurrentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_order);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        mCurrentUri = intent.getData();

        if (mCurrentUri != null) {
            getLoaderManager().initLoader(ORDER_PRODUCT_LOADER, null, this);

        }
        buyProduct();

        orderNow.setVisibility(View.GONE);

    }

    private void editProduct() {

        Intent intent = new Intent(ProductOrder.this, AddAProduct.class);

        Uri contenUri = mCurrentUri;

        intent.setData(contenUri);

        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edi_product_onmenu:
                editProduct();
                finish();
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

        return new CursorLoader(this, mCurrentUri, projection, null, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {

            int nameColumnIndex = cursor.getColumnIndex(InventoryEntrys.PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntrys.PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntrys.PRODUCT_QUANTITY);
            int weightColumnIndex = cursor.getColumnIndex(InventoryEntrys.PRODUCT_WEIGHT);

            String productName = cursor.getString(nameColumnIndex);
            productPrice = cursor.getDouble(priceColumnIndex);
            productQuantity = cursor.getInt(quantityColumnIndex);
            productWeight = cursor.getInt(weightColumnIndex);


            nameOrder.setText(productName);
            priceOrder.setText(Double.toString(productPrice));
            quantityOrder.setText(Integer.toString(productQuantity));
            weightOrder.setText(Integer.toString(productWeight));


        }
    }

    private void buyProduct() {

        liveQuantityTextview.setText("" + myOrderQuantity);

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myOrderQuantity += 1;

                liveQuantityTextview.setText("" + myOrderQuantity);
                updateInformation(myOrderQuantity);

            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOrderQuantity -= 1;
                if (myOrderQuantity < 0) {

                    myOrderQuantity = 0;
                    liveQuantityTextview.setText("" + myOrderQuantity);

                } else {
                    liveQuantityTextview.setText("" + myOrderQuantity);
                    updateInformation(myOrderQuantity);
                }

                if (productQuantity > 0) {

                    decreaseStock();

                }
            }
        });

    }

    private void decreaseStock() {
        if (productQuantity != 0) {
            int decreaseValue = productQuantity - 1;

            ContentValues values = new ContentValues();
            values.put(InventoryEntrys.PRODUCT_QUANTITY, decreaseValue);

            getContentResolver().update(mCurrentUri, values, null, null);
        }

    }

    private void updateInformation(int localQuantity) {


        int totalWeight = localQuantity * productWeight;

        int totalPrice = (int) (localQuantity * productPrice);

        int newValue = productQuantity + localQuantity;

        if (newValue < 0) {

            newValue = 0;
        }
        orderNow.setVisibility(View.VISIBLE);

        summarize_textview.setText("Product : " + nameOrder.getText() + "\n" + "Quantity Order : " + localQuantity + "\n" + "Order Total : $" + totalPrice + "\n" + "Order Weight : " + totalWeight);

        final int finalNewValue = newValue;
        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(InventoryEntrys.PRODUCT_QUANTITY, finalNewValue);
                if (finalNewValue > 0) {
                    Toast.makeText(ProductOrder.this, "Product Have Been purchase, Thank you for you business.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ProductOrder.this, "Quantity require for purchase is at least 1", Toast.LENGTH_SHORT).show();
                }
                getContentResolver().update(mCurrentUri, values, null, null);

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/html");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Order  more " + nameOrder.getText() + " Product");
                emailIntent.putExtra(Intent.EXTRA_TEXT, summarize_textview.getText().toString());

                if (emailIntent.resolveActivity(getPackageManager()) != null) {

                    startActivity(emailIntent);
                }
                finish();
            }
        });


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        nameOrder.setText("");
        priceOrder.setText("");
        weightOrder.setText("");
        quantityOrder.setText("");

    }
}
