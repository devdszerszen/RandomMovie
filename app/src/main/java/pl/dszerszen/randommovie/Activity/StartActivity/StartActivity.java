package pl.dszerszen.randommovie.Activity.StartActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import pl.dszerszen.randommovie.Activity.FavListActivity.FavListActivity;
import pl.dszerszen.randommovie.Activity.MovieDetailsActivity.MovieDetailsActivity;
import pl.dszerszen.randommovie.Carousel.CarouselAdapter;
import pl.dszerszen.randommovie.Carousel.CarouselMoviePOJO;
import pl.dszerszen.randommovie.R;
import pl.dszerszen.randommovie.Network.SingleMovieDetails;

public class StartActivity extends AppCompatActivity implements StartInterface.View{
    
    final String TAG = "RandomMovie_log";
    final int RC_LOGIN = 187;

    @BindView(R.id.start_search_btn) Button randomButton;
    @BindView(R.id.start_fav_btn) Button favorites;
    @BindView(R.id.start_tmdb_image) ImageView tmdbImage;
    @BindView(R.id.start_carousel_frame) FrameLayout carouselFrame;
    FeatureCoverFlow carousel;
    ActionBar actionBar;

    private StartInterface.Presenter presenter;
    private CarouselAdapter carouselAdapter;

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
    public void setPostersList(ArrayList<CarouselMoviePOJO> postersUriList) {
        carousel = new FeatureCoverFlow(StartActivity.this);
        carousel.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        int coverHeight = 400;
        int coverWidth = Math.toIntExact(Math.round(coverHeight*0.66));
        carousel.setCoverHeight(coverHeight);
        carousel.setCoverWidth(coverWidth);
        carousel.setMaxScaleFactor(1.7f);
        carousel.setSpacing(0.6f);
        carousel.setRotationTreshold(0.4f);
        carousel.setScalingThreshold(0.3f);
        carouselFrame.addView(carousel);
        carouselAdapter = new CarouselAdapter(this, postersUriList);
        carousel.setAdapter(carouselAdapter);
    }

    @Override
    public void showLoginPrompt(Intent intent) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_login);
        dialog.setCancelable(false);
        dialog.show();

        Button positive = dialog.findViewById(R.id.login_btn_pos);
        Button negative = dialog.findViewById(R.id.login_btn_neg);

        negative.setOnClickListener(v -> {
            dialog.dismiss();
        });
        positive.setOnClickListener(v -> {
            startActivityForResult(intent, RC_LOGIN);
            dialog.dismiss();
        });


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
