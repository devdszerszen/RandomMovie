//package pl.dszerszen.randommovie;
//
//import android.app.Activity;
//import android.app.Application;
//
//import javax.inject.Inject;
//
//import dagger.android.AndroidInjector;
//import dagger.android.DispatchingAndroidInjector;
//import dagger.android.HasActivityInjector;
//
//public class MyApplication extends Application implements HasActivityInjector {
//
//    @Inject
//    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
//
//    @Override
//    public AndroidInjector<Activity> activityInjector() {
//        return dispatchingAndroidInjector;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        DaggerMyComponent.builder()
//                .application(this)
//                .build()
//                .inject(this);
//    }
//}
