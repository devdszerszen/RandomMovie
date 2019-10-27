package pl.dszerszen.randommovie.Activity.StartActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import pl.dszerszen.randommovie.Activity.FavListActivity.FavListActivity;
import pl.dszerszen.randommovie.Activity.MovieDetailsActivity.MovieDetailsActivity;
import pl.dszerszen.randommovie.R;
import pl.dszerszen.randommovie.Network.SingleMovieDetails;

public class StartActivity extends AppCompatActivity implements StartInterface.View{
    
    final String TAG = "RandomMovie_log";
    final int RC_LOGIN = 187;

    @BindView(R.id.start_search_btn) Button randomButton;
    @BindView(R.id.start_fav_btn) Button favorites;
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
    public void showToast(String message) {
        Toast.makeText(StartActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startDetailsActivity() {
        Intent intent = new Intent(this,MovieDetailsActivity.class);
        SingleMovieDetails details = new SingleMovieDetails();
        startActivity(intent);
    }

    @Override
    public void startFavListActivity() {
        Intent intent = new Intent(this,FavListActivity.class);
        startActivity(intent);
    }

    @Override
    public void showLoginPrompt(Intent intent) {
        startActivityForResult(intent, RC_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LOGIN) {
            presenter.loginToFirebaseWithSelectedGoogleAccount(data);
        }
    }

    @OnClick(R.id.start_search_btn)
    public void onDetailsButtonClicked() {
        presenter.searchButtonClicked();
    }

    @OnClick(R.id.start_fav_btn)
    public void onFavouritesButtonClicked() {
        presenter.favouritesButtonClicked();
    }

}
