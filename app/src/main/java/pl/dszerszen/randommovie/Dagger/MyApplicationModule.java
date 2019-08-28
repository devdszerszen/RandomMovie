package pl.dszerszen.randommovie.Dagger;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.dszerszen.randommovie.StartActivity;

@Module
public abstract class MyApplicationModule {

    @ContributesAndroidInjector
    abstract StartActivity contributeActivityInjector();


}
