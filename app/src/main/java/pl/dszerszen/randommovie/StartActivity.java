package pl.dszerszen.randommovie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

import static android.view.View.GONE;

public class StartActivity extends AppCompatActivity implements StartInterface.View{
    
    final String TAG = "RandomMovie_log";

    @BindView(R.id.start_search_btn) Button randomButton;
    @BindView(R.id.start_favourites_btn) Button favorites;
    @BindView(R.id.start_tmdb_image) ImageView tmdbImage;
    ActionBar actionBar;

    private StartInterface.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Dagger
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Butterknife
        ButterKnife.bind(this);

        //ActionBar
        setupActionBar();

        //Presenter
        presenter = new StartPresenter(this);

        // Favourites button
        favorites.setEnabled(false);
        favorites.setClickable(false);

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
    public void showError(String message) {
        StartActivity.this.runOnUiThread(() -> Toast.makeText(StartActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void startDetailsActivity() {
        Intent intent = new Intent(this,MovieDetailsActivity.class);
        SingleMovieDetails details = new SingleMovieDetails();
        startActivity(intent);
    }

    @OnClick(R.id.start_search_btn)
    public void onDetailsButtonClicked() {
        presenter.searchButtonClicked();
    }

}
