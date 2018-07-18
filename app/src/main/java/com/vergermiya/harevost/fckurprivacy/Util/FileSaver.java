package com.vergermiya.harevost.fckurprivacy.Util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by harevost on 18-7-18.
 */

public class FileSaver {

    public File getPublicStorageDir(String dirName) {
        File file = new File(Environment.getExternalStorageDirectory(), dirName);
        if (!file.mkdirs()) {
            Log.d("FileSaver", "Directory not created");
        }
        return file;
    }

    public void saveFile(String fileName, String fileType, String fileData) {
        File fileDir = getPublicStorageDir("cryptoa");
        try {
            File file = File.createTempFile(fileName, fileType, fileDir);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(fileData.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
