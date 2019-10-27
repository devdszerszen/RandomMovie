package pl.dszerszen.randommovie.Activity.MovieDetailsActivity;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import pl.dszerszen.randommovie.Dagger.MyApplication;
import pl.dszerszen.randommovie.Firebase.DatabaseManager;
import pl.dszerszen.randommovie.Firebase.FirebaseDBInterface;
import pl.dszerszen.randommovie.Firebase.FirebaseStoredMovie;
import pl.dszerszen.randommovie.Network.TmdbConnector;
import pl.dszerszen.randommovie.R;
import pl.dszerszen.randommovie.Network.ResponseGenre;
import pl.dszerszen.randommovie.Network.ResponseMovieList;
import pl.dszerszen.randommovie.Network.SingleMovieDetails;

public class MovieDetailsPresenter implements MovieDetailsInterface.Presenter{

    private final String TAG = "RandomMovie_log";

    private TmdbConnector connector;
    private MovieDetailsInterface.View view;
    private FirebaseDBInterface firebaseDatabase;

    @Inject @Named("language")
    String language;

    int apiCallsCounter = 0;
    final int MAX_API_CALLS = 10;

    public MovieDetailsPresenter(MovieDetailsInterface.View view) {
        this.view = view;
        this.connector = new TmdbConnector(MyApplication.getContext().getResources().getString(R.string.language_key));
        this.firebaseDatabase = DatabaseManager.getInstance();
    }

    @Override
    public void onRandomMovieButtonClicked() {
        apiCallsCounter = 0;
        view.showLoader();
        getRandomMovie(500);
    }


    @SuppressLint("CheckResult")
    public void getRandomMovie(int maxPage) {


        apiCallsCounter++;
        if (apiCallsCounter >MAX_API_CALLS) {
            view.showMessage("Something went wrong, sorry");
            view.hideLoader();
            return;
        }

        int page = 500;
        if (maxPage<500) {
            page = maxPage;
        }

        //connector.getMoviesList(page).subscribeWith(getMoviesListObserver());
        connector.getMoviesList(page).subscribe(new DisposableObserver<ResponseMovieList>() {
            @Override
            public void onNext(ResponseMovieList responseMovieList) {
                Log.d(TAG, "logRX onNext: Current page:" + responseMovieList.page);
                Log.d(TAG, "logRX onNext: Total pages:" + responseMovieList.totalPages);
                Log.d(TAG, "logRX onNext: Results list size:" + responseMovieList.results.size());

                if (responseMovieList.totalPages == 0) {
                    Log.d(TAG, "logRX onNext: No results");
                    view.showMessage("No results for query, change filters");
                } else if (responseMovieList.page>responseMovieList.totalPages || responseMovieList.results.size()==0) {
                    Log.d(TAG, "logRX onNext: Current page greater than total pages");
                    getRandomMovie(responseMovieList.totalPages);
                } else {
                    Log.d(TAG, "logRX onNext: Result OK");
                    int movieId = getValidMovieId(responseMovieList);
                    if (movieId>-1) {
                        getMovieDetails(movieId);
                    } else {
                        getRandomMovie(responseMovieList.totalPages);
                    }
                }
            }


            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "logRX onComplete: Observer disposed");
                Log.d(TAG, "logRX --------------------------------");
                dispose();
            }
        });
    }


    private int getValidMovieId(ResponseMovieList moviesList) {
        Random random = new Random();

        List<ResponseMovieList.Result> goodMoviesList = new ArrayList<>();

        for (ResponseMovieList.Result resultMovie: moviesList.results) {
            if (resultMovie != null) {
                if (!resultMovie.overview.equals("") && resultMovie.backdropPath != null) {
                    goodMoviesList.add(resultMovie);
                }
            }
        }
        Log.d(TAG, "logRX getValidMovieId: goodMovies size:"+goodMoviesList.size());

        if (goodMoviesList.size()>0) {
            int movieId = goodMoviesList.get(random.nextInt(goodMoviesList.size())).id;
            Log.d(TAG, "logRX getValidMovieId: returns id:" + movieId);
            return movieId;
        } else {
            return -1;
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void getMovieDetails(int movieId) {
        connector.getMovieDetails(movieId).subscribeWith(new Observer<SingleMovieDetails>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SingleMovieDetails movieDetails) {
                view.showMovie(movieDetails);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                view.hideLoader();
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void getGenresList() {
        connector.getGenresList().subscribeWith(new DisposableObserver<ResponseGenre>() {
            @Override
            public void onNext(ResponseGenre responseGenre) {
                view.saveGenresList(responseGenre.genres);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                dispose();
            }
        });
    }

    @Override
    public void addMovieToFavourities(SingleMovieDetails currentMovie) {
        FirebaseStoredMovie firebaseStoredMovie = new FirebaseStoredMovie(currentMovie);
        firebaseDatabase.addMovie(firebaseStoredMovie);
    }

//
//    // Collecting movies genresList response + call api to get movie details
//    private Observer<ResponseMovieList> getMoviesListObserver() {
//
//        return new Observer<ResponseMovieList>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(ResponseMovieList moviesList) {
//                int validMovieId = getValidMovieId(moviesList);
//                if (validMovieId > 0)
//                    getMovieDetails(validMovieId);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//            }
//        };
//    }


}
