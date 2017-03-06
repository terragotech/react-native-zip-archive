package com.rnziparchive;

import com.facebook.react.bridge.Promise;

import android.content.res.AssetFileDescriptor;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.json.JSONObject;

import java.io.File;


public class RNZipArchiveModule extends ReactContextBaseJavaModule {

    public RNZipArchiveModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNZipArchive";
    }

    @ReactMethod
    public void unzip(String zipFilePath, String destDirectory,Promise promise) {
        JSONObject response = Zip4jArchive.unzip(new File(zipFilePath), destDirectory, null);
        try {
            boolean isSuccess = response.getBoolean("isSuccess");

            if (isSuccess) {
                promise.resolve("Success");
            } else {
                String message = response.getString("response");
                promise.reject("unzip error"+message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject("unzip error"+e);
        }
    }
    @ReactMethod
    public void unzipWithPassword(String zipFilePath, String destDirectory, String password, Promise promise) {
        JSONObject response = Zip4jArchive.unzip(new File(zipFilePath), destDirectory, password);
        System.out.println("zipPath is "+zipFilePath+"   "+password);
        try {
            boolean isSuccess = response.getBoolean("isSuccess");

            if (isSuccess) {
                promise.resolve("Success");
            } else {
                String message = response.getString("response");
                promise.reject("unzip error"+message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject("unzip error"+e);
        }
    }

    @ReactMethod
    public void unzipAssets(String assetsPath, String destDirectory, Promise promise) {

    }

    //zip,unip,zipwith password,unzip with password.
    @ReactMethod
    public void zip(String fileOrDirectory, String destDirectory, Promise promise) {
        JSONObject response = Zip4jArchive.zip(fileOrDirectory, destDirectory, null);
        try {
            boolean isSuccess = response.getBoolean("isSuccess");
            String message = response.getString("response");
            promise.resolve(makePayloadResponse(isSuccess, message));
        } catch (Exception e) {
            promise.reject(null, "Couldn't open file " + fileOrDirectory + ". ");
            e.printStackTrace();
        }
    }
    @ReactMethod
    public void zipWithPassword(String fileOrDirectory, String destDirectory, String password, Promise promise) {
        JSONObject response = Zip4jArchive.zip(fileOrDirectory, destDirectory, password);
        try {
            boolean isSuccess = response.getBoolean("isSuccess");
            String message = response.getString("response");
            promise.resolve(makePayloadResponse(isSuccess, message));
        } catch (Exception e) {
            promise.reject(null, "Couldn't open file " + fileOrDirectory + ". ");
            e.printStackTrace();
        }
    }

    private void zipStream(String[] files, String destFile, long totalSize, Callback completionCallback) {
        try {

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private WritableMap makeErrorPayload(String message, Exception ex) {
        WritableMap error = Arguments.createMap();
        error.putString("message", String.format("%s (%s)", message, ex.getMessage()));
        return error;
    }

    private WritableMap makePayloadResponse(boolean success, String message) {
        WritableMap error = Arguments.createMap();
        error.putString("message", message);
        error.putBoolean("success", success);
        return error;
    }

    private WritableMap makeErrorPayloadFromMessage(String message, String ex) {
        WritableMap error = Arguments.createMap();
        error.putString("message", String.format("%s (%s)", message, ex));
        return error;
    }
}