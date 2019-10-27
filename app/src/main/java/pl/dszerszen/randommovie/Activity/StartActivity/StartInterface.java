package pl.dszerszen.randommovie.Activity.StartActivity;

import android.content.Intent;

public interface StartInterface {

    interface View{
        void showToast(String message);
        void startDetailsActivity();
        void showLoginPrompt(Intent intent);
        void startFavListActivity();
    }

    interface Presenter {
        void searchButtonClicked();
        void favouritesButtonClicked();
        void loginToFirebaseWithSelectedGoogleAccount(Intent data);
    }
}
