package pl.dszerszen.randommovie.Firebase;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class RemoteConfig {

    public static String ERROR_CODE = "Remote config error";
    public static String ALLOW_ADS = "allow_ads";

    private FirebaseRemoteConfig remoteConfig;
    private boolean isReady = false;

    public RemoteConfig() {
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(600)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.fetchAndActivate().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                isReady = true;
            } else {
                //TODO
            }
        });
    }

    public String getValue(String key) {
        if (isReady) {
            return remoteConfig.getValue(key).asString();
        } else return ERROR_CODE;
    }
}
