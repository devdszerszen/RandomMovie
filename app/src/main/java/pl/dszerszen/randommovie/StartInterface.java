package pl.dszerszen.randommovie;

import java.util.List;

import pl.dszerszen.randommovie.GSON.Genre;

public interface StartInterface {

    interface View{
        void populateGenresList(List<Genre> genres);
        void showError(String message);
        void showRandomMovie(SingleMovieDetails movie);
    }

    interface Presenter {
        void reportError(String message);
        void sendGenresList(List<Genre> list);
        void getRandomMovie(int page, FilterData filter);
        void getMovieDetails(int id);
        void getGenresList();
        void callbackRandomMovie(SingleMovieDetails movie);
    }

    interface Model {
        void getGenresList();
        void getRandomMovie(int page, FilterData filter);
        void getMovieDetails(int id);
    }
}
