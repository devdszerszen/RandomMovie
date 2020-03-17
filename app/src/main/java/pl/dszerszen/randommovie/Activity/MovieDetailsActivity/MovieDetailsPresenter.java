package pl.dszerszen.randommovie.Activity.MovieDetailsActivity;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import pl.dszerszen.randommovie.Base.BasePresenter;
import pl.dszerszen.randommovie.Error.ErrorType;
import pl.dszerszen.randommovie.Firebase.AuthManager;
import pl.dszerszen.randommovie.Firebase.DatabaseManager;
import pl.dszerszen.randommovie.Firebase.FirebaseAuthInterface;
import pl.dszerszen.randommovie.Firebase.FirebaseDBInterface;
import pl.dszerszen.randommovie.Firebase.FirebaseStoredMovie;
import pl.dszerszen.randommovie.MessageCode;
import pl.dszerszen.randommovie.Network.ResponseGenre;
import pl.dszerszen.randommovie.Network.ResponseMovieList;
import pl.dszerszen.randommovie.Network.SingleMovieDetails;
import pl.dszerszen.randommovie.Network.TmdbConnector;

public class MovieDetailsPresenter extends BasePresenter implements MovieDetailsInterface.Presenter{

    private TmdbConnector connector;
    private MovieDetailsInterface.View view;
    private FirebaseDBInterface firebaseDatabase;
    private FirebaseAuthInterface firebaseAuth;

    @Inject @Named("language")
    String language;

    private int apiCallsCounter = 0;
    private final int MAX_API_CALLS = 10;

    //Fav movies id
    ArrayList<Integer> favMoviesIdsList = new ArrayList<>();
    private boolean shouldRefreshFavMoviesIdList = true;

    public MovieDetailsPresenter(MovieDetailsInterface.View view) {
        this.view = view;
        this.connector = TmdbConnector.getConnectorInstance();
        this.firebaseDatabase = DatabaseManager.getInstance();
        this.firebaseAuth = AuthManager.getInstance((Context)view);
        updateFavMoviesIdList();
        if (isAdModuleActive()) {
            view.initGoogleAds();
        }
    }

    @Override
    public void onRandomMovieButtonClicked() {
        apiCallsCounter = 0;
        view.showLoader();
        getRandomMovie(500);
    }


    @SuppressLint("CheckResult")
    public void getRandomMovie(int maxPage) {

        if (shouldRefreshFavMoviesIdList) updateFavMoviesIdList();

        apiCallsCounter++;
        if (apiCallsCounter > MAX_API_CALLS) {
            view.showError(ErrorType.LACK_OF_RESULT);
            return;
        }

        int page = 500;
        if (maxPage<500) {
            page = maxPage;
        }

        connector.getMoviesList(page).subscribe(new DisposableObserver<ResponseMovieList>() {
            @Override
            public void onNext(ResponseMovieList responseMovieList) {


                if (responseMovieList.totalPages == 0) {
                    view.showError(ErrorType.LACK_OF_RESULT);
                } else if (responseMovieList.page>responseMovieList.totalPages || responseMovieList.results.size()==0) {
                    getRandomMovie(responseMovieList.totalPages);
                } else {
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
                view.showError(ErrorType.NETWORK);
            }

            @Override
            public void onComplete() {
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

        if (goodMoviesList.size()>0) {
            int movieId = goodMoviesList.get(random.nextInt(goodMoviesList.size())).id;
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
                view.setMovieAsFavourite(isSetAsFavourite(movieDetails));
                view.showMovie(movieDetails);
            }

            @Override
            public void onError(Throwable e) {
                view.showError(ErrorType.NETWORK);
            }

            @Override
            public void onComplete() {
                //view.hideLoader();
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void getGenresList() {
        connector.getGenresList().subscribeWith(new DisposableObserver<ResponseGenre>() {
            @Override
            public void onNext(ResponseGenre responseGenre) {
                List<ResponseGenre.Genre> genresList = responseGenre.genres;
                Collections.sort(genresList, (ResponseGenre.Genre g1, ResponseGenre.Genre g2) -> g1.name.compareTo(g2.name));
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
    public void onFavIconClicked(SingleMovieDetails movie, boolean isSetAsFavourite) {
        if (isUserLogged()) {
            if (isSetAsFavourite) {
                deleteMovieFromFavourites(movie.id);
                view.setMovieAsFavourite(false);
            } else {
                addMovieToFavourities(movie);
                view.setMovieAsFavourite(true);
                view.showToast(MessageCode.MOVIE_ADDED_FAV);
            }
        } else {
            view.showLoginPrompt();
        }
    }

    @Override
    public void addMovieToFavourities(SingleMovieDetails currentMovie) {
        FirebaseStoredMovie firebaseStoredMovie = new FirebaseStoredMovie(currentMovie);
        firebaseDatabase.addMovie(firebaseStoredMovie);
        shouldRefreshFavMoviesIdList = true;
    }

    @SuppressLint("CheckResult")
    @Override
    public void deleteMovieFromFavourites(int id) {
        shouldRefreshFavMoviesIdList = true;
        firebaseDatabase.deleteMovie(id).subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                view.showToast(MessageCode.MOVIE_DELETED_FAV);
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }

    @SuppressLint("CheckResult")
    private void updateFavMoviesIdList() {
        firebaseDatabase.getFavouriteMoviesIds().subscribeWith(new SingleObserver<ArrayList<Integer>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(ArrayList<Integer> integers) {
                favMoviesIdsList = integers;
                shouldRefreshFavMoviesIdList = false;
            }

            @Override
            public void onError(Throwable e) {
                e.getMessage();
            }
        });
    }

    private boolean isSetAsFavourite(SingleMovieDetails movieDetails) {
        for (Integer id: favMoviesIdsList) {
            if (movieDetails.id == id) {
                return true;
            }
        }
        return false;
    }

    private boolean isUserLogged() {
        return firebaseAuth.isUserSignedToFirebase() && firebaseAuth.isUserSignedToGoogle();
    }
}
