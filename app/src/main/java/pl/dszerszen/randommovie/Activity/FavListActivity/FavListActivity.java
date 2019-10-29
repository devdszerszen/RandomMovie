package pl.dszerszen.randommovie.Activity.FavListActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.dszerszen.randommovie.Firebase.FirebaseStoredMovie;
import pl.dszerszen.randommovie.R;

import android.os.Bundle;

import java.util.ArrayList;

public class FavListActivity extends AppCompatActivity implements FavInterface.View, FavAdapterInterface {

    RecyclerView favRecyclerView;
    FavInterface.Presenter presenter;
    FavListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_list);

        this.presenter = new FavPresenter(this);
        this.adapter = new FavListAdapter(this);

        favRecyclerView = findViewById(R.id.fav_recycler_view);
        favRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favRecyclerView.setAdapter(adapter);

        presenter.getList();

    }

    @Override
    public void showLoader() {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void showMoviesList(ArrayList<FirebaseStoredMovie> moviesList) {
        adapter.update(moviesList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteMovie(int id) {
        presenter.deleteMovie(id);
    }


}
