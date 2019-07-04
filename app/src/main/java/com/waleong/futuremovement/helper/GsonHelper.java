package com.waleong.futuremovement.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

import timber.log.Timber;

/**
 * A helper to help us serialise/deserialise gson data.
 * Created by raymondleong on 03,July,2019
 */
public class GsonHelper {

    private static Gson mGson;
    private static final String TAG = "GsonHelper";

    public static Gson getInstance() {
        if (mGson == null) {
            mGson = new GsonBuilder().setLenient().enableComplexMapKeySerialization().serializeNulls().create();
        }

        return mGson;
    }

    public static <T> T parse(String json, Type typeOfT) {
        try {
            return GsonHelper.getInstance().fromJson(json, typeOfT);
        } catch (Exception e) {
            Timber.tag(TAG).v("parse() : with exception as " + e.getMessage());
        }

        return null;
    }
}

