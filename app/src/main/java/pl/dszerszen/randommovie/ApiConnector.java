package pl.dszerszen.randommovie;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;


import java.io.IOException;
import java.lang.reflect.Type;

import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.dszerszen.randommovie.GSON.Genre;

public class ApiConnector implements StartInterface.Model{
    final String TAG = "Damian";

    private OkHttpClient client;
    private Gson gson;
    private StartInterface.Presenter startPresenter;
    private RequestBuilder requestHelper = new RequestBuilder();


    public ApiConnector(StartInterface.Presenter presenter) {
        this.client = new OkHttpClient();
        this.gson = new Gson();
        this.startPresenter = presenter;
        this.requestHelper = new RequestBuilder();
    }

//    public NewApiConnector() {
//        this.client = new OkHttpClient();
//        this.api = new Api();
//        this.gson = new Gson();
//        this.requestHelper = new RequestBuilder();
//
//        getRandomMovie(288,RequestBuilder.NO_FILTER);
//    }

    @Override
    public void getGenresList() {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(requestHelper.createGenresListRequest());
        Request request = requestBuilder.build();
        Log.d(TAG, "APIConnector: Genres url is: " + request.toString());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                startPresenter.reportError(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonOutput = response.body().string();
                Log.d(TAG, "onResponse: response: "+ jsonOutput);

                JsonObject jsonObject = gson.fromJson(jsonOutput,JsonObject.class);
                JsonArray array = jsonObject.get("genres").getAsJsonArray();

                Type listType = new TypeToken<List<Genre>>(){}.getType();
                List<Genre> genresList = gson.fromJson(array,listType);
                startPresenter.sendGenresList(genresList);
            }
        });
    }

    @Override
    public void getRandomMovie(int page, String filter) {
        //Create random
        Random random = new Random();

        //Create request url
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(requestHelper.createRandomMovieRequest(page, filter));
        Request request = requestBuilder.build();
        Log.d(TAG, "APIConnector: Random movie url is: " + request.toString());

        //Call to api
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "APIConnector: fail: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d(TAG, "APIConnector: response code is: " + response.code());
                if (response.code() != 200) {
                    //handle error
                } else {
                    String jsonOutput = response.body().string();
                    MoviesList moviesList = gson.fromJson(jsonOutput, MoviesList.class);
                    Log.d(TAG, "APIConnector: moviesListObject:" + moviesList.toString());

                    // Wrong page or lack of results
                    if (moviesList.page > moviesList.totalPages || moviesList.results.size() == 0) {
                        Log.d(TAG, "APIConnector: should call method again with correct page");
                        int randomPage = random.nextInt(moviesList.totalPages);
                        getRandomMovie(randomPage, filter);
                        return;

                        // Movie page is OK, get random movie from that page
                    } else {
                        int randomInt = random.nextInt(moviesList.results.size());
                        MoviesList.Result randomMovie = moviesList.results.get(randomInt);

                        // Check description and picture
                        int counter = 0;
                        while (randomMovie.overview.equals("") || randomMovie.backdropPath == null) {
                            if (counter > 3) {
                                Log.d(TAG, "APIConnector: try another page");
                                int randomPage = random.nextInt(moviesList.totalPages);
                                getRandomMovie(randomPage, filter);
                                return;
                            }
                            Log.d(TAG, "APIConnector: lack of info for movie with id: " + randomMovie.id);
                            randomInt = random.nextInt(moviesList.results.size());
                            randomMovie = moviesList.results.get(randomInt);
                            counter++;
                        }
                        Log.d(TAG, "APIConnector: got movie: " + randomMovie);
                        getMovieDetails(randomMovie.id);
                    }
                }
            }
        });
    }

    public void getMovieDetails(int id) {
        //Create request url
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(requestHelper.createMovieDetailsRequest(id));
        Request request = requestBuilder.build();
        Log.d(TAG, "APIConnector: Details url is: " + request.toString());


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                startPresenter.reportError(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonOutput = response.body().string();
                SingleMovieDetails movieDetails = gson.fromJson(jsonOutput,SingleMovieDetails.class);
                startPresenter.callbackRandomMovie(movieDetails);
            }
        });
    }

}
