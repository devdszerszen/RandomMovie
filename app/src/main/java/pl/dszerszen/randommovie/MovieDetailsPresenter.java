package pl.dszerszen.randommovie;

import android.annotation.SuppressLint;
import android.util.Log;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import pl.dszerszen.randommovie.Dagger.MyApplication;
import pl.dszerszen.randommovie.Network.TmdbConnector;

public class MovieDetailsPresenter implements MovieDetailsInterface.Presenter{

    private final String TAG = "RandomMovie_log";

    private TmdbConnector connector;
    private MovieDetailsInterface.View view;

    private FilterData savedFilter;

    @Inject @Named("language")
    String language;

    public MovieDetailsPresenter(MovieDetailsInterface.View view) {
        this.view = view;
        this.connector = new TmdbConnector(MyApplication.getContext().getResources().getString(R.string.language_key));
        Log.d(TAG, "MovieDetailsPresenter: TestDagger: " + language);
    }


    @SuppressLint("CheckResult")
    @Override
    public void getRandomMovie(FilterData filterData, int maxPage) {
        view.showLoader();

        int page = 500;
        if (maxPage<500) {
            page = maxPage;
        }

        connector.getMoviesList(page).subscribeWith(getMoviesListObserver());
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


    // Collecting movies genresList response + call api to get movie details
    private Observer<ResponseMovieList> getMoviesListObserver() {

        return new Observer<ResponseMovieList>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseMovieList moviesList) {
                int validMovieId = getValidMovieId(moviesList);
                if (validMovieId > 0)
                    getMovieDetails(validMovieId);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        };
    }

    private int getValidMovieId(ResponseMovieList moviesList) {
        Random random = new Random();

        if (moviesList.page > moviesList.totalPages || moviesList.results.size() == 0) {
            int randomPage = random.nextInt(moviesList.totalPages);
            if (randomPage>500) {
                randomPage = 500;
            }
            getRandomMovie(savedFilter,randomPage);
            return -2;

            // Movie page is OK, get random movie from that page
        } else {
            int randomInt = random.nextInt(moviesList.results.size());
            ResponseMovieList.Result randomMovie = moviesList.results.get(randomInt);

            // Check description and picture
            int counter = 0;
            while (randomMovie.overview.equals("") || randomMovie.backdropPath == null) {
                if (counter > 3) {
                    int randomPage = random.nextInt(moviesList.totalPages);
                    if (randomPage>500) {
                        randomPage = 500;
                    }
                    getRandomMovie(savedFilter, randomPage);
                    return -1;
                }
                randomInt = random.nextInt(moviesList.results.size());
                randomMovie = moviesList.results.get(randomInt);
                counter++;
            }
            return randomMovie.id;
        }
    }
}
