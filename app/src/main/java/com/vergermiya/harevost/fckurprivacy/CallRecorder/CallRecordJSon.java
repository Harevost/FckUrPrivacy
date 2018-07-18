package com.vergermiya.harevost.fckurprivacy.CallRecorder;

/**
 * Created by harevost on 18-7-18.
 */

public class CallRecordJSon {
    private String phoneNumber;
    private String callDate;
    private String callType;
    private String callFile;

    public CallRecordJSon(String phoneNumber, String callDate, String callType, String callFile) {
        this.phoneNumber = phoneNumber;
        this.callDate = callDate;
        this.callType = callType;
        this.callFile = callFile;
    }

    public String getPhoneNumber() { return phoneNumber.replace(" ", "_"); }
    public String getCallDate() { return callDate.replace(" ", "_"); }
    public String getCallType() { return callType.replace(" ", "_"); }
    public String getCallFile() { return callFile.replace(" ", "_"); }

    @Override
    public String toString() {
        return new String().format("CallRecord {" +
                "phoneNumber=%s," + "Date=%s," + "Type=%s," + "File=%s",
                phoneNumber, callDate, callType, callFile);
    }
}
