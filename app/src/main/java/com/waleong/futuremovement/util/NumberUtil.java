package com.waleong.futuremovement.util;

/**
 * A util to parse string into an Integer object.
 * Created by raymondleong on 03,July,2019
 */
public class NumberUtil {
    public static Integer parseInteger(String numberText) {
        try {
            return Integer.parseInt(numberText);
        } catch (Exception e) {
        }

        return null;
    }
}
