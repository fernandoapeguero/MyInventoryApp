package com.fernandoapeguero.myinventory.data;

import android.provider.BaseColumns;

/**
 * Created by nico on 8/3/2017.
 */

public class InventoryContract  {

    public static final class InventoryEntrys implements BaseColumns{

        public static final String TABLE_NAME = "inventory";
        public static final String PRODUCT_NAME = "name";
        public static final String PRODUCT_PRICE = "price";
        public static final String PRODUCT_QUANTITY = "quantity";
        public static final String PRODUCT_WEIGHT = "weight";


    }
}
