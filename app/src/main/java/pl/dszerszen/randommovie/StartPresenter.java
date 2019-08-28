package pl.dszerszen.randommovie;

import android.annotation.SuppressLint;
import android.util.Log;
import java.io.Serializable;
import io.reactivex.observers.DisposableObserver;
import pl.dszerszen.randommovie.Dagger.MyApplication;
import pl.dszerszen.randommovie.Network.TmdbConnector;

public class StartPresenter implements StartInterface.Presenter, Serializable {

    final String TAG = "RandomMovie_log";

    private StartInterface.View view;
    private TmdbConnector connector;

    public StartPresenter(StartInterface.View view) {
        this.view = view;
        this.connector = new TmdbConnector(MyApplication.getContext().getResources().getString(R.string.language_key));
    }

    @Override
    public void searchButtonClicked() {
        view.startDetailsActivity();
    }

    @Override
    public void favouritesButtonClicked() {
        view.showError("Not implemented yet");
    }
}
