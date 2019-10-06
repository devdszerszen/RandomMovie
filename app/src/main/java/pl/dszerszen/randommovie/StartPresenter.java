package pl.dszerszen.randommovie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import pl.dszerszen.randommovie.Dagger.MyApplication;
import pl.dszerszen.randommovie.Firebase.AuthManager;
import pl.dszerszen.randommovie.Firebase.FirebaseAuthInterface;
import pl.dszerszen.randommovie.Network.TmdbConnector;

public class StartPresenter implements StartInterface.Presenter, Serializable {

    final String TAG = "RandomMovie_log";

    private StartInterface.View view;
    private TmdbConnector connector;
    private FirebaseAuthInterface firebaseAuth;

    public StartPresenter(StartInterface.View view) {
        this.view = view;
        this.connector = new TmdbConnector(MyApplication.getContext().getResources().getString(R.string.language_key));
        this.firebaseAuth = AuthManager.getInstance((Context)view);
    }

    @Override
    public void searchButtonClicked() {
        view.startDetailsActivity();
    }

    @Override
    public void favouritesButtonClicked() {
        loginToFirebase();
    }

    private void loginToFirebase() {
        if (firebaseAuth.isUserSignedToGoogle() && firebaseAuth.isUserSignedToFirebase()) {
            Log.d(TAG, "loginToFirebase: User is signed correctly");
        } else {
            view.showLoginPrompt(firebaseAuth.getSignInIntent());
            Log.d(TAG, "favouritesButtonClicked: User not signed");
        }
    }


    @Override
    public void loginToFirebaseWithSelectedGoogleAccount(Intent data) {
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d(TAG, "loginToFirebaseWithSelectedGoogleAccount: Logged successful on: " + account.getEmail());
            loginToFirebase(account);
        } catch (ApiException e) {
            Log.d(TAG, "loginToFirebaseWithSelectedGoogleAccount: Error:" + e.getMessage());
        }
    }

    @SuppressLint("CheckResult")
    private void loginToFirebase(GoogleSignInAccount account) {
        firebaseAuth.loginToFirebase(account).subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: Firebase login successful");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: Firebase login error: " + e.getMessage());
            }
        });
    }
}
