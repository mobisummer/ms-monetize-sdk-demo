package com.ms.monetize.ads.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.monetize.ads.Ad;
import com.ms.monetize.ads.AdError;
import com.ms.monetize.ads.AdListener;
import com.ms.monetize.ads.AdRequest;
import com.ms.monetize.ads.BannerAdView;


public class BannerFragment extends Fragment {
  private BannerAdView mBannerAdView;
  private FrameLayout mContentContainer;
  private LinearLayout mTopContainer;
  private Button mBtnLoad;
  private TextView mTvStatus;

  /**
   * You should use your own **PUB** in production
   */
  private String PUB = "";
  private java.util.List<String> pubs;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mBannerAdView = new BannerAdView(getActivity());
    mBannerAdView.setAdListener(mAdListener);

    pubs = new java.util.ArrayList<>();
    pubs.add("ms2.1.0@banner_admob");
    pubs.add("ms2.1.0@banner_facebook");
    pubs.add("ms2.1.0@banner_adtiming");
    pubs.add("ms2.1.0@banner_ping_start");
    pubs.add("ms2.1.0@banner_batmobi");
    pubs.add("ms2.1.0@banner_mobpower");

    initView();
    initAction();
  }

  private void initView() {
    android.widget.Spinner spinner = new android.widget.Spinner(getActivity());
    android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(getActivity(),
                                                                                    android.R.layout.simple_spinner_item,
                                                                                    pubs);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(android.widget.AdapterView<?> parent,
                                 View view,
                                 int position,
                                 long id) {
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

    mTvStatus = new TextView(getActivity());
    mTvStatus.setGravity(Gravity.CENTER);

    mTopContainer = new LinearLayout(getActivity());
    {
      mTopContainer.setOrientation(LinearLayout.VERTICAL);
      mTopContainer.addView(spinner);

      mTopContainer.addView(mBtnLoad);
      mTopContainer.addView(mTvStatus);
    }
  }

  private void initAction() {
    mBtnLoad.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mBtnLoad.setEnabled(false);
        mTvStatus.setText(getString(R.string.ad_start_loading));
        AdRequest request = AdRequest.newBuilder().pid(PUB).build();
        mBannerAdView.loadAd(request);
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

    mContentContainer.addView(mBannerAdView,
                              new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                                           FrameLayout.LayoutParams.WRAP_CONTENT,
                                                           Gravity.BOTTOM));

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
    mBannerAdView = null;
    super.onDestroy();
  }

  private final AdListener mAdListener = new AdListener() {
    @Override
    public void onAdLoaded(Ad ad) {
      if (ad == mBannerAdView) {
        mTvStatus.setText(getString(R.string.ad_load_success));
      }
    }

    @Override
    public void onAdClosed(Ad ad) {

    }

    @Override
    public void onAdShowed(Ad ad) {
      if (ad == mBannerAdView) {
        mBtnLoad.setEnabled(true);
        mTvStatus.setText(getString(R.string.ad_showed));
      }
    }

    @Override
    public void onAdClicked(Ad ad) {
      if (ad == mBannerAdView) {
        Toast.makeText(getActivity(), getString(R.string.ad_clicked), Toast.LENGTH_SHORT).show();
      }
    }

    @Override
    public void onAdError(Ad ad, AdError adError) {
      if (ad == mBannerAdView) {
        mBtnLoad.setEnabled(true);
        mTvStatus.setText(getString(R.string.ad_load_error, adError.getErrorMessage()));
      }
    }
  };
}
