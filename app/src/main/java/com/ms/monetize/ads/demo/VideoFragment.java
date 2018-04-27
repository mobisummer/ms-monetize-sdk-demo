package com.ms.monetize.ads.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.monetize.ads.Ad;
import com.ms.monetize.ads.AdError;
import com.ms.monetize.ads.AdListener;
import com.ms.monetize.ads.VideoAd;

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment {
  private VideoAd mVideoAd;
  private FrameLayout mContentContainer;
  private LinearLayout mTopContainer;
  private Button mBtnLoad;
  private Button mBtnShow;
  private TextView mTvStatus;
  /**
   * You should use your own **PUB** in production
   */
  private List<String> pubs;
  private String PUB = "";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    pubs = new ArrayList<>();
    pubs.add("ms2.0@rewardVideo");

    initView();
    initAction();
  }

  private void initView() {
    mBtnLoad = new Button(getActivity());
    mBtnLoad.setText(getString(R.string.load));

    mBtnShow = new Button(getActivity());
    mBtnShow.setText(getString(R.string.show));
    mBtnShow.setEnabled(false);

    mTvStatus = new TextView(getActivity());
    mTvStatus.setGravity(Gravity.CENTER);

    Spinner spinner = new Spinner(getActivity());
    ArrayAdapter<String> adapter =
        new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, pubs);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        PUB = pubs.get(position);
        if (PUB == null || PUB.equals("")) {
          return;
        }
        mVideoAd = new VideoAd(getActivity(), PUB, "12345");
        mVideoAd.init();

        mBtnLoad.setEnabled(true);
        mBtnShow.setEnabled(false);

        mTvStatus.setText("");
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    mTopContainer = new LinearLayout(getActivity());
    {
      mTopContainer.setOrientation(LinearLayout.VERTICAL);
      mTopContainer.addView(spinner);
      mTopContainer.addView(mBtnLoad);
      mTopContainer.addView(mBtnShow);
      mTopContainer.addView(mTvStatus);
    }
  }

  private void initAction() {
    mBtnLoad.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (PUB == null || PUB.equals("")) {
          return;
        }
        mBtnLoad.setEnabled(false);
        mBtnShow.setEnabled(false);
        mTvStatus.setText(getString(R.string.ad_start_loading));

        mVideoAd.loadAd(mAdListener);
      }
    });

    mBtnShow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mBtnLoad.setEnabled(true);
        mBtnShow.setEnabled(false);

        mVideoAd.show();
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
    mVideoAd = null;
    super.onDestroy();
  }

  private final AdListener mAdListener = new AdListener() {
    @Override
    public void onAdLoaded(Ad ad) {
      if (ad == mVideoAd) {
        mBtnShow.setEnabled(true);
        mTvStatus.setText(getString(R.string.ad_load_success));
        mBtnShow.setVisibility(View.VISIBLE);
      }
    }

    @Override
    public void onAdClosed(Ad ad) {
      if (ad == mVideoAd) {
        mTvStatus.setText(getString(R.string.ad_closed));
      }
    }

    @Override
    public void onAdShowed(Ad ad) {
      if (ad == mVideoAd) {
        mTvStatus.setText(getString(R.string.ad_showed));
      }
    }

    @Override
    public void onAdClicked(Ad ad) {
      if (ad == mVideoAd) {
        Toast.makeText(getActivity(), getString(R.string.ad_clicked), Toast.LENGTH_SHORT).show();
      }
    }

    @Override
    public void onAdError(Ad ad, AdError adError) {
      if (ad == mVideoAd) {
        mBtnLoad.setEnabled(true);
        mBtnShow.setEnabled(false);

        mTvStatus.setText(getString(R.string.ad_load_error, adError.getErrorMessage()));
      }
    }
  };
}
