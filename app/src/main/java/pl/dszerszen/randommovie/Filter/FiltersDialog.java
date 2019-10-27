package pl.dszerszen.randommovie.Filter;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import pl.dszerszen.randommovie.CustomViews.CustomRecyclerView;
import pl.dszerszen.randommovie.CustomViews.FilterExpandView;
import pl.dszerszen.randommovie.CustomViews.MinMaxView;
import pl.dszerszen.randommovie.Activity.StartActivity.StartActivityFilter;
import pl.dszerszen.randommovie.R;
import pl.dszerszen.randommovie.Network.ResponseGenre;

public class FiltersDialog extends Dialog {
    public final String TAG = "RandomMovie_log";

    StartActivityFilter activity;

    //Filter
    FilterData filterData;

    //Genres
    FilterExpandView genresHeader;
    CustomRecyclerView genresRecyclerView;
    List<ResponseGenre.Genre> genresList;
    GenreFilterAdapter genresAdapter;

    //Years
    FilterExpandView yearsHeader;
    MinMaxView yearsSelector;

    //Votes
    FilterExpandView votesHeader;
    MinMaxView votesSelector;



    public FiltersDialog(@NonNull Context context, List<ResponseGenre.Genre> list) {
        super(context);
        this.activity = (StartActivityFilter)context;
        this.filterData = FilterData.getInstance();
        this.setContentView(R.layout.dialog_filter_view);
        this.show();

        //Genres
        this.genresList = list;
        this.genresAdapter = new GenreFilterAdapter(this.genresList, this.filterData);
        genresRecyclerView = findViewById(R.id.dialog_genres_recycler);
        genresRecyclerView.setAdapter(genresAdapter);
        genresRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        genresRecyclerView.setHasFixedSize(true);
        genresHeader = findViewById(R.id.dialog_genres_header);
        genresHeader.setParams(genresRecyclerView,FilterData.FilterType.GENRE);
        genresHeader.setTitle();

        //Years
        yearsHeader = findViewById(R.id.dialog_years_header);
        yearsSelector = findViewById(R.id.dialog_years_selector);
        yearsHeader.setParams(yearsSelector,FilterData.FilterType.YEAR);
        yearsSelector.setupSelectorType(FilterData.FilterType.YEAR);
        if (isFilterSet(FilterData.FilterType.YEAR)) {
            setSeekbarValues(yearsSelector,FilterData.FilterType.YEAR);
        }

        //Votes
        votesHeader = findViewById(R.id.dialog_vote_header);
        votesSelector = findViewById(R.id.dialog_vote_selector);
        votesHeader.setParams(votesSelector,FilterData.FilterType.VOTE);
        votesSelector.setupSelectorType(FilterData.FilterType.VOTE);
        if (isFilterSet(FilterData.FilterType.VOTE)) {
            setSeekbarValues(votesSelector,FilterData.FilterType.VOTE);
        }

        //Buttons
        Button positiveButton = findViewById(R.id.dialog_positive_btn);
        positiveButton.setOnClickListener(v -> {
            saveFilters();
            activity.onFiltersSaved();
            this.dismiss();
        });

        Button negativeButton = findViewById(R.id.dialog_negative_btn);
        negativeButton.setOnClickListener(v ->{
            filterData.clearFilters();
            activity.onFiltersSaved();
            this.dismiss();
        });
        }

        public void saveFilters() {
            filterData.clearFilters();
            filterData.setFilter(genresHeader.getFilter(),genresHeader.isChecked());
            filterData.setFilter(yearsHeader.getFilter(),yearsHeader.isChecked());
            filterData.setFilter(votesHeader.getFilter(),votesHeader.isChecked());
        }

        private boolean isFilterSet(FilterData.FilterType type) {
            switch (type) {
                case YEAR: {
                    return filterData.minYear>-1 && filterData.maxYear>-1;
                } case GENRE: {
                    return filterData.genreId>-1;
                } case VOTE: {
                    return filterData.minVote>-1 && filterData.maxVote>-1;
                }
            }
            return false;
        }

        private void setSeekbarValues(MinMaxView view, FilterData.FilterType type) {
            switch (type) {
                case VOTE: {
                    view.setSeekBarValues(filterData.minVote,filterData.maxVote);
                    votesHeader.setTitle();
                    break;
                }
                case YEAR: {
                    view.setSeekBarValues(filterData.minYear,filterData.maxYear);
                    yearsHeader.setTitle();
                    break;
                }
            }
        }
}
