package pl.dszerszen.randommovie;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pl.dszerszen.randommovie.GSON.Genre;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    final String TAG = "Damian";

    private List<Genre> list;

    //Radiobutton
    private int selectedItem = -1;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.row_text);
            radioButton = itemView.findViewById(R.id.row_radiobutton);

            radioButton.setOnClickListener(v -> {
                selectedItem = getAdapterPosition();
                Log.d(TAG, "ViewHolder: Selected category is: " + list.get(selectedItem).getName());
                notifyDataSetChanged();
            });
        }
    }

    public RecyclerAdapter(List<Genre> list) {
        this.list = list;
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
        holder.radioButton.setChecked(position == selectedItem);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getGenres() {
        if (selectedItem>-1) {
            return list.get(selectedItem).getId();
        }
        return 0;
    }
}
