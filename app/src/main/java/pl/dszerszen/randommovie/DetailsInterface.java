package pl.dszerszen.randommovie;

public interface DetailsInterface {

    interface View {
        void showNewMovie(SingleMovieDetails movie);
        void reportError(String message);
    }

    interface Model {
        void getRandomMovie(String presenterType);
    }

    interface Presenter {
        void changeMovie();
        void reportError(String message);
        void callbackRandomMovie(SingleMovieDetails movieDetails);
    }
}
