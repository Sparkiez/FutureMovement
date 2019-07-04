package com.waleong.futuremovement.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by raymondleong on 03,July,2019
 */
public class FileReader {

    public interface OnLineParsedListener {
        public void onLineParsed(String line);
    }

    public static String readFile(String fileName) {
        return readFile(fileName, null);
    }

    public static String readFile(String fileName, OnLineParsedListener listener) {
        InputStream inputStream = FileReader.class.getClassLoader().getResourceAsStream(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputStreamReader);
        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            String line;

            while ((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');

                if (listener != null) {
                    listener.onLineParsed(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return text.toString();
    }
}
