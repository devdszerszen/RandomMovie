package pl.dszerszen.randommovie;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import pl.dszerszen.randommovie.CustomViews.CustomRecyclerView;
import pl.dszerszen.randommovie.CustomViews.FilterExpandView;
import pl.dszerszen.randommovie.CustomViews.MinMaxView;
import pl.dszerszen.randommovie.Filter.FilterData;

public class FiltersDialog extends Dialog {
    public final String TAG = "RandomMovie_log";

    StartActivityFilter activity;

    //Filter
    FilterData filterData;

    //Genres
    FilterExpandView genresHeader;
    CustomRecyclerView genresRecyclerView;
    List<ResponseGenre.Genre> genresList;
    RecyclerAdapter genresAdapter;

    //Years
    FilterExpandView yearsHeader;
    MinMaxView yearsSelector;

    //Votes
    FilterExpandView votesHeader;
    MinMaxView votesSelector;

    //Runtime
    FilterExpandView runtimeHeader;
    MinMaxView runtimeSelector;


    public FiltersDialog(@NonNull Context context, List<ResponseGenre.Genre> list) {
        super(context);
        this.activity = (StartActivityFilter)context;
        this.filterData = FilterData.getInstance();
        this.setContentView(R.layout.dialog_filter_view);
        this.show();

        //Genres
        this.genresList = list;
        this.genresAdapter = new RecyclerAdapter(this.genresList, this.filterData);
        genresRecyclerView = findViewById(R.id.dialog_genres_recycler);
        genresRecyclerView.setAdapter(genresAdapter);
        genresRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        genresRecyclerView.setHasFixedSize(true);
        genresHeader = findViewById(R.id.dialog_genres_header);
        genresHeader.setParams(genresRecyclerView,FilterData.FilterType.GENRE);

        //Years
        yearsHeader = findViewById(R.id.dialog_years_header);
        yearsSelector = findViewById(R.id.dialog_years_selector);
        yearsHeader.setParams(yearsSelector,FilterData.FilterType.YEAR);
        yearsSelector.setupSelectorType(FilterData.FilterType.YEAR);

        //Votes
        votesHeader = findViewById(R.id.dialog_vote_header);
        votesSelector = findViewById(R.id.dialog_vote_selector);
        votesHeader.setParams(votesSelector,FilterData.FilterType.VOTE);
        votesSelector.setupSelectorType(FilterData.FilterType.VOTE);

        //Runtime
        runtimeHeader = findViewById(R.id.dialog_runtime_header);
        runtimeSelector = findViewById(R.id.dialog_runtime_selector);
        runtimeHeader.setParams(runtimeSelector,FilterData.FilterType.RUNTIME);
        runtimeSelector.setupSelectorType(FilterData.FilterType.RUNTIME);

        //Buttons
        Button positiveButton = findViewById(R.id.dialog_positive_btn);
        positiveButton.setOnClickListener(v -> {
            saveFilters();
            activity.onFiltersSaved();
            this.dismiss();
        });

        Button negativeButton = findViewById(R.id.dialog_negative_btn);
        negativeButton.setOnClickListener(v ->{
            this.dismiss();
        });
        }

        public void saveFilters() {
            filterData.setFilter(genresHeader.getFilter());
            filterData.setFilter(yearsHeader.getFilter());
            filterData.setFilter(votesHeader.getFilter());
            filterData.setFilter(runtimeHeader.getFilter());
        }
}
