package pl.dszerszen.randommovie.Firebase;

import pl.dszerszen.randommovie.Network.SingleMovieDetails;

public class FirebaseStoredMovie {
    public String title;
    public String year;
    public int id;

    public FirebaseStoredMovie() {
    }

    public FirebaseStoredMovie(SingleMovieDetails movieDetails) {
        this.title = movieDetails.title;
        this.year = movieDetails.releaseDate.substring(0,4);
        this.id = movieDetails.id;
    }
}
