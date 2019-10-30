package pl.dszerszen.randommovie.Activity.FavListActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.dszerszen.randommovie.Firebase.FirebaseStoredMovie;
import pl.dszerszen.randommovie.R;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class FavListActivity extends AppCompatActivity implements FavInterface.View, FavAdapterInterface {

    RecyclerView favRecyclerView;
    ProgressBar loader;
    FavInterface.Presenter presenter;
    FavListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_list);

        this.presenter = new FavPresenter(this);

        loader = findViewById(R.id.fav_loader);
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
        loader.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        loader.setVisibility(View.GONE);
        favRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMoviesList(ArrayList<FirebaseStoredMovie> moviesList) {
        hideLoader();
        adapter.update(moviesList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteMovie(int id) {
        presenter.deleteMovie(id);
    }


}
