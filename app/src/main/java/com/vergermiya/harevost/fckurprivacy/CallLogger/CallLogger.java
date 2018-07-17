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

    public static ArrayList<CallLogs> queryCallLogs(Context context) {
        ArrayList<CallLogs> callLogs = new ArrayList<>();
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
            String[] callTypeStr = {"来电", "去电", "未接"};

            CallLogs mCallLogs = new CallLogs(callName, phoneNumber, callDateStr, callTypeStr[callType - 1]);
            callLogs.add(mCallLogs);
            Log.d("callLogs", mCallLogs.toString());
        }
        c.close();
        return callLogs;
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
                Log.d("CallLogs", callId + ":" + c.getString(c.getColumnIndex(CallLog.Calls.NUMBER)));
            }
        }
        c.close();
    }

    public static CallLogs getLatestCallLog(Context context) {
        CallLogs mCallLogs = new CallLogs();
        ArrayList<CallLogs> callLogs = queryCallLogs(context);
        if (!callLogs.isEmpty()) {
            mCallLogs = callLogs.get(0);
        }
        return mCallLogs;
    }

}
