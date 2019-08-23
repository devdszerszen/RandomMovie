package pl.dszerszen.randommovie;

public class DetailsPresenter implements DetailsInterface.Presenter {

    DetailsInterface.View view;
    DetailsInterface.Model model;

    public DetailsPresenter(DetailsInterface.View view) {
        this.view = view;
        this.model = new ApiConnector(this);
    }

    @Override
    public void changeMovie() {
        model.getRandomMovie(ApiConnector.DETAILS);
    }

    @Override
    public void reportError(String message) {
        view.reportError(message);
    }

    @Override
    public void callbackRandomMovie(SingleMovieDetails movieDetails) {
        view.showNewMovie(movieDetails);
    }
}
