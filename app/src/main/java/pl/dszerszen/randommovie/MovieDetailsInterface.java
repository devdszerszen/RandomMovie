package pl.dszerszen.randommovie;

import java.util.List;

import pl.dszerszen.randommovie.Base.BasePresenter;

public interface MovieDetailsInterface {

    interface View{
        void showMovie(SingleMovieDetails movieDetails);
        void showLoader();
        void hideLoader();
        void saveGenresList(List<ResponseGenre.Genre> genresList);
    }

    interface Presenter {
        void getRandomMovie(FilterData filterData, int maxPage);
        void getMovieDetails(int movieId);
        void getGenresList();
    }
}
