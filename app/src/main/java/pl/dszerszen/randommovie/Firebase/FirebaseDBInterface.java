package pl.dszerszen.randommovie.Firebase;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import io.reactivex.Observable;

public interface FirebaseDBInterface {
    void addUser(GoogleSignInAccount account);
    void incrementCounter();
    void addMovie(FirebaseStoredMovie movie);
    Observable getMovies();
}
