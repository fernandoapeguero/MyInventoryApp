package com.fernandoapeguero.myinventory.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.fernandoapeguero.myinventory.R;

import com.fernandoapeguero.myinventory.data.InventoryContract.InventoryEntrys;

/**
 * Created by nico on 8/7/2017.
 */

public class InventoryCursorAdapter extends CursorAdapter {



    public InventoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        TextView product = (TextView) view.findViewById(R.id.product_name_textview);
        TextView price = (TextView) view.findViewById(R.id.product_price_textview);
        TextView quantity = (TextView) view.findViewById(R.id.product_quantity_textView);
        TextView weight = (TextView) view.findViewById(R.id.weight_textview);

        String productName = cursor.getString(cursor.getColumnIndexOrThrow(InventoryEntrys.PRODUCT_NAME));
        String productPrice = cursor.getString(cursor.getColumnIndexOrThrow(InventoryEntrys.PRODUCT_PRICE));
        String productQuantity = cursor.getString(cursor.getColumnIndexOrThrow(InventoryEntrys.PRODUCT_QUANTITY));
        String productWeight = cursor.getString(cursor.getColumnIndexOrThrow(InventoryEntrys.PRODUCT_WEIGHT));


        product.setText(productName);
        price.setText(productPrice);
        quantity.setText(productQuantity);
        weight.setText(productWeight);


    }
}
