package pl.dszerszen.randommovie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import pl.dszerszen.randommovie.GSON.Genre;

import static android.view.View.GONE;

public class StartActivity extends AppCompatActivity implements StartInterface.View {
    
    final String TAG = "DAMIAN";

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.loader) ProgressBar loader;
    @BindView(R.id.randomButton) Button randomButton;
    @BindView(R.id.start_movie_title) TextView title;
    @BindView(R.id.start_movie_desc) TextView desc;
    @BindView(R.id.start_movie_img) ImageView image;
    @BindView(R.id.start_movie_layout) ConstraintLayout movieLayout;
    ActionBar actionBar;

    private StartInterface.Presenter presenter;

    //Recycler view
    List<Genre> recyclerList;
    RecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        actionBar = getSupportActionBar();
        presenter = new StartPresenter(this);
        //initRecyclerView();
        //startLoader();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLoader();
    }

    public void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: called");
        recyclerList = new ArrayList<>();
        adapter = new RecyclerAdapter(recyclerList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void populateGenresList(List<Genre> genres) {
        Log.d(TAG, "populateGenresList: called");
        Log.d(TAG, "populateGenresList: send from model list size is: " + genres.size());
        StartActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new RecyclerAdapter(genres);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //stopLoader();
                Log.d(TAG, "run: updated recycler list size is: " + recyclerList.size());
            }
        });

    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRandomMovie(SingleMovieDetails movie) {
        //New activity
        Intent intent = new Intent(this,DetailsActivity.class);
        intent.putExtra("movie",movie);
        startActivity(intent);

        //Same activity
//        stopLoader();
//        populateMovieView(movie);
    }

    public void populateMovieView(SingleMovie movie) {
        title.setText(movie.title);
        desc.setText(movie.overview);
        if (movie.posterPath != null) {
            Log.d(TAG, "showMovieInfo: path is: "+ Api.getImageUrl()+movie.posterPath);
            Glide.with(this).load(Api.getImageUrl()+movie.posterPath).into(image);
        }
    }

    public void startLoader() {
        loader.setVisibility(View.VISIBLE);
        //movieLayout.setVisibility(GONE);
    }

    public void stopLoader() {
        loader.setVisibility(GONE);
        //movieLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.randomButton)
    public void getRandomMovie() {
        startLoader();
        presenter.getRandomMovie();
    }
}
