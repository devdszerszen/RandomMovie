package pl.dszerszen.randommovie.Firebase;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface FirebaseDBInterface {
    void addUser(GoogleSignInAccount account);
    void incrementCounter();
    void addMovie(FirebaseStoredMovie movie);
    Completable deleteMovie(int id);
    Observable getMovies();
}
