package pl.dszerszen.randommovie.Network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkService {

    @GET("discover/movie")
    Observable<ResponseMovieList> getMovies(@Query("api_key") String api_key,
                                            @Query("language") String language,
                                            @Query("page") int page,
                                            @Query("primary_release_date.gte") String minYear,
                                            @Query("primary_release_date.lte") String maxYear,
                                            @Query("with_genres") String filter,
                                            @Query("vote_average.gte") String minVote,
                                            @Query("vote_average.lte") String maxVote
                                            );

    @GET("discover/movie")
    Observable<ResponseMovieList> getPosters(@Query("api_key") String api_key,
                                            @Query("language") String language,
                                            @Query("page") int page
    );

    @GET("genre/movie/list")
    Observable<ResponseGenre> getGenres(@Query("api_key") String api_key, @Query("language") String language);

    @GET("movie/{movie_id}")
    Observable<SingleMovieDetails> getMovieDetails(@Path("movie_id") int movieId, @Query("api_key") String api_key, @Query("language") String language);
}
