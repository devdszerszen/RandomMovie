package pl.dszerszen.randommovie.Activity.MovieDetailsActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.dszerszen.randommovie.CustomViews.LoadingView;
import pl.dszerszen.randommovie.Error.ErrorType;
import pl.dszerszen.randommovie.Network.SingleMovieDetails;
import pl.dszerszen.randommovie.R;

import static android.view.View.GONE;


public class MovieDetailsFragment extends Fragment {
    final String TAG = "RandomMovie_log";
    final String NOT_AVAILABLE = "N/A";

    private OnFragmentInteractionListener mListener;
    private Unbinder unbinder;

    @BindView(R.id.errorLayout) ConstraintLayout errorLayout;
    @BindView(R.id.df_title) TextView title;
    @BindView(R.id.df_desc) TextView desc;
    @BindView(R.id.df_poster) ImageView posterView;
    @BindView(R.id.df_layout) ConstraintLayout detailsLayout;
    @BindView(R.id.df_genres_layout) LinearLayout genresLayout;
    @BindView(R.id.df_time_value) TextView timeValue;
    @BindView(R.id.df_year_value) TextView yearValue;
    @BindView(R.id.df_rating_value) TextView rating;
    @BindView(R.id.df_new_loader) LoadingView newLoader;
    @BindView(R.id.df_random_btn) Button randomButton;
    @BindView(R.id.df_previous_btn) Button previousButton;
    @BindView(R.id.df_tab_header) TabLayout tabHeader;
    @BindView(R.id.df_descriptionScrollView) ScrollView tabDescription;
    @BindView(R.id.df_detailsScrollView) ScrollView tabDetails;
    @BindView(R.id.df_error_desc) TextView errorDescription;

    ArrayList<Integer> previousMovies = new ArrayList<>();
    private boolean isPreviousMovie = false;
    boolean errorInfoShowed = false;

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
        startLoader();
        mListener.getFirstMovie();

//        tabHeader.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                setTabContent(tab);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
        return view;
    }

//    private void setTabContent(TabLayout.Tab tab) {
//        switch (tab.getPosition()) {
//            case 0: {
//                tabDetails.setVisibility(GONE);
//                tabDescription.setVisibility(View.VISIBLE);
//                break;
//            }
//            case 1: {
//                tabDescription.setVisibility(GONE);
//                tabDetails.setVisibility(View.VISIBLE);
//                break;
//            }
//        }
//    }

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
        if (errorInfoShowed) hideNetworkError();
        mListener.getRandomMovie();
        isPreviousMovie = true;
    }

    @OnClick(R.id.df_previous_btn)
    public void onPreviousButtonClicked() {
        if (errorInfoShowed) hideNetworkError();
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
            //tabDescription.scrollTo(0, 0);

            //Rating
            if (movieDetails.voteAverage == 0.0) {
                rating.setText(NOT_AVAILABLE);
            } else {
                rating.setText(String.valueOf(movieDetails.voteAverage));
            }

            //Previous movie button
            previousMovies.add(movieDetails.id);
            setPreviousMovieButton();

            //Picture - picasso
            if (movieDetails.backdropPath != null) {
                Picasso.get()
                        .load(BASE_URL + movieDetails.backdropPath)
                        .into(posterView, new Callback() {
                            @Override
                            public void onSuccess() {
                                stopLoader();
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
            }
        }
        } catch (Exception e) {
            Log.d(TAG, "Catched: " + e.getLocalizedMessage());
        }
    }

    public void startLoader() {
        detailsLayout.setVisibility(GONE);
        newLoader.showLoader();
    }

    public void stopLoader() {
        try {
            newLoader.hideLoader();
            detailsLayout.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.d(TAG,"Catched: " + e.getLocalizedMessage());
        }
    }

    public void showError(ErrorType errorType) {
        errorInfoShowed = true;
        newLoader.hideLoader();
        detailsLayout.setVisibility(GONE);
        errorLayout.setVisibility(View.VISIBLE);
        switch (errorType) {
            case NETWORK: errorDescription.setText(getString(R.string.error_msg_network));
            case LACK_OF_RESULT: errorDescription.setText(getString(R.string.error_msg_lack_of_results));
        }

    }

    private void hideNetworkError() {
        errorInfoShowed = false;
        detailsLayout.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(GONE);
    }

    public interface OnFragmentInteractionListener {
        void getRandomMovie();
        void getMovieDetails(int id);
        void getFirstMovie();
    }
}
