package pl.dszerszen.randommovie.Carousel;

import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import pl.dszerszen.randommovie.Activity.MovieDetailsActivity.MovieDetailsActivity;
import pl.dszerszen.randommovie.EventBus.CarouselReadyEvent;
import pl.dszerszen.randommovie.R;

public class CarouselAdapter extends BaseAdapter {

    final String TAG = "RandomMovie_log";

    private AppCompatActivity activity;
    private ArrayList<CarouselMoviePOJO> data;
    private FeatureCoverFlow view;
    private int loadedImagesCounter = 0;

    public CarouselAdapter(AppCompatActivity context, ArrayList<CarouselMoviePOJO> data, FeatureCoverFlow view) {
        this.activity = context;
        this.data = data;
        this.view = view;
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

        Picasso.get().load(BASE_URL+data.get(position).posterPath).into(viewHolder.posterImage, new Callback() {
            @Override
            public void onSuccess() {
                checkCarouselReady();
            }

            @Override
            public void onError(Exception e) {

            }
        });

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, MovieDetailsActivity.class);
            intent.putExtra("MOVIE_ID",data.get(position).id);
            activity.startActivity(intent);
        });

        return convertView;
    }

    private synchronized void checkCarouselReady() {
        loadedImagesCounter++;
        Log.d(TAG,"Check carousel, ready: " +  + loadedImagesCounter);
        if (loadedImagesCounter == data.size()) {
            EventBus.getDefault().post(new CarouselReadyEvent());
        }
    }

    private static class ViewHolder {
        private ImageView posterImage;

        public ViewHolder(View v) {
            posterImage = v.findViewById(R.id.carousel_image);
        }
    }
}
