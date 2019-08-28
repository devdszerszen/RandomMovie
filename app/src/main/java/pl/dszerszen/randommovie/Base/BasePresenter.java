package pl.dszerszen.randommovie.Base;

import java.util.List;

import pl.dszerszen.randommovie.ResponseGenre;
import pl.dszerszen.randommovie.SingleMovieDetails;

public interface BasePresenter {
    void sendRandomMovieDetails(SingleMovieDetails movie);
    void reportError(String message);
    void sendGenresList(List<ResponseGenre> list);
}
