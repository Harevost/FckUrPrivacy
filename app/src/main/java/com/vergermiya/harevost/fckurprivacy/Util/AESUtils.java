package com.vergermiya.harevost.fckurprivacy.Util;

import android.provider.Settings;

import java.security.SecureRandom;
 
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
 

public class AESUtils {
    private final static String HEX = "0123456789ABCDEF";
    private static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    private static final String AES = "AES";//AES ����
    private static final String SHA1PRNG = "SHA1PRNG";
    public static final String AES_KEY = "2B916EFA26AF0BF8F773EC173D9E837DDA94FC14";

    public static String generateKey() {
        try {
            SecureRandom localSecureRandom = SecureRandom.getInstance(SHA1PRNG);
            byte[] bytes_key = new byte[20];
            localSecureRandom.nextBytes(bytes_key);
            String str_key = toHex(bytes_key);
            return str_key;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(AES);
        //for android
        SecureRandom sr = null;
        sr = SecureRandom.getInstance(SHA1PRNG, "Crypto");
        // for Java
        //sr = SecureRandom.getInstance(SHA1PRNG);
        sr.setSeed(seed);
        kgen.init(128, sr); //256 bits or 128 bits,192bits
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    public static String encrypt(String key, String cleartext) {
        if ("".equals(cleartext)) {
            return cleartext;
        }
        try {
            byte[] result = encrypt(key, cleartext.getBytes());
            return Base64Encoder.encode(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 
    /*
    * ����
    */
    private static byte[] encrypt(String key, byte[] clear) throws Exception {
        byte[] raw = getRawKey(key.getBytes());
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    public static String decrypt(String key, String encrypted) {
        if ("".equals(encrypted)) {
            return encrypted;
        }
        try {
            byte[] enc = Base64Decoder.decodeToBytes(encrypted);
            byte[] result = decrypt(key, enc);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] decrypt(String key, byte[] encrypted) throws Exception {
        byte[] raw = getRawKey(key.getBytes());
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }
 
    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
    
    public void dealonefile(String filename) {
    	
    }
    
    public static String filetoStr(String filePath) {
 	   rwfile file = new rwfile();
 	   String result = file.readFile(filePath);
 	   return result;
    }
    
    public static void strtoFile(String filePath, String fileContent) {
 	   rwfile file = new rwfile();
 	   file.writeFile(filePath, fileContent);
    }


    /*public static void main(String[] args) {

    	String pathname1 = "D:\\xxq\\AllSms-1527689662.json";
    	String[] sArray = pathname1.split("\\.") ; 
    	String pathname2 = sArray[0]+"_encode."+sArray[1];
    	String pathname3 = "D:\\xxq\\35436007029032-1776191319_decode.json";
    	String jsonData = filetoStr(pathname1);
        System.out.println("AESUtils jsonData ---->" + jsonData);
        System.out.println("AESUtils jsonData length ---->" + jsonData.length());
 
        //����key
        String secretKey = "2B916EFA26AF0BF8F773EC173D9E837DDA94FC14";
        System.out.println("AESUtils secretKey ---->" + secretKey);
 

        String encryStr = AESUtils.encrypt(secretKey, jsonData);
        System.out.println("AESUtils jsonData(encrypted) ---->" + encryStr);
        System.out.println("AESUtils jsonData(encrypted) length ---->" + encryStr.length());
        strtoFile(pathname2, encryStr);
 
        //AES����
        String decryStr = AESUtils.decrypt(secretKey, encryStr);
        System.out.println("AESUtils jsonData(decrypted) ---->" + decryStr);
        strtoFile(pathname3, decryStr);
        
    }*/
}
