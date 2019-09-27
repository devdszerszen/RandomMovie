package pl.dszerszen.randommovie;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.Component;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static android.view.View.GONE;


public class MovieDetailsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Unbinder unbinder;

    //@BindView(R.id.loader) ProgressBar loader;
    //@BindView(R.id.randomButton) Button randomButton;
    //@BindView(R.id.previousButton) Button previousButton;
    @BindView(R.id.df_title) TextView title;
    @BindView(R.id.df_desc) TextView desc;
    @BindView(R.id.df_poster) ImageView posterView;
    @BindView(R.id.df_layout) ConstraintLayout detailsLayout;
    @BindView(R.id.df_genres_layout) LinearLayout genresLayout;
    @BindView(R.id.df_time_value) TextView timeValue;
    @BindView(R.id.df_year_value) TextView yearValue;
    @BindView(R.id.df_rating_value) TextView rating;
    @BindView(R.id.df_scrollView) ScrollView scrolledMovieDescView;
    @BindView(R.id.df_loader) ProgressBar loader;
    @BindView(R.id.df_random_btn) Button randomButton;
    @BindView(R.id.df_previous_btn) Button previousButton;

    ArrayList<Integer> previousMovies = new ArrayList<>();
    boolean isPreviousMovie = false;

    public MovieDetailsFragment() {
    }


    public static MovieDetailsFragment newInstance() {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        mListener.getRandomMovie();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.df_random_btn)
    public void onButtonClicked() {
        mListener.getRandomMovie();
        isPreviousMovie = true;
    }

    @OnClick(R.id.df_previous_btn)
    public void onPreviousButtonClicked() {
        isPreviousMovie = false;
        mListener.getMovieDetails(previousMovies.get(previousMovies.size()-2));
    }

    private void setPreviousMovieButton() {
        if (previousMovies.size()>1 && isPreviousMovie) {
            previousButton.setVisibility(View.VISIBLE);
        } else {
            previousButton.setVisibility(GONE);
        }
    }


    public void showMovieDetails(SingleMovieDetails movieDetails) {
        if (movieDetails != null) {

            //URL
            final String BASE_URL = "https://image.tmdb.org/t/p/original/";

            // Picture
            if (movieDetails.backdropPath != null) {
                Glide.with(this).load(BASE_URL + movieDetails.backdropPath).into(posterView);
            }

            //Title
            title.setText(movieDetails.title);
            detailsLayout.bringChildToFront(title);

            //Genres
            genresLayout.removeAllViews();
            for (int i = 0; i < movieDetails.genres.size(); i++) {
                TextView tmpTxtView = new TextView((Context)mListener);
                tmpTxtView.setTextColor(Color.WHITE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                tmpTxtView.setLayoutParams(params);
                tmpTxtView.setTextSize(14f);
                tmpTxtView.setGravity(Gravity.CENTER);
                tmpTxtView.setText(movieDetails.genres.get(i).name.toLowerCase());
                genresLayout.addView(tmpTxtView);
            }

            //Year
            yearValue.setText(String.valueOf(movieDetails.releaseDate).substring(0,4));

            //Time
            timeValue.setText(String.valueOf(movieDetails.runtime) + " min");

            //Description
            desc.setText(movieDetails.overview);
            desc.scrollTo(0, 0);
            scrolledMovieDescView.scrollTo(0, 0);

            //Rating
            rating.setText(String.valueOf(movieDetails.voteAverage));

            //Previous movie button
            previousMovies.add(movieDetails.id);
            setPreviousMovieButton();

            //Hide loader
            stopLoader();
        }
    }

    public void startLoader() {
        detailsLayout.setVisibility(GONE);
        loader.setVisibility(View.VISIBLE);
    }

    public void stopLoader() {
        loader.setVisibility(GONE);
        detailsLayout.setVisibility(View.VISIBLE);
    }

    public interface OnFragmentInteractionListener {
        void getRandomMovie();
        void getMovieDetails(int id);
    }
}
