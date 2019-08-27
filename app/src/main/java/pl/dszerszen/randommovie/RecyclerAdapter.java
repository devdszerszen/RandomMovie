package pl.dszerszen.randommovie;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pl.dszerszen.randommovie.GSON.Genre;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    final String TAG = "Damian";

    private List<Genre> list;

    private FilterData filterData;

    //Radiobutton
    private int selectedCategoryPosition = -1;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.row_text);
            radioButton = itemView.findViewById(R.id.row_radiobutton);

            radioButton.setOnClickListener(v -> {
                selectedCategoryPosition = getAdapterPosition();

                //Saving filter
                filterData.genrePosition = selectedCategoryPosition;
                filterData.genreId = list.get(selectedCategoryPosition).getId();
                notifyDataSetChanged();
            });
        }
    }

    public RecyclerAdapter(List<Genre> list, FilterData filter) {
        this.list = list;
        this.filterData = filter;

        if (filter.isSet()) {
            selectedCategoryPosition = filterData.genrePosition;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row,parent,false);

        return new RecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Genre genre = list.get(position);

        holder.textView.setText(genre.getName());
        holder.radioButton.setChecked(position == selectedCategoryPosition);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public FilterData getFilters() {
        return filterData;
    }
}
