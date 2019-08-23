//package pl.dszerszen.randommovie;
//
//import android.app.Application;
//
//import javax.inject.Inject;
//import javax.inject.Singleton;
//
//import dagger.BindsInstance;
//import dagger.Component;
//import dagger.Provides;
//
//
//
//@Singleton @Component(modules = {MyAppModule.class})
//public interface MyComponent {
//
//    @Component.Builder
//    interface Builder {
//
//        @BindsInstance
//        Builder application(Application application);
//        MyComponent build();
//    }
//
//    void inject(MyApplication application);
//}