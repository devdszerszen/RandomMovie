package pl.dszerszen.randommovie;

import android.content.Intent;

import java.util.List;

import pl.dszerszen.randommovie.Base.BasePresenter;

public interface StartInterface {

    interface View{
        void showToast(String message);
        void startDetailsActivity();
        void showLoginPrompt(Intent intent);
    }

    interface Presenter {
        void searchButtonClicked();
        void favouritesButtonClicked();
        void loginToFirebaseWithSelectedGoogleAccount(Intent data);
    }
}
