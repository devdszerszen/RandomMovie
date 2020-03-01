package pl.dszerszen.randommovie.Activity.MovieDetailsActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.dszerszen.randommovie.Activity.StartActivity.StartActivityFilter;
import pl.dszerszen.randommovie.Base.BaseActivity;
import pl.dszerszen.randommovie.BuildConfig;
import pl.dszerszen.randommovie.CustomViews.LoadingView;
import pl.dszerszen.randommovie.Error.ErrorType;
import pl.dszerszen.randommovie.EventBus.ShowMessageEvent;
import pl.dszerszen.randommovie.Filter.FilterData;
import pl.dszerszen.randommovie.Filter.FiltersDialog;
import pl.dszerszen.randommovie.MessageCode;
import pl.dszerszen.randommovie.Network.ResponseGenre;
import pl.dszerszen.randommovie.Network.SingleMovieDetails;
import pl.dszerszen.randommovie.R;

import static android.view.View.GONE;


public class MovieDetailsActivity extends BaseActivity implements MovieDetailsInterface.View,
        MovieDetailsFragment.OnFragmentInteractionListener, StartActivityFilter {

    MovieDetailsFragment detailsFragment;
    MovieDetailsInterface.Presenter presenter;

    ActionBar actionBar;
    TextView notificationBadge;
    Menu menu;
    @BindView(R.id.movie_activity_layout) ConstraintLayout mainLayout;
    @BindView(R.id.movie_errorLayout) ConstraintLayout errorLayout;
    @BindView(R.id.error_desc) TextView errorDescription;
    @BindView(R.id.movie_new_loader) LoadingView loader;
    @BindView(R.id.df_previous_btn) Button previousButton;

    //Genres genresList
    List<ResponseGenre.Genre> genres = new ArrayList<>();

    //Filters
    FilterData filterData;

    //Current movie
    SingleMovieDetails currentMovie;

    //Previous movies
    ArrayList<Integer> previousMovies = new ArrayList<>();

    private boolean isSetAsFavourite = false;
    private boolean errorInfoShowed = false;
    private boolean isPreviousMovie = false;
    int firstMovieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        //Loader
        showLoader();

        //Filter data
        this.filterData = FilterData.getInstance();

        //Presenter
        presenter = new MovieDetailsPresenter(this);

        //Fragment
        detailsFragment = MovieDetailsFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentMovieLayout, detailsFragment);
        fragmentTransaction.commit();

        firstMovieId = getIntent().getIntExtra("MOVIE_ID",-1);

        //ActionBar
        setupActionBar();

        //Genres genresList
        presenter.getGenresList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void setupActionBar() {
        actionBar = getSupportActionBar();
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
            if (!errorInfoShowed)
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
    public void saveGenresList(List<ResponseGenre.Genre> genresList) {
        this.genres = genresList;
    }

    @Override
    public void onFiltersSaved() {
        this.filterData = FilterData.getInstance();
        setupBadge();
    }

    @Subscribe
    public void showMessage(ShowMessageEvent event) {
        switch (event.msgType) {
            case FILTER_CLEARED: showToast(getString(R.string.toast_filter_cleared)); break;
            case FILTER_SAVED: showToast(getString(R.string.toast_filter_ok)); break;
        }
    }

    @Override
    public void showMovie(SingleMovieDetails movieDetails) {
        currentMovie = movieDetails;
        detailsFragment.showMovieDetails(movieDetails);
        previousMovies.add(movieDetails.id);
        setPreviousMovieButton();
    }

    @Override
    public void showLoader() {
        mainLayout.setVisibility(GONE);
        loader.showLoader();
    }

    @Override
    public void showToast(String message) {
        runOnUiThread(() -> showToastMessage(message));
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int code) {
        switch (code) {
            case MessageCode.MOVIE_ADDED_FAV: showToast(getString(R.string.movie_fav_toast)); break;
            case MessageCode.MOVIE_DELETED_FAV: showToast(getString(R.string.movie_deleted_fav_toast)); break;
        }
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

        negative.setOnClickListener(v -> dialog.dismiss());
        positive.setOnClickListener(v -> {
            backToStartActivityWithLoginPrompt();
            dialog.dismiss();
        });
    }

    @Override
    public void showError(ErrorType errorType) {
        errorInfoShowed = true;
        loader.hideLoader();
        mainLayout.setVisibility(GONE);
        errorLayout.setVisibility(View.VISIBLE);
        switch (errorType) {
            case NETWORK: errorDescription.setText(getString(R.string.error_msg_network));
            case LACK_OF_RESULT: errorDescription.setText(getString(R.string.error_msg_lack_of_results));
        }
    }

    @Override
    public void hideLoader() {
        loader.hideLoader();
        mainLayout.setVisibility(View.VISIBLE);
    }

    private void hideNetworkError() {
        errorInfoShowed = false;
        mainLayout.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(GONE);
    }

    @Override
    public void backToStartActivityWithLoginPrompt() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("MOVIE_ID",currentMovie.id);
        setResult(Activity.RESULT_FIRST_USER,returnIntent);
        finish();
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

    @OnClick(R.id.df_random_btn)
    public void onNextButtonClicked() {
        if (errorInfoShowed) hideNetworkError();
        isPreviousMovie = true;
        presenter.onRandomMovieButtonClicked();
    }

    @OnClick(R.id.df_previous_btn)
    public void onPreviousButtonClicked() {
        if (errorInfoShowed) hideNetworkError();
        isPreviousMovie = false;
        getMovieDetails(previousMovies.get(previousMovies.size()-2));
    }

    private void setPreviousMovieButton() {
        if (previousMovies.size()>1 && isPreviousMovie) {
            previousButton.setVisibility(View.VISIBLE);
        } else {
            previousButton.setVisibility(GONE);
        }
    }

    @Override
    public void initGoogleAds() {
        MobileAds.initialize(this, getString(R.string.google_ads_id));
        AdView adView = findViewById(R.id.adView);
        AdRequest.Builder adRequest = new AdRequest.Builder();
        if (BuildConfig.DEBUG) {
            adRequest.addTestDevice("94D4F809E44EC23AAB4C3005DE1B5130");
        }
        adView.loadAd(adRequest.build());
    }

    @Override
    public void notifyImageReady() {
        hideLoader();
    }

    @Override
    public void notifyError() {
        showError(ErrorType.NETWORK);
    }
}
