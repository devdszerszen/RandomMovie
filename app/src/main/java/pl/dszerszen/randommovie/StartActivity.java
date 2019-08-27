package pl.dszerszen.randommovie;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.dszerszen.randommovie.GSON.Genre;

import static android.view.View.GONE;

public class StartActivity extends AppCompatActivity implements StartInterface.View, StartActivityFilter {
    
    final String TAG = "Damian";

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.loader) ProgressBar loader;
    @BindView(R.id.randomButton) Button randomButton;
    @BindView(R.id.previousButton) Button previousButton;
    @BindView(R.id.details_title) TextView title;
    @BindView(R.id.details_desc) TextView desc;
    @BindView(R.id.details_poster) ImageView posterView;
    @BindView(R.id.details_layout) ConstraintLayout detailsLayout;
    @BindView(R.id.details_genres_layout) LinearLayout genresLayout;
    @BindView(R.id.details_time_layout) LinearLayout timeLayout;
    @BindView(R.id.details_time_value) TextView timeValue;
    @BindView(R.id.details_rating) TextView rating;
    @BindView(R.id.tmdb_image) ImageView tmdbImage;
    ActionBar actionBar;

    private StartInterface.Presenter presenter;

    //Recycler view
    List<Genre> recyclerList = new ArrayList<>();
    RecyclerAdapter adapter;

    //Filters
    FilterData filterData;

    //Previous movie
    ArrayList<Integer> previousMovies = new ArrayList<>();
    boolean isPreviousMovie = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        setupActionBar();
        String languageKey = getResources().getString(R.string.language_key);
        presenter = new StartPresenter(this, languageKey);
        this.filterData = new FilterData();
        detailsLayout.setVisibility(GONE);
        tmdbImage.setVisibility(View.VISIBLE);
        presenter.getGenresList();
    }

    public void setupActionBar() {
        actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @OnClick(R.id.randomButton)
    public void getRandomMovie() {
        isPreviousMovie = true;
        startLoader();
        presenter.getRandomMovie(500, filterData);
    }

    public void startLoader() {
        loader.setVisibility(View.VISIBLE);
        detailsLayout.setVisibility(GONE);
        randomButton.setClickable(false);
        previousButton.setClickable(false);
    }
    public void stopLoader() {
        loader.setVisibility(GONE);
        detailsLayout.setVisibility(View.VISIBLE);
        randomButton.setClickable(true);
        previousButton.setClickable(true);
    }

    @Override
    public void showRandomMovie(SingleMovieDetails movie) {
        StartActivity.this.runOnUiThread(() -> {
            stopLoader();
            populateMovieView(movie);
            previousMovies.add(movie.id);

//            // Handling previous movie - approach 1
//            if (previousMovies.size()==0) {
//                previousMovies.add(movie.id);
//            } else {
//                if (movie.id != previousMovies.get(previousMovies.size()-1)) {
//                    previousMovies.add(movie.id);
//                }
//            }
            setPreviousMovieButton();
        });
    }

    private void setPreviousMovieButton() {
        if (previousMovies.size()>1 && isPreviousMovie) {
            previousButton.setVisibility(View.VISIBLE);
        } else {
            previousButton.setVisibility(GONE);
        }
    }

    @OnClick(R.id.previousButton)
    public void onPreviousButtonClicked() {
//        //Approach 1
//        Log.d(TAG, "PREV: ----------- clicked PREVIOUS btn -------------------");
//        currentMovie--;
//        Log.d(TAG, "PREV: currentMovie value is " + currentMovie);
//        for (int i = currentMovie+1;i<previousMovies.size();i++) {
//            Log.d(TAG, "PREV: before deleting list size is: " + previousMovies.size());
//            Log.d(TAG, "PREV: deleted movie on index:" + i);
//            previousMovies.remove(i);
//            Log.d(TAG, "PREV: after deleting list size is: " + previousMovies.size());
//            for (int id: previousMovies) {
//                Log.d(TAG, "PREV: list item is: " + id);
//            }
//        }
//        presenter.getMovieDetails(previousMovies.get(currentMovie));
//        currentMovie--;
//        Log.d(TAG, "PREV: currentMovie value is " + currentMovie);
        isPreviousMovie = false;
        presenter.getMovieDetails(previousMovies.get(previousMovies.size()-2));
    }

    public void populateMovieView(SingleMovieDetails movie) {
        tmdbImage.setVisibility(GONE);
        if (movie != null) {

            // Picture
            if (movie.backdropPath != null) {
                Glide.with(this).load(Api.getImageUrl()+movie.backdropPath).into(posterView);
            }

            //Title
            title.setText(movie.title);
            detailsLayout.bringChildToFront(title);

            //Description
            desc.setText(movie.overview);

            //Genres
            genresLayout.removeAllViews();
            for (int i = 0; i<movie.genres.size(); i++) {
                TextView tmpTxtView = new TextView(this);
                tmpTxtView.setTextColor(Color.WHITE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                tmpTxtView.setLayoutParams(params);
                tmpTxtView.setTextSize(14f);
                tmpTxtView.setGravity(Gravity.CENTER);
                tmpTxtView.setText(movie.genres.get(i).name.toLowerCase());
                genresLayout.addView(tmpTxtView);
            }

            //Time
            timeValue.setText(String.valueOf(movie.runtime)+ " min");

            //Rating
            rating.setText(String.valueOf(movie.voteAverage));
        }
    }

    @Override
    public void showError(String message) {
        StartActivity.this.runOnUiThread(() -> Toast.makeText(StartActivity.this, message, Toast.LENGTH_SHORT).show());
    }


    // Used to get genres list


    @Override
    public void populateGenresList(List<Genre> genres) {
        StartActivity.this.runOnUiThread(() -> {
            recyclerList = genres;
        });

    }

    public void onFilterIconClicked (MenuItem item) {
        FiltersDialog dialog = new FiltersDialog(this, recyclerList, filterData);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        dialog.getWindow().setLayout(width, height);
    }


    @Override
    public void onFiltersSaved(FilterData filterData) {
        this.filterData = filterData;
        Toast.makeText(this, getResources().getString(R.string.toast_filter_ok), Toast.LENGTH_SHORT).show();
    }
}
