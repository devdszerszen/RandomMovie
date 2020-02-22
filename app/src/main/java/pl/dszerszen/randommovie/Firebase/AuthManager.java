package pl.dszerszen.randommovie.Firebase;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.dszerszen.randommovie.SharPrefs.SharPrefsManager;

public class AuthManager implements FirebaseAuthInterface {

    public static final String TAG = "RandomMovie_log";
    private Context context;


    private GoogleSignInClient googleSignClient;
    private GoogleSignInAccount googleSignAccount;
    private FirebaseAuth firebaseAuth;
    private SharPrefsManager sharPrefsManager;

    //Singleton
    private static AuthManager authManagerInstance = null;
    public static AuthManager getInstance(Context context) {
        if (authManagerInstance == null) {
            authManagerInstance = new AuthManager(context);
        }
        return authManagerInstance;
    }

    public AuthManager(Context context) {
        GoogleSignInOptions googleSignOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("35181507042-g5h0rn8kv5bvc9s6v2ncdeet6thi0dsp.apps.googleusercontent.com")
                .requestEmail()
                .build();

        this.googleSignClient = GoogleSignIn.getClient(context, googleSignOptions);


        this.googleSignAccount = GoogleSignIn.getLastSignedInAccount(context);


        this.firebaseAuth = FirebaseAuth.getInstance();

        this.sharPrefsManager = SharPrefsManager.getSharPrefsInstance();
    }

    @Override
    public GoogleSignInAccount getLoggedAccount() {
        return googleSignAccount;
    }

    @Override
    public Intent getSignInIntent() {
        return googleSignClient.getSignInIntent();
    }

    @Override
    public boolean isUserSignedToGoogle() {
        return googleSignAccount!=null;
    }

    @Override
    public boolean isUserSignedToFirebase() {
        return firebaseAuth.getCurrentUser() != null;
    }

    @Override
    public Completable loginToFirebase(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        return Completable.create(source -> {
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(command -> {
                if (command.isSuccessful()) {
                    saveUserUid();
                    source.onComplete();
                } else {
                    source.onError(new Throwable("Firebase login error: " + command.getException().getMessage()));
                }

            });
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private void saveUserUid() {
        String uid = firebaseAuth.getCurrentUser().getUid();
        sharPrefsManager.setFirebaseKey(uid);
    }

    @Override
    public void updateGoogleAccount(GoogleSignInAccount account) {
        this.googleSignAccount = account;
    }
}

