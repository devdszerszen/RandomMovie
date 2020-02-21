package pl.dszerszen.randommovie.Base;

import pl.dszerszen.randommovie.Dagger.MyApplication;
import pl.dszerszen.randommovie.Firebase.RemoteConfig;

public class BasePresenter {
    public final String TAG = "RandomMovie_log";

    public boolean isAdModuleActive() {
        return MyApplication.getRemoteConfig().getValue(RemoteConfig.ALLOW_ADS).equals("true");
    }

    public String getGoogleAdsId() {
        return MyApplication.getGoogleAdsId();
    }
}
