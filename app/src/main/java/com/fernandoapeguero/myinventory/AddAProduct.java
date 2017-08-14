package com.fernandoapeguero.myinventory;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fernandoapeguero.myinventory.data.InventoryContract.InventoryEntrys;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.Integer.parseInt;

public class AddAProduct extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int EDIT_INIT_LOADER = 0;
    private static final String STATE_URI = "STATE_URI";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String LOG_TAG = AddAProduct.class.getName();
    @BindView(R.id.product_name_editfield)
    EditText editProductName;
    @BindView(R.id.product_price_editfield)
    EditText editProductPrice;
    @BindView(R.id.product_quantity_editfield)
    EditText editProductQuantity;
    @BindView(R.id.product_weight_editfield)
    EditText editProductWeight;
    @BindView(R.id.image_String_uri)
    TextView mTextView;
    @BindView(R.id.add_image)
    ImageView mImageView;
    private Uri mCurrentUri;
    private boolean hasProductChange = false;
    public View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            hasProductChange = true;
            return false;
        }
    };
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        ButterKnife.bind(this);

        // getting data from the uri pass from the activity to set the single item on the activity with the correct information
        Intent intent = getIntent();

        mCurrentUri = intent.getData();

        if (mCurrentUri == null) {

            setTitle("Add a Product");

            invalidateOptionsMenu();
        } else {
            setTitle("Edit a Product");

            getLoaderManager().initLoader(EDIT_INIT_LOADER, null, this);
        }

        editProductName.setOnTouchListener(mTouchListener);
        editProductQuantity.setOnTouchListener(mTouchListener);
        editProductWeight.setOnTouchListener(mTouchListener);
        editProductPrice.setOnTouchListener(mTouchListener);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageSelector();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_menu, menu);

        return true;
    }

    private void deleteThisPetDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this Product?");
        builder.setPositiveButton(R.string.delete_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct();
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertIt = builder.create();
        alertIt.show();
    }

    private void showUnsavedChanges(DialogInterface.OnClickListener diascardChanges) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes);
        builder.setPositiveButton("Discard", diascardChanges);

        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertIt = builder.create();
        alertIt.show();

    }

    /**
     * delete a single item out of the table
     */

    private void deleteProduct() {

        if (mCurrentUri != null) {

            int rowDeleted = getContentResolver().delete(mCurrentUri, null, null);

            if (rowDeleted == 0) {

                Toast.makeText(this, "Error deleting product", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, "Product have been deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * save information for the new product in the table
     */
    private void saveProduct() {

        String productName = editProductName.getText().toString().trim();
        String productPrice = editProductPrice.getText().toString().trim();
        String productQuantity = editProductQuantity.getText().toString().trim();
        String productWeight = editProductWeight.getText().toString().trim();

        String textString = mTextView.getText().toString().trim();

        if (mCurrentUri == null && TextUtils.isEmpty(productName) || TextUtils.isEmpty(productPrice) ||
                TextUtils.isEmpty(productQuantity) || TextUtils.isEmpty(productWeight) || TextUtils.isEmpty(textString)) {
            Toast.makeText(this, "fill all of the field before adding product", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = 0;
        int quantity = 0;
        int weight = 0;

        if (!TextUtils.isEmpty(productPrice)) {
            price = Double.parseDouble(productPrice);
        }

        if (!TextUtils.isEmpty(productQuantity)) {

            quantity = parseInt(productQuantity);
        }

        if (!TextUtils.isEmpty(productWeight)) {

            weight = parseInt(productWeight);

        }

        ContentValues values = new ContentValues();
        values.put(InventoryEntrys.PRODUCT_IMAGE, textString);
        values.put(InventoryEntrys.PRODUCT_NAME, productName);
        values.put(InventoryEntrys.PRODUCT_PRICE, price);
        values.put(InventoryEntrys.PRODUCT_QUANTITY, quantity);
        values.put(InventoryEntrys.PRODUCT_WEIGHT, weight);


        if (mCurrentUri == null) {
            getContentResolver().insert(InventoryEntrys.CONTENT_URI, values);

        } else {

            getContentResolver().update(mCurrentUri, values, null, null);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentUri == null) {

            MenuItem menuItem = menu.findItem(R.id.delete_product);
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public void onBackPressed() {

        if (!hasProductChange) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardChanges = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };

        showUnsavedChanges(discardChanges);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.im_done:
                saveProduct();
                finish();
                // write the logic to save the inventory data for the product
                return true;
            case R.id.delete_product:
                deleteThisPetDialog();
                // write logic to deleted specific product from the inventory
                return true;
            case android.R.id.home:

                if (!hasProductChange) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;

                }

                DialogInterface.OnClickListener discardChanges = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(AddAProduct.this);

                    }
                };

                showUnsavedChanges(discardChanges);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                InventoryEntrys._ID,
                InventoryEntrys.PRODUCT_NAME,
                InventoryEntrys.PRODUCT_IMAGE,
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
            int imageColumIndex = cursor.getColumnIndex(InventoryEntrys.PRODUCT_IMAGE);


            String productName = cursor.getString(nameColumnIndex);
            String productImage = cursor.getString(imageColumIndex);
            int productPrice = cursor.getInt(priceColumnIndex);
            int productQuantity = cursor.getInt(quantityColumnIndex);
            int productWeight = cursor.getInt(weightColumnIndex);

            if (!TextUtils.isEmpty(productImage)) {
                mTextView.setText(productImage);
                Uri uri = Uri.parse(productImage);
                mImageView.setImageBitmap(getBitmapFromUri(uri));
            }
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

    public void openImageSelector() {
        Intent intent;

        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }

        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(STATE_URI) &&
                !savedInstanceState.getString(STATE_URI).equals("")) {
            imageUri = Uri.parse(savedInstanceState.getString(STATE_URI));
            mTextView.setText(imageUri.toString());

            ViewTreeObserver viewTreeObserver = mImageView.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    mImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mImageView.setImageBitmap(getBitmapFromUri(imageUri));
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code READ_REQUEST_CODE.
        // If the request code seen here doesn't match, it's the response to some other intent,
        // and the below code shouldn't run at all.

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.  Pull that uri using "resultData.getData()"

            if (resultData != null) {
                imageUri = resultData.getData();
                Log.i(LOG_TAG, "Uri: " + imageUri.toString());

                mTextView.setText(imageUri.toString());
                mImageView.setImageBitmap(getBitmapFromUri(imageUri));
            }
        }
//        else if (requestCode == SEND_MAIL_REQUEST && resultCode == Activity.RESULT_OK) {
//
//        }
    }

    public Bitmap getBitmapFromUri(Uri uri) {

        if (uri == null || uri.toString().isEmpty())
            return null;

        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        InputStream input = null;
        try {
            input = this.getContentResolver().openInputStream(uri);

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            input = this.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();
            return bitmap;

        } catch (FileNotFoundException fne) {
            Log.e(LOG_TAG, "Failed to load image.", fne);
            return null;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to load image.", e);
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException ioe) {

            }
        }
    }
}
