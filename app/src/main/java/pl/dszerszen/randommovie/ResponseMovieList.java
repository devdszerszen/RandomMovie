package pl.dszerszen.randommovie;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseMovieList {
    @Override
    public String toString() {
        return "ResponseMovieList{" +
                "page=" + page +
                ", totalResults=" + totalResults +
                ", totalPages=" + totalPages +
                ", resultArraySize=" + results.size() +
                "}';";
    }

    @SerializedName("page")
    @Expose
    public int page;
    @SerializedName("total_results")
    @Expose
    public int totalResults;
    @SerializedName("total_pages")
    @Expose
    public int totalPages;
    @SerializedName("results")
    @Expose
    public List<Result> results = null;


    public class Result {

        @SerializedName("vote_count")
        @Expose
        public int voteCount;
        @SerializedName("id")
        @Expose
        public int id;
        @SerializedName("video")
        @Expose
        public boolean video;
        @SerializedName("vote_average")
        @Expose
        public float voteAverage;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("popularity")
        @Expose
        public float popularity;
        @SerializedName("poster_path")
        @Expose
        public String posterPath;
        @SerializedName("original_language")
        @Expose
        public String originalLanguage;
        @SerializedName("original_title")
        @Expose
        public String originalTitle;
        @SerializedName("genre_ids")
        @Expose
        public List<Integer> genreIds = null;
        @SerializedName("backdrop_path")
        @Expose
        public Object backdropPath;
        @SerializedName("adult")
        @Expose
        public boolean adult;
        @SerializedName("overview")
        @Expose
        public String overview;
        @SerializedName("release_date")
        @Expose
        public String releaseDate;

        @Override
        public String toString() {
            return "Result{" +
                    "title='" + title + '\'' +
                    ", backdropPath=" + backdropPath +
                    ", overview='" + overview.substring(0,10)+"..." + '\'' +
                    '}';
        }
    }
}
