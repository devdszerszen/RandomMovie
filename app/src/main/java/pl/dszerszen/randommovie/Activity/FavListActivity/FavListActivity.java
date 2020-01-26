package pl.dszerszen.randommovie.Activity.FavListActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.dszerszen.randommovie.Activity.MovieDetailsActivity.MovieDetailsActivity;
import pl.dszerszen.randommovie.Base.BaseActivity;
import pl.dszerszen.randommovie.CustomViews.LoadingView;
import pl.dszerszen.randommovie.Firebase.FirebaseStoredMovie;
import pl.dszerszen.randommovie.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class FavListActivity extends BaseActivity implements FavInterface.View, FavAdapterInterface {

    RecyclerView favRecyclerView;
    LoadingView newLoader;
    ConstraintLayout noResultsLayout;
    FavInterface.Presenter presenter;
    FavListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.presenter = new FavPresenter(this);

        newLoader = findViewById(R.id.fav_new_loader);
        noResultsLayout = findViewById(R.id.fav_noresults);
        favRecyclerView = findViewById(R.id.fav_recycler_view);
        favRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.adapter = new FavListAdapter(this);
        favRecyclerView.setAdapter(adapter);
        showLoader();
        presenter.getList();

    }

    @Override
    public void showLoader() {
        favRecyclerView.setVisibility(View.GONE);
        newLoader.showLoader();
    }

    @Override
    public void hideLoader() {
        newLoader.hideLoader();
        favRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMoviesList(ArrayList<FirebaseStoredMovie> moviesList) {
        hideLoader();
        adapter.update(moviesList);
        adapter.notifyDataSetChanged();

        if (moviesList.size()==0) {
            showNoResultsMessage();
        }
    }

    @Override
    public void startDetailsActivity(int id) {
        Intent intent = new Intent(this,MovieDetailsActivity.class);
        intent.putExtra("MOVIE_ID",id);
        startActivity(intent);
    }

    @Override
    public void deleteMovie(int id) {
        presenter.deleteMovie(id);
    }

    @Override
    public void showNoResultsMessage() {
        favRecyclerView.setVisibility(View.GONE);
        noResultsLayout.setVisibility(View.VISIBLE);
    }
}
