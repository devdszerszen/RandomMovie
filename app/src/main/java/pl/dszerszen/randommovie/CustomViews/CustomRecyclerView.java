package pl.dszerszen.randommovie.CustomViews;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import pl.dszerszen.randommovie.Filter.FilterData;
import pl.dszerszen.randommovie.Filter.GenreFilterAdapter;
import pl.dszerszen.randommovie.Filter.SingleFilter;

public class CustomRecyclerView extends RecyclerView implements FilterExpandView.AttachedView {
    final String TAG = "RandomMovie_log";

    public CustomRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public String getCurrentValue() {
        GenreFilterAdapter adapter = (GenreFilterAdapter)this.getAdapter();
        return adapter.getCurrentGenreName();
    }

    @Override
    public SingleFilter getFilter() {
        GenreFilterAdapter adapter = (GenreFilterAdapter)this.getAdapter();

        SingleFilter singleFilter = new SingleFilter();
        singleFilter.type = FilterData.FilterType.GENRE;
        singleFilter.genreId = adapter.getCurrentGenreId();
        singleFilter.genreName = adapter.getCurrentGenreName();
        singleFilter.genrePosition = adapter.getCurrentGenrePosition();
        return singleFilter;
    }
}
