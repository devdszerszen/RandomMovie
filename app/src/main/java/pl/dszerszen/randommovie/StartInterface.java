package pl.dszerszen.randommovie;

import java.util.List;

import pl.dszerszen.randommovie.Base.BasePresenter;

public interface StartInterface {

    interface View{
        void showError(String message);
        void startDetailsActivity();
    }

    interface Presenter {
        void searchButtonClicked();
        void favouritesButtonClicked();
    }
}
