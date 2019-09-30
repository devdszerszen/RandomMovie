package pl.dszerszen.randommovie.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import pl.dszerszen.randommovie.Filter.FilterData;
import pl.dszerszen.randommovie.Filter.SingleFilter;
import pl.dszerszen.randommovie.R;

public class FilterExpandView extends LinearLayout {

    final String TAG = "RandomMovie_log";

    private boolean isExpanded = false;

    String title = "Title";

    LinearLayout header;
    TextView tv_title;
    ImageView chevron;
    CheckBox checkBox;

    AttachedView child;

    FilterData.FilterType filterType;

    public FilterExpandView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context,R.layout.filter_expand_view,this);
        header = findViewById(R.id.expand_header);
        tv_title = findViewById(R.id.expand_title);
        chevron = findViewById(R.id.expand_chevron);
        checkBox = findViewById(R.id.expand_checkbox);
        checkBox.setChecked(true);

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
        setTitle();
    }

    public void setTitle() {
        String currentFilterValue = child.getCurrentValue();
        if (currentFilterValue!=null) {
            StringBuilder builder = new StringBuilder();
            builder.append(title).append(": ").append(currentFilterValue);
            tv_title.setText(builder);
            tv_title.setTextColor(getResources().getColor(R.color.colorPrimary,null));
            tv_title.setTypeface(null,Typeface.BOLD);
        }
    }

    private void showContent() {
        ((View)child).setVisibility(VISIBLE);
        chevron.setRotation(180f);
        tv_title.setText(title);
        tv_title.setTextColor(getResources().getColor(R.color.colorGray,null));
        tv_title.setTypeface(null,Typeface.NORMAL);
    }

    public boolean isChecked() {
        return checkBox.isChecked();
    }

    public void setParams(AttachedView child, FilterData.FilterType filterType) {
        this.child = child;
        this.filterType = filterType;
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
            } case RUNTIME: {
                title = getResources().getString(R.string.filter_runtime);
                break;
            }
        }
        tv_title.setText(title);
        ((View)child).setVisibility(GONE);
    }

    public SingleFilter getFilter() {
        return child.getFilter();
    }

    public interface AttachedView {
        String getCurrentValue();
        SingleFilter getFilter();
    }
}