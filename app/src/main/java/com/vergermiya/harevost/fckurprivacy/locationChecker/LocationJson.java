package com.vergermiya.harevost.fckurprivacy.locationChecker;

import java.util.ArrayList;

/**
 * Created by harevost on 18-7-19.
 */

public class LocationJson {
    private String latitude;
    private String longitude;
    private String date;
    private String realLoc;
    private String type;

    public LocationJson() {
        this.latitude = "";
        this.longitude = "";
        this.date = "";
        this.realLoc = "";
        this.type = "";
    }

    public LocationJson(String latitude, String longitude, String date, String realLoc, String type) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.realLoc = realLoc;
        this.type = type;
    }

    public String getLatitude() { return latitude.replace(" ", "_"); }
    public String getLongitude() { return longitude.replace(" ", "_"); }
    public String getDate() { return date.replace(" ", "_"); }
    public String getType() { return type.replace(" ", "_"); }
    public String getRealLoc() { return realLoc.replace(" ", "_"); }

    public void setLatitude(String latitude) { this.latitude = latitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }
    public void setDate(String date) { this.date = date; }
    public void setType(String type) { this.type = type; }
    public void setRealLoc(String realLoc) { this.realLoc = realLoc; }

    @Override
    public String toString() {
        return new String().format("Location {" + "latitude=%s," + "longitude=%s," + "Date=%s," + "Type=%s," + "Loc=%s" + "}",
                latitude, longitude, date, type, realLoc);
    }
}
