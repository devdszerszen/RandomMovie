package pl.dszerszen.randommovie;

import android.util.Log;
import java.io.Serializable;
import java.util.List;
import pl.dszerszen.randommovie.GSON.Genre;

public class StartPresenter implements StartInterface.Presenter, Serializable {

    final String TAG = "DAMIAN";

    private StartInterface.View view;
    private StartInterface.Model model;

    public StartPresenter(StartInterface.View view, String languageKey) {
        this.view = view;
        this.model = new ApiConnector(this, languageKey);
    }

    @Override
    public void getGenresList() {
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
    public void getRandomMovie(int page, FilterData filter) {
        model.getRandomMovie(page,filter);
    }

    @Override
    public void getMovieDetails(int id) {
        model.getMovieDetails(id);
    }

    @Override
    public void callbackRandomMovie(SingleMovieDetails movie) {
        view.showRandomMovie(movie);
    }
}
