package pl.dszerszen.randommovie.Firebase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.dszerszen.randommovie.SharPrefs.SharPrefsManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

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
        Log.d(TAG, "AuthManager: GoogleSignClient: " + googleSignClient);

        this.googleSignAccount = GoogleSignIn.getLastSignedInAccount(context);
        Log.d(TAG, "AuthManager: GoogleSignAccount: " + googleSignAccount);

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
        Log.d(TAG, "AuthManager: firebaseLogin called: " + account.getIdToken());

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






//
//
//
//
//
//
//        // OLD <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//
//
//        //Sign options
//
//
//
//
//        //Firebase auth
//        firebaseAuth = FirebaseAuth.getSharPrefsInstance();
//    }
//
//
//
//    }
//
//    public void signIn(Intent data) throws ApiException {
//        Log.d(TAG, "AuthManager: signIn method called");
//        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//        googleLogin(task);
//    }
//
//
//    public boolean isUserLogged() {
//        return isUserAuthorizedInFirebase() && isUserLoggedWithGoogleAccount();
//    }
//
//    private boolean isUserLoggedWithGoogleAccount() {
//        if (googleSignAccount != null) {
//            Log.d(TAG, "AuthManager: isUserLoggedWithGoogleAccount method returned: TRUE");
//            return true;
//        }
//        Log.d(TAG, "AuthManager: isUserLoggedWithGoogleAccount method returned: FALSE");
//        return false;
//    }
//
//    private boolean isUserAuthorizedInFirebase() {
//        if (firebaseAuth.getCurrentUser() != null) {
//            Log.d(TAG, "AuthManager: isUserAuthorizedInFirebase method returned: TRUE");
//            return true;
//        }
//        Log.d(TAG, "AuthManager: isUserAuthorizedInFirebase method returned: FALSE");
//        return false;
//    }
//
//    private void googleLogin(Task<GoogleSignInAccount> completedTask) throws ApiException {
//        Log.d(TAG, "AuthManager: googleLogin called");
//        try {
//            this.googleSignAccount = completedTask.getResult(ApiException.class);
//
//            //Used to add user to firebase auth
//            if (googleSignAccount != null)
//                firebaseLogin(googleSignAccount);
//
//
//            // Signed in successfully
//            Log.d(TAG, "AuthManager: Signed successfully on Google Account: " + googleSignAccount);
//            //user.setGoogleAccount(this.googleSignAccount);
//
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            Log.d(TAG, "AuthManager: signInResult:failed code=" + e.getLocalizedMessage());
//            throw e;
//        }
//    }
//
//    private void firebaseLogin(GoogleSignInAccount account) {
//        Log.d(TAG, "AuthManager: firebaseLogin called: " + account.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener((Activity) context, task -> {
//                    if (task.isSuccessful()) {
//                        Log.d(TAG, "AuthManager: Firebase login successfull: ");
//                        handleFirebaseLoginCompleted();
//                    }
//                });
//    }
//
//    private void handleFirebaseLoginCompleted() {
//        Log.d(TAG, "AuthManager: handleFirebaseLoginCompleted method called");
//        //saveUser(user, (success, id) -> {
//
//    }
//
////    private void saveUser(MyUser user, DatabaseActionCallback mCallback) {
////        Log.d(MyConstants.TAG, "AuthManager: saveUser method called");
////        DatabaseReference usersReference = FirebaseDatabase.getSharPrefsInstance().getReference().child("users");
////        DatabaseReference newReference = usersReference.push();
////        newReference.setValue(user, (databaseError, databaseReference) -> {
////            if (databaseError != null) {
////                Log.d(MyConstants.TAG, "Model: User data save failed: "  + databaseError.getMessage());
////                mCallback.onCallback(false, null);
////            } else {
////                Log.d(MyConstants.TAG, "Model: User data saved successfully");
////                mCallback.onCallback(true, newReference.getKey());
////            }
////        });
////    }
//
////    public void getFirebaseToken () {
////        Log.d(TAG, "AuthManager: getFirebaseToken method called");
////        FirebaseInstanceId.getSharPrefsInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
////            @Override
////            public void onComplete(@NonNull Task<InstanceIdResult> task) {
////                if (!task.isSuccessful()) {
////                    Log.d(TAG, "Error with token: " + task.getException().getMessage());
////                } else {
////                    presenter.notifyTokenReceived(task.getResult().getToken());
////                }
////            }
////        });
////    }
//
//    public interface DatabaseActionCallback {
//        void onCallback(boolean success, String id);
//    }
//
//
//}
