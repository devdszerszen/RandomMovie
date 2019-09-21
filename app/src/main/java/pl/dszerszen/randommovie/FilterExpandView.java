package pl.dszerszen.randommovie;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class FilterExpandView extends LinearLayout {

    final String TAG = "RandomMovie_log";

    private boolean isExpanded = false;

    String title = "Title";

    LinearLayout header;
    TextView tv_title;
    ImageView chevron;

    AttachedView child;

    FilterData.FilterType filterType;

    public FilterExpandView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context,R.layout.filter_expand_view,this);
        header = findViewById(R.id.expand_header);
        tv_title = findViewById(R.id.expand_title);
        chevron = findViewById(R.id.expand_chevron);

        header.setOnClickListener(v -> {
            if (child != null) {
                if (isExpanded) {
                    hideContent();
                    isExpanded = false;
                }
                else {
                    showContent();
                    isExpanded = true;
                }
            } else {
                Log.d(TAG, "FilterExpandView: Child is null");
            }
        });
    }

    private void hideContent() {
        ((View)child).setVisibility(GONE);
        chevron.setRotation(0f);
        StringBuilder builder = new StringBuilder();
        builder.append(title).append(": ").append(child.getCurrentValue());
        tv_title.setText(builder);
    }

    private void showContent() {
        ((View)child).setVisibility(VISIBLE);
        chevron.setRotation(180f);
        tv_title.setText(title);
    }

    public void setParams(AttachedView child, FilterData.FilterType filterType) {
        this.child = child;
        switch (filterType) {
            case GENRE: {
                title = getResources().getString(R.string.filter_genre);
                break;
            } case YEAR: {
                title = getResources().getString(R.string.filter_year);
                break;
            } case VOTE: {
                title = getResources().getString(R.string.filter_votes);
                break;
            }
        }
        tv_title.setText(title);
        ((View)child).setVisibility(GONE);
    }

    public interface AttachedView {
        String getCurrentValue();
    }
}
