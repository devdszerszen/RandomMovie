package pl.dszerszen.randommovie;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    final String TAG = "Damian";

    @BindView(R.id.details_title) TextView title;
    @BindView(R.id.details_desc) TextView desc;
    @BindView(R.id.details_poster) ImageView posterView;
    @BindView(R.id.details_layout) ConstraintLayout detailsLayout;
    ActionBar actionBar;

    SingleMovieDetails movie;
    Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        actionBar = getSupportActionBar();
        api = new Api();
        getMovieData();
        showMovieInfo();
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
                Log.d(TAG, "showMovieInfo: path is: "+ api.getImageUrl()+movie.backdropPath);
                Glide.with(this).load(api.getImageUrl()+movie.backdropPath).into(posterView);
            }
        title.setText(movie.title);
        desc.setText(movie.overview);
        detailsLayout.bringChildToFront(title);
        }
    }
}
