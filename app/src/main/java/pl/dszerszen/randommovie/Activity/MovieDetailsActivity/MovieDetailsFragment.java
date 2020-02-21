package pl.dszerszen.randommovie.Activity.MovieDetailsActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.dszerszen.randommovie.Network.SingleMovieDetails;
import pl.dszerszen.randommovie.R;


public class MovieDetailsFragment extends Fragment {
    final String TAG = "RandomMovie_log";
    final String NOT_AVAILABLE = "N/A";

    private OnFragmentInteractionListener mListener;
    private Unbinder unbinder;

    @BindView(R.id.df_title) TextView title;
    @BindView(R.id.df_desc) TextView desc;
    @BindView(R.id.df_poster) ImageView posterView;
    @BindView(R.id.df_layout) ConstraintLayout detailsLayout;
    @BindView(R.id.df_genres_layout) LinearLayout genresLayout;
    @BindView(R.id.df_time_value) TextView timeValue;
    @BindView(R.id.df_year_value) TextView yearValue;
    @BindView(R.id.df_rating_value) TextView rating;

    public MovieDetailsFragment() {
    }


    public static MovieDetailsFragment newInstance() {
        return new MovieDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        mListener.getFirstMovie();
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

    public void showMovieDetails(SingleMovieDetails movieDetails) {
        try {
        if (movieDetails != null) {

            //URL
            final String BASE_URL = "https://image.tmdb.org/t/p/original/";

            //Title
            title.setText(movieDetails.title);
            detailsLayout.bringChildToFront(title);

            //Genres
            genresLayout.removeAllViews();
            for (int i = 0; i < movieDetails.genres.size(); i++) {
                TextView tmpTxtView = new TextView((Context) mListener);
                tmpTxtView.setTextColor(Color.WHITE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                tmpTxtView.setLayoutParams(params);
                tmpTxtView.setTextSize(14f);
                tmpTxtView.setGravity(Gravity.CENTER);
                tmpTxtView.setText(movieDetails.genres.get(i).name.toLowerCase());
                genresLayout.addView(tmpTxtView);
            }

            //Year
            yearValue.setText(String.valueOf(movieDetails.releaseDate).substring(0, 4));

            //Time
            if (movieDetails.runtime > 0) {
                String timeWithMins = String.format(Locale.getDefault(), "%d min", movieDetails.runtime);
                timeValue.setText(timeWithMins);
            } else {
                timeValue.setText(NOT_AVAILABLE);
            }

            //Description
            desc.setText(movieDetails.overview);
            desc.scrollTo(0, 0);

            //Rating
            if (movieDetails.voteAverage == 0.0) {
                rating.setText(NOT_AVAILABLE);
            } else {
                rating.setText(String.valueOf(movieDetails.voteAverage));
            }

            //Picture - picasso
            if (movieDetails.backdropPath != null) {
                Picasso.get()
                        .load(BASE_URL + movieDetails.backdropPath)
                        .into(posterView, new Callback() {
                            @Override
                            public void onSuccess() {
                                mListener.notifyImageReady();
                            }

                            @Override
                            public void onError(Exception e) { mListener.notifyError(); }
                        });
            }
        }
        } catch (Exception e) {
            mListener.notifyError();
        }
    }

    public interface OnFragmentInteractionListener {
        void getRandomMovie();
        void getMovieDetails(int id);
        void getFirstMovie();
        void notifyImageReady();
        void notifyError();
    }
}
