package pl.dszerszen.randommovie.Network;


import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.dszerszen.randommovie.Dagger.MyApplication;
import pl.dszerszen.randommovie.Filter.FilterData;
import pl.dszerszen.randommovie.R;

public class TmdbConnector {

    private final String API_KEY = "2a85fd50596d300dc0916d427958caa0";
    private final String LANGUAGE_KEY;
    private final String SORT_KEY = "popularity.desc";
    boolean DO_NOT_INCLUDE_ADULT = false;

    private final NetworkService client;

    private static TmdbConnector connectorInstance = null;
    public static TmdbConnector getConnectorInstance() {
        if (connectorInstance == null) {
            connectorInstance = new TmdbConnector(MyApplication.getContext().getResources().getString(R.string.language_key));
        }
        return connectorInstance;
    }

    public TmdbConnector(String LANGUAGE_KEY) {
        this.LANGUAGE_KEY = LANGUAGE_KEY;
        this.client = NetworkClient.getRetrofit().create(NetworkService.class);
    }

    public Observable<ResponseMovieList> getMoviesList(int page) {
        Random random = new Random();
        int queryPage = 1;
        if (page>1) {
            queryPage = random.nextInt(page-1)+1;
        }


        FilterData filter = FilterData.getInstance();


        return client.getMovies(
                API_KEY, LANGUAGE_KEY,DO_NOT_INCLUDE_ADULT,
                queryPage,
                filter.getMinYear(),
                filter.getMaxYear(),
                filter.getGenreId(),
                filter.getMinVote(),
                filter.getMaxVote()
                )

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResponseGenre> getGenresList() {
        return client.getGenres(API_KEY, LANGUAGE_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<SingleMovieDetails> getMovieDetails(int movieId) {
        return client.getMovieDetails(movieId, API_KEY, LANGUAGE_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResponseMovieList> getPostersList() {
        Random random = new Random();
        int randomPage = random.nextInt(20)+1;
        return client.getPosters(API_KEY,LANGUAGE_KEY,DO_NOT_INCLUDE_ADULT,SORT_KEY,randomPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
