package pl.dszerszen.randommovie.Base;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import pl.dszerszen.randommovie.CustomViews.LoadingView;
import pl.dszerszen.randommovie.R;

public abstract class BaseActivity extends AppCompatActivity {
    final String TAG = "RandomMovie_log";
    Dialog loaderDialog;

    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showFullscreenLoaderDialog() {
        loaderDialog = new Dialog(this);
        loaderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loaderDialog.getWindow().setDimAmount(0.9f);
        LoadingView loader = (LoadingView) LayoutInflater.from(this).inflate(R.layout.loader_for_dialog, null);
        loaderDialog.setContentView(loader);
        loader.startLoader();
        loaderDialog.setCancelable(false);
        loaderDialog.show();
    }

    public void hideFullScreenLoaderDialog() {
        if (loaderDialog != null) {
            loaderDialog.dismiss();
        }
    }
}

