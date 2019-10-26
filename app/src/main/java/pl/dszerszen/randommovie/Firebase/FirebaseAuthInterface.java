package pl.dszerszen.randommovie.Firebase;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import io.reactivex.Completable;

public interface FirebaseAuthInterface {
    GoogleSignInAccount getLoggedAccount();
    Intent getSignInIntent();
    boolean isUserSignedToGoogle();
    boolean isUserSignedToFirebase();
    Completable loginToFirebase(GoogleSignInAccount account);
    void updateGoogleAccount(GoogleSignInAccount account);
}
