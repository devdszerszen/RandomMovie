package pl.dszerszen.randommovie.Activity.FavListActivity;

import android.annotation.SuppressLint;

import java.util.ArrayList;

import io.reactivex.CompletableObserver;
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
            }

            @Override
            public void onError(Throwable e) {
                view.showNoResultsMessage();
            }

            @Override
            public void onComplete() {
                view.hideLoader();
                sortListByDate();
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
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }

    private void sortListByName() {
        moviesList.sort((movie1,movie2) -> movie1.title.compareTo(movie2.title));
    }

    private void sortListByDate() {
        moviesList.sort((movie1,movie2) -> movie1.timestamp - movie2.timestamp);
    }
}
