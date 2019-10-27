package pl.dszerszen.randommovie.Dagger;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.dszerszen.randommovie.Activity.StartActivity.StartActivity;

@Module
public abstract class MyApplicationModule {

    @ContributesAndroidInjector
    abstract StartActivity contributeActivityInjector();


}
