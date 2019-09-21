package pl.dszerszen.randommovie.Network;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import pl.dszerszen.randommovie.ResponseGenre;
import pl.dszerszen.randommovie.ResponseMovieList;
import pl.dszerszen.randommovie.SingleMovieDetails;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkService {

    @GET("discover/movie")
    Observable<ResponseMovieList> getMovies(@Query("api_key") String api_key,
                                            @Query("language") String language,
                                            @Query("with_genres") String filter,
                                            @Query("release_date_gte") String minYear,
                                            @Query("release_date_lte") String maxYear,
                                            @Query("with_runtime.gte") String minRuntime,
                                            @Query("with_runtime.lte") String maxRuntime,
                                            @Query("page") int page);

    @GET("genre/movie/list")
    Observable<ResponseGenre> getGenres(@Query("api_key") String api_key, @Query("language") String language);

    @GET("movie/{movie_id}")
    Observable<SingleMovieDetails> getMovieDetails(@Path("movie_id") int movieId, @Query("api_key") String api_key, @Query("language") String language);
}
