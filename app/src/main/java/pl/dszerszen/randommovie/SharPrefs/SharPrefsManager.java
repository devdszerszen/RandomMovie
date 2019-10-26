package pl.dszerszen.randommovie.SharPrefs;

import android.content.Context;
import android.content.SharedPreferences;

import pl.dszerszen.randommovie.Dagger.MyApplication;

public class SharPrefsManager {

    final static String TAG = "RandomMovie_log";
    final static String SHARPREFS_NAME = "RandomMovie_sharPrefs";

    final static String FIREBASE_KEY_PATH = "FIREBASE_KEY";

    private SharedPreferences preferences;

    //Singleton
    private static SharPrefsManager sharPrefsInstance = null;
    public static SharPrefsManager getSharPrefsManager() {
        if (sharPrefsInstance == null) {
            sharPrefsInstance = new SharPrefsManager();
        }
        return sharPrefsInstance;
    }

    public SharPrefsManager() {
        preferences = MyApplication.getContext().getSharedPreferences(SHARPREFS_NAME,Context.MODE_PRIVATE);
    }

    public void setFirebaseKey(String key) {
        preferences.edit().putString(FIREBASE_KEY_PATH,key).apply();
    }

    public String getFirebaseKey() {
        return preferences.getString(FIREBASE_KEY_PATH,null);
    }
}
