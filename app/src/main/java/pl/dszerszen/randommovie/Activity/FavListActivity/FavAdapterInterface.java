package pl.dszerszen.randommovie.Activity.FavListActivity;

public interface FavAdapterInterface {
    void deleteMovie(int id);
    void showNoResultsMessage();
    void startDetailsActivity(int id);
}
