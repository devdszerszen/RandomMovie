package pl.dszerszen.randommovie.Carousel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import pl.dszerszen.randommovie.Activity.MovieDetailsActivity.MovieDetailsActivity;
import pl.dszerszen.randommovie.R;

public class CarouselAdapter extends BaseAdapter {

    private AppCompatActivity activity;
    private ArrayList<CarouselMoviePOJO> data;

    public CarouselAdapter(AppCompatActivity context, ArrayList<CarouselMoviePOJO> data) {
        this.activity = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public CarouselMoviePOJO getItem(int position) {
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

        Picasso.get().load(BASE_URL+data.get(position).posterPath).into(viewHolder.posterImage);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MovieDetailsActivity.class);
                intent.putExtra("MOVIE_ID",data.get(position).id);
                activity.startActivity(intent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        private ImageView posterImage;

        public ViewHolder(View v) {
            posterImage = v.findViewById(R.id.carousel_image);
        }
    }
}
