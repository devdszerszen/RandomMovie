package pl.dszerszen.randommovie.Firebase;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface FirebaseDBInterface {
    void addUser(GoogleSignInAccount account);
    void incrementCounter();
    void addMovie(FirebaseStoredMovie movie);
    Completable deleteMovie(int id);
    Observable getMovies();
    Single<ArrayList<Integer>> getFavouriteMoviesIds();
}
