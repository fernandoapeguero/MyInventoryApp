package com.fernandoapeguero.myinventory.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fernandoapeguero.myinventory.R;
import com.fernandoapeguero.myinventory.data.InventoryContract.InventoryEntrys;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nico on 8/7/2017.
 */

public class InventoryCursorAdapter extends CursorAdapter {

    public InventoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view =  LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(final View view, Context context, final Cursor cursor){
        ViewHolder holder = (ViewHolder) view.getTag();

        String productName = cursor.getString(cursor.getColumnIndexOrThrow(InventoryEntrys.PRODUCT_NAME));
        String productPrice = cursor.getString(cursor.getColumnIndexOrThrow(InventoryEntrys.PRODUCT_PRICE));
        String productQuantity = cursor.getString(cursor.getColumnIndexOrThrow(InventoryEntrys.PRODUCT_QUANTITY));

        final int rowId = cursor.getInt(cursor.getColumnIndex(InventoryEntrys._ID));


        final int quantityParse = Integer.parseInt(productQuantity);

        holder.saleButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                int quantity = quantityParse;

                if (quantity > 0 ){
                    quantity -= 1;

                    ContentValues values = new ContentValues();
                    Uri updatedUri = ContentUris.withAppendedId(InventoryEntrys.CONTENT_URI , rowId);
                    values.put(InventoryEntrys.PRODUCT_QUANTITY, quantity);
                    view.getContext().getContentResolver().update(updatedUri, values,null , null);
                } else {
                    Toast.makeText(view.getContext(), "sorry the product is out of stock ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.product.setText(productName);
        holder.price.setText(productPrice);
        holder.quantity.setText(productQuantity);

    }

    static class ViewHolder {
        @BindView(R.id.product_name_textview) TextView product;
        @BindView(R.id.product_price_textview) TextView price;
        @BindView(R.id.product_quantity_textView) TextView quantity;
        @BindView(R.id.sale_button)
        Button saleButton;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }

    }
}
