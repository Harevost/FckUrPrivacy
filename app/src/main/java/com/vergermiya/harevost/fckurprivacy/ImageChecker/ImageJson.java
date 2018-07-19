package com.vergermiya.harevost.fckurprivacy.ImageChecker;

import java.io.File;

/**
 * Created by harevost on 18-7-19.
 */

public class ImageJson{
    private String path, name, title, addDate, modifyDate, latitude, longitude, size, image;

    public ImageJson(){
        this.path = "";
        this.name = "";
        this.title = "";
        this.addDate = "";
        this.modifyDate = "";
        this.latitude = "";
        this.longitude = "";
        this.size = "";
        this.image = "";
    }

    public ImageJson(String Path, String Name, String Title, String AddDate, String ModifyDate, String Latitude, String Longitude, String Size){
        this.path = Path;
        this.name = Name;
        this.addDate = AddDate;
        this.title = Title;
        this.modifyDate = ModifyDate;
        this.latitude = Latitude;
        this.longitude = Longitude;
        this.size = Size;
        this.image = "";
    }

    @Override
    public String toString(){
        return new String().format("ImageInfo {" + "path=%s, "+"name=%s, "+"addDate=%s, "+"modifyDate=%s, "+"latitude=%s, "+"longitude=%s, "+"size=%s, "+"image=%s}",
                path, name, addDate, title, modifyDate, latitude, longitude, size, image);
    }

    public String getPath() { return path; }
    public String getName() { return name; }
    public String getAddDate() { return addDate; }
    public String getTitle() { return title; }
    public String getModifyDate() { return modifyDate; }
    public String getLatitude() { return latitude; }
    public String getLongitude() { return longitude; }
    public String getSize() { return size; }
    public String getImage() { return image; }

    public void setImage(String imageData) { this.image = imageData; }

}
