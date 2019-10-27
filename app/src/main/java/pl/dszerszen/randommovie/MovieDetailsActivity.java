package pl.dszerszen.randommovie;

import androidx.appcompat.app.ActionBar;
import pl.dszerszen.randommovie.Base.BaseActivity;
import pl.dszerszen.randommovie.Filter.FilterData;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;


public class MovieDetailsActivity extends BaseActivity implements MovieDetailsInterface.View,
        MovieDetailsFragment.OnFragmentInteractionListener, StartActivityFilter {

    final String TAG = "RandomMovie_log";

    MovieDetailsFragment detailsFragment;
    MovieDetailsInterface.Presenter presenter;

    ActionBar actionBar;
    TextView notificationBadge;

    //Genres genresList
    List<ResponseGenre.Genre> genres = new ArrayList<>();

    //Filters
    FilterData filterData;

    //Current movie
    SingleMovieDetails currentMovie;

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

        //Filter
        MenuItem filterAction = menu.getItem(0);
        View filterActionView = filterAction.getActionView();
        notificationBadge = filterActionView.findViewById(R.id.cart_badge);
        setupBadge();
        filterActionView.setOnClickListener(v -> onFilterIconClicked());

        return true;
    }


    public void onFilterIconClicked () {
        FiltersDialog dialog = new FiltersDialog(this, genres);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        dialog.getWindow().setLayout(width, height);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_favIcon) {
            presenter.addMovieToFavourities(currentMovie);
        }

        return true;
    }

    public void setupBadge() {
        int notificationsCount = filterData.getFiltersCount();
        if (notificationBadge!=null) {
            if (notificationsCount>0) {
                notificationBadge.setVisibility(View.VISIBLE);
                notificationBadge.setText(Integer.toString(notificationsCount));
            } else {
                notificationBadge.setVisibility(GONE);
            }
        }
    }

    @Override
    public void onFiltersSaved() {
        this.filterData = FilterData.getInstance();
        Toast.makeText(this, getResources().getString(R.string.toast_filter_ok), Toast.LENGTH_SHORT).show();
        setupBadge();
    }


    @Override
    public void showMovie(SingleMovieDetails movieDetails) {
        currentMovie = movieDetails;
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
    public void showErrorMessage(String message) {
        runOnUiThread(() -> showToastMessage(message));
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getRandomMovie() {
        presenter.onRandomMovieButtonClicked();
    }

    @Override
    public void getMovieDetails(int id) {
        presenter.getMovieDetails(id);
    }


}
