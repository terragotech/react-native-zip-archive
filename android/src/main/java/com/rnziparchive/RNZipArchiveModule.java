package com.rnziparchive;

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
  public void unzip(String zipFilePath, String destDirectory, String password, Callback callback) {
    JSONObject response = Zip4jArchive.unzip(new File(zipFilePath), destDirectory, password);
    try{
      boolean isSuccess = response.getBoolean("isSuccess");

      if(isSuccess) {
        callback.invoke(null, null);
      }else {
        String message = response.getString("response");
        callback.invoke(makeErrorPayloadFromMessage("Couldn't open file - ", message));
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  @ReactMethod
  public void unzipAssets(String assetsPath, String destDirectory, Callback completionCallback) {
  }

  @ReactMethod
  public void zip(String fileOrDirectory, String destDirectory,  String password,Callback callback) {
    JSONObject response = Zip4jArchive.zip(fileOrDirectory, destDirectory, password);
    try{
      boolean isSuccess = response.getBoolean("isSuccess");
      String message = response.getString("response");
      callback.invoke(makePayloadResponse(isSuccess, message));
    }catch (Exception e){
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

  private WritableMap makePayloadResponse(boolean success,String message){
    WritableMap error = Arguments.createMap();
    error.putString("message", message);
    error.putBoolean("success",success);
    return error;
  }

  private WritableMap makeErrorPayloadFromMessage(String message, String ex) {
    WritableMap error = Arguments.createMap();
    error.putString("message", String.format("%s (%s)", message, ex));
    return error;
  }
}