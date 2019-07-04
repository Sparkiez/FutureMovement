package com.waleong.futuremovement.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import timber.log.Timber;

/**
 * A generic file writer.
 * Created by raymondleong on 03,July,2019
 */
public class FileWriterUtil {

    /**
     * @param context
     * @param data
     * @param fileName
     * @return path to the files that was written in if successful.
     * @throws IOException
     */
    public static String writeToFile(Context context, String data, String fileName) throws IOException {
        File path = context.getExternalFilesDir(null);
        File outputFile = new File(path, fileName);
        Timber.tag(FileWriterUtil.class.toString()).v("writeToFile() : attempting to write to path " + outputFile.getPath());

        FileOutputStream outputStreamWriter = new FileOutputStream(outputFile);
        outputStreamWriter.write(data.getBytes());
        outputStreamWriter.close();

        return outputFile.getPath();
    }
}
