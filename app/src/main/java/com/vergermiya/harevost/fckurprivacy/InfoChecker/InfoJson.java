package com.vergermiya.harevost.fckurprivacy.InfoChecker;

/**
 * Created by harevost on 18-7-17.
 */

public class InfoJson {
    private String Imei;
    private String Imsi;
    private String DeviceVersion;
    private String Iccid;
    private String SerialNo;
    private String DeviceType;
    private String Ip;
    private String NetworkMac;
    private String BluetoothMac;

    public InfoJson(String imei, String imsi, String devVer, String iccid, String serialNo, String devType, String ip, String netMac, String btMac) {
        super();
        this.Imei = imei;
        this.Imsi = imsi;
        this.DeviceVersion = devVer;
        this.Iccid = iccid;
        this.SerialNo = serialNo;
        this.DeviceType = devType;
        this.Ip = ip == null ? "" : ip;
        this.NetworkMac = netMac == null ? "" : netMac;
        this.BluetoothMac = btMac == null ? "" : btMac;
    }

    public String getImei() { return Imei.replace(" ", "_"); }
    public String getImsi() { return Imsi.replace(" ", "_"); }
    public String getDeviceVersion() { return DeviceVersion.replace(" ", "_"); }
    public String getIccid() { return Iccid.replace(" ", "_"); }
    public String getSerialNo() { return SerialNo.replace(" ", "_"); }
    public String getDeviceType() { return DeviceType.replace(" ", "_"); }
    public String getIp() { return Ip.replace(" ", "_"); }
    public String getNetworkMac() { return NetworkMac.replace(" ", "_"); }
    public String getBluetoothMac() { return BluetoothMac.replace(" ", "_"); }

    @Override
    public String toString() {
        return new String().format("BaseInfo {" +
                        "IMEI=%s," + "IMSI=%s," + "DevVer=%s," +
                        "ICCID=%s," + "SerialNo=%s," + "DevType=%s," +
                        "IP=%s," + "NetMac=%s," + "BtMac=%s" +"}"
                , Imei, Imsi, DeviceVersion, Iccid, SerialNo, DeviceType, Ip, NetworkMac, BluetoothMac);
    }
}
