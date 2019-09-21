package pl.dszerszen.randommovie;

public class FilterData {
    private int genreId = -1;
    public int genrePosition = -1;
    public String genreName = null;

    private int minYear = -1;
    private int maxYear = -1;

    private int minRuntime = -1;
    private int maxRunTime = -1;

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

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }
}
