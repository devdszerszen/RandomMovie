package pl.dszerszen.randommovie.Activity.InfoActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import pl.dszerszen.randommovie.Base.BaseActivity;
import pl.dszerszen.randommovie.R;

public class InfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        if (getVersionName() != null) {
            TextView versionView = findViewById(R.id.versionNameView);
            versionView.setText("Version: " + getVersionName());
        }

    }

    public String getVersionName () {
        PackageInfo pinfo = null;
        String tmp = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return tmp;
        }
        tmp = pinfo.versionName;
        return tmp;
    }

    public void sendMessage(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+getResources().getString(R.string.devMail)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        startActivity(emailIntent);
    }
}
