package pl.dszerszen.randommovie.Activity.FavListActivity;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.ArrayList;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
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
                view.showNoResultsMessage();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Fav movie onComplete, list size: " + moviesList.size());
                view.hideLoader();
                view.showMoviesList(moviesList);
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void deleteMovie(int id) {
        firebaseDatabase.deleteMovie(id).subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: Movie deleted successfully");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: Movie not deleted: " + e.getMessage());
            }
        });
    }
}
