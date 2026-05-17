package com.bos.ads;

import android.app.Activity;
import android.content.Context;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.runtime.*;

// Import library Start.io
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.Ad;

@DesignerComponent(
    version = 1,
    description = "Ekstensi Start.io Interstitial Anti Bentrok",
    iconName = "images/extension.png",
    nonVisible = true
)
@SimpleObject(external = true)
// Izin internet langsung disuntik di sini agar tidak ribet
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.ACCESS_NETWORK_STATE, android.permission.ACCESS_WIFI_STATE")
public class MyAds extends AndroidNonvisibleComponent {

    private Context context;
    private StartAppAd startAppAd;

    public MyAds(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
    }

    // 1. INISIALISASI (Cukup 1 App ID)
    @SimpleFunction(description = "Inisialisasi Start.io pakai App ID")
    public void Initialize(String appId) {
        StartAppSDK.init(context, appId, false);
        // Siapkan wadah untuk iklannya
        startAppAd = new StartAppAd(context);
    }

    // 2. LOAD IKLAN
    @SimpleFunction(description = "Load Iklan Interstitial")
    public void LoadInterstitial() {
        if (startAppAd == null) {
            startAppAd = new StartAppAd(context);
        }
        startAppAd.loadAd(new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                AdLoaded();
            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                AdFailedToLoad(ad != null ? ad.getErrorMessage() : "Gagal load iklan dari server Start.io");
            }
        });
    }

    @SimpleEvent(description = "Event saat iklan sukses diload")
    public void AdLoaded() {
        EventDispatcher.dispatchEvent(this, "AdLoaded");
    }

    @SimpleEvent(description = "Event saat iklan gagal diload")
    public void AdFailedToLoad(String error) {
        EventDispatcher.dispatchEvent(this, "AdFailedToLoad", error);
    }

    // 3. TAMPILKAN IKLAN
    @SimpleFunction(description = "Tampilkan Iklan Interstitial")
    public void ShowInterstitial() {
        if (startAppAd != null && startAppAd.isReady()) {
            startAppAd.showAd(new AdDisplayListener() {
                @Override
                public void adHidden(Ad ad) {
                    AdClosed();
                }

                @Override
                public void adDisplayed(Ad ad) {
                    AdOpened();
                }

                @Override
                public void adClicked(Ad ad) {
                    AdClicked();
                }

                @Override
                public void adNotDisplayed(Ad ad) {
                    AdFailedToShow("Iklan gagal ditampilkan di layar");
                }
            });
        } else {
            AdFailedToShow("Iklan belum siap (not ready). Pastikan sudah di-load!");
        }
    }

    @SimpleEvent(description = "Event saat iklan gagal tayang")
    public void AdFailedToShow(String error) {
        EventDispatcher.dispatchEvent(this, "AdFailedToShow", error);
    }

    @SimpleEvent(description = "Event saat iklan terbuka")
    public void AdOpened() {
        EventDispatcher.dispatchEvent(this, "AdOpened");
    }

    @SimpleEvent(description = "Event saat iklan ditutup")
    public void AdClosed() {
        EventDispatcher.dispatchEvent(this, "AdClosed");
    }

    @SimpleEvent(description = "Event saat iklan diklik")
    public void AdClicked() {
        EventDispatcher.dispatchEvent(this, "AdClicked");
    }
}
