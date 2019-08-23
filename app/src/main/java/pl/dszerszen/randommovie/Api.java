package pl.dszerszen.randommovie;

public class Api {
    private String hostUrl = "https://api.themoviedb.org";
    private String apiKey = "2a85fd50596d300dc0916d427958caa0";
    private static String imageUrl = "https://image.tmdb.org/t/p/original/";

    public String getHostUrl() {
        return hostUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public static String getImageUrl() {
        return imageUrl;
    }
}
