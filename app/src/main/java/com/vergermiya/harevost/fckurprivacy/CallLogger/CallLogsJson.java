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

    public String getPhoneNumber() { return phoneNumber.replace(" ", "_"); }
    public String getCallDate() { return callDate.replace(" ", "_"); }
    public String getCallType() { return callType.replace(" ", "_"); }
    public String getCallName() { return callName.replace(" ", "_"); }

    @Override
    public String toString() {
        return new String().format("CallLog {" +
            "Name=%s," + "Date=%s," + "Type=%s," + "PhoneNumber=%s}", callName, callDate, callType, phoneNumber);

    }
}
