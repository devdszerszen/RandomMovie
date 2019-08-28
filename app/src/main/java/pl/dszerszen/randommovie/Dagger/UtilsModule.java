package pl.dszerszen.randommovie.Dagger;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.dszerszen.randommovie.R;

@Module
public class UtilsModule {

    @Provides @Singleton
    Context provideContext() {
        return MyApplication.getContext();
    }
}
