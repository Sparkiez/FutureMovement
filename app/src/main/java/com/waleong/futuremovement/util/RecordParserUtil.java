package com.waleong.futuremovement.util;

import android.text.TextUtils;

import com.waleong.futuremovement.model.ColumnDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A parser to parse a transactional record into a key pair, based on the column key, and its value.
 * Created by raymondleong on 03,July,2019
 */
public class RecordParserUtil {

    /**
     * @param columnDefinitions
     * @param line
     * @return a mapping of the column values to its respective column key.
     */
    public static Map<String, String> parse(List<ColumnDefinition> columnDefinitions, String line) {
        Map<String, String> keyPair = new HashMap<>();

        if (TextUtils.isEmpty(line)) {
            return keyPair;
        }

        for (ColumnDefinition columnDefinition : columnDefinitions) {
            String key = columnDefinition.getName();
            int colStart = columnDefinition.getColumnStart() - 1;
            int colEnd = columnDefinition.getColumnEnd();

            if (colStart < 0 || colEnd > line.length()) {
                continue;
            }

            String value = line.substring(colStart, colEnd);
            keyPair.put(key, value);
        }

        return keyPair;
    }
}
