package pl.dszerszen.randommovie.Activity.StartActivity;

import android.content.Intent;

import java.util.ArrayList;

import pl.dszerszen.randommovie.Carousel.CarouselMoviePOJO;

public interface StartInterface {

    interface View{
        void showToast(String message);
        void startDetailsActivity();
        void showLoginPrompt(Intent intent);
        void startFavListActivity();
        void setPostersList(ArrayList<CarouselMoviePOJO> postersUriList);
    }

    interface Presenter {
        void searchButtonClicked();
        void favouritesButtonClicked();
        void loginToFirebaseWithSelectedGoogleAccount(Intent data);
    }
}
