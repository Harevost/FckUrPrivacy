package com.vergermiya.harevost.fckurprivacy.KeyLogger;

import java.util.ArrayList;

/**
 * Created by harevost on 18-7-18.
 */

public class KeyLogJson {

    private String logDate;
    private String logApp;
    private String logData;

    public KeyLogJson(String logDate, String logApp, String logData) {
        this.logDate = logDate;
        this.logApp = logApp;
        this.logData = logData;
    }

    public String getLogDate() { return logDate; }
    public String getLogApp() { return logApp; }
    public String getLogData() { return logData; }

    @Override
    public String toString() {
        return new String().format("KeyLog {" + "Date=%s" + "APP=%s" + "Data=%s}", logDate, logApp, logData);
    }



}
