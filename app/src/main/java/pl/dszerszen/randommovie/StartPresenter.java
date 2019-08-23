package pl.dszerszen.randommovie;

import android.app.Activity;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import pl.dszerszen.randommovie.GSON.Genre;

public class StartPresenter implements StartInterface.Presenter, Serializable {

    final String TAG = "DAMIAN";

    private StartInterface.View view;
    private StartInterface.Model model;

    public StartPresenter(StartInterface.View view) {
        this.view = view;
        this.model = new ApiConnector(this);
        //getGenresList();
    }

    private void getGenresList() {
        Log.d(TAG, "getGenresList: called");
        model.getGenresList();
    }

    @Override
    public void reportError(String message) {
        Log.d(TAG, "reportError: called");
        view.showError(message);
    }

    @Override
    public void sendGenresList(List<Genre> list) {
        Log.d(TAG, "sendGenresList: called");
        view.populateGenresList(list);
    }

    @Override
    public void getRandomMovie() {
        model.getRandomMovie(ApiConnector.START);
    }

    @Override
    public void callbackRandomMovie(SingleMovieDetails movie) {
        view.showRandomMovie(movie);
    }
}
