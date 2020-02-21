package pl.dszerszen.randommovie.Firebase;

import android.annotation.SuppressLint;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.dszerszen.randommovie.SharPrefs.SharPrefsManager;

public class DatabaseManager implements FirebaseDBInterface {

    final String TAG = "RandomMovie_log";

    final String USERS_PATH = "users";
    final String MOVIES_PATH = "movies";

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
    DatabaseReference movies;
    GoogleSignInAccount account;
    SharPrefsManager sharPrefsManager;


    public DatabaseManager() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        users = mReference.child(USERS_PATH);
        movies = mReference.child(MOVIES_PATH);

        sharPrefsManager = SharPrefsManager.getSharPrefsInstance();

    }

    @Override
    public void addUser(GoogleSignInAccount googleAccount) {
        FirebaseStoredUser user = new FirebaseStoredUser(googleAccount.getEmail(),googleAccount.getDisplayName());
        account = googleAccount;
        users.child(sharPrefsManager.getFirebaseKey()).setValue(user);
    }

    @Override
    public void incrementCounter() {
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

    public void addMovie(FirebaseStoredMovie movie) {
        movies.child(sharPrefsManager.getFirebaseKey()).child(String.valueOf(movie.id)).setValue(movie);
    }

    @Override
    @SuppressLint("CheckResult")
    public Completable deleteMovie(int id) {

        return Completable.create(emitter -> {
            movies.child(sharPrefsManager.getFirebaseKey()).child(String.valueOf(id)).removeValue((databaseError, databaseReference) -> {
                if (databaseError!=null) {
                    emitter.onError(new Throwable(databaseError.getMessage()));
                } else {
                    emitter.onComplete();
                }
            });
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable getMovies() {

        return Observable.create(source -> {

            movies.child(sharPrefsManager.getFirebaseKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleItem : dataSnapshot.getChildren()) {
                        FirebaseStoredMovie movie = singleItem.getValue(FirebaseStoredMovie.class);
                        source.onNext(movie);
                    }
                    source.onComplete();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<ArrayList<Integer>> getFavouriteMoviesIds() {
        return Single.create(emitter -> {
            movies.child(sharPrefsManager.getFirebaseKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count = (int)dataSnapshot.getChildrenCount();
                    ArrayList<Integer> movieIds = new ArrayList<>();
                    for (DataSnapshot singleItem : dataSnapshot.getChildren()) {
                        movieIds.add(Integer.parseInt(singleItem.getKey()));
                    }
                    emitter.onSuccess(movieIds);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
    }
}
