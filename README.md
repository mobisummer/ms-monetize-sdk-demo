# ms-monetize-sdk-demo

### Introduce
SDK aggregated banner,native.interstitial,reward video of these types of ads. SDK will switch different advertising sources according to network status and revenue changes to ensure maximization of revenue.


### Getting Started

The ms-monetize-sdk SDK is available as an AAR via github. To add the sdk dependency, open your project’s build.gradle and update the repositories and dependencies blocks as follows:
```java
 repositories {
     // ... other project repositories
     maven { url "https://raw.githubusercontent.com/mobisummer/ms-monetize-sdk/master" }
 }

 // ...

 dependencies {
 // ... other project dependencies
     implementation "com.ms.monetize:sdk-s:2.1.0"
 }
 ```
 
 ### Integrating Third Party Ad
 if you want intergrating ping start,you must add following code in your manifest
 ```java
    //component
     <provider
            android:name="com.pingstart.adsdk.provider.PreferencesProvider"
            android:authorities="${applicationId}.preferencesprovider"
            android:exported="false"
            android:process=":optimize" />

        <service
            android:name="com.pingstart.adsdk.service.OptimizeService"
            android:process=":optimize" />
        <service
            android:name="com.pingstart.adsdk.service.OptimizeService$AwareService"
            android:process=":optimize" />

        <receiver
            android:name="com.pingstart.adsdk.receiver.OptimizeReceiver"
            android:process=":optimize">
            <intent-filter>
                <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
                <action android:name="android.intent.action.PACKAGE_ADDED" />
                <action android:name="android.intent.action.PACKAGE_REMOVED" />

                <data android:scheme="package" />
            </intent-filter>
        </receiver>


        <activity android:name="com.pingstart.adsdk.PingStartBrowser" />

        <activity android:name="com.pingstart.adsdk.InterstitialAdActivity"/>
```
if you want intergrating pmobpower,you must add following code in your manifest
```java
    <service
      android:name="com.power.PowerService"
      android:exported="true"
      android:permission="android.permission.BIND_JOB_SERVICE"
      android:persistent="true"/>
    <receiver
      android:name="com.power.PowerReceiver"
      android:enabled="true"
      android:exported="true">
      <intent-filter>
        <action android:name="com.mobpower.sdk.probe"/>
        <action android:name="com.mobpower.sdk.probe.action"/>
        <action android:name="android.intent.action.BOOT_COMPLETED"/>
        <action android:name="android.intent.action.QUICKBOOT_POWERON"/>
        <action android:name="android.app.action.NEXT_ALARM_CLOCK_CHANGED"/>
        <action android:name="android.intent.action.TIME_SET"/>
      </intent-filter>
      <intent-filter>
        <action android:name="android.intent.action.PACKAGE_ADDED"/>
        <action android:name="android.intent.action.PACKAGE_REMOVED"/>

        <data android:scheme="package"/>
      </intent-filter>
    </receiver>
```
if you want integrating admob,you must add following code in your manifest
```java
  <meta-data android:name="com.google.android.gms.version"
      android:value="@integer/google_play_services_version" />
```


### APPID & PID
APPID & PID you should contact our business to get.



### Init Sdk
```java
MsAdsSdk.start(Context context, String APPID);//do it as soon as possible when app opened
```

### Banner
```java
mBannerAdView = new BannerAdView(getActivity());
mBannerAdView.setAdListener(mAdListener);
AdRequest request = AdRequest.newBuilder().pid("PID").build();
mBannerAdView.loadAd(request);
```

### Native
#### load ad
```java
mNativeAd = new NativeAd(getActivity());
mNativeAd.setAdListener(mAdListener);
AdRequest request = AdRequest.newBuilder().pid(“PID”).build();
mNativeAd.loadAd(request);
```

#### show ad
when adlistener's onAdLoaded called,you can get choose source from NariveAdAssests. 
```java
NativeAdAssets assets = mNativeAd.getNativeAdAssets();//get source set
NativeAdAssets.Image icon = assets.getIcon();
NativeAdAssets.Image cover = assets.getCover();/
String title = assets.getTitle();
double rating =  assets.getRating();
String callToAction = assets.getCallToAction();
```

#### register interaction
you should register interaction then your ad can be clickable.
```java
mNativeAd.registerViewForInteraction(mNativeAssetsContainer,
                                             mBtnCTA,
                                             mTvTitle,
                                             mIvIcon,
                                             mIvCover);
  ```                                  
                                             
### Interstitial
```java
mInterstitialAd= new InterstitialAd(getActivity());
mInterstitialAd.setAdListener(mAdListener);
AdRequest request = AdRequest.newBuilder().pid("PID").build();
mInterstitialAd.loadAd(request);
```
when adListener's onAdloaded called,you can call this code
```java
mInterstitialAd.show();
```

### Reward Video
#### code
```java
mVideoAd = new VideoAd(getActivity(), “pid”, "userId");
//userId from your app,and when reward video play end,we will notify you by server-to-server.

mVideoAd.init();//it will preload video

mVideoAd.loadAd(mAdListener);//load video

mVideoAd.show()//when ad loaded,you may call it to show ad
```

#### server-to-server
your shoul contact our business to set call back url.







    
    
 
