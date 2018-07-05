# ms-monetize-sdk-demo

### Introduce
SDK aggregated banner,native,interstitial,reward video of these types of ads. SDK will switch different advertising sources according to network status and revenue changes to ensure maximization of revenue.


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
 // MUST add this dependency
  implementation 'com.google.code.gson:gson:2.8.5'
  implementation 'com.ms.small:small:0.0.9'
  implementation 'com.ms.monetize:sdk:3.0.0'
 }
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
your should contact our business to set call back url .







    
    
 
