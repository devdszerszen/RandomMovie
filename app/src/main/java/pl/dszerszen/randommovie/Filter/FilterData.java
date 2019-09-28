package pl.dszerszen.randommovie.Filter;

import android.util.Log;

import java.util.Calendar;
import java.util.Map;

public class FilterData {
    final String TAG = "RandomMovie_log";

    final int F_GENRE_ID = -1;
    final int F_GENRE_POS = -1;

    final int F_MIN_YEAR = 1980;
    final int F_MAX_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    final int F_MIN_VOTE = 0;
    final int F_MAX_VOTE = 10;

    //Genre
    public boolean genreFilterEnabled = false;
    public int genreId = F_GENRE_ID;
    public int genrePosition = F_GENRE_POS;
    public String genreName = null;

    //Years
    public boolean yearsFilterEnabled = false;
    public int minYear = F_MIN_YEAR;
    public int maxYear = F_MAX_YEAR;

    //Votes
    public boolean voteFilterEnabled = false;
    public int minVote = F_MIN_VOTE;
    public int maxVote = F_MAX_VOTE;

    public enum FilterType {
        GENRE,
        YEAR,
        RUNTIME,
        VOTE
    }




    private static FilterData filterDataInstance = null;

    public static FilterData getInstance() {
        if (filterDataInstance == null) {
            filterDataInstance = new FilterData();
        }
        return filterDataInstance;
    }


    public boolean isSet() {
        if (genreId>-1 || minYear>-1 || maxYear>-1)
            return true;
        return false;
    }

    public String getMinYear() {
        String result = null;
        if (minYear > F_MIN_YEAR)
            result = Integer.toString(minYear)+"-01-01";
        return result;
    }

    public String getMaxYear() {
        String result = null;
        if (maxYear < F_MAX_YEAR)
            result = Integer.toString(maxYear)+"-12-31";
        return result;
    }

    public String getGenreId() {
        String result = null;
        if (genreId > F_GENRE_ID)
            result = Integer.toString(genreId);
        return result;
    }

    public int getGenrePosition() {
        return genrePosition;
    }

    public void setFilter (SingleFilter singleFilter, boolean filterEnabled) {

        if (singleFilter != null) {
            if (singleFilter.type.equals(FilterType.GENRE)) {
                this.genreFilterEnabled = filterEnabled;
                this.genreId = singleFilter.genreId;
                this.genrePosition = singleFilter.genrePosition;
                this.genreName = singleFilter.genreName;
            } else if (singleFilter.type.equals(FilterType.VOTE)){
                this.voteFilterEnabled = filterEnabled;
                this.minVote = singleFilter.min;
                this.maxVote = singleFilter.max;
            } else if (singleFilter.type.equals(FilterType.YEAR)){
                this.yearsFilterEnabled = filterEnabled;
                this.minYear = singleFilter.min;
                this.maxYear = singleFilter.max;
            }
        }


    }
}
