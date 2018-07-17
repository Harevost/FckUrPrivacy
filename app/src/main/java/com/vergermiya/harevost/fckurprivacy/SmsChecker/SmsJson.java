package com.vergermiya.harevost.fckurprivacy.SmsChecker;

/**
 * Created by harevost on 18-7-17.
 */

public class SmsJson {
    private String PhoneNumber;
    private String ThreadId;
    private String Date;
    private String Type;
    private String Body;

    public SmsJson() {
        this.PhoneNumber = "";
        this.ThreadId = "";
        this.Date = "";
        this.Type = "";
        this.Body = "";
    }

    public SmsJson(String phoneNumber, String threadId, String date, String type, String body) {
        this.PhoneNumber = phoneNumber;
        this.ThreadId = threadId;
        this.Date = date;
        this.Type = type;
        this.Body = body;
    }

    public String getPhoneNumber() { return PhoneNumber; }
    public String getThreadId() { return ThreadId; }
    public String getDate() { return Date; }
    public String getType() { return Type; }
    public String getBody() { return Body; }

    @Override
    public String toString() {
        return new String().format("SmsInfo {" + "PhoneNumber=%s," + "ThreadId=%s," + "Date=%s," + "Type=%s," + "Body=%s" + "}",
                PhoneNumber, ThreadId, Date, Type, Body);
    }
}