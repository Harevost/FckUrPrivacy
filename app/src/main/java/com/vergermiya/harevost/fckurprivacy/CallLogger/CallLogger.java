package com.vergermiya.harevost.fckurprivacy.CallLogger;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by harevost on 18-7-16.
 */

public class CallLogger {

    public static ArrayList<CallLogsJson> queryCallLogsJson(Context context) {
        ArrayList<CallLogsJson> CallLogsJson = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] mProjection = new String[]{
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE
        };

        Cursor c = contentResolver.query(uri, mProjection, null, null, "date desc");
        while (c.moveToNext()) {
            String callName = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
            if (callName == null) { callName = "Unknown Number"; }

            String phoneNumber = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));

            long callDate = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String callDateStr = dateFormat.format(callDate);

            int callType = c.getInt(c.getColumnIndex(CallLog.Calls.TYPE));
            String[] callTypeStr = {"Incoming", "Outgoing", "Missing"};

            CallLogsJson mCallLogsJson = new CallLogsJson(callName, phoneNumber, callDateStr, callTypeStr[callType - 1]);
            CallLogsJson.add(mCallLogsJson);
            Log.d("CallLogsJson", mCallLogsJson.toString());
        }
        c.close();
        return CallLogsJson;
    }

    public static void deleteLatestCallLog(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] mProjection = new String[]{
                CallLog.Calls._ID,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE
        };

        Cursor c = contentResolver.query(uri, mProjection, null, null, "date desc");
        if (c != null) {
            if (c.moveToFirst()) {
                String callId = c.getString(c.getColumnIndex(CallLog.Calls._ID));
                contentResolver.delete(uri, "_id=?", new String[]{callId + ""});
                Log.d("CallLogsJson", callId + ":" + c.getString(c.getColumnIndex(CallLog.Calls.NUMBER)));
            }
        }
        c.close();
    }

    public static CallLogsJson getLatestCallLog(Context context) {
        CallLogsJson mCallLogsJson = new CallLogsJson("", "", "", "");
        ArrayList<CallLogsJson> CallLogsJson = queryCallLogsJson(context);
        if (!CallLogsJson.isEmpty()) {
            mCallLogsJson = CallLogsJson.get(0);
        }
        return mCallLogsJson;
    }

}
