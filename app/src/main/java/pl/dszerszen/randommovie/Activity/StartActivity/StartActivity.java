package pl.dszerszen.randommovie.Activity.StartActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import pl.dszerszen.randommovie.Activity.FavListActivity.FavListActivity;
import pl.dszerszen.randommovie.Activity.InfoActivity.InfoActivity;
import pl.dszerszen.randommovie.Activity.MovieDetailsActivity.MovieDetailsActivity;
import pl.dszerszen.randommovie.Carousel.CarouselAdapter;
import pl.dszerszen.randommovie.Carousel.CarouselMoviePOJO;
import pl.dszerszen.randommovie.EventBus.CarouselReadyEvent;
import pl.dszerszen.randommovie.MessageCode;
import pl.dszerszen.randommovie.R;

public class StartActivity extends AppCompatActivity implements StartInterface.View{
    
    final public static int RC_LOGIN = 187;
    final static int RC_MOVIE = 188;

    @BindView(R.id.start_search_btn) Button randomButton;
    @BindView(R.id.start_fav_btn) Button favorites;
    @BindView(R.id.start_buttons_layout) ConstraintLayout buttonsLayout;
    @BindView(R.id.start_logo_image) ImageView logoImage;

    @BindView(R.id.start_carousel_frame) FrameLayout carouselFrame;
    Menu menu;
    FeatureCoverFlow carousel;
    ActionBar actionBar;

    private StartInterface.Presenter presenter;

    //Used in unlogged user use case - to show the same movie after logging in
    private int unloggedMovieId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Dagger
        //AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Butterknife
        ButterKnife.bind(this);

        //ActionBar
        setupActionBar();

        //Presenter
        presenter = new StartPresenter(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {
            Intent intent = new Intent(this,InfoActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public void setupActionBar() {
        actionBar = getSupportActionBar();
    }

    @Subscribe
    public void showCarousel(CarouselReadyEvent event) {
        logoImage.animate()
                .alpha(0.0f)
                .setDuration(400)
                .withEndAction(() -> {
                    carouselFrame.setVisibility(View.VISIBLE);
                    carouselFrame.setAlpha(0.0f);
                    carouselFrame.animate()
                            .alpha(1.0f)
                            .setDuration(500)
                            .withEndAction(() -> {
                            })
                            .start();
                }).start();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(StartActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int code) {
        switch (code) {
            case MessageCode.USER_LOGGED_OK:
                showToast(getString(R.string.user_logged_toast));
        }
    }

    @Override
    public void startDetailsActivity() {
        Intent intent = new Intent(this,MovieDetailsActivity.class);
        if (unloggedMovieId != -1) {
            intent.putExtra("MOVIE_ID",unloggedMovieId);
            unloggedMovieId = -1;
        }
        startActivityForResult(intent,RC_MOVIE);
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
        CarouselAdapter carouselAdapter = new CarouselAdapter(this, postersUriList, carousel);
        carousel.setAdapter(carouselAdapter);
    }

    @Override
    public void showLoginPrompt(Intent intent) {
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
            startActivityForResult(intent, RC_LOGIN);
            dialog.dismiss();
        });
    }

//    public void showTutorial(View view) {
//        new FancyShowCaseView.Builder(this)
//                .focusShape(FocusShape.ROUNDED_RECTANGLE)
//                .focusOn(randomButton)
//                .titleGravity(-1)
//                .title("Click to get movie")
//                .build()
//                .show();
//    }

    @Override
    public void showLoginPromptWithoutDialog(Intent intent) {
        startActivityForResult(intent, RC_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LOGIN) {
            presenter.loginToFirebaseWithSelectedGoogleAccount(data);
        }
        else if (requestCode == RC_MOVIE) {
            if (resultCode == Activity.RESULT_FIRST_USER) {
                if (data != null) {
                    unloggedMovieId = data.getExtras().getInt("MOVIE_ID");
                }
                presenter.showLoginPrompt(false);
            }

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
