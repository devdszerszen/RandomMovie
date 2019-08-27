package pl.dszerszen.randommovie;

class FilterData {
    public int genreId = -1;
    public int genrePosition = -1;
    public int minYear = -1;
    public int maxYear = -1;

    public boolean isSet() {
        if (genreId>-1 || minYear>-1 || maxYear>-1)
            return true;
        return false;
    }
}
