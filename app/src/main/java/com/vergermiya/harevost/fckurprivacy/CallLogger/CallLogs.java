package com.vergermiya.harevost.fckurprivacy.CallLogger;

/**
 * Created by harevost on 18-7-16.
 */

public class CallLogs {

    private String callName;
    private String phoneNumber;
    private String callDate;
    private String callType;

    public CallLogs() {
        this.callName = "";
        this.phoneNumber = "";
        this.callDate = "";
        this.callType = "";
    }


    public CallLogs(String callName, String phoneNumber, String callDate, String callType) {
        this.callName = callName;
        this.phoneNumber = phoneNumber;
        this.callDate = callDate;
        this.callType = callType;
    }

    @Override
    public String toString() {
        return "CallLog {" +
                "callName=" + callName + "," +
                "phoneNumber=" + phoneNumber + "," +
                "callDate=" + callDate + "," +
                "callType=" + callType + "}";
    }
}
