package com.ms.monetize.ads.demo;

import android.app.Application;
import android.util.Log;

import com.ms.monetize.ads.AdError;
import com.ms.monetize.ads.MsAdsSdk;
import com.ms.monetize.ads.StartupListener;


public class MsAdsDemoApp extends Application {
  private final static String TAG = MsAdsDemoApp.class.getSimpleName();

  @Override
  public void onCreate() {
    MsAdsSdk.start(this, "ms2.1.0@monetize", new StartupListener() {
      @Override
      public void onSuccess() {
        Log.i(TAG, "onSuccess: ");
      }

      @Override
      public void onFailed(AdError adError) {
        Log.e(TAG, "onFailed: " + adError.getErrorMessage());
      }
    });
  }


}
