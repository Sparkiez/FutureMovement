package com.waleong.futuremovement.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by raymondleong on 03,July,2019
 */
public class ColumnDefinition {

    @SerializedName("name")
    private String mName;
    @SerializedName("colStart")
    private int mColumnStart;
    @SerializedName("colEnd")
    private int mColumnEnd;

    public static final String KEY_CLIENT_TYPE = "CLIENT TYPE";
    public static final String KEY_CLIENT_NUMBER = "CLIENT NUMBER";
    public static final String KEY_ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
    public static final String KEY_SUBACCOUNT_NUMBER = "SUBACCOUNT NUMBER";
    public static final String KEY_PRODUCT_GROUP_CODE = "PRODUCT GROUP CODE";
    public static final String KEY_EXCHANGE_CODE = "EXCHANGE CODE";
    public static final String KEY_SYMBOL = "SYMBOL";
    public static final String KEY_EXPIRATION_DATE = "EXPIRATION DATE";
    public static final String KEY_QUANTITY_LONG = "QUANTITY LONG";
    public static final String KEY_QUANTITY_SHORT = "QUANTITY SHORT";

    public String getName() {
        return mName;
    }

    public int getColumnStart() {
        return mColumnStart;
    }

    public int getColumnEnd() {
        return mColumnEnd;
    }
}
