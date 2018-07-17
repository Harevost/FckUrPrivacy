package com.vergermiya.harevost.fckurprivacy.SmsChecker;

/**
 * Created by harevost on 18-7-17.
 */

public class SmsInfo {

    private String phoneNumber;
    private int smsThreadId;
    private String smsDate;
    private String smsType;
    private String smsBody;

    public SmsInfo() {
        this.phoneNumber = "";
        this.smsThreadId = 0;
        this.smsDate = "";
        this.smsType = "";
        this.smsBody = "";
    }


    public SmsInfo(String phoneNumber, int smsThreadId, String smsDate, String smsType, String smsBody) {
        this.phoneNumber = phoneNumber;
        this.smsThreadId = smsThreadId;
        this.smsDate = smsDate;
        this.smsType = smsType;
        this.smsBody = smsBody;
    }

    @Override
    public String toString() {
        return "SmsInfo {" +
                "phoneNumber=" + phoneNumber + "," +
                "smsThreadId=" + smsThreadId + "," +
                "smsDate=" + smsDate + "," +
                "smsType=" + smsType + "," +
                "smsBody=" + smsBody + "}";
    }
}
