package com.vergermiya.harevost.fckurprivacy.CallLogger;

/**
 * Created by harevost on 18-7-16.
 */

public class CallLogsJson {

    private String callName;
    private String phoneNumber;
    private String callDate;
    private String callType;

    public CallLogsJson(String callName, String phoneNumber, String callDate, String callType) {
        this.callName = callName;
        this.phoneNumber = phoneNumber;
        this.callDate = callDate;
        this.callType = callType;
    }

    public String getPhoneNumber() { return phoneNumber; }
    public String getCallDate() { return callDate; }
    public String getCallType() { return callType; }
    public String getCallName() { return callName; }

    @Override
    public String toString() {
        return new String().format("CallLog {" +
            "Name=%s," + "Date=%s," + "Type=%s," + "PhoneNumber=%s}", callName, callDate, callType, phoneNumber);

    }
}
