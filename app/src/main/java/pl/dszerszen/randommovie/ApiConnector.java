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

public class ApiConnector implements StartInterface.Model {
    final String TAG = "Damian";
    private String apiKey = "1ecff66"+"&t=Harry";
    private OkHttpClient client;
    private Api api;
    private Gson gson;
    private StartInterface.Presenter presenter;


    public ApiConnector(StartInterface.Presenter presenter) {
        this.client = new OkHttpClient();
        this.api = new Api();
        this.gson = new Gson();
        this.presenter = presenter;

    }

    public void callApi() {
        final Request request = new Request.Builder()
                .url(api.getHostUrl())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: call: " + call.toString());
                Log.d(TAG, "onFailure: exception: "+ e.getLocalizedMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d(TAG, "onResponse: call: "+ call.toString());
                Log.d(TAG, "onResponse: response: "+ response.body().string());
            }
        });
    }

    @Override
    public void getGenresList() {
        final Request request = new Request.Builder()
                .url(api.getHostUrl()+"/3/genre/movie/list?language=pl-PL&api_key="+api.getApiKey())
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                presenter.reportError(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonOutput = response.body().string();
                Log.d(TAG, "onResponse: response: "+ jsonOutput);

                JsonObject jsonObject = gson.fromJson(jsonOutput,JsonObject.class);
                JsonArray array = jsonObject.get("genres").getAsJsonArray();

                Type listType = new TypeToken<List<Genre>>(){}.getType();
                List<Genre> genresList = gson.fromJson(array,listType);
                presenter.sendGenresList(genresList);
            }
        });
    }

    @Override
    public void getRandomMovie() {
        Random random = new Random();
        int page = random.nextInt(500);

        final Request request = new Request.Builder()
                .url(api.getHostUrl()+"/3/discover/movie?language=pl-PL&page="
                        +page
                        +"&api_key="
                        +api.getApiKey())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                presenter.reportError(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonOutput = response.body().string();
                Log.d(TAG, "onResponse: response: "+ jsonOutput);

                JsonObject jsonObject = gson.fromJson(jsonOutput,JsonObject.class);
                JsonArray array = jsonObject.get("results").getAsJsonArray();

                Type listType = new TypeToken<List<SingleMovie>>(){}.getType();
                List<SingleMovie> moviesList = gson.fromJson(array,listType);
                int randomMovie = random.nextInt(moviesList.size());
                SingleMovie movie = moviesList.get(randomMovie);

                while (movie.overview.equals("")) {
                    randomMovie = random.nextInt(moviesList.size());
                    movie = moviesList.get(randomMovie);
                    Log.d(TAG, "onResponse: Pętla while się wykonałą");
                }

                Log.d(TAG, "onResponse: Single movie data is: " + movie);
                //presenter.callbackRandomMovie(movie);
                getMovieDetails(movie.id);
            }
        });
    }

    public void getMovieDetails(int id) {
        final Request request = new Request.Builder()
                .url(api.getHostUrl()+"/3/movie/"+id+"?language=pl-PL&api_key="+api.getApiKey())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                presenter.reportError(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonOutput = response.body().string();
                Log.d(TAG, "onResponse: movie details: " + jsonOutput);

                SingleMovieDetails movieDetails = gson.fromJson(jsonOutput,SingleMovieDetails.class);
                Log.d(TAG, "onResponse: SingleMovieDetails class details: " + movieDetails.toString());
                presenter.callbackRandomMovie(movieDetails);
            }
        });
    }
}
