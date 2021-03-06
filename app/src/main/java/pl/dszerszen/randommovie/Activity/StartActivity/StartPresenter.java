package pl.dszerszen.randommovie.Activity.StartActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.util.ArrayList;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pl.dszerszen.randommovie.Base.BasePresenter;
import pl.dszerszen.randommovie.Carousel.CarouselMoviePOJO;
import pl.dszerszen.randommovie.Dagger.MyApplication;
import pl.dszerszen.randommovie.Firebase.AuthManager;
import pl.dszerszen.randommovie.Firebase.DatabaseManager;
import pl.dszerszen.randommovie.Firebase.FirebaseAuthInterface;
import pl.dszerszen.randommovie.Firebase.FirebaseDBInterface;
import pl.dszerszen.randommovie.MessageCode;
import pl.dszerszen.randommovie.Network.ResponseMovieList;
import pl.dszerszen.randommovie.Network.TmdbConnector;
import pl.dszerszen.randommovie.R;

public class StartPresenter extends BasePresenter implements StartInterface.Presenter, Serializable {


    private final int postersCount = 8;

    private StartInterface.View view;
    private TmdbConnector connector;
    private FirebaseAuthInterface firebaseAuth;
    private FirebaseDBInterface firebaseDatabase;
    private ArrayList<CarouselMoviePOJO> postersList = new ArrayList<>();

    public StartPresenter(StartInterface.View view) {
        this.view = view;
        this.connector = new TmdbConnector(MyApplication.getContext().getResources().getString(R.string.language_key));
        this.firebaseAuth = AuthManager.getInstance((Context) view);
        this.firebaseDatabase = DatabaseManager.getInstance();

        if (isUserLogged()) {
            firebaseDatabase.incrementCounter();
        }
    }

    @Override
    public void searchButtonClicked() {
        view.startDetailsActivity();
    }

    @SuppressLint("CheckResult")
    @Override
    public void favouritesButtonClicked() {
        if (isUserLogged()) {
            view.startFavListActivity();
        } else {
            showLoginPrompt(true);
        }
    }

    @Override
    public void showLoginPrompt(Boolean withDialog) {
        if (withDialog) {
            view.showLoginPrompt(firebaseAuth.getSignInIntent());
        } else {
            view.showLoginPromptWithoutDialog(firebaseAuth.getSignInIntent());
        }

    }

    //Used when user selects google account to be logged in
    @Override
    public void loginToFirebaseWithSelectedGoogleAccount(Intent data) {
        view.showGoogleLoginLoader();
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult(ApiException.class);
            loginToFirebase(account);
            firebaseAuth.updateGoogleAccount(account);
        } catch (ApiException e) {
            view.hideGoogleLoginLoader();
            view.showToast(MessageCode.USER_LOGIN_ERROR);
        }
    }

    @SuppressLint("CheckResult")
    private void loginToFirebase(GoogleSignInAccount account) {
        firebaseAuth.loginToFirebase(account).subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                firebaseDatabase.addUser(firebaseAuth.getLoggedAccount());
                view.hideGoogleLoginLoader();
                view.showToast(MessageCode.USER_LOGGED_OK);
            }

            @Override
            public void onError(Throwable e) {
                view.hideGoogleLoginLoader();
                view.showToast(MessageCode.USER_LOGIN_ERROR);
            }
        });
    }

    private boolean isUserLogged() {
        return firebaseAuth.isUserSignedToGoogle() && firebaseAuth.isUserSignedToFirebase();
    }

    @Override
    @SuppressLint("CheckResult")
    public void showPosters() {

        if (postersList.size() > 0) {
            view.setPostersList(postersList);
        } else {
            connector.getPostersList().subscribeWith(new Observer<ResponseMovieList>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(ResponseMovieList responseMovieList) {
                    for (int i = 0; i < postersCount; i++) {
                        if (responseMovieList.results.get(i).posterPath != null) {
                            String path = responseMovieList.results.get(i).posterPath;
                            String title = responseMovieList.results.get(i).title;
                            int id = responseMovieList.results.get(i).id;
                            postersList.add(new CarouselMoviePOJO(path, title, id));
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                    view.setPostersList(postersList);
                }
            });
        }
    }
}
