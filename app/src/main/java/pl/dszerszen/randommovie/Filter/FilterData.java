package pl.dszerszen.randommovie.Filter;

import android.util.Log;

import java.util.Map;

public class FilterData {
    final String TAG = "RandomMovie_log";

    public int genreId = -1;
    public int genrePosition = -1;
    public String genreName = null;

    public int minYear = -1;
    public int maxYear = -1;

    public int minRuntime = -1;
    public int maxRunTime = -1;

    public int minVote = -1;
    public int maxVote = -1;

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
        if (minYear>-1)
            result = Integer.toString(minYear)+"-01-01";
        return result;
    }

    public String getMaxYear() {
        String result = null;
        if (maxYear>-1)
            result = Integer.toString(maxYear)+"-12-31";
        return result;
    }

    public String getGenreId() {
        String result = null;
        if (genreId>-1)
            result = Integer.toString(genreId);
        return result;
    }

    public int getGenrePosition() {
        return genrePosition;
    }

    public String getMinRuntime() {
        String result = null;
        if (minRuntime>-1)
            result = Integer.toString(minRuntime);
        return result;
    }

    public String getMaxRunTime() {
        String result = null;
        if (maxRunTime>-1)
            result = Integer.toString(maxRunTime);
        return result;
    }


    public void setFilter (SingleFilter singleFilter) {

        if (singleFilter.type.equals(FilterType.GENRE)) {
            this.genreId = singleFilter.genreId;
            this.genrePosition = singleFilter.genrePosition;
            this.genreName = singleFilter.genreName;
            Log.d(TAG, "GENREFILTER: Filter data saves genreId: " + singleFilter.genreId);
        } else if (singleFilter.type.equals(FilterType.RUNTIME)){
            this.minRuntime = singleFilter.min;
            this.maxRunTime = singleFilter.max;
        } else if (singleFilter.type.equals(FilterType.VOTE)){
            this.minVote = singleFilter.min;
            this.maxVote = singleFilter.max;
        } else if (singleFilter.type.equals(FilterType.YEAR)){
            this.minYear = singleFilter.min;
            this.maxYear = singleFilter.max;
        }
    }
}
