package com.fernandoapeguero.myinventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by nico on 8/3/2017.
 */

public class InventoryContract  {

    public static final String CONTENT_AUTHORITY = "com.inventoryprovider";
    public static final String PATH_INVENTORY = "inventory";
    public static final Uri BASECONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class InventoryEntrys implements BaseColumns{

        public static final String TABLE_NAME = "inventory";
        public static final String PRODUCT_NAME = "name";
        public static final String PRODUCT_PRICE = "price";
        public static final String PRODUCT_QUANTITY = "quantity";
        public static final String PRODUCT_WEIGHT = "weight";

        public static  final Uri CONTENT_URI = Uri.withAppendedPath(BASECONTENT_URI , PATH_INVENTORY);


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of products.
         */

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**
         *  The MIME type of the {@link #CONTENT_URI} for a single product.
         */

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;


    }
}
