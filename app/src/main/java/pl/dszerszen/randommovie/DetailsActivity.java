package pl.dszerszen.randommovie;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity implements DetailsInterface.View {

    DetailsInterface.Presenter presenter;

    final String TAG = "Damian";

    @BindView(R.id.details_title) TextView title;
    @BindView(R.id.details_desc) TextView desc;
    @BindView(R.id.details_poster) ImageView posterView;
    @BindView(R.id.details_layout) ConstraintLayout detailsLayout;
    @BindView(R.id.details_genres_layout) LinearLayout genresLayout;
    @BindView(R.id.details_time_layout) LinearLayout timeLayout;
    @BindView(R.id.details_time_value) TextView timeValue;
    @BindView(R.id.details_rating) TextView rating;
    ActionBar actionBar;

    SingleMovieDetails movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        actionBar = getSupportActionBar();
        presenter = new DetailsPresenter(this);
        setBackground();
        getMovieData();
        showMovieInfo();
    }

    public void setBackground() {
        View root = timeValue.getRootView();
        root.setBackgroundColor(Color.BLACK);
    }

    public void getMovieData() {
        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            movie = (SingleMovieDetails)extras.get("movie");
        }
    }

    public void showMovieInfo() {
        if (movie != null) {
            if (movie.backdropPath != null) {
                Log.d(TAG, "showMovieInfo: path is: "+ Api.getImageUrl()+movie.backdropPath);
                Glide.with(this).load(Api.getImageUrl()+movie.backdropPath).into(posterView);
            }
        title.setText(movie.title);
        desc.setText(movie.overview);
        detailsLayout.bringChildToFront(title);

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
        timeValue.setText(String.valueOf(movie.runtime)+ " min");
        rating.setText(String.valueOf(movie.voteAverage));
        detailsLayout.bringChildToFront(rating);
        }
    }

    @Override
    public void showNewMovie(SingleMovieDetails movie) {
        DetailsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setNewMovie(movie);
                genresLayout.removeAllViews();
                showMovieInfo();
            }
        });

    }

    @Override
    public void reportError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.details_random_movie_btn)
    public void getNextMovie() {
        presenter.changeMovie();
    }

    private void setNewMovie(SingleMovieDetails movie) {
        this.movie = movie;
    }
}
