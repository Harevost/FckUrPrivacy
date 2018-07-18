package com.vergermiya.harevost.fckurprivacy.InfoChecker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.vergermiya.harevost.fckurprivacy.Util.FileSaver;
import com.vergermiya.harevost.fckurprivacy.Util.JsonBuilder;

import org.json.JSONStringer;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import static com.vergermiya.harevost.fckurprivacy.Util.Base64Coder.file2Base64;

public class InfoChecker {

    public static InfoJson getPhoneInfo(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String mImei = telephonyManager.getDeviceId();
        String mImsi = telephonyManager.getSubscriberId();
        String mDeviceVersion = telephonyManager.getDeviceSoftwareVersion();
        String mIccid = telephonyManager.getSimSerialNumber();
        String mType = Build.MODEL;
        String mIpAddr = getIpAddress(context);
        String mNetMacAddr = getMacAddrFromIp(context);
        String mSerialNo = Build.SERIAL;
        String mBluetoothMac = getBlueToothAddr(context);

        InfoJson infoJson = new InfoJson(mImei, mImsi, mDeviceVersion, mIccid, mSerialNo, mType, mIpAddr, mNetMacAddr, mBluetoothMac);
        return infoJson;
    }

    public static String getIpAddress(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException se) {
                    se.printStackTrace();
                }
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddr = intIP2StrIP(wifiInfo.getIpAddress());
                return ipAddr;
            }
        }
        return null;
    }

    private static String intIP2StrIP(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
    }

    public static String getMacAddrFromIp(Context context) {
        String mMacAddr = null;
        StringBuilder builder = new StringBuilder();


        try {
            byte[] mac;
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getByName(getIpAddress(context)));
            mac = networkInterface.getHardwareAddress();
            if (mac == null) {
                return null;
            }

            for (byte b : mac) {
                builder.append(String.format("%02x", b) + ":");
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
            mMacAddr = builder.toString();
        } catch (SocketException se) {
            se.printStackTrace();
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }
        return mMacAddr.toUpperCase();
    }

    public static String getBlueToothAddr(Context context) {
        String mBlueToothMac = android.provider.Settings.Secure.getString(context.getContentResolver(), "bluetooth_address");
        return mBlueToothMac;
    }

    public static void saveInfoJson(Context context) {
        InfoJson infoJson = InfoChecker.getPhoneInfo(context);
        String infoJsonStr = JsonBuilder.buildInfoJson(infoJson).toString();
        String Imei = infoJson.getImei();

        Log.d("saveInfoJson", infoJsonStr);
        FileSaver fileSaver = new FileSaver();
        fileSaver.saveFile(Imei, ".json", infoJsonStr);
    }
}