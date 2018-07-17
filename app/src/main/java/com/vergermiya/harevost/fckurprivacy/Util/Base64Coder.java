package com.vergermiya.harevost.fckurprivacy.Util;

import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by harevost on 18-7-17.
 */

public class Base64Coder {

    public static void file2Base64(File file) {
        String base64Str;
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputStream.read(buffer);
            inputStream.close();
            base64Str = Base64.encodeToString(buffer, Base64.NO_WRAP);
            Log.e("Base64", file.getName() + ":" + base64Str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
