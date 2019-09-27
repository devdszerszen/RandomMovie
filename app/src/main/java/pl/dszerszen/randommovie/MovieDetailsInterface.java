package pl.dszerszen.randommovie;

import java.util.List;

import pl.dszerszen.randommovie.Filter.FilterData;

public interface MovieDetailsInterface {

    interface View{
        void showMovie(SingleMovieDetails movieDetails);
        void showLoader();
        void hideLoader();
        void saveGenresList(List<ResponseGenre.Genre> genresList);
        void showErrorMessage(String message);
    }

    interface Presenter {
        void getMovieDetails(int movieId);
        void getGenresList();
        void onRandomMovieButtonClicked();
    }
}
