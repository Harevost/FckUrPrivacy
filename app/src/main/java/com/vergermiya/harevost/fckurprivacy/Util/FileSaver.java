package com.vergermiya.harevost.fckurprivacy.Util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

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

            OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter(new FileOutputStream(file), "gbk");
            outputStreamWriter1.append(fileData);
            outputStreamWriter1.close();

            String encodeData = AESUtils.encrypt(AESUtils.AES_KEY, AESUtils.filetoStr(file.getPath()));

            File encodeFile = File.createTempFile(fileName, ".encode", fileDir);
            OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(new FileOutputStream(encodeFile), "gbk");
            outputStreamWriter2.append(encodeData);
            outputStreamWriter2.close();

            Log.d("AES", fileName + ":" + encodeData);
            Log.d("AES", fileName + ":" + AESUtils.decrypt(AESUtils.AES_KEY, AESUtils.filetoStr(encodeFile.getPath())));



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
