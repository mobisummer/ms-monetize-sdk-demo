package com.ms.monetize.ads.demo;

import android.app.Application;
import android.widget.Toast;

import com.ms.monetize.ads.AdError;
import com.ms.monetize.ads.MsAdsSdk;
import com.ms.monetize.ads.StartupListener;


public class MsAdsDemoApp extends Application {
  private final static String TAG = MsAdsDemoApp.class.getSimpleName();

  @Override
  public void onCreate() {
    super.onCreate();

    MsAdsSdk.start(this, "ms2.1.0@monetize", new StartupListener() {
      @Override
      public void onSuccess() {
        Toast.makeText(MsAdsDemoApp.this,"success",Toast.LENGTH_LONG).show();
      }

      @Override
      public void onFailed(AdError adError) {
        Toast.makeText(MsAdsDemoApp.this,"failed",Toast.LENGTH_LONG).show();
      }
    });
  }


}
