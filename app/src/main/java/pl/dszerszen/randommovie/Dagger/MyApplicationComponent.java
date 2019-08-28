package pl.dszerszen.randommovie.Dagger;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import pl.dszerszen.randommovie.MovieDetailsPresenter;

@Singleton
@Component (modules = {AndroidInjectionModule.class,MyApplicationModule.class, NetworkModule.class, UtilsModule.class})
public interface MyApplicationComponent extends AndroidInjector<MyApplication> {
    void inject(MyApplication application);


}
