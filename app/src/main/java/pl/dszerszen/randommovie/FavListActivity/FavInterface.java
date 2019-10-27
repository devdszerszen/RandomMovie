package pl.dszerszen.randommovie.FavListActivity;

import java.util.ArrayList;

import pl.dszerszen.randommovie.Firebase.FirebaseStoredMovie;

public interface FavInterface {
    interface View {
        void showLoader();
        void hideLoader();
        void showMoviesList(ArrayList<FirebaseStoredMovie> moviesList);
    }

    interface Presenter {
        void getList();
    }
}
