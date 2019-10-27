package pl.dszerszen.randommovie.FavListActivity;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pl.dszerszen.randommovie.Firebase.DatabaseManager;
import pl.dszerszen.randommovie.Firebase.FirebaseDBInterface;
import pl.dszerszen.randommovie.Firebase.FirebaseStoredMovie;

public class FavPresenter implements FavInterface.Presenter {

    final String TAG = "RandomMovie_log";

    FavInterface.View view;
    FirebaseDBInterface firebaseDatabase;

    ArrayList<FirebaseStoredMovie> moviesList = new ArrayList<>();

    public FavPresenter(FavInterface.View view) {
        this.view = view;
        this.firebaseDatabase = DatabaseManager.getInstance();
    }

    @SuppressLint("CheckResult")
    @Override
    public void getList() {
        firebaseDatabase.getMovies().subscribeWith(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                moviesList.add((FirebaseStoredMovie)o);
                Log.d(TAG, "Fav movie onNext: " + o.toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Fav movie onComplete, list size: " + moviesList.size());
                view.hideLoader();
                view.showMoviesList(moviesList);
            }
        });
    }
}
