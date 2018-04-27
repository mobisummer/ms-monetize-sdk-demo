package com.ms.monetize.ads.demo;

import android.app.Application;

import com.ms.monetize.ads.MsAdsSdk;


public class MsAdsDemoApp extends Application {
  private final static String TAG = MsAdsDemoApp.class.getSimpleName();

  @Override
  public void onCreate() {
    super.onCreate();

    MsAdsSdk.start(this, "ms2.1.0@monetize");
  }


}
