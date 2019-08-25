package pl.dszerszen.randommovie;

public class Api {
    private static String hostUrl = "https://api.themoviedb.org";
    private static String randomMoviePath = "/3/discover/movie";
    private static String languagePath = "language=";
    private static String apiPath = "api_key=";
    private static String apiKey = "2a85fd50596d300dc0916d427958caa0";
    private static String genresPath = "/3/genre/movie/list";
    private static String imageUrl = "https://image.tmdb.org/t/p/original/";
    private static String movieDetailsPath = "/3/movie/";
    private static String filterGenre = "with_genres";

    public static String NO_FILTER = "emptyFilter";

    public static String getHostUrl() {
        return hostUrl;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getImageUrl() {
        return imageUrl;
    }

    public static String getRandomMoviePath() {
        return randomMoviePath;
    }

    public static String getLanguagePath() {
        return languagePath;
    }

    public static String getApiPath() {
        return apiPath;
    }

    public static String getMovieDetailsPath() {
        return movieDetailsPath;
    }

    public static String getGenresPath() {
        return genresPath;
    }

    public static String getFilterGenre() {
        return filterGenre;
    }
}
