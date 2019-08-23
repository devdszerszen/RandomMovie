//package pl.dszerszen.randommovie;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import javax.inject.Inject;
//import javax.inject.Singleton;
//
//import dagger.Module;
//import dagger.Provides;
//
//@Module
//public class MyAppModule {
//    @Provides
//    @Singleton
//    public Context provideContext(MyApplication application) {
//        return application;
//    }
//
//    @Provides @Singleton @Inject
//    public SharedPreferences sharPrefs (Context context) {
//        return context.getSharedPreferences("NAME",Context.MODE_PRIVATE);
//    }
//}
