package pl.dszerszen.randommovie.Firebase;

import pl.dszerszen.randommovie.Network.SingleMovieDetails;

public class FirebaseStoredMovie {
    public String title;
    public String year;
    public String description;
    public String imgUrl;
    public String genre;
    public int id;
    public int timestamp;

    public FirebaseStoredMovie() {
    }

    public FirebaseStoredMovie(SingleMovieDetails movieDetails) {
        this.title = movieDetails.title;
        this.year = movieDetails.releaseDate.substring(0,4);
        this.description = movieDetails.overview;
        this.imgUrl = movieDetails.posterPath;
        this.id = movieDetails.id;

        this.genre = movieDetails.genres.get(0).name;
        if (movieDetails.genres.size()>1) {
            for (int i = 1; i < Integer.min(3,movieDetails.genres.size()); i++) {
                this.genre = this.genre.concat(", " + movieDetails.genres.get(i).name);
            }
        }

        Long currentTime = System.currentTimeMillis()/1000L;
        this.timestamp = currentTime.intValue();
    }
}
