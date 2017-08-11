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
    public void bindView(View view, Context context, Cursor cursor){
        ViewHolder holder = (ViewHolder) view.getTag();

        String productName = cursor.getString(cursor.getColumnIndexOrThrow(InventoryEntrys.PRODUCT_NAME));
        String productPrice = cursor.getString(cursor.getColumnIndexOrThrow(InventoryEntrys.PRODUCT_PRICE));
        String productQuantity = cursor.getString(cursor.getColumnIndexOrThrow(InventoryEntrys.PRODUCT_QUANTITY));

        holder.product.setText(productName);
        holder.price.setText(productPrice);
        holder.quantity.setText(productQuantity);

    }

    static class ViewHolder {
        @BindView(R.id.product_name_textview) TextView product;
        @BindView(R.id.product_price_textview) TextView price;
        @BindView(R.id.product_quantity_textView) TextView quantity;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }

    }
}
