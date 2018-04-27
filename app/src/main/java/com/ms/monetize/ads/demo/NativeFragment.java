package com.ms.monetize.ads.demo;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.monetize.ads.Ad;
import com.ms.monetize.ads.AdError;
import com.ms.monetize.ads.AdListener;
import com.ms.monetize.ads.AdRequest;
import com.ms.monetize.ads.ImageDownloader;
import com.ms.monetize.ads.ImageFilter;
import com.ms.monetize.ads.NativeAd;
import com.ms.monetize.ads.NativeAdAssets;

public class NativeFragment extends Fragment {
  private static final String TAG = "NativeFragment";
  private NativeAd mNativeAd;
  private FrameLayout mContentContainer;
  private LinearLayout mTopContainer;
  private FrameLayout mAdMarkContainer;
  private Button mBtnLoad;
  private Button mBtnShow;
  private TextView mTvStatus;

  private LinearLayout mNativeAssetsContainer;
  private ImageView mIvIcon;
  private TextView mTvTitle;
  private TextView mTvRating;
  private ImageView mIvCover;
  private Button mBtnCTA;

  /**
   * You should use your own **PUB** in production
   */
  private String PUB = "";
  private java.util.List<String> pubs;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mNativeAd = new NativeAd(getActivity());
    mNativeAd.setAdListener(mAdListener);

    pubs = new java.util.ArrayList<>();
    pubs.add("ms2.1.0@native_admob");
    pubs.add("ms2.1.0@native_ping_start");
    pubs.add("ms2.1.0@native_batmobi");
    pubs.add("ms2.1.0@native_mobpower");

