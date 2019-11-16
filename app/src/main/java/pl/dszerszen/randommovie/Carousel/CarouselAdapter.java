package pl.dszerszen.randommovie.Carousel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import pl.dszerszen.randommovie.R;

public class CarouselAdapter extends BaseAdapter {

    private AppCompatActivity activity;
    private ArrayList<String> data;

    public CarouselAdapter(AppCompatActivity context, ArrayList<String> data) {
        this.activity = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String BASE_URL = "https://image.tmdb.org/t/p/original/";
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.carousel_image, null, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.get().load(BASE_URL+data.get(position)).into(viewHolder.posterImage);

        return convertView;
    }

    public void updateList(ArrayList<String> postersUriList) {
        this.data = postersUriList;
    }

    private static class ViewHolder {
        private ImageView posterImage;

        public ViewHolder(View v) {
            posterImage = v.findViewById(R.id.carousel_image);
        }
    }
}
