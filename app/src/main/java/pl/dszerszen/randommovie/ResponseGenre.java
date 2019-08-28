package pl.dszerszen.randommovie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResponseGenre implements Serializable {

    @SerializedName("genres")
    @Expose
    public List<Genre> genres = null;


    class Genre implements Serializable {

        @SerializedName("id")
        @Expose
        public int id;
        @SerializedName("name")
        @Expose
        public String name;
    }
}