    initView();
    initAction();
  }

  private void initView() {
    android.widget.Spinner spinner = new android.widget.Spinner(getActivity());
    android.widget.ArrayAdapter<String> adapter =
            new android.widget.ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, pubs);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
        PUB = pubs.get(position);
        if (PUB == null || PUB.equals("")) {
          return;
        }

      }

      @Override
      public void onNothingSelected(android.widget.AdapterView<?> parent) {

      }
    });
    mBtnLoad = new Button(getActivity());
    mBtnLoad.setText(getString(R.string.load));

    mBtnShow = new Button(getActivity());
    mBtnShow.setText(getString(R.string.show));
    mBtnShow.setEnabled(false);

    mTvStatus = new TextView(getActivity());
    mTvStatus.setGravity(Gravity.CENTER);

    mTopContainer = new LinearLayout(getActivity());
    {
      mTopContainer.setOrientation(LinearLayout.VERTICAL);
      mTopContainer.addView(spinner);
      mTopContainer.addView(mBtnLoad);
      mTopContainer.addView(mBtnShow);
      mTopContainer.addView(mTvStatus);
    }

    mNativeAssetsContainer = new LinearLayout(getActivity());
    mNativeAssetsContainer.setOrientation(LinearLayout.VERTICAL);
    mNativeAssetsContainer.setVisibility(View.GONE);
    mNativeAssetsContainer.setBackgroundColor(getResources().getColor(R.color.bg_native_assets_container));
    {
      LinearLayout titleLine = new LinearLayout(getActivity());
      titleLine.setOrientation(LinearLayout.VERTICAL);
      mTvTitle = new TextView(getActivity());
      mTvTitle.setGravity(Gravity.CENTER);
      titleLine.addView(mTvTitle,
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                      LinearLayout.LayoutParams.WRAP_CONTENT));
      mTvRating = new TextView(getActivity());
      mTvRating.setGravity(Gravity.CENTER);
      titleLine.addView(mTvRating,
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                      LinearLayout.LayoutParams.WRAP_CONTENT));

      LinearLayout iconTitleLine = new LinearLayout(getActivity());
      iconTitleLine.setOrientation(LinearLayout.HORIZONTAL);
      iconTitleLine.setGravity(Gravity.CENTER);
      mIvIcon = new ImageView(getActivity());
      iconTitleLine.addView(mIvIcon,
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                          LinearLayout.LayoutParams.WRAP_CONTENT));
      iconTitleLine.addView(titleLine,
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                          LinearLayout.LayoutParams.WRAP_CONTENT,
                                                          1));
      mBtnCTA = new Button(getActivity());
      iconTitleLine.addView(mBtnCTA,
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                          LinearLayout.LayoutParams.WRAP_CONTENT));

      mNativeAssetsContainer.addView(iconTitleLine,
                                     new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                   LinearLayout.LayoutParams.WRAP_CONTENT));
      mIvCover = new ImageView(getActivity());
      mNativeAssetsContainer.addView(mIvCover,
                                     new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                   LinearLayout.LayoutParams.WRAP_CONTENT));
      mAdMarkContainer = new FrameLayout(getContext());
    }
  }

  private void initAction() {
    mBtnLoad.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mBtnLoad.setEnabled(false);
        mBtnShow.setEnabled(false);
        mTvStatus.setText(getString(R.string.ad_start_loading));

        AdRequest request = AdRequest.newBuilder().pid(PUB).testDeviceId("").build();
        mNativeAd.loadAd(request);
      }
    });

    mBtnShow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mBtnLoad.setEnabled(true);
        mBtnShow.setEnabled(false);

        NativeAdAssets assets = mNativeAd.getNativeAdAssets();
        if (assets == null) {
          // Usually this won't happen here.
          mTvStatus.setText(getString(R.string.ad_load_error, "Invalid native ad assets"));
          return;
        }
        /**
         * This is only an example for how to layout native ad assets.
         * Developer should follow their own design and handle all the unexpected situation,
         * such as no icon or cover.
         */
        // icon
        {
          NativeAdAssets.Image icon = assets.getIcon();
          BitmapDrawable fallback = new BitmapDrawable(getResources(),
                                                       BitmapFactory.decodeResource(getResources(),
                                                                                    R.drawable.icon_stub));
          if (icon != null && !TextUtils.isEmpty(icon.getUrl())) {
            ImageDownloader.downloadAndDisplayImage(icon, mIvIcon, fallback);
          } else {
            mIvIcon.setImageDrawable(fallback);
          }
        }
        // cover
        {
          if (assets.getCovers() != null) {
            for (NativeAdAssets.Image image : assets.getCovers()) {
            }
          }
          NativeAdAssets.Image cover = ImageFilter.filter(assets.getCovers(), 600, 314);
          if (cover == null) {
            cover = assets.getCover();
          }
          BitmapDrawable fallback = new BitmapDrawable(getResources(),
                                                       BitmapFactory.decodeResource(getResources(),
                                                                                    R.drawable.cover_stub));
          if (cover != null && !TextUtils.isEmpty(cover.getUrl())) {
            ImageDownloader.downloadAndDisplayImage(cover, mIvCover, fallback);
          } else {
            mIvCover.setImageDrawable(fallback);
          }
        }
        mTvTitle.setText(assets.getTitle());
        mTvRating.setText(getString(R.string.app_star, assets.getRating()));
        mBtnCTA.setText(assets.getCallToAction());
        mNativeAd.registerViewForInteraction(mNativeAssetsContainer,
                                             mBtnCTA,
                                             mTvTitle,
                                             mIvIcon,
                                             mIvCover);

        mNativeAssetsContainer.setVisibility(View.VISIBLE);
        mAdMarkContainer.removeAllViews();
        mAdMarkContainer.addView(mNativeAd.getNativeAdAssets().getAdMarkView());
      }
    });
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    mContentContainer = new FrameLayout(getActivity());
    mContentContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                 ViewGroup.LayoutParams.MATCH_PARENT));

    mContentContainer.addView(mTopContainer,
                              new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                                           FrameLayout.LayoutParams.WRAP_CONTENT,
                                                           Gravity.TOP));

    mContentContainer.addView(mNativeAssetsContainer,
                              new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                                           FrameLayout.LayoutParams.WRAP_CONTENT,
                                                           Gravity.BOTTOM));
    mContentContainer.addView(mAdMarkContainer,
                              new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                                           FrameLayout.LayoutParams.WRAP_CONTENT,
                                                           Gravity.BOTTOM | Gravity.RIGHT));
    return mContentContainer;
  }

  @Override
  public void onDestroyView() {
    mContentContainer.removeAllViews();
    mContentContainer = null;
    super.onDestroyView();
  }

  @Override
  public void onDestroy() {
    mTopContainer.removeAllViews();
    mTopContainer = null;
    mNativeAd = null;
    super.onDestroy();
  }

  private final AdListener mAdListener = new AdListener() {
    @Override
    public void onAdLoaded(Ad ad) {
      if (ad == mNativeAd) {
        mBtnShow.setEnabled(true);
        mTvStatus.setText(getString(R.string.ad_load_success));
      }
    }

    @Override
    public void onAdClosed(Ad ad) {
    }

    @Override
    public void onAdShowed(Ad ad) {
      if (ad == mNativeAd) {
        mTvStatus.setText(getString(R.string.ad_showed));
      }
    }

    @Override
    public void onAdClicked(Ad ad) {
      if (ad == mNativeAd) {
        Toast.makeText(getActivity(), getString(R.string.ad_clicked), Toast.LENGTH_SHORT).show();
      }
    }

    @Override
    public void onAdError(Ad ad, AdError adError) {
      if (ad == mNativeAd) {
        mBtnLoad.setEnabled(true);
        mBtnShow.setEnabled(false);
        mTvStatus.setText(getString(R.string.ad_load_error, adError.getErrorMessage()));
      }
    }
  };
}
