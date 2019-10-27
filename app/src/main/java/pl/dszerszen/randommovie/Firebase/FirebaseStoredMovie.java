package pl.dszerszen.randommovie.Firebase;

import pl.dszerszen.randommovie.SingleMovieDetails;

public class FirebaseStoredMovie {
    String title;
    String genre;
    int id;

    public FirebaseStoredMovie(SingleMovieDetails movieDetails) {
        this.title = movieDetails.title;
        this.genre = movieDetails.genres.get(0).toString();
        this.id = movieDetails.id;
    }
}
