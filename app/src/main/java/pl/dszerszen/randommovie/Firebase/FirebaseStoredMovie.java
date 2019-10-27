package pl.dszerszen.randommovie.Firebase;

import pl.dszerszen.randommovie.SingleMovieDetails;

public class FirebaseStoredMovie {
    public String title;
    public int id;

    public FirebaseStoredMovie() {
    }

    public FirebaseStoredMovie(SingleMovieDetails movieDetails) {
        this.title = movieDetails.title;
        this.id = movieDetails.id;
    }
}
