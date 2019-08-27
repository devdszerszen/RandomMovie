package pl.dszerszen.randommovie;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.dszerszen.randommovie.GSON.Genre;

public class FiltersDialog extends Dialog {
    public final String TAG = "Damian";

    List<Genre> list;
    RecyclerAdapter adapter;
    StartActivityFilter activity;
    FilterData filterData;

    public FiltersDialog(@NonNull Context context, List<Genre> list, FilterData filter) {
        super(context);
        this.activity = (StartActivityFilter)context;
        this.filterData = filter;
        this.setContentView(R.layout.dialog_filter_view);
        this.show();
        this.list = list;
        this.adapter = new RecyclerAdapter(this.list, this.filterData);
        RecyclerView genresList = findViewById(R.id.dialog_recycler);
        genresList.setAdapter(adapter);
        genresList.setLayoutManager(new LinearLayoutManager(context));


        //
        Button positiveButton = findViewById(R.id.dialog_positive_btn);
        positiveButton.setOnClickListener(v -> {
            activity.onFiltersSaved(adapter.getFilters());
            this.dismiss();
        });

        Button negativeButton = findViewById(R.id.dialog_negative_btn);
        negativeButton.setOnClickListener(v ->{
            this.dismiss();
        });
    }
}
