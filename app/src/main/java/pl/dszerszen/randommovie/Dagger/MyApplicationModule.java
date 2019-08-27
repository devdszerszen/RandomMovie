package pl.dszerszen.randommovie.Dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import pl.dszerszen.randommovie.StartActivity;

@Module
public abstract class MyApplicationModule {

    @ContributesAndroidInjector
    abstract StartActivity contributeActivityInjector();
}
