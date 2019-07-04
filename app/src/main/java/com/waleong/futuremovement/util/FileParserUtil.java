package com.waleong.futuremovement.util;

import android.content.Context;
import android.os.Environment;
import android.widget.TextView;

import com.waleong.futuremovement.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import timber.log.Timber;

/**
 * A generic file reader.
 * Created by raymondleong on 03,July,2019
 */
public class FileParserUtil {

    public interface OnLineParsedListener {
        public void onLineParsed(String line);
    }

    public static String readFile(Context context, int rawResId) throws IOException {
        return readFile(context, rawResId, null);
    }

    public static String readFile(Context context, int rawResId, OnLineParsedListener listener) throws IOException {

        InputStream inputStream = context.getResources().openRawResource(rawResId); // getting XML
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputStreamReader);
        //Read text from file
        StringBuilder text = new StringBuilder();

        String line;

        while ((line = buffreader.readLine()) != null) {
            text.append(line);
            text.append('\n');


            if (listener != null) {
                listener.onLineParsed(line);
            }
        }

        return text.toString();
    }
}
