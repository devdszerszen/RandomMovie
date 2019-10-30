package pl.dszerszen.randommovie.Activity.FavListActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.dszerszen.randommovie.Dagger.MyApplication;
import pl.dszerszen.randommovie.Firebase.FirebaseStoredMovie;
import pl.dszerszen.randommovie.R;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.ViewHolder> {

    public final String TAG = "RandomMovie_log";
    final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185/";

    Context context;

    ArrayList<FirebaseStoredMovie> moviesList = new ArrayList<>();
    FavAdapterInterface activity;
    int expandedPosition = -1;


    public FavListAdapter(FavAdapterInterface activity) {
        this.activity = activity;
        this.context = (Context) activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout row;
        public TextView title;
        public ImageButton deleteButton;
        public ConstraintLayout details;
        public TextView description;
        public TextView genre;
        public ImageView poster;
        public int id = 0;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            row = itemView.findViewById(R.id.fav_row);
            title = itemView.findViewById(R.id.fav_title);
            deleteButton = itemView.findViewById(R.id.fav_delete_btn);
            details = itemView.findViewById(R.id.fav_details);
            description = itemView.findViewById(R.id.fav_description);
            poster = itemView.findViewById(R.id.fav_poster);
            genre = itemView.findViewById(R.id.fav_genre);
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
        Log.d(TAG, "onBindViewHolder: called");
        FirebaseStoredMovie movie = moviesList.get(position);
        holder.title.setText(String.format("%s (%s)",movie.title, movie.year));
        holder.id = movie.id;
        holder.description.setText(movie.description);
        holder.genre.setText(movie.genre);

        if (movie.imgUrl != null) {
            Glide.with(context).load(POSTER_BASE_URL + movie.imgUrl).into(holder.poster);
        }


        final boolean isExpanded = position==expandedPosition;
        holder.details.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.row.setOnClickListener(v -> {
            notifyItemChanged(expandedPosition);
            expandedPosition = isExpanded ? -1 : position;
            Log.d(TAG, "onBindViewHolder: POSITION: " + position);
            Log.d(TAG, "onBindViewHolder: EXPANDED POSITION: " + expandedPosition);
            notifyItemChanged(expandedPosition);
        });

        holder.deleteButton.setOnClickListener(v -> {
            activity.deleteMovie(holder.id);
            expandedPosition = -1;
            moviesList.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
            notifyItemRangeChanged(position, getItemCount());
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void update(ArrayList<FirebaseStoredMovie> movies) {
        moviesList = movies;
    }


}
