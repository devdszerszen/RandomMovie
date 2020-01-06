package pl.dszerszen.randommovie.Activity.MovieDetailsActivity;

import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.dszerszen.randommovie.Base.BaseActivity;
import pl.dszerszen.randommovie.Filter.FilterData;
import pl.dszerszen.randommovie.Filter.FiltersDialog;
import pl.dszerszen.randommovie.R;
import pl.dszerszen.randommovie.Network.ResponseGenre;
import pl.dszerszen.randommovie.Network.SingleMovieDetails;
import pl.dszerszen.randommovie.Activity.StartActivity.StartActivityFilter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    Menu menu;

    //Genres genresList
    List<ResponseGenre.Genre> genres = new ArrayList<>();

    //Filters
    FilterData filterData;

    //Current movie
    SingleMovieDetails currentMovie;

    boolean isSetAsFavourite = false;
    int firstMovieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_movie_details);

        //Filter data
        this.filterData = FilterData.getInstance();

        //Presenter
        presenter = new MovieDetailsPresenter(this);

        //Fragment
        detailsFragment = MovieDetailsFragment.newInstance();
        setFragment(detailsFragment);
        firstMovieId = getIntent().getIntExtra("MOVIE_ID",-1);

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
        this.menu = menu;
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
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.80);
        dialog.getWindow().setLayout(width, height);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_favIcon) {
            if (!detailsFragment.errorInfoShowed)
            presenter.onFavIconClicked(currentMovie, isSetAsFavourite);
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void setMovieAsFavourite(boolean isFavourite) {
        isSetAsFavourite = isFavourite;
        if (isFavourite) menu.findItem(R.id.menu_favIcon).setIcon(android.R.drawable.btn_star_big_on);
        else menu.findItem(R.id.menu_favIcon).setIcon(android.R.drawable.btn_star_big_off);
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
    public void showMessage(String message) {
        runOnUiThread(() -> showToastMessage(message));
    }

    @Override
    public void showLoginPrompt() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_login);
        dialog.setCancelable(false);
        dialog.getWindow().setDimAmount(0.9f);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.filter_background);
        dialog.show();

        Button positive = dialog.findViewById(R.id.login_btn_pos);
        Button negative = dialog.findViewById(R.id.login_btn_neg);

        negative.setOnClickListener(v -> {
            dialog.dismiss();
        });
        positive.setOnClickListener(v -> {
            backToStartActivityWithLoginPrompt();
            dialog.dismiss();
        });
    }

    @Override
    public void backToStartActivityWithLoginPrompt() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("MOVIE_ID",currentMovie.id);
        setResult(Activity.RESULT_FIRST_USER,returnIntent);
        finish();
    }

    @Override
    public void showNetworkError() {
        detailsFragment.showNetworkError();
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

    @Override
    public void getFirstMovie() {
        if (firstMovieId > -1) {
            getMovieDetails(firstMovieId);
        } else {
            getRandomMovie();
        }
    }
}
