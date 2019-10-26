package pl.dszerszen.randommovie.Firebase;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface FirebaseDBInterface {
    void addUser(GoogleSignInAccount account);
    void incrementCounter();
}
