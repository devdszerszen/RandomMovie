package pl.dszerszen.randommovie.Filter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import pl.dszerszen.randommovie.CustomViews.CustomRecyclerView;
import pl.dszerszen.randommovie.Network.ResponseGenre;
import pl.dszerszen.randommovie.R;

public class GenreFilterAdapter extends CustomRecyclerView.Adapter<GenreFilterAdapter.ViewHolder> {

    final String TAG = "RandomMovie_log";

    private List<ResponseGenre.Genre> list;

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

            itemView.setOnClickListener(v -> {
                saveSelectedItem(getAdapterPosition());
            });

            radioButton.setOnClickListener(v -> {
                saveSelectedItem(getAdapterPosition());
            });
        }
    }

    private void saveSelectedItem(int adapterPosition) {
        selectedCategoryPosition = adapterPosition;
        notifyDataSetChanged();
    }

    public GenreFilterAdapter(List<ResponseGenre.Genre> list, FilterData filter) {
        this.list = list;
        this.filterData = filter;

        if (filter.isSet()) {
            selectedCategoryPosition = filterData.getGenrePosition();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row,parent,false);

        return new GenreFilterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResponseGenre.Genre genre = list.get(position);

        holder.textView.setText(genre.name);
        holder.radioButton.setChecked(position == selectedCategoryPosition);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public FilterData getFilters() {
        return filterData;
    }

    public String getCurrentGenreName() {
        if (selectedCategoryPosition > -1) {
            return list.get(selectedCategoryPosition).name;
        } else {
            return null;
        }
    }

    public int getCurrentGenreId() {
        if (selectedCategoryPosition>-1) {
            return list.get(selectedCategoryPosition).id;
        } else {
            return -1;
        }
    }

    public int getCurrentGenrePosition() {
        return selectedCategoryPosition;
    }
}
