package pl.dszerszen.randommovie.Activity.FavListActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import pl.dszerszen.randommovie.Firebase.FirebaseStoredMovie;
import pl.dszerszen.randommovie.R;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.ViewHolder> {

    public final String TAG = "RandomMovie_log";

    ArrayList<FirebaseStoredMovie> moviesList = new ArrayList<>();

    public FavListAdapter() {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout row;
        public TextView title;
        public ImageButton deleteButton;
        public int id = 0;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            row = itemView.findViewById(R.id.fav_row);
            title = itemView.findViewById(R.id.fav_title);
            deleteButton = itemView.findViewById(R.id.fav_delete_btn);

            row.setOnClickListener(v -> {
                Log.d(TAG, "ViewHolder: ROW CLICKED");
            });

            deleteButton.setOnClickListener(v -> {
                Log.d(TAG, "ViewHolder: DELETE CLICKED");
            });
        }
    }

    @NonNull
    @Override
    public FavListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_movie_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseStoredMovie movie = moviesList.get(position);
        holder.title.setText(movie.title);
        holder.id = movie.id;
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void update(ArrayList<FirebaseStoredMovie> movies) {
        moviesList = movies;
    }


}
