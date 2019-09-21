package pl.dszerszen.randommovie.Network;


import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.dszerszen.randommovie.ResponseGenre;
import pl.dszerszen.randommovie.ResponseMovieList;
import pl.dszerszen.randommovie.SingleMovieDetails;
import pl.dszerszen.randommovie.FilterData;

public class TmdbConnector {

    private final String API_KEY = "2a85fd50596d300dc0916d427958caa0";
    private final String LANGUAGE_KEY;

    private final NetworkService client;

    public TmdbConnector(String LANGUAGE_KEY) {
        this.LANGUAGE_KEY = LANGUAGE_KEY;
        this.client = NetworkClient.getRetrofit().create(NetworkService.class);
    }

    public Observable<ResponseMovieList> getMoviesList(int page) {
        Random random = new Random();
        int queryPage = random.nextInt(page);

        FilterData filter = FilterData.getInstance();


        return client.getMovies(
                API_KEY, LANGUAGE_KEY,
                filter.getGenreId(),
                filter.getMinYear(),
                filter.getMaxYear(),
                filter.getMinRuntime(),
                filter.getMaxRunTime(),
                queryPage)

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


}
