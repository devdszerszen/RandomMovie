package pl.dszerszen.randommovie;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleMovieDetails implements Serializable {
    @Override
    public String toString() {
        return "SingleMovieDetails{" +
                "backdropPath='" + backdropPath + '\'' +
                ", genres=" + genres +
                ", overview='" + overview + '\'' +
                ", runtime=" + runtime +
                ", title='" + title + '\'' +
                '}';
    }

    @SerializedName("adult")
        @Expose
        public boolean adult;
        @SerializedName("backdrop_path")
        @Expose
        public String backdropPath;
        @SerializedName("belongs_to_collection")
        @Expose
        public Object belongsToCollection;
        @SerializedName("budget")
        @Expose
        public int budget;
        @SerializedName("genres")
        @Expose
        public List<Genre> genres = null;
        @SerializedName("homepage")
        @Expose
        public String homepage;
        @SerializedName("id")
        @Expose
        public int id;
        @SerializedName("imdb_id")
        @Expose
        public String imdbId;
        @SerializedName("original_language")
        @Expose
        public String originalLanguage;
        @SerializedName("original_title")
        @Expose
        public String originalTitle;
        @SerializedName("overview")
        @Expose
        public String overview;
        @SerializedName("popularity")
        @Expose
        public float popularity;
        @SerializedName("poster_path")
        @Expose
        public String posterPath;
        @SerializedName("production_companies")
        @Expose
        public List<ProductionCompany> productionCompanies = null;
        @SerializedName("production_countries")
        @Expose
        public List<ProductionCountry> productionCountries = null;
        @SerializedName("release_date")
        @Expose
        public String releaseDate;
        @SerializedName("revenue")
        @Expose
        public int revenue;
        @SerializedName("runtime")
        @Expose
        public int runtime;
        @SerializedName("spoken_languages")
        @Expose
        public List<SpokenLanguage> spokenLanguages = null;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("tagline")
        @Expose
        public String tagline;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("video")
        @Expose
        public boolean video;
        @SerializedName("vote_average")
        @Expose
        public float voteAverage;
        @SerializedName("vote_count")
        @Expose
        public int voteCount;

    public class Genre implements Serializable {

        @SerializedName("id")
        @Expose
        public int id;
        @SerializedName("name")
        @Expose
        public String name;

    }

    public class ProductionCompany implements Serializable {

        @SerializedName("id")
        @Expose
        public int id;
        @SerializedName("logo_path")
        @Expose
        public String logoPath;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("origin_country")
        @Expose
        public String originCountry;

    }

    public class ProductionCountry implements Serializable {

        @SerializedName("iso_3166_1")
        @Expose
        public String iso31661;
        @SerializedName("name")
        @Expose
        public String name;

    }

    public class SpokenLanguage implements Serializable {

        @SerializedName("iso_639_1")
        @Expose
        public String iso6391;
        @SerializedName("name")
        @Expose
        public String name;

    }
}
