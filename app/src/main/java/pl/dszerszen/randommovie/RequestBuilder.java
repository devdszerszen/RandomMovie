package pl.dszerszen.randommovie;

import java.util.ArrayList;
import java.util.Random;


public class RequestBuilder {

    public static final String TAG = "Damian";

    private String language = "pl-PL";
    ArrayList<SingleParam> parameters = new ArrayList<>();

    public void addParameter(String key, String value) {
        parameters.add(new SingleParam(key, value));
    }

    // Standard url to get random movie
    public String createRandomMovieRequest(int maxPage, String filter) {
        Random random = new Random();
        String page = String.valueOf(random.nextInt(maxPage));

        StringBuilder builder = new StringBuilder();
        builder.append(Api.getHostUrl())
                .append(Api.getRandomMoviePath())
                .append("?")
                .append(Api.getApiPath())
                .append(Api.getApiKey())
                .append("&")
                .append(Api.getLanguagePath())
                .append(language)
                .append("&page=").append(page);

        if (!filter.equals(Api.NO_FILTER)) {
            builder.append(filter);
        }

        return builder.toString();
    }

    public String createFilterPath() {
        StringBuilder builder = new StringBuilder();

        for (SingleParam param: parameters) {
            builder.append("&").append(param.getKey()).append("=").append(param.getValue());
        }
        parameters.clear();
        return builder.toString();
    }

    public String createMovieDetailsRequest(int id) {
        StringBuilder builder = new StringBuilder();
        builder.append(Api.getHostUrl())
                .append(Api.getMovieDetailsPath())
                .append(id)
                .append("?")
                .append(Api.getLanguagePath())
                .append(language)
                .append("&")
                .append(Api.getApiPath())
                .append(Api.getApiKey());

        return builder.toString();
    }

    public String createGenresListRequest() {
        StringBuilder builder = new StringBuilder();
        builder.append(Api.getHostUrl())
                .append(Api.getGenresPath())
                .append("?")
                .append(Api.getLanguagePath())
                .append(language)
                .append("&")
                .append(Api.getApiPath())
                .append(Api.getApiKey());

        return builder.toString();
    }

    class SingleParam {
        private String key;
        private String value;

        public SingleParam(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
