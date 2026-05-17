package com.bos.ads;

import android.app.Activity;
import android.content.Context;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.runtime.*;

import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

@DesignerComponent(
    version = 1,
    description = "Ekstensi Unity Ads Interstitial",
    iconName = "images/extension.png",
    nonVisible = true
)
@SimpleObject(external = true)
public class MyAds extends AndroidNonvisibleComponent {

    private Context context;
    private Activity activity;

    public MyAds(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
        this.activity = (Activity) context;
    }

    @SimpleFunction(description = "Mulai koneksi ke Unity Ads pakai Game ID")
    public void Initialize(String gameId, boolean testMode) {
        UnityAds.initialize(context, gameId, testMode, new IUnityAdsInitializationListener() {
            @Override
            public void onInitializationComplete() {
                InitializationSuccess();
            }

            @Override
            public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                InitializationFailed(message);
            }
        });
    }

    @SimpleEvent(description = "Event saat inisialisasi sukses")
    public void InitializationSuccess() {
        EventDispatcher.dispatchEvent(this, "InitializationSuccess");
    }

    @SimpleEvent(description = "Event saat inisialisasi gagal")
    public void InitializationFailed(String error) {
        EventDispatcher.dispatchEvent(this, "InitializationFailed", error);
    }

    @SimpleFunction(description = "Load iklan Interstitial")
    public void LoadInterstitial(String adUnitId) {
        UnityAds.load(adUnitId, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                AdLoaded(placementId);
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                AdFailedToLoad(placementId, message);
            }
        });
    }

    @SimpleEvent(description = "Event saat iklan berhasil disiapkan")
    public void AdLoaded(String placementId) {
        EventDispatcher.dispatchEvent(this, "AdLoaded", placementId);
    }

    @SimpleEvent(description = "Event saat iklan gagal disiapkan")
    public void AdFailedToLoad(String placementId, String error) {
        EventDispatcher.dispatchEvent(this, "AdFailedToLoad", placementId, error);
    }

    @SimpleFunction(description = "Tampilkan iklan Interstitial")
    public void ShowInterstitial(String adUnitId) {
        UnityAds.show(activity, adUnitId, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
            @Override
            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                AdFailedToShow(placementId, message);
            }
            @Override
            public void onUnityAdsShowStart(String placementId) {
                AdOpened(placementId);
            }
            @Override
            public void onUnityAdsShowClick(String placementId) {
                AdClicked(placementId);
            }
            @Override
            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                AdClosed(placementId);
            }
        });
    }

    @SimpleEvent(description = "Event saat iklan gagal tayang")
    public void AdFailedToShow(String placementId, String error) {
        EventDispatcher.dispatchEvent(this, "AdFailedToShow", placementId, error);
    }
    @SimpleEvent(description = "Event saat iklan terbuka")
    public void AdOpened(String placementId) {
        EventDispatcher.dispatchEvent(this, "AdOpened", placementId);
    }
    @SimpleEvent(description = "Event saat iklan diklik")
    public void AdClicked(String placementId) {
        EventDispatcher.dispatchEvent(this, "AdClicked", placementId);
    }
    @SimpleEvent(description = "Event saat iklan ditutup")
    public void AdClosed(String placementId) {
        EventDispatcher.dispatchEvent(this, "AdClosed", placementId);
    }
}
