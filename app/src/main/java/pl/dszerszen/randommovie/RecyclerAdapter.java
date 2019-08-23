package pl.dszerszen.randommovie;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pl.dszerszen.randommovie.GSON.Genre;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    final String TAG = "DAMIAN";

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.row_text);
            checkBox = itemView.findViewById(R.id.row_checkbox);
        }
    }

    private List<Genre> list;
    private AdapterView.OnItemClickListener listener;


    public RecyclerAdapter(List<Genre> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row,parent,false);
        return new RecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        Genre genre = list.get(position);

        TextView textView = holder.textView;
        CheckBox checkBox = holder.checkBox;

        textView.setText(genre.getName());
        checkBox.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
