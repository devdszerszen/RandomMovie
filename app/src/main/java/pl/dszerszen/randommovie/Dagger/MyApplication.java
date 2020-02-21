package pl.dszerszen.randommovie.Dagger;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import pl.dszerszen.randommovie.Firebase.RemoteConfig;
import pl.dszerszen.randommovie.R;

public class MyApplication extends Application implements HasActivityInjector {

    private static Context context;
    private static RemoteConfig remoteConfig;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerMyApplicationComponent.create().inject(this);
        MyApplication.context = getApplicationContext();
        initRemoteConfig();
    }

    private void initRemoteConfig() {
        remoteConfig = new RemoteConfig();
    }

    public static Context getContext() {
        return context;
    }

    public static RemoteConfig getRemoteConfig() {return remoteConfig; }

    public static String getGoogleAdsId() {return context.getString(R.string.google_ads_id);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
