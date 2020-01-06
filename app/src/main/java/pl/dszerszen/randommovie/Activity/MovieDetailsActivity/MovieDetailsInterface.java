package pl.dszerszen.randommovie.Activity.MovieDetailsActivity;

import java.util.List;

import pl.dszerszen.randommovie.Network.ResponseGenre;
import pl.dszerszen.randommovie.Network.SingleMovieDetails;

public interface MovieDetailsInterface {

    interface View{
        void setMovieAsFavourite(boolean isFavourite);
        void showMovie(SingleMovieDetails movieDetails);
        void showLoader();
        void hideLoader();
        void saveGenresList(List<ResponseGenre.Genre> genresList);
        void showMessage(String message);
        void showLoginPrompt();
        void showNetworkError();
        void backToStartActivityWithLoginPrompt();
    }

    interface Presenter {
        void getMovieDetails(int movieId);
        void getGenresList();
        void onRandomMovieButtonClicked();
        void addMovieToFavourities(SingleMovieDetails currentMovie);
        void deleteMovieFromFavourites(int id);
        void onFavIconClicked(SingleMovieDetails movie, boolean isSetAsFavourite);
    }
}
