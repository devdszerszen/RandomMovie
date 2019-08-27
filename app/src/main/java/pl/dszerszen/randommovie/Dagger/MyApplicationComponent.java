package pl.dszerszen.randommovie.Dagger;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Component (modules = {AndroidInjectionModule.class,MyApplicationModule.class})
public interface MyApplicationComponent extends AndroidInjector<MyApplication> {
    void inject(MyApplication application);
}
