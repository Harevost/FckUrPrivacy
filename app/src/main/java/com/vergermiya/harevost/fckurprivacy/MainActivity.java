package com.vergermiya.harevost.fckurprivacy;

import android.Manifest;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vergermiya.harevost.fckurprivacy.CallLogger.CallLogger;
import com.vergermiya.harevost.fckurprivacy.CallLogger.CallLogs;
import com.vergermiya.harevost.fckurprivacy.CallRecorder.CallRecord;
import com.vergermiya.harevost.fckurprivacy.InfoChecker.InfoChecker;
import com.vergermiya.harevost.fckurprivacy.PermissionsChecker.PermissionsActivity;
import com.vergermiya.harevost.fckurprivacy.PermissionsChecker.PermissionsChecker;
import com.vergermiya.harevost.fckurprivacy.SmsChecker.SmsCheckService;
import com.vergermiya.harevost.fckurprivacy.SmsChecker.SmsInfo;
import com.vergermiya.harevost.fckurprivacy.SmsChecker.SmsObserver;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0; // 请求码

    private Button getInfoButton;
    private TextView gottenInfoText;
    private Button getCallLogsButton;
    private Button deleteCallLogsButton;
    private Button getSmsButton;
    private Button delSmsButton;

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
    }

    private void initEvent() {
        getInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneInfo = InfoChecker.getPhoneInfo(getApplicationContext());
                gottenInfoText.setText(phoneInfo);
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

        getCallLogsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallLogs callLogs = CallLogger.getLatestCallLog(MainActivity.this);
                gottenInfoText.setText(callLogs.toString());
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
                SmsInfo smsInfo = SmsObserver.getLatestSmsInfo(MainActivity.this, SmsObserver.SMS_INBOX);
                gottenInfoText.setText(smsInfo.toString());
            }
        });

        delSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsObserver.deleteLatestSms(getApplicationContext(), SmsObserver.SMS_SENT);
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
    }

}
