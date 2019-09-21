package pl.dszerszen.randommovie;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;
import pl.dszerszen.randommovie.Base.BaseActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MovieDetailsActivity extends BaseActivity implements MovieDetailsInterface.View,
        MovieDetailsFragment.OnFragmentInteractionListener, StartActivityFilter {

    final String TAG = "RandomMovie_log";

    MovieDetailsFragment detailsFragment;
    MovieDetailsInterface.Presenter presenter;

    ActionBar actionBar;

    //Genres genresList
    List<ResponseGenre.Genre> genres = new ArrayList<>();

    //Filters
    FilterData filterData;

    //Previous movie
    ArrayList<Integer> previousMovies = new ArrayList<>();
    boolean isPreviousMovie = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Filter data
        this.filterData = FilterData.getInstance();

        //Presenter
        presenter = new MovieDetailsPresenter(this);

        //Fragment
        detailsFragment = MovieDetailsFragment.newInstance();
        setFragment(detailsFragment);

        //ActionBar
        setupActionBar();

        //Genres genresList
        presenter.getGenresList();
    }

    private void setupActionBar() {
        actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    public void onFilterIconClicked (MenuItem item) {
        FiltersDialog dialog = new FiltersDialog(this, genres);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        dialog.getWindow().setLayout(width, height);
    }

    @Override
    public void onFiltersSaved(FilterData filterData) {
        this.filterData = filterData;
        Toast.makeText(this, getResources().getString(R.string.toast_filter_ok), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showMovie(SingleMovieDetails movieDetails) {
        detailsFragment.showMovieDetails(movieDetails);
    }

    @Override
    public void showLoader() {
        detailsFragment.startLoader();
    }

    @Override
    public void hideLoader() {
        detailsFragment.stopLoader();
    }

    @Override
    public void saveGenresList(List<ResponseGenre.Genre> genresList) {
        this.genres = genresList;
    }

    @Override
    public void getRandomMovie() {
        presenter.getRandomMovie(filterData,500);
    }

    @Override
    public void getMovieDetails(int id) {
        presenter.getMovieDetails(id);
    }


}
