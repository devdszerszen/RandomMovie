package pl.dszerszen.randommovie.Firebase;

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import pl.dszerszen.randommovie.SharPrefs.SharPrefsManager;

public class DatabaseManager implements FirebaseDBInterface {

    final String TAG = "RandomMovie_log";

    final String USERS_PATH = "users";

    private static DatabaseManager databaseInstance = null;
    public static DatabaseManager getInstance() {
        if (databaseInstance == null) {
            databaseInstance = new DatabaseManager();
        }
        return databaseInstance;
    }

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    DatabaseReference users;
    GoogleSignInAccount account;
    SharPrefsManager sharPrefsManager;


    public DatabaseManager() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        users = mReference.child(USERS_PATH);
        sharPrefsManager = SharPrefsManager.getSharPrefsManager();

    }

    @Override
    public void addUser(GoogleSignInAccount googleAccount) {
        FirebaseStoredUser user = new FirebaseStoredUser(googleAccount.getEmail(),googleAccount.getDisplayName());
        account = googleAccount;
        String firebaseUserKey = users.push().getKey();
        sharPrefsManager.setFirebaseKey(firebaseUserKey);
        users.child(firebaseUserKey).setValue(user);
    }

    @Override
    public void incrementCounter() {
        Log.d(TAG, "incrementCounter: called");
        users.child(sharPrefsManager.getFirebaseKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseStoredUser user = dataSnapshot.getValue(FirebaseStoredUser.class);
                int counter = user.counter;
                users.child(sharPrefsManager.getFirebaseKey()).child("counter").setValue(++counter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
