package com.vergermiya.harevost.fckurprivacy.SmsChecker;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;

import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.Telephony;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

//import com.vergermiya.harevost.fckurprivacy.SmsCheckService;

/**
 * Created by harevost on 18-7-16.
 */

public class SmsObserver extends ContentObserver {

    private Context mContext;
    private Handler mHandler;

    private String SMS_RAW = "content://sms/raw";
    private String SMS_INBOX = "content://sms/inbox";
    private String SMS_SENT = "content://sms/sent";

    private String ALL_SMS = "ALL_SMS";
    private String LATEST_SMS = "LATEST_SMS";

    private int mRunCount = 0;

    public SmsObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        Uri inboxUri = Uri.parse(SMS_INBOX);
        Uri sentUri = Uri.parse(SMS_SENT);

        ArrayList<String> allInboxSms = new ArrayList<>();
        ArrayList<String> allSentSms = new ArrayList<>();
        ArrayList<String> latestInboxSms = new ArrayList<>();
        ArrayList<String> latestSentSms = new ArrayList<>();

        mRunCount += 1;

       if (mRunCount <= 1) {
           allInboxSms = getSmsFromDatabase(inboxUri, ALL_SMS);
           allSentSms = getSmsFromDatabase(sentUri, ALL_SMS);
       } else {
           latestInboxSms = getSmsFromDatabase(inboxUri, LATEST_SMS);
           latestSentSms = getSmsFromDatabase(sentUri, LATEST_SMS);
       }

       //TODO
    }

    public ArrayList<String> getSmsFromDatabase(Uri uri, String mode) {

        String latestSms;
        ArrayList<String> allSms = new ArrayList<>();

        Cursor c = mContext.getContentResolver().query(uri, null, null, null, "date desc");
        if (c != null && c.getCount() != 0) {
            if (mode.equals(LATEST_SMS)) {
                if (c.moveToFirst()) {
                    String mAddress = c.getString(c.getColumnIndex("address"));
                    String mBody = c.getString(c.getColumnIndex("body"));
                    long mDate = c.getLong(c.getColumnIndex("date"));
                    String mDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(mDate);

                    latestSms = mDateStr + "===" + mAddress + ":" + mBody;
                    allSms.add(latestSms);
                    return allSms;
                }
            } else if (mode.equals(ALL_SMS)) {
                c.moveToFirst();
                while(c != null) {
                    String mAddress = c.getString(c.getColumnIndex("address"));
                    String mBody = c.getString(c.getColumnIndex("body"));
                    long mDate = c.getLong(c.getColumnIndex("date"));
                    String mDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(mDate);

                    String mSms = mDateStr + "===" + mAddress + ":" + mBody;
                    allSms.add(mSms);

                    if (c.isLast()) {
                        break;
                    } else {
                        c.moveToNext();
                    }
                }
            }
        }
        c.close();
        return allSms;
    }
}
